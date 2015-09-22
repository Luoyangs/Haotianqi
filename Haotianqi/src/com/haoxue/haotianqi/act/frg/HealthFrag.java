package com.haoxue.haotianqi.act.frg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.act.HealthDetailAct;
import com.haoxue.haotianqi.base.Constant;
import com.haoxue.haotianqi.bean.HeaResponse;
import com.haoxue.haotianqi.bean.HealthBean;
import com.haoxue.haotianqi.util.DownloadUtils;
import com.haoxue.haotianqi.util.NetWorkUtil;
import com.haoxue.haotianqi.util.ReqUtil;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.LoadingDialog;
import com.haoxue.haotianqi.view.RefreshLayout;
import com.haoxue.haotianqi.view.RefreshLayout.OnLoadListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 说明：健康页面 作者： Luoyangs 时间： 2015年8月24日
 */
@SuppressLint("InflateParams")
public class HealthFrag extends Fragment implements OnRefreshListener, OnLoadListener {

	private RefreshLayout swipeLayout;
	private ListView listView;
	private static HeaResponse response;
	private static List<HealthBean> list;
	private MyAdapter adapter;
	private LoadingDialog dialog;// 加载框
	private static int page = 1;

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.LOAD_OK:
				dialog.dismiss();
				ToastUtil.showShort(getActivity(), "加载完成");
				display();
				break;
			case Constant.LOAD_FAIL:
				dialog.dismiss();
				ToastUtil.showShort(getActivity(), "加载失败");
				break;
			case Constant.LOAD_NO_NET:
				dialog.dismiss();
				ToastUtil.showShort(getActivity(), "当前网络不好，请稍后重试");
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_health_layout, null,false);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		listView = (ListView) view.findViewById(R.id.list);
		list = new ArrayList<HealthBean>();
		response = new HeaResponse();
		
		swipeLayout.setColorScheme(R.color.color_bule2, R.color.color_bule1,
				R.color.color_bule2, R.color.color_bule3);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		// 设置自动刷新 swipeLayout.setRefreshing(true);
		// setRefreshing(true)是不会触发onRefresh的,必须要手动调用一次
		swipeLayout.post(new Thread(new Runnable() {

			@Override
			public void run() {
				swipeLayout.setRefreshing(true);
			}
		}));
		onRefresh();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		page = 1;
	}

	private void display() {
		list = JSON.parseArray(response.getNewslist(), HealthBean.class);
		adapter = new MyAdapter(getActivity(), list);
		listView.setAdapter(adapter);
	}

	/** 网络请求 */
	private void loadData(final int page) {
		dialog = LoadingDialog.getInstance(getActivity(), "正在更新...");
		dialog.show();
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
						String jsonString = NetWorkUtil.doGet(ReqUtil.getHealthRequestURL(page), null,null,null);
						response = JSON.parseObject(jsonString,
								HeaResponse.class);
						if (response != null
								&& (Integer.parseInt(response.getCode()) == 200)) {
							handler.sendEmptyMessage(Constant.LOAD_OK);
						} else {
							handler.sendEmptyMessage(Constant.LOAD_FAIL);
						}
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(Constant.LOAD_FAIL);
				}
			}
		}).start();
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
				page = 1;
				loadData(page);

				swipeLayout.setRefreshing(false);
			}
		}, 2000);
	}

	private class MyAdapter extends BaseAdapter {
		public List<HealthBean> list;
		public LayoutInflater layoutInflater;

		public MyAdapter(Context context, List<HealthBean> list) {
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = layoutInflater.inflate(
						R.layout.fragment_health_layout_item, null);
			} else {
				view = convertView;
			}
			TextView title = (TextView) view.findViewById(R.id.hea_titile);
			TextView content = (TextView) view.findViewById(R.id.hea_content);
			ImageView imageView = (ImageView) view.findViewById(R.id.hea_image);
			TextView time = (TextView) view.findViewById(R.id.hea_time);
			LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
			title.setText(list.get(position).getTitle());
			imageView.setImageDrawable(getDrawable(list.get(position).getPicUrl()));
			content.setText("\t" + list.get(position).getDescription());
			time.setText(list.get(position).getCtime());

			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent intent = new Intent(getActivity(),
							HealthDetailAct.class);
					intent.putExtra("url", list.get(position).getUrl());
					startActivity(intent);
				}
			});

			return view;
		}
	}

	private Drawable getDrawable(final String source) {
		Drawable drawable = null;
		String fileString = Constant.IMAGE_PATH + String.valueOf(source.hashCode());
		// 判断SD卡里面是否存在图片文件
		if (new File(fileString).exists()) {
			// 获取本地文件返回Drawable
			drawable = Drawable.createFromPath(fileString);
			// 设置图片边界
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			return drawable;
		} else {
			// 启动新线程下载
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						DownloadUtils.download(source, Constant.IMAGE_PATH + String.valueOf(source.hashCode()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			return drawable;
		}
	}
}
