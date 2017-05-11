package com.example.myreadfdemo.ui.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myreadfdemo.R;


/**
 * Created by aspsine on 16/3/31.
 */
public class BottomNavigatorView extends LinearLayoutCompat {

    OnBottomNavigatorViewItemClickListener mOnBottomNavigatorViewItemClickListener;
	
	private int mMiddleIndex = -1;

    public interface OnBottomNavigatorViewItemClickListener {
        void onBottomNavigatorViewItemClick(int position, View view);
    }

    public BottomNavigatorView(Context context) {
        this(context, null);
    }

    public BottomNavigatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);
        inflate(context, R.layout.layout_bottom_navigator, this);

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnBottomNavigatorViewItemClickListener.onBottomNavigatorViewItemClick(finalI, v);
                }
            });
        }
    }
    
    public void setMiddleIndex(int middleIndex) {
		this.mMiddleIndex = middleIndex;

		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			final int finalI;
			if (i == middleIndex) finalI = -1;
			else if (i > middleIndex) finalI = i - 1;
			else finalI = i;
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnBottomNavigatorViewItemClickListener.onBottomNavigatorViewItemClick(finalI, v);
				}
			});
		}
	}

    public void select(int position) {
		if (position == -1) return;
		
        for (int i = 0; i < getChildCount(); i++) {
			int index = i;
			if (i == mMiddleIndex) continue;
			else if (i > mMiddleIndex) index--;
            View child = getChildAt(i);
            if (index == position) {
                selectChild(child, true);
            } else {
                selectChild(child, false);
            }
        }
    }

    private void selectChild(View child, boolean select) {
        if (child instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) child;
            group.setSelected(select);
            for (int i = 0; i < group.getChildCount(); i++) {
                selectChild(group.getChildAt(i), select);
            }
        } else {
            child.setSelected(select);
            if (child instanceof ImageView) {
                ImageView iv = (ImageView) child;
                Drawable drawable = iv.getDrawable().mutate();
                if (select) {
                    drawable.setColorFilter(getResources().getColor(R.color.colorTabSelected), PorterDuff.Mode.SRC_ATOP);
                } else {
                    drawable.setColorFilter(getResources().getColor(R.color.colorTabNormal), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }

    public void setOnBottomNavigatorViewItemClickListener(OnBottomNavigatorViewItemClickListener listener) {
        this.mOnBottomNavigatorViewItemClickListener = listener;
    }
}
