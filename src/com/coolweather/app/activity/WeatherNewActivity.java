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
	 * ˫�򻬶��˵�����
	 */
	private BidirSlidingLayout bidirSldingLayout;
	private RelativeLayout rightMenu;
	private RelativeLayout leftMenu;
	private ImageView addCity;
	private RelativeLayout rightMenuShowWeather;
	/**
	 * ��������Ϣ
	 */
	private TextView wnTextInfo;
	/**
	 * ��ߺ��������
	 */
	private TextView wnTextTempAll;
	/**
	 * PMֵ
	 */
	private TextView wnTextPm;
	/**
	 * PM�ȼ�
	 */
	private String wnPmLevel;
	/**
	 * PM��ֵ
	 */
	private int pm;
	/**
	 * ����
	 */
	private TextView wnTextWind;
	/**
	 * ��ǿ
	 */
	private String wnWindStrength;
	/**
	 * ����ʪ��
	 */
	private TextView wnTextHumidity;
	/**
	 * �ճ�������ʱ��
	 */
	private TextView wnTextSunTime;
	private String sunUpTime;
	private String sunDownTiem;

	/**
	 * ʵʱ�¶�
	 */
	private TextView wnTextTempReal;
	/**
	 * PMֵ��ͼƬ
	 */
	private ImageView wnImagePm;
	/**
	 * ������ͼƬ
	 */
	private ImageView wnImageWind;
	/**
	 * ����ʪ�ȵ�ͼƬ
	 */
	private ImageView wnImageHumidity;
	/**
	 * ����ʱ���ͼƬ
	 */
	private ImageView wnImageSuntime;
	/**
	 * ������Ϣ��ͼƬ
	 */
	private ImageView wnImageInfo;
	/**
	 * �Ƿ��˳��ı�ʾ
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
		// ʵ�����ؼ�
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
		// ���һ���
		bidirSldingLayout.setScrollEvent(scrollView);
		// ����ѡ��õĳ��еĴ��ţ�����ʾ����
		String countyWeatherCode = getIntent().getStringExtra(
				"county_weather_code");
		currentWeatherCode = "101280101";
		if (!TextUtils.isEmpty(countyWeatherCode)) {
			// ��ѯʵʱ������ַ��δ�������������ַ
			queryRealWeatherAddress(countyWeatherCode);
			queryFutureWeatherAddress(countyWeatherCode);
		} else {
			// û�д��ž���ʾ���е�
			showFutureWeather();
			showRealWeather();
			LogUtil.i("coolweaher", "û�г��д���response fail");
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
		LogUtil.i("coolweaher", "weatherNewActivity queryRealWeatherAddress: "
				+ address);
		queryFromServer2("real", address);
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
		LogUtil.i("coolweaher",
				"weatherNewActivity queryFutureWeatherAddress: " + address);
		queryFromServer2("future", address);
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
					// Toast.makeText(MyApplication.getContext(), "ʵʱ���������쳣",
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
					// Toast.makeText(WeatherNewActivity.this, "δ�����������쳣",
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
		//�������
		String wind = prefs.getString("realWind", "");
		String windStrength = prefs.getString("realWindStrength", "");
		if(!TextUtils.isEmpty(windStrength)){
			wnTextWind.setText(wind + windStrength);
		}else{
			wnTextWind.setText(wind + "��");
		}
		//����ʪ��
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
		weather = prefs.getString("weather1", "");// ����
		temp = prefs.getString("temp1", "");// �������¶�
		pmValues = prefs.getString("pm", "");// PMֵ
		LogUtil.i("coolweahter", " weather : "+weather +" temp : "+prefs.getString("temp1", "") +" pmValues : "+prefs.getString("pm", "") );
		String sunRise = prefs.getString("rc1", "");// �ճ�ʱ��
		String sunSet = prefs.getString("rl1", "");// ����ʱ��
		// ��ʾ������Ϣ����
		if (!TextUtils.isEmpty(weather)) {
			wnTextInfo.setText(weather);
		} else {
			wnTextInfo.setText("None");
		}
		// ��ʾȫ���������
		if (!TextUtils.isEmpty(temp)) {
			wnTextTempAll.setText(temp);
		} else {
			wnTextTempAll.setText("None");
		}
		// ��ʾ�ճ����䣺�Ȼ�ȡϵͳ��ǰʱ�䣬Ȼ���ڶԱ�
		Time t = new Time();
		t.setToNow();
		int currentTime = t.hour;// 0-23
		String up[] = sunRise.split(":");
		String down[] = sunSet.split(":");
		LogUtil.i("coolweahter", "up[0]ʱ�䣺 " + up[0] + "down[0]ʱ�䣺 " + down[0]
				+ "��ǰʱ�䣺 " + currentTime);
		if (currentTime > Integer.parseInt(up[0])
				&& currentTime < (Integer.parseInt(down[0]) + 2)) {
			wnImageSuntime.setImageResource(R.drawable.sunset);
			wnTextSunTime.setText(prefs.getString("rl1", ""));
		} else {
			wnImageSuntime.setImageResource(R.drawable.sunrise);
			wnTextSunTime.setText(prefs.getString("rc1", ""));
		}
		// ����PMֵ��ͼƬ
		if (!TextUtils.isEmpty(pmValues)) {

			pm = Integer.parseInt(pmValues);
			wnPmLevel = prefs.getString("pmLevel", "");
			LogUtil.i("coolweahter", "Pmֵ������� " + pm + wnPmLevel);
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
	 * ˫���˳�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Timer timer;
			if (!isExit) {
				isExit = true;
				Toast.makeText(this, "�ٰ�һ���˳�", Toast.LENGTH_SHORT).show();
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
