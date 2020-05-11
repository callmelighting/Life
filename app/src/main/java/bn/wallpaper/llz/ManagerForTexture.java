package bn.wallpaper.llz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ManagerForTexture {
    private static Resources r;
    private static Map<String, Integer> map = new HashMap<>(); // 纹理名字与ID对照字典

    // 初始化
    public static void init(Resources r) {
        ManagerForTexture.r = r;
    }

    // 添加纹理
    public static void addTexture(String name, String path) {
        int[] texture = new int[1];
        GLES30.glGenTextures(1, texture, 0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture[0]);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT);

        // 通过输入流加载图片
        Bitmap bitmapTmp = null;
        try {
            InputStream is = r.getAssets().open(path);
            bitmapTmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 加载纹理到显存
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmapTmp, 0);

        // 回收图片
        if (bitmapTmp != null) {
            bitmapTmp.recycle();
        }

        // 把名字和生成的纹理ID放到字典里
        map.put(name, texture[0]);
    }

    // 获取指定名字的纹理ID
    public static int getTexture(String name) {
        return map.get(name);
    }
}
