package com.haoxue.haotianqi.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.bean.CityWetherBean;

/** 
 *	说明：最近访问城市适配器
 *	作者： Luoyangs
 *	时间： 2015年8月27日
 */
public class HisCityAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<CityWetherBean> hotCitys;

	public HisCityAdapter(Context context, List<CityWetherBean> hotCitys) {
		this.context = context;
		inflater = LayoutInflater.from(this.context);
		this.hotCitys = hotCitys;
	}

	@Override
	public int getCount() {
		int len = hotCitys.size();
		for (int i = 0; i < hotCitys.size(); i++) {
			if (hotCitys.get(i).getCity().equals("添加")) {
				len = len - 1;
				hotCitys.remove(i);//此地只删除不保存
			}
		}
		return len;
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
		convertView = inflater.inflate(R.layout.item_city, null);
		TextView city = (TextView) convertView.findViewById(R.id.city);
		city.setText(hotCitys.get(position).getCity());
		return convertView;
	}
}
