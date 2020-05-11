package bn.wallpaper.llz;

import android.opengl.Matrix;

public class UtilForVector {
    // 计算两向量叉积
    public static float[] cross(float x1, float y1, float z1, float x2, float y2, float z2) {
        return new float[]{y1 * z2 - y2 * z1, -(x1 * z2 - x2 * z1), x1 * y2 - x2 * y1};
    }

    // 计算两向量点积
    public static float dot(float x1, float y1, float z1, float x2, float y2, float z2) {
        return x1 * x2 + y1 * y2 + z1 * z2;
    }

    // 计算两向量夹角sin值
    public static float sin(float x1, float y1, float z1, float x2, float y2, float z2) {
        float[] cross = cross(x1, y1, z1, x2, y2, z2);
        return Matrix.length(cross[0], cross[1], cross[2]) /
                (Matrix.length(x1, y1, z1) * Matrix.length(x2, y2, z2));
    }

    // 计算两向量夹角cos值
    public static float cos(float x1, float y1, float z1, float x2, float y2, float z2) {
        return dot(x1, y1, z1, x2, y2, z2) /
                (Matrix.length(x1, y1, z1) * Matrix.length(x2, y2, z2));
    }

    // 计算两向量夹角(以弧度计)
    public static float angle(float x1, float y1, float z1, float x2, float y2, float z2) {
        return (float) Math.acos(cos(x1, y1, z1, x2, y2, z2));
    }
}
