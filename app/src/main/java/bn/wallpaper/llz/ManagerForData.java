package bn.wallpaper.llz;

import android.content.res.Resources;
import android.opengl.GLES30;

public class ManagerForData {
    private static Resources r;

    private static ReaderForObject benchR;          // 长凳R
    private static ReaderForObject houseR;          // 房子R
    private static ReaderForObject house_fixR;      // 房子补丁R
    private static ReaderForObject landR;           // 大地R
    private static ReaderForObject fenceR;          // 栅栏R
    private static ReaderForObject waterR;          // 水面R
    private static ReaderForObject carR;            // 车R
    private static ReaderForObject soilR;           // 土壤R
    private static ReaderForObject flower1R;        // 花1R
    private static ReaderForObject flower2R;        // 花2R
    private static ReaderForObject flower3R;        // 花3R
    private static ReaderForObject flower4R;        // 花4R
    private static ReaderForObject grass1R;         // 草1R
    private static ReaderForObject grass2R;         // 草2R

    private static ReaderForBnggdh cat_eatR;        // 猫_吃R
    private static ReaderForBnggdh cat_idleR;       // 猫_闲R
    private static ReaderForBnggdh cat_soundR;      // 猫_叫R
    private static ReaderForBnggdh cat_walkR;       // 猫_走R
    private static ReaderForBnggdh treeR;           // 树R

    private static ReaderForBnggdh water_tapR;      // 水龙头R
    private static ReaderForBnggdh pipe1R;          // 水管动作1R
    private static ReaderForBnggdh pipe2R;          // 水管动作2R

    private static ReaderForBnggdh people_walkR;    // 人走R
    private static ReaderForBnggdh people_idleR;    // 人呆R
    private static ReaderForBnggdh people_watering1R; // 人拿水管动作1R
    private static ReaderForBnggdh people_watering2R; // 人拿水管动作2R

    private static LoaderForObject benchL;          // 长凳L
    private static LoaderForObject houseL;          // 房子L
    private static LoaderForObject house_fixL;      // 房子补丁L
    private static LoaderForObject landL;           // 大地L
    private static LoaderForObject fenceL;          // 栅栏L
    private static LoaderForObject waterL;          // 水面L
    private static LoaderForObject carL;            // 车L
    private static LoaderForObject soilL;           // 土壤L
    private static LoaderForObject flower1L;        // 花1L
    private static LoaderForObject flower2L;        // 花2L
    private static LoaderForObject flower3L;        // 花3L
    private static LoaderForObject flower4L;        // 花4L
    private static LoaderForObject grass1L;         // 草1L
    private static LoaderForObject grass2L;         // 草2L

    private static LoaderForBnggdh cat_eatL;        // 猫_吃L
    private static LoaderForBnggdh cat_idleL;       // 猫_闲L
    private static LoaderForBnggdh cat_soundL;      // 猫_叫L
    private static LoaderForBnggdh cat_walkL;       // 猫_走L
    private static LoaderForBnggdh treeL;           // 树L

    private static LoaderForBnggdh water_tapL;      // 水龙头L
    private static LoaderForBnggdh pipe1L;          // 水管动作1L
    private static LoaderForBnggdh pipe2L;          // 水管动作2L

    private static LoaderForBnggdh people_walkL;    // 人走L
    private static LoaderForBnggdh people_idleL;    // 人呆L
    private static LoaderForBnggdh people_watering1L; // 人拿水管动作1L
    private static LoaderForBnggdh people_watering2L; // 人拿水管动作2L

    private static LoaderForNightSky nightSky;      // 夜晚天空L
    private static LoaderForDaySky daySkyL;         // 白天天空L
    private static LoaderForNightSky lightingL;     // 闪电L
    private static LoaderForParticle rainL;         // 雨L
    private static LoaderForParticle snowL;         // 雪L
    private static LoaderForParticle fountainL;     // 喷水L
    private static LoaderForFireworks1 fireworks1L; // 烟花1L
    private static LoaderForFireworks2 fireworks2L; // 烟花2L
    public static LoaderForSediment sedimentL;      // 积水L

    private static GLDrawableObject benchGLDO;      // 长凳GLDO
    private static GLDrawableObject houseGLDO;      // 房子GLDO
    private static GLDrawableObject house_fixGLDO;  // 房子补丁GLDO
    private static GLDrawableObject landGLDO;       // 大地GLDO
    private static GLDrawableObject fenceGLDO;      // 栅栏GLDO
    private static GLDrawableCode waterGLDC;        // 水面GLDC
    private static GLDrawableObject carGLDO;        // 车GLDO
    private static GLDrawableObject soilGLDO;       // 土壤GLDO
    private static GLDrawableObject flower1GLDO;    // 花1GLDO
    private static GLDrawableObject flower2GLDO;    // 花2GLDO
    private static GLDrawableObject flower3GLDO;    // 花3GLDO
    private static GLDrawableObject flower4GLDO;    // 花4GLDO
    private static GLDrawableObject grass1GLDO;     // 草1GLDO
    private static GLDrawableObject grass2GLDO;     // 草2GLDO
    private static GLDrawableObject catGLDO;        // 猫GLDO
    private static GLDrawableObject treeGLDO;       // 树GLDO

    private static GLDrawableObject water_tapGLDO;  // 水龙头GLDO
    private static GLDrawableObject pipeGLDO;       // 水管GLDO
    private static GLDrawableObject peopleGLDO;     // 人类GLDO

