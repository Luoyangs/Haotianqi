package com.haoxue.haotianqi.act.frg;

import com.haoxue.haotianqi.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/** 
 *	说明：欢笑页面
 *	作者： Luoyangs
 *	时间： 2015年8月24日
 */
@SuppressLint("HandlerLeak")
public class HappyFrag extends Fragment implements OnClickListener{

	private TextView txtText;//笑话
	private TextView txtPic;//图片
	private int selectItem = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_happy_layout, null,false);
		txtText = (TextView) view.findViewById(R.id.txtText);
		txtPic = (TextView) view.findViewById(R.id.txtPic);
		txtText.setOnClickListener(this);
		txtPic.setOnClickListener(this);
		selectItem = 1;
		
		appendFragment(new HappyTextFrag());
		return view;
	}
	
	// 加载页面的公共方法
	private void appendFragment(Fragment target) {
		getChildFragmentManager().beginTransaction().replace(R.id.mainContainer, target).commitAllowingStateLoss();
	}

	// 重置导航栏样式
	private void resetNavigation() {
		txtText.setBackgroundResource(R.drawable.left_icon);	
		txtText.setTextColor(getResources().getColor(R.color.title_blue));
		txtPic.setBackgroundResource(R.drawable.right_icon);	
		txtPic.setTextColor(getResources().getColor(R.color.title_blue));
	}
	
	//设置导航栏样式
	private void setNavigation(int pos) {
		// 重置导航栏样式
		resetNavigation();
		switch (pos) {
		case 0:
			txtText.setBackgroundResource(R.drawable.left_icon_press);	
			txtText.setTextColor(getResources().getColor(R.color.bg_white));
			break;

		case 1:
			txtPic.setBackgroundResource(R.drawable.right_icon_press);	
			txtPic.setTextColor(getResources().getColor(R.color.bg_white));
			break;
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.txtText:
			setNavigation(0);
			if (selectItem == 1) {
				return;//避免重复加载
			}
			appendFragment(new HappyTextFrag());
			selectItem = 1;
			break;

		case R.id.txtPic:
			setNavigation(1);
			if (selectItem == 2) {
				return;//避免重复加载
			}
			appendFragment(new HappyPicFrag());
			selectItem = 2;
			break;
		}
	}
}
