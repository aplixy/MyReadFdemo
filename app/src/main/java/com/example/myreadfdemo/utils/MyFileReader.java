package com.example.myreadfdemo.utils;

import android.widget.TextView;

import com.example.myreadfdemo.office.render.IBaseOfficeRender;
import com.example.myreadfdemo.office.render.RenderFactory;

/**
 * Created by lixinyu on 2017/5/8.
 */

public class MyFileReader {
	
	private static MyFileReader mInstance;

	private IBaseOfficeRender mOfficeRender;

	private MyFileReader() {

	}

	public static MyFileReader getInstance() {
		synchronized (FileReader.class) {
			if (null == mInstance) {
				synchronized (FileReader.class) {
					if (null == mInstance) {
						mInstance = new MyFileReader();
					}
				}
			}
		}

		return mInstance;
	}

//	public String read(String filePath) {
//
//		String content = null;
//
//		if (filePath.endsWith(".doc")) {
//			mTextType = new DocOfficeRender(filePath);
//		} else if (filePath.endsWith(".docx")) {
//			mTextType = new DocxOfficeRender(filePath);
//		} else if (filePath.endsWith(".xls")) {
//
//		} else if (filePath.endsWith(".xlsx")) {
//
//		}
//		
//		CharSequence sequence = mTextType.getContent();
//
//		if (null != sequence) content = sequence.toString();
//
//		return content;
//	}
	
	public void render(String filePath, TextView textView) {


		mOfficeRender = RenderFactory.getRender(filePath);

		if (null != mOfficeRender) {
			mOfficeRender.render(textView);
		}

	}
}