    public static GLDrawableObject nightSkyGLDO;    // 夜晚天空GLDO
    public static GLDrawableObject daySkyGLDO;      // 白天天空GLDO
    public static GLDrawableObject lightingGLDO;    // 闪电GLDO
    private static GLDrawableObject rainGLDO;       // 雨GLDO
    private static GLDrawableObject snowGLDO;       // 雪GLDO
    private static GLDrawableObject fountainGLDO;   // 喷水GLDO
    public static GLDrawableObject fireworks1GLDO;  // 烟花1GLDO
    public static GLDrawableObject fireworks2GLDO;  // 烟花2GLDO

    private static GLDrawableObject nowObject;      // 现在挂载的GLDO

    private static GLDrawableGroup cloudyGLDG;      // 阴_GLDG
    private static GLDrawableGroup sunnyGLDG;       // 晴_GLDG
    private static GLDrawableGroup rainyGLDG;       // 雨_GLDG
    private static GLDrawableGroup snowyGLDG;       // 雪_GLDG
    private static GLDrawableGroup lightningGLDG;   // 电_GLDG

    private static GLDrawableGroup sedimentGLDG;    // 积水GLDG

    private static GLDrawableGroup nowGLDG;         // 现在使用的GLDG
    private static GLDrawableGroup nowSedimentGLDG; // 现在的积水GLDG

    private static float[] peopleMatrix;            // 人类浇水位置矩阵

    // 初始化
    public static void init(Resources r) {
        ManagerForData.r = r;
    }

    // 读取数据
    public static void readData() {
        benchR = new ReaderForObject("obj/bench/bench.obj", r);
        houseR = new ReaderForObject("obj/house/house.obj", r);
        house_fixR = new ReaderForObject("obj/house/house_fix.obj", r);
        landR = new ReaderForObject("obj/land/land.obj", r);
        fenceR = new ReaderForObject("obj/fence/fence.obj", r);
        waterR = new ReaderForObject("obj/water/water.obj", r);
        carR = new ReaderForObject("obj/car/car.obj", r);
        soilR = new ReaderForObject("obj/soil/soil.obj", r);
        flower1R = new ReaderForObject("obj/flower/flower1.obj", r);
        flower2R = new ReaderForObject("obj/flower/flower2.obj", r);
        flower3R = new ReaderForObject("obj/flower/flower3.obj", r);
        flower4R = new ReaderForObject("obj/flower/flower4.obj", r);
        grass1R = new ReaderForObject("obj/grass/grass1.obj", r);
        grass2R = new ReaderForObject("obj/grass/grass2.obj", r);

        cat_eatR = new ReaderForBnggdh("bnggdh/cat/cat_eat.bnggdh", r);
        cat_idleR = new ReaderForBnggdh("bnggdh/cat/cat_idle.bnggdh", r);
        cat_soundR = new ReaderForBnggdh("bnggdh/cat/cat_sound.bnggdh", r);
        cat_walkR = new ReaderForBnggdh("bnggdh/cat/cat_walk.bnggdh", r);
        treeR = new ReaderForBnggdh("bnggdh/tree/tree.bnggdh", r);

        water_tapR = new ReaderForBnggdh("obj/water_tap/water_tap.bnggdh", r);
        pipe1R = new ReaderForBnggdh("obj/water_tap/pipe1.bnggdh", r);
        pipe2R = new ReaderForBnggdh("obj/water_tap/pipe2.bnggdh", r);

        people_walkR = new ReaderForBnggdh("bnggdh/people/people_walk.bnggdh", r);
        people_idleR = new ReaderForBnggdh("bnggdh/people/people_idle.bnggdh", r);
        people_watering1R = new ReaderForBnggdh("bnggdh/people/people_watering1.bnggdh", r);
        people_watering2R = new ReaderForBnggdh("bnggdh/people/people_watering2.bnggdh", r);
    }

    // 加载数据
    public static void loadData() {
        // 加载纹理
        loadTexture();
        // 加载物体
        loadObject();
        // 创建GLDO
        newGLDO();
        // 创建GLDG
        newGLDG();
        // 计算GLDG
        calculateGLDG();
    }

    // 加载纹理
    private static void loadTexture() {
        // 初始化纹理管理器
        ManagerForTexture.init(r);
        // sky
        ManagerForTexture.addTexture("night", "img/sky_1/night.png");
        ManagerForTexture.addTexture("lightning", "img/sky_1/lightning.png");
        // color
        ManagerForTexture.addTexture("white", "img/color/white.png");
        // obj
        ManagerForTexture.addTexture("house", "obj/house/house.png");
        ManagerForTexture.addTexture("house_snowy", "obj/house/house_snowy.png");
        ManagerForTexture.addTexture("house_fix", "obj/house/house_fix.png");
        ManagerForTexture.addTexture("land", "obj/land/land.png");
        ManagerForTexture.addTexture("land_snowy", "obj/land/land_snowy.png");
        ManagerForTexture.addTexture("car", "obj/car/car.png");
        ManagerForTexture.addTexture("soil", "obj/soil/soil.png");
        ManagerForTexture.addTexture("soil_snowy", "obj/soil/soil_snowy.png");
        ManagerForTexture.addTexture("flower1", "obj/flower/flower1.jpg");
        ManagerForTexture.addTexture("flower2", "obj/flower/flower2.jpg");
        ManagerForTexture.addTexture("flower3", "obj/flower/flower3.jpg");
        ManagerForTexture.addTexture("flower4", "obj/flower/flower4.jpg");
        ManagerForTexture.addTexture("grass1", "obj/grass/grass1.png");
        ManagerForTexture.addTexture("grass2", "obj/grass/grass2.png");
        // particle
        ManagerForTexture.addTexture("rain", "img/particle/rain.png");
        ManagerForTexture.addTexture("snow", "img/particle/snow.png");
        // fireworks
        ManagerForTexture.addTexture("fireworks1", "img/fireworks/fireworks1.png");
        ManagerForTexture.addTexture("fireworks2", "img/fireworks/fireworks2.png");
        // bnggdh
        ManagerForTexture.addTexture("cat", "bnggdh/cat/cat.png");
        ManagerForTexture.addTexture("tree", "bnggdh/tree/tree.png");
        ManagerForTexture.addTexture("tree_snowy", "bnggdh/tree/tree_snowy.png");
        // sediment
        ManagerForTexture.addTexture("sediment", "img/sediment/sediment.png");
        // noise
        ManagerForTexture.addTexture("noise0", "img/noise/noise_0.png");
        ManagerForTexture.addTexture("noise1", "img/noise/noise_1.png");
        ManagerForTexture.addTexture("noise2", "img/noise/noise_2.png");
        ManagerForTexture.addTexture("noise3", "img/noise/noise_3.png");
        ManagerForTexture.addTexture("noise4", "img/noise/noise_4.png");
    }

