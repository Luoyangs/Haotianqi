package com.haoxue.haotianqi.act;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.adapter.HisCityAdapter;
import com.haoxue.haotianqi.adapter.HotCityAdapter;
import com.haoxue.haotianqi.adapter.SearchCityListAdapter;
import com.haoxue.haotianqi.base.CityListDBHelper;
import com.haoxue.haotianqi.base.ShareDataHelper;
import com.haoxue.haotianqi.bean.CityBean;
import com.haoxue.haotianqi.bean.CityWetherBean;
import com.haoxue.haotianqi.util.PingYinUtil;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.CustomDialog;
import com.haoxue.haotianqi.view.LetterListView;
import com.haoxue.haotianqi.view.LetterListView.OnTouchingLetterChangedListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/** 
 *	说明：添加城市
 *	作者： Luoyangs
 *	时间： 2015年8月26日
 */
@ContentView(R.layout.city_layout)
public class AddCityAct extends Activity implements OnScrollListener {

	@ViewInject(R.id.list_view)
	private ListView personListView;
	@ViewInject(R.id.search_result)
	private ListView resultListView;
	private TextView overlay; // 对话框首字母textview
	@ViewInject(R.id.MyLetterListView01)
	private LetterListView letterListView;
	@ViewInject(R.id.tv_noresult)
	private TextView noresult;
	@ViewInject(R.id.sh)
	private EditText seachText;
	
	private OverlayThread overlayThread; // 显示首字母对话框
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	
	private ArrayList<CityBean> allCity_lists; // 所有城市列表
	private ArrayList<CityBean> city_lists;// 城市列表
	private ArrayList<CityBean> city_hot;
	private ArrayList<CityBean> city_result;
	private ArrayList<CityWetherBean> city_history;
	
	private SearchCityListAdapter resultListAdapter;
	private BaseAdapter adapter;
	private HisCityAdapter hisAdapter;//已经选择的城市适配器
	private HotCityAdapter hotCityAdapter;//热门城市适配器
	
	//  百度定位:默认城市（程序第一次进入网络加载失败或者没有网络时候设置）
	private String normalCity = "北京";
	private LocationClient mLocClient = null;
	private MyLocationListenner myListener = null;
	
	private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
	private boolean isNeedFresh;
	private boolean isScroll = false;
	private boolean mReady;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//注册
		ViewUtils.inject(this);
		
		allCity_lists = new ArrayList<CityBean>();
		city_hot = new ArrayList<CityBean>();
		city_result = new ArrayList<CityBean>();
		city_history = new ArrayList<CityWetherBean>();
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		isNeedFresh = true;
		
