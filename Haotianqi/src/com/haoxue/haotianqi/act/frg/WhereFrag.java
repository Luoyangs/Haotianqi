package com.haoxue.haotianqi.act.frg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.act.ShowImageAct;
import com.haoxue.haotianqi.base.Constant;
import com.haoxue.haotianqi.bean.WhereBook;
import com.haoxue.haotianqi.bean.WhereBookResponse;
import com.haoxue.haotianqi.util.NetWorkUtil;
import com.haoxue.haotianqi.util.ReqUtil;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.CuListView;
import com.haoxue.haotianqi.view.RefreshLayout;
import com.haoxue.haotianqi.view.RefreshLayout.OnLoadListener;
import com.lidroid.xutils.BitmapUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 *	说明：景点
 *	作者： Luoyangs
 *	时间： 2015年8月24日
 */
public class WhereFrag extends Fragment implements OnRefreshListener,OnLoadListener {
	
	private RefreshLayout swipeLayout;
	private LinearLayout layout0;
	private LinearLayout layout;
	private ImageView seach;
	private AutoCompleteTextView find;//搜索框
	private Button btn;
	private ImageView iv_quit;
	private CuListView listview;
	private View view1;//下划线
	
	private MyAdapter adapter;
	private static int page = 1;
	private BitmapUtils bitmapUtils;
	
	private static WhereBookResponse response;
	private List<WhereBook> list;
	private static List<String> whereList = new ArrayList<String>();//景点集合
	
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
		View view = inflater.inflate(R.layout.fragment_where_layout, null, false);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		layout0 = (LinearLayout) view.findViewById(R.id.title_bar0);
		layout = (LinearLayout) view.findViewById(R.id.title_bar);
		seach = (ImageView) view.findViewById(R.id.iv_seach);
		find = (AutoCompleteTextView) view.findViewById(R.id.find);
		btn = (Button) view.findViewById(R.id.btn);
		iv_quit = (ImageView) view.findViewById(R.id.iv_quit);
		listview = (CuListView) view.findViewById(R.id.listview);
		view1 = (View) view.findViewById(R.id.view);
		
		response = new WhereBookResponse();
		list = new ArrayList<WhereBook>();
		bitmapUtils = new BitmapUtils(getActivity());
		
		layout0.setVisibility(View.GONE);
		layout.setVisibility(View.VISIBLE);
		view1.setVisibility(View.VISIBLE);
		
		seach.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				layout0.setVisibility(View.VISIBLE);
				layout.setVisibility(View.GONE);
				view1.setVisibility(View.GONE);
			}
		});
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String cityName = find.getText().toString();
				
			}
		});
		
		iv_quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				find.setText("");
				layout0.setVisibility(View.GONE);
				layout.setVisibility(View.VISIBLE);
				view1.setVisibility(View.VISIBLE);
			}
		});
		
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
		list = response.getData().getBooks();
		adapter = new MyAdapter(getActivity(), list);
		listview.setAdapter(adapter);
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
						String jsonString = NetWorkUtil.doGet(ReqUtil.getWhereBookRequestURL(page),null,"apikey",Constant.WHERE_BOOK_APPKEY);
						response = JSON.parseObject(jsonString, WhereBookResponse.class);
						if (response != null && response.getErrcode() == 0) {
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
	
	private class MyAdapter extends BaseAdapter{

		private List<WhereBook> list;
		private LayoutInflater layoutInflater;

		public MyAdapter(Context context, List<WhereBook> list) {
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
				view = layoutInflater.inflate(R.layout.fragment_where_list_item, null);
			} else {
				view = convertView;
			}
			TextView name = (TextView) view.findViewById(R.id.tv_name);
			TextView time = (TextView) view.findViewById(R.id.tv_time);
			TextView title = (TextView) view.findViewById(R.id.tv_title);
			ImageView headImg = (ImageView) view.findViewById(R.id.headImg);
			TextView content = (TextView) view.findViewById(R.id.tv_content);
			ImageView img = (ImageView) view.findViewById(R.id.iv_img);
			TextView tv_view = (TextView) view.findViewById(R.id.tv_view);
			TextView tv_comm = (TextView) view.findViewById(R.id.tv_comm);
			TextView tv_like = (TextView) view.findViewById(R.id.tv_like);
			
			final WhereBook bean = list.get(position);
			name.setText(bean.getUserName());
			time.setText(bean.getStartTime());
			title.setText(bean.getTitle());
			content.setText("\t"+bean.getText());
			tv_view.setText(bean.getViewCount()+"");
			tv_comm.setText(bean.getCommentCount()+"");
			tv_like.setText(bean.getLikeCount()+"");
			bitmapUtils.display(headImg, bean.getUserHeadImg());
			bitmapUtils.display(img, bean.getHeadImage());
			
			//查看大图
			img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(getActivity(),ShowImageAct.class);
					intent.putExtra("image",bean.getHeadImage());
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			});
			return view;
		}
		
	}

	@Override
	public void onLoad() {
		Log.e("Main", "Load");
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
