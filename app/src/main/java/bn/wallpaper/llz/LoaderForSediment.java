package bn.wallpaper.llz;

import android.content.res.Resources;
import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

// 积水加载类
public class LoaderForSediment implements GLDrawable {
    private int mProgram; // 渲染管线着色器程序ID
    private int mMVPMatrixHandle; // 总变换矩阵引用
    private int mMPCMatrixHandle; // 投影与观察组合矩阵引用
    private int mMMatrixHandle; // 具体变换矩阵引用
    private int mVertexHandle; // 顶点位置引用
    private int mTexCoorHandle; // 纹理坐标引用
    private int mNormalHandle; // 法向量引用
    private int mMirrorTexHandle; // 水面反射纹理引用
    private int mWaterTexHandle; // 水面自身纹理引用

    private int mLightDirectionHandle; // 光源方向引用
    private int mCameraLocationHandle; // 摄像机位置引用
    private int mLight1Handle; // 环境光强度引用
    private int mLight2Handle; // 散射光强度引用
    private int mLight3Handle; // 镜面光强度引用
    private int mRoughnessHandle; // 粗糙度引用

    private int vertexBufferID; // 顶点坐标数据缓冲
    private int texCoorBufferID; // 纹理坐标数据缓冲
    private int normalBufferID; // 法向量数据缓冲

    private String[] index; // 顶点索引记录数组
    private float[] vertex; // 顶点坐标数组
    private float[] texCoor; // 纹理坐标数组
    private float[] normal; // 法向量坐标数组

    private FloatBuffer fBVertex; // 顶点坐标缓冲
    private FloatBuffer fBTexCoor; // 纹理坐标缓冲
    private FloatBuffer fBNormal; // 法向量缓冲

    private float time; // 时间

    private float width;
    private float length;
    private float spanW;
    private float spanL;

    private float[] wavePosition1; // 波源位置1
    private float[] wavePosition2; // 波源位置2
    private float[] wavePosition3; // 波源位置3
    private float[] wavePosition4; // 波源位置4

    private float waveTimeK1; // 波源时间系数1
    private float waveTimeK2; // 波源时间系数2
    private float waveTimeK3; // 波源时间系数3
    private float waveTimeK4; // 波源时间系数4

    private float waveDistanceK1; // 波源距离系数1
    private float waveDistanceK2; // 波源距离系数2
    private float waveDistanceK3; // 波源距离系数3
    private float waveDistanceK4; // 波源距离系数4

    private float waveAmplitude1; // 波源振幅1
    private float waveAmplitude2; // 波源振幅2
    private float waveAmplitude3; // 波源振幅3
    private float waveAmplitude4; // 波源振幅4

    private int mirrorTexID; // 水面反射纹理ID
    private float[] mpcMatrix; // 投影与观察组合矩阵

    private float[] position; // 物体的位置
    private float[] direction; // 物体的朝向

    // 构造函数
    public LoaderForSediment(
            float width, float length,
            float spanW, float spanL,
            Resources r
    ) {
        mpcMatrix = ManagerForMatrix.getIdentityM();
        position = new float[]{0f, 0f, 0f};
        direction = new float[]{0f, 0f, 1f};

        this.width = width;
        this.length = length;
        this.spanW = spanW;
        this.spanL = spanL;

        wavePosition1 = new float[]{-width / 2.0f, -length / 2.0f, 0.0f};
        wavePosition2 = new float[]{width / 2.0f, -length / 2.0f, 0.0f};
        wavePosition3 = new float[]{-width / 2.0f, length / 2.0f, 0.0f};
        wavePosition4 = new float[]{width / 2.0f, length / 2.0f, 0.0f};

        waveTimeK1 = (float) Math.PI * 0.01f;
        waveTimeK2 = (float) Math.PI * 0.02f;
        waveTimeK3 = (float) Math.PI * 0.03f;
        waveTimeK4 = (float) Math.PI * 0.04f;

        waveAmplitude1 = 0.001f * 3.0f;
        waveAmplitude2 = 0.002f * 3.0f;
        waveAmplitude3 = 0.003f * 3.0f;
        waveAmplitude4 = 0.004f * 3.0f;

        waveDistanceK1 = 100.0f * 0.5f;
        waveDistanceK2 = 200.0f * 0.5f;
        waveDistanceK3 = 300.0f * 0.5f;
        waveDistanceK4 = 400.0f * 0.5f;

        initVertexTexCoor();
        refreshVertex();
        refreshNormal();
        refreshBuffer();
        initShader(r);
        initVBO();
    }

