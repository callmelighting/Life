package bn.wallpaper.llz;

import android.opengl.Matrix;

// 存储系统矩阵状态的类
public class ManagerForMatrix {
    private static float[] MVPMatrix = new float[16];     // 总变换矩阵
    private static float[] MPCMatrix = new float[16];     // 投影与观察组合矩阵
    private static float[] MMatrix = new float[16];       // 当前变换矩阵
    private static float[] projMatrix = new float[16];    // 投影矩阵

    private static float[][] stack = new float[10][16];   // 保存矩阵的栈
    private static int stackTop = -1;                     // 栈顶索引

    // 产生单位矩阵
    public static float[] getIdentityM() {
        float[] identityM = new float[16];
        Matrix.setIdentityM(identityM, 0);
        return identityM;
    }

    // 初始化当前变换矩阵
    public static void initMatrix() {
        Matrix.setIdentityM(MMatrix, 0);
    }

    // 将当前变换矩阵存入栈中
    public static void pushMatrix() {
        // 栈顶索引加1
        stackTop++;
        // 当前变换矩阵中的各元素入栈
        System.arraycopy(MMatrix, 0, stack[stackTop], 0, 16);
    }

    // 从栈顶取出当前变换矩阵
    public static void popMatrix() {
        // 栈顶矩阵元素进当前变换矩阵
        System.arraycopy(stack[stackTop], 0, MMatrix, 0, 16);
        // 栈顶索引减1
        stackTop--;
    }

    // 沿X, Y, Z轴方向进行平移变换
    public static void translate(float x, float y, float z) {
        Matrix.translateM(MMatrix, 0, x, y, z);
    }

    // 沿X, Y, Z轴方向进行旋转变换
    public static void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(MMatrix, 0, angle, x, y, z);
    }

    // 沿X, Y, Z轴方向进行缩放变换
    public static void scale(float x, float y, float z) {
        Matrix.scaleM(MMatrix, 0, x, y, z);
    }

    // 设置透视投影参数
    public static void setProjectFrustum(
            float left, float right,
            float bottom, float top,
            float near, float far
    ) {
        Matrix.frustumM(projMatrix, 0, left, right, bottom, top, near, far);
    }

    // 设置正交投影参数
    public static void setProjectOrtho(
            float left, float right,
            float bottom, float top,
            float near, float far
    ) {
        Matrix.orthoM(projMatrix, 0, left, right, bottom, top, near, far);
    }

    // 获取当前变换矩阵
    public static float[] getMMatrix() {
        return MMatrix;
    }

    // 获取总变换矩阵
    public static float[] getMVPMatrix(float[] selfMatrix) {
        // 摄像机矩阵乘以变换矩阵
        Matrix.multiplyMM(MVPMatrix, 0, ManagerForCamera.getCameraMatrix(), 0, selfMatrix, 0);
        // 投影矩阵乘以上一步的结果矩阵
        Matrix.multiplyMM(MVPMatrix, 0, projMatrix, 0, MVPMatrix, 0);
        return MVPMatrix;
    }

    // 获取投影与观察组合矩阵
    public static float[] getMPCMatrix() {
        // 投影矩阵乘以摄像机矩阵
        Matrix.multiplyMM(MPCMatrix, 0, projMatrix, 0, ManagerForCamera.getCameraMatrix(), 0);
        return MPCMatrix;
    }

    // 计算AB点坐标
    public static float[] getABPosition(
            float pointX, float pointY,
            float screenWidth, float screenHeight,
            float right, float top,
            float near, float far
    ) {
        // 将以屏幕左上角为原点的触控点坐标换算为以屏幕中心为坐标原点的坐标
        float x0 = pointX - screenWidth / 2;
        float y0 = screenHeight / 2 - pointY;
        // 计算对应的near面上的x、y坐标
        float xNear = 2 * x0 * right / screenWidth;
        float yNear = 2 * y0 * top / screenHeight;
        // 计算对应的far面上的x、y坐标
        float ratio = far / near;   // 计算far与near的比值
        float xFar = ratio * xNear; // 计算对应的far面上的x坐标
        float yFar = ratio * yNear; // 计算对应的far面上的y坐标
        // 摄像机坐标系中A的坐标
        float ax = xNear;           // 摄像机坐标系中A点的x坐标
        float ay = yNear;           // 摄像机坐标系中A点的y坐标
        float az = -near;           // 摄像机坐标系中A点的z坐标
        // 摄像机坐标系中B的坐标
        float bx = xFar;            // 摄像机坐标系中B点的x坐标
        float by = yFar;            // 摄像机坐标系中B点的y坐标
        float bz = -far;            // 摄像机坐标系中B点的z坐标
        // 通过摄像机坐标系中A、B两点的坐标，求世界坐标系中A、B两点的坐标
        float[] A = fromPtoPreP(new float[]{ax, ay, az});   // 求世界坐标系中A点坐标
        float[] B = fromPtoPreP(new float[]{bx, by, bz});   // 求世界坐标系中B点坐标
        return new float[]{                                 // 返回AB两点在世界坐标系中的坐标
                A[0], A[1], A[2],
                B[0], B[1], B[2]
        };
    }

    // 将摄像机坐标系中的坐标变换为世界坐标系中的坐标
    public static float[] fromPtoPreP(float[] point) {
        // 通过逆变换, 得到变换之前的点
        float[] invertedCameraM = new float[16];
        // 求摄像机矩阵的逆矩阵
        Matrix.invertM(invertedCameraM, 0, ManagerForCamera.getCameraMatrix(), 0);
        // 用于存储结果坐标的数组
        float[] resultM = new float[4];
        // 求世界坐标系中的坐标
        Matrix.multiplyMV(resultM, 0, invertedCameraM, 0, new float[]{point[0], point[1], point[2], 1.0f}, 0);
        // 返回世界坐标系中的坐标
        return new float[]{resultM[0], resultM[1], resultM[2]};
    }

    // 将世界坐标系中的坐标变换为物体坐标系中的坐标
    public static float[] fromGToO(float[] point, float[] selfMatrix) {
        //通过逆变换, 得到变换之前的点
        float[] invertedSelfM = new float[16];
        // 求基本变换矩阵的逆矩阵
        Matrix.invertM(invertedSelfM, 0, selfMatrix, 0);
        // 用于存储结果坐标的数组
        float[] resultM = new float[4];
        // 求物体坐标系中的坐标
        Matrix.multiplyMV(resultM, 0, invertedSelfM, 0, new float[]{point[0], point[1], point[2], 1.0f}, 0);
        // 返回物体坐标系中的坐标
        return new float[]{resultM[0], resultM[1], resultM[2]};
    }
}
