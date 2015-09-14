package com.haoxue.haotianqi.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 说明:吐司提示 
 * 作者:Luoyangs 
 * 时间:2015-7-26
 */
public class ToastUtil {

	public static void showShort(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showShort(Context context, int text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showLong(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static void showLong(Context context, int textRes) {
		Toast.makeText(context, textRes, Toast.LENGTH_LONG).show();
	}

	public static void showLong(Context context, String text, int gravity) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}

	public static void showLong(Context context, int text, int gravity) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}

	public static void showShort(Context context, String text, int gravity) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}

	public static void showShort(Context context, int text, int gravity) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}
}
