package com.haoxue.haotianqi.act.frg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.adapter.LifeIndexAdapter;
import com.haoxue.haotianqi.adapter.WetherListAdapter;
import com.haoxue.haotianqi.base.Constant;
import com.haoxue.haotianqi.base.ShareDataHelper;
import com.haoxue.haotianqi.bean.CityWetherBean;
import com.haoxue.haotianqi.bean.ResponseBean;
import com.haoxue.haotianqi.bean.SportIndexBean;
import com.haoxue.haotianqi.bean.WeatherBean;
import com.haoxue.haotianqi.bean.WeatherSubBean;
import com.haoxue.haotianqi.util.LunarUtil;
import com.haoxue.haotianqi.util.NetWorkUtil;
import com.haoxue.haotianqi.util.ReqUtil;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.CuGridView;
import com.haoxue.haotianqi.view.CuListView;
import com.haoxue.haotianqi.view.LoadingDialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 说明：天气预报的界面 
 * 作者： Luoyangs 
 * 时间： 2015年8月20日
 */
@SuppressLint({ "ValidFragment", "InflateParams" })
public class WeatherFrag extends Fragment {

	private int fragmentId;//当前的选项卡号
	private static ResponseBean response; //存储的天气
	private String local = "";//默认城市
	private List<CityWetherBean> list;//天气预报
	private List<SportIndexBean> lifeIndexs;//生活指数
	
