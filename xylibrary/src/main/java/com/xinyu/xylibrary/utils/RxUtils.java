package com.xinyu.xylibrary.utils;

import java.io.FileReader;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lixinyu on 2017/5/17.
 */

public class RxUtils {
	public static RxUtils mInstance;

	public static RxUtils getInstance() {
		synchronized (FileReader.class) {
			if (null == mInstance) {
				synchronized (FileReader.class) {
					if (null == mInstance) {
						mInstance = new RxUtils();
					}
				}
			}
		}

		return mInstance;
	}

	public <T> Subscription startWorkThread(final RxThreadInterface<T> rxThreadInterface) {
		//创建一个被观察者(发布者)
		Observable observable = Observable.create(new Observable.OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> subscriber) {
				Logger.d("OnSubscribe");
				
				if (null != rxThreadInterface) {
					subscriber.onNext(rxThreadInterface.workThread());
				}
				
				subscriber.onCompleted();
			}
		});

		//创建一个观察者
		Subscriber<T> subscriber = new Subscriber<T>() {
			@Override
			public void onCompleted() {
				Logger.i("onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Logger.i("onError");
			}

			@Override
			public void onNext(T t) {
				Logger.i("onNext");
				if (null != rxThreadInterface) {
					rxThreadInterface.uiThread(t);
				}
			}
		};

		//注册观察者(这个方法名看起来有点怪，还不如写成regisiterSubscriber(..)或者干脆addSubscriber(..))
		//注册后就会开始调用call()中的观察者执行的方法 onNext() onCompleted()等
		//observable.subscribe(subscriber);

		//设置观察者和发布者代码所要运行的线程后注册观察者
		return observable
				//.subscribeOn(Schedulers.immediate())//在当前线程执行subscribe()方法
				.subscribeOn(Schedulers.io())//在io线程执行subscribe()方法
				.observeOn(AndroidSchedulers.mainThread())//在UI线程执行观察者的方法
				.subscribe(subscriber);
	}
	
	public interface RxThreadInterface<T> {
		T workThread();
		void uiThread(T t);
	}
}
