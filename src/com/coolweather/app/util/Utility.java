package com.coolweather.app.util;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

/**
 * 解析省市县数据的工具类
 * 
 * @author Tomi_Enn
 * 
 */
public class Utility {
	/**
	 * 解析和处理服务器返回的省级数据
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleProvinceResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String nodeName = xmlPullParser.getName();
						if (nodeName.equalsIgnoreCase("city")) {
							Province province = new Province();
							province.setProvinceQuName(xmlPullParser
									.getAttributeValue(null, "quName"));
							province.setProvincePyName(xmlPullParser
									.getAttributeValue(null, "pyName"));
							coolWeatherDB.saveProvince(province);
						}

					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 解析市级返回的数据
	 * @param coolWeatherDB
	 * @param response
	 * @param provincePy
	 * @return
	 */
	public synchronized static boolean handleCityResponse(
			CoolWeatherDB coolWeatherDB, String response, String provincePy) {
		if (!TextUtils.isEmpty(response)) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String nodeName = xmlPullParser.getName();
						if (nodeName.equalsIgnoreCase("city")) {
							City city = new City();
							//设置city的名字
							city.setCityName(xmlPullParser.getAttributeValue(
									null, "cityname"));
							//设置city的url
							city.setCityUrl(Integer.parseInt(xmlPullParser.getAttributeValue(
									null, "url")));
							//设置city的pyName
							city.setCityPy(xmlPullParser.getAttributeValue(
									null, "pyName"));
							//设置city的provincePy
							city.setProvincePy(provincePy);
							 coolWeatherDB.saveCity(city);
						}
						break;
						case XmlPullParser.END_TAG:
							String endName = xmlPullParser.getName();
					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 解析县级返回的数据
	 * @param coolWeatherDB
	 * @param response
	 * @param cityPy
	 * @return
	 */
	public synchronized static boolean handleCountyResponse(
			CoolWeatherDB coolWeatherDB, String response, String cityPy) {
		if (!TextUtils.isEmpty(response)) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String nodeName = xmlPullParser.getName();
						if (nodeName.equalsIgnoreCase("city")) {
							County county = new County();
							//设置county的名字
							county.setCountyName(xmlPullParser.getAttributeValue(
									null, "cityname"));
							//设置county的url
							county.setCountyUrl(Integer.parseInt(xmlPullParser.getAttributeValue(
									null, "url")));
							//设置county的countyName
							county.setCityPy(xmlPullParser.getAttributeValue(
									null, "pyName"));
							//设置county的CountyPy
							county.setCityPy(cityPy);
							 coolWeatherDB.saveCounty(county);
						}
						break;
						case XmlPullParser.END_TAG:
							String endName = xmlPullParser.getName();
							System.out.println("***********endName: "+endName);
					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
	 */
	public static void handleWeatherResponse(Context context, String response){
		try{
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String  cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferen文件中
	 * @param context
	 * @param cityName
	 * @param weatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param publishTime
	 */
	public static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		// TODO Auto-generated method stub
		SimpleDateFormat date = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weahter_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", date.format(new Date()));
		editor.commit();
	}
	
	public static void handleWeatherFutureResponse(Context context, String response){
		if(!TextUtils.isEmpty(response)){
		try{
			response = response.replace("weather_callback(", "");
			response = response.replace(")", "");
			LogUtil.i("coolweather", "Uility weatherFutureResponse: "+response);
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String  cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			LogUtil.i("coolweather", "Uility weatherResponse: "+temp1);
			String temp2 = weatherInfo.getString("temp2");
			String temp3 = weatherInfo.getString("temp3");
			String temp4 = weatherInfo.getString("temp4");
			String temp5 = weatherInfo.getString("temp5");
			String temp6 = weatherInfo.getString("temp6");
			String weather1 = weatherInfo.getString("weather1");
			String weather2 = weatherInfo.getString("weather2");
			String weather3 = weatherInfo.getString("weather3");
			String weather4 = weatherInfo.getString("weather4");
			String weather5 = weatherInfo.getString("weather5");
			String weather6 = weatherInfo.getString("weather6");
			String pmLevel = weatherInfo.getString("pm-level");
			String pm = weatherInfo.getString("pm");
			LogUtil.i("coolweather", "Uility PM值: "+pm);
			String rc1 = weatherInfo.getString("rc1");
			String rl1 = weatherInfo.getString("rl1");
			String week = weatherInfo.getString("week");
			//存入到SharePreference里
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putString("city", cityName);
			editor.putString("cityid", weatherCode);
			editor.putString("temp1", temp1);
			editor.putString("temp2", temp2);
			editor.putString("temp3", temp3);
			editor.putString("temp4", temp4);
			editor.putString("temp5", temp5);
			editor.putString("temp6", temp6);
			editor.putString("weather1", weather1);
			editor.putString("weather2", weather2);
			editor.putString("weather3", weather3);
			editor.putString("weather4", weather4);
			editor.putString("weather5", weather5);
			editor.putString("weather6", weather6);
			editor.putString("pm", pm);
			editor.putString("rc1", rc1);
			editor.putString("rl1", rl1);
			editor.putString("pmLevel", pmLevel);
			editor.putString("week", week);
			editor.commit();
		}catch(Exception e){
			Toast.makeText(context, "网络错误，请重试", Toast.LENGTH_SHORT).show();
		}
	}
	}
	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
	 */
	public static void handleWeatherRealResponse(Context context, String response){
		try{
			JSONObject jsonObject = new JSONObject(response);
			LogUtil.i("coolweather", "Uility weatherRealResponse: "+response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String realTemp = weatherInfo.getString("temp");
			String realWind = weatherInfo.getString("WD");
			String realWindStrength = weatherInfo.getString("WS");
			String realHumidity = weatherInfo.getString("SD");
			String realPublishTime = weatherInfo.getString("time");
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putString("realTemp", realTemp);
			editor.putString("realWind", realWind);
			editor.putString("realWindStrength", realWindStrength);
			editor.putString("realHumidity", realHumidity);
			editor.putString("realPublishTime", realPublishTime);
			editor.commit();
		}catch(Exception e){
			Toast.makeText(context, "网络错误，请重试", Toast.LENGTH_SHORT).show();
			
		}
	}
	
	
}
