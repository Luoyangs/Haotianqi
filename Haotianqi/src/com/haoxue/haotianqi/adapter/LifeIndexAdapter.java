package com.haoxue.haotianqi.adapter;

import java.util.List;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.bean.SportIndexBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 *	说明：生活指数的适配器
 *	作者： Luoyangs
 *	时间： 2015年8月18日
 */
public class LifeIndexAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<SportIndexBean> list;
	private int[] resours = {R.drawable.ic_todaycan_date,  
			R.drawable.ic_todaycan_jingdian, R.drawable.ic_todaycan_dress,
			R.drawable.ic_todaycan_carwash, R.drawable.ic_todaycan_tour,
			R.drawable.ic_todaycan_coldl, R.drawable.ic_todaycan_sport,
			R.drawable.ic_todaycan_ultravioletrays };
	
	public LifeIndexAdapter(Context context,List<SportIndexBean> list){
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list == null ?0:list.size();
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
		if (convertView == null ) {
			convertView = inflater.inflate(R.layout.fragment_lifeindex_item, parent, false);
		}
		TextView dothing = (TextView) convertView.findViewById(R.id.dothing);
		TextView index = (TextView) convertView.findViewById(R.id.index);
		ImageView image_index = (ImageView) convertView.findViewById(R.id.image_index);
		ImageView image_click = (ImageView) convertView.findViewById(R.id.image_click);
		// 设置数据
		if (position == 0) {
			dothing.setText("今天");
		} else if (position == 1) {
			dothing.setText("城市景点");
		} else if (position == 2) {
			dothing.setText("穿衣指数");
		} else if (position == 3) {
			dothing.setText("洗车指数");
		} else if (position == 4) {
			dothing.setText("旅游指数");
		} else if (position == 5) {
			dothing.setText("感冒指数");
		} else if (position == 6) {
			dothing.setText("运动指数");
		} else if (position == 7) {
			dothing.setText("紫外线指数");
		} else {
			dothing.setText(list.get(position).getTipt());
		}
		try{
			if(list.get(position).getZs() != null){
				if(position == 0){
					index.setText(list.get(position +6).getZs());
				}else if(position == 1){
					index.setText(list.get(position +6).getZs());
				}else {
					index.setText(list.get(position-2).getZs());
				}
			}else{
				index.setText("暂无");
			}
		}catch(NullPointerException e){
			index.setText("暂无");
		}
		image_index.setBackgroundResource(resours[position]);
		image_click.setBackgroundResource(R.drawable.ic_todaycan_clickbt);
		return convertView;
	}

}
