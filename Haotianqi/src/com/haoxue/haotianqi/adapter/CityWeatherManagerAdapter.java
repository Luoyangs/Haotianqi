package com.haoxue.haotianqi.adapter;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.base.ShareDataHelper;
import com.haoxue.haotianqi.bean.CityWetherBean;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.CustomDialog;
import com.haoxue.haotianqi.view.HXImageView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/** 
 *	说明：城市天气列表适配器（已经选择的城市）
 *	作者： Luoyangs
 *	时间： 2015年8月17日
 */
public class CityWeatherManagerAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private List<CityWetherBean> list;
	private Context context;
	
	public CityWeatherManagerAdapter(Context context,List<CityWetherBean> list){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_citymanager_layout_item, parent,false);
		}
		TextView grid_city = (TextView) convertView.findViewById(R.id.grid_city);
		TextView grid_temp = (TextView) convertView.findViewById(R.id.grid_temp);
		TextView grid_weather = (TextView) convertView.findViewById(R.id.grid_weather);
		TextView city_item_layout = (TextView) convertView.findViewById(R.id.city_item_layout);
		TextView grid_item_delete = (TextView) convertView.findViewById(R.id.grid_item_delete);
		HXImageView grid_weatherimage = (HXImageView) convertView.findViewById(R.id.grid_weatherimage);
		Button grid_set_normal = (Button) convertView.findViewById(R.id.grid_set_normal);
		
		final CityWetherBean bean = list.get(position);
		if (position == list.size() -1) {
			grid_city.setText("");
			grid_temp.setText("");
			grid_weather.setText("");
			grid_set_normal.setText("");
			grid_weatherimage.setImageDrawable(null);
			grid_set_normal.setBackgroundColor(Color.TRANSPARENT);
			city_item_layout.setBackgroundResource(R.drawable.cityadd_bg);
			grid_item_delete.setText("");
		}else{
			grid_city.setText(bean.getCity());	
			grid_temp.setText(bean.getTemp());
			grid_weather.setText(bean.getWeather());
			if (position == 0) {
				grid_set_normal.setText("默认");
			}else{
				grid_set_normal.setText("设为默认");
			}
			grid_weatherimage.loadImage(bean.getWeatherimage());
			city_item_layout.setBackgroundColor(Color.TRANSPARENT);
			grid_set_normal.setBackgroundResource(R.drawable.citym_normal_bg);
			grid_item_delete.setText("×");
		}
		grid_item_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new CustomDialog.Builder(context).setTitle("提示信息")
						.setMessage("删除当前城市？")
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								//删除城市
								if (list.remove(position) != null) {
									ToastUtil.showShort(context, "删除成功");
								}
								String jsonString = JSON.toJSONString(list);
								ShareDataHelper.getInstance(context).saveCitys(jsonString);
								notifyDataSetChanged();
							}
						}).create().show();
			}
		});
		grid_set_normal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtil.showShort(context, "len: "+list.size());
				if (list.size() <= 1) {
					return;
				}
				//设置默认的城市
				List<CityWetherBean> temps = new ArrayList<CityWetherBean>();
				temps.add(list.get(position));
				for (int i = 0; i < list.size(); i++) {
					if (i == position) {
						continue;
					}
					temps.add(list.get(i));
				}
				ToastUtil.showShort(context, "已将 "+list.get(position).getCity()+" 设为默认城市");
				list = temps;
				String jsonString = JSON.toJSONString(list);
				ShareDataHelper.getInstance(context).saveCitys(jsonString);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
	
	/**加载数据*/
	public void setData(List<CityWetherBean> list){
		this.list = list;
	}

}
