package bn.wallpaper.llz;

// 所有要绘制的物体都必须实现这个接口
public interface GLDrawable {
    // 绘制自身 参数依次为: 纹理ID, 自身的基本变换矩阵, 是否绘制影子标志位, 影子颜色
    void drawSelf(int textureID, float[] selfMatrix, boolean drawShadow, float[] shadowColor);

    // 获取自身的AABB包围盒
    AABB getAABB();

    // 获取自身的中心位置
    float[] getPosition();

    // 设置自身的方向
    void setDirection(float x, float y, float z);

    // 获取自身的方向
    float[] getDirection();
}
