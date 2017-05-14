package com.xinyu.xylibrary.http;

/**
 * Created by lixinyu on 2017/5/14.
 */

public interface HttpRequestCallback<T> {
	void onSuccess(int code, T t);
	void onFail(int code, T t, String message);
}
