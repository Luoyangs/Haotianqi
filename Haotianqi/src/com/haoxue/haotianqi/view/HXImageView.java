package com.haoxue.haotianqi.view;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.graphics.Bitmap;

/** 
 *	说明：继承自ImageView，用于异步加载图片，在下载图片时使用设置的loading图片占位，图片下载好后刷新View
 *	作者： Luoyangs
 *	时间： 2015年8月17日
 */
public class HXImageView extends ImageView {

	/**
	 * 用于记录默认下载中状态的图片
	 */
	private int downLoadingImageId = 0;
	private int downLoadingImagefailureId = 0;
	// 图片是否加载成功
	private boolean loadSuccess = false;
	
	public HXImageView(Context context) {
		super(context);
	}

	public HXImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HXImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**不设置将使用默认图片 设置下载中，与加载失败的图片,*/
	public void setDefultDownLoadAndFailureImage(int downlding, int failureId) {
		downLoadingImageId = downlding;
		downLoadingImagefailureId = failureId;
	}
	
	/**加载图片*/
	public void loadImage(String url) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(downLoadingImageId)
				.showImageForEmptyUri(downLoadingImagefailureId)
				.cacheInMemory().cacheOnDisc()
				.showImageOnFail(downLoadingImagefailureId)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		ImageLoader.getInstance().displayImage(url, this,options);
	}
	
	/**图片加载成功*/
	public boolean isLoadSuccess() {
		return loadSuccess;
	}

}
