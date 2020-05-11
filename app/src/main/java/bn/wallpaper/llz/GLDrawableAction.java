package bn.wallpaper.llz;

// GLDrawableObject 的行为接口
public interface GLDrawableAction {
    // 物体绘画开始时要执行的方法
    void drawStart(GLDrawableObject GLDO);

    // 物体移动开始时要执行的方法
    void moveStart(GLDrawableObject GLDO);

    // 物体旋转开始时要执行的方法
    void rotateStart(GLDrawableObject GLDO);

    // 物体绘画结束时要执行的方法
    void drawEnd(GLDrawableObject GLDO);

    // 物体移动结束时要执行的方法
    void moveEnd(GLDrawableObject GLDO);

    // 物体旋转结束时要执行的方法
    void rotateEnd(GLDrawableObject GLDO);

    // 物体被触摸到时要执行的方法
    void beTouched(GLDrawableObject GLDO, float x, float y, float z);
}
