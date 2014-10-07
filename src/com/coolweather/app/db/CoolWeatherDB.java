package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.ItemWeather;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 常用的数据库操作
 * 
 * @author Tomi_Enn
 * 
 */
public class CoolWeatherDB {

	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "cool_weather";

	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;

	private static CoolWeatherDB coolWeatherDB;

	private SQLiteDatabase db;

	/**
	 * 将构造方法私有化
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
		LogUtil.i("coolweather", "CoolWeatherOpenHelper oncreate");
	}

	/**
	 * 获取Coolweather实例
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		LogUtil.i("coolweather", "CoolWeatherDB getInstance");
		return coolWeatherDB;
	}

	/**
	 * 将Province实例存储到数据库
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_quname", province.getProvinceQuName());
			values.put("province_pyname", province.getProvincePyName());
			db.insert("Province", null, values);
		}
	}

	/**
	 * 从数据库读取Province的省份信息
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		//Cursor cursor = db.query("Province", null, null, null, null, null, null);
		Cursor cursor = db.rawQuery("select * from Province", null);
		LogUtil.i("coolweather", "start loadProvinces ");
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId((cursor.getInt(cursor.getColumnIndex("_id"))));
				province.setProvincePyName((cursor.getString(cursor
						.getColumnIndex("province_pyname"))));
				province.setProvinceQuName((cursor.getString(cursor
						.getColumnIndex("province_quname"))));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;

	}

	/**
	 * 将City实例存数到数据库
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_py", city.getCityPy());
			values.put("city_url", city.getCityUrl());
			values.put("province_code", city.getProvincePy());
			db.insert("City", null, values);

		}
	}

	/**
	 * 从数据库读取某省下的城市信息
	 */
	public List<City> loudCities(String provinceCode) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_code = ?",
				new String[] {provinceCode}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityUrl(Integer.parseInt(cursor.getString(cursor
						.getColumnIndex("city_url"))));
				city.setCityPy(cursor.getString(cursor.getColumnIndex("city_py")));
				city.setProvincePy((provinceCode));
				list.add(city);
			} while (cursor.moveToNext());

		}
		return list;
	}

	/**
	 * 将county实例存储到数据库
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_url", county.getCountyUrl());
			values.put("city_code", county.getCityPy());
			db.insert("County", null, values);
		}
	}

	/**
	 * 从数据库读取某城市的所有县信息
	 */
	public List<County> loudCounties(String cityCode) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_code = ?",
				new String[] {cityCode}, null, null, null);

		if(cursor.moveToFirst()){
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name"	)));
				county.setCountyUrl(cursor.getInt(cursor.getColumnIndex("county_url")));
				county.setCityPy(cityCode);
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;

	}
	/**
	 * 将itemWeather实例存储到数据库
	 */
	public void saveItemWeather(ItemWeather weather) {
		if (weather != null) {
			ContentValues values = new ContentValues();
			values.put("name", weather.getLocal());
			values.put("weather", weather.getWeather());
			values.put("temp", weather.getTemp());
			db.insert("ItemWeatehr", null, values);
		}
	}
	/**
	 * 从itemWeather中删除数据
	 */
	public void deleteItemWeather(String cityCode){
		if(cityCode != null){
			db.execSQL("delete from ItemWeather where city_code = ?", new String[]{"cityCode"});
		}
	}

	/**
	 * 从数据库读取某城市的所有天气信息
	 */
	public List<ItemWeather> loudItemWeatehr(String cityCode) {
		List<ItemWeather> list = new ArrayList<ItemWeather>();
		Cursor cursor = db.query("ItemWeather", null, "city_code = ?",
				new String[] {cityCode}, null, null, null);

		if(cursor.moveToFirst()){
			do {
				ItemWeather itemWeather = new ItemWeather();
				itemWeather.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				itemWeather.setLocal(cursor.getString(cursor.getColumnIndex("name"	)));
				itemWeather.setTemp(cursor.getString(cursor.getColumnIndex("weather")));
				itemWeather.setWeather(cursor.getString(cursor.getColumnIndex("temp")));
				itemWeather.setCityCode(cityCode);
				list.add(itemWeather);
			} while (cursor.moveToNext());
		}
		return list;

	}


}
