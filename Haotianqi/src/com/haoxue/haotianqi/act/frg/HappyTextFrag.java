package com.haoxue.haotianqi.act.frg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.base.Constant;
import com.haoxue.haotianqi.bean.HappyResponse;
import com.haoxue.haotianqi.bean.HappyText;
import com.haoxue.haotianqi.bean.HappyTextItem;
import com.haoxue.haotianqi.util.NetWorkUtil;
import com.haoxue.haotianqi.util.ReqUtil;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.RefreshLayout;
import com.haoxue.haotianqi.view.RefreshLayout.OnLoadListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 说明:文本区
 * 作者:Luoyangs
 * 时间:2015-9-18
 */
public class HappyTextFrag extends Fragment implements OnRefreshListener,OnLoadListener {

	private RefreshLayout swipeLayout;
	private ListView listView;
	private static HappyResponse response;
	private static HappyText textBean;
	private static List<HappyTextItem> list;
	private MyAdapter adapter;
	private static int page = 1;
	
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.LOAD_OK:
				ToastUtil.showShort(getActivity(), "加载完成");
				display();
				break;
			case Constant.LOAD_FAIL:
				ToastUtil.showShort(getActivity(), "加载失败");
				break;
			case Constant.LOAD_NO_NET:
				ToastUtil.showShort(getActivity(), "当前网络不好，请稍后重试");
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_happy_item_layout, null, false);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		listView = (ListView) view.findViewById(R.id.list);
		
		response = new HappyResponse();
		textBean = new HappyText();
		list = new ArrayList<HappyTextItem>();
		
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

	private void display(){
		textBean = JSON.parseObject(response.getShowapi_res_body(), HappyText.class);
		list = textBean.getContentlist();
		adapter = new MyAdapter(getActivity(), list);
		listView.setAdapter(adapter);
	}
	
	/**网络请求*/
	private void loadData(final int page){
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
						String jsonString = NetWorkUtil.doGet(ReqUtil.getHappyTextRequestURL(page),null,"apikey",Constant.HAPPY_APPKEY);
						response = JSON.parseObject(jsonString, HappyResponse.class);
						if (response != null && response.getShowapi_res_code() == 0) {
							handler.sendEmptyMessage(Constant.LOAD_OK);
						}else{
							handler.sendEmptyMessage(Constant.LOAD_FAIL);
						}
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(Constant.LOAD_FAIL);
				}
			}
		}).start();
	}
	
	private class MyAdapter extends BaseAdapter {
		public List<HappyTextItem> list;
		public LayoutInflater layoutInflater;

		public MyAdapter(Context context, List<HappyTextItem> list) {
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
				view = layoutInflater.inflate(R.layout.fragment_happy_item_layout_text, null);
			} else {
				view = convertView;
			}
			TextView title = (TextView) view.findViewById(R.id.hap_titile);
			TextView content = (TextView) view.findViewById(R.id.hap_content);
			TextView time = (TextView) view.findViewById(R.id.hap_time);
			title.setText(list.get(position).getTitle());
			content.setText(Html.fromHtml(list.get(position).getText()));
			time.setText(list.get(position).getCt());
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
				page += 1;
				loadData(page);
			}
		}, 1500);
	}

	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				list.clear();
				loadData(page);
				swipeLayout.setRefreshing(false);
			}
		}, 1500);
	}
}
