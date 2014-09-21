package com.coolweather.app.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;

public class MainActivity extends Activity {

	private Province province;
	private List<Province> provinces;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		sandRequestWithHttpClient();

	}

	private void sandRequestWithHttpClient() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet htpGet = new HttpGet(
							"http://flash.weather.com.cn/wmaps/xml/heilongjiang.xml");
					HttpResponse httpResponse = httpClient.execute(htpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity, "utf-8");
						System.out.println("------------response: " + response);
						//parseXMLWithPull(response);
						boolean asd = handleCityResponse(response);
						LogUtil.i("syso", asd + "");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void parseXMLWithPull(String response) {
		// TODO Auto-generated method stub
		provinces = new ArrayList<Province>();
		province = null;
		try {
			XmlPullParser xmlPullParser = Xml.newPullParser();
			xmlPullParser.setInput(new StringReader(response));
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					String nodeName = xmlPullParser.getName();
					if (nodeName.equalsIgnoreCase("city")) {
						province = new Province();
						province.setProvinceQuName(xmlPullParser
								.getAttributeValue(null, "quName"));
						province.setProvincePyName(xmlPullParser
								.getAttributeValue(null, "pyName"));
						System.out.println("---------quName and pyName : "
								+ province.getProvinceQuName() + "and"
								+ province.getProvincePyName());
						provinces.add(province);
					}
					break;

				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// return provinces;

	}

	public synchronized static boolean handleCityResponse(String response) {
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
						System.out.println("***********nodeName: "+nodeName);
						if (nodeName.equalsIgnoreCase("city")) {
							City city = new City();
							city.setCityName(xmlPullParser.getAttributeValue(
									null, "cityname"));
							city.setCityUrl(Integer.parseInt(xmlPullParser.getAttributeValue(
									null, "url")));
							System.out.println("**************city: "+xmlPullParser.getAttributeValue(
									null, "cityname"));
							LogUtil.i("syso", xmlPullParser.getAttributeValue(
									null, "cityname"));
							LogUtil.i("syso", xmlPullParser.getAttributeValue(
									null, "url"));
							// coolWeatherDB.saveCity(city);
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