package bn.wallpaper.llz;

import android.content.res.Resources;
import android.opengl.GLES30;

import com.bn.jar.bnggdh.Bnggdh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

// Bnggdh加载类
public class LoaderForBnggdh implements GLDrawable {
    private Bnggdh bnggdh;         // Bnggdh对象
    private float maxKeyTime;      // 动画时长
    private float nowTime;         // 当前时间
    private float speed;           // 动画速度
    private float stepTime;        // 步进长度
    private float intervalTime;    // 间隔时间
    private boolean playFlag;      // 是否播放

    private int texCoorID;         // 纹理坐标缓冲ID
    private int indexID;           // 顶点索引缓冲ID
    private int positionID;        // 顶点坐标缓冲ID
    private int normalID;          // 法向量缓冲ID

    private int mProgram;                       // 着色器程序ID
    private int mMVPMatrixHandle;               // 总变换矩阵引用
    private int mMatrixHandle;                  // 具体变换矩阵引用
    private int mMPCMatrixHandle;               // 投影与观察组合矩阵引用
    private int mLightDirectionHandle;          // 光源方向引用
    private int mCameraLocationHandle;          // 摄像机位置引用
    private int mLight1Handle;                  // 环境光强度引用
    private int mLight2Handle;                  // 散射光强度引用
    private int mLight3Handle;                  // 镜面光强度引用
    private int mShadowColor;                   // 阴影颜色值引用
    private int mPositionHandle;                // 顶点位置引用
    private int mNormalVectorHandle;            // 法向量引用
    private int mTexCoorHandle;                 // 纹理坐标引用
    private int mRoughnessHandle;               // 粗糙度引用
    private int mShadowHandle;                  // 影子绘制标志位引用

    private float mRoughness = 20;              // 粗糙度

    private AABB aabb;          // 物体的AABB包围盒
    private float[] minMaxXYZ;  // 物体的最大最小XYZ
    private float[] position;   // 物体的位置
    private float[] direction;  // 物体的朝向

    // 构造函数
    public LoaderForBnggdh(ReaderForBnggdh rFB, Resources r) {
        // 获取Bnggdh对象
        bnggdh = rFB.bnggdh;
        // 获取最大关键帧时间
        maxKeyTime = bnggdh.getMaxKeytime();
        // 将现在时间设置为负无穷
        nowTime = Float.NEGATIVE_INFINITY;
        // 设置速度
        setSpeed(0.1f);

        // 设置物体朝向
        direction = new float[]{0f, 0f, 1f};

        // 初始化着色器
        initShader(r);
        // 初始化VBO
        initVBO();
    }

