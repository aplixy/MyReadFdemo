package com.example.myreadfdemo.utils;


import android.text.TextUtils;
import android.util.Log;

/**
 * Created by lixinyu on 2017/5/4.
 */

public class Logger {
	private static final String TAG = "lxy";
	private static final int LEVEL_DEBUG = 100;
	private static final int LEVEL_INFO = 101;
	private static final int LEVEL_VERBOSE = 102;
	private static final int LEVEL_WARNING = 103;
	private static final int LEVEL_ERROR = 104;

	public static int d(Object info) {
		return d(null, info);
	}

	public static int i(Object info) {
		return i(null, info);
	}

	public static int v(Object info) {
		return v(null, info);
	}

	public static int w(Object info) {
		return w(null, info);
	}

	public static int e(Object info) {
		return e(null, info);
	}
	
	public static int d(String tag, Object info) {
		return printLog(LEVEL_DEBUG, tag, info);
	}
	
	public static int i(String tag, Object info) {
		return printLog(LEVEL_INFO, tag, info);
	}
	
	public static int v(String tag, Object info) {
		return printLog(LEVEL_VERBOSE, tag, info);
	}
	
	public static int w(String tag, Object info) {
		return printLog(LEVEL_WARNING, tag, info);
	}
	
	public static int e(String tag, Object info) {
		return printLog(LEVEL_ERROR, tag, info);
	}
	
	private static int printLog(int level, String tag, Object info) {
		String myTag;
		String myInfo;
		
		if (TextUtils.isEmpty(tag)) {
			myTag = TAG;
		} else {
			myTag = tag;
		}
		
		if (null == info) {
			myInfo = "";
		} else if (info instanceof String) {
			myInfo = (String) info;
		} else {
			myInfo = info.toString();
		}
		
		int result;
		
		switch (level) {
			case LEVEL_DEBUG:
				result = Log.d(myTag, myInfo);
				break;
			case LEVEL_INFO:
				result = Log.i(myTag, myInfo);
				break;
			case LEVEL_VERBOSE:
				result = Log.v(myTag, myInfo);
				break;
			case LEVEL_WARNING:
				result = Log.w(myTag, myInfo);
				break;
			case LEVEL_ERROR:
				result = Log.e(myTag, myInfo);
				break;
			default:
				result = Log.d(myTag, myInfo);
				break;
		}
		
		return result;
	}
	
	
}
