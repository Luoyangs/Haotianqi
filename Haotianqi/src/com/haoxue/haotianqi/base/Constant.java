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
	public static final String BAIDU_MCODE = "4C:10:B0:6C:5B:17:1D:C7:8C:E9:58:F8:1E:4A:63:0B:E1:DA:A2:63;com.haoxue.haotianqi";
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
	
	/**访问数据成功的返回码*/
	public static final int LOAD_OK = 0x01;
	/**访问数据失败的返回码*/
	public static final int LOAD_FAIL = 0x02;
	/**访问数据没有网络的返回码*/
	public static final int LOAD_NO_NET = 0x03;
	
}
