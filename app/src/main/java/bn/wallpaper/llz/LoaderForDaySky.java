package bn.wallpaper.llz;

import android.content.res.Resources;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

// 白天天空加载类
public class LoaderForDaySky implements GLDrawable {
    private int mProgram;           // 着色器程序ID
    private int mMVPMatrixHandle;   // 总变换矩阵引用
    private int mVertexHandle;      // 顶点位置引用
    private int mTexCoorHandle;     // 纹理坐标引用
    private int mTimeHandle;        // 时间引用
    private int mEmptinessHandle;   // 天空参数1引用
    private int mSharpnessHandle;   // 天空参数2引用
    private int mSkyColor1Handle;   // 天空颜色1引用
    private int mSkyColor2Handle;   // 天空颜色2引用
    private int mCloudColor1Handle; // 云层颜色1引用
    private int mCloudColor2Handle; // 云层颜色2引用
    private int mTexture0Handle;    // 噪声纹理0引用
    private int mTexture1Handle;    // 噪声纹理1引用
    private int mTexture2Handle;    // 噪声纹理2引用
    private int mTexture3Handle;    // 噪声纹理3引用
    private int mTexture4Handle;    // 噪声纹理4引用

    private int noiseID0;   // 噪声纹理0ID
    private int noiseID1;   // 噪声纹理1ID
    private int noiseID2;   // 噪声纹理2ID
    private int noiseID3;   // 噪声纹理3ID
    private int noiseID4;   // 噪声纹理4ID

    private int vaoID; // VAO ID

    private int time; // 时间

    private float emptiness; // 天空参数1
    private float sharpness; // 天空参数2
    private float[] skyColor1 = new float[3]; // 天空颜色1
    private float[] skyColor2 = new float[3]; // 天空颜色2
    private float[] cloudColor1 = new float[3]; // 云层颜色1
    private float[] cloudColor2 = new float[3]; // 云层颜色2

    private int mCount;   // 顶点数目
    private float length; // 长度
    private float width;  // 宽度

    private float[] position;  // 物体的位置
    private float[] direction; // 物体的朝向

    public LoaderForDaySky(float length, float width, Resources r) {
        this.length = length;
        this.width = width;

        // 设置物体位置
        position = new float[]{0f, 0f, 0f};
        // 设置物体朝向
        direction = new float[]{0f, 0f, 1f};

        // 根据天气更新Sky
        updateSky();
        // 初始化着色器
        initShader(r);
        // 初始化VAO, VBO
        initVBAO();
    }

    // 设置纹理ID
    public void setNoiseIDs(int noiseID0, int noiseID1, int noiseID2, int noiseID3, int noiseID4) {
        this.noiseID0 = noiseID0;
        this.noiseID1 = noiseID1;
        this.noiseID2 = noiseID2;
        this.noiseID3 = noiseID3;
        this.noiseID4 = noiseID4;
    }

