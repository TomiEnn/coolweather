package com.coolweather.app.db;

import com.coolweather.app.util.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * Province表建表语句
	 */
	public static final String CREATE_PROVINCE = "create table Province ("
			+ "_id integer primary key autoincrement,"
			+ "province_quname text," + "province_pyname text)";

	/**
	 * City表建表语句
	 */
	public static final String CREATE_CITY = "create table City("
			+ "_id integer primary key autoincrement," + "city_name text,"
			+ "city_py text," + "city_url integer," + "province_code text)";

	/**
	 * County表建表语句
	 */
	public static final String CREATE_COUNTY = "create table County("
			+ "_id integer primary key autoincrement," + "county_name text,"
			+ "county_url integer," + "city_code text)";

	/**
	 * 天气建表语句
	 */
	public static final String CREATE_ITEMWEATHER = "create table ItemWeatehr("
			+"_id integer primary key autoincrement," + "name text,"
			+"weather text,"+"temp text,"+"city_code text)";
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
		db.execSQL(CREATE_ITEMWEATHER);
		LogUtil.i("coolweather", "database oncrete");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
