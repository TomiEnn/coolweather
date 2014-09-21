package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallBackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.LogUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {

	private static final int LEVEL_PROVINCE = 1;
	private static final int LEVEL_CITY = 2;
	private static final int LEVEL_COUNTY = 3;
	private int currentLevel ;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<String>();
	/**
	 * 省的列表
	 */
	private List<Province> provinceList;
	/**
	 * 市的列表
	 */
	private List<City> cityList;
	/**
	 * 县的列表
	 */
	private List<County> countyList;
	private Province selectProvince;
	private City selectCity;
	private County selectCounty;
	private CoolWeatherDB coolWeatherDB;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		titleText = (TextView) findViewById(R.id.choose_title);
		listView = (ListView) findViewById(R.id.choose_listview);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		queryProvinces();//加载省级的数据
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(currentLevel == LEVEL_PROVINCE){
					selectProvince = provinceList.get(position);
					queryCities();
					LogUtil.i("coolweather", "*******selectProvince: "+selectProvince.getProvincePyName());
				}else if(currentLevel == LEVEL_CITY){
					selectCity = cityList.get(position);
					queryCounties();
				}
			}
			
		});
	}
	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有就从网路上查询
	 */
	private void queryProvinces() {
		// TODO Auto-generated method stub
		LogUtil.i("coolweather", "start queryProvinces");
		provinceList = coolWeatherDB.loadProvinces();
		LogUtil.i("coolweather", "start queryProvinces*************");
		if(provinceList.size() > 0){
			dataList.clear();
			for(Province province : provinceList){
				dataList.add(province.getProvinceQuName());
				LogUtil.i("coolweather","for循环： "+province.getProvincePyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
			
		}else{
			queryFromServer(null,"Province");
			LogUtil.i("coolweather", "start queryProvinceFromServer********");
		}
	}
	private void queryFromServer(final String code, final String type) {
		// TODO Auto-generated method stub
		String address; //存放需要解析的地址
		if(!TextUtils.isEmpty(code)){
			address = "http://flash.weather.com.cn/wmaps/xml/"+code+".xml";
		}else{
			address = "http://flash.weather.com.cn/wmaps/xml/china.xml";
		}
		showProgressDialog();
		LogUtil.i("coolweather", address);
		//加载省级的数据，重写onFish（）方法接收返回的数据
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				if("Province".equals(type)){
					//解析服务器返回的数据，并存储到Province和数据库中
					result = Utility.handleProvinceResponse(coolWeatherDB, response);
				}else if("city".equals(type)){
					
					result = Utility.handleCityResponse(coolWeatherDB, response, code);
					LogUtil.i("coolwather", "*****result: " + result);
				}else if("county".equals(type)){
					result = Utility.handleCountyResponse(coolWeatherDB, response, code);
				}
				if(result){
					//通过runOnUiThread()方法返回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("Province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("county".equals(type)){
								queryCounties();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if("Province".equals(type)){
							Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}
	/**
	 * 查询选中省份中所有的市，先从数据库查，没有的话再去服务器查询
	 */
	private void queryCities(){
		cityList =coolWeatherDB.loudCities(selectProvince.getProvincePyName());
		//cityList =coolWeatherDB.loudCities("beijin");
		LogUtil.i("coolweather", "queryCities() "+selectProvince.getProvincePyName());
		if(cityList.size() > 0 ){
			dataList.clear();
			for(City city : cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectProvince.getProvinceQuName());
			currentLevel = LEVEL_CITY;
		}else{
			queryFromServer(selectProvince.getProvincePyName(), "city");
			//queryFromServer("beijing", "city");
		}
	}
	/**
	 *  查询选中的市内查询所有的县，先从数据库查，没有的话再去服务器查询
	 */
	private void queryCounties(){
		countyList = coolWeatherDB.loudCounties(selectCity.getCityPy());
		if(countyList.size() > 0){
			dataList.clear();
			for(County county : countyList){
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		}else{
			queryFromServer(selectCity.getCityPy(), "county");
		}
	}
	/**
	 * 显示进度的对话框
	 */
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	/**
	 * 关闭进度的对话框
	 */
	private void closeProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	/**
	 * 捕获Back按键，根据当前状态判断是退出还是返回
	 */
	@Override
	public void onBackPressed() {
		if(currentLevel == LEVEL_COUNTY){
			queryCities();
		}else if(currentLevel == LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
	}
}
