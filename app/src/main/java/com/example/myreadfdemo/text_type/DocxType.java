package com.example.myreadfdemo.text_type;

import android.util.Xml;

import com.example.myreadfdemo.utils.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by lixinyu on 2017/5/8.
 */

public class DocxType extends AbsTextType {
	
	private String mFilePath;
	
	public DocxType(String filePath) {
		this.mFilePath = filePath;
	}
	
	@Override
	public CharSequence getContent() {
		StringBuilder riverBuilder = null;
		try {

			ZipFile xlsxFile = new ZipFile(new File(mFilePath));
			ZipEntry sharedStringXML = xlsxFile.getEntry("word/document.xml");
			InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(inputStream, "utf-8");
			int evtType = xmlParser.getEventType();

			boolean isTable = false; //是表格  用来统计 列 行 数
			boolean isSize = false;  //大小状态
			boolean isColor = false;  //颜色状态
			boolean isCenter = false; //居中状态
			boolean isRight = false; //居右状态
			boolean isItalic = false; //是斜体
			boolean isUnderline = false;  //是下划线
			boolean isBold = false;  //加粗
			boolean isR = false;    //在那个r中

			int pictureIndex = 1;  //docx 压缩包中的图片名  iamge1 开始  所以索引从1开始

			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
					//开始标签
					case XmlPullParser.START_TAG:
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
							String color = xmlParser.getAttributeValue(0);
							Logger.d("检测到颜色");
							Logger.d("color : " + color);
							isColor = true;
						}
						if (tag.equalsIgnoreCase("sz")) {   //判断大小
							if (isR == true) {
								int size = decideSize(Integer.valueOf(xmlParser.getAttributeValue(0)));
								Logger.d("size : " + size);
								isSize = true;
							}
						}
						//下面是表格处理
						if (tag.equalsIgnoreCase("tbl")) {  //检测到tbl  表格开始  
							Logger.d("table begin");
							isTable = true;
						}
						if (tag.equalsIgnoreCase("tr")) {    //行
							Logger.d("row begin");
						}
						if (tag.equalsIgnoreCase("tc")) {   //列
							Logger.d("col begin");
						}

						if (tag.equalsIgnoreCase("pict")) {  //检测到标签  pict  图片
							Logger.d("检测到图片");
							ZipEntry sharePicture = xlsxFile.getEntry("word/media/image" + pictureIndex + ".jpeg");//一下为读取docx的图片  转化为流数组
							InputStream pictIS = xlsxFile.getInputStream(sharePicture);
							ByteArrayOutputStream pOut = new ByteArrayOutputStream();
							byte[] bt = null;
							byte[] b = new byte[1000];
							int len = 0;
							while ((len = pictIS.read(b)) != -1) {
								pOut.write(b, 0, len);
							}
							pictIS.close();
							pOut.close();
							bt = pOut.toByteArray();
							Logger.i("图片byteArray : ", "" + bt);
							if (pictIS != null)
								pictIS.close();
							if (pOut != null)
								pOut.close();
							writeDOCXPicture(bt);

							pictureIndex++;  //转换一张后 索引+1
						}

						if (tag.equalsIgnoreCase("b")) {  //检测到加粗标签
							Logger.d("检测到b标签");
							isBold = true;
						}
						if (tag.equalsIgnoreCase("p")) {//检测到 p 标签
							if (isTable == false) {     // 如果在表格中 就无视
								Logger.d("检测到p标签");
							}
						}
						if (tag.equalsIgnoreCase("i")) {   //斜体
							isItalic = true;
						}
						//检测到值 标签
						if (tag.equalsIgnoreCase("t")) {
							if (isBold == true) {   //加粗
								Logger.d("加粗开始");
							}
							if (isUnderline == true) {    //检测到下划线标签,输入<u>
								Logger.d("下划线开始");
							}
							if (isItalic == true) {       //检测到斜体标签,输入<i>
								Logger.d("斜体开始");
							}
							
							// 输出内容
							String river = xmlParser.nextText();
							Logger.w("river : " + river);
							if (null == riverBuilder) {
								riverBuilder = new StringBuilder();
							}
							
							riverBuilder.append(river);
							
							
							if (isItalic == true) {      //检测到斜体标签,在输入值之后,输入</i>,并且斜体状态=false
								Logger.d("斜体结束");
								isItalic = false;
							}
							if (isUnderline == true) {//检测到下划线标签,在输入值之后,输入</u>,并且下划线状态=false
								Logger.d("下划线结束");
								isUnderline = false;
							}
							if (isBold == true) {   //加粗
								Logger.d("加粗结束");
								isBold = false;
							}
							if (isSize == true) {   //检测到大小设置,输入结束标签
								Logger.d("大小设置结束");
								isSize = false;
							}
							if (isColor == true) {  //检测到颜色设置存在,输入结束标签
								Logger.d("颜色结束");
								isColor = false;
							}
							if (isCenter == true) {   //检测到居中,输入结束标签
								Logger.d("居中结束");
								isCenter = false;
							}
							if (isRight == true) {  //居右不能使用<right></right>,使用div可能会有状况,先用着
								Logger.d("右对齐结束");
								isRight = false;
							}
						}
						break;
					//结束标签
					case XmlPullParser.END_TAG:
						String tag2 = xmlParser.getName();
						if (tag2.equalsIgnoreCase("tbl")) {  //检测到表格结束,更改表格状态
							Logger.d("docx表格结束");
							isTable = false;
						}
						if (tag2.equalsIgnoreCase("tr")) {  //行结束
							Logger.d("docx表格行结束");
						}
						if (tag2.equalsIgnoreCase("tc")) {  //列结束
							Logger.d("docx表格列结束");
						}
						if (tag2.equalsIgnoreCase("p")) {   //p结束,如果在表格中就无视
							Logger.d("docx p标签结束");
							if (isTable == false) {
								if (null != riverBuilder) {
									riverBuilder.append("\n");
								}
							}
						}
						if (tag2.equalsIgnoreCase("r")) {
							Logger.d("docx isR标签结束");
							isR = false;
						}
						break;
					default:
						break;
				}
				evtType = xmlParser.next();
			}
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		if (riverBuilder == null) {
			Logger.e("解析文件出错");
		} else {
			return riverBuilder.toString();
		}
		
		return null;
	}

	private void writeDOCXPicture(byte[] pictureBytes) {
		//Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
		
	}
}
