package com.haoxue.haotianqi.view;

import com.haoxue.haotianqi.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

/** 
 *	说明：加载的弹出框
 *	作者： Luoyangs
 *	时间： 2015年8月20日
 */
public class LoadingDialog  extends Dialog {
	
	/**创建一个弹出框*/
	public static LoadingDialog getInstance(Context context,String strMessage){
		LoadingDialog instance = new LoadingDialog(context,strMessage);
		instance.setCanceledOnTouchOutside(false);
		return instance;
	}

	private LoadingDialog(Context context,String strMessage) {
		super(context, R.style.CustomProgressDialog);
		this.setContentView(R.layout.custom_loading_rotate);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		TextView tvMsg = (TextView) this.findViewById(R.id.text);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		if (!hasFocus) {
			dismiss();
		}
	}
}
