package bn.wallpaper.llz;

import android.opengl.Matrix;

public class ManagerForCamera {
    private static float[] camera9P = new float[9];       // 摄像机9参数记录数组
    private static float[] cameraRotateM;                 // 摄像机绝对旋转矩阵
    private static float[] cameraSurroundM;               // 摄像机绝对环绕矩阵
    private static float[] cameraLocation = new float[3]; // 摄像机位置数组
    private static float[] cameraMatrix = new float[16];  // 摄像机位置矩阵

    // 旋转摄像机
    public static void rotateCamera(float angle, float x, float y, float z) {
        synchronized (ManagerForCamera.class) {
            // 如果摄像机绝对旋转矩阵为空, 那么就新建一个单位矩阵给它.
            if (cameraRotateM == null) cameraRotateM = ManagerForMatrix.getIdentityM();
            // 把这次的旋转轴用摄像机绝对旋转矩阵旋转一下
            float[] oldAxis = new float[]{x, y, z, 1.0f};
            float[] newAxis = new float[4];
            Matrix.multiplyMV(newAxis, 0, cameraRotateM, 0, oldAxis, 0);
            // 用旧的旋转轴把摄像机绝对旋转矩阵旋转一下
            Matrix.rotateM(cameraRotateM, 0, angle, x, y, z);
            // 创建摄像机相对旋转矩阵并用新的旋转轴将它旋转一下
            float[] tempCameraRotateM = new float[16];
            Matrix.setRotateM(tempCameraRotateM, 0, angle, newAxis[0], newAxis[1], newAxis[2]);
            // 临时向量
            float[] tempVector;
            // 结果向量
            float[] resultVector = new float[4];
            // 用摄像机相对旋转矩阵旋转视线向量
            tempVector = new float[]{camera9P[3] - camera9P[0], camera9P[4] - camera9P[1], camera9P[5] - camera9P[2], 1.0f};
            Matrix.multiplyMV(resultVector, 0, tempCameraRotateM, 0, tempVector, 0);
            camera9P[3] = camera9P[0] + resultVector[0];
            camera9P[4] = camera9P[1] + resultVector[1];
            camera9P[5] = camera9P[2] + resultVector[2];
            // 用摄像机相对旋转矩阵旋转头顶向量
            tempVector = new float[]{camera9P[6], camera9P[7], camera9P[8], 1f};
            Matrix.multiplyMV(resultVector, 0, tempCameraRotateM, 0, tempVector, 0);
            camera9P[6] = resultVector[0];
            camera9P[7] = resultVector[1];
            camera9P[8] = resultVector[2];
            // 设置摄像机位置
            _setCamera(
                    camera9P[0], camera9P[1], camera9P[2],
                    camera9P[3], camera9P[4], camera9P[5],
                    camera9P[6], camera9P[7], camera9P[8]
            );
        }
    }

    // 环绕摄像机
    public static void surroundCamera(float angle, float x, float y, float z) {
        synchronized (ManagerForCamera.class) {
            // 如果摄像机绝对环绕矩阵为空, 那么就新建一个单位矩阵给它.
            if (cameraSurroundM == null) cameraSurroundM = ManagerForMatrix.getIdentityM();
            // 把这次的旋转轴用摄像机绝对环绕矩阵旋转一下
            float[] oldAxis = new float[]{x, y, z, 1.0f};
            float[] newAxis = new float[4];
            Matrix.multiplyMV(newAxis, 0, cameraSurroundM, 0, oldAxis, 0);
            // 用旧的旋转轴把摄像机绝对环绕矩阵旋转一下
            Matrix.rotateM(cameraSurroundM, 0, angle, x, y, z);
            // 创建摄像机相对环绕矩阵并用新的旋转轴将它旋转一下
            float[] tempCameraSurroundM = new float[16];
            Matrix.setRotateM(tempCameraSurroundM, 0, angle, newAxis[0], newAxis[1], newAxis[2]);
            // 临时向量
            float[] tempVector;
            // 结果向量
            float[] resultVector = new float[4];
            // 用摄像机相对环绕矩阵环绕视线向量
            tempVector = new float[]{camera9P[0] - camera9P[3], camera9P[1] - camera9P[4], camera9P[2] - camera9P[5], 1f};
            Matrix.multiplyMV(resultVector, 0, tempCameraSurroundM, 0, tempVector, 0);
            camera9P[0] = camera9P[3] + resultVector[0];
            camera9P[1] = camera9P[4] + resultVector[1];
            camera9P[2] = camera9P[5] + resultVector[2];
            // 用摄像机相对环绕矩阵环绕头顶向量
            tempVector = new float[]{camera9P[6], camera9P[7], camera9P[8], 1f};
            Matrix.multiplyMV(resultVector, 0, tempCameraSurroundM, 0, tempVector, 0);
            camera9P[6] = resultVector[0];
            camera9P[7] = resultVector[1];
            camera9P[8] = resultVector[2];
            // 设置摄像机位置
            _setCamera(
                    camera9P[0], camera9P[1], camera9P[2],
                    camera9P[3], camera9P[4], camera9P[5],
                    camera9P[6], camera9P[7], camera9P[8]
            );
        }
    }

