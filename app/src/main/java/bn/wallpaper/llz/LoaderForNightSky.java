package bn.wallpaper.llz;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.opengl.GLES30;


public class LoaderForNightSky implements GLDrawable {
    private int mProgram;               // 渲染管线着色器程序ID
    private int mMVPMatrixHandle;       // 总变换矩阵引用
    private int mPositionHandle;        // 顶点位置引用
    private int mTexCoorHandle;         // 纹理坐标引用

    private FloatBuffer mVertexBuffer;  // 顶点坐标数据缓冲
    private FloatBuffer mTexCoorBuffer; // 纹理坐标数据缓冲

    private int mCount;                 // 顶点数目
    private float length;               // 矩形长度
    private float width;                // 矩形宽度

    private float[] position;   // 物体的中心位置
    private float[] direction;  // 物体的方向

    public LoaderForNightSky(float length, float width, Resources r) {
        this.length = length;
        this.width = width;

        position = new float[]{0f, 0f, 0f};
        direction = new float[]{0f, 0f, 1f};

        initBuffer();
        initShader(r);
    }

    // 初始化数据缓冲
    private void initBuffer() {
        // 顶点坐标数据的初始化
        float[] vertices = new float[]{
                -length / 2, width / 2, 0,
                -length / 2, -width / 2, 0,
                length / 2, width / 2, 0,

                length / 2, width / 2, 0,
                -length / 2, -width / 2, 0,
                length / 2, -width / 2, 0,
        };
        mCount = vertices.length / 3;
        // 创建顶点坐标数据缓冲
        ByteBuffer bBVertex = ByteBuffer.allocateDirect(vertices.length * 4);
        bBVertex.order(ByteOrder.nativeOrder());    // 设置字节顺序
        mVertexBuffer = bBVertex.asFloatBuffer();   // 转换为Float型缓冲
        mVertexBuffer.put(vertices);                // 向缓冲区中放入数据
        mVertexBuffer.rewind();                     // 设置缓冲区起始位置

        // 纹理坐标数据的初始化
        float texCoor[] = new float[]
                {
                        0, 0,
                        0, 1,
                        1, 0,
                        1, 0,
                        0, 1,
                        1, 1
                };
        // 创建纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cbb.order(ByteOrder.nativeOrder());     // 设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();   // 转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);            // 向缓冲区中放入数据
        mTexCoorBuffer.rewind();                // 设置缓冲区起始位置
    }

    // 初始化着色器
    private void initShader(Resources r) {
        // 加载顶点着色器的脚本内容
        String mVertexShader = UtilForShader.loadFromAssetsFile("sh/DayNightSky.vert", r);
        // 加载片元着色器的脚本内容
        String mFragmentShader = UtilForShader.loadFromAssetsFile("sh/NightSky.frag", r);
        // 基于顶点着色器与片元着色器创建程序
        mProgram = UtilForShader.createProgram(mVertexShader, mFragmentShader);
        // 获取程序中顶点位置引用
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        // 获取程序中纹理坐标引用
        mTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "vTexCoor");
        // 获取程序中总变换矩阵引用
        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    @Override
    public void drawSelf(int textureID, float[] selfMatrix, boolean drawShadow, float[] shadowColor) {
        // 指定使用某套着色器程序
        GLES30.glUseProgram(mProgram);
        // 将最终变换矩阵送入渲染管线
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, ManagerForMatrix.getMVPMatrix(selfMatrix), 0);
        // 将顶点位置数据送入渲染管线
        GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        // 启用顶点位置数组
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        // 将纹理坐标数据送入渲染管线
        GLES30.glVertexAttribPointer(mTexCoorHandle, 2, GLES30.GL_FLOAT, false, 2 * 4, mTexCoorBuffer);
        // 启用纹理坐标数组
        GLES30.glEnableVertexAttribArray(mTexCoorHandle);
        // 激活0号纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        // 绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID);
        // 绘制
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, mCount);
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
