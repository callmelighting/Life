package bn.wallpaper.llz;

import android.content.res.Resources;

import com.bn.jar.bnggdh.Bnggdh;

import java.io.IOException;
import java.io.InputStream;

// Bnggdh读取类
public class ReaderForBnggdh {
    public Bnggdh bnggdh;

    public ReaderForBnggdh(String path, Resources r) {
        try {
            InputStream in = r.getAssets().open(path);
            bnggdh = new Bnggdh(in);
            bnggdh.init();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