    // 根据天气更新Sky
    public void updateSky() {
        if (Constant.weather == 0) {
            skyColor1[0] = 170.0f / 255.0f;
            skyColor1[1] = 170.0f / 255.0f;
            skyColor1[2] = 170.0f / 255.0f;

            skyColor2[0] = 170.0f / 255.0f;
            skyColor2[1] = 170.0f / 255.0f;
            skyColor2[2] = 170.0f / 255.0f;

            emptiness = 0.1f;
            sharpness = 0.2f;

            cloudColor1[0] = 160.0f / 255.0f;
            cloudColor1[1] = 160.0f / 255.0f;
            cloudColor1[2] = 160.0f / 255.0f;

            cloudColor2[0] = 180.0f / 255.0f;
            cloudColor2[1] = 180.0f / 255.0f;
            cloudColor2[2] = 180.0f / 255.0f;
        } else if (Constant.weather == 1) {
            skyColor1[0] = 135.0f / 255.0f;
            skyColor1[1] = 220.0f / 255.0f;
            skyColor1[2] = 255.0f / 255.0f;

            skyColor2[0] = 190.0f / 255.0f;
            skyColor2[1] = 235.0f / 255.0f;
            skyColor2[2] = 255.0f / 255.0f;

            emptiness = 0.5f;
            sharpness = 0.6f;

            cloudColor1[0] = 198.0f / 255.0f;
            cloudColor1[1] = 198.0f / 255.0f;
            cloudColor1[2] = 198.0f / 255.0f;

            cloudColor2[0] = 255.0f / 255.0f;
            cloudColor2[1] = 255.0f / 255.0f;
            cloudColor2[2] = 255.0f / 255.0f;
        } else if (Constant.weather == 2) {
            skyColor1[0] = 50.0f / 255.0f;
            skyColor1[1] = 85.0f / 255.0f;
            skyColor1[2] = 90.0f / 255.0f;

            skyColor2[0] = 50.0f / 255.0f;
            skyColor2[1] = 85.0f / 255.0f;
            skyColor2[2] = 90.0f / 255.0f;

            emptiness = 0.8f;
            sharpness = 0.9f;

            cloudColor1[0] = 198.0f / 255.0f;
            cloudColor1[1] = 198.0f / 255.0f;
            cloudColor1[2] = 198.0f / 255.0f;

            cloudColor2[0] = 80.0f / 255.0f;
            cloudColor2[1] = 80.0f / 255.0f;
            cloudColor2[2] = 80.0f / 255.0f;
        } else if (Constant.weather == 3) {
            skyColor1[0] = 255.0f / 255.0f;
            skyColor1[1] = 255.0f / 255.0f;
            skyColor1[2] = 255.0f / 255.0f;

            skyColor2[0] = 255.0f / 255.0f;
            skyColor2[1] = 255.0f / 255.0f;
            skyColor2[2] = 255.0f / 255.0f;

            emptiness = 1.0f;
            sharpness = 1.0f;

            cloudColor1[0] = 198.0f / 255.0f;
            cloudColor1[1] = 198.0f / 255.0f;
            cloudColor1[2] = 198.0f / 255.0f;

            cloudColor2[0] = 255.0f / 255.0f;
            cloudColor2[1] = 255.0f / 255.0f;
            cloudColor2[2] = 255.0f / 255.0f;
        } else {
            skyColor1[0] = 51.0f / 255.0f;
            skyColor1[1] = 88.0f / 255.0f;
            skyColor1[2] = 105.0f / 255.0f;

            skyColor2[0] = 51.0f / 255.0f;
            skyColor2[1] = 88.0f / 255.0f;
            skyColor2[2] = 105.0f / 255.0f;

            emptiness = 1.0f;
            sharpness = 1.0f;

            cloudColor1[0] = 28.0f / 255.0f;
            cloudColor1[1] = 51.0f / 255.0f;
            cloudColor1[2] = 71.0f / 255.0f;

            cloudColor2[0] = 143.0f / 255.0f;
            cloudColor2[1] = 184.0f / 255.0f;
            cloudColor2[2] = 221.0f / 255.0f;
        }

    }

    // 初始化着色器
    private void initShader(Resources r) {
        // 加载顶点着色器的脚本内容
        String mVertexShader = UtilForShader.loadFromAssetsFile("sh/DayNightSky.vert", r);
        // 加载片元着色器的脚本内容
        String mFragmentShader = UtilForShader.loadFromAssetsFile("sh/DaySky.frag", r);
        // 基于顶点着色器与片元着色器创建程序
        mProgram = UtilForShader.createProgram(mVertexShader, mFragmentShader);

        // 获取程序中总变换矩阵引用
        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        // 获取程序中顶点位置引用
        mVertexHandle = GLES30.glGetAttribLocation(mProgram, "vVertex");
        // 获取程序中纹理坐标引用
        mTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "vTexCoor");

