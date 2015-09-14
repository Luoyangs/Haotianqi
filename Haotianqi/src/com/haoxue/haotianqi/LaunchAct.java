package com.haoxue.haotianqi;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.act.GuideAct;
import com.haoxue.haotianqi.act.MainAct;
import com.haoxue.haotianqi.base.ShareDataHelper;
import com.haoxue.haotianqi.util.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

/**
 * 说明：启动页面（只定位，不加载程序数据） 
 * 作者： Luoyangs 
 * 时间： 2015年8月18日
 */
public class LaunchAct extends Activity {

	// 默认城市（程序第一次进入网络加载失败或者没有网络时候设置）
	private String normalCity = "北京";
	// 百度定位
	private LocationClient mLocClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_activity);

		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//加载消息
				load();
			}
		}, 4500);
	}

	private void load(){
		boolean isFirstIn = ShareDataHelper.getInstance(this).getLoadingInfo();
		String local = ShareDataHelper.getInstance(this).getLastCity();
		if (isFirstIn || local == null || local.length() == 0 ) {
			// 第一次进来或者之前定位失败：先定位，然后进入引导页
			mLocClient = new LocationClient(this);
			mLocClient.registerLocationListener(myListener);// 注册监听函数：

			LocationClientOption option = new LocationClientOption();
			option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
			option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
			option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
			option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
			mLocClient.setLocOption(option);
			mLocClient.start();

			// 保存定位的城市
			ShareDataHelper.getInstance(this).saveLoadingInfo(false);
			ShareDataHelper.getInstance(this).saveLastCity(normalCity);
			
			// 完成页面的跳转
			startActivity(new Intent(LaunchAct.this, GuideAct.class));
			finish();
		} else {
			// 完成页面的跳转
			startActivity(new Intent(LaunchAct.this, MainAct.class));
			finish();
		}
	}
	
	/** 定位SDK监听函数 */
	private class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location != null) {
				normalCity = location.getCity();
				if (normalCity == null) {
					ToastUtil.showShort(LaunchAct.this, "定位失败，请检查网络");
				} else {
					String[] str = normalCity.split("市");
					normalCity = str[0];
					if ("北京".equals(normalCity)) {
						ToastUtil.showShort(LaunchAct.this, "定位失败，默认为北京");
					}
				}
			}
		}
	}
	
	/** 拦截返回键 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
