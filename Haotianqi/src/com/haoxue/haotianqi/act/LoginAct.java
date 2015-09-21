package com.haoxue.haotianqi.act;

import com.haoxue.haotianqi.R;
import com.haoxue.haotianqi.util.ToastUtil;
import com.haoxue.haotianqi.view.SpotsDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * 说明：登录
 * 作者：Luoyangs
 * 时间：2015-9-15
 */
@ContentView(R.layout.act_login_layout)
public class LoginAct extends Activity {

	@ViewInject(R.id.username)
	private EditText etName;
	@ViewInject(R.id.password)
	private EditText etPass;
	private SpotsDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
		dialog = SpotsDialog.createDialog(this);
	}
	
	@OnClick({R.id.btnlogin,R.id.btnregister})
	public void onLogin(View view){
		if (view.getId() == R.id.btnlogin) {
			ToastUtil.showShort(this, "登录");
			dialog.show();
		}else if(view.getId() == R.id.btnregister){
			ToastUtil.showShort(this, "注册");
		}
	}
	
	@OnClick({R.id.login_qq,R.id.login_weixin,R.id.login_weibo})
	public void onOtherLogin(View view){
		switch (view.getId()) {
		case R.id.login_qq:

			break;
		case R.id.login_weixin:

			break;
		case R.id.login_weibo:

			break;
		}
	}
	
}
