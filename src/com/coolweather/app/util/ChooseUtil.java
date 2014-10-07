package com.coolweather.app.util;

import android.content.Context;
import android.text.TextUtils;

/**
 * 匹配天气图片的工具类
 * 
 * @author Tomi_Enn
 * 
 */
public class ChooseUtil {

	public String chooseWeatherImage(Context context, String weather,
			String temp, String wind, String pm) {
		String result = "";
		if (!TextUtils.isEmpty(weather)) {
			if (weather.equals("晴")) {
				return result = "jqwy_normal";
			}
			if (weather.equals("阵雨")) {
				return result = "oyzy_normal";
			}
		}
		return result = "jqwy_normal";
	}
}
