package com.xinyu.xylibrary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinyu.xylibrary.http.HttpRequestCallback;
import com.xinyu.xylibrary.http.IHttpUtils;
import com.xinyu.xylibrary.http.impl.HttpUtilsImpl;
import com.xinyu.xylibrary.utils.RxUtils;

import rx.Subscription;

/**
 * Created by lixinyu on 2017/5/14.
 */

public abstract class BaseRxFragment extends Fragment {
	
	
	private IHttpUtils mHttpUitls;

	
	protected View mView;
	protected FragmentActivity mActivity;
	
	private Subscription mSubscription;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getArgs();

		mHttpUitls = new HttpUtilsImpl();
		
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(getLayoutRes(), container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.mView = view;
		
		findViews(view);
		initWidget();
		setListener();
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.mActivity = getActivity();
		
		if (savedInstanceState == null) {
			loadData();
		} else {
			bindData();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mSubscription) {
			mSubscription.unsubscribe();
		}
	}

	protected  <E extends View> E findViewById(int resId) {
		return (E) mView.findViewById(resId);
	}

	
	
	/**
	 * mock load data
	 */
	protected <T> void loadData() {
		showProgressBar(true);
		
		this.mSubscription = RxUtils.getInstance().<T>startWorkThread(new RxUtils.RxThreadInterface<T>() {
			@Override
			public T workThread() {
				firstLoadDataBackground();
				return null;
			}

			@Override
			public void uiThread(T t) {
				showProgressBar(false);
				bindData();
			}
		});
	}
	
	
	protected <T> void sendDataRequest(String url, HttpRequestCallback<T> callback) {
		mHttpUitls.sendHttpRequest(url, callback);
	}
	

	protected abstract int getLayoutRes();
	protected abstract void findViews(View view);
	protected abstract void getArgs();
	protected abstract void initWidget();
	protected abstract void setListener();

	protected abstract void showProgressBar(boolean show);
	protected abstract void bindData();
	protected abstract void firstLoadDataBackground();
}