    // 加载物体
    private static void loadObject() {
        // sky
        nightSky = new LoaderForNightSky(12.0f, 12.0f, r);
        lightingL = new LoaderForNightSky(12.0f, 12.0f, r);
        daySkyL = new LoaderForDaySky(12.0f, 12.0f, r);
        // particle
        rainL = new LoaderForParticle(
                4.0f, 2000, 32,
                new float[]{218 / 255f, 247 / 255f, 255 / 255f, 1.0f},
                new float[]{255 / 255f, 255 / 255f, 255 / 255f, 1.0f},
                new float[]{100, 600},
                new float[]{0f, 0f, 0f, 0f, -0.1f, 0f},
                new float[]{0f, 0f, 0f},
                r
        );
        snowL = new LoaderForParticle(
                4.0f, 2000, 24,
                new float[]{255 / 255f, 255 / 255f, 255 / 255f, 1.0f},
                new float[]{255 / 255f, 255 / 255f, 255 / 255f, 1.0f},
                new float[]{200, 1200},
                new float[]{0.006f, 0f, 0.006f, -0.006f, -0.02f, -0.006f},
                new float[]{0f, 0f, 0f},
                r
        );
        fountainL = new LoaderForParticle(
                0.001f, 3000, 3,
                new float[]{218 / 255f, 247 / 255f, 255 / 255f, 1.0f},
                new float[]{255 / 255f, 255 / 255f, 255 / 255f, 1.0f},
                new float[]{100, 600},
                new float[]{-0.005f, 0f, -0.01f, 0.020f, 0f, -0.05f},
                new float[]{0f, -0.0001f, 0f},
                r
        );
        // fireworks
        fireworks1L = new LoaderForFireworks1(
                0.05f, 300, 8f,
                new float[]{0f, 0f, 0f, 1.0f},
                new float[]{0f, 0f, 0f, 1.0f},
                new float[]{70f, 130f},
                0.5f,
                new float[]{0f, 0f, 0f},
                r
        );
        fireworks2L = new LoaderForFireworks2(r);
        // obj
        benchL = new LoaderForObject(benchR, r);
        houseL = new LoaderForObject(houseR, r);
        house_fixL = new LoaderForObject(house_fixR, r);
        landL = new LoaderForObject(landR, r);
        fenceL = new LoaderForObject(fenceR, r);
        waterL = new LoaderForObject(waterR, r);
        carL = new LoaderForObject(carR, r);
        soilL = new LoaderForObject(soilR, r);
        flower1L = new LoaderForObject(flower1R, r);
        flower2L = new LoaderForObject(flower2R, r);
        flower3L = new LoaderForObject(flower3R, r);
        flower4L = new LoaderForObject(flower4R, r);
        grass1L = new LoaderForObject(grass1R, r);
        grass2L = new LoaderForObject(grass2R, r);
        // bnggdh
        cat_eatL = new LoaderForBnggdh(cat_eatR, r);
        cat_eatL.setSpeed(0.005f);
        cat_eatL.setPlayFlag(true);

        cat_idleL = new LoaderForBnggdh(cat_idleR, r);
        cat_idleL.setSpeed(0.0025f);
        cat_idleL.setPlayFlag(true);

        cat_soundL = new LoaderForBnggdh(cat_soundR, r);
        cat_soundL.setSpeed(0.005f);
        cat_soundL.setPlayFlag(true);

        cat_walkL = new LoaderForBnggdh(cat_walkR, r);
        cat_walkL.setSpeed(0.025f);
        cat_walkL.setPlayFlag(true);

        treeL = new LoaderForBnggdh(treeR, r);
        treeL.setSpeed(0.003f);
        treeL.setPlayFlag(true);
        // sediment
        sedimentL = new LoaderForSediment(5f, 4f, 12, 12, r);
        //
        water_tapL = new LoaderForBnggdh(water_tapR, r);
        water_tapL.setSpeed(0.003f);
        water_tapL.setPlayFlag(false);
        //
        pipe1L = new LoaderForBnggdh(pipe1R, r);
        pipe1L.setSpeed(0.03f);
        pipe1L.setPlayFlag(false);

        pipe2L = new LoaderForBnggdh(pipe2R, r);
        pipe2L.setSpeed(0.03f);
        pipe2L.setPlayFlag(false);
        //
        people_walkL = new LoaderForBnggdh(people_walkR, r);
        people_walkL.setSpeed(0.06f);
        people_walkL.setPlayFlag(true);

        people_idleL = new LoaderForBnggdh(people_idleR, r);
        people_idleL.setSpeed(0.03f);
        people_idleL.setPlayFlag(true);

        people_watering1L = new LoaderForBnggdh(people_watering1R, r);
        people_watering1L.setSpeed(0.03f);
        people_watering1L.setPlayFlag(true);

        people_watering2L = new LoaderForBnggdh(people_watering2R, r);
        people_watering2L.setSpeed(0.03f);
        people_watering2L.setPlayFlag(true);
    }

