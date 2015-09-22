package com.haoxue.haotianqi.act.frg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.act.AddCityAct;
import com.haoxue.haotianqi.act.MainAct;
import com.haoxue.haotianqi.adapter.CityWeatherManagerAdapter;
import com.haoxue.haotianqi.base.ShareDataHelper;
import com.haoxue.haotianqi.bean.CityWetherBean;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/** 
 *	说明：城市天气管理页面
 *	作者： Luoyangs
 *	时间： 2015年8月24日
 */
public class CityFrag extends Fragment {

	private RelativeLayout ci;
	private GridView cityViews;
	
	private List<CityWetherBean> list = new ArrayList<CityWetherBean>();
	private CityWeatherManagerAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_citymanager_layout, null, false);
		cityViews = (GridView) view.findViewById(R.id.gridview);
		cityViews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (position == list.size() -1) {
					startActivity(new Intent(getActivity(), AddCityAct.class));
				}else{
					MainAct activity = (MainAct) getActivity();
					activity.buttom_menu_city.setBackgroundColor(Color.TRANSPARENT);
					activity.buttom_menu_weather.setTextSize(18);
					activity.buttom_menu_city.setTextSize(16);
					activity.buttom_menu_weather.setTextColor(Color.parseColor(getString(R.string.color_bottombg)));
					activity.buttom_menu_city.setTextColor(Color.parseColor(getString(R.string.color_bottombgn)));
					activity.moveTo(new MainFrag(position));
				}
			}
		});

		//设置背景
		ci = (RelativeLayout) view.findViewById(R.id.ci);
		int imgId = ShareDataHelper.getInstance(getActivity()).getBgImage();
		ci.setBackgroundResource(imgId);
		
		adapter = new CityWeatherManagerAdapter(getActivity(), list);
		cityViews.setAdapter(adapter);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// 加载数据
		getCityLists();
		// 更新适配器
		adapter.setData(list);
		adapter.notifyDataSetChanged();
	}
	
	private void getCityLists(){
		String jsonString = ShareDataHelper.getInstance(getActivity()).getCitys();
		if (jsonString != null && jsonString.length() >0) {
			list = JSON.parseArray(jsonString, CityWetherBean.class);
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCity().equals("添加")) {
				list.remove(i);
			}
		}
		// 末尾添加一个“+”
		CityWetherBean cwb = new CityWetherBean();
		cwb.setCity("添加");
		list.add(list.size(), cwb);
	}
}
