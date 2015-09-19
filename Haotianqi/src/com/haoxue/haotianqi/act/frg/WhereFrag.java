package com.haoxue.haotianqi.act.frg;

import java.util.ArrayList;
import java.util.List;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.bean.HappyPicItem;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/** 
 *	说明：景点
 *	作者： Luoyangs
 *	时间： 2015年8月24日
 */
public class WhereFrag extends Fragment {
	
	private LinearLayout layout;
	private ImageView seach;
	private AutoCompleteTextView find;//搜索框
	private ListView listview;
	private View view1;//下划线
	
	private static List<String> whereList = new ArrayList<String>();//景点集合
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_where_layout, null, false);
		layout = (LinearLayout) view.findViewById(R.id.title_bar);
		seach = (ImageView) view.findViewById(R.id.iv_seach);
		find = (AutoCompleteTextView) view.findViewById(R.id.find);
		listview = (ListView) view.findViewById(R.id.listview);
		view1 = (View) view.findViewById(R.id.view);
		
		find.setVisibility(View.GONE);
		layout.setVisibility(View.VISIBLE);
		view1.setVisibility(View.VISIBLE);
		ImageView emptyView = new ImageView(getActivity());
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,  
		        LayoutParams.MATCH_PARENT));  
		emptyView.setImageResource(R.drawable.no_data);
		listview.setEmptyView(emptyView);
		
		seach.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				find.setVisibility(View.VISIBLE);
				layout.setVisibility(View.GONE);
				view1.setVisibility(View.GONE);
			}
		});
		return view;
	}
	
	private class MyAdapter extends BaseAdapter{

		private List<HappyPicItem> list;
		private LayoutInflater layoutInflater;

		public MyAdapter(Context context, List<HappyPicItem> list) {
			this.list = list;
			layoutInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			return null;
		}
		
	}
}
