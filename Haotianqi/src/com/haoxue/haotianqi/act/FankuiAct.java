package com.haoxue.haotianqi.act;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.util.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

/**
 * 说明:反馈
 * 作者:Luoyangs
 * 时间:2015-9-21
 */
@ContentView(R.layout.act_fankui_layout)
public class FankuiAct extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
	}
	
	@OnClick({R.id.btn1,R.id.btn2})
	public void onClick(View view){
		if (view.getId() == R.id.btn2) {
			ToastUtil.showShort(this, "提交成功，谢谢您的宝贵意见");
			(new Handler()).postDelayed(new Runnable() {
				
				@Override
				public void run() {
					finishPage();
				}
			}, 1000);
		}else if (view.getId() == R.id.btn1){
			finishPage();
		}else if (view.getId() == R.id.titilbar_left){
			finishPage();
		}
	}
	
	private void finishPage(){
		this.finish();
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}
}
