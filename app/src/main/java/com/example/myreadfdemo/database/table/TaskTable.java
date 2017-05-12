
package com.example.myreadfdemo.database.table;

import android.net.Uri;

import com.example.myreadfdemo.database.NoteProvider;
import com.xinyu.xylibrary.database.AppBaseColumns;


/**
 * @Description: 我的电台，包含收藏电台和播放历史电台
 * @author lwz
 * @date 2013-9-13 上午9:43:02
 * @version V1.0
 */
public class TaskTable implements AppBaseColumns {

	/**
	 * 表名称
	 */
	public static final String TABLE_NAME = "task";

    public static final Uri CONTENT_URI = Uri.parse(NoteProvider.CONTENT_AUTHORITY_SLASH
            + TABLE_NAME);

    public static final String CONTENT_TYPE = BASE_MIME_TYPE_MULTIPLE + "notedb.task";

    public static final String ENTRY_CONTENT_TYPE = BASE_MIME_TYPE_IDENTIFY + "notedb.task";

	

    /**
     * 默认的排序字段
     */
    public static final String DEFAULT_SORT_ORDER = _ID;

    public static final String TITLE = "title";
	
    public static final String SUMMARY = "type";

    public static final String PATH = "name";
	
    public static final String IMG_URLS = "img_urls";
	
    public static final String ATTACH_URLS = "attach_urls";

    /**
     * 创建语句
     */
    public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " ("
            + AppBaseColumns._ID + " INTEGER PRIMARY KEY,"
            + TaskTable.TITLE + " TEXT,"
            + TaskTable.SUMMARY + " TEXT,"
            + TaskTable.PATH + " TEXT,"
            + TaskTable.IMG_URLS + " TEXT,"
            + TaskTable.ATTACH_URLS + " TEXT,"
            + TaskTable.CREATE_AT + " TEXT,"
            + TaskTable.MODIFIED_AT + " TEXT);";

}
