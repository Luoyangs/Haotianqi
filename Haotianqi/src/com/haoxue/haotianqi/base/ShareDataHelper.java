package com.haoxue.haotianqi.base;

import com.haoxue.haotianqi.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;

/** 
 *	说明：程序运行保存本地的数据
 *	作者： Luoyangs
 *	时间： 2015年8月18日
 */
public class ShareDataHelper {
	
	/**程序数据（第一次进入，上一次的定位）*/
	private SharedPreferences appshare;
	/**缓存当前位置的天气信息*/
	private SharedPreferences wethershare;
	/**缓存城市列表*/
	private SharedPreferences cityshare;
	/**缓存用户信息*/
	private SharedPreferences usershare;
	
	private static ShareDataHelper instance = null;
	
	public static ShareDataHelper getInstance(Context context){
		if (instance == null) {
			instance = new ShareDataHelper(context);
		}
		return instance;
	}
	
	private ShareDataHelper(Context context){
		//初始化操作
		appshare = context.getSharedPreferences("appshare", Context.MODE_PRIVATE);
		wethershare = context.getSharedPreferences("wethershare", Context.MODE_PRIVATE);
		cityshare = context.getSharedPreferences("cityshare", Context.MODE_PRIVATE);
		usershare = context.getSharedPreferences("usershare", Context.MODE_PRIVATE);
	}
	
	/**保存用户登录信息*/
	public void saveLoginAccount(String userName,String password){
		Editor editor = usershare.edit();
    	editor.putString("userName", userName);
    	editor.putString("password", password);
		editor.commit();
	}
	
	/**获取用户登录信息*/
	public String getLoginAccount(){
		return usershare.getString("userName","");
	}
	
	/**保存登录信息（是否第一次初始化程序）*/
	public void saveLoadingInfo(boolean isFirstIn){
		Editor editor = appshare.edit();
    	editor.putBoolean("isFirstIn", isFirstIn);
		editor.commit();
	}
	
	/**保存背景图片*/
	public void saveBgImage(int imgId){
		Editor editor = appshare.edit();
		editor.putInt("imgId", imgId);
		editor.commit();
	}
	
	/**保存登录信息（是否第一次初始化程序）*/
	public int getBgImage(){
		return appshare.getInt("imgId", R.drawable.bg_snow_2);
	}
	
	/**保存上一次定位的城市（是否第一次初始化程序）*/
	public void saveLastCity(String city){
		Editor editor = appshare.edit();
    	editor.putString("cur_city", city);
		editor.commit();
	}
	
	/**获取登录信息（是否第一次初始化程序,默认为true）*/
	public boolean getLoadingInfo(){
		return appshare.getBoolean("isFirstIn", true);
	}
	
	/**获取上一次定位的城市*/
	public String getLastCity(){
		return appshare.getString("cur_city","北京");
	}
	
	/**缓存当前位置的天气信息(保存的为JSON格式)*/
	public void saveWetherInfo(String cityName,String wehter){
		Editor editor = wethershare.edit();
		editor.putString(cityName, wehter);
		editor.commit();
	}
	
	/**获取缓存的天气信息(为JSON格式)*/
	public String getWether(String cityName){
		return wethershare.getString(cityName,"");
	}
	
	/**缓存城市列表信息(保存的为JSON格式)*/
	public void saveCitys(String citys){
		Editor editor = cityshare.edit();
		editor.putString("citylist", citys.toString());
		editor.commit();
	}
	
	/**获取城市列表信息(为JSON格式)*/
	public String getCitys(){
		return cityshare.getString("citylist","");
	}
}
