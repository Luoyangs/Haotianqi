package com.haoxue.haotianqi.act;

import com.haoxue.haotianqi.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 说明:显示大图
 * 作者:Luoyangs
 * 时间:2015-9-18
 */
@ContentView(R.layout.act_showimage_layout)
public class ShowImageAct extends Activity {
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	@ViewInject(R.id.image)
	private ImageView imageView;
	@ViewInject(R.id.loading)
	private ProgressBar spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		String imageUrl = getIntent().getStringExtra("image");
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.pictures_no)
		.showImageOnFail(R.drawable.loaderr).resetViewBeforeLoading()
		.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
		imageLoader.displayImage(imageUrl, imageView, options, new mySimpleImageLoadingListener());
		
		//添加监听
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
	
	private class mySimpleImageLoadingListener extends SimpleImageLoadingListener{
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			spinner.setVisibility(View.VISIBLE);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			String message = null;
			switch (failReason.getType()) { // 获取图片失败类型
			case IO_ERROR: // 文件I/O错误
				message = "Input/Output error";
				break;
			case DECODING_ERROR: // 解码错误
				message = "Image can't be decoded";
				break;
			case NETWORK_DENIED: // 网络延迟
				message = "Downloads are denied";
				break;
			case OUT_OF_MEMORY: // 内存不足
				message = "Out Of Memory error";
				break;
			case UNKNOWN: // 原因不明
				message = "Unknown error";
				break;
			}
			Toast.makeText(ShowImageAct.this, message, Toast.LENGTH_SHORT).show();
			spinner.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			spinner.setVisibility(View.GONE); // 不显示圆形进度条
		}
	}

}
