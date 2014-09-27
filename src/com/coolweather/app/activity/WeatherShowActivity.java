package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallBackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.LogUtil;
import com.coolweather.app.util.Utility;

import android.R.layout;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WeatherShowActivity extends Activity {

	private TextView weatherToday;
	private TextView weatherAllday;
	private TextView weatherInfo;
	private TextView weatherLocal;
	private TextView weatherPulishTime;
	private ImageView weatherImage;
	private ImageView weatherRefresh;
	private TextView weatherDate;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_main);
		// 实例化控件
		weatherToday = (TextView) findViewById(R.id.weather_main_temp_today);
		weatherAllday = (TextView) findViewById(R.id.weather_main_allday);
		weatherInfo = (TextView) findViewById(R.id.weather_main_info);
		weatherLocal = (TextView) findViewById(R.id.weather_main_local);
		weatherPulishTime = (TextView) findViewById(R.id.weather_main_time);
		weatherDate = (TextView) findViewById(R.id.weather_main_date);
		// weatherImage = (ImageView) findViewById(R.id.weather_main_image);
		weatherRefresh = (ImageView) findViewById(R.id.weather_main_refresh);
	//	listView = (ListView) findViewById(R.id.weather_main_listview);
		// 接收选择好的城市的代号，以显示天气
		String countyWeatherCode = getIntent().getStringExtra(
				"county_weather_code");
		if (!TextUtils.isEmpty(countyWeatherCode)) {
			// 查询实时天气地址和未来几天的天气地址
			queryRealWeatherAddress(countyWeatherCode);
			queryFutureWeatherAddress(countyWeatherCode);
		} else {
			// showWeather();
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
		LogUtil.i("coolweaher", "queryRealWeatherAddress: " + address);
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
		LogUtil.i("coolweaher", "queryFutureWeatherAddress: " + address);
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

	/**
	 * 显示实时天气
	 */
	private void showRealWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		weatherToday.setText(prefs.getString("realtemp", ""));
		weatherLocal.setText(prefs.getString("realcity", ""));
		weatherPulishTime.setText(prefs.getString("realtime", ""));

	}

	/**
	 * 显示未来天气
	 */
	private void showFutureWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		LogUtil.i("coolweather", "showFutureWeather: "+prefs.getString("temp1", ""));
		weatherAllday.setText(prefs.getString("temp1", ""));
		weatherInfo.setText(prefs.getString("weather1", ""));
		LogUtil.i("coolweather", "showFutureWeather: "+prefs.getString("week", ""));
		weatherDate.setText(prefs.getString("week", ""));
		LogUtil.i("coolweather",
				"weatehrInfo: " + prefs.getString("weather1", ""));
	}
}
