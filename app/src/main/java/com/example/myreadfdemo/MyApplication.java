package com.example.myreadfdemo;

import android.support.multidex.MultiDexApplication;

/**
 * Created by lixinyu on 2017/5/10.
 */

public class MyApplication extends MultiDexApplication {
	
	public static MyApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static MyApplication getApplication() {
		return mInstance;
	}
}
