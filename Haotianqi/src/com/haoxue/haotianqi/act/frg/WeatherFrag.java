package com.haoxue.haotianqi.act.frg;

import com.haoxue.haotianqi.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 说明：天气预报的界面 
 * 作者： Luoyangs 
 * 时间： 2015年8月20日
 */
@SuppressLint("ValidFragment")
public class WeatherFrag extends Fragment {

	private int fragmentId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.wetherfragment_layout, null);
		TextView tView = (TextView) view.findViewById(R.id.temp);
		tView.setText(fragmentId+"");
		return view;
	}
	
	@Override
	public void setArguments(Bundle args) {
		fragmentId = args.getInt("fragmentId");
	}
}
