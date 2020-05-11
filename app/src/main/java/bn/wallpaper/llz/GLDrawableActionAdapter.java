package bn.wallpaper.llz;

// GLDrawableObject 的行为接口的适配器
public class GLDrawableActionAdapter implements GLDrawableAction {
    @Override
    public void drawStart(GLDrawableObject GLDO) {
        // 物体绘画开始时要执行的方法
    }

    @Override
    public void moveStart(GLDrawableObject GLDO) {
        // 物体移动开始时要执行的方法
    }

    @Override
    public void rotateStart(GLDrawableObject GLDO) {
        // 物体旋转开始时要执行的方法
    }

    @Override
    public void drawEnd(GLDrawableObject GLDO) {
        // 物体绘画结束时要执行的方法
    }

    @Override
    public void moveEnd(GLDrawableObject GLDO) {
        // 物体移动结束时要执行的方法
    }

    @Override
    public void rotateEnd(GLDrawableObject GLDO) {
        // 物体旋转结束时要执行的方法
    }

    @Override
    public void beTouched(GLDrawableObject GLDO, float x, float y, float z) {
        // 物体被触摸到时要执行的方法
    }
}
