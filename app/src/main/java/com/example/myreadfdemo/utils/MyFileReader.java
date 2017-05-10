package com.example.myreadfdemo.utils;

import android.widget.TextView;

import com.example.myreadfdemo.text_type.DocOfficeItem;
import com.example.myreadfdemo.text_type.DocxOfficeItem;
import com.example.myreadfdemo.text_type.DocxOfficeItem2;
import com.example.myreadfdemo.text_type.IBaseOfficeItem;
import com.example.myreadfdemo.text_type.IBaseOfficeItemRender;

/**
 * Created by lixinyu on 2017/5/8.
 */

public class MyFileReader {
	
	private static MyFileReader mInstance;

	private IBaseOfficeItem mTextType;

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

	public String read(String filePath) {

		String content = null;

		if (filePath.endsWith(".doc")) {
			mTextType = new DocOfficeItem(filePath);
		} else if (filePath.endsWith(".docx")) {
			mTextType = new DocxOfficeItem(filePath);
		} else if (filePath.endsWith(".xls")) {

		} else if (filePath.endsWith(".xlsx")) {

		}
		
		CharSequence sequence = mTextType.getContent();

		if (null != sequence) content = sequence.toString();

		return content;
	}
	
	public void render(String filePath, TextView textView) {


		if (filePath.endsWith(".doc")) {
			
		} else if (filePath.endsWith(".docx")) {
			IBaseOfficeItemRender itemRender = new DocxOfficeItem2(filePath, textView);
			itemRender.render();
		} else if (filePath.endsWith(".xls")) {

		} else if (filePath.endsWith(".xlsx")) {

		}
		


	}
}
