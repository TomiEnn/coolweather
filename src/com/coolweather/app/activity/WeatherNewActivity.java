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
     * ˫�򻬶��˵����� 
     */  
    private BidirSlidingLayout bidirSldingLayout;  
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
	private int pm ;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.slide_layout);
		// ʵ�����ؼ�
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
		//���һ���
		bidirSldingLayout.setScrollEvent(scrollView);
		// ����ѡ��õĳ��еĴ��ţ�����ʾ����
		String countyWeatherCode = getIntent().getStringExtra(
				"county_weather_code");
		if (!TextUtils.isEmpty(countyWeatherCode)) {
			// ��ѯʵʱ������ַ��δ�������������ַ
			queryRealWeatherAddress(countyWeatherCode);
			queryFutureWeatherAddress(countyWeatherCode);
		} else {
			// û�д��ž���ʾ���е�
			showFutureWeather();
			showRealWeather();
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
		LogUtil.i("coolweaher", "weatherNewActivity queryRealWeatherAddress: "
				+ address);
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
		LogUtil.i("coolweaher",
				"weatherNewActivity queryFutureWeatherAddress: " + address);
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
		LogUtil.i("coolweahter", "�ճ�ʱ�䣺 " + sunUpTime);
		String up[] = sunUpTime.split(":");
		LogUtil.i("coolweahter", "�ճ�ʱ�䣺 " + up[0] + Integer.parseInt(up[0]));
		if (Integer.parseInt(up[0]) < 12 && Integer.parseInt(up[0]) > 0) {
			wnImageSuntime.setImageResource(R.drawable.sunrise);
			wnTextSunTime.setText(prefs.getString("rc1", ""));
		} else {
			wnImageSuntime.setImageResource(R.drawable.sunset);
			wnTextSunTime.setText(prefs.getString("rl1", ""));
		}
		// ����PMֵ��ͼƬ
		
		if(!TextUtils.isEmpty(prefs.getString("pm", ""))){
			
		pm = Integer.parseInt(prefs.getString("pm", ""));
		wnPmLevel = prefs.getString("pmLevel", "");
		LogUtil.i("coolweahter", "Pm�� " + pm + wnPmLevel);
		if (pm < 40) {
			wnImagePm.setImageResource(R.drawable.pm25_1);
			wnTextPm.setText("PM��" + pm);
		} else {
			wnImagePm.setImageResource(R.drawable.pm25_2);
			wnTextPm.setText("PM��" + pm);
		}
		}else{
			wnImagePm.setImageResource(R.drawable.pm25_2);
			wnTextPm.setText("PM��?" );
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
				finish();
			}
		}
		return false;
	}
}
