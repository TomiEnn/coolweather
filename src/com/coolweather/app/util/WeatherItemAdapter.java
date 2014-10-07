package com.coolweather.app.util;

import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.model.ItemWeather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeatherItemAdapter extends BaseAdapter {

	private Context context = null;
	private List<ItemWeather> datas;

	private LayoutInflater myInflater = null;

	public WeatherItemAdapter(Context context, List<ItemWeather> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			myInflater = LayoutInflater.from(context);
			convertView = myInflater.inflate(R.layout.item_add_city, null);

			holder.itemLocal = (TextView) convertView
					.findViewById(R.id.item_local_tv);
			holder.itemTemp = (TextView) convertView
					.findViewById(R.id.item_temp_tv);
			holder.itemWeather = (TextView) convertView
					.findViewById(R.id.item_weather_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			holder.itemLocal.setText(datas.get(position).getLocal());
			holder.itemTemp.setText(datas.get(position).getTemp());
			holder.itemWeather.setText(datas.get(position).getWeather());
		return convertView;
	}

	private class ViewHolder {
		TextView itemLocal;
		TextView itemTemp;
		TextView itemWeather;
	}

}
