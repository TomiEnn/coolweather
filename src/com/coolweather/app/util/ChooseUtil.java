package com.coolweather.app.util;

import android.content.Context;
import android.text.TextUtils;

/**
 * ƥ������ͼƬ�Ĺ�����
 * 
 * @author Tomi_Enn
 * 
 */
public class ChooseUtil {

	public String chooseWeatherImage(Context context, String weather,
			String temp, String wind, String pm) {
		String result = "";
		if (!TextUtils.isEmpty(weather)) {
			if (weather.equals("��")) {
				return result = "jqwy_normal";
			}
			if (weather.equals("����")) {
				return result = "oyzy_normal";
			}
		}
		return result = "jqwy_normal";
	}
}
