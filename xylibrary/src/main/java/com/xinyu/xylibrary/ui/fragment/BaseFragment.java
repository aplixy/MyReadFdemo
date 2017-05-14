package com.xinyu.xylibrary.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinyu.xylibrary.http.HttpRequestCallback;
import com.xinyu.xylibrary.http.IHttpUtils;
import com.xinyu.xylibrary.http.impl.HttpUtilsImpl;

import java.lang.ref.WeakReference;

/**
 * Created by lixinyu on 2017/5/14.
 */

public abstract class BaseFragment extends Fragment {
	
	private static final int MSG_CODE_UI = -1000;
	
	private IHttpUtils mHttpUitls;

	private HandlerThread mHandlerThread;
	protected BackgroundHandler mBackgroundHandler;
	
	protected UiHandler mUiHandler;
	
	private WeakRunnable mRunnable;
	
	protected View mView;
	protected FragmentActivity mActivity;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getArgs();

		mHttpUitls = new HttpUtilsImpl();
		
		mHandlerThread = new HandlerThread("activity worker:" + getClass().getSimpleName());
		mHandlerThread.start();
		mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());

		mUiHandler = new UiHandler();
		
		mRunnable = new WeakRunnable(this, mUiHandler);
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
		if (mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
			mBackgroundHandler.getLooper().quit();
		}
	}

	/**
	 * 处理后台操作
	 */
	protected void handleBackgroundMessage(Message msg) {
		
	}
	
	protected void handleUiMessage(Message msg) {
		
	}

	/**
	 * 发送后台操作
	 *
	 * @param msg
	 */
	protected void sendBackgroundMessage(Message msg) {
		mBackgroundHandler.sendMessage(msg);
	}

	

	/**
	 * mock load data
	 */
	private void loadData() {
		showProgressBar(true);
		mBackgroundHandler.post(mRunnable);
	}

	private static class WeakRunnable implements Runnable {

		WeakReference<BaseFragment> mMainFragmentReference;
		WeakReference<UiHandler> mUiHandlerRefrence;

		public WeakRunnable(BaseFragment fragment, UiHandler uiHandler) {
			this.mMainFragmentReference = new WeakReference<BaseFragment>(fragment);
			this.mUiHandlerRefrence = new WeakReference<UiHandler>(uiHandler);
		}

		@Override
		public void run() {
			BaseFragment fragment = mMainFragmentReference.get();
			UiHandler uiHandler = mUiHandlerRefrence.get();
			if (fragment != null && !fragment.isDetached() && uiHandler != null) {
				fragment.firstLoadData();

				uiHandler.sendEmptyMessage(MSG_CODE_UI);
				
				
			}
		}
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
	protected abstract void firstLoadData();

	// 后台Handler
	public class BackgroundHandler extends Handler {

		BackgroundHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handleBackgroundMessage(msg);
		}
	}
	
	public class UiHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			BaseFragment.this.showProgressBar(false);
			
			if (MSG_CODE_UI == msg.what) {
				BaseFragment.this.bindData();
			}
			
			handleUiMessage(msg);
		}
	}
}
