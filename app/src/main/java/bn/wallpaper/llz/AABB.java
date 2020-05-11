package bn.wallpaper.llz;

// AABB包围盒
public class AABB {
    private float minX; // AABB包围盒的最小X值
    private float maxX; // AABB包围盒的最大X值
    private float minY; // AABB包围盒的最小Y值
    private float maxY; // AABB包围盒的最大Y值
    private float minZ; // AABB包围盒的最小Z值
    private float maxZ; // AABB包围盒的最大Z值

    // 构造函数
    public AABB(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    // 获取射线与AABB包围盒相交的T值
    public float getIntersectionT(float[] rayStart, float[] rayEnd) {
        // 使用参数方程 P = P起点 + t * Pd (其中Pd为终点减去起点得到的差值)
        // 解开成三个轴的参数方程则为
        // X = X起点 + t * Xd (Xd = X终点 - X起点)
        // Y = Y起点 + t * Yd (Yd = Y终点 - Y起点)
        // Z = Z起点 + t * Zd (Zd = Z终点 - Z起点)
        // 有效的t应该在 (0～1之间)


        // 如果未相交, t等于正无穷
        float t = Float.POSITIVE_INFINITY;

        // 计算射线段是否与 X = min.x 平面相交
        float tempT = (minX - rayStart[0]) / (rayEnd[0] - rayStart[0]);
        // 若t值在0～1的范围内
        if (tempT >= 0 && tempT <= 1) {
            // 计算出射线段与X = min.x 平面的交点Y、Z坐标
            float y = rayStart[1] + tempT * (rayEnd[1] - rayStart[1]);
            float z = rayStart[2] + tempT * (rayEnd[2] - rayStart[2]);
            // 考察交点Y、Z坐标是否在包围盒此面的范围内
            if (y >= minY && y <= maxY && z >= minZ && z <= maxZ) {
                // 若交点在包围盒此面的范围内则考察当前交点值是否小于原先t值
                // 若小于则说明此交点离摄像机更近, 则更新t值
                if (tempT < t) {
                    t = tempT;
                }
            }
        }

        // 计算射线段是否与 X = max.x 平面相交
        tempT = (maxX - rayStart[0]) / (rayEnd[0] - rayStart[0]);
        if (tempT >= 0 && tempT <= 1) {
            float y = rayStart[1] + tempT * (rayEnd[1] - rayStart[1]);
            float z = rayStart[2] + tempT * (rayEnd[2] - rayStart[2]);
            if (y >= minY && y <= maxY && z >= minZ && z <= maxZ) {
                if (tempT < t) {
                    t = tempT;
                }
            }
        }

        // 计算射线段是否与 Y = min.y 平面相交
        tempT = (minY - rayStart[1]) / (rayEnd[1] - rayStart[1]);
        if (tempT >= 0 && tempT <= 1) {
            float x = rayStart[0] + tempT * (rayEnd[0] - rayStart[0]);
            float z = rayStart[2] + tempT * (rayEnd[2] - rayStart[2]);
            if (x >= minX && x <= maxX && z >= minZ && z <= maxZ) {
                if (tempT < t) {
                    t = tempT;
                }
            }
        }

        // 计算射线段是否与 Y = max.y 平面相交
        tempT = (maxY - rayStart[1]) / (rayEnd[1] - rayStart[1]);
        if (tempT >= 0 && tempT <= 1) {
            float x = rayStart[0] + tempT * (rayEnd[0] - rayStart[0]);
            float z = rayStart[2] + tempT * (rayEnd[2] - rayStart[2]);
            if (x >= minX && x <= maxX && z >= minZ && z <= maxZ) {
                if (tempT < t) {
                    t = tempT;
                }
            }
        }

        // 计算射线段是否与 Z = min.z 平面相交
        tempT = (minZ - rayStart[2]) / (rayEnd[2] - rayStart[2]);
        if (tempT >= 0 && tempT <= 1) {
            float x = rayStart[0] + tempT * (rayEnd[0] - rayStart[0]);
            float y = rayStart[1] + tempT * (rayEnd[1] - rayStart[1]);
            if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                if (tempT < t) {
                    t = tempT;
                }
            }
        }

        // 计算射线段是否与 Z = max.z 平面相交
        tempT = (maxZ - rayStart[2]) / (rayEnd[2] - rayStart[2]);
        if (tempT >= 0 && tempT <= 1) {
            float x = rayStart[0] + tempT * (rayEnd[0] - rayStart[0]);
            float y = rayStart[1] + tempT * (rayEnd[1] - rayStart[1]);
            if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                if (tempT < t) {
                    t = tempT;
                }
            }
        }

        // 返回t值
        return t;
    }
}
