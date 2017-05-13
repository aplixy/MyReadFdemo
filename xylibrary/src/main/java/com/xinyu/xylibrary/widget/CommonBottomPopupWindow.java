package com.xinyu.xylibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.xinyu.xylibrary.R;


/**
 * 带半透明背景的PopupWindow，可设置子View动画
 * Created by lixinyu on 2016/12/6.
 */

public class CommonBottomPopupWindow extends PopupWindow implements Animation.AnimationListener, View.OnClickListener, PopupWindow.OnDismissListener {

	protected Context mContext;

	protected ViewGroup mRootView;
	protected ViewGroup mBottomLayout;

	private Animation mInAnim;
	private Animation mOutAnim;
	
	private OnDismissListener mOnDismissListener;
	
	/** 点击背景是否关闭 */
	private boolean mIsClickBgDismiss = true;


	public CommonBottomPopupWindow(Context context) {
		super(context);
		initView(context);
	}

	protected void initView(Context context) {
		mContext = context;

		findViews();
		init();
		setListener();
	}

	private void findViews() {
		mRootView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.common_popup_layout, null);
	}

	private void init() {
		super.setContentView(mRootView);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);

		mInAnim = AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom);
		mOutAnim = AnimationUtils.loadAnimation(mContext, R.anim.out_to_bottom);
	}

	private void setListener() {
		mOutAnim.setAnimationListener(this);
		mRootView.setOnClickListener(this);
		super.setOnDismissListener(this);
	}


	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		if (mContext instanceof Activity && !((Activity)mContext).isFinishing()) {
			super.showAtLocation(parent, gravity, x, y);
			if (mBottomLayout != null && mBottomLayout.getVisibility() == View.VISIBLE) mBottomLayout.startAnimation(mInAnim);
		}
	}

	@Override
	public void dismiss() {
		if (mBottomLayout != null && mBottomLayout.getVisibility() == View.VISIBLE) mBottomLayout.startAnimation(mOutAnim);
		else super.dismiss();
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.common_popup_layout_root) {
			if (mIsClickBgDismiss) dismiss();
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
	public void setContentView(View view) {
		mRootView.removeAllViews();
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		mRootView.addView(view, lp);
	}

	/**
	 * 指定动画的子View ID
	 * @param resId
	 */
	public void setBottomLayoutId(int resId) {
		mBottomLayout = findViewById(resId);
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
	
	public void setClickBgDismiss(boolean isDismiss) {
		mIsClickBgDismiss = isDismiss;
	}
	
	public void setBackgroundDim(boolean isDimBg) {
		if (isDimBg) {
			mRootView.setBackgroundColor(mContext.getResources().getColor(R.color.black_transparency));
		} else {
			mRootView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
		}
	}
	
	public void showAtBottom() {
		if (mContext != null && mContext instanceof Activity) {
			showAtLocation(((Activity)mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
			
		}
	}
}
