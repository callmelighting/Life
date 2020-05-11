package bn.wallpaper.llz;

import android.opengl.Matrix;

import java.util.ArrayList;

public class Vector3f {
    public float x, y, z;

    public Vector3f() {

    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void normalize() {
        float length = Matrix.length(x, y, z);
        x /= length;
        y /= length;
        z /= length;
    }

    public float dotMultiply(Vector3f vec) {
        return Vector3f.dotMultiply(this, vec);
    }

    public Vector3f crossMultiply(Vector3f vec) {
        return Vector3f.crossMultiply(this, vec);
    }

    public static float dotMultiply(Vector3f vec1, Vector3f vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
    }

    public static Vector3f crossMultiply(Vector3f vec1, Vector3f vec2) {
        return new Vector3f(
                vec1.y * vec2.z - vec2.y * vec1.z,
                -(vec1.x * vec2.z - vec2.x * vec1.z),
                vec1.x * vec2.y - vec2.x * vec1.y
        );
    }

    public static Vector3f getAverage(ArrayList<Vector3f> vecList) {
        float sumX = 0;
        float sumY = 0;
        float sumZ = 0;
        for (Vector3f tempVec : vecList) {
            sumX += tempVec.x;
            sumY += tempVec.y;
            sumZ += tempVec.z;
        }
        return new Vector3f(
                sumX / vecList.size(),
                sumY / vecList.size(),
                sumZ / vecList.size()
        );
    }
}