    // 初始化顶点数据
    private void initVertexTexCoor() {
        ArrayList<Float> vertexList = new ArrayList<>();
        ArrayList<Float> texCoorList = new ArrayList<>();
        ArrayList<String> indexList = new ArrayList<>();
        float divW = width / spanW;
        float divL = length / spanL;
        for (float startW = 0; startW < width; startW += divW) {
            float i = startW / divW;
            for (float startL = 0; startL < length; startL += divL) {
                float j = startL / divL;

                vertexList.add(startW - width / 2.0f);
                vertexList.add(startL - length / 2.0f);
                vertexList.add(0.0f);
                texCoorList.add(0.0f + i);
                texCoorList.add(1.0f + j);
                indexList.add((int) i + "_" + (int) j);

                vertexList.add(startW + divW - width / 2.0f);
                vertexList.add(startL - length / 2.0f);
                vertexList.add(0.0f);
                texCoorList.add(1.0f + i);
                texCoorList.add(1.0f + j);
                indexList.add(((int) i + 1) + "_" + (int) j);

                vertexList.add(startW + divW - width / 2.0f);
                vertexList.add(startL + divL - length / 2.0f);
                vertexList.add(0.0f);
                texCoorList.add(1.0f + i);
                texCoorList.add(0.0f + j);
                indexList.add(((int) i + 1) + "_" + ((int) j + 1));

                vertexList.add(startW - width / 2.0f);
                vertexList.add(startL - length / 2.0f);
                vertexList.add(0.0f);
                texCoorList.add(0.0f + i);
                texCoorList.add(1.0f + j);
                indexList.add((int) i + "_" + (int) j);

                vertexList.add(startW + divW - width / 2.0f);
                vertexList.add(startL + divL - length / 2.0f);
                vertexList.add(0.0f);
                texCoorList.add(1.0f + i);
                texCoorList.add(0.0f + j);
                indexList.add(((int) i + 1) + "_" + ((int) j + 1));

                vertexList.add(startW - width / 2.0f);
                vertexList.add(startL + divL - length / 2.0f);
                vertexList.add(0.0f);
                texCoorList.add(0.0f + i);
                texCoorList.add(0.0f + j);
                indexList.add((int) i + "_" + ((int) j + 1));
            }
        }
        vertex = new float[vertexList.size()];
        for (int i = 0; i < vertexList.size(); i++) {
            vertex[i] = vertexList.get(i);
        }
        texCoor = new float[texCoorList.size()];
        for (int i = 0; i < texCoorList.size(); i++) {
            texCoor[i] = texCoorList.get(i);
        }
        index = new String[indexList.size()];
        for (int i = 0; i < indexList.size(); i++) {
            index[i] = indexList.get(i);
        }
    }

    // 刷新顶点数据
    private void refreshVertex() {
        for (int i = 0; i < vertex.length / 3; i++) {
            float waveDistance1 = Matrix.length(
                    wavePosition1[0] - vertex[3 * i],
                    wavePosition1[1] - vertex[3 * i + 1],
                    wavePosition1[2] - 0.0f
            );
            float waveDistance2 = Matrix.length(
                    wavePosition2[0] - vertex[3 * i],
                    wavePosition2[1] - vertex[3 * i + 1],
                    wavePosition2[2] - 0.0f
            );
            float waveDistance3 = Matrix.length(
                    wavePosition3[0] - vertex[3 * i],
                    wavePosition3[1] - vertex[3 * i + 1],
                    wavePosition3[2] - 0.0f
            );
            float waveDistance4 = Matrix.length(
                    wavePosition4[0] - vertex[3 * i],
                    wavePosition4[1] - vertex[3 * i + 1],
                    wavePosition4[2] - 0.0f
            );
            float offsetZ1 = (float) Math.sin(waveDistance1 * waveDistanceK1 + waveTimeK1 * time) * waveAmplitude1;
            float offsetZ2 = (float) Math.sin(waveDistance2 * waveDistanceK2 + waveTimeK2 * time) * waveAmplitude2;
            float offsetZ3 = (float) Math.sin(waveDistance3 * waveDistanceK3 + waveTimeK3 * time) * waveAmplitude3;
            float offsetZ4 = (float) Math.sin(waveDistance4 * waveDistanceK4 + waveTimeK4 * time) * waveAmplitude4;
            vertex[3 * i + 2] = offsetZ1 + offsetZ2 + offsetZ3 + offsetZ4;
        }
    }

