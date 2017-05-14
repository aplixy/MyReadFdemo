package com.xinyu.xylibrary.utils;

import android.content.Context;
import android.widget.Toast;

/***
 *  统一调用，方便修改样式
 * @author fuxinrong
 *
 */
public class ToastUtils {

	public static void showShortToast(Context context, int resId) {
		if(context == null){
			return ;
		}
		Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT)
				.show();
	}

	public static void showShortToast(Context context, String text) {
		if(context == null){
			return ;
		}
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, int resId, int duration) {
		if(context == null){
			return ;
		}
		Toast.makeText(context, context.getString(resId), duration).show();
	}

	public static void showToast(Context context, String text, int duration) {
		if(context == null){
			return ;
		}
		Toast.makeText(context, text, duration).show();
	}
	
	
	
}
