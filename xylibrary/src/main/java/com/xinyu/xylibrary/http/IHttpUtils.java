package com.xinyu.xylibrary.http;

/**
 * Created by lixinyu on 2017/5/14.
 */

public interface IHttpUtils {
	
	void sendHttpRequest(String url, HttpRequestCallback callback);
}
