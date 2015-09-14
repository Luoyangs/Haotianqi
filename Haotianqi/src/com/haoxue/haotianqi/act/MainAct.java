package com.haoxue.haotianqi.act;

import com.alibaba.fastjson.JSON;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.act.frg.CityFrag;
import com.haoxue.haotianqi.act.frg.HappyFrag;
import com.haoxue.haotianqi.act.frg.HealthFrag;
import com.haoxue.haotianqi.act.frg.LifeIndexFrag;
import com.haoxue.haotianqi.act.frg.MainFrag;
import com.haoxue.haotianqi.act.frg.MovieFrag;
import com.haoxue.haotianqi.act.frg.WhereFrag;
import com.haoxue.haotianqi.act.frg.XingzuoFrag;
import com.haoxue.haotianqi.base.Constant;
import com.haoxue.haotianqi.base.ShareDataHelper;
import com.haoxue.haotianqi.bean.ResponseBean;
import com.haoxue.haotianqi.receiver.NetReceiver;
import com.haoxue.haotianqi.util.NetWorkUtil;
import com.haoxue.haotianqi.util.ReqUtil;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.CustomDialog;
import com.haoxue.haotianqi.view.LoadingDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** 
 *	说明：主页面
 *	作者： Luoyangs
 *	时间： 2015年8月19日
 */
@ContentView(R.layout.homepager_main_activity)
public class MainAct extends FragmentActivity{

	//底部菜单
	@ViewInject(R.id.bottom_weathertext)
	public TextView buttom_menu_weather;
	@ViewInject(R.id.bottom_citymanager)
	public TextView buttom_menu_city;
	@ViewInject(R.id.bottom_todaycan)
	public TextView buttom_menu_index;	
	@ViewInject(R.id.drawerlayout_main)
	private DrawerLayout drawerlayout;
	@ViewInject(R.id.left_drawer)
	private View left_menu;
	@ViewInject(R.id.tip)
	private RelativeLayout tip;//网络信息提示

	private ResponseBean response = null;//天气数据
	private ResponseBean response2 = null;
	
	private NetReceiver receiver;//网络监听器
	private IntentFilter filter;
	private String local = "";//默认城市
	