    // 初始化着色器
    private void initShader(Resources r) {
        // 加载顶点着色器的脚本内容
        String mVertexShader = UtilForShader.loadFromAssetsFile("sh/Public.vert", r);
        // 加载片元着色器的脚本内容
        String mFragmentShader = UtilForShader.loadFromAssetsFile("sh/Public.frag", r);
        // 基于顶点着色器与片元着色器创建程序
        mProgram = UtilForShader.createProgram(mVertexShader, mFragmentShader);
        // 获取程序中总变换矩阵引用
        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        // 获取程序中具体变换矩阵引用
        mMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");
        // 获取程序中投影与观察组合矩阵引用
        mMPCMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMPCMatrix");
        // 获取程序中光源方向引用
        mLightDirectionHandle = GLES30.glGetUniformLocation(mProgram, "uLightDirection");
        // 获取程序中摄像机位置引用
        mCameraLocationHandle = GLES30.glGetUniformLocation(mProgram, "uCameraLocation");
        // 获取程序中环境光强度引用
        mLight1Handle = GLES30.glGetUniformLocation(mProgram, "uLight1");
        // 获取程序中散射光强度引用
        mLight2Handle = GLES30.glGetUniformLocation(mProgram, "uLight2");
        // 获取程序中镜面光强度引用
        mLight3Handle = GLES30.glGetUniformLocation(mProgram, "uLight3");
        // 获取程序中粗糙度引用
        mRoughnessHandle = GLES30.glGetUniformLocation(mProgram, "uRoughness");
        // 获取程序中顶点位置引用
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        // 获取程序中法向量引用
        mNormalVectorHandle = GLES30.glGetAttribLocation(mProgram, "vNormalVector");
        // 获取程序中纹理坐标引用
        mTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "vTexCoor");
        // 获取程序中影子绘制标志位引用
        mShadowHandle = GLES30.glGetUniformLocation(mProgram, "uShadow");
        // 获取程序中阴影颜色值引用
        mShadowColor = GLES30.glGetUniformLocation(mProgram, "uShadowColor");
    }

    // 初始化VBO
    private void initVBO() {
        // 创建纹理坐标数据缓冲
        ByteBuffer bBT = ByteBuffer.allocateDirect(bnggdh.getTextures().length * 4);
        bBT.order(ByteOrder.nativeOrder()); // 设置字节顺序
        FloatBuffer fBT = bBT.asFloatBuffer(); // 转换为Float型缓冲
        fBT.put(bnggdh.getTextures()); // 向缓冲区中放入数据
        fBT.rewind(); // 设置缓冲区起始位置

        // 创建顶点坐标索引数据缓冲
        ByteBuffer bBI = ByteBuffer.allocateDirect(bnggdh.getIndices().length * 2);
        bBI.order(ByteOrder.nativeOrder()); // 设置字节顺序
        ShortBuffer sBI = bBI.asShortBuffer(); // 转换为Short型缓冲
        sBI.put(bnggdh.getIndices()); // 向缓冲区中放入数据
        sBI.rewind(); // 设置缓冲区起始位置

        // 创建顶点坐标数据缓冲
        ByteBuffer bBP = ByteBuffer.allocateDirect(bnggdh.getPosition().length * 4);
        bBP.order(ByteOrder.nativeOrder()); // 设置字节顺序
        FloatBuffer fBP = bBP.asFloatBuffer(); // 转换为Float型缓冲
        fBP.put(bnggdh.getPosition()); // 向缓冲区中放入数据
        fBP.rewind(); // 设置缓冲区起始位置

        // 创建法向量坐标数据缓冲
        ByteBuffer bBN = ByteBuffer.allocateDirect(bnggdh.getCurrentNormal().length * 4);
        bBN.order(ByteOrder.nativeOrder()); // 设置字节顺序
        FloatBuffer fBN = bBN.asFloatBuffer(); // 转换为Float型缓冲
        fBN.put(bnggdh.getCurrentNormal()); // 向缓冲区中放入数据
        fBN.rewind(); // 设置缓冲区起始位置

        int[] tempIDs = new int[4];
        GLES30.glGenBuffers(4, tempIDs, 0);
        texCoorID = tempIDs[0];
        indexID = tempIDs[1];
        positionID = tempIDs[2];
        normalID = tempIDs[3];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoorID);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER, bnggdh.getTextures().length * 4,
                fBT, GLES30.GL_STATIC_DRAW
        );

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, indexID);
        GLES30.glBufferData(
                GLES30.GL_ELEMENT_ARRAY_BUFFER, bnggdh.getIndices().length * 2,
                sBI, GLES30.GL_STATIC_DRAW
        );
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, positionID);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER, bnggdh.getPosition().length * 4,
                fBP, GLES30.GL_DYNAMIC_DRAW
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalID);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER, bnggdh.getCurrentNormal().length * 4,
                fBN, GLES30.GL_DYNAMIC_DRAW
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }

    // 更新VBO
    private void updateVBO() {
        // 创建顶点坐标数据缓冲
        ByteBuffer bBP = ByteBuffer.allocateDirect(bnggdh.getPosition().length * 4);
        bBP.order(ByteOrder.nativeOrder()); // 设置字节顺序
        FloatBuffer fBP = bBP.asFloatBuffer(); // 转换为Float型缓冲
        fBP.put(bnggdh.getPosition()); // 向缓冲区中放入数据
        fBP.rewind(); // 设置缓冲区起始位置

        // 创建法向量坐标数据缓冲
        ByteBuffer bBN = ByteBuffer.allocateDirect(bnggdh.getCurrentNormal().length * 4);
        bBN.order(ByteOrder.nativeOrder()); // 设置字节顺序
        FloatBuffer fBN = bBN.asFloatBuffer(); // 转换为Float型缓冲
        fBN.put(bnggdh.getCurrentNormal()); // 向缓冲区中放入数据
        fBN.rewind(); // 设置缓冲区起始位置

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, positionID);
        GLES30.glBufferSubData(
                GLES30.GL_ARRAY_BUFFER, 0,
                bnggdh.getPosition().length * 4, fBP
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalID);
        GLES30.glBufferSubData(
                GLES30.GL_ARRAY_BUFFER, 0,
                bnggdh.getCurrentNormal().length * 4, fBN
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }

    // 更新动画时间
    private void updateTime() {
        nowTime += stepTime;
        if (nowTime > maxKeyTime + intervalTime || nowTime < 0) {
            nowTime = 0;
        }
        bnggdh.updata(nowTime);
    }

    // 设置动画速度
    public void setSpeed(float speed) {
        if (speed > 0f && speed < 1f) {
            this.speed = speed;
            stepTime = maxKeyTime * speed;
        }
    }

    // 获取动画速度
    public float getSpeed() {
        return speed;
    }

    // 设置动画间隔时间
    public void setIntervalTime(float intervalTime) {
        this.intervalTime = intervalTime;
    }

    // 获取动画间隔时间
    public float getIntervalTime() {
        return intervalTime;
    }

    // 获取最大时间
    public float getMaxKeyTime() {
        return maxKeyTime;
    }

    // 设置是否播放标志位
    public void setPlayFlag(boolean playFlag) {
        this.playFlag = playFlag;
    }

    // 获取是否播放标志位
    public boolean getPlayFlag() {
        return playFlag;
    }

    // 设置现在的时间
    public void setNowTime(float nowTime) {
        if (nowTime >= 0 && nowTime <= maxKeyTime + intervalTime) {
            this.nowTime = nowTime;
            bnggdh.updata(nowTime);
            updateVBO();
        }
    }

    // 获取现在的时间
    public float getNowTime() {
        return nowTime;
    }

    @Override
    public void drawSelf(int textureID, float[] selfMatrix, boolean drawShadow, float[] shadowColor) {
        // -------------------------绘制物体-------------------------
        // 指定使用某套着色器程序
        GLES30.glUseProgram(mProgram);
        // 将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, ManagerForMatrix.getMVPMatrix(selfMatrix), 0);
        // 将具体变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(mMatrixHandle, 1, false, selfMatrix, 0);
        // 将光源方向传入渲染管线
        GLES30.glUniform3fv(mLightDirectionHandle, 1, ManagerForLight.getLightDirection(), 0);
        // 将摄像机位置传入渲染管线
        GLES30.glUniform3fv(mCameraLocationHandle, 1, ManagerForCamera.getCameraLocation(), 0);
        // 将环境光强度传入渲染管线
        GLES30.glUniform4fv(mLight1Handle, 1, ManagerForLight.getLight1(), 0);
        // 将散射光强度传入渲染管线
        GLES30.glUniform4fv(mLight2Handle, 1, ManagerForLight.getLight2(), 0);
        // 将镜面光强度传入渲染管线
        GLES30.glUniform4fv(mLight3Handle, 1, ManagerForLight.getLight3(), 0);
        // 将粗糙度传入渲染管线
        GLES30.glUniform1f(mRoughnessHandle, mRoughness);
        // 将影子绘制标志位传入渲染管线
        GLES30.glUniform1f(mShadowHandle, 0.0f);

        // 将顶点位置传入渲染管线
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, positionID);
        GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);
        // 将法向量传入渲染管线
        GLES30.glEnableVertexAttribArray(mNormalVectorHandle);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalID);
        GLES30.glVertexAttribPointer(mNormalVectorHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);
        // 将纹理坐标传入渲染管线
        GLES30.glEnableVertexAttribArray(mTexCoorHandle);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoorID);
        GLES30.glVertexAttribPointer(mTexCoorHandle, 2, GLES30.GL_FLOAT, false, 2 * 4, 0);

        // 激活0号纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        // 绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID);

        // 绑定到索引缓冲
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, indexID);
        // 绘制
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, bnggdh.getIndices().length, GLES30.GL_UNSIGNED_SHORT, 0);
        // 绑定到系统默认缓冲
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
        // 绑定到系统默认纹理ID
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        // 绑定到系统默认缓冲
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        GLES30.glDisableVertexAttribArray(mPositionHandle);
        GLES30.glDisableVertexAttribArray(mNormalVectorHandle);
        GLES30.glDisableVertexAttribArray(mTexCoorHandle);
        // -------------------------绘制物体-------------------------

        if (drawShadow) {
            // -------------------------绘制影子-------------------------
            // 指定使用某套着色器程序
            GLES30.glUseProgram(mProgram);
            // 将基本变换矩阵传入渲染管线
            GLES30.glUniformMatrix4fv(mMatrixHandle, 1, false, selfMatrix, 0);
            // 将投影与观察组合矩阵传入渲染管线
            GLES30.glUniformMatrix4fv(mMPCMatrixHandle, 1, false, ManagerForMatrix.getMPCMatrix(), 0);
            // 将影子绘制标志位传入渲染管线
            GLES30.glUniform1f(mShadowHandle, 1.0f);
            // 将阴影颜色值传入渲染管线
            GLES30.glUniform4fv(mShadowColor, 1, shadowColor, 0);
            // 将光源方向传入渲染管线
            GLES30.glUniform3fv(mLightDirectionHandle, 1, ManagerForLight.getLightDirection(), 0);
            // 将环境光强度传入渲染管线
            GLES30.glUniform4fv(mLight1Handle, 1, ManagerForLight.getLight1(), 0);

            // 将顶点位置传入渲染管线
            GLES30.glEnableVertexAttribArray(mPositionHandle);
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, positionID);
            GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);

            // 绑定到索引缓冲
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, indexID);
            // 绘制
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, bnggdh.getIndices().length, GLES30.GL_UNSIGNED_SHORT, 0);
            // 绑定到系统默认缓冲
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);

            // 绑定到系统默认缓冲
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

            GLES30.glDisableVertexAttribArray(mPositionHandle);
            // -------------------------绘制影子-------------------------
        }

        if (Constant.timeUpdate) {
            // 如果动画仍在继续播放, 则更新时间和数据缓冲
            if (playFlag) {
                updateTime();
                updateVBO();
            }
        }
    }

    // 获取最大最小XYZ
    public float[] getMinMaxXYZ() {
        if (minMaxXYZ == null) {
            float minX = Float.POSITIVE_INFINITY;
            float minY = Float.POSITIVE_INFINITY;
            float minZ = Float.POSITIVE_INFINITY;

            float maxX = Float.NEGATIVE_INFINITY;
            float maxY = Float.NEGATIVE_INFINITY;
            float maxZ = Float.NEGATIVE_INFINITY;

            for (int i = 0; i < bnggdh.getPosition().length / 3; i++) {
                float tempX = bnggdh.getPosition()[3 * i];
                float tempY = bnggdh.getPosition()[3 * i + 1];
                float tempZ = bnggdh.getPosition()[3 * i + 2];

                if (minX > tempX) minX = tempX;
                if (minY > tempY) minY = tempY;
                if (minZ > tempZ) minZ = tempZ;

                if (maxX < tempX) maxX = tempX;
                if (maxY < tempY) maxY = tempY;
                if (maxZ < tempZ) maxZ = tempZ;
            }

            minMaxXYZ = new float[]{minX, maxX, minY, maxY, minZ, maxZ};
        }
        return minMaxXYZ;
    }

    @Override
    public AABB getAABB() {
        if (aabb == null) {
            if (minMaxXYZ == null) getMinMaxXYZ();
            aabb = new AABB(minMaxXYZ[0], minMaxXYZ[1], minMaxXYZ[2], minMaxXYZ[3], minMaxXYZ[4], minMaxXYZ[5]);
        }
        return aabb;
    }

    @Override
    public float[] getPosition() {
        if (position == null) {
            if (minMaxXYZ == null) getMinMaxXYZ();
            position = new float[]{
                    (minMaxXYZ[0] + minMaxXYZ[1]) / 2f,
                    (minMaxXYZ[2] + minMaxXYZ[3]) / 2f,
                    (minMaxXYZ[4] + minMaxXYZ[5]) / 2f
            };
        }
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
