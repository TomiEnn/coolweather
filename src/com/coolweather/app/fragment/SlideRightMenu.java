package com.coolweather.app.fragment;

import com.coolweather.app.R;
import com.coolweather.app.activity.ChooseAreaActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SlideRightMenu extends Fragment implements OnClickListener {

	private RelativeLayout rightMenu;
	private ImageView addCity;
	private RelativeLayout rightMenuShowWeather;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.add_city_layout, null, false);
		rightMenu = (RelativeLayout) view.findViewById(R.id.select_city_rl);
		addCity = (ImageView) view.findViewById(R.id.add_layout_image_plus);
		addCity.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_layout_image_plus:
			
			Intent intent =new Intent(getActivity(),ChooseAreaActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
