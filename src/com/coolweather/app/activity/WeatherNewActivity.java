package com.coolweather.app.activity;

import java.util.Timer;
import java.util.TimerTask;

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
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherNewActivity extends Activity {
	/** 
     * 双向滑动菜单布局 
     */  
    private BidirSlidingLayout bidirSldingLayout;  
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
	private int pm ;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.slide_layout);
		// 实例化控件
		bidirSldingLayout = (BidirSlidingLayout) findViewById(R.id.bidir_sliding_layout); 
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
		//左右滑动
		bidirSldingLayout.setScrollEvent(scrollView);
		// 接收选择好的城市的代号，以显示天气
		String countyWeatherCode = getIntent().getStringExtra(
				"county_weather_code");
		if (!TextUtils.isEmpty(countyWeatherCode)) {
			// 查询实时天气地址和未来几天的天气地址
			queryRealWeatherAddress(countyWeatherCode);
			queryFutureWeatherAddress(countyWeatherCode);
		} else {
			// 没有代号就显示现有的
			showFutureWeather();
			showRealWeather();
			LogUtil.i("coolweaher", "response fail");
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
		queryFromServer("real", address);
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
		queryFromServer("future", address);
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

				}
			});
		}

	}

	private void showRealWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		wnTextTempReal.setText(prefs.getString("realTemp", ""));
		wnWindStrength = prefs.getString("realWindStrength", "");
		wnTextWind.setText(prefs.getString("realWind", "") + wnWindStrength);
		wnTextHumidity.setText(prefs.getString("realHumidity", ""));
		// wnTextPulishTime.setText(prefs.getString("realPublishTime", ""));

	}

	private void showFutureWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		wnTextInfo.setText(prefs.getString("weather1", ""));
		wnTextTempAll.setText(prefs.getString("temp1", ""));
		sunUpTime = prefs.getString("rc1", "");
		sunDownTiem = prefs.getString("rl1", "");
		LogUtil.i("coolweahter", "日出时间： " + sunUpTime);
		String up[] = sunUpTime.split(":");
		LogUtil.i("coolweahter", "日出时间： " + up[0] + Integer.parseInt(up[0]));
		if (Integer.parseInt(up[0]) < 12 && Integer.parseInt(up[0]) > 0) {
			wnImageSuntime.setImageResource(R.drawable.sunrise);
			wnTextSunTime.setText(prefs.getString("rc1", ""));
		} else {
			wnImageSuntime.setImageResource(R.drawable.sunset);
			wnTextSunTime.setText(prefs.getString("rl1", ""));
		}
		// 设置PM值的图片
		
		if(!TextUtils.isEmpty(prefs.getString("pm", ""))){
			
		pm = Integer.parseInt(prefs.getString("pm", ""));
		wnPmLevel = prefs.getString("pmLevel", "");
		LogUtil.i("coolweahter", "Pm： " + pm + wnPmLevel);
		if (pm < 40) {
			wnImagePm.setImageResource(R.drawable.pm25_1);
			wnTextPm.setText("PM：" + pm);
		} else {
			wnImagePm.setImageResource(R.drawable.pm25_2);
			wnTextPm.setText("PM：" + pm);
		}
		}else{
			wnImagePm.setImageResource(R.drawable.pm25_2);
			wnTextPm.setText("PM：?" );
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
				finish();
			}
		}
		return false;
	}
}
