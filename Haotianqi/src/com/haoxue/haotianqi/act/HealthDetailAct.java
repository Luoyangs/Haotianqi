package com.haoxue.haotianqi.act;

import com.haoxue.haotianqi.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

/**
 * 说明：
 * 作者：Luoyangs
 * 时间：2015-9-16
 */
@SuppressLint("SetJavaScriptEnabled")
@ContentView(R.layout.act_health_detail_layout)
public class HealthDetailAct extends Activity {

	@ViewInject(R.id.web)
	private WebView webView;
	@ViewInject(R.id.titilbar_right)
	private Button titilbar_right;
	@ViewInject(R.id.titilbar_title)
	private TextView titilbar_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
		
		titilbar_right.setVisibility(View.GONE);
		titilbar_title.setText("健康资讯");
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		webView.loadUrl(url);
		//启用支持javascript
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
	}
	
	@OnClick(R.id.titilbar_left)
	public void onBack(View view){
		this.finish();
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}
}
