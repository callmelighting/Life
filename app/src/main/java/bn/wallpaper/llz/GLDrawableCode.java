package bn.wallpaper.llz;

// 若要在GLDrawableGroup中添加一段代码则使用该类
public class GLDrawableCode extends GLDrawableObject {

    // 构造函数, 调用父类构造函数
    public GLDrawableCode() {
        super(null, null);
    }

    // 创建匿名类时请重写该方法
    public void runCode() {
    }

    // GLDrawableGroup自动调用的draw方法会调用runCode()方法
    public void draw() {
        runCode();
    }
}
