package com.haoxue.haotianqi.adapter;

import java.util.List;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.bean.WeatherSubBean;
import com.haoxue.haotianqi.view.HXImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 *	说明：天气列表
 *	作者： Luoyangs
 *	时间： 2015年8月17日
 */
public class WetherListAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private List<WeatherSubBean> weatherlist;
	
	public WetherListAdapter(Context context,List<WeatherSubBean> list){
		this.inflater = LayoutInflater.from(context);
		this.weatherlist = list;
	}
	
	@Override
	public int getCount() {
		return weatherlist == null?0:weatherlist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_wether_listview_item,null);
		}
		WeatherSubBean bean = weatherlist.get(position);
		// 日期
		TextView list_date_nextday = (TextView) convertView.findViewById(R.id.list_date_nextday);
		if (position > 0) {
			list_date_nextday.setText(bean.getDate());
		} else if (position == 0) {
			list_date_nextday.setText("今天");
		}
		// 白天天气图片&夜晚天气图片
		HXImageView list_day_picture = (HXImageView) convertView.findViewById(R.id.list_day_picture);
		String dayp = bean.getDayPictureUrl();
		list_day_picture.loadImage(dayp);
		HXImageView list_night_picture = (HXImageView) convertView.findViewById(R.id.list_night_picture);
		String nightp = bean.getNightPictureUrl();
		list_night_picture.loadImage(nightp);
		//加载天气
		TextView list_day_weather = (TextView) convertView.findViewById(R.id.list_weather);
		list_day_weather.setText(bean.getWeather());
		//温度
		TextView list_day_temp = (TextView)convertView.findViewById(R.id.list_temperature);
		String temp = bean.getTemperature();
		if (temp.length() > 4) {
			//默认将最低温度显示在最前面
			String sub1 = temp.substring(0, temp.length() -1);
			String sub2 = temp.substring(temp.length() -1, temp.length());
			String[] temps = sub1.split("~", 2);
			temp = temps[1]+"~"+temps[0]+sub2;
		}
		list_day_temp.setText(temp);
		//风力
		TextView list_day_wind = (TextView)convertView.findViewById(R.id.list_wind);
		list_day_wind.setText(bean.getWind());
		
		return convertView;
	}

}