    // 创建GLDO
    private static void newGLDO() {
        ////////////////// new sky //////////////////
        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(-4.5f, 4f, -10f);
        ManagerForMatrix.rotate(24f, 0f, 1f, 0f);
        nightSkyGLDO = new GLDrawableObject(nightSky, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("night"));
        daySkyL.setNoiseIDs(
                ManagerForTexture.getTexture("noise0"),
                ManagerForTexture.getTexture("noise1"),
                ManagerForTexture.getTexture("noise2"),
                ManagerForTexture.getTexture("noise3"),
                ManagerForTexture.getTexture("noise4")
        );
        daySkyGLDO = new GLDrawableObject(daySkyL, ManagerForMatrix.getMMatrix());

        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(0.2f, -1.0f, 5.0f);
        ManagerForMatrix.scale(0.5f, 0.5f, 0.5f);
        lightingGLDO = new GLDrawableObject(lightingL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("lightning"));
        ManagerForMatrix.popMatrix();

        ManagerForMatrix.popMatrix();

        lightingGLDO.setAction(new GLDrawableActionAdapter() {
            @Override
            public void drawStart(GLDrawableObject GLDO) {
                // 闪电时钟减1
                Constant.lightingClock--;

                if (Constant.lightingClock <= 10) {
                    // 设置闪电可见
                    GLDO.setVisible(true);
                    // 提高场景亮度
                    ManagerForLight.setLight1(0.6f, 0.6f, 0.6f, 1.0f);
                    ManagerForLight.setLight2(0.7f, 0.7f, 0.7f, 1.0f);
                    ManagerForLight.setLight3(0.5f, 0.5f, 0.5f, 1.0f);

                    if (Constant.lightingClock <= 1) {
                        // 恢复闪电时钟
                        Constant.lightingClock = Math.random() * 200;
                        // 恢复场景亮度
                        ManagerForLight.calculateLight();
                    }
                } else {
                    // 设置闪电不可见
                    GLDO.setVisible(false);
                }

            }
        });

        ////////////////// new obj bnggdh //////////////////
        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(0.4f, -1.3f, 0.0f);
        ManagerForMatrix.scale(0.2f, 0.2f, 0.2f);
        benchGLDO = new GLDrawableObject(benchL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("white"));
        houseGLDO = new GLDrawableObject(houseL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("house"));
        house_fixGLDO = new GLDrawableObject(house_fixL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("house_fix"));
        landGLDO = new GLDrawableObject(landL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("land"));
        treeGLDO = new GLDrawableObject(treeL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("tree"));
        fenceGLDO = new GLDrawableObject(fenceL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("white"));
        carGLDO = new GLDrawableObject(carL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("car"));
        soilGLDO = new GLDrawableObject(soilL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("soil"));
        flower1GLDO = new GLDrawableObject(flower1L, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("flower1"));
        flower2GLDO = new GLDrawableObject(flower2L, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("flower2"));
        flower3GLDO = new GLDrawableObject(flower3L, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("flower3"));
        flower4GLDO = new GLDrawableObject(flower4L, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("flower4"));
        grass1GLDO = new GLDrawableObject(grass1L, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("grass1"));
        grass2GLDO = new GLDrawableObject(grass2L, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("grass2"));
        ManagerForMatrix.popMatrix();

        benchGLDO.setDrawShadow(true);
        houseGLDO.setDrawShadow(true);
        house_fixGLDO.setDrawShadow(true);
        treeGLDO.setDrawShadow(true);
        fenceGLDO.setDrawShadow(true);
        carGLDO.setDrawShadow(true);
        soilGLDO.setDrawShadow(true);
        flower1GLDO.setDrawShadow(true);
        flower2GLDO.setDrawShadow(true);
        flower3GLDO.setDrawShadow(true);
        flower4GLDO.setDrawShadow(true);
        grass1GLDO.setDrawShadow(true);
        grass2GLDO.setDrawShadow(true);

        benchGLDO.setShadowColor(Constant.uniteShadowColor);
        houseGLDO.setShadowColor(Constant.uniteShadowColor);
        house_fixGLDO.setShadowColor(Constant.uniteShadowColor);
        treeGLDO.setShadowColor(Constant.uniteShadowColor);
        fenceGLDO.setShadowColor(Constant.uniteShadowColor);
        carGLDO.setShadowColor(Constant.uniteShadowColor);
        soilGLDO.setShadowColor(Constant.uniteShadowColor);
        flower1GLDO.setShadowColor(Constant.uniteShadowColor);
        flower2GLDO.setShadowColor(Constant.uniteShadowColor);
        flower3GLDO.setShadowColor(Constant.uniteShadowColor);
        flower4GLDO.setShadowColor(Constant.uniteShadowColor);
        grass1GLDO.setShadowColor(Constant.uniteShadowColor);
        grass2GLDO.setShadowColor(Constant.uniteShadowColor);

        landGLDO.setTouchable(true);
        landGLDO.setAction(new GLDrawableActionAdapter() {
            @Override
            public void beTouched(GLDrawableObject GLDO, float x, float y, float z) {
                Constant.peopleWatering = false;
                if (nowObject == catGLDO) {
                    catGLDO.setTarget(x, y, z);
                    catGLDO.setMove(true);
                    catGLDO.setRotate(true);
                } else if (nowObject == peopleGLDO) {
                    if (peopleGLDO.getGlDrawable() == people_idleL || peopleGLDO.getGlDrawable() == people_walkL) {
                        pipe1L.setNowTime(0);
                        pipe1L.setPlayFlag(false);
                        water_tapGLDO.setTouchable(true);
                        peopleGLDO.setTarget(x, y, z);
                        peopleGLDO.setMove(true);
                        peopleGLDO.setRotate(true);
                    }
                }
            }
        });
        ////////////////// new obj bnggdh //////////////////

        ////////////////// new sediment //////////////////
        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(3f, -1f, 3f);
        ManagerForMatrix.rotate(-90, 1, 0, 0);
        final float[] tempM2 = ManagerForMatrix.getMMatrix().clone();
        ManagerForMatrix.popMatrix();
        ////////////////// new sediment //////////////////

        ////////////////// new water //////////////////
        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(0.4f, -1.3f, 0.0f);
        ManagerForMatrix.scale(0.2f, 0.2f, 0.2f);
        final float[] tempM1 = ManagerForMatrix.getMMatrix().clone();
        ManagerForMatrix.popMatrix();

        waterGLDC = new GLDrawableCode() {
            @Override
            public void runCode() {
                GLES30.glEnable(GLES30.GL_STENCIL_TEST);                                    // 打开模板测试
                GLES30.glClear(GLES30.GL_STENCIL_BUFFER_BIT);                               // 清除模板测试缓冲
                GLES30.glStencilFunc(GLES30.GL_ALWAYS, 1, 1);                     // 设置参数
                GLES30.glStencilOp(GLES30.GL_KEEP, GLES30.GL_KEEP, GLES30.GL_REPLACE);      // 设置操作
                waterL.drawSelf(ManagerForTexture.getTexture("white"), tempM1, false, null); // 画水的形状
                GLES30.glStencilFunc(GLES30.GL_EQUAL, 1, 1);                      // 设置参数
                GLES30.glStencilOp(GLES30.GL_KEEP, GLES30.GL_KEEP, GLES30.GL_KEEP);         // 设置操作
                sedimentL.drawSelf(ManagerForTexture.getTexture("sediment"), tempM2, false, null); // 画倒影
                GLES30.glDisable(GLES30.GL_STENCIL_TEST);                                   // 关闭模板测试
            }
        };
        ////////////////// new water //////////////////

        ////////////////// new particle //////////////////
        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(2f, 10f, 2.5f);
        rainGLDO = new GLDrawableObject(rainL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("rain"));
        snowGLDO = new GLDrawableObject(snowL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("snow"));
        ManagerForMatrix.popMatrix();

        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(3.5f, -0.6f, 1.7f);
        fountainGLDO = new GLDrawableObject(fountainL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("snow"));
        fountainGLDO.setVisible(false);
        ManagerForMatrix.popMatrix();
        ////////////////// new particle //////////////////

        ////////////////// new fireworks //////////////////
        fireworks1GLDO = new GLDrawableObject(fireworks1L, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("fireworks1"));
        fireworks2GLDO = new GLDrawableObject(fireworks2L, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("fireworks2"));

        fireworks2GLDO.setAction(new GLDrawableActionAdapter() {
            @Override
            public void drawStart(GLDrawableObject GLDO) {
                GLES30.glDisable(GLES30.GL_DEPTH_TEST);
            }

            @Override
            public void drawEnd(GLDrawableObject GLDO) {
                GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            }
        });
        ////////////////// new fireworks //////////////////

        ////////////////// new bnggdh //////////////////
        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(2f, -1.05f, 3.5f);
        ManagerForMatrix.scale(0.003f, 0.003f, 0.003f);
        catGLDO = new GLDrawableObject(cat_idleL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("cat"));
        ManagerForMatrix.popMatrix();

        catGLDO.setDrawShadow(true);
        catGLDO.setShadowColor(Constant.uniteShadowColor);
        catGLDO.setTouchable(true);
        catGLDO.setMoveStep(0.005f);
        catGLDO.setRotateStep(0.2f);
        catGLDO.setAction(new GLDrawableActionAdapter() {
            @Override
            public void moveStart(GLDrawableObject GLDO) {
                cat_walkL.setNowTime(0);
                GLDO.setGlDrawable(cat_walkL);
            }

            @Override
            public void moveEnd(GLDrawableObject GLDO) {
                cat_eatL.setNowTime(0);
                GLDO.setGlDrawable(cat_eatL);
                Constant.catStatus = 0;
            }

            @Override
            public void drawStart(GLDrawableObject GLDO) {
                if (GLDO.getGlDrawable() == cat_eatL) {
                    if (Constant.catStatus > 400) {
                        cat_soundL.setNowTime(0);
                        GLDO.setGlDrawable(cat_soundL);
                        Constant.catStatus = 500;
                    } else {
                        Constant.catStatus++;
                    }
                } else if (GLDO.getGlDrawable() == cat_soundL) {
                    if (Constant.catStatus > 900) {
                        cat_idleL.setNowTime(0);
                        GLDO.setGlDrawable(cat_idleL);
                        Constant.catStatus = 0;
                    } else {
                        Constant.catStatus++;
                    }
                }
            }

            @Override
            public void beTouched(GLDrawableObject GLDO, float x, float y, float z) {
                nowObject = GLDO;
            }
        });
        ////////////////// new bnggdh //////////////////

        ////////////////// new bnggdh //////////////////
        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(2.0f, -0.8f, 3.0f);
        ManagerForMatrix.scale(0.008f, 0.008f, 0.008f);
        peopleGLDO = new GLDrawableObject(people_idleL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("white"));
        peopleGLDO.setDrawShadow(true);
        ManagerForMatrix.popMatrix();

        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(3.7f, -0.8f, 1.9f);
        ManagerForMatrix.rotate(180, 0, 1, 0);
        ManagerForMatrix.scale(0.008f, 0.008f, 0.008f);
        peopleMatrix = ManagerForMatrix.getMMatrix().clone();
        ManagerForMatrix.popMatrix();

        ManagerForMatrix.pushMatrix();
        ManagerForMatrix.translate(3.2f, -0.8f, 1.7f);
        ManagerForMatrix.scale(0.008f, 0.008f, 0.008f);
        pipeGLDO = new GLDrawableObject(pipe1L, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("white"));
        water_tapGLDO = new GLDrawableObject(water_tapL, ManagerForMatrix.getMMatrix(), ManagerForTexture.getTexture("white"));
        ManagerForMatrix.popMatrix();

        pipeGLDO.setDrawShadow(true);
        water_tapGLDO.setDrawShadow(true);
        pipeGLDO.setShadowColor(Constant.uniteShadowColor);
        water_tapGLDO.setShadowColor(Constant.uniteShadowColor);

        water_tapGLDO.setTouchable(true);
        water_tapGLDO.setAction(new GLDrawableActionAdapter() {
            @Override
            public void beTouched(GLDrawableObject GLDO, float x, float y, float z) {
                if (nowObject == peopleGLDO) {
                    nowObject.setTarget(3.5f, -0.8f, 1.9f);
                    nowObject.setMove(true);
                    nowObject.setRotate(true);
                    Constant.peopleWatering = true;
                }
            }
        });

        peopleGLDO.setDrawShadow(true);
        peopleGLDO.setShadowColor(Constant.uniteShadowColor);
        peopleGLDO.setTouchable(true);
        peopleGLDO.setMoveStep(0.005f);
        peopleGLDO.setRotateStep(0.2f);
        peopleGLDO.setAction(new GLDrawableActionAdapter() {
            @Override
            public void moveStart(GLDrawableObject GLDO) {
                people_walkL.setNowTime(0);
                GLDO.setGlDrawable(people_walkL);
            }

            @Override
            public void moveEnd(GLDrawableObject GLDO) {
                if (Constant.peopleWatering) {
                    GLDO.setSelfMatrix(peopleMatrix);
                    pipe1L.setNowTime(0);
                    pipe1L.setPlayFlag(true);
                    pipeGLDO.setGlDrawable(pipe1L);

                    people_watering1L.setNowTime(0);
                    people_watering1L.setPlayFlag(true);
                    GLDO.setGlDrawable(people_watering1L);
                } else {
                    people_idleL.setNowTime(0);
                    GLDO.setGlDrawable(people_idleL);
                }
            }

            @Override
            public void drawStart(GLDrawableObject GLDO) {

                int p1MT = (int) (1 / people_watering1L.getSpeed()) - 1;
                int p2MT = (int) (1 / people_watering2L.getSpeed()) - 2;

                if (GLDO.getGlDrawable() == people_watering1L) {
                    water_tapGLDO.setTouchable(false);
                    if (Constant.peopleStatus == p1MT) {
                        people_watering1L.setPlayFlag(false);
                        pipe1L.setPlayFlag(false);
                        fountainGLDO.setVisible(true);
                        Constant.peopleStatus++;
                    } else {
                        Constant.peopleStatus++;
                        if (Constant.peopleStatus > p1MT + 1 + Constant.wateringTime) {
                            people_watering2L.setNowTime(0);
                            peopleGLDO.setGlDrawable(people_watering2L);
                            pipe2L.setNowTime(0);
                            pipe2L.setPlayFlag(true);
                            pipeGLDO.setGlDrawable(pipe2L);
                            fountainGLDO.setVisible(false);
                        }
                    }
                } else if (GLDO.getGlDrawable() == people_watering2L) {
                    if (Constant.peopleStatus > p1MT + 1 + Constant.wateringTime + 1 + p2MT) {
                        peopleGLDO.setGlDrawable(people_idleL);
                        pipe2L.setPlayFlag(false);
                        Constant.peopleStatus = 0;
                        water_tapGLDO.setTouchable(true);
                        Constant.peopleWatering = false;
                    } else {
                        Constant.peopleStatus++;
                    }
                }
            }

            @Override
            public void beTouched(GLDrawableObject GLDO, float x, float y, float z) {
                nowObject = GLDO;
            }
        });
    }

