package com.coolweather.app.util;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

/**
 * ����ʡ�������ݵĹ�����
 * 
 * @author Tomi_Enn
 * 
 */
public class Utility {
	/**
	 * �����ʹ�����������ص�ʡ������
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
	 * �����м����ص�����
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
						LogUtil.i("coolweather","handleCityResponse():");
						if (nodeName.equalsIgnoreCase("city")) {
							City city = new City();
							//����city������
							city.setCityName(xmlPullParser.getAttributeValue(
									null, "cityname"));
							//����city��url
							city.setCityUrl(Integer.parseInt(xmlPullParser.getAttributeValue(
									null, "url")));
							//����city��pyName
							city.setCityPy(xmlPullParser.getAttributeValue(
									null, "pyName"));
							//����city��provincePy
							city.setProvincePy(provincePy);
							LogUtil.i("coolweather","handleCityResponse(): "+provincePy);
							 coolWeatherDB.saveCity(city);
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
	 * �����ؼ����ص�����
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
							//����county������
							county.setCountyName(xmlPullParser.getAttributeValue(
									null, "cityname"));
							//����county��url
							county.setCountyUrl(Integer.parseInt(xmlPullParser.getAttributeValue(
									null, "url")));
							//����county��countyName
							county.setCityPy(xmlPullParser.getAttributeValue(
									null, "pyName"));
							//����county��CountyPy
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
}
