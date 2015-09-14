package com.haoxue.haotianqi.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.bean.CityBean;

/** 
 *	说明：搜索城市结果适配器
 *	作者： Luoyangs
 *	时间： 2015年8月26日
 */
public class SearchCityListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<CityBean> results = new ArrayList<CityBean>();

	public SearchCityListAdapter(Context context, ArrayList<CityBean> results) {
		inflater = LayoutInflater.from(context);
		this.results = results;
	}

	@Override
	public int getCount() {
		return results.size();
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(results.get(position).getName());
		return convertView;
	}

	class ViewHolder {
		TextView name;
	}
}
