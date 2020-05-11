package bn.wallpaper.llz;

import java.util.ArrayList;

// 物体绘制组, 可以向该组中添加GLDrawableObject, 然后调用drawAll()方法统一绘制
public class GLDrawableGroup {
    private ArrayList<GLDrawableObject> gLDrawableObjectList; // GLDrawableObject的ArrayList
    private GLDrawableObject intersectingObject;              // 与射线相交距离最短的GLDrawableObject
    private float minT;                                       // 该GLDrawableObject与射线起始点的距离
    private float[] point;                                    // 该GLDrawableObject与射线的相交点

    // 构造函数
    public GLDrawableGroup() {
        // 初始化数据, 创建对象
        gLDrawableObjectList = new ArrayList<>();
        intersectingObject = null;
        minT = Float.POSITIVE_INFINITY;
        point = new float[3];
    }

    // 绘制GLDrawableGroup内全部GLDrawableObject
    public void drawAll() {
        for (int i = 0; i < gLDrawableObjectList.size(); i++) {
            gLDrawableObjectList.get(i).draw();
        }
    }

    // 添加GLDrawableObject
    public void add(GLDrawableObject glDrawableObject) {
        gLDrawableObjectList.add(glDrawableObject);
    }

    // 添加GLDrawableObject
    public void add(GLDrawable glDrawable, float[] selfMatrix) {
        add(glDrawable, selfMatrix, -1);
    }

    // 添加GLDrawableObject
    public void add(GLDrawable glDrawable, float[] selfMatrix, int textureID) {
        gLDrawableObjectList.add(new GLDrawableObject(glDrawable, selfMatrix, textureID));
    }

    // 获取GLDrawableObject
    public GLDrawableObject get(int id) {
        return gLDrawableObjectList.get(id);
    }

    // 移除GLDrawableObject
    public void remove(int id) {
        gLDrawableObjectList.remove(id);
    }

    // 移除GLDrawableObject
    public void remove(GLDrawableObject glDrawableObject) {
        gLDrawableObjectList.remove(glDrawableObject);
    }

    // 获取该组中GLDrawableObject的数量
    public int getSize() {
        return gLDrawableObjectList.size();
    }

    // 发射射线, 返回值为相交点, 没有的话返回null
    public float[] launchRay(float[] ray) {
        // 每次发射射线时, 都需要把上一次的intersectingObject和minT重置为空.
        // 否则它们对应的get方法会返回上一次的值.
        // 但是point没有对应的get方法, 所以不用重置point.
        intersectingObject = null;
        minT = Float.POSITIVE_INFINITY;

        // 遍历整个绘制组, 向组中每一个物体发射射线, 并记录最小的相交t值
        for (int i = 0; i < gLDrawableObjectList.size(); i++) {
            GLDrawableObject tempGLDrawableObject = gLDrawableObjectList.get(i);
            float tempT = tempGLDrawableObject.getIntersectionT(ray);
            if (tempT < minT) {
                intersectingObject = tempGLDrawableObject;
                minT = tempT;
            }
        }

        // 如果射线没有与组中任何一个物体相交, 则返回null, 否则返回相交点
        if (minT == Float.POSITIVE_INFINITY) {
            return null;
        } else {
            point[0] = ray[0] + minT * (ray[3] - ray[0]);
            point[1] = ray[1] + minT * (ray[4] - ray[1]);
            point[2] = ray[2] + minT * (ray[5] - ray[2]);
            return point;
        }
    }

    // 获取与射线相交距离最短的GLDrawableObject
    public GLDrawableObject getIntersectingObject() {
        return intersectingObject;
    }

    // 获取该GLDrawableObject与射线起始点的距离
    public float getMinT() {
        return minT;
    }
}
