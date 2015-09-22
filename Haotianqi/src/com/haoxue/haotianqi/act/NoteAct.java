package com.haoxue.haotianqi.act;

import com.haoxue.haotianqi.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * 说明:通知页面
 * 作者:Luoyangs
 * 时间:2015-9-22
 */
@ContentView(R.layout.act_note_layout)
public class NoteAct extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.titilbar_left)
	public void onClick(View view){
		this.finish();
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}
}