        // 获取程序中时间引用
        mTimeHandle = GLES30.glGetUniformLocation(mProgram, "uTime");
        // 获取天气颜色1引用
        mSkyColor1Handle = GLES30.glGetUniformLocation(mProgram, "uSkyColor1");
        // 获取天气颜色2引用
        mSkyColor2Handle = GLES30.glGetUniformLocation(mProgram, "uSkyColor2");
        // 获取云层颜色1引用
        mCloudColor1Handle = GLES30.glGetUniformLocation(mProgram, "uCloudColor1");
        // 获取云层颜色2引用
        mCloudColor2Handle = GLES30.glGetUniformLocation(mProgram, "uCloudColor2");
        // 获取天空参数1引用
        mEmptinessHandle = GLES30.glGetUniformLocation(mProgram, "uEmptiness");
        // 获取天空参数2引用
        mSharpnessHandle = GLES30.glGetUniformLocation(mProgram, "uSharpness");
        // 获取噪声纹理引用
        mTexture0Handle = GLES30.glGetUniformLocation(mProgram, "uTexture0");
        mTexture1Handle = GLES30.glGetUniformLocation(mProgram, "uTexture1");
        mTexture2Handle = GLES30.glGetUniformLocation(mProgram, "uTexture2");
        mTexture3Handle = GLES30.glGetUniformLocation(mProgram, "uTexture3");
        mTexture4Handle = GLES30.glGetUniformLocation(mProgram, "uTexture4");
    }

    // 初始化VBO, VAO
    private void initVBAO() {
        float[] vertex = new float[]{
                -length / 2, width / 2, 0,
                -length / 2, -width / 2, 0,
                length / 2, width / 2, 0,

                length / 2, width / 2, 0,
                -length / 2, -width / 2, 0,
                length / 2, -width / 2, 0,
        };
        mCount = vertex.length / 3;
        ByteBuffer bBV = ByteBuffer.allocateDirect(vertex.length * 4);
        bBV.order(ByteOrder.nativeOrder());
        FloatBuffer fBV = bBV.asFloatBuffer();
        fBV.put(vertex);
        fBV.rewind();

        float texCoor[] = new float[]
                {
                        0, 0,
                        0, 1,
                        1, 0,

                        1, 0,
                        0, 1,
                        1, 1
                };
        ByteBuffer bBT = ByteBuffer.allocateDirect(texCoor.length * 4);
        bBT.order(ByteOrder.nativeOrder());
        FloatBuffer fBT = bBT.asFloatBuffer();
        fBT.put(texCoor);
        fBT.rewind();

        int[] tempIDs = new int[2];

        GLES30.glGenVertexArrays(1, tempIDs, 0);
        vaoID = tempIDs[0];
        GLES30.glBindVertexArray(vaoID);

        GLES30.glGenBuffers(2, tempIDs, 0);
        int vertexID = tempIDs[0];
        int texCoorID = tempIDs[1];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexID);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER, vertex.length * 4,
                fBV, GLES30.GL_STATIC_DRAW
        );
        GLES30.glEnableVertexAttribArray(mVertexHandle);
        GLES30.glVertexAttribPointer(
                mVertexHandle, 3, GLES30.GL_FLOAT,
                false, 3 * 4, 0
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoorID);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER, texCoor.length * 4,
                fBT, GLES30.GL_STATIC_DRAW
        );
        GLES30.glEnableVertexAttribArray(mTexCoorHandle);
        GLES30.glVertexAttribPointer(
                mTexCoorHandle, 2, GLES30.GL_FLOAT,
                false, 2 * 4, 0
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
    }

    @Override
    public void drawSelf(int textureID, float[] selfMatrix, boolean drawShadow, float[] shadowColor) {
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(
                mMVPMatrixHandle, 1, false,
                ManagerForMatrix.getMVPMatrix(selfMatrix), 0
        );
        GLES30.glUniform1i(mTimeHandle, time);
        GLES30.glUniform1f(mEmptinessHandle, emptiness);
        GLES30.glUniform1f(mSharpnessHandle, sharpness);
        GLES30.glUniform3fv(mSkyColor1Handle, 1, skyColor1, 0);
        GLES30.glUniform3fv(mSkyColor2Handle, 1, skyColor2, 0);
        GLES30.glUniform3fv(mCloudColor1Handle, 1, cloudColor1, 0);
        GLES30.glUniform3fv(mCloudColor2Handle, 1, cloudColor2, 0);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, noiseID0);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, noiseID1);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE2);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, noiseID2);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE3);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, noiseID3);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE4);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, noiseID4);
        GLES30.glUniform1i(mTexture0Handle, 0);
        GLES30.glUniform1i(mTexture1Handle, 1);
        GLES30.glUniform1i(mTexture2Handle, 2);
        GLES30.glUniform1i(mTexture3Handle, 3);
        GLES30.glUniform1i(mTexture4Handle, 4);

        GLES30.glBindVertexArray(vaoID);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, mCount);
        GLES30.glBindVertexArray(0);

        if (Constant.timeUpdate) {
            if (time < Integer.MAX_VALUE) {
                time++;
            } else {
                time = 0;
            }
        }
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