    // 推缩摄像机
    public static void zoomCamera(float distance) {
        synchronized (ManagerForCamera.class) {
            // 获取摄像机到视线目标点向量
            float fx = camera9P[3] - camera9P[0];
            float fy = camera9P[4] - camera9P[1];
            float fz = camera9P[5] - camera9P[2];
            // 获取该向量长度
            float fLength = Matrix.length(fx, fy, fz);
            // 单位化该向量
            fx /= fLength;
            fy /= fLength;
            fz /= fLength;
            // 将摄像机位置沿视线向量方向推进distance距离
            camera9P[0] += fx * distance;
            camera9P[1] += fy * distance;
            camera9P[2] += fz * distance;
            // 将视线目标点沿视线向量方向推进distance距离
            camera9P[3] += fx * distance;
            camera9P[4] += fy * distance;
            camera9P[5] += fz * distance;
            // 设置摄像机位置
            _setCamera(
                    camera9P[0], camera9P[1], camera9P[2],
                    camera9P[3], camera9P[4], camera9P[5],
                    camera9P[6], camera9P[7], camera9P[8]
            );
        }
    }

    // 设置摄像机
    public static void setCamera(
            float ex, float ey, float ez,
            float cx, float cy, float cz,
            float upx, float upy, float upz
    ) {
        synchronized (ManagerForCamera.class) {
            // 设置摄像机9参数记录数组
            camera9P[0] = ex;
            camera9P[1] = ey;
            camera9P[2] = ez;
            camera9P[3] = cx;
            camera9P[4] = cy;
            camera9P[5] = cz;
            camera9P[6] = upx;
            camera9P[7] = upy;
            camera9P[8] = upz;

            // 将摄像机绝对旋转矩阵和绝对环绕矩阵置为null
            cameraRotateM = null;
            cameraSurroundM = null;

            // 设置摄像机(内部方法)
            _setCamera(ex, ey, ez, cx, cy, cz, upx, upy, upz);
        }
    }

    // 获取摄像机位置数组
    public static float[] getCameraLocation() {
        synchronized (ManagerForCamera.class) {
            return cameraLocation;
        }
    }

    // 镜像翻转摄像机
    public static void reverseCamera(float mirrorY) {
        synchronized (ManagerForCamera.class) {
            // 设置摄像机9参数记录数组
            camera9P[1] = -camera9P[1] + 2 * mirrorY;
            camera9P[4] = -camera9P[4] + 2 * mirrorY;
            camera9P[7] = -camera9P[7];

            // 设置摄像机(内部方法)
            _setCamera(
                    camera9P[0], camera9P[1], camera9P[2],
                    camera9P[3], camera9P[4], camera9P[5],
                    camera9P[6], camera9P[7], camera9P[8]
            );
        }
    }

    // 获取摄像机位置矩阵
    public static float[] getCameraMatrix() {
        synchronized (ManagerForCamera.class) {
            return cameraMatrix;
        }
    }

    // 设置摄像机(内部方法)
    private static void _setCamera(
            float ex, float ey, float ez,
            float cx, float cy, float cz,
            float upx, float upy, float upz
    ) {
        Matrix.setLookAtM(cameraMatrix, 0, ex, ey, ez, cx, cy, cz, upx, upy, upz);
        cameraLocation[0] = ex;
        cameraLocation[1] = ey;
        cameraLocation[2] = ez;
    }
}
