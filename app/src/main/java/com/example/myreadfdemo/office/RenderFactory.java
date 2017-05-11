package com.example.myreadfdemo.office;

/**
 * Created by lixinyu on 2017/5/11.
 */

public class RenderFactory {
	public static IBaseOfficeRender getRender(String filePath) {
		
		if (filePath.endsWith(".doc")) {
			return new DocOfficeRender(filePath);
		} else if (filePath.endsWith(".docx")) {
			return new DocxOfficeRender(filePath);
		} else if (filePath.endsWith(".xls")) {

		} else if (filePath.endsWith(".xlsx")) {

		}
		
		return null;
	}
	
}
