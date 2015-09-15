package com.haoxue.haotianqi.util;

import com.haoxue.haotianqi.base.Constant;

/** 
 *	说明：发送请求的URL
 *	作者： Luoyangs
 *	时间： 2015年8月17日
 */
public class ReqUtil {
	
	/**
	 *	说明：获取请求百度天气API的URL
	 *	@param cityName 城市名字
	 */
	public static String getRequestURL(String cityName){
		return Constant.WETHER_URL+"?location=" +cityName + "&output="+ Constant.DATA_TYPE +"&ak="+ Constant.BAIDU_AK
				+"&mcode="+ Constant.BAIDU_MCODE;
	}
	
	/**
	 * 说明：获取历史今天URL
	 * @param month 月
	 * @param day 天
	 * @return
	 */
	public static String getHistoryRequestURL(String month,String day){
		return Constant.HISTORY_URL + "?month=" + month + "&day=" + day
				+ "&appkey=" + Constant.HISTORY_APPKEY;
	}
	
	//http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=iGs8rFvzh1e8c7C9DjXT5toK
	//http://wthrcdn.etouch.cn/WeatherApi?city=成都
}
