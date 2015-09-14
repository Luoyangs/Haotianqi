package com.haoxue.haotianqi.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import android.content.Context;
import android.content.res.AssetManager;

/** 
 *	说明：Office 文件操作
 *	作者： Luoyangs
 *	时间： 2015年8月13日
 */
public class OfficeUtil {

	/**
	 *	说明：读取Asset目录下.xls格式的Excel文件
	 *  @param context 上下文
	 *  @param fileName 文件的名字
	 */
	public static List<Map<String, String>> readExcel(Context context,String fileName){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		AssetManager manager = context.getAssets();
		InputStream inputStream = null;
		try {
			inputStream = manager.open(fileName);
			Workbook workbook = Workbook.getWorkbook(inputStream);
			Sheet sheet = workbook.getSheet(0);
			int row = sheet.getRows();
			int column = sheet.getColumns();
			Map<String, String> map;			
			for (int i = 1; i < row; i++) {
				map = new HashMap<String, String>();
				for (int j = 0; j < column; j++) {
					String key = sheet.getCell(j, 0).getContents();
					String value = sheet.getCell(j, i).getContents();
					map.put(key, value);
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
}
