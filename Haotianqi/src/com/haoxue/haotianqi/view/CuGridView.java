package com.haoxue.haotianqi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/** 
 *	说明：设置不滚动GridView
 *	作者： Luoyangs
 *	时间： 2015年8月26日
 */
public class CuGridView  extends GridView {

	public CuGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CuGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CuGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}