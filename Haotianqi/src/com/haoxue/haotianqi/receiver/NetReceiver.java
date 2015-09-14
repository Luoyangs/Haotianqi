package com.haoxue.haotianqi.receiver;

import com.haoxue.haotianqi.util.NetWorkUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;

/** 
 *	说明：网络广播接收器
 *	作者： Luoyangs
 *	时间： 2015年8月24日
 */
public class NetReceiver extends BroadcastReceiver {

	private View view;
	
	public NetReceiver(View view){
		this.view = view;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			boolean isConnected = NetWorkUtil.isNetworkAvailable(context);
			if (isConnected) {
				view.setVisibility(View.GONE);
			}else{
				view.setVisibility(View.VISIBLE);
			}
		}
	}

}
