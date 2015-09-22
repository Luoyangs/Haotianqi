package com.haoxue.haotianqi.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.base.ShareDataHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/** 
 *	说明：皮肤管理
 *	作者： Luoyangs
 *	时间： 2015年8月24日
 */
@ContentView(R.layout.act_skin_layout)
public class SkinAct extends Activity {

	@ViewInject(R.id.gridview)
	private GridView gridView;
	private MyBaseAdapter adapter;
	private List<Map<String, Object>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
		list = new ArrayList<Map<String, Object>>();
		loadData();
		
		adapter = new MyBaseAdapter(list, this);
		gridView.setAdapter(adapter);
	}
	
	@SuppressLint("Recycle")
	private void loadData(){
		TypedArray ar = this.getResources().obtainTypedArray(R.array.bg_image_attr);
		for (int i = 0; i < ar.length(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("imgId", ar.getResourceId(i, 0));
			int oldBg = ShareDataHelper.getInstance(this).getBgImage();
			if (oldBg == ar.getResourceId(i, 0)) {
				map.put("check", true);
			}else{
				map.put("check", false);
			}
			list.add(map);
		}
	}
	
	@OnClick(R.id.titilbar_left)
	public void onClick(View view){
		this.finish();
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}
	
	private class MyBaseAdapter extends BaseAdapter{
		
		private List<Map<String, Object>> list;
		private LayoutInflater inflater;
		
		public MyBaseAdapter(List<Map<String, Object>> list,Context context){
			this.list = list;
			this.inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return list.size();
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
			View view = inflater.inflate(R.layout.act_skin_layout_item, null, false);
			ImageView image = (ImageView) view.findViewById(R.id.item_image);
			final ImageView select = (ImageView) view.findViewById(R.id.item_select);
			Map<String, Object> map = list.get(position);
			final int imgId = Integer.parseInt(map.get("imgId").toString());
			image.setImageResource(imgId);
			if (Boolean.parseBoolean(map.get("check").toString())) {
				select.setImageResource(R.drawable.pictures_selected);
			}else{
				select.setImageResource(R.drawable.picture_unselected);
			}
			
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					select.setImageResource(R.drawable.pictures_selected);
					ShareDataHelper.getInstance(SkinAct.this).saveBgImage(imgId);
					finishPage();
				}
			});
			return view;
		}
	}
	
	private void finishPage(){
		this.finish();
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}
}
