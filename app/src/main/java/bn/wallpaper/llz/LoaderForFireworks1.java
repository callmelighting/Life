package bn.wallpaper.llz;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.opengl.GLES30;

public class LoaderForFireworks1 implements GLDrawable {
    private float mTime;                // 现在时间
    private boolean drawable;           // 是否可绘制
    private float[] birthPlace;         // 出生地点数组

    private int mProgram;               // 渲染管线着色器程序ID
    private int mMVPMatrixHandle;       // 总变换矩阵引用
    private int mSizeHandle;            // 大小引用
    private int mCurrentTimeHandle;     // 现在时间引用
    private int mGravityHandle;         // 重力引用
    private int mPositionHandle;        // 位置应用
    private int mSpeedHandle;           // 速度引用
    private int mColorHeadHandle;       // 头部颜色值引用
    private int mColorTailHandle;       // 尾部颜色值引用
    private int mLiveTimeHandle;        // 生存时间引用

    private FloatBuffer mFBBirthPlace;   // 位置数据缓冲
    private FloatBuffer mFBSpeed;       // 速度数据缓冲
    private FloatBuffer mFBLiveTime;    // 生存时间数据缓冲
    private FloatBuffer mFBGravity;     // 重力数据缓冲
    private FloatBuffer mFBColorHead;   // 头部颜色值数据缓冲
    private FloatBuffer mFBColorTail;   // 尾部颜色值数据缓冲

    private float mLaunchRadius;        // 发射区域半径
    private int mCount;                 // 数目
    private float[] mColorHead;         // 头部颜色值
    private float[] mColorTail;         // 尾部颜色值
    private float mSize;                // 大小
    private float[] mLiveTime;          // 生存时间
    private float mSpeed;               // 速度
    private float[] mGravity;           // 重力

    private float[] position;           // 位置
    private float[] direction;          // 方向

    public LoaderForFireworks1(
            float launchRadius, int count, float size,
            float[] colorHead,
            float[] colorTail,
            float[] liveTime,
            float speed,
            float[] gravity,
            Resources r
    ) {
        mLaunchRadius = launchRadius;
        mCount = count;
        mSize = size;
        mColorHead = colorHead;
        mColorTail = colorTail;
        mLiveTime = liveTime;
        mSpeed = speed;
        mGravity = gravity;

        position = new float[]{0f, 0f, 0f};
        direction = new float[]{0f, 0f, 1f};

        initBirthPlaceData();
        initSpeedData();
        initLiveTimeData();
        initGravityData();
        initColorData();
        initShader(r);
    }

    // 初始化出生地点数据
    private void initBirthPlaceData() {
        birthPlace = new float[mCount * 3];

        for (int i = 0; i < mCount; ) {
            float tempX = (float) Math.random() * mLaunchRadius * 2 - mLaunchRadius;
            float tempY = (float) Math.random() * mLaunchRadius * 2 - mLaunchRadius;
            float tempZ = (float) Math.random() * mLaunchRadius * 2 - mLaunchRadius;

            if ((float) (Math.sqrt(tempX * tempX + tempY * tempY + tempZ * tempZ)) <= mLaunchRadius) {
                birthPlace[3 * i + 0] = tempX;
                birthPlace[3 * i + 1] = tempY;
                birthPlace[3 * i + 2] = tempZ;
                i++;
            }
        }

        ByteBuffer bBBirthPlace = ByteBuffer.allocateDirect(birthPlace.length * 4);
        bBBirthPlace.order(ByteOrder.nativeOrder());  // 设置字节顺序
        mFBBirthPlace = bBBirthPlace.asFloatBuffer(); // 转换为Float型缓冲
        mFBBirthPlace.put(birthPlace);                // 向缓冲区中放入数据
        mFBBirthPlace.rewind();                       // 设置缓冲区起始位置
    }

    // 初始化速度数据
    private void initSpeedData() {
        float[] speed = new float[mCount * 3];

        for (int i = 0; i < mCount; i++) {
            speed[3 * i + 0] = birthPlace[3 * i + 0] * mSpeed;
            speed[3 * i + 1] = birthPlace[3 * i + 1] * mSpeed;
            speed[3 * i + 2] = birthPlace[3 * i + 2] * mSpeed;
        }

        ByteBuffer bBSpeed = ByteBuffer.allocateDirect(speed.length * 4);
        bBSpeed.order(ByteOrder.nativeOrder());     // 设置字节顺序
        mFBSpeed = bBSpeed.asFloatBuffer();         // 转换为Float型缓冲
        mFBSpeed.put(speed);                        // 向缓冲区中放入数据
        mFBSpeed.rewind();                          // 设置缓冲区起始位置
    }

    // 初始化生存时间数据
    private void initLiveTimeData() {
        float[] liveTime = new float[mCount];

        for (int i = 0; i < mCount; i++) {
            liveTime[i] = (float) Math.random() * (mLiveTime[1] - mLiveTime[0]) + mLiveTime[0];
        }


        ByteBuffer bBLiveTime = ByteBuffer.allocateDirect(liveTime.length * 4);
        bBLiveTime.order(ByteOrder.nativeOrder());  // 设置字节顺序
        mFBLiveTime = bBLiveTime.asFloatBuffer();   // 转换为Float型缓冲
        mFBLiveTime.put(liveTime);                  // 向缓冲区中放入数据
        mFBLiveTime.rewind();                       // 设置缓冲区起始位置
    }