    // 创建GLDG
    private static void newGLDG() {
        // cloudyGLDG
        cloudyGLDG = new GLDrawableGroup();
        cloudyGLDG.add(nightSkyGLDO);
        cloudyGLDG.add(daySkyGLDO);
        cloudyGLDG.add(benchGLDO);
        cloudyGLDG.add(houseGLDO);
        cloudyGLDG.add(house_fixGLDO);
        cloudyGLDG.add(landGLDO);
        cloudyGLDG.add(treeGLDO);
        cloudyGLDG.add(fenceGLDO);
        cloudyGLDG.add(carGLDO);
        cloudyGLDG.add(soilGLDO);
        cloudyGLDG.add(flower1GLDO);
        cloudyGLDG.add(flower2GLDO);
        cloudyGLDG.add(flower3GLDO);
        cloudyGLDG.add(flower4GLDO);
        cloudyGLDG.add(grass1GLDO);
        cloudyGLDG.add(grass2GLDO);
        cloudyGLDG.add(catGLDO);
        cloudyGLDG.add(fireworks1GLDO);
        cloudyGLDG.add(fireworks2GLDO);
        cloudyGLDG.add(peopleGLDO);
        cloudyGLDG.add(water_tapGLDO);
        cloudyGLDG.add(pipeGLDO);
        cloudyGLDG.add(fountainGLDO);
        // sunnyGLDG
        sunnyGLDG = new GLDrawableGroup();
        sunnyGLDG.add(nightSkyGLDO);
        sunnyGLDG.add(daySkyGLDO);
        sunnyGLDG.add(benchGLDO);
        sunnyGLDG.add(houseGLDO);
        sunnyGLDG.add(house_fixGLDO);
        sunnyGLDG.add(landGLDO);
        sunnyGLDG.add(treeGLDO);
        sunnyGLDG.add(fenceGLDO);
        sunnyGLDG.add(carGLDO);
        sunnyGLDG.add(soilGLDO);
        sunnyGLDG.add(flower1GLDO);
        sunnyGLDG.add(flower2GLDO);
        sunnyGLDG.add(flower3GLDO);
        sunnyGLDG.add(flower4GLDO);
        sunnyGLDG.add(grass1GLDO);
        sunnyGLDG.add(grass2GLDO);
        sunnyGLDG.add(catGLDO);
        sunnyGLDG.add(fireworks1GLDO);
        sunnyGLDG.add(fireworks2GLDO);
        sunnyGLDG.add(peopleGLDO);
        sunnyGLDG.add(water_tapGLDO);
        sunnyGLDG.add(pipeGLDO);
        sunnyGLDG.add(fountainGLDO);
        // rainyGLDG
        rainyGLDG = new GLDrawableGroup();
        rainyGLDG.add(nightSkyGLDO);
        rainyGLDG.add(daySkyGLDO);
        rainyGLDG.add(benchGLDO);
        rainyGLDG.add(houseGLDO);
        rainyGLDG.add(house_fixGLDO);
        rainyGLDG.add(landGLDO);
        rainyGLDG.add(treeGLDO);
        rainyGLDG.add(fenceGLDO);
        rainyGLDG.add(carGLDO);
        rainyGLDG.add(soilGLDO);
        rainyGLDG.add(flower1GLDO);
        rainyGLDG.add(flower2GLDO);
        rainyGLDG.add(flower3GLDO);
        rainyGLDG.add(flower4GLDO);
        rainyGLDG.add(grass1GLDO);
        rainyGLDG.add(grass2GLDO);
        rainyGLDG.add(rainGLDO);
        rainyGLDG.add(waterGLDC);
        rainyGLDG.add(fireworks1GLDO);
        rainyGLDG.add(fireworks2GLDO);
        rainyGLDG.add(water_tapGLDO);
        rainyGLDG.add(pipeGLDO);
        // snowyGLDG
        snowyGLDG = new GLDrawableGroup();
        snowyGLDG.add(nightSkyGLDO);
        snowyGLDG.add(daySkyGLDO);
        snowyGLDG.add(benchGLDO);
        snowyGLDG.add(houseGLDO);
        snowyGLDG.add(house_fixGLDO);
        snowyGLDG.add(landGLDO);
        snowyGLDG.add(treeGLDO);
        snowyGLDG.add(fenceGLDO);
        snowyGLDG.add(carGLDO);
        snowyGLDG.add(soilGLDO);
        snowyGLDG.add(flower1GLDO);
        snowyGLDG.add(flower2GLDO);
        snowyGLDG.add(flower3GLDO);
        snowyGLDG.add(flower4GLDO);
        snowyGLDG.add(grass1GLDO);
        snowyGLDG.add(grass2GLDO);
        snowyGLDG.add(snowGLDO);
        snowyGLDG.add(catGLDO);
        snowyGLDG.add(fireworks1GLDO);
        snowyGLDG.add(fireworks2GLDO);
        snowyGLDG.add(water_tapGLDO);
        snowyGLDG.add(pipeGLDO);
        // lightningGLDG
        lightningGLDG = new GLDrawableGroup();
        lightningGLDG.add(nightSkyGLDO);
        lightningGLDG.add(daySkyGLDO);
        lightningGLDG.add(benchGLDO);
        lightningGLDG.add(houseGLDO);
        lightningGLDG.add(house_fixGLDO);
        lightningGLDG.add(landGLDO);
        lightningGLDG.add(treeGLDO);
        lightningGLDG.add(fenceGLDO);
        lightningGLDG.add(carGLDO);
        lightningGLDG.add(soilGLDO);
        lightningGLDG.add(flower1GLDO);
        lightningGLDG.add(flower2GLDO);
        lightningGLDG.add(flower3GLDO);
        lightningGLDG.add(flower4GLDO);
        lightningGLDG.add(grass1GLDO);
        lightningGLDG.add(grass2GLDO);
        lightningGLDG.add(rainGLDO);
        lightningGLDG.add(waterGLDC);
        lightningGLDG.add(lightingGLDO);
        lightningGLDG.add(fireworks1GLDO);
        lightningGLDG.add(fireworks2GLDO);
        lightningGLDG.add(water_tapGLDO);
        lightningGLDG.add(pipeGLDO);
        // sedimentGLDG
        sedimentGLDG = new GLDrawableGroup();
        sedimentGLDG.add(nightSkyGLDO);
        sedimentGLDG.add(daySkyGLDO);
        sedimentGLDG.add(benchGLDO);
        sedimentGLDG.add(houseGLDO);
        sedimentGLDG.add(house_fixGLDO);
        sedimentGLDG.add(treeGLDO);
        sedimentGLDG.add(rainGLDO);
        sedimentGLDG.add(water_tapGLDO);
        sedimentGLDG.add(pipeGLDO);
    }

