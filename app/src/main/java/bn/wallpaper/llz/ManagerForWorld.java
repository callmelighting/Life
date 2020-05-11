package bn.wallpaper.llz;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;
import java.util.Calendar;

public class ManagerForWorld {
    private static MyThread th;
    private static Calendar calendar = Calendar.getInstance();

    // 刷新天气和时间
    public static void refreshWeatherAndTime(Context context) {
        th = new MyThread();
        // 声明AMapLocationClient类对象
        AMapLocationClient mLocationClient;
        // 声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        th.cityName = amapLocation.getAdCode(); // 城市编码
                        while (th.cityName != null) {
                            th.start();
                        }
                    } else {
                        // 定位失败时, 可通过ErrCode(错误码)信息来确定失败的原因, errInfo是错误信息, 详见错误码表.
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
        // 初始化定位
        mLocationClient = new AMapLocationClient(context);
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        // 声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption;
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为AMapLocationMode.Hight_Accuracy, 高精度模式.
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息(默认返回地址信息)
        mLocationOption.setNeedAddress(true);
        // 设置是否允许模拟位置, 默认为true, 允许模拟位置.
        mLocationOption.setMockEnable(true);
        // 单位是毫秒, 默认30000毫秒, 建议超时时间不要低于8000毫秒.
        mLocationOption.setHttpTimeOut(20000);
        // 关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }

    // 网络申请获得json天气
    private static String getJSonstr(String cityName) throws Exception {
        String jsonStr = "";
        StringBuilder URLLocation;
        String URL;
        URL url;
        URLConnection uc;

        URLLocation = new StringBuilder("https://restapi.amap.com/v3/weather/weatherInfo?city=");
        URLLocation.append(cityName);
        URLLocation.append("&key=7dae1a04b989eadce549b71e1ce65181");

        URL = new String(URLLocation.toString().getBytes());
        url = new URL(URL);

        uc = url.openConnection();
        uc.addRequestProperty("accept-language", "zh_CN");

        try {
            InputStream in = uc.getInputStream();
            int ch;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            byte[] bb = baos.toByteArray();
            baos.close();
            in.close();
            jsonStr = new String(bb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    // 解析json获得天气
    private static String parseJSon(String jSonStr) throws JSONException {
        String weather;
        JSONObject jo1 = new JSONObject(jSonStr);
        JSONArray ja = jo1.getJSONArray("lives");
        JSONObject jo2 = new JSONObject(ja.getString(0));
        weather = jo2.getString("weather");
        return weather;
    }

    // 更新数据内部线程
    private static class MyThread extends Thread {
        private static final String TAG = "MyThread";
        private String cityName;

        public void run() {
            try {
                String cityData = ManagerForWorld.getJSonstr(cityName);
                String weather = ManagerForWorld.parseJSon(cityData);
                Constant.weather = Constant.convertWeatherToId(weather);
                Constant.time = calendar.get(Calendar.HOUR_OF_DAY);
                ManagerForLight.calculateLight();
                ManagerForData.calculateGLDG();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

