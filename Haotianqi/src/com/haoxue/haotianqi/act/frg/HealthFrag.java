package com.haoxue.haotianqi.act.frg;

import com.haoxue.haotianqi.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** 
 *	说明：健康页面
 *	作者： Luoyangs
 *	时间： 2015年8月24日
 */
public class HealthFrag extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.from(getActivity()).inflate(R.layout.layout_guide_1, null,false);
	}
}
