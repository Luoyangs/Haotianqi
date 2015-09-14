package com.haoxue.haotianqi.view;

import android.util.AttributeSet;
import android.widget.GridView;

/** 
 *	说明：设置不滚动GridView
 *	作者： Luoyangs
 *	时间： 2015年8月26日
 */
public class CustomGridView extends GridView {
	
	public CustomGridView(android.content.Context context,AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
