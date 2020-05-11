package bn.wallpaper.llz;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.opengl.GLES30;

// Object加载类
public class LoaderForObject implements GLDrawable {
    private int mProgram;                       // 渲染管线着色器程序ID
    private int mMVPMatrixHandle;               // 总变换矩阵引用
    private int mMMatrixHandle;                 // 具体变换矩阵引用
    private int mMPCMatrixHandle;               // 投影与观察组合矩阵引用
    private int mLight1Handle;                  // 环境光强度引用
    private int mLight2Handle;                  // 散射光强度引用
    private int mLight3Handle;                  // 镜面光强度引用
    private int mShadowColor;                   // 阴影颜色值引用
    private int mLightDirectionHandle;          // 光源方向引用
    private int mCameraLocationHandle;          // 摄像机位置引用
    private int mRoughnessHandle;               // 粗糙度引用
    private int mShadowHandle;                  // 影子绘制标志位引用
    private int mPositionHandle;                // 顶点位置引用
    private int mNormalVectorHandle;            // 法向量引用
    private int mTexCoorHandle;                 // 纹理坐标引用

    private FloatBuffer mVertexBuffer;          // 顶点坐标数据缓冲
    private FloatBuffer mNormalBuffer;          // 法向量数据缓冲
    private FloatBuffer mTexCoorBuffer;         // 纹理坐标数据缓冲

    private int mCount;                         // 顶点数目

    private AABB aabb;
    private float[] vertices;   // 物体的AABB包围盒
    private float[] minMaxXYZ;  // 物体的最大最小XYZ
    private float[] position;   // 物体的位置
    private float[] direction;  // 物体的方向

    public LoaderForObject(ReaderForObject rFO, Resources r) {
        direction = new float[]{0f, 0f, 1f};

        initBuffer(rFO.vXYZ, rFO.nXYZ, rFO.tST);
        initShader(r);
        this.vertices = rFO.vXYZ;
    }

    // 初始化数据缓冲
    private void initBuffer(float[] vertices, float[] normals, float texCoors[]) {
        mCount = vertices.length / 3;

        // 创建顶点坐标数据缓冲
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());     // 设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();    // 转换为Float型缓冲
        mVertexBuffer.put(vertices);            // 向缓冲区中放入数据
        mVertexBuffer.rewind();                 // 设置缓冲区起始位置

        // 创建法向量数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length * 4);
        cbb.order(ByteOrder.nativeOrder());     // 设置字节顺序
        mNormalBuffer = cbb.asFloatBuffer();    // 转换为Float型缓冲
        mNormalBuffer.put(normals);             // 向缓冲区中放入数据
        mNormalBuffer.rewind();                 // 设置缓冲区起始位置