    // 初始化重力数据
    private void initGravityData() {
        ByteBuffer bBG = ByteBuffer.allocateDirect(mGravity.length * 4);
        bBG.order(ByteOrder.nativeOrder());         // 设置字节顺序
        mFBGravity = bBG.asFloatBuffer();           // 转换为Float型缓冲
        mFBGravity.put(mGravity);                   // 向缓冲区中放入数据
        mFBGravity.rewind();                        // 设置缓冲区起始位置
    }

    // 初始化颜色数据
    private void initColorData() {
        ByteBuffer bBCH = ByteBuffer.allocateDirect(mColorHead.length * 4);
        bBCH.order(ByteOrder.nativeOrder());        // 设置字节顺序
        mFBColorHead = bBCH.asFloatBuffer();        // 转换为Float型缓冲
        mFBColorHead.put(mColorHead);               // 向缓冲区中放入数据
        mFBColorHead.rewind();                      // 设置缓冲区起始位置

        ByteBuffer bBCT = ByteBuffer.allocateDirect(mColorTail.length * 4);
        bBCT.order(ByteOrder.nativeOrder());        // 设置字节顺序
        mFBColorTail = bBCT.asFloatBuffer();        // 转换为Float型缓冲
        mFBColorTail.put(mColorTail);               // 向缓冲区中放入数据
        mFBColorTail.rewind();                      // 设置缓冲区起始位置
    }

    // 初始化着色器
    private void initShader(Resources r) {
        // 加载顶点着色器的脚本内容
        String mVertexShader = UtilForShader.loadFromAssetsFile("sh/Particle.vert", r);
        // 加载片元着色器的脚本内容
        String mFragmentShader = UtilForShader.loadFromAssetsFile("sh/Particle.frag", r);
        // 基于顶点着色器与片元着色器创建程序
        mProgram = UtilForShader.createProgram(mVertexShader, mFragmentShader);
        // 获取程序中总变换矩阵引用
        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        // 获取程序中粒子大小引用
        mSizeHandle = GLES30.glGetUniformLocation(mProgram, "uSize");
        // 获取程序中现在时间引用
        mCurrentTimeHandle = GLES30.glGetUniformLocation(mProgram, "uCurrentTime");
        // 获取程序中重力引用
        mGravityHandle = GLES30.glGetUniformLocation(mProgram, "uGravity");
        // 获取程序中生存时间引用
        mLiveTimeHandle = GLES30.glGetAttribLocation(mProgram, "vLiveTime");
        // 获取程序中位置引用
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        // 获取程序中速度引用
        mSpeedHandle = GLES30.glGetAttribLocation(mProgram, "vSpeed");
        // 获取程序中头部颜色引用
        mColorHeadHandle = GLES30.glGetUniformLocation(mProgram, "uColorHead");
        // 获取程序中尾部颜色引用
        mColorTailHandle = GLES30.glGetUniformLocation(mProgram, "uColorTail");
    }

    @Override
    public void drawSelf(int textureID, float[] selfMatrix, boolean drawShadow, float[] shadowColor) {
        if (drawable) {
            // 指定使用某套着色器程序
            GLES30.glUseProgram(mProgram);
            // 将最终变换矩阵传入渲染管线
            GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, ManagerForMatrix.getMVPMatrix(selfMatrix), 0);
            // 将粒子大小传入渲染管线
            GLES30.glUniform1f(mSizeHandle, mSize);
            // 将现在时间传入渲染管线
            GLES30.glUniform1f(mCurrentTimeHandle, mTime);
            // 将重力传入渲染管线
            GLES30.glUniform3fv(mGravityHandle, 1, mFBGravity);
            // 将生存时间传入渲染管线
            GLES30.glVertexAttribPointer(mLiveTimeHandle, 1, GLES30.GL_FLOAT, false, 1 * 4, mFBLiveTime);
            // 启用生存时间数组
            GLES30.glEnableVertexAttribArray(mLiveTimeHandle);
            // 将位置传入渲染管线
            GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, mFBBirthPlace);
            // 启用位置数组
            GLES30.glEnableVertexAttribArray(mPositionHandle);
            // 将速度传入渲染管线
            GLES30.glVertexAttribPointer(mSpeedHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, mFBSpeed);
            // 启用速度数组
            GLES30.glEnableVertexAttribArray(mSpeedHandle);
            // 将头部颜色值传入渲染管线
            GLES30.glUniform4fv(mColorHeadHandle, 1, mFBColorHead);
            // 将尾部颜色值传入渲染管线
            GLES30.glUniform4fv(mColorTailHandle, 1, mFBColorTail);
            // 激活0号纹理
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            // 绑定纹理
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID);
            // 绘制
            GLES30.glDrawArrays(GLES30.GL_POINTS, 0, mCount);
            // 判断现在时间与最小生存时间的大小
            if (Constant.timeUpdate) {
                if (mTime >= mLiveTime[0] - 1) {
                    drawable = false;
                    mTime = 0;
                } else {
                    mTime++;
                }
            }
        }
    }

    // 刷新颜色值
    public void refreshColor() {
        mColorHead[0] = (float) Math.random();
        mColorHead[1] = (float) Math.random();
        mColorHead[2] = (float) Math.random();

        mColorTail[0] = (float) Math.random();
        mColorTail[1] = (float) Math.random();
        mColorTail[2] = (float) Math.random();

        initColorData();
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
