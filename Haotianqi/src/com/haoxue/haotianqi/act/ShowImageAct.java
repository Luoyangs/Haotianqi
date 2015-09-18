package com.haoxue.haotianqi.act;

import com.haoxue.haotianqi.view.SmoothImageView;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

/**
 * 说明:显示大图
 * 作者:Luoyangs
 * 时间:2015-9-18
 */
public class ShowImageAct extends Activity {
	
	private SmoothImageView imageView;
	private BitmapUtils bitmapUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String imageUrl = getIntent().getStringExtra("image");
		imageView = new SmoothImageView(this);
		bitmapUtils = new BitmapUtils(this);
		
		imageView.transformIn();
		imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
		imageView.setScaleType(ScaleType.FIT_CENTER);
		setContentView(imageView);
		bitmapUtils.display(imageView,imageUrl);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
				imageView.transformOut();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
			@Override
			public void onTransformComplete(int mode) {
				if (mode == 2) {
					finish();
				}
			}
		});
		imageView.transformOut();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(0, 0);
		}
	}

}
