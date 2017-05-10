package com.example.myreadfdemo.text_type;

import com.example.myreadfdemo.utils.Logger;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by lixinyu on 2017/5/8.
 */

public class DocOfficeItem extends AbsOfficeItem {
	private String mFilePath;
	private FileInputStream mFileInputStream;

	private List<Picture> pictures;
	private TableIterator tableIterator;

	private int presentPicture = 0;

	public DocOfficeItem(String filePath) {
		this.mFilePath = filePath;
	}
	

	@Override
	public CharSequence getContent() {
		Range range = getRange();
		StringBuilder builder = readDOC(range);
		
		if (null != builder) return builder.toString();

		return null;
	}

	private Range getRange() {
		FileInputStream in = null;
		POIFSFileSystem pfs = null;

		HWPFDocument hwpf = null;
		Range range = null;

		try {
			in = new FileInputStream(mFilePath);
			pfs = new POIFSFileSystem(in);
			hwpf = new HWPFDocument(pfs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (null != hwpf) {
			range = hwpf.getRange();
			pictures = hwpf.getPicturesTable().getAllPictures();
			tableIterator = new TableIterator(range);
		}
		
		return range;

	}

	/*读取word中的内容写到sdcard上的.html文件中*/
	private StringBuilder readDOC(Range range) {
		if (null == range) return null;
		
		StringBuilder builder = null;

		try {

			int numParagraphs = range.numParagraphs();

			for (int i = 0; i < numParagraphs; i++) {
				Paragraph p = range.getParagraph(i);

				if (!(p.isInTable())) {
					Logger.d("paragraph begin");
					
					if (builder == null) {
						builder = new StringBuilder();
					}
					
					builder.append(writeParagraphContent(p));
					Logger.d("paragraph end");
				} else {
					int temp = i;
					if (tableIterator.hasNext()) {


						Table table = tableIterator.next();

						Logger.d("table begin");

						int rows = table.numRows();

						for (int r = 0; r < rows; r++) {
							Logger.d("row begin");
							TableRow row = table.getRow(r);
							int cols = row.numCells();
							int rowNumParagraphs = row.numParagraphs();
							int colsNumParagraphs = 0;
							for (int c = 0; c < cols; c++) {
								Logger.d("col begin");
								TableCell cell = row.getCell(c);
								int max = temp + cell.numParagraphs();
								colsNumParagraphs = colsNumParagraphs + cell.numParagraphs();
								for (int cp = temp; cp < max; cp++) {
									Paragraph p1 = range.getParagraph(cp);
									Logger.d("paragraph begin");

									if (builder == null) {
										builder = new StringBuilder();
									}
									builder.append(writeParagraphContent(p1));
									
									Logger.d("paragraph end");
									temp++;
								}
								Logger.d("col end");
							}
							int max1 = temp + rowNumParagraphs;
							for (int m = temp + colsNumParagraphs; m < max1; m++) {
								Paragraph p2 = range.getParagraph(m);
								temp++;
							}
							Logger.d("row end");
						}
						Logger.d("table end");
					}
					i = temp;
				}
			}

		} catch (Exception e) {
			Logger.d("readAndWrite Exception");
		}
		
		return builder;
	}

	private StringBuilder writeParagraphContent(Paragraph paragraph) {
		StringBuilder builder = null;

		int pnumCharacterRuns = paragraph.numCharacterRuns();

		for (int j = 0; j < pnumCharacterRuns; j++) {

			CharacterRun run = paragraph.getCharacterRun(j);

			if (run.getPicOffset() == 0 || run.getPicOffset() >= 1000) {
				writePicture();
			} else {
				try {
					String text = run.text();
					if (builder == null) {
						builder = new StringBuilder();
					}

					builder.append(text);

					if (text.length() >= 2 && pnumCharacterRuns < 2) {
						Logger.i("没有样式");
					} else {
						Logger.i("有样式");

						int size = run.getFontSize();
						int color = run.getColor();

						Logger.d("size : " + size);
						Logger.d("color : " + color);
						Logger.d("isBold : " + run.isBold());
						Logger.d("isItalic : " + run.isItalic());

					}

					Logger.w("run.text() : " + text);

				} catch (Exception e) {
					Logger.d("Write File Exception");
				}
			}
		}
		
		if (null != builder) builder.append("\n");

		return builder;
	}

	private void writePicture() {
		if (presentPicture >= pictures.size()) return;

		Picture picture = pictures.get(presentPicture);

		presentPicture++;

		//byte[] pictureBytes = picture.getContent();
		//Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);


	}
}
