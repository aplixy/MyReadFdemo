package com.example.myreadfdemo.office.render;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.example.myreadfdemo.utils.Logger;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by lixinyu on 2017/5/8.
 */

public abstract class AbsOfficeRender implements IBaseOfficeRender {

	protected void realRender(StringBuilder stringBuilder, TextView textView, final WeakReference<Map<String, Drawable>> weakRefDrawable) {
		Logger.w("render", "stringBuilder--->" + stringBuilder);

		if (null == stringBuilder) return;

		Spanned spanned = Html.fromHtml(stringBuilder.toString(), new Html.ImageGetter() {
			@Override
			public Drawable getDrawable(String source) {
				Logger.i("source--->" + source);
				if (null != weakRefDrawable) {
					Map<String, Drawable> drawables = weakRefDrawable.get();
					if (null != drawables) {
						Drawable drawable = drawables.get(source);

						return drawable;
					}
				}
				return null;
			}
		}, null);

		textView.setText(spanned);
	}

	protected int decideSize(int size) {

		if (size >= 1 && size <= 8) {
			return 1;
		}
		if (size >= 9 && size <= 11) {
			return 2;
		}
		if (size >= 12 && size <= 14) {
			return 3;
		}
		if (size >= 15 && size <= 19) {
			return 4;
		}
		if (size >= 20 && size <= 29) {
			return 5;
		}
		if (size >= 30 && size <= 39) {
			return 6;
		}
		if (size >= 40) {
			return 7;
		}
		return 3;
	}
}
