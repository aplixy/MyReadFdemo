package com.example.myreadfdemo.utils;

import com.example.myreadfdemo.database.dao.NoteDao;
import com.example.myreadfdemo.database.entity.NoteEntity;
import com.xinyu.xylibrary.utils.FileUtils;

import java.io.File;

/**
 * Created by lixinyu on 2017/5/14.
 */

public class NoteSaveHelper {
	
	private static NoteSaveHelper mInstance;
	
	private NoteDao mNoteDao;
	
	private NoteSaveHelper() {
		mNoteDao = new NoteDao(null);
	}
	
	public static NoteSaveHelper getInstance() {
		synchronized (NoteSaveHelper.class) {
			if (null == mInstance) {
				synchronized (NoteSaveHelper.class) {
					if (null == mInstance) {
						mInstance = new NoteSaveHelper();
					}
				}
			}
		}
		
		
		return mInstance;
	}
	
	public void saveNote(long id, String title, String content) {
		content = null == content ? "" : content;
		
		NoteEntity entity = new NoteEntity();
		entity.title = title;
		entity.summary = content.substring(0, Math.min(50, content.length()));
		
		if (id > 0) {
			entity.id = id;
			mNoteDao.update(entity);
		} else {
			id = mNoteDao.insert(entity);
		}
		
		String filePath = getFilePath(id);
		
		FileUtils.saveFile(filePath, content);
	}
	
	public String readNote(long id) {
		if (id <= 0) return null;
		
		return FileUtils.readFileByLines(getFilePath(id));
	}
	
	private String getFilePath(long id) {
		return FileUtils.APP_PATH + "NormalNote" + File.separator + id + ".txt";
	}
}
