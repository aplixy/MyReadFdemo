package com.example.myreadfdemo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.aspsine.fragmentnavigator.widget.BottomNavigatorView;
import com.example.myreadfdemo.R;

/**
 * Created by lixinyu on 2017/5/12.
 */

public class MidBtnBottomNaviView extends BottomNavigatorView {
	private int mMiddleIndex = 2;
	private int mInvalidPosition = -1;
	private int mChildCount;
	
	public MidBtnBottomNaviView(Context context) {
		super(context);
		init(context);
	}

	public MidBtnBottomNaviView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MidBtnBottomNaviView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	private void init(Context context) {
		removeAllViews();
		inflate(context, R.layout.layout_bottom_navigator, this);

		mChildCount = getChildCount();
		for (int i = 0; i < mChildCount; i++) {
			View view = getChildAt(i);
			final int finalI;
			if (i == mMiddleIndex) finalI = mInvalidPosition;
			else if (i > mMiddleIndex) finalI = i - 1;
			else finalI = i;
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnBottomNavigatorViewItemClickListener.onBottomNavigatorViewItemClick(finalI, v);
				}
			});
		}
	}

	@Override
	public void select(int position) {
		if (position == mInvalidPosition) return;
		else if (position >= mMiddleIndex) position++;

		for (int i = 0; i < mChildCount; i++) {
			if (i == mMiddleIndex) i++;
			
			View child = getChildAt(i);
			if (i == position) {
				selectChild(child, true);
			} else {
				selectChild(child, false);
			}
		}
	}
}
