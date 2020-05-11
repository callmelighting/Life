package bn.wallpaper.llz;

public class ManagerForLight {
    private static final float MORNING = 6;              // 白天起始时间点(白天不包括此值)
    private static final float NIGHT = 20;               // 白天终止时间点(白天不包括此值)
    private static final float DAY = NIGHT - MORNING;    // 白天时间段长度
    private static final float NOON = DAY / 2 + MORNING; // 白天中午时间点

    private static final float XMIN = -30; // 光线X轴最小值
    private static final float YMIN = 15;  // 光线Y轴最小值
    private static final float XMAX = 30;  // 光线X轴最大值
    private static final float YMAX = 30;  // 光线Y轴最大值

    private static float[] lightDirection = new float[3]; // 光线方向
    private static float[] light1 = new float[4];         // 环境光强度
    private static float[] light2 = new float[4];         // 散射光强度
    private static float[] light3 = new float[4];         // 镜面光强度

    // 计算灯光
    public static void calculateLight() {
        if (isDaytime()) {
            // 计算光线方向
            setLightDirection(
                    (Constant.time - MORNING) / DAY * (XMAX - XMIN) + XMIN,
                    YMAX - (Math.abs(Constant.time - NOON) / (DAY / 2) * (YMAX - YMIN)),
                    30.0f
            );
            // 计算三种光的强度
            float tempLight1 = 0.6f - ((Math.abs(Constant.time - NOON) / DAY) * (0.6f - 0.3f));
            float tempLight2 = 0.7f - ((Math.abs(Constant.time - NOON) / DAY) * (0.7f - 0.4f));
            float tempLight3 = 0.5f - ((Math.abs(Constant.time - NOON) / DAY) * (0.5f - 0.2f));
            // 根据天气调整光线强度
            if (Constant.weather == 0) {        // 阴
                tempLight1 *= 0.8f;
                tempLight2 *= 0.8f;
                tempLight3 *= 0.8f;
            } else if (Constant.weather == 1) { // 晴
                tempLight1 *= 0.9f;
                tempLight2 *= 0.9f;
                tempLight3 *= 0.9f;
            } else if (Constant.weather == 2) { // 雨
                tempLight1 *= 0.7f;
                tempLight2 *= 0.7f;
                tempLight3 *= 0.7f;
            } else if (Constant.weather == 3) { // 雪
                tempLight1 *= 0.95f;
                tempLight2 *= 0.95f;
                tempLight3 *= 0.95f;
            } else if (Constant.weather == 4) { // 电
                tempLight1 *= 0.7f;
                tempLight2 *= 0.7f;
                tempLight3 *= 0.7f;
            }
            setLight1(tempLight1, tempLight1, tempLight1, 1.0f);
            setLight2(tempLight2, tempLight2, tempLight2, 1.0f);
            setLight3(tempLight3, tempLight3, tempLight3, 1.0f);
        } else {
            // 计算光线方向
            setLightDirection(20.0f, 20.0f, 30.0f);
            // 计算三种光的强度
            setLight1(0.2f, 0.2f, 0.2f, 1.0f);
            setLight2(0.3f, 0.3f, 0.3f, 1.0f);
            setLight3(0.1f, 0.1f, 0.1f, 1.0f);
        }
    }

    // 判断现在是白天还是晚上
    public static boolean isDaytime() {
        return !(Constant.time >= NIGHT) && !(Constant.time <= MORNING);
    }

    // 设置光线方向
    public static void setLightDirection(float x, float y, float z) {
        lightDirection[0] = x;
        lightDirection[1] = y;
        lightDirection[2] = z;
    }

    // 设置环境光强度
    public static void setLight1(float r, float g, float b, float a) {
        light1[0] = r;
        light1[1] = g;
        light1[2] = b;
        light1[3] = a;
    }

    // 设置散射光强度
    public static void setLight2(float r, float g, float b, float a) {
        light2[0] = r;
        light2[1] = g;
        light2[2] = b;
        light2[3] = a;
    }

    // 设置镜面光强度
    public static void setLight3(float r, float g, float b, float a) {
        light3[0] = r;
        light3[1] = g;
        light3[2] = b;
        light3[3] = a;
    }

    // 获得光线方向
    public static float[] getLightDirection() {
        return lightDirection;
    }

    // 获得环境光强度
    public static float[] getLight1() {
        return light1;
    }

    // 获得散射光强度
    public static float[] getLight2() {
        return light2;
    }

    // 获得镜面光强度
    public static float[] getLight3() {
        return light3;
    }
}
