package bn.wallpaper.llz;

import android.content.res.Resources;
import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class LoaderForFireworks2 implements GLDrawable {

    // 基本粒子接口
    private interface Particle {
        float getSize();

        float getCR();

        float getCG();

        float getCB();

        float getCA();

        float getPX();

        float getPY();

        float getPZ();

        boolean isAlive();
    }

    // 烟花核心类
    private class FireworksCore implements Particle {
        public float size; // 尺寸
        public float life; // 寿命
        public float pX, pY, pZ; // 初始位置
        public float rS, gS, bS, aS; // 起始颜色
        public float rE, gE, bE, aE; // 终止颜色

        public int headNumber; // 头部粒子数目
        public ArrayList<FireworksHead> headList = new ArrayList<>(); // 头部粒子列表

        public FireworksCore(
                float size, // 尺寸
                float life, // 寿命
                float pX, float pY, float pZ, // 初始位置
                float rS, float gS, float bS, float aS, // 起始颜色
                float rE, float gE, float bE, float aE, // 终止颜色

                int headNumber // 头部粒子数目
        ) {
            this.size = size;
            this.life = life;
            this.pX = pX;
            this.pY = pY;
            this.pZ = pZ;
            this.rS = rS;
            this.gS = gS;
            this.bS = bS;
            this.aS = aS;
            this.rE = rE;
            this.gE = gE;
            this.bE = bE;
            this.aE = aE;

            this.headNumber = headNumber;
        }

        public void addHead(FireworksHead head) {
            headList.add(head);
        }

        @Override
        public float getSize() {
            return size + mClock;
        }

        @Override
        public float getCR() {
            return (1 - (mClock / life)) * rS + (mClock / life) * rE;
        }

        @Override
        public float getCG() {
            return (1 - (mClock / life)) * gS + (mClock / life) * gE;
        }

        @Override
        public float getCB() {
            return (1 - (mClock / life)) * bS + (mClock / life) * bE;
        }

        @Override
        public float getCA() {
            return (1 - (mClock / life)) * aS + (mClock / life) * aE;
        }

        @Override
        public float getPX() {
            return pX;
        }

        @Override
        public float getPY() {
            return pY;
        }

        @Override
        public float getPZ() {
            return pZ;
        }

        @Override
        public boolean isAlive() {
            return mClock < life;
        }
    }

    // 烟花头部类
    private class FireworksHead implements Particle {
        public float size; // 尺寸
        public float life; // 寿命
        public float pX, pY, pZ; // 初始位置
        public float vX, vY, vZ; // 速度
        public float aX, aY, aZ; // 加速度
        public float rS, gS, bS, aS; // 起始颜色
        public float rE, gE, bE, aE; // 终止颜色

        public FireworksCore core; // 核心粒子引用

        public int tailNumber; // 尾巴粒子数目
        public ArrayList<FireworksTail> tailList = new ArrayList<>(); // 尾巴粒子列表

        public FireworksHead(
                float size, // 尺寸
                float life, // 寿命
                float pX, float pY, float pZ, // 初始位置
                float vX, float vY, float vZ, // 速度
                float aX, float aY, float aZ, // 加速度
                float rS, float gS, float bS, float aS, // 起始颜色
                float rE, float gE, float bE, float aE, // 终止颜色

                FireworksCore core, // 核心粒子引用

                int tailNumber // 尾巴粒子数目
        ) {
            this.size = size;
            this.life = life;
            this.pX = pX;
            this.pY = pY;
            this.pZ = pZ;
            this.vX = vX;
            this.vY = vY;
            this.vZ = vZ;
            this.aX = aX;
            this.aY = aY;
            this.aZ = aZ;
            this.rS = rS;
            this.gS = gS;
            this.bS = bS;
            this.aS = aS;
            this.rE = rE;
            this.gE = gE;
            this.bE = bE;
            this.aE = aE;

            this.core = core;

            this.tailNumber = tailNumber;
        }

        public void addTail(FireworksTail tail) {
            tailList.add(tail);
        }

        @Override
        public float getSize() {
            return size;
        }

        @Override
        public float getCR() {
            return (1 - (mClock / life)) * rS + (mClock / life) * rE;
        }

        @Override
        public float getCG() {
            return (1 - (mClock / life)) * gS + (mClock / life) * gE;
        }

        @Override
        public float getCB() {
            return (1 - (mClock / life)) * bS + (mClock / life) * bE;
        }

        @Override
        public float getCA() {
            return (1 - (mClock / life)) * aS + (mClock / life) * aE;
        }

        @Override
        public float getPX() {
            return pX + vX * mClock + 0.5f * aX * mClock * mClock;
        }

        @Override
        public float getPY() {
            return pY + vY * mClock + 0.5f * aY * mClock * mClock;
        }

        @Override
        public float getPZ() {
            return pZ + vZ * mClock + 0.5f * aZ * mClock * mClock;
        }

        @Override
        public boolean isAlive() {
            return mClock < life;
        }
    }

    // 烟花尾部类
    private class FireworksTail implements Particle {
        public float size; // 尺寸
        public float life; // 寿命
        public float pX, pY, pZ; // 初始位置
        public float vX, vY, vZ; // 速度
        public float aX, aY, aZ; // 加速度
        public float rS, gS, bS, aS; // 起始颜色
        public float rE, gE, bE, aE; // 终止颜色

        public float shakeK; // 抖动系数

        public FireworksHead head; // 头部粒子引用

        public FireworksTail(
                float size, // 尺寸
                float life, // 寿命
                float pX, float pY, float pZ, // 初始位置
                float vX, float vY, float vZ, // 速度
                float aX, float aY, float aZ, // 加速度
                float rS, float gS, float bS, float aS, // 起始颜色
                float rE, float gE, float bE, float aE, // 终止颜色

                float shakeK, // 抖动系数

                FireworksHead head // 头部粒子引用
        ) {
            this.size = size;
            this.life = life;
            this.pX = pX;
            this.pY = pY;
            this.pZ = pZ;
            this.vX = vX;
            this.vY = vY;
            this.vZ = vZ;
            this.aX = aX;
            this.aY = aY;
            this.aZ = aZ;
            this.rS = rS;
            this.gS = gS;
            this.bS = bS;
            this.aS = aS;
            this.rE = rE;
            this.gE = gE;
            this.bE = bE;
            this.aE = aE;

            this.shakeK = shakeK;

            this.head = head;
        }

        @Override
        public float getSize() {
            return size;
        }

        @Override
        public float getCR() {
            return (1 - (mClock / life)) * rS + (mClock / life) * rE;
        }

        @Override
        public float getCG() {
            return (1 - (mClock / life)) * gS + (mClock / life) * gE;
        }

        @Override
        public float getCB() {
            return (1 - (mClock / life)) * bS + (mClock / life) * bE;
        }

        @Override
        public float getCA() {
            return (1 - (mClock / life)) * aS + (mClock / life) * aE;
        }

        @Override
        public float getPX() {
            return pX + vX * mClock + 0.5f * aX * mClock * mClock - shakeK * (((float) Math.random() * size) - size / 2);
        }

        @Override
        public float getPY() {
            return pY + vY * mClock + 0.5f * aY * mClock * mClock - shakeK * (((float) Math.random() * size) - size / 2);
        }

        @Override
        public float getPZ() {
            return pZ + vZ * mClock + 0.5f * aZ * mClock * mClock - shakeK * (((float) Math.random() * size) - size / 2);
        }

        @Override
        public boolean isAlive() {
            return mClock < life;
        }
    }

    ///////////////////////////////////
    private float mClock; // 时钟
    private boolean drawable; // 是否可绘制

    // 存活粒子列表
    private ArrayList<Particle> aliveParticleList = new ArrayList<>();
    // 全部粒子列表
    private ArrayList<Particle> totalParticleList = new ArrayList<>();

    private int mProgram; // 自定义渲染管线着色器程序ID
    private int mMVPMatrixHandle; // 总变换矩阵引用
    private int mSizeHandle; // 大小引用
    private int mColorHandle; // 颜色引用
    private int mPositionHandle; // 位置引用

    private FloatBuffer mFBSize; // 大小数据缓冲
    private FloatBuffer mFBColor; // 颜色数据缓冲
    private FloatBuffer mFBPosition; // 位置数据缓冲

    private float[] position; // 位置
    private float[] direction; // 方向
    ///////////////////////////////////

    // 构造函数
    public LoaderForFireworks2(Resources r) {
        position = new float[]{0f, 0f, 0f};
        direction = new float[]{0f, 0f, 1f};

        initData();
        initShader(r);
    }

    // 初始化数据
    private void initData() {
        // 创建烟花核心
        FireworksCore core = new FireworksCore(
                16f,
                250f,
                position[0], position[1], position[2],
                255f / 255f, 245f / 255f, 0f / 255f, 1.0f,
                255f / 255f, 65f / 255f, 0f / 255f, 0.0f,
                150
        );
        // 创建烟花头部
        float speedK = 0.006f;
        for (int i = 0; i < core.headNumber; i++) {
            boolean canUse = false;
            float tempX = 0f;
            float tempY = 0f;
            float tempZ = 0f;
            while (!canUse) {
                tempX = (float) Math.random() * 2 * speedK - speedK;
                tempY = (float) Math.random() * 2 * speedK - speedK;
                tempZ = (float) Math.random() * 2 * speedK - speedK;
                if (Matrix.length(tempX, tempY, tempZ) <= speedK) canUse = true;
            }
            core.addHead(new FireworksHead(
                    4f,
                    core.life / 8f * 7f,
                    core.pX, core.pY, core.pZ,
                    tempX, tempY, tempZ,
                    0f, -0.00003f, 0f,
                    255f / 255f, 255f / 255f, 255f / 255f, 1.0f,
                    255f / 255f, 245f / 255f, 0f / 255f, 0.0f,
                    core,
                    50
            ));
        }
        // 创建烟花尾部
        for (int i = 0; i < core.headNumber; i++) {
            FireworksHead tempHead = core.headList.get(i);
            for (int j = 0; j < tempHead.tailNumber; j++) {
                float tempRatio1 = (float) Math.pow(1 - j / (float) tempHead.tailNumber, 0.2f);
                float tempRatio2 = (float) Math.pow(tempRatio1, 6.0f);
                tempHead.addTail(new FireworksTail(
                        tempHead.size * tempRatio1,
                        tempHead.life,
                        tempHead.pX, tempHead.pY, tempHead.pZ,
                        tempHead.vX * tempRatio1,
                        tempHead.vY * tempRatio1,
                        tempHead.vZ * tempRatio1,
                        tempHead.aX * tempRatio2,
                        tempHead.aY * tempRatio2,
                        tempHead.aZ * tempRatio2,
                        255f / 255f * tempRatio1 + 255f / 255f * (1 - tempRatio1),
                        245f / 255f * tempRatio1 + 65f / 255f * (1 - tempRatio1),
                        0f / 255f * tempRatio1 + 0f / 255f * (1 - tempRatio1),
                        1.0f,
                        255f / 255f, 65f / 255f, 0f / 255f, 0.0f,
                        0.0005f,
                        tempHead
                ));
            }
        }
        // 创建基本粒子列表
        totalParticleList.clear();
        totalParticleList.add(core);
        for (int i = 0; i < core.headNumber; i++) {
            FireworksHead tempHead = core.headList.get(i);
            totalParticleList.add(tempHead);
            for (int j = 0; j < tempHead.tailNumber; j++) {
                totalParticleList.add(tempHead.tailList.get(j));
            }
        }
    }

    // 刷新 Buffer
    private void refreshBuffer() {
        // 清空存活粒子列表
        aliveParticleList.clear();
        // 将所有粒子列表中的存活粒子放入存活粒子列表中
        for (int i = 0; i < totalParticleList.size(); i++) {
            Particle tempParticle = totalParticleList.get(i);
            if (tempParticle.isAlive()) {
                aliveParticleList.add(tempParticle);
            }
        }
        // 如果没有存活粒子, 则重置时钟, 将 drawable 设置为假, 并返回
        if (aliveParticleList.size() == 0) {
            mClock = 0;
            drawable = false;
            return;
        }
        // 存放 大小 颜色 位置 的数组
        float[] sizeArray = new float[aliveParticleList.size()];
        float[] colorArray = new float[aliveParticleList.size() * 4];
        float[] positionArray = new float[aliveParticleList.size() * 3];
        // 将存活粒子的大小数据存入大小数组中
        for (int i = 0; i < sizeArray.length; i++) {
            sizeArray[i] = aliveParticleList.get(i).getSize();
        }
        // 将存活粒子的颜色数据存入颜色数组中
        for (int i = 0; i < colorArray.length / 4; i++) {
            Particle tempParticle = aliveParticleList.get(i);
            colorArray[4 * i + 0] = tempParticle.getCR();
            colorArray[4 * i + 1] = tempParticle.getCG();
            colorArray[4 * i + 2] = tempParticle.getCB();
            colorArray[4 * i + 3] = tempParticle.getCA();
        }
        // 将存活粒子的位置数据存入位置数组中
        for (int i = 0; i < positionArray.length / 3; i++) {
            Particle tempParticle = aliveParticleList.get(i);
            positionArray[3 * i + 0] = tempParticle.getPX();
            positionArray[3 * i + 1] = tempParticle.getPY();
            positionArray[3 * i + 2] = tempParticle.getPZ();
        }
        // 创建 Byte 型缓冲
        ByteBuffer mBBSize = ByteBuffer.allocateDirect(sizeArray.length * 4);
        ByteBuffer mBBColor = ByteBuffer.allocateDirect(colorArray.length * 4);
        ByteBuffer mBBPosition = ByteBuffer.allocateDirect(positionArray.length * 4);
        // 设置字节顺序
        mBBSize.order(ByteOrder.nativeOrder());
        mBBColor.order(ByteOrder.nativeOrder());
        mBBPosition.order(ByteOrder.nativeOrder());
        // 转换为 Float 型缓冲
        mFBSize = mBBSize.asFloatBuffer();
        mFBColor = mBBColor.asFloatBuffer();
        mFBPosition = mBBPosition.asFloatBuffer();
        // 向缓冲区中放入数据
        mFBSize.put(sizeArray);
        mFBColor.put(colorArray);
        mFBPosition.put(positionArray);
        // 设置缓冲区起始位置
        mFBSize.rewind();
        mFBColor.rewind();
        mFBPosition.rewind();
        // 时钟加1
        if (mClock < Float.MAX_VALUE) {
            mClock++;
        } else {
            mClock = 0.0f;
        }
    }

    // 初始化着色器
    private void initShader(Resources r) {
        // 加载顶点着色器的脚本内容
        String mVertexShader = UtilForShader.loadFromAssetsFile("sh/Fireworks2.vert", r);
        // 加载片元着色器的脚本内容
        String mFragmentShader = UtilForShader.loadFromAssetsFile("sh/Fireworks2.frag", r);
        // 基于顶点着色器与片元着色器创建程序
        mProgram = UtilForShader.createProgram(mVertexShader, mFragmentShader);
        // 获取程序中总变换矩阵引用
        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        // 获取程序中大小引用
        mSizeHandle = GLES30.glGetAttribLocation(mProgram, "vSize");
        // 获取程序中颜色引用
        mColorHandle = GLES30.glGetAttribLocation(mProgram, "vColor");
        // 获取程序中位置引用
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
    }

    // 绘制
    @Override
    public void drawSelf(int textureID, float[] selfMatrix, boolean drawShadow, float[] shadowColor) {
        if (Constant.timeUpdate) {
            // 如果可绘制
            if (drawable) {
                // 刷新 Buffer
                refreshBuffer();
            }
        }
        // 如果可绘制
        if (drawable) {
            // 指定使用某套着色器程序
            GLES30.glUseProgram(mProgram);
            // 将总变换矩阵传入渲染管线
            GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, ManagerForMatrix.getMVPMatrix(selfMatrix), 0);
            // 将大小数据传入渲染管线并启用
            GLES30.glVertexAttribPointer(mSizeHandle, 1, GLES30.GL_FLOAT, false, 1 * 4, mFBSize);
            GLES30.glEnableVertexAttribArray(mSizeHandle);
            // 将颜色数据传入渲染管线并启用
            GLES30.glVertexAttribPointer(mColorHandle, 4, GLES30.GL_FLOAT, false, 4 * 4, mFBColor);
            GLES30.glEnableVertexAttribArray(mColorHandle);
            // 将位置数据传入渲染管线并启用
            GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, mFBPosition);
            GLES30.glEnableVertexAttribArray(mPositionHandle);
            // 激活0号纹理并绑定纹理
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID);
            // 绘制
            GLES30.glDrawArrays(GLES30.GL_POINTS, 0, aliveParticleList.size());
        }
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }

    public boolean getDrawable() {
        return drawable;
    }

    @Override
    public AABB getAABB() {
        return null;
    }

    @Override
    public float[] getPosition() {
        return position;
    }

    @Override
    public void setDirection(float x, float y, float z) {
        direction[0] = x;
        direction[1] = y;
        direction[2] = z;
    }

    @Override
    public float[] getDirection() {
        return direction;
    }
}