	private LoadingDialog dialog;
	
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.LOAD_OK:
				dialog.dismiss();
				ToastUtil.showShort(MainAct.this, "天气更新完成");
				break;
			case Constant.LOAD_FAIL:
				dialog.dismiss();
				ToastUtil.showShort(MainAct.this, "加载天气失败");
				break;
			case Constant.LOAD_NO_NET:
				dialog.dismiss();
				ToastUtil.showShort(MainAct.this, "当前网络不好，请稍后重试");
				break;
			}
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		receiver = new NetReceiver(tip);
		filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver , filter);
		//获取位置
		local = ShareDataHelper.getInstance(this).getLastCity();
		//获取天气
		String jsonString = ShareDataHelper.getInstance(this).getWether();
		if (jsonString != null && jsonString.length() > 0) {
			response = JSON.parseObject(jsonString, ResponseBean.class);
		}
		
		//设置默认界面
		if (savedInstanceState == null) {
			this.moveTo(new MainFrag(0));
		}
	}
	
	/**获取天气*/
	public ResponseBean getLocalWether(){
		if (response != null) {
			return response;
		}else{
			//天气加载失败，重新尝试加载
			dialog = LoadingDialog.getInstance(this, "正在更新天气...");
			dialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(2000);// 注：异步线程中不能设置UI
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sendRequest();
				}
			}).start();
			return response;
		}
	}
	
	// 获取天气预报
	private void sendRequest() {
		String jsonString = "";
		try {
			if (!NetWorkUtil.isNetworkAvailable(MainAct.this)) {
				handler.sendEmptyMessage(Constant.LOAD_NO_NET);
			} else {
				jsonString = NetWorkUtil.doGet(ReqUtil.getRequestURL(local),null);
				response = JSON.parseObject(jsonString, ResponseBean.class);
				if (response != null && response.getError() == 0) {
					// 保存到本地
					ShareDataHelper.getInstance(MainAct.this).saveWetherInfo(jsonString);
					handler.sendEmptyMessage(Constant.LOAD_OK);
				}
			}
		} catch (Exception e) {
			handler.sendEmptyMessage(Constant.LOAD_FAIL);
		}
	}
	
	@OnClick(R.id.homep_menu)
	public void openLeftMenu(View view){
		if (drawerlayout.isDrawerOpen(left_menu)) {
			drawerlayout.closeDrawer(left_menu);
		} else {
			drawerlayout.openDrawer(left_menu);
		}
	}
	
	@OnClick(R.id.tip)
	public void setNetwork(View view){
		//设置网络
		
	}
	
	@OnClick({R.id.homep_seach,R.id.changInfo,R.id.set,R.id.pifu,R.id.tongzhi,R.id.app})
	public void doSkip(View view){
		switch (view.getId()) {
		case R.id.homep_seach:// 打开右侧的搜索
			startActivity(new Intent(MainAct.this, SeachAct.class));
			break;
		case R.id.changInfo://修改个人信息
			startActivity(new Intent(MainAct.this, UserInfoAct.class));
			break;
		case R.id.set://设置
			startActivity(new Intent(MainAct.this, SetAct.class));
			break;
		case R.id.pifu://皮肤管理
			startActivity(new Intent(MainAct.this, SkinAct.class));
			break;
		case R.id.tongzhi://通知管理
			startActivity(new Intent(MainAct.this, NoteAct.class));
			break;
		case R.id.app://应用管理
			startActivity(new Intent(MainAct.this, AppStoreAct.class));
			break;
		}
	}
	
	@OnClick({R.id.bottom_todaycan,R.id.bottom_citymanager,R.id.bottom_weathertext})
	public void buttomMenu(View view){
		switch (view.getId()) {
		case R.id.bottom_todaycan://天气指数
			buttom_menu_index.setTextSize(18);
			buttom_menu_city.setTextSize(16);
			buttom_menu_weather.setTextSize(16);
			buttom_menu_index.setTextColor(Color.parseColor(getString(R.string.color_bottombg)));
			buttom_menu_city.setTextColor(Color.parseColor(getString(R.string.color_bottombgn)));
			buttom_menu_weather.setTextColor(Color.parseColor(getString(R.string.color_bottombgn)));
			this.moveTo(new LifeIndexFrag());
			break;
		case R.id.bottom_citymanager://城市管理
			buttom_menu_index.setTextSize(16);
			buttom_menu_city.setTextSize(18);
			buttom_menu_weather.setTextSize(16);
			buttom_menu_index.setTextColor(Color.parseColor(getString(R.string.color_bottombgn)));
			buttom_menu_city.setTextColor(Color.parseColor(getString(R.string.color_bottombg)));
			buttom_menu_weather.setTextColor(Color.parseColor(getString(R.string.color_bottombgn)));
			this.moveTo(new CityFrag());
			break;
		case R.id.bottom_weathertext://天气预报
			buttom_menu_index.setTextSize(16);
			buttom_menu_city.setTextSize(16);
			buttom_menu_weather.setTextSize(18);
			buttom_menu_index.setTextColor(Color.parseColor(getString(R.string.color_bottombgn)));
			buttom_menu_city.setTextColor(Color.parseColor(getString(R.string.color_bottombgn)));
			buttom_menu_weather.setTextColor(Color.parseColor(getString(R.string.color_bottombg)));
			this.moveTo(new MainFrag(0));
			break;
		}
	}
	
	@OnClick({R.id.gv_tianqi,R.id.gv_jiankang,R.id.gv_jingdian,R.id.gv_yingshi,R.id.gv_xingzuo,R.id.gv_kuaile})
	public void ItemClickFor(View view){
		switch (view.getId()) {
		case R.id.gv_tianqi://天气（首页）
			this.moveTo(new MainFrag(0));
			break;
		case R.id.gv_jiankang ://健康
			this.moveTo(new HealthFrag());
			break;
		case R.id.gv_jingdian://景点
			this.moveTo(new WhereFrag());
			break;
		case R.id.gv_yingshi://影视
			this.moveTo(new MovieFrag());
			break;
		case R.id.gv_xingzuo://星座
			this.moveTo(new XingzuoFrag());
			break;
		case R.id.gv_kuaile://每日一笑
			this.moveTo(new HappyFrag());
			break;
		}
	}
	
	/**退出系统*/
	@OnClick(R.id.exit)
	public void exit(View view){
		CustomDialog dialog = new CustomDialog.Builder(MainAct.this).setTitle("提示")
				.setMessage("真的要离开么？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss(); 
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.create();
		dialog.show();
	}
	
	/**页面跳转*/
	public void moveTo(Fragment target){
		getSupportFragmentManager().beginTransaction().replace(R.id.fragmentlayout, target).commitAllowingStateLoss();
		if (drawerlayout.isDrawerOpen(left_menu)) {
			drawerlayout.closeDrawer(left_menu);
		} 
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
