package com.coolweather.app.fragment;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.activity.ChooseAreaActivity;
import com.coolweather.app.model.ItemWeather;
import com.coolweather.app.util.LogUtil;
import com.coolweather.app.util.WeatherItemAdapter;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class SlideRightMenu extends Fragment implements OnClickListener {

	private ImageView addCity;
	private ListView addListView;
	private WeatherItemAdapter adapter;
	private List<ItemWeather> datas;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.add_city_layout, null, false);
		addCity = (ImageView) view.findViewById(R.id.add_layout_image_plus);
		addCity.setOnClickListener(this);
		addListView = (ListView) view.findViewById(R.id.add_city_listview);
		datas = new ArrayList<ItemWeather>();
		adapter = new WeatherItemAdapter(getActivity(), datas);

		addListView.setAdapter(adapter);
		

		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		LogUtil.i("coolweather", "onActivityCreated执行了");
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_layout_image_plus:

			Intent intent = new Intent(getActivity(), ChooseAreaActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	public void getDatas() {
		ItemWeather weather = new ItemWeather();
		weather.setLocal(PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString("city", ""));
		LogUtil.i("coolweather", "getDatas方法 :" + PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString("city", ""));
		weather.setTemp(PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString("temp1", ""));
		weather.setWeather(PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString("weather1", ""));
		LogUtil.i("coolweather", "getDatas方法 :" + PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString("weather1", ""));
		LogUtil.i("coolweather", "getDatas方法 :" + PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString("temp1", ""));
		datas.add(weather);
		LogUtil.i("coolweather", "datas的大小： "+datas.size());
		adapter.notifyDataSetChanged();
	}
}