    // 计算物体绘制组
    public static void calculateGLDG() {
        switch (Constant.weather) {
            case 0: // 阴
                houseGLDO.setTextureID(ManagerForTexture.getTexture("house"));
                landGLDO.setTextureID(ManagerForTexture.getTexture("land"));
                treeGLDO.setTextureID(ManagerForTexture.getTexture("tree"));
                soilGLDO.setTextureID(ManagerForTexture.getTexture("soil"));

                Constant.uniteShadowColor[0] = 0.0f;
                Constant.uniteShadowColor[1] = 0.58f;
                Constant.uniteShadowColor[2] = 0.0f;

                nightSkyGLDO.setDrawable(false);
                daySkyL.updateSky();
                daySkyGLDO.setDrawable(true);
                nowGLDG = cloudyGLDG;
                break;
            case 1: // 晴
                houseGLDO.setTextureID(ManagerForTexture.getTexture("house"));
                landGLDO.setTextureID(ManagerForTexture.getTexture("land"));
                treeGLDO.setTextureID(ManagerForTexture.getTexture("tree"));
                soilGLDO.setTextureID(ManagerForTexture.getTexture("soil"));

                Constant.uniteShadowColor[0] = 0.0f;
                Constant.uniteShadowColor[1] = 0.58f;
                Constant.uniteShadowColor[2] = 0.0f;

                nightSkyGLDO.setDrawable(false);
                daySkyL.updateSky();
                daySkyGLDO.setDrawable(true);
                nowGLDG = sunnyGLDG;
                break;
            case 2: // 雨
                houseGLDO.setTextureID(ManagerForTexture.getTexture("house"));
                landGLDO.setTextureID(ManagerForTexture.getTexture("land"));
                treeGLDO.setTextureID(ManagerForTexture.getTexture("tree"));
                soilGLDO.setTextureID(ManagerForTexture.getTexture("soil"));

                Constant.uniteShadowColor[0] = 0.0f;
                Constant.uniteShadowColor[1] = 0.58f;
                Constant.uniteShadowColor[2] = 0.0f;

                nightSkyGLDO.setDrawable(false);
                daySkyL.updateSky();
                daySkyGLDO.setDrawable(true);
                nowGLDG = rainyGLDG;
                nowSedimentGLDG = sedimentGLDG;
                break;
            case 3: // 雪
                houseGLDO.setTextureID(ManagerForTexture.getTexture("house_snowy"));
                landGLDO.setTextureID(ManagerForTexture.getTexture("land_snowy"));
                treeGLDO.setTextureID(ManagerForTexture.getTexture("tree_snowy"));
                soilGLDO.setTextureID(ManagerForTexture.getTexture("soil_snowy"));

                Constant.uniteShadowColor[0] = 0.58f;
                Constant.uniteShadowColor[1] = 0.58f;
                Constant.uniteShadowColor[2] = 0.58f;

                nightSkyGLDO.setDrawable(false);
                daySkyL.updateSky();
                daySkyGLDO.setDrawable(true);
                nowGLDG = snowyGLDG;
                break;
            case 4: // 电
                houseGLDO.setTextureID(ManagerForTexture.getTexture("house"));
                landGLDO.setTextureID(ManagerForTexture.getTexture("land"));
                treeGLDO.setTextureID(ManagerForTexture.getTexture("tree"));
                soilGLDO.setTextureID(ManagerForTexture.getTexture("soil"));

                Constant.uniteShadowColor[0] = 0.0f;
                Constant.uniteShadowColor[1] = 0.58f;
                Constant.uniteShadowColor[2] = 0.0f;

                nightSkyGLDO.setDrawable(false);
                daySkyL.updateSky();
                daySkyGLDO.setDrawable(true);
                nowGLDG = lightningGLDG;
                nowSedimentGLDG = sedimentGLDG;
                break;
        }

        if (!ManagerForLight.isDaytime()) {
            nightSkyGLDO.setDrawable(true);
            nightSkyGLDO.setTextureID(ManagerForTexture.getTexture("night"));
            daySkyGLDO.setDrawable(false);
        }
    }

    // 获取现在的GLDG
    public static GLDrawableGroup getNowGLDG() {
        return nowGLDG;
    }

    public static GLDrawableGroup getNowSedimentGLDG() {
        return nowSedimentGLDG;
    }
}
