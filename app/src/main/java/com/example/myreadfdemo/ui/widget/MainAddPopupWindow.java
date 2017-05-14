package com.example.myreadfdemo.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.myreadfdemo.R;
import com.example.myreadfdemo.ui.activity.EditNoteActivity;

/**
 * Created by lixinyu on 2017/5/12.
 */

public class MainAddPopupWindow extends PopupWindow implements Animation.AnimationListener, View.OnClickListener, PopupWindow.OnDismissListener {

	protected Context mContext;

	protected ViewGroup mRootView;
	protected ViewGroup mBottomLayout;

	private Animation mInAnim;
	private Animation mOutAnim;

	private OnDismissListener mOnDismissListener;

	/** 点击背景是否关闭 */
	private boolean mIsClickBgDismiss = true;
	
	private int popupWidth;
	private int popupHeight;

	private Button mBtnNewNote;
	private Button mBtnLeadInto;

	public MainAddPopupWindow(Context context) {
		initView(context);
	}
	

	protected void initView(Context context) {
		mContext = context;
		
		findViews();
		init();
		setListener();
	}

	private void findViews() {
		mRootView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.popup_add, null);
		mBtnNewNote = findViewById(R.id.main_popup_add_btn_new);
		mBtnLeadInto = findViewById(R.id.main_popup_add_btn_lead);
		
	}

	private void init() {
		super.setContentView(mRootView);
		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);

		mInAnim = AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom);
		mOutAnim = AnimationUtils.loadAnimation(mContext, R.anim.out_to_bottom);

		mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		popupHeight = mRootView.getMeasuredHeight();
		popupWidth = mRootView.getMeasuredWidth();
	}

	private void setListener() {
		mOutAnim.setAnimationListener(this);
		mRootView.setOnClickListener(this);
		mBtnNewNote.setOnClickListener(this);
		mBtnLeadInto.setOnClickListener(this);
		super.setOnDismissListener(this);
	}


	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		if (mContext instanceof Activity && !((Activity)mContext).isFinishing()) {
			super.showAtLocation(parent, gravity, x, y);

			mRootView.startAnimation(mInAnim);
			if (mBottomLayout != null && mBottomLayout.getVisibility() == View.VISIBLE) mBottomLayout.startAnimation(mInAnim);
		}
	}

	@Override
	public void dismiss() {
//		if (mBottomLayout != null && mBottomLayout.getVisibility() == View.VISIBLE) 
//			mBottomLayout.startAnimation(mOutAnim);
//		else super.dismiss();

		mRootView.startAnimation(mOutAnim);
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		switch (v.getId()) {
			case R.id.common_popup_layout_root: {
				if (mIsClickBgDismiss) dismiss();
				break;
			}
			case R.id.main_popup_add_btn_new: {
				EditNoteActivity.start(mContext, null, 0);
				dismiss();
				break;
			}
			case R.id.main_popup_add_btn_lead: {
				
				break;
			}
			default: {
				break;
			}
		}
		
	}

	// 面板动画收回动画
	@Override
	public void onAnimationStart(Animation animation) {}
	@Override
	public void onAnimationRepeat(Animation animation) {}
	@Override
	public void onAnimationEnd(Animation animation) {
		super.dismiss();
	}

	public <E extends View> E findViewById(int resId) {
		return (E) mRootView.findViewById(resId);
	}

	public void setContentView(int resId) {
		setContentView(LayoutInflater.from(mContext).inflate(resId, null));
	}

	@Override
	public void onDismiss() {
		if (mOnDismissListener != null) mOnDismissListener.onDismiss();
	}

	@Override
	public void setOnDismissListener(OnDismissListener onDismissListener) {
		super.setOnDismissListener(onDismissListener);
		this.mOnDismissListener = onDismissListener;
	}
	
	public void show(View v) {
		//获取需要在其上方显示的控件的位置信息
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		//在控件上方显示
		showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
	}
}
