package com.example.myreadfdemo.utils;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by lixinyu on 2017/5/10.
 */

public class TextStyle {

	//private boolean isTable = false; //是表格  用来统计 列 行 数
	private boolean isSize = false;  //大小状态
	private boolean isColor = false;  //颜色状态
	private boolean isCenter = false; //居中状态
	private boolean isRight = false; //居右状态
	private boolean isItalic = false; //是斜体
	private boolean isUnderline = false;  //是下划线
	private boolean isBold = false;  //加粗
	private boolean isR = false;    //在那个r中
	
	public String format(String enter, XmlPullParser xmlParser) {
		String color = null;
		int size = -1;
		
		String tag = xmlParser.getName();
		Logger.d("tag : " + tag);

		if (tag.equalsIgnoreCase("r")) {
			Logger.d("isR : " + isR);
			isR = true;
		}
		
		if (tag.equalsIgnoreCase("u")) {  //判断下划线
			Logger.d("下划线");
			isUnderline = true;
		}
		
		if (tag.equalsIgnoreCase("jc")) {  //判断对齐方式
			String align = xmlParser.getAttributeValue(0);
			Logger.d("检测到对齐方式");
			Logger.d("aligh : " + align);
			if (align.equals("center")) {
				isCenter = true;
			}
			if (align.equals("right")) {
				isRight = true;
			}
		}
		
		if (tag.equalsIgnoreCase("color")) {   //判断颜色
			color = xmlParser.getAttributeValue(0);
			Logger.d("检测到颜色");
			Logger.d("color : " + color);
			isColor = true;
		}
		
		if (tag.equalsIgnoreCase("sz")) {   //判断大小
			if (isR) {
				size = decideSize(Integer.valueOf(xmlParser.getAttributeValue(0)));
				Logger.d("size : " + size);
				isSize = true;
			}
		}
		
		
		
		if (tag.equalsIgnoreCase("b")) {  //检测到加粗标签
			Logger.d("检测到b标签");
			isBold = true;
		}
		
		
		
		if (tag.equalsIgnoreCase("i")) {   //斜体
			isItalic = true;
		}
		
		//检测到值 标签(真正的内容从这里开始)
		if (tag.equalsIgnoreCase("t")) {
			if (isBold) {   //加粗
				Logger.d("加粗开始");
			}
			if (isUnderline) {    //检测到下划线标签,输入<u>
				Logger.d("下划线开始");
			}
			if (isItalic) {       //检测到斜体标签,输入<i>
				Logger.d("斜体开始");
			}


			
		}
		
		
		return null;
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

	//下面是表格处理
	//if (tag.equalsIgnoreCase("tbl")) {  //检测到tbl  表格开始  
	//	Logger.d("table begin");
	//	isTable = true;
	//}
	//
	//if (tag.equalsIgnoreCase("tr")) {    //行
	//	Logger.d("row begin");
	//}
	//
	//if (tag.equalsIgnoreCase("tc")) {   //列
	//	Logger.d("col begin");
	//}
	//if (tag.equalsIgnoreCase("p")) {//检测到 p 标签
	//	if (isTable!) {     // 如果在表格中 就无视
	//		Logger.d("检测到p标签");
	//	}
	//}
	
}
