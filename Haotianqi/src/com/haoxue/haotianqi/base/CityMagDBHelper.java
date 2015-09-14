package com.haoxue.haotianqi.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/** 
 *	说明：城市天气管理数据库
 *	作者： Luoyangs
 *	时间： 2015年8月17日
 */
public class CityMagDBHelper extends SQLiteOpenHelper {

	public CityMagDBHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table "+Constant.CITY_WEATHER_MANAGER_TABLE_NAME+" (_id integer primary key autoincrement, cityname varchar(20), "
				+ "imageurl varchar(20), weather varchar(20), temp varchar(20));";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("drop table if exists person;");
	}

}
