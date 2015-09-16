package com.haoxue.haotianqi.act.frg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.base.Constant;
import com.haoxue.haotianqi.bean.HisResponse;
import com.haoxue.haotianqi.bean.HistoryDay;
import com.haoxue.haotianqi.util.DensityUtil;
import com.haoxue.haotianqi.util.DownloadUtils;
import com.haoxue.haotianqi.util.NetWorkUtil;
import com.haoxue.haotianqi.util.ReqUtil;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.LoadingDialog;
import com.haoxue.haotianqi.view.RefreshLayout;
import com.haoxue.haotianqi.view.RefreshLayout.OnLoadListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/** 
 *	说明：历史今天
 *	作者： Luoyangs
 *	时间： 2015年8月24日
 */
@SuppressLint("InflateParams")
public class HistoryFrag extends Fragment implements OnRefreshListener,OnLoadListener {

	private RefreshLayout swipeLayout;
	private ListView listView;
	private static List<HistoryDay> list;
	private static HisResponse response;
	private MyAdapter adapter;
	private LoadingDialog dialog;//加载框
	private static int month;
	private static int day;
	
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

	
	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history_layout, null, false);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		listView = (ListView) view.findViewById(R.id.list);
		list = new ArrayList<HistoryDay>();
		response = new HisResponse();
		
		Calendar calendar = Calendar.getInstance();
		month = calendar.get(Calendar.MONTH) + 1; 
		day = calendar.get(calendar.DAY_OF_MONTH);
		
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
		list = JSON.parseArray(response.getData(), HistoryDay.class);
		adapter = new MyAdapter(getActivity(), list);
		listView.setAdapter(adapter);
	}
	
	/**网络请求*/
	private void loadData(final String month,final String day){
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
						String jsonString = NetWorkUtil.doGet(ReqUtil.getHistoryRequestURL(month,day),null);
						response = JSON.parseObject(jsonString, HisResponse.class);
						if (response != null && !response.getData().equals("[]")) {
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
		public List<HistoryDay> list;
		public LayoutInflater layoutInflater;

		public MyAdapter(Context context, List<HistoryDay> list) {
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
			TextView title = (TextView) view.findViewById(R.id.hi_titile);
			TextView content = (TextView) view.findViewById(R.id.hi_content);
			title.setText(list.get(position).getName());
			
			String str = list.get(position).getContent();
			List<Integer> index = new ArrayList<Integer>();
			for (int i = 0; i < str.length() - 2; i++) {
				if (str.substring(i, i+3).equals("src")) {
					index.add(i);
				}
			}
			final String head = "http://api.46644.com";
			int cnt = 0;
			for(int i=0;i<index.size();i++)
			{
				int k= index.get(i) + cnt;
				String preStr = str.substring(0, k+5);
				String steStr = str.substring(k+5);
				
				cnt = head.length();
				str = preStr + head + steStr;
			}
			  
			content.setText(Html.fromHtml(str,imageGetter,null));
			return view;
		}

	}
	
	final Html.ImageGetter imageGetter = new Html.ImageGetter() {
        public Drawable getDrawable(final String source) {
            Drawable drawable = null;
            String fileString = Constant.IMAGE_PATH + String.valueOf(source.hashCode());
            //判断SD卡里面是否存在图片文件
            if (new File(fileString).exists()) {
                //获取本地文件返回Drawable
                drawable=Drawable.createFromPath(fileString);
                //设置图片边界
                int sc_width = new DensityUtil(getActivity()).getScreenWidth();
                drawable.setBounds(0, 0, sc_width, sc_width*9/16);
                return drawable;
            }else {
                //启动新线程下载
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
        };
    };
	 
	@Override
	public void onLoad() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				swipeLayout.setLoading(false);
				day -= 1;
				loadData(String.valueOf(month), String.valueOf(day));
			}
		}, 2000);
	}

	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@SuppressWarnings("static-access")
			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				list.clear();
				
				Calendar calendar = Calendar.getInstance();
				month = calendar.get(Calendar.MONTH) + 1; 
				day = calendar.get(calendar.DAY_OF_MONTH);
				loadData(String.valueOf(month), String.valueOf(day));
				
				swipeLayout.setRefreshing(false);
			}
		}, 2000);
	}
}
