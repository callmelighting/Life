package bn.wallpaper.llz;

// 程序常量
public class Constant {
    // 闪电时钟
    public static double lightingClock = Math.random() * 200.0;
    // 天气ID (阴晴雨雪电)
    public static int weather = 2;
    // 时间ID
    public static float time = 19f;
    // 统一阴影颜色
    public static float[] uniteShadowColor = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
    // 场景ID, 默认为第1幅场景
    public static int sceneID = 1;
    // 场景时间是否需要更新标志位
    public static boolean timeUpdate = true;
    // 猫咪状态累计值
    public static int catStatus = 0;
    // 人类状态累计值
    public static float peopleStatus = 0;
    // 人类浇水时间
    public static final float wateringTime = 60;
    // 人类是否在浇水标志位
    public static boolean peopleWatering = false;

    ////////////////////////视口数据/////////////////////////////
    public static float V_WIDTH;  // GLSurfaceView的宽
    public static float V_HEIGHT; // GLSurfaceView的高
    public static float V_RIGHT;  // 近平面RIGHT
    public static float V_TOP;    // 近平面TOP
    public static float V_NEAR;   // 近平面与视点的距离
    public static float V_FAR;    // 远平面与视点的距离
    public static float V_RATIO;  // GLSurfaceView的宽高比
    ////////////////////////视口数据/////////////////////////////

    // 天气数组0, 包含在此数组内的天气均被视为阴天
    private static final String[] weatherArrays0 = new String[]{
            "阴", "浮尘", "扬沙", "沙尘暴",
            "强沙尘暴", "龙卷风", "雾", "浓雾",
            "强浓雾", "轻雾", "大雾", "特强浓雾",
            "霾", "中度霾", "重度霾", "严重霾"
    };

    // 天气数组1, 包含在此数组内的天气均被视为晴天
    private static final String[] weatherArrays1 = new String[]{
            "晴", "少云", "晴间多云", "多云",
            "有风", "平静", "微风", "和风",
            "清风", "强风/劲风", "疾风", "大风",
            "烈风", "风暴", "狂爆风", "飓风",
            "热带风暴", "热", "未知"
    };

    // 天气数组2, 包含在此数组内的天气均被视为雨天
    private static final String[] weatherArrays2 = new String[]{
            "雨", "阵雨", "小雨", "中雨",
            "大雨", "强阵雨", "毛毛雨/细雨", "小雨-中雨",
            "中雨-大雨", "雨雪天气", "雨夹雪", "阵雨夹雪", "冻雨"
    };

    // 天气数组3, 包含在此数组内的天气均被视为雪天
    private static final String[] weatherArrays3 = new String[]{
            "雪", "阵雪", "小雪", "中雪",
            "大雪", "暴雪", "小雪-中雪", "中雪-大雪",
            "大雪-暴雪", "冷"
    };

    // 天气数组4, 包含在此数组内的天气均被视为雷雨天
    private static final String[] weatherArrays4 = new String[]{
            "暴雨", "大暴雨", "特大暴雨", "大雨-暴雨",
            "暴雨-大暴雨", "大暴雨-特大暴雨", "雷阵雨", "雷阵雨并伴有冰雹",
            "强雷阵雨", "极端降雨"
    };

    // 将从网络端获取的天气名称转换为我们规定的天气ID
    public static int convertWeatherToId(String name) {
        // 遍历天气数组0
        for (String aWeatherArrays0 : weatherArrays0) {
            // 如果天气数据属于数组0则返回0
            if (aWeatherArrays0.equals(name)) {
                return 0;
            }
        }

        for (String aWeatherArrays1 : weatherArrays1) {
            if (aWeatherArrays1.equals(name)) {
                return 1;
            }
        }

        for (String aWeatherArrays2 : weatherArrays2) {
            if (aWeatherArrays2.equals(name)) {
                return 2;
            }
        }

        for (String aWeatherArrays3 : weatherArrays3) {
            if (aWeatherArrays3.equals(name)) {
                return 3;
            }
        }

        for (String aWeatherArrays4 : weatherArrays4) {
            if (aWeatherArrays4.equals(name)) {
                return 4;
            }
        }

        // 默认返回0
        return 0;
    }
}
