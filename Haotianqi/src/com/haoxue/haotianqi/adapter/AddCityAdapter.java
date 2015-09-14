package com.haoxue.haotianqi.adapter;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.base.CityMagDBHelper;
import com.haoxue.haotianqi.base.Constant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 *	说明：添加城市的适配器
 *	作者： Luoyangs
 *	时间： 2015年8月18日
 */
public class AddCityAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	//热门城市
	private static final String[] cityname = 
			{ "北京", "上海", "广州", "南京", "成都", "武汉", "杭州", "西安", "济南", "长春", "东莞",
					"沈阳", "天津", "哈尔滨", "长沙", "呼和浩特", "石家庄", "重庆", "无锡", "包头",
					"大连", "深圳", "福州", "海口", "乌鲁木齐", "兰州", "银川", "太原", "郑州",
					"合肥", "南昌", "南宁", "贵阳", "昆明", "拉萨", "西宁", "台北", "香港", "澳门" };
	SparseBooleanArray sba = new SparseBooleanArray();
	private String nowcityname;
	
	public AddCityAdapter(Context context){
		inflater = LayoutInflater.from(context);
		CityMagDBHelper dbHelper = new CityMagDBHelper(context, Constant.CITY_WEATHER_MANAGER_DB_NAME, null, 1);
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(Constant.CITY_WEATHER_MANAGER_TABLE_NAME, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			nowcityname = cursor.getString(cursor.getColumnIndex("cityname"));
			for(int i=0;i<cityname.length;i++){
				if(nowcityname.equals(cityname[i])){
					sba.put(i, true);
				}
			}
		}
	}
	
	@Override
	public int getCount() {
		return cityname == null ? 0 : cityname.length;
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
			convertView = inflater.inflate(R.layout.addcity_gridview_item, parent, false);
		}
		TextView citytext = (TextView) convertView.findViewById(R.id.citytext);
		citytext.setText(cityname[position]);
		// 查询数据库，数据库中有该城市则设置勾选
		if (sba.get(position)) {
			citytext.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.city_checkbox_selected, 0);
		}
		return convertView;
	}

}
