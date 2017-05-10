package com.example.myreadfdemo.utils;

import com.example.myreadfdemo.text_type.DocType;
import com.example.myreadfdemo.text_type.DocxType;
import com.example.myreadfdemo.text_type.IBaseTextType;

/**
 * Created by lixinyu on 2017/5/8.
 */

public class MyFileReader {
	
	private static MyFileReader mInstance;

	private IBaseTextType mTextType;

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
			mTextType = new DocType(filePath);
		} else if (filePath.endsWith(".docx")) {
			mTextType = new DocxType(filePath);
		} else if (filePath.endsWith(".xls")) {

		} else if (filePath.endsWith(".xlsx")) {

		}
		
		CharSequence sequence = mTextType.getContent();

		if (null != sequence) content = sequence.toString();

		return content;
	}
}
