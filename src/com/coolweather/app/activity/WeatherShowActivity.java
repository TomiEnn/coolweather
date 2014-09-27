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
		// ʵ�����ؼ�
		weatherToday = (TextView) findViewById(R.id.weather_main_temp_today);
		weatherAllday = (TextView) findViewById(R.id.weather_main_allday);
		weatherInfo = (TextView) findViewById(R.id.weather_main_info);
		weatherLocal = (TextView) findViewById(R.id.weather_main_local);
		weatherPulishTime = (TextView) findViewById(R.id.weather_main_time);
		weatherDate = (TextView) findViewById(R.id.weather_main_date);
		// weatherImage = (ImageView) findViewById(R.id.weather_main_image);
		weatherRefresh = (ImageView) findViewById(R.id.weather_main_refresh);
	//	listView = (ListView) findViewById(R.id.weather_main_listview);
		// ����ѡ��õĳ��еĴ��ţ�����ʾ����
		String countyWeatherCode = getIntent().getStringExtra(
				"county_weather_code");
		if (!TextUtils.isEmpty(countyWeatherCode)) {
			// ��ѯʵʱ������ַ��δ�������������ַ
			queryRealWeatherAddress(countyWeatherCode);
			queryFutureWeatherAddress(countyWeatherCode);
		} else {
			// showWeather();
			LogUtil.i("coolweaher", "response fail");
		}
	}

	/**
	 * ������������ת��ʵʱ������ַ
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
	 * ������������ת��δ�������������ַ
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
	 * ��ѯ����
	 * 
	 * @param address
	 *            �����ĵ�ַ
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
					// �ص����̸߳���UI
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
					// �ص����̸߳���UI
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
	 * ��ʾʵʱ����
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
	 * ��ʾδ������
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
