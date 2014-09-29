package com.coolweather.app.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.coolweather.app.MyApplication;
import com.coolweather.app.R;
import com.coolweather.app.util.BidirSlidingLayout;
import com.coolweather.app.util.HttpCallBackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.LogUtil;
import com.coolweather.app.util.Utility;

import android.R.integer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherNewActivity extends Activity {
	/**
	 * 双向滑动菜单布局
	 */
	private BidirSlidingLayout bidirSldingLayout;
	private RelativeLayout rightMenu;
	private RelativeLayout leftMenu;
	private ImageView addCity;
	private RelativeLayout rightMenuShowWeather;
	/**
	 * 天气的信息
	 */
	private TextView wnTextInfo;
	/**
	 * 最高和最低气温
	 */
	private TextView wnTextTempAll;
	/**
	 * PM值
	 */
	private TextView wnTextPm;
	/**
	 * PM等级
	 */
	private String wnPmLevel;
	/**
	 * PM数值
	 */
	private int pm;
	/**
	 * 风力
	 */
	private TextView wnTextWind;
	/**
	 * 风强
	 */
	private String wnWindStrength;
	/**
	 * 空气湿度
	 */
	private TextView wnTextHumidity;
	/**
	 * 日出或日落时间
	 */
	private TextView wnTextSunTime;
	private String sunUpTime;
	private String sunDownTiem;

	/**
	 * 实时温度
	 */
	private TextView wnTextTempReal;
	/**
	 * PM值得图片
	 */
	private ImageView wnImagePm;
	/**
	 * 风力的图片
	 */
	private ImageView wnImageWind;
	/**
	 * 空气湿度的图片
	 */
	private ImageView wnImageHumidity;
	/**
	 * 发布时间的图片
	 */
	private ImageView wnImageSuntime;
	/**
	 * 天气信息的图片
	 */
	private ImageView wnImageInfo;
	/**
	 * 是否退出的标示
	 */
	private boolean isExit = false;
	private ScrollView scrollView;
	private String currentWeatherCode;
	private String weather;
	private String temp;
	private String pmValues;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide_layout);
		// 实例化控件
		bidirSldingLayout = (BidirSlidingLayout) findViewById(R.id.bidir_sliding_layout);
		rightMenu = (RelativeLayout) findViewById(R.id.right_menu);
		leftMenu = (RelativeLayout) findViewById(R.id.left_menu);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		wnTextInfo = (TextView) findViewById(R.id.new_text_1);
		wnTextTempAll = (TextView) findViewById(R.id.new_text_down_2);
		wnTextTempReal = (TextView) findViewById(R.id.new_text_up_2);
		wnTextPm = (TextView) findViewById(R.id.new_text_down_3);
		wnTextWind = (TextView) findViewById(R.id.new_text_4);
		wnTextHumidity = (TextView) findViewById(R.id.new_text_5);
		wnTextSunTime = (TextView) findViewById(R.id.new_text_6);
		wnImageInfo = (ImageView) findViewById(R.id.new_image_1);
		wnImagePm = (ImageView) findViewById(R.id.new_image_3);
		wnImageWind = (ImageView) findViewById(R.id.new_image_4);
		wnImageHumidity = (ImageView) findViewById(R.id.new_image_5);
		wnImageSuntime = (ImageView) findViewById(R.id.new_image_6);
		// 左右滑动
		bidirSldingLayout.setScrollEvent(scrollView);
		// 接收选择好的城市的代号，以显示天气
		String countyWeatherCode = getIntent().getStringExtra(
				"county_weather_code");
		currentWeatherCode = "101280101";
		if (!TextUtils.isEmpty(countyWeatherCode)) {
			// 查询实时天气地址和未来几天的天气地址
			queryRealWeatherAddress(countyWeatherCode);
			queryFutureWeatherAddress(countyWeatherCode);
		} else {
			// 没有代号就显示现有的
			showFutureWeather();
			showRealWeather();
			LogUtil.i("coolweaher", "没有城市代号response fail");
		}

	}

	/**
	 * 根据天气代号转换实时天气地址
	 * 
	 * @param countyWeatherCode
	 */
	private void queryRealWeatherAddress(String countyWeatherCode) {
		// TODO Auto-generated method stub
		String address = "http://www.weather.com.cn/data/sk/"
				+ countyWeatherCode + ".html";
		LogUtil.i("coolweaher", "weatherNewActivity queryRealWeatherAddress: "
				+ address);
		queryFromServer2("real", address);
	}

	/**
	 * 根据天气代号转换未来几天的天气地址
	 * 
	 * @param countyWeatherCode
	 */
	private void queryFutureWeatherAddress(String countyWeatherCode) {
		// TODO Auto-generated method stub
		String address = "http://weather.123.duba.net/static/weather_info/"
				+ countyWeatherCode + ".html";
		LogUtil.i("coolweaher",
				"weatherNewActivity queryFutureWeatherAddress: " + address);
		queryFromServer2("future", address);
	}

	/**
	 * 查询天气
	 * 
	 * @param address
	 *            天气的地址
	 */
	private void queryFromServer(String type, String address) {
		// TODO Auto-generated method stub
		if (type.equals("real")) {
			HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {

				@Override
				public void onFinish(String response) {
					// TODO Auto-generated method stub
					Utility.handleWeatherRealResponse(getApplicationContext(),
							response);
					// 回到主线程跟新UI
					runOnUiThread(new Runnable() {
						public void run() {
							showRealWeather();

						}

					});
				}

				@Override
				public void onError(Exception e) {
					// TODO Auto-generated method stub
					// Toast.makeText(MyApplication.getContext(), "实时天气更新异常",
					// Toast.LENGTH_SHORT).show();
				}
			});

		}
		if (type.equals("future")) {

			HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {

				@Override
				public void onFinish(String response) {
					// TODO Auto-generated method stub
					Utility.handleWeatherFutureResponse(
							getApplicationContext(), response);
					// 回到主线程跟新UI
					runOnUiThread(new Runnable() {
						public void run() {
							showFutureWeather();
						}

					});
				}

				@Override
				public void onError(Exception e) {
					// TODO Auto-generated method stub
					// Toast.makeText(WeatherNewActivity.this, "未来天气更新异常",
					// Toast.LENGTH_SHORT).show();

				}
			});
		}

	}

	private Handler handler1 = new Handler() {

		public void handleMessage(android.os.Message msg) {

			LogUtil.i("coolweather", "handler1: " + msg.obj.toString());
			Utility.handleWeatherRealResponse(WeatherNewActivity.this,
					msg.obj.toString());
			showRealWeather();

		};
	};
	private Handler handler2 = new Handler() {

		public void handleMessage(android.os.Message msg) {
			LogUtil.i("coolweather", "handler2: future" + msg.obj.toString());
			Utility.handleWeatherFutureResponse(WeatherNewActivity.this,
					msg.obj.toString());
			showFutureWeather();
		};
	};

	private void queryFromServer2(String type, String address) {
		// TODO Auto-generated method stub

		if (type.equals("real")) {

			HttpUtil.sendHttpRequest2(address, handler1);

		}
		if (type.equals("future")) {

			HttpUtil.sendHttpRequest2(address, handler2);
		}
	}

	private void showRealWeather() {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		//风力情况
		String wind = prefs.getString("realWind", "");
		String windStrength = prefs.getString("realWindStrength", "");
		if(!TextUtils.isEmpty(windStrength)){
			wnTextWind.setText(wind + windStrength);
		}else{
			wnTextWind.setText(wind + "风");
		}
		//空气湿度
		String humidity = prefs.getString("realHumidity", "");
		if(!TextUtils.isEmpty(humidity)){
			String temp = humidity.replace("%", "");
			if(Integer.parseInt(temp) < 40){
				wnImageHumidity.setImageResource(R.drawable.humidity_40);
			}else{
				wnImageHumidity.setImageResource(R.drawable.humidity_70);
			}
			wnTextHumidity.setText(humidity);
		}else{
			wnTextHumidity.setText("None");
			wnImageHumidity.setImageResource(R.drawable.humidity_00);
		}

	}

	private void showFutureWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WeatherNewActivity.this);
		weather = prefs.getString("weather1", "");// 天气
		temp = prefs.getString("temp1", "");// 最高最低温度
		pmValues = prefs.getString("pm", "");// PM值
		LogUtil.i("coolweahter", " weather : "+weather +" temp : "+prefs.getString("temp1", "") +" pmValues : "+prefs.getString("pm", "") );
		String sunRise = prefs.getString("rc1", "");// 日出时间
		String sunSet = prefs.getString("rl1", "");// 日落时间
		// 显示天气信息布局
		if (!TextUtils.isEmpty(weather)) {
			wnTextInfo.setText(weather);
		} else {
			wnTextInfo.setText("None");
		}
		// 显示全天气温情况
		if (!TextUtils.isEmpty(temp)) {
			wnTextTempAll.setText(temp);
		} else {
			wnTextTempAll.setText("None");
		}
		// 显示日出日落：先获取系统当前时间，然后在对比
		Time t = new Time();
		t.setToNow();
		int currentTime = t.hour;// 0-23
		String up[] = sunRise.split(":");
		String down[] = sunSet.split(":");
		LogUtil.i("coolweahter", "up[0]时间： " + up[0] + "down[0]时间： " + down[0]
				+ "当前时间： " + currentTime);
		if (currentTime > Integer.parseInt(up[0])
				&& currentTime < (Integer.parseInt(down[0]) + 2)) {
			wnImageSuntime.setImageResource(R.drawable.sunset);
			wnTextSunTime.setText(prefs.getString("rl1", ""));
		} else {
			wnImageSuntime.setImageResource(R.drawable.sunrise);
			wnTextSunTime.setText(prefs.getString("rc1", ""));
		}
		// 设置PM值的图片
		if (!TextUtils.isEmpty(pmValues)) {

			pm = Integer.parseInt(pmValues);
			wnPmLevel = prefs.getString("pmLevel", "");
			LogUtil.i("coolweahter", "Pm值和情况： " + pm + wnPmLevel);
			if (pm <= 50) {
				wnImagePm.setImageResource(R.drawable.pm25_1);
				wnTextPm.setText(wnPmLevel + pm);
			}
			if (pm > 51 && pm <= 100) {
				wnImagePm.setImageResource(R.drawable.pm25_2);
				wnTextPm.setText(wnPmLevel + pm);
			}
			if (pm > 101) {
				wnImagePm.setImageResource(R.drawable.pm25_3);
				wnTextPm.setText(wnPmLevel + pm);
			}
		} else {
			wnImagePm.setImageResource(R.drawable.munion_user_icon);
			wnTextPm.setText("None");
		}

	}

	/**
	 * 双击退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Timer timer;
			if (!isExit) {
				isExit = true;
				Toast.makeText(this, "再按一下退出", Toast.LENGTH_SHORT).show();
				timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						isExit = false;
					}
				}, 1500);
			} else {

				System.exit(0);
			}
		}
		return false;
	}
}
