package com.haoxue.haotianqi.act.frg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.base.ShareDataHelper;
import com.haoxue.haotianqi.bean.CityWetherBean;
import com.haoxue.haotianqi.view.LinePageIndicator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** 
 *	说明：天气预报主框架
 *	作者： Luoyangs
 *	时间： 2015年8月20日
 */
@SuppressLint({ "ValidFragment", "InflateParams" })
public class MainFrag extends Fragment{

	private ViewPager viewPager;
    private LinePageIndicator indicator;
    
    private List<Fragment> fragmentList;
    private List<CityWetherBean> citys = null;
    
    private MainFragmentAdapter adapter;
    private int currentId = 0;
	
	public MainFrag(int FragmentId){
		this.currentId = FragmentId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_main_layout, null);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		indicator = (LinePageIndicator) view.findViewById(R.id.indicator);

		citys = new ArrayList<CityWetherBean>();
		fragmentList = new ArrayList<Fragment>();
		
		//加载数据
		this.loadData(); 
		adapter = new MainFragmentAdapter(getFragmentManager());
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager);
		indicator.setCurrentItem(currentId);
		
		return view;
	}
	
	/**加载数据*/
	private void loadData(){
		//加载城市列表
		String jsonString = ShareDataHelper.getInstance(getActivity()).getCitys();
		if (jsonString != null && jsonString.length() >0) {
			citys = JSON.parseArray(jsonString, CityWetherBean.class);
		}
		if (citys.size() == 1 && citys.get(0).getCity().equals("添加")) {
			citys.add(new CityWetherBean("北京", ""));
		}
		for (int i = 0; i < citys.size(); i++) {
			if (citys.get(i).getCity().equals("添加")) {
				continue;
			}
			WeatherFrag frag = new WeatherFrag();
			Bundle bundle = new Bundle();
			bundle.putInt("fragmentId", i);
			frag.setArguments(bundle);
			fragmentList.add(frag);
		}
	}
	
	private class MainFragmentAdapter extends FragmentStatePagerAdapter {

		public MainFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
	}
}
