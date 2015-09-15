package com.haoxue.haotianqi.act.frg;

import java.util.ArrayList;
import java.util.HashMap;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.view.RefreshLayout;
import com.haoxue.haotianqi.view.RefreshLayout.OnLoadListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/** 
 *	说明：历史今天
 *	作者： Luoyangs
 *	时间： 2015年8月24日
 */
@SuppressLint("InflateParams")
public class HistoryFrag extends Fragment implements OnRefreshListener,OnLoadListener {

	private RefreshLayout swipeLayout;
	private ListView listView;
	private ArrayList<HashMap<String, String>> list;
	private MyAdapter adapter;
	private View header;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history_layout, null, false);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		listView = (ListView) view.findViewById(R.id.list);
		header = inflater.inflate(R.layout.swiperelayout_header, null);
		list = new ArrayList<HashMap<String,String>>();
		
		this.initData();
		swipeLayout.setColorScheme(R.color.color_bule2, R.color.color_bule1,
				R.color.color_bule2, R.color.color_bule3);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		//设置自动刷新 swipeLayout.setRefreshing(true);
		//setRefreshing(true)是不会触发onRefresh的,必须要手动调用一次
		swipeLayout.post(new Thread(new Runnable() {
			
			@Override
			public void run() {
				swipeLayout.setRefreshing(true);
			}
		}));
		onRefresh();
		return view;
	}
	
	private void initData(){
		for (int i = 0; i < 10; i++) {
			list.add(new HashMap<String, String>());
		}
		adapter = new MyAdapter(getActivity(), list);
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
	}
	
	
	private class MyAdapter extends BaseAdapter {
		public ArrayList<HashMap<String, String>> list;
		public LayoutInflater layoutInflater;

		public MyAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = layoutInflater.inflate(R.layout.fragment_history_item, null);
			} else {
				view = convertView;
			}
			return view;
		}

	}


	@Override
	public void onLoad() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				swipeLayout.setLoading(false);
				for (int i = 0; i < 10; i++) {
					list.add(new HashMap<String, String>());
				}
				adapter.notifyDataSetChanged();
			}
		}, 2000);
	}

	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				list.clear();
				for (int i = 0; i < 10; i++) {
					list.add(new HashMap<String, String>());
				}
				adapter.notifyDataSetChanged();
				swipeLayout.setRefreshing(false);
			}
		}, 2000);
	}
}
