package bn.wallpaper.llz;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class MyWallpaperService extends WallpaperService {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化程序数据
        ManagerForData.init(getResources());
        // 读取数据
        ManagerForData.readData();
    }

    @Override
    public Engine onCreateEngine() {
        return new GLEngine();
    }

    private class GLEngine extends Engine {
        private float preX;            // 上次的触控位置X坐标
        private float preY;            // 上次的触控位置Y坐标
        private float yAngle = 0;      // 绕Y轴旋转的角度
        private float xAngle = 0;      // 绕X轴旋转的角度
        private long preTime = 0;      // 上次触摸时间

        private WallpaperGLSurfaceView mySurfaceView;   // SurfaceView
        private MySceneRenderer mySceneRenderer;        // 渲染器
        private boolean rendererHasBeenSet;             // 渲染器是否已设置标志位

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // 创建 SurfaceView
            mySurfaceView = new WallpaperGLSurfaceView(MyWallpaperService.this);

            // 检查系统是否支持 OpenGL ES 3.0
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ConfigurationInfo configurationInfo;
            boolean supportES3 = false;

            if (activityManager != null) {
                configurationInfo = activityManager.getDeviceConfigurationInfo();
                supportES3 = configurationInfo.reqGlEsVersion >= 0x00030000;
            }

            if (supportES3) { // 如果支持
                // 设置使用 OpenGL ES 3.0
                mySurfaceView.setEGLContextClientVersion(3);
                // 某些手机不添加这句话则无法使用模板测试
                mySurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
                // 设置保留上下文
                mySurfaceView.setPreserveEGLContextOnPause(true);
                // 创建渲染器
                mySceneRenderer = new MySceneRenderer();
                // 设置渲染器
                mySurfaceView.setRenderer(mySceneRenderer);
                // 设置渲染模式为连续渲染
                mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                // 将渲染器是否已设置标志位设置为真
                rendererHasBeenSet = true;
            } else { // 如果不支持
                // 显示 Toast
                Toast.makeText(
                        MyWallpaperService.this,
                        "该设备不支持 OpenGL ES 3.0, 程序无法运行.",
                        Toast.LENGTH_LONG
                ).show();
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (rendererHasBeenSet) {
                if (visible) {
                    mySurfaceView.onResume();
                    ManagerForWorld.refreshWeatherAndTime(getApplicationContext());
                } else {
                    mySurfaceView.onPause();
                }
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            mySurfaceView.onDestroy();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            // 获取现在手指触摸的的XY坐标
            float x = event.getX();
            float y = event.getY();
            // 获取现在的时间
            long time = System.currentTimeMillis();

            if (y < Constant.V_HEIGHT / 3) {
                // 如果现在的时间距离上一次触摸的时间间隔大于100毫秒
                if ((time - preTime) > 100) {


                    // ====== 演示模式代码 ======
                    if (x < Constant.V_WIDTH / 3) {
                        if (Constant.weather < 4)
                            Constant.weather++;
                        else
                            Constant.weather = 0;
                        ManagerForLight.calculateLight();
                        ManagerForData.calculateGLDG();
                    } else if (x < Constant.V_WIDTH / 3 * 2) {
                        if (Constant.time < 20)
                            Constant.time++;
                        else
                            Constant.time = 7;
                        ManagerForLight.calculateLight();
                        ManagerForData.calculateGLDG();
                        // ====== 演示模式代码 ======
                    } else {
                        // 如果是晚上, 且天气为阴天或晴天, 就放烟花
                        if (!ManagerForLight.isDaytime() && (Constant.weather == 0 || Constant.weather == 1)) {
                            // 如果场景为1且烟花1不可见
                            if (Constant.sceneID == 1 && !((LoaderForFireworks1) ManagerForData.fireworks1GLDO.getGlDrawable()).getDrawable()) {
                                // 创建临时矩阵
                                float[] tempM = ManagerForMatrix.getIdentityM();
                                // 变换矩阵
                                Matrix.translateM(tempM, 0,
                                        (float) Math.random() - 0.5f,
                                        (float) Math.random() + 2.3f,
                                        (float) Math.random() - 4.0f
                                );
                                // 设置烟花的矩阵
                                ManagerForData.fireworks1GLDO.setSelfMatrix(tempM);
                                // 刷新烟花的颜色
                                ((LoaderForFireworks1) ManagerForData.fireworks1GLDO.getGlDrawable()).refreshColor();
                                // 设置烟花可见
                                ((LoaderForFireworks1) ManagerForData.fireworks1GLDO.getGlDrawable()).setDrawable(true);
                            }
                            // 如果场景为2且烟花2不可见
                            else if (Constant.sceneID == 2 && !((LoaderForFireworks2) ManagerForData.fireworks2GLDO.getGlDrawable()).getDrawable()) {
                                // 创建临时矩阵
                                float[] tempM = ManagerForMatrix.getIdentityM();
                                // 变换矩阵
                                Matrix.translateM(tempM, 0,
                                        4.0f + (float) Math.random() * 0.8f,
                                        2.9f + (float) Math.random() * 0.3f,
                                        -1.0f + (float) Math.random() * 0.5f
                                );
                                // 设置烟花的矩阵
                                ManagerForData.fireworks2GLDO.setSelfMatrix(tempM);
                                // 设置烟花可见
                                ((LoaderForFireworks2) ManagerForData.fireworks2GLDO.getGlDrawable()).setDrawable(true);
                            }
                        }
                    }
                }
            } else if (y < Constant.V_HEIGHT / 3 * 2) {
                switch (event.getAction()) { // 判断手指行为
                    case MotionEvent.ACTION_MOVE: // 如果手指行为是移动
                        if (preX - x > 10 && Constant.sceneID == 1) { // 如果手指向左滑动且现在是场景1
                            // 旋转摄像机
                            ManagerForCamera.rotateCamera(-23, 0, 1, 0);
                            // 移动 sky_1 和 lighting
                            float[] tempM1 = ManagerForMatrix.getIdentityM();
                            Matrix.translateM(tempM1, 0, tempM1, 0, 3.95f, 0f, -0.6f);
                            Matrix.rotateM(tempM1, 0, tempM1, 0, -23f, 0f, 1f, 0f);
                            float[] newM1 = new float[16];
                            Matrix.multiplyMM(newM1, 0, tempM1, 0, ManagerForData.nightSkyGLDO.getSelfMatrix(), 0);
                            ManagerForData.nightSkyGLDO.setSelfMatrix(newM1);
                            Matrix.multiplyMM(newM1, 0, tempM1, 0, ManagerForData.daySkyGLDO.getSelfMatrix(), 0);
                            ManagerForData.daySkyGLDO.setSelfMatrix(newM1);
                            Matrix.multiplyMM(newM1, 0, tempM1, 0, ManagerForData.lightingGLDO.getSelfMatrix(), 0);
                            ManagerForData.lightingGLDO.setSelfMatrix(newM1);
                            // 修改场景ID
                            Constant.sceneID = 2;
                        } else if (x - preX > 10 && Constant.sceneID == 2) { // 如果手指向右滑动且现在是场景2
                            // 旋转摄像机
                            ManagerForCamera.rotateCamera(23, 0, 1, 0);
                            // 移动 sky_1 和 lighting
                            float[] tempM1 = ManagerForMatrix.getIdentityM();
                            Matrix.rotateM(tempM1, 0, tempM1, 0, 23f, 0f, 1f, 0f);
                            Matrix.translateM(tempM1, 0, tempM1, 0, -3.95f, 0f, 0.6f);
                            float[] newM1 = new float[16];
                            Matrix.multiplyMM(newM1, 0, tempM1, 0, ManagerForData.nightSkyGLDO.getSelfMatrix(), 0);
                            ManagerForData.nightSkyGLDO.setSelfMatrix(newM1);
                            Matrix.multiplyMM(newM1, 0, tempM1, 0, ManagerForData.daySkyGLDO.getSelfMatrix(), 0);
                            ManagerForData.daySkyGLDO.setSelfMatrix(newM1);
                            Matrix.multiplyMM(newM1, 0, tempM1, 0, ManagerForData.lightingGLDO.getSelfMatrix(), 0);
                            ManagerForData.lightingGLDO.setSelfMatrix(newM1);
                            // 修改场景ID
                            Constant.sceneID = 1;
                        }
                }
                // 更新XY坐标值
                preX = x;
                preY = y;
            } else if (y < Constant.V_HEIGHT / 40 * 39) {
                // 如果现在的时间距离上一次触摸的时间间隔大于100毫秒
                if ((time - preTime) > 100) {
                    // 检测是否触摸到物体
                    float[] tempPoint = ManagerForData.getNowGLDG().launchRay(
                            ManagerForMatrix.getABPosition(
                                    x, y,
                                    Constant.V_WIDTH, Constant.V_HEIGHT,
                                    Constant.V_RIGHT, Constant.V_TOP,
                                    Constant.V_NEAR, Constant.V_FAR
                            ));
                    // 如果触摸到了物体
                    if (tempPoint != null) {
                        // 执行被触摸物体的 beTouched() 方法
                        ManagerForData.getNowGLDG().getIntersectingObject().beTouched(
                                tempPoint[0], tempPoint[1], tempPoint[2]);
                    }
                }
            }

            // 更新触摸时间戳
            preTime = time;
        }

        private class WallpaperGLSurfaceView extends GLSurfaceView {

            public WallpaperGLSurfaceView(Context context) {
                super(context);
            }

            @Override
            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }

            public void onDestroy() {
                super.onDetachedFromWindow();
            }
        }
    }
}

