package bn.wallpaper.llz;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySceneRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "MySceneRenderer";
    private int fboID;
    private int rboID;
    private int texID;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置屏幕背景色RGBA
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // 打开深度检测
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        // 打开背面剪裁
        GLES30.glEnable(GLES30.GL_CULL_FACE);
        // 打开混合
        GLES30.glEnable(GLES30.GL_BLEND);
        // 设置混合参数
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
        // 初始化物体变换矩阵
        ManagerForMatrix.initMatrix();
        // 初始化摄像机位置
        ManagerForCamera.setCamera(0f, 2f, 10f, 0f, 0f, 0f, 0f, 1f, 0f);
        // 环绕摄像机到合适的角度
        ManagerForCamera.surroundCamera(24f, 0f, 1f, 0f);
        Constant.sceneID = 1;
        // 加载程序数据
        ManagerForData.loadData();
        // 计算物体绘制组
        ManagerForData.calculateGLDG();
        // 计算灯光
        ManagerForLight.calculateLight();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置视口大小及位置
        GLES30.glViewport(0, 0, width, height);
        // 获取GLSurfaceView的宽高比, 宽和高
        Constant.V_RATIO = (float) width / height;
        Constant.V_WIDTH = (float) width;
        Constant.V_HEIGHT = (float) height;
        // 设置近平面RIGHT与TOP
        Constant.V_RIGHT = 2 * Constant.V_RATIO;
        Constant.V_TOP = 2;
        // 设置近平面和远平面与视点的距离
        Constant.V_NEAR = 5;
        Constant.V_FAR = 200;
        // 调用此方法计算产生透视投影矩阵
        ManagerForMatrix.setProjectFrustum(
                -Constant.V_RIGHT, Constant.V_RIGHT,
                -Constant.V_TOP, Constant.V_TOP,
                Constant.V_NEAR, Constant.V_FAR
        );
        // 初始化帧缓冲
        initFBO();
        ManagerForData.sedimentL.setMirrorTexID(texID);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Constant.timeUpdate = false;
        drawSediment();
        Constant.timeUpdate = true;
        drawScenes();
    }

    // 离屏渲染, 获取镜像纹理
    private void drawSediment() {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fboID);
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

        ManagerForCamera.reverseCamera(-1);
        if (ManagerForData.getNowSedimentGLDG() != null && (Constant.weather == 2 || Constant.weather == 4)) {
            ManagerForData.getNowSedimentGLDG().drawAll();
        }
    }

    // 正常绘制
    private void drawScenes() {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

        ManagerForCamera.reverseCamera(-1);
        if (ManagerForData.getNowGLDG() != null) {
            ManagerForData.sedimentL.setMPCMatrix(ManagerForMatrix.getMPCMatrix());
            ManagerForData.getNowGLDG().drawAll();
        }
    }

    // 初始化帧缓冲
    private void initFBO() {
        //用于存放产生的缓冲编号的临时数组
        int[] tempIDs = new int[1];
        //创建一个帧缓冲
        GLES30.glGenFramebuffers(1, tempIDs, 0);
        //将帧缓冲编号记录到临时数组中
        fboID = tempIDs[0];
        //绑定帧缓冲
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fboID);
        //创建一个渲染缓冲
        GLES30.glGenRenderbuffers(1, tempIDs, 0);
        //将渲染缓冲编号记录到临时数组中
        rboID = tempIDs[0];
        //绑定指定的渲染缓冲
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, rboID);
        //为渲染缓冲初始化存储
        GLES30.glRenderbufferStorage(
                GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16,
                (int) Constant.V_WIDTH, (int) Constant.V_HEIGHT
        );
        //创建一个纹理
        GLES30.glGenTextures(1, tempIDs, 0);
        //绑定纹理
        texID = tempIDs[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texID);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        //设置颜色附件纹理的格式
        GLES30.glTexImage2D(
                GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA,//类型、层次与内部格式
                (int) Constant.V_WIDTH, (int) Constant.V_HEIGHT, //宽度与高度
                0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null//边界宽度、格式与每个像素数据的格式
        );
        //设置自定义帧缓冲的颜色附件
        GLES30.glFramebufferTexture2D(
                GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0,//帧缓冲类型、附件类型——颜色附件0
                GLES30.GL_TEXTURE_2D, texID, 0//附件为2D纹理、纹理ID与纹理层次
        );
        //设置自定义帧缓冲的深度附件
        GLES30.glFramebufferRenderbuffer(
                GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT,//帧缓冲类型、附件类型——深度附件
                GLES30.GL_RENDERBUFFER, rboID// 附件为渲染缓冲、深度渲染缓冲编号
        );
        //绑定到0号纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        //绑定到0号渲染缓冲
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, 0);
        //检查帧缓冲是否完整
        if (GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER) != GLES30.GL_FRAMEBUFFER_COMPLETE) {
            Log.e(TAG, "initFBO: " + "帧缓冲不完整!");
        }
        //绑定到0号帧缓冲
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
    }
}