        // 创建纹理坐标数据缓冲
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length * 4);
        tbb.order(ByteOrder.nativeOrder());     // 设置字节顺序
        mTexCoorBuffer = tbb.asFloatBuffer();   // 转换为Float型缓冲
        mTexCoorBuffer.put(texCoors);           // 向缓冲区中放入数据
        mTexCoorBuffer.rewind();                // 设置缓冲区起始位置
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
        // 获取程序中基本变换矩阵引用
        mMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");
        // 获取程序中投影与观察组合矩阵引用
        mMPCMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMPCMatrix");
        // 获取程序中环境光强度引用
        mLight1Handle = GLES30.glGetUniformLocation(mProgram, "uLight1");
        // 获取程序中散射光强度引用
        mLight2Handle = GLES30.glGetUniformLocation(mProgram, "uLight2");
        // 获取程序中镜面光强度引用
        mLight3Handle = GLES30.glGetUniformLocation(mProgram, "uLight3");
        // 获取程序中光源方向引用
        mLightDirectionHandle = GLES30.glGetUniformLocation(mProgram, "uLightDirection");
        // 获取程序中摄像机位置引用
        mCameraLocationHandle = GLES30.glGetUniformLocation(mProgram, "uCameraLocation");
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

    @Override
    public void drawSelf(int textureID, float[] selfMatrix, boolean drawShadow, float[] shadowColor) {
        // -------------------------绘制物体-------------------------
        //　指定使用某套着色器程序
        GLES30.glUseProgram(mProgram);
        // 将最终变换矩阵传入着色器程序
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false,
                ManagerForMatrix.getMVPMatrix(selfMatrix), 0);
        // 将位置、旋转变换矩阵传入着色器程序
        GLES30.glUniformMatrix4fv(mMMatrixHandle, 1, false, selfMatrix, 0);
        //  将环境光强度传入渲染管线
        GLES30.glUniform4fv(mLight1Handle, 1, ManagerForLight.getLight1(), 0);
        //  将散射光强度传入渲染管线
        GLES30.glUniform4fv(mLight2Handle, 1, ManagerForLight.getLight2(), 0);
        //  将镜面光强度传入渲染管线
        GLES30.glUniform4fv(mLight3Handle, 1, ManagerForLight.getLight3(), 0);
        // 将光源方向传入渲染管线
        GLES30.glUniform3fv(mLightDirectionHandle, 1, ManagerForLight.getLightDirection(), 0);
        // 将摄像机位置传入渲染管线
        GLES30.glUniform3fv(mCameraLocationHandle, 1, ManagerForCamera.getCameraLocation(),0);
        // 将粗糙度传入渲染管线
        GLES30.glUniform1f(mRoughnessHandle, 50f);
        // 将影子绘制标志位传入渲染管线
        GLES30.glUniform1f(mShadowHandle, 0.0f);
        // 将顶点位置传入渲染管线
        GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false,
                3 * 4, mVertexBuffer);
        // 启用顶点位置数组
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        // 将法向量传入渲染管线
        GLES30.glVertexAttribPointer(mNormalVectorHandle, 3, GLES30.GL_FLOAT, false,
                3 * 4, mNormalBuffer);
        // 启用法向量数组
        GLES30.glEnableVertexAttribArray(mNormalVectorHandle);
        // 将纹理坐标传入渲染管线
        GLES30.glVertexAttribPointer(mTexCoorHandle, 2, GLES30.GL_FLOAT,
                false, 2 * 4, mTexCoorBuffer);
        // 启用纹理坐标数组
        GLES30.glEnableVertexAttribArray(mTexCoorHandle);
        // 激活0号纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        // 绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID);
        // 绘制
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, mCount);
        // -------------------------绘制物体-------------------------

        if (drawShadow) {
            // -------------------------绘制影子-------------------------
            //　指定使用某套着色器程序
            GLES30.glUseProgram(mProgram);
            // 将基本变换变换矩阵传入渲染管线
            GLES30.glUniformMatrix4fv(mMMatrixHandle, 1, false, selfMatrix, 0);
            // 将投影与观察组合矩阵传入渲染管线
            GLES30.glUniformMatrix4fv(mMPCMatrixHandle, 1, false,
                    ManagerForMatrix.getMPCMatrix(), 0);
            // 将影子绘制标志位传入渲染管线
            GLES30.glUniform1f(mShadowHandle, 1.0f);
            // 将阴影颜色值传入渲染管线
            GLES30.glUniform4fv(mShadowColor, 1, shadowColor, 0);
            // 将光源方向传入渲染管线
            GLES30.glUniform3fv(mLightDirectionHandle, 1, ManagerForLight.getLightDirection(), 0);
            // 将环境光强度传入渲染管线
            GLES30.glUniform4fv(mLight1Handle, 1, ManagerForLight.getLight1(), 0);
            // 将顶点位置传入渲染管线
            GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false,
                    3 * 4, mVertexBuffer);
            // 启用顶点位置数组
            GLES30.glEnableVertexAttribArray(mPositionHandle);
            // 绘制
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, mCount);
            // -------------------------绘制影子-------------------------
        }
    }

    public float[] getMinMaxXYZ() {
        if (minMaxXYZ == null) {
            float minX = Float.POSITIVE_INFINITY;
            float minY = Float.POSITIVE_INFINITY;
            float minZ = Float.POSITIVE_INFINITY;

            float maxX = Float.NEGATIVE_INFINITY;
            float maxY = Float.NEGATIVE_INFINITY;
            float maxZ = Float.NEGATIVE_INFINITY;

            for (int i = 0; i < vertices.length / 3; i++) {
                float tempX = vertices[3 * i];
                float tempY = vertices[3 * i + 1];
                float tempZ = vertices[3 * i + 2];

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
