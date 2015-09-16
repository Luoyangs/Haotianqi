package com.haoxue.haotianqi.act;

import com.haoxue.haotianqi.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * 说明：
 * 作者：Luoyangs
 * 时间：2015-9-16
 */
@ContentView(R.layout.act_health_detail_layout)
public class HealthDetailAct extends Activity {

	@ViewInject(R.id.web)
	private WebView webView;
	@ViewInject(R.id.titilbar_right)
	private Button titilbar_right;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
		
		titilbar_right.setVisibility(View.GONE);
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		webView.loadUrl(url);
	}
	
	@OnClick(R.id.titilbar_left)
	public void onBack(View view){
		this.finish();
		overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
	}
}