		overlayThread = new OverlayThread();
		resultListAdapter = new SearchCityListAdapter(this, city_result);
		letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
		seachText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString() == null || "".equals(s.toString())) {
					letterListView.setVisibility(View.VISIBLE);
					personListView.setVisibility(View.VISIBLE);
					resultListView.setVisibility(View.GONE);
					noresult.setVisibility(View.GONE);
				}else{
					city_result.clear();
					letterListView.setVisibility(View.GONE);
					personListView.setVisibility(View.GONE);
					getSearchResult(s.toString());
					if (city_result.size() <= 0) {
						noresult.setVisibility(View.VISIBLE);
						resultListView.setVisibility(View.GONE);
					}else{
						noresult.setVisibility(View.GONE);
						resultListView.setVisibility(View.VISIBLE);
						resultListAdapter.notifyDataSetChanged();
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		personListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (position >= 4) {
					ArrayList<String> lists = new ArrayList<String>();
					if (city_history != null && city_history.size() >0) {
						for (int i = 0; i < city_history.size(); i++) {
							lists.add(city_history.get(i).getCity());
						}
					}
					if (lists != null && lists.contains(allCity_lists.get(position).getName())) {
						ToastUtil.showShort(getApplication(), allCity_lists.get(position).getName()+"已经添加，不得重复添加");
						return;
					}
					//添加城市
					CityWetherBean cityBean = null;
					cityBean = new CityWetherBean(allCity_lists.get(position).getName(), "1");
					city_history.add(cityBean);
					String jsonString = JSON.toJSONString(city_history);
					ShareDataHelper.getInstance(AddCityAct.this).saveCitys(jsonString);
					hisAdapter.notifyDataSetChanged();
					finishPage();
				}
			}
		});
		locateProcess = 1;
		personListView.setAdapter(adapter);
		personListView.setOnScrollListener(this);
		
		//搜索结果
		resultListView.setAdapter(resultListAdapter);
		resultListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) { 
				ArrayList<String> lists = new ArrayList<String>();
				if (city_history != null && city_history.size() >0) {
					for (int i = 0; i < city_history.size(); i++) {
						lists.add(city_history.get(i).getCity());
					}
				}
				if (lists != null && lists.contains(city_result.get(position).getName())) {
					ToastUtil.showShort(getApplication(), city_result.get(position).getName()+"已经添加，不得重复添加");
					return;
				}
				//添加城市
				CityWetherBean cityBean = new CityWetherBean(city_result.get(position).getName(), "1");
				city_history.add(cityBean);
				String jsonString = JSON.toJSONString(city_history);
				ShareDataHelper.getInstance(AddCityAct.this).saveCitys(jsonString);
				hisAdapter.notifyDataSetChanged();
				finishPage();
			}
		});
		
		initOverlay();
		cityInit();
		hotCityInit();
		hisCityInit();
		setAdapter(allCity_lists, city_hot, city_history);
		
		//初始化百度地图
		mLocClient = new LocationClient(getApplication());
		myListener = new MyLocationListenner();
		mLocClient.registerLocationListener(myListener);
		initLocalWhere();
		mLocClient.start();
	}
	
	private void setAdapter(List<CityBean> list, List<CityBean> hotList, List<CityWetherBean> hisCity) {
		adapter = new ListAdapter(this, list, hotList, hisCity);
		personListView.setAdapter(adapter);
	}
	
	/**获取搜索城市的结果*/
	@SuppressWarnings("unchecked")
	private void getSearchResult(String keyword){
		CityListDBHelper helper = new CityListDBHelper(this);
		try {
			helper.createDataBase();
			SQLiteDatabase database = helper.getWritableDatabase();
			Cursor cursor = database.rawQuery("select * from city where name like \"%" + keyword
							+ "%\" or pinyin like \"%" + keyword + "%\"", null);
			CityBean cityBean = null;
			while (cursor.moveToNext()) {
				cityBean = new CityBean(cursor.getString(1), cursor.getString(2));
				city_result.add(cityBean);
			}
			cursor.close();
			database.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(city_result, comparator);
	}
	
	/**a~z排序*/
	@SuppressWarnings("rawtypes")
	private Comparator comparator = new Comparator<CityBean>() {
		@Override
		public int compare(CityBean lhs, CityBean rhs) {
			String a = lhs.getPinyi().substring(0, 1);
			String b = rhs.getPinyi().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}
		}
	};
	
	/**右侧字母滑动提示*/
	private class LetterListViewListener implements OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			isScroll = false;
			if (alphaIndexer.get(s) != null) {
				
				int position = alphaIndexer.get(s);
				personListView.setSelection(position);
				overlay.setText(s);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1000);
			}
		}
	}
	
	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}
	
	/**初始化汉语拼音首字母弹出提示框*/
	private void initOverlay() {
		mReady = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}
	
	private void cityInit() {
		CityBean city = new CityBean("定位", "0"); // 当前定位城市
		allCity_lists.add(city);
		city = new CityBean("最近", "1"); // 最近访问的城市
		allCity_lists.add(city);
		city = new CityBean("热门", "2"); // 热门城市
		allCity_lists.add(city);
		city = new CityBean("全部", "3"); // 全部城市
		allCity_lists.add(city);
		city_lists = getCityList();
		allCity_lists.addAll(city_lists);
	}

	@SuppressWarnings("unchecked")
	private ArrayList<CityBean> getCityList() {
		CityListDBHelper dbHelper = new CityListDBHelper(this);
		ArrayList<CityBean> list = new ArrayList<CityBean>();
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select * from city", null);
			CityBean city;
			while (cursor.moveToNext()) {
				city = new CityBean(cursor.getString(1), cursor.getString(2));
				list.add(city);
			}
			cursor.close();
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(list, comparator);
		return list;
	}
	
	/**已经选择的城市*/
	private void hisCityInit() {
		String jsonString = ShareDataHelper.getInstance(this).getCitys();
		if (jsonString != null && jsonString.length() >0) {
			List<CityWetherBean> list = JSON.parseArray(jsonString, CityWetherBean.class);
			city_history = (ArrayList<CityWetherBean>) list;
		}
	}
	
	/**  热门城市 */
	public void hotCityInit() {
		CityBean city = new CityBean("上海", "2");
		city_hot.add(city);
		city = new CityBean("北京", "2");
		city_hot.add(city);
		city = new CityBean("广州", "2");
		city_hot.add(city);
		city = new CityBean("深圳", "2");
		city_hot.add(city);
		city = new CityBean("武汉", "2");
		city_hot.add(city);
		city = new CityBean("天津", "2");
		city_hot.add(city);
		city = new CityBean("西安", "2");
		city_hot.add(city);
		city = new CityBean("南京", "2");
		city_hot.add(city);
		city = new CityBean("杭州", "2");
		city_hot.add(city);
		city = new CityBean("成都", "2");
		city_hot.add(city);
		city = new CityBean("重庆", "2");
		city_hot.add(city);
	}
	
	private class ListAdapter extends BaseAdapter {

		private Context context;
		private LayoutInflater inflater;
		private List<CityBean> list;
		private List<CityBean> hotList;
		private List<CityWetherBean> hisCity;
		final int VIEW_TYPE = 5;
		
		public ListAdapter(Context context,List<CityBean> list,List<CityBean> hotList,List<CityWetherBean> hisCity){
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			this.hotList = hotList;
			this.hisCity = hisCity;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = getAlpha(list.get(i).getPinyi());
				// 上一个汉语拼音首字母，如果不存在为" "
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1).getPinyi()) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getPinyi());
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}
		
		@Override
		public int getViewTypeCount() {
			return VIEW_TYPE;
		}
		
		@Override
		public int getItemViewType(int position) {
			return position < 4 ? position : 4;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("CutPasteId")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int viewType = getItemViewType(position);
			switch (viewType) {
			case 0:// 定位
				convertView = inflater.inflate(R.layout.frist_list_item, parent,false);
				TextView locateHint = (TextView) convertView .findViewById(R.id.locateHint);
				final TextView city = (TextView) convertView.findViewById(R.id.lng_city);
				ProgressBar pbLocate = (ProgressBar) convertView.findViewById(R.id.pbLocate);
				city.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (locateProcess == 2) { //定位已经完成
							ToastUtil.showShort(getApplication(), city.getText().toString());
						}else if (locateProcess == 3) { // 定位失败，重新定位
							locateProcess = 1;
							personListView.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							mLocClient.stop();
							isNeedFresh = true;
							initLocalWhere();
							normalCity = "";
							mLocClient.start();
						}
					}
				});
				if (locateProcess == 1) { // 正在定位
					locateHint.setText("正在定位");
					city.setVisibility(View.GONE);
					pbLocate.setVisibility(View.VISIBLE);
				} else if (locateProcess == 2) { // 定位成功
					locateHint.setText("当前定位城市");
					city.setVisibility(View.VISIBLE);
					city.setText(normalCity);
					mLocClient.stop();
					pbLocate.setVisibility(View.GONE);
				} else if (locateProcess == 3) {
					locateHint.setText("未定位到城市,请选择");
					city.setVisibility(View.VISIBLE);
					city.setText("重新定位");
					pbLocate.setVisibility(View.GONE);
				}
				break;
			case 1:// 已经选定的城市
				convertView = inflater.inflate(R.layout.recent_city, null);
				GridView rencentCity = (GridView) convertView.findViewById(R.id.recent_city);
				TextView recentHint = (TextView) convertView.findViewById(R.id.recentHint);
				recentHint.setText("已经选定的城市");
				hisAdapter = new HisCityAdapter(context, this.hisCity);
				rencentCity.setAdapter(hisAdapter);
				rencentCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						ToastUtil.showShort(getApplication(), city_history.get(position).getCity());
					}
				});
				rencentCity.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
						new CustomDialog.Builder(AddCityAct.this).setTitle("提示")
								.setMessage("真的要删除当前选定么？")
								.setNegativeButton("取消", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								})
								.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss(); 
										city_history.remove(position);
										String jsonString = JSON.toJSONString(city_history);
										ShareDataHelper.getInstance(AddCityAct.this).saveCitys(jsonString);
										hisAdapter.notifyDataSetChanged();
									}
								})
								.create().show();
						return true;
					}
				});
				break;
			case 2:// 热门
				convertView = inflater.inflate(R.layout.recent_city, null);
				GridView hotCity = (GridView) convertView.findViewById(R.id.recent_city); 
				TextView hotHint = (TextView) convertView.findViewById(R.id.recentHint);
				hotHint.setText("热门城市");
				hotCityAdapter = new HotCityAdapter(context, this.hotList);
				hotCity.setAdapter(hotCityAdapter);
				hotCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						ArrayList<String> lists = new ArrayList<String>();
						if (city_history != null && city_history.size() >0) {
							for (int i = 0; i < city_history.size(); i++) {
								lists.add(city_history.get(i).getCity());
							}
						}
						if (lists != null && lists.contains(city_hot.get(position).getName())) {
							ToastUtil.showShort(getApplication(), city_hot.get(position).getName()+"已经添加，不得重复添加");
							return;
						}
						//添加城市
						CityWetherBean cityBean = new CityWetherBean(city_hot.get(position).getName(), "1");
						city_history.add(cityBean);
						String jsonString = JSON.toJSONString(city_history);
						ShareDataHelper.getInstance(AddCityAct.this).saveCitys(jsonString);
						hisAdapter.notifyDataSetChanged();
						finishPage();
					}
				});
				break;
			case 3:// 全部
				convertView = inflater.inflate(R.layout.total_item, null);
				break;
			default:
				ViewHolder holder = null;
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.list_item, null);
					holder = new ViewHolder();
					holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
					holder.name = (TextView) convertView.findViewById(R.id.name);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				if (position >= 1) {
					holder.name.setText(list.get(position).getName());
					String currentStr = getAlpha(list.get(position).getPinyi());
					String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getPinyi()) : " ";
					if (!previewStr.equals(currentStr)) {
						holder.alpha.setVisibility(View.VISIBLE);
						holder.alpha.setText(currentStr);
					} else {
						holder.alpha.setVisibility(View.GONE);
					}
				}
				break;
			}
			return convertView;
		}
		
		class ViewHolder {
			TextView alpha; // 首字母标题
			TextView name; // 城市名字
		}
		
	}
	
	@Override
	protected void onStop() {
		//停止定位
		mLocClient.stop();
		super.onStop();
	}
	
	/** 获得汉语拼音首字母 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else if (str.equals("0")) {
			return "定位";
		} else if (str.equals("1")) {
			return "最近";
		} else if (str.equals("2")) {
			return "热门";
		} else if (str.equals("3")) {
			return "全部";
		} else {
			return "#";
		}
	}
	
	/**实例化百度定位*/
	private void initLocalWhere(){
		// 第一次进来或者之前定位失败：先定位，然后进入引导页
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);// 注册监听函数：

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(10000);// 设置发起定位请求的间隔时间为10分钟
		option.setAddrType("all");
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocClient.setLocOption(option);
	}
	
	/** 定位SDK监听函数 */
	private class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (!isNeedFresh) {
				return;
			}
			isNeedFresh = false;
			if (location != null) {
				normalCity = location.getCity();
				if (normalCity == null) {
					locateProcess = 3; // 定位失败
					personListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					return;
				} else {
					String[] str = normalCity.split("市");
					normalCity = str[0];
					locateProcess = 2; // 定位成功
					personListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					//添加定位城市
					ArrayList<String> lists = new ArrayList<String>();
					if (city_history != null && city_history.size() >0) {
						for (int i = 0; i < city_history.size(); i++) {
							lists.add(city_history.get(i).getCity());
						}
					}
					if (lists != null && lists.contains(normalCity)) {
						return;
					}
					CityWetherBean cityBean = new CityWetherBean(normalCity, "1");
					city_history.add(cityBean);
					String jsonString = JSON.toJSONString(city_history);
					ShareDataHelper.getInstance(AddCityAct.this).saveCitys(jsonString);
					hisAdapter.notifyDataSetChanged();
					
					//判断定位是否发生变化
					if (!ShareDataHelper.getInstance(AddCityAct.this).getLastCity().equals(normalCity)) {
						ShareDataHelper.getInstance(AddCityAct.this).saveLastCity(normalCity);
					}
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
			isScroll = true;
		}
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		if (!isScroll) {
			return;
		}

		if (mReady) {
			String text;
			String name = allCity_lists.get(firstVisibleItem).getName();
			String pinyin = allCity_lists.get(firstVisibleItem).getPinyi();
			if (firstVisibleItem < 4) {
				text = name;
			} else {
				text = PingYinUtil.converterToFirstSpell(pinyin).substring(0, 1).toUpperCase();
			}
			overlay.setText(text);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			// 延迟一秒后执行，让overlay为不可见
			handler.postDelayed(overlayThread, 1000);
		}
	}
	
	@OnClick(R.id.goback)
	public void goBack(View view){
		finishPage();
	}
	
	/**离开本页面*/
	private void finishPage(){
		this.finish();
		overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
	}
	
}