	private TextView cityName;//城市
	private TextView pm25;//pm2.5值
	private TextView temp;//温度
	private TextView pollution;//污染程度
	private TextView todayTime0;//阳历
	private TextView todayTime;//农历
	private CuListView weatherList;//天气预报
	private CuGridView lifeIndexList;//生活指数
	private LoadingDialog dialog;//加载框
	
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.LOAD_OK:
				dialog.dismiss();
				ToastUtil.showShort(getActivity(), "天气更新完成");
				display();
				break;
			case Constant.LOAD_FAIL:
				dialog.dismiss();
				ToastUtil.showShort(getActivity(), "加载天气失败");
				break;
			case Constant.LOAD_NO_NET:
				dialog.dismiss();
				ToastUtil.showShort(getActivity(), "当前网络不好，请稍后重试");
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wether_layout, null);
		cityName = (TextView) view.findViewById(R.id.currentcity);
		pm25 = (TextView) view.findViewById(R.id.pm25);
		temp = (TextView) view.findViewById(R.id.temp);
		pollution = (TextView) view.findViewById(R.id.pollution_level);
		todayTime0 = (TextView) view.findViewById(R.id.today_date_dec0);
		todayTime = (TextView) view.findViewById(R.id.today_date_dec);
		weatherList = (CuListView) view.findViewById(R.id.tomorrow_weather);
		lifeIndexList = (CuGridView) view.findViewById(R.id.lifeIndex);
		
		list = new ArrayList<CityWetherBean>();
		lifeIndexs = new ArrayList<SportIndexBean>();
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//加载信息
		loadData();
	}
	
	/**加载数据*/
	private void loadData(){
		String jsonString = ShareDataHelper.getInstance(getActivity()).getCitys();
		if (jsonString != null && jsonString.length() >0) {
			list = JSON.parseArray(jsonString, CityWetherBean.class);
		}
		if (list == null || list.size() == 0) {
			return;
		}
		CityWetherBean cityWetherBean = list.get(fragmentId);
		local = cityWetherBean.getCity();
		if (cityWetherBean.getWeather().equals("点击加载")) {
			//重新加载天气信息,保存到本地
			Log.e("HHA", "hha:"+cityWetherBean.getWeather());
			loadWeather(local);
		}else{
			display();
		}
		
	}
	
	private void display(){
		// 读取天气信息
		String jsonString = ShareDataHelper.getInstance(getActivity()).getWether(local);
		if (jsonString != null && jsonString.length() > 0) {
			response = JSON.parseObject(jsonString, ResponseBean.class);
		} else {
			return;
		}
		// 显示信息
		cityName.setText(local);
		WeatherBean bean = response.getResults().get(0);
		if (bean == null || "".equals(bean.getPm25())) {
			pm25.setText("PM2.5：");
			pollution.setText(R.string.no_data);
			pollution.setBackgroundColor(Color.TRANSPARENT);
		} else {
			pm25.setText("PM2.5: " + bean.getPm25());
			int pm = Integer.parseInt(bean.getPm25());
			if (pm < 75) {
				pollution.setText(R.string.pollution_no);
				pollution.setBackgroundResource(R.drawable.ic_dl_b);
			} else if (pm > 75 && pm < 100) {
				pollution.setText(R.string.pollution_little);
				pollution.setBackgroundResource(R.drawable.ic_dl_c);
			} else if (pm > 100 && pm < 150) {
				pollution.setText(R.string.pollution_mild);
				pollution.setBackgroundResource(R.drawable.ic_dl_d);
			} else if (pm > 150 && pm < 200) {
				pollution.setText(R.string.polltion_moderate);
				pollution.setBackgroundResource(R.drawable.ic_dl_e);
			} else if (pm > 200) {
				pollution.setText(R.string.polltion_severe);
				pollution.setBackgroundResource(R.drawable.ic_dl_f);
			}
			
			String todaydata = bean.getWeather_data().get(0).getDate();
			String temperature = bean.getWeather_data().get(0).getTemperature();
			String subs = null;
			if (todaydata.length() > 14) {
				subs = todaydata.substring(14, todaydata.length() - 1);
				temp.setText(subs);
			} else if (temperature.length() > 5) {
				String[] str = temperature.split("~ ", 2);
				subs = str[1];
				temp.setText(subs);
			} else {
				temp.setText(temperature);
			}

			List<WeatherSubBean> tempbean= bean.getWeather_data();
			if (tempbean != null && tempbean.size() > 0) {
				String todayString = tempbean.get(0).getWeather().trim(); 
				//设置背景图片
				int imgId = R.drawable.bg_fine_day;
				if (todayString.equals("晴")) {
					imgId = R.drawable.bg_homepager;
				}else if (todayString.equals("晴转多云")) {
					imgId = R.drawable.bg_fine_day;
				}else if (todayString.equals("多云转晴")) {
					imgId = R.drawable.bg_cloudy_day;
				}else if (todayString.contains("雨")) {
					imgId = R.drawable.bg_rain;
				}else if (todayString.contains("雷")) {
					imgId = R.drawable.bg_thunder_storm;
				}else if (todayString.contains("雪")) {
					imgId = R.drawable.bg_snow_2;
				}
				MainFrag.mylayout.setBackgroundResource(imgId);
			}
			//加载预报天气列表
			weatherList.setAdapter(new WetherListAdapter(getActivity(), tempbean));
			lifeIndexs = bean.getIndex();
			if (lifeIndexs == null || lifeIndexs.size() == 0) {
				for (int i = 0; i < 8; i++) {
					lifeIndexs.add(new SportIndexBean());
				}
			}else{
				lifeIndexs.add(new SportIndexBean());
				lifeIndexs.add(new SportIndexBean());
			}
			lifeIndexList.setAdapter(new LifeIndexAdapter(getActivity(), lifeIndexs));
			
			// 时间
			Calendar calendar = Calendar.getInstance();
			LunarUtil lunarUtil = new LunarUtil(calendar);
			String dateString = lunarUtil.animalsYear() + "年(";
			dateString += lunarUtil.cyclical() + ")";
			dateString += lunarUtil.toString();
			todayTime0.setText("阳历: " + calendar.get(Calendar.YEAR)+"年"+
					(calendar.get(Calendar.MONTH) +1) +"月"+
					calendar.get(Calendar.DAY_OF_MONTH)+"日");
			todayTime.setText("农历: " + dateString +"    "+ getWeekOfDate());

			// 更新城市列表里面的信息（将"点击更新"换成"天气"）
			list.get(fragmentId).setWeatherimage(bean.getWeather_data().get(0).getDayPictureUrl());
			list.get(fragmentId).setWeather(bean.getWeather_data().get(0).getWeather());
			list.get(fragmentId).setTemp(subs);
			jsonString = JSON.toJSONString(list);
			ShareDataHelper.getInstance(getActivity()).saveCitys(jsonString);
		}
	}
	
	/**获取星期几*/
	public String getWeekOfDate() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	
	/**
	 * @param cityName
	 */
	private void loadWeather(String cityName) {
		//天气加载失败，重新尝试加载
		dialog = LoadingDialog.getInstance(getActivity(), "正在更新天气...");
		dialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);// 注：异步线程中不能设置UI
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					if (!NetWorkUtil.isNetworkAvailable(getActivity())) {
						handler.sendEmptyMessage(Constant.LOAD_NO_NET);
					} else {
						String jsonString = NetWorkUtil.doGet(ReqUtil.getRequestURL(local),null,null,null);
						response = JSON.parseObject(jsonString, ResponseBean.class);
						if (response != null && response.getError() == 0) {
							// 保存到本地
							ShareDataHelper.getInstance(getActivity()).saveWetherInfo(local,jsonString);
							handler.sendEmptyMessage(Constant.LOAD_OK);
						}
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(Constant.LOAD_FAIL);
				}
			}
		}).start();
	}

	@Override
	public void setArguments(Bundle args) {
		fragmentId = args.getInt("fragmentId");
	}
}
