package com.haoxue.haotianqi.act;

import com.haoxue.haotianqi.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 说明：登录
 * 作者：Luoyangs
 * 时间：2015-9-15
 */
@ContentView(R.layout.act_login_layout)
public class LoginAct extends Activity {

	@ViewInject(R.id.root)
	private View root; // 最外层布局
	@ViewInject(R.id.logo)
	private View logo; // Logo图标
	@ViewInject(R.id.label)
	private View label; // Logo附近的文字
	@ViewInject(R.id.homep_title)
	private View homep_title; 
	@ViewInject(R.id.titilbar_right)
	private Button titilbar_right;
	private int rootBottom = Integer.MIN_VALUE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
		titilbar_right.setVisibility(View.GONE);
		root.getViewTreeObserver().addOnGlobalLayoutListener(new myGlobalLayoutListener());
	}
	
	@OnClick(R.id.titilbar_left)
	public void onBack(View view){
		this.finish();
		overridePendingTransition(R.anim.transit, R.anim.push_top_out);
	}
	
	private class myGlobalLayoutListener implements OnGlobalLayoutListener{

		@Override
		public void onGlobalLayout() {
			Rect r = new Rect();
			root.getGlobalVisibleRect(r);
			// 进入Activity时会布局，第一次调用onGlobalLayout，先记录开始软键盘没有弹出时底部的位置
			if (rootBottom == Integer.MIN_VALUE) {
				rootBottom = r.bottom;
				return;
			}
			// adjustResize，软键盘弹出后高度会变小
			if (r.bottom < rootBottom) {
				RelativeLayout.LayoutParams lp = (LayoutParams) logo.getLayoutParams();
				// 如果Logo不是水平居中，说明是因为接下来的改变Logo大小位置导致的再次布局，忽略掉，否则无限循环
				if (lp.getRules()[RelativeLayout.CENTER_HORIZONTAL] != 0) {
					// Logo显示到左上角
					lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0); // 取消水平居中
					lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT); // 左对齐

					// 缩小Logo为1/2
					int height = logo.getHeight(); // getMeasuredHeight()
					int width = logo.getWidth();
					lp.width = width / 2;
					lp.height = height / 2;
					logo.setLayoutParams(lp);

					// Logo下的文字
					RelativeLayout.LayoutParams labelParams = (LayoutParams) label.getLayoutParams();
					labelParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0); // 取消水平居中
					labelParams.addRule(RelativeLayout.BELOW, 0); // 取消显示到logo的下方
					labelParams.addRule(RelativeLayout.RIGHT_OF,R.id.logo); // 显示到Logo的右方
					labelParams.addRule(RelativeLayout.CENTER_VERTICAL); // 垂直居中
					label.setLayoutParams(labelParams);
				}
			} else { // 软键盘收起或初始化时
				RelativeLayout.LayoutParams lp = (LayoutParams) logo
						.getLayoutParams();
				// 如果没有水平居中，说明是软键盘收起，否则是开始时的初始化或者因为此处if条件里的语句修改控件导致的再次布局，忽略掉，否则无限循环
				if (lp.getRules()[RelativeLayout.CENTER_HORIZONTAL] == 0) {
					// 居中Logo
					lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);

					// 还原Logo为原来大小
					int height = logo.getHeight();
					int width = logo.getWidth();
					lp.width = width * 2;
					lp.height = height * 2;
					logo.setLayoutParams(lp);

					// Logo下的文字
					RelativeLayout.LayoutParams labelParams = (LayoutParams) label.getLayoutParams();
					labelParams.addRule(RelativeLayout.CENTER_HORIZONTAL); // 设置水平居中
					labelParams.addRule(RelativeLayout.BELOW,R.id.logo); // 设置显示到Logo下面
					labelParams.addRule(RelativeLayout.RIGHT_OF, 0); // 取消显示到Logo右面
					labelParams.addRule(RelativeLayout.CENTER_VERTICAL, 0); // 取消垂直居中
					label.setLayoutParams(labelParams);
				}
			}
		}
	}
}
