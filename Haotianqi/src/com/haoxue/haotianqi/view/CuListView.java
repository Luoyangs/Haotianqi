package com.haoxue.haotianqi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * 自定义列表，解决一些适配问题
 * 说明:
 * 作者:Luoyangs
 * 时间:2015-7-26
 */
public class CuListView extends ListView{
	
	public CuListView(Context context) {
		super(context);
	}
	
	public CuListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CuListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override  
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);  
		super.onMeasure(widthMeasureSpec, expandSpec);  
	} 
}

