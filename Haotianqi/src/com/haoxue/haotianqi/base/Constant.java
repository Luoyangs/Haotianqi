package com.haoxue.haotianqi.base;

import android.annotation.SuppressLint;

/** 
 *	说明：程序常量
 *	作者： Luoyangs
 *	时间： 2015年8月17日
 */
public class Constant {

	/**百度天气API*/
	public static final String WETHER_URL = "http://api.map.baidu.com/telematics/v3/weather";
	/**默认的数据格式*/
	public static final String DATA_TYPE = "json";
	/**百度天气API的ak*/
	public static final String BAIDU_AK = "zwioFusFO9VIxEY9MhE5o9XX";
	/**百度天气API的mcode*/
	public static final String BAIDU_MCODE = "D2:F7:D4:59:C2:1D:64:F1:78:24:BF:48:6E:15:97:F0:EC:79:2C:13;com.haoxue.haotianqi";
	/**城市天气管理数据库名字*/
	public static final String CITY_WEATHER_MANAGER_DB_NAME = "citymanager_db";
	/**城市天气管理表的名字*/
	public static final String CITY_WEATHER_MANAGER_TABLE_NAME = "tab_weather";
	/**城市列表的名字*/
	public static final String CITY_LIST_DB_NAME = "citie_lists.db";
	/**城市列表文件的名字*/
	public static final String CITY_LIST_FILE_NAME = "citie_lists.db";
	/**城市列表数据库文件目标存放路径*/
	@SuppressLint("SdCardPath")
	public static final String DB_PATH = "/data/data/com.haoxue.haotianqi/databases/";
	
	/**历史今天*/
	public static final String HISTORY_URL = "http://api.46644.com/todayhistory/";
	/**历史今天appkey*/
	public static final String HISTORY_APPKEY = "1307ee261de8bbcf83830de89caae73f";
	
	/**健康资讯微信*/
	public static final String HEALTH_URL = "http://api.huceo.com/health/other/";
	/**健康资讯微信appkey*/
	public static final String HEALTH_APPKEY = "bb28b914fc5ba4ba97cd37408c9b75d8";
	
	/**每日一笑(Text)*/
	public static final String HAPPY_TEXT_URL = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text";
	/**每日一笑(Pic)*/
	public static final String HAPPY_PIC_URL = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_pic";
	/**每日一笑appkey*/
	public static final String HAPPY_APPKEY = "86c994266ab96e8b233d3fde78e775d8";
	
	/**热门游记*/
	public static final String WHERE_BOOK_URL = "http://apis.baidu.com/qunartravel/travellist/travellist";
	/**热门游记appkey*/
	public static final String WHERE_BOOK_APPKEY = "86c994266ab96e8b233d3fde78e775d8";
	
	
	/**保存图片文件路径*/
	@SuppressLint("SdCardPath")
	public static final String IMAGE_PATH="/mnt/sdcard/downimg";
    
	/**访问数据成功的返回码*/
	public static final int LOAD_OK = 0x01;
	/**访问数据失败的返回码*/
	public static final int LOAD_FAIL = 0x02;
	/**访问数据没有网络的返回码*/
	public static final int LOAD_NO_NET = 0x03;
	
}
