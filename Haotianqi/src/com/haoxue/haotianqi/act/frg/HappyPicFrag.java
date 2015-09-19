package com.haoxue.haotianqi.act.frg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.act.ShowImageAct;
import com.haoxue.haotianqi.base.Constant;
import com.haoxue.haotianqi.bean.HappyPic;
import com.haoxue.haotianqi.bean.HappyPicItem;
import com.haoxue.haotianqi.bean.HappyResponse;
import com.haoxue.haotianqi.util.NetWorkUtil;
import com.haoxue.haotianqi.util.ReqUtil;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.RefreshLayout;
import com.haoxue.haotianqi.view.RefreshLayout.OnLoadListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 说明:图片区
 * 作者:Luoyangs
 * 时间:2015-9-18
 */
@SuppressLint("ValidFragment")
public class HappyPicFrag extends Fragment implements OnRefreshListener,OnLoadListener {

	private RefreshLayout swipeLayout;
	private ListView listView;
	private static HappyResponse response;
	private static HappyPic textBean;
	private static List<HappyPicItem> list;
	private MyAdapter adapter;
	private static int page = 1;
	private BitmapUtils bitmapUtils;
	
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
		textBean = new HappyPic();
		list = new ArrayList<HappyPicItem>();
		bitmapUtils = new BitmapUtils(getActivity());
		
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
		textBean = JSON.parseObject(response.getShowapi_res_body(), HappyPic.class);
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
						String jsonString = NetWorkUtil.doGet(ReqUtil.getHappyPicRequestURL(page),null,"apikey",Constant.HAPPY_APPKEY);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = layoutInflater.inflate(R.layout.fragment_happy_item_layout_pic, null);
			} else {
				view = convertView;
			}
			TextView title = (TextView) view.findViewById(R.id.hap_titile);
			final ImageView content = (ImageView) view.findViewById(R.id.hap_content);
			TextView time = (TextView) view.findViewById(R.id.hap_time);
			title.setText(list.get(position).getTitle());
			bitmapUtils.display(content, list.get(position).getImg(), new BitmapLoadCallBack<View>() {

				@Override
				public void onLoadCompleted(View arg0, String arg1, Bitmap bitmap, BitmapDisplayConfig arg3,BitmapLoadFrom arg4) {
					content.setImageBitmap(bitmap);
				}

				@Override
				public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
					content.setImageResource(R.drawable.nopic);
				}
			});
			time.setText(list.get(position).getCt());
			//点击查看大图
			content.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(getActivity(),ShowImageAct.class);
					intent.putExtra("image",list.get(position).getImg());
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			});
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

