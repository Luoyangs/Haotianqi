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
	
	/**
	 * 说明：获取健康资讯URL（每次10条）
	 * @param page 页码
	 * @return
	 */
	public static String getHealthRequestURL(int page){
		return Constant.HEALTH_URL + "?page=" + page + "&num=10" + "&key="
				+ Constant.HEALTH_APPKEY;
	}
	
	/**
	 * 说明：获取笑话（Text）URL（每次10条）
	 * @param page 页码
	 * @return
	 */
	public static String getHappyTextRequestURL(int page){
		return Constant.HAPPY_TEXT_URL + "?page=" + page;
	}
	
	/**
	 * 说明：获取笑话（Pic）URL（每次10条）
	 * @param page 页码
	 * @return
	 */
	public static String getHappyPicRequestURL(int page){
		return Constant.HAPPY_PIC_URL + "?page=" + page;
	}
	
	/**
	 * 说明：获取热门游记URL（每次10条）
	 * @param page 页码
	 * @return
	 */
	public static String getWhereBookRequestURL(int page){
		return Constant.WHERE_BOOK_URL + "?page=" + page;
	}
	
	//http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=iGs8rFvzh1e8c7C9DjXT5toK
	//http://wthrcdn.etouch.cn/WeatherApi?city=成都
}