    // 刷新法向量数据
    private void refreshNormal() {
        HashMap<String, ArrayList<Vector3f>> normalMap = new HashMap<>();
        for (int i = 0; i < vertex.length; i += 3 * 6) {
            float pX1 = vertex[i];
            float pY1 = vertex[i + 1];
            float pZ1 = vertex[i + 2];

            float pX2 = vertex[i + 3];
            float pY2 = vertex[i + 4];
            float pZ2 = vertex[i + 5];

            float pX3 = vertex[i + 6];
            float pY3 = vertex[i + 7];
            float pZ3 = vertex[i + 8];

            Vector3f vec12 = new Vector3f(pX2 - pX1, pY2 - pY1, pZ2 - pZ1);
            Vector3f vec13 = new Vector3f(pX3 - pX1, pY3 - pY1, pZ3 - pZ1);
            Vector3f nor123 = Vector3f.crossMultiply(vec12, vec13);

            for (String tempIndex : new String[]{
                    index[i / 3],
                    index[i / 3 + 1],
                    index[i / 3 + 2]
            }) {
                if (normalMap.containsKey(tempIndex)) {
                    normalMap.get(tempIndex).add(nor123);
                } else {
                    ArrayList<Vector3f> tempList = new ArrayList<>();
                    tempList.add(nor123);
                    normalMap.put(tempIndex, tempList);
                }
            }

            pX1 = vertex[i + 9];
            pY1 = vertex[i + 10];
            pZ1 = vertex[i + 11];

            pX2 = vertex[i + 12];
            pY2 = vertex[i + 13];
            pZ2 = vertex[i + 14];

            pX3 = vertex[i + 15];
            pY3 = vertex[i + 16];
            pZ3 = vertex[i + 17];

            vec12 = new Vector3f(pX2 - pX1, pY2 - pY1, pZ2 - pZ1);
            vec13 = new Vector3f(pX3 - pX1, pY3 - pY1, pZ3 - pZ1);
            nor123 = Vector3f.crossMultiply(vec12, vec13);

            for (String tempIndex : new String[]{
                    index[i / 3 + 3],
                    index[i / 3 + 4],
                    index[i / 3 + 5]
            }) {
                if (normalMap.containsKey(tempIndex)) {
                    normalMap.get(tempIndex).add(nor123);
                } else {
                    ArrayList<Vector3f> tempList = new ArrayList<>();
                    tempList.add(nor123);
                    normalMap.put(tempIndex, tempList);
                }
            }
        }

        HashMap<String, Vector3f> averageNormalMap = new HashMap<>();
        for (String tempIndex : normalMap.keySet()) {
            averageNormalMap.put(tempIndex, Vector3f.getAverage(normalMap.get(tempIndex)));
        }

        normal = new float[vertex.length];
        for (int i = 0; i < vertex.length / 3; i++) {
            Vector3f tempNormalVec = averageNormalMap.get(index[i]);
            normal[3 * i] = tempNormalVec.x;
            normal[3 * i + 1] = tempNormalVec.y;
            normal[3 * i + 2] = tempNormalVec.z;
        }
    }

    // 刷新Buffer
    private void refreshBuffer() {
        ByteBuffer bBV = ByteBuffer.allocateDirect(vertex.length * 4);
        bBV.order(ByteOrder.nativeOrder());
        fBVertex = bBV.asFloatBuffer();
        fBVertex.put(vertex);
        fBVertex.rewind();

        ByteBuffer bBT = ByteBuffer.allocateDirect(texCoor.length * 4);
        bBT.order(ByteOrder.nativeOrder());
        fBTexCoor = bBT.asFloatBuffer();
        fBTexCoor.put(texCoor);
        fBTexCoor.rewind();

        ByteBuffer bBN = ByteBuffer.allocateDirect(normal.length * 4);
        bBN.order(ByteOrder.nativeOrder());
        fBNormal = bBN.asFloatBuffer();
        fBNormal.put(normal);
        fBNormal.rewind();
    }

    // 初始化着色器
    private void initShader(Resources r) {
        String mVertexShader = UtilForShader.loadFromAssetsFile("sh/Sediment.vert", r);
        String mFragmentShader = UtilForShader.loadFromAssetsFile("sh/Sediment.frag", r);
        mProgram = UtilForShader.createProgram(mVertexShader, mFragmentShader);

        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        mMPCMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMPCMatrix");
        mMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");

        mVertexHandle = GLES30.glGetAttribLocation(mProgram, "vVertex");
        mTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "vTexCoor");
        mNormalHandle = GLES30.glGetAttribLocation(mProgram, "vNormal");

