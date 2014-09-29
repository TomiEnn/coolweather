package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.coolweather.app.MyApplication;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.Toast;
/**
 * 从网络获取数据的工具类
 * @author Tomi_Enn
 *
 */
public class HttpUtil {
	public static boolean isConnected = false;

	public static void sendHttpRequest(final String adress,
			final HttpCallBackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
				HttpURLConnection connection = null;
				try {
					URL url = new URL(adress);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					int statusCode = connection.getResponseCode();
					if(statusCode == HttpURLConnection.HTTP_OK){
						/*isConnected = true;
						editor.putBoolean("isConnected", isConnected);*/
						while ((line = reader.readLine()) != null) {
							response.append(line);
						}
					}else{
						Toast.makeText(MyApplication.getContext(), "网络异常...", Toast.LENGTH_SHORT).show();
					}
					
					if (listener != null) {
						LogUtil.i("coolweather", "htttputil: listener done");
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();

	}
	public static void sendHttpRequest2(final String adress,
			final Handler handler) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
				HttpURLConnection connection = null;
				try {
					URL url = new URL(adress);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					int statusCode = connection.getResponseCode();
					if(statusCode == HttpURLConnection.HTTP_OK){
						/*isConnected = true;
						editor.putBoolean("isConnected", isConnected);*/
						while ((line = reader.readLine()) != null) {
							response.append(line);
							String result = response.toString();
							Message message = new Message();
							message.obj = result;
						
							handler.sendMessage(message);
						}
					}else{
						Toast.makeText(MyApplication.getContext(), "网络异常...", Toast.LENGTH_SHORT).show();
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();

	}

}
