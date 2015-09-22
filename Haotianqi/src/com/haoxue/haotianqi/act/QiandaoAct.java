package com.haoxue.haotianqi.act;

import com.haoxue.haotianqi.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

/**
 * 说明:签到
 * 作者:Luoyangs
 * 时间:2015-9-21
 */
@ContentView(R.layout.act_qiandao_layout)
public class QiandaoAct extends Activity{

	@ViewInject(R.id.rl_payInfo)
	private RelativeLayout rl_payInfo;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if (msg.what == 0x01) {
				startAnimation();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
		newThread();
	}

	@OnClick(R.id.titilbar_left)
	public void onClick(View view){
		this.finish();
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}
	
	private void newThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0x01;
				handler.sendMessage(msg);

			}
		}).start();
	}
	
	private void startAnimation() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_down_in);
		anim.setDuration(1200);
		anim.setFillAfter(true);
		rl_payInfo.startAnimation(anim);
	}
}