        mMirrorTexHandle = GLES30.glGetUniformLocation(mProgram, "uMirrorTex");
        mWaterTexHandle = GLES30.glGetUniformLocation(mProgram, "uWaterTex");

        mLightDirectionHandle = GLES30.glGetUniformLocation(mProgram, "uLightDirection");
        mCameraLocationHandle = GLES30.glGetUniformLocation(mProgram, "uCameraLocation");
        mLight1Handle = GLES30.glGetUniformLocation(mProgram, "uLight1");
        mLight2Handle = GLES30.glGetUniformLocation(mProgram, "uLight2");
        mLight3Handle = GLES30.glGetUniformLocation(mProgram, "uLight3");
        mRoughnessHandle = GLES30.glGetUniformLocation(mProgram, "uRoughness");
    }

    // 初始化VBO
    private void initVBO() {
        int[] tempIDs = new int[3];
        GLES30.glGenBuffers(3, tempIDs, 0);
        vertexBufferID = tempIDs[0];
        texCoorBufferID = tempIDs[1];
        normalBufferID = tempIDs[2];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferID);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER, vertex.length * 4,
                fBVertex, GLES30.GL_DYNAMIC_DRAW
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoorBufferID);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER, texCoor.length * 4,
                fBTexCoor, GLES30.GL_DYNAMIC_DRAW
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalBufferID);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER, normal.length * 4,
                fBNormal, GLES30.GL_DYNAMIC_DRAW
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }

    // 刷新VBO
    private void refreshVBO() {
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferID);
        GLES30.glBufferSubData(
                GLES30.GL_ARRAY_BUFFER, 0,
                vertex.length * 4, fBVertex
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoorBufferID);
        GLES30.glBufferSubData(
                GLES30.GL_ARRAY_BUFFER, 0,
                texCoor.length * 4, fBTexCoor
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalBufferID);
        GLES30.glBufferSubData(
                GLES30.GL_ARRAY_BUFFER, 0,
                normal.length * 4, fBNormal
        );

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void drawSelf(int textureID, float[] selfMatrix, boolean drawShadow, float[] shadowColor) {
        refreshVertex();
        refreshNormal();
        refreshBuffer();
        refreshVBO();

        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, ManagerForMatrix.getMVPMatrix(selfMatrix), 0);
        GLES30.glUniformMatrix4fv(mMPCMatrixHandle, 1, false, mpcMatrix, 0);
        GLES30.glUniformMatrix4fv(mMMatrixHandle, 1, false, selfMatrix, 0);

        GLES30.glUniform3fv(mLightDirectionHandle, 1, ManagerForLight.getLightDirection(), 0);
        GLES30.glUniform3fv(mCameraLocationHandle, 1, ManagerForCamera.getCameraLocation(), 0);
        GLES30.glUniform4fv(mLight1Handle, 1, ManagerForLight.getLight1(), 0);
        GLES30.glUniform4fv(mLight2Handle, 1, ManagerForLight.getLight2(), 0);
        GLES30.glUniform4fv(mLight3Handle, 1, ManagerForLight.getLight3(), 0);
        GLES30.glUniform1f(mRoughnessHandle, 8.0f);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mirrorTexID);
        GLES30.glUniform1i(mMirrorTexHandle, 0);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID);
        GLES30.glUniform1i(mWaterTexHandle, 1);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferID);
        GLES30.glEnableVertexAttribArray(mVertexHandle);
        GLES30.glVertexAttribPointer(mVertexHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoorBufferID);
        GLES30.glEnableVertexAttribArray(mTexCoorHandle);
        GLES30.glVertexAttribPointer(mTexCoorHandle, 2, GLES30.GL_FLOAT, false, 2 * 4, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalBufferID);
        GLES30.glEnableVertexAttribArray(mNormalHandle);
        GLES30.glVertexAttribPointer(mNormalHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertex.length / 3);

        GLES30.glDisableVertexAttribArray(mVertexHandle);
        GLES30.glDisableVertexAttribArray(mTexCoorHandle);
        GLES30.glDisableVertexAttribArray(mNormalHandle);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        if (time < Float.MAX_VALUE) {
            time++;
        } else {
            time = 0.0f;
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

    public void setMirrorTexID(int mirrorTexID) {
        this.mirrorTexID = mirrorTexID;
    }

    public void setMPCMatrix(float[] mpcMatrix) {
        System.arraycopy(mpcMatrix, 0, this.mpcMatrix, 0, mpcMatrix.length);
    }
}
