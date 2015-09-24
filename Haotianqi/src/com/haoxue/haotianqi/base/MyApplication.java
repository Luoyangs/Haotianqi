package com.haoxue.haotianqi.base;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

/** 
 *	说明：
 *	作者： Luoyangs
 *	时间： 2015年8月17日
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		//加载百度地图
		SDKInitializer.initialize(getApplicationContext());
		//初始化图片加载缓存
		this.init(this);
	}

	/**
	 * 初始化图片加载缓存
	 */
	private void init(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.enableLogging().build();
		ImageLoader.getInstance().init(config);
	}
}
