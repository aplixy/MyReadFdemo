package com.example.myreadfdemo.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.myreadfdemo.database.table.NoteTable;
import com.example.myreadfdemo.database.table.TaskTable;
import com.xinyu.xylibrary.database.AppBaseColumns;
import com.xinyu.xylibrary.database.BaseContentProvider;


/**
 * @author lwz
 * @version V1.0
 * @Description:
 * @date 2013-9-13 上午10:24:55
 */
public class NoteProvider extends BaseContentProvider {

	private static final String TAG = "NoteProvider";

	public static final String AUTHORITY = "com.csi.note";

	public static final String CONTENT_AUTHORITY_SLASH = "content://"
			+ AUTHORITY + "/";

	private static final UriMatcher mUriMatcher;

	private static final int URI_CODE_NOTE = 1;
	private static final int URI_CODE_NOTE_ID = 2;

	private static final int URI_CODE_TASK = 3;
	private static final int URI_CODE_TASK_ID = 4;

	


	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(AUTHORITY, NoteTable.TABLE_NAME, URI_CODE_NOTE);
		mUriMatcher.addURI(AUTHORITY, NoteTable.TABLE_NAME + "/#", URI_CODE_NOTE_ID);
	}

	/**
	 * 数据库名称
	 */
	public static final String DATABASE_NAME = "note.db";

	/**
	 * 数据库版本号
	 */
	public static final int DATABASE_VERSION = 1;// 2015-5-12 3.0.2发版是10,3.1.1版本改成14


	@Override
	protected String getDatabaseName() {
		return DATABASE_NAME;
	}

	@Override
	protected int getDatabaseVersion() {
		return DATABASE_VERSION;
	}

	/**
	 * 数据库初始化完毕，开始创建表
	 * @param db
	 */
	@Override
	protected void onDatabaseCreate(SQLiteDatabase db) {
		// 创建评论表
		db.execSQL(NoteTable.CREATE_TABLE_SQL);

	}

	@Override
	protected void onDatabaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// *****数据库升级逻辑*****//
		for (int i = oldVersion + 1; i <= newVersion; i++) {
			databaseUpgradeTo(db, i);
		}
	}

	private void databaseUpgradeTo(SQLiteDatabase db, int newVersion) {
		switch (newVersion) {
			case 2:
				// 创建搜索历史列表
				//db.execSQL(SearchHistoryTable.CREATE_TABLE_SQL);
				break;
			default:
				break;
		}
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		switch (mUriMatcher.match(uri)) {
			case URI_CODE_NOTE:
				return NoteTable.CONTENT_TYPE;
			case URI_CODE_NOTE_ID:
				return NoteTable.ENTRY_CONTENT_TYPE;
			case URI_CODE_TASK:
				return TaskTable.CONTENT_TYPE;
			case URI_CODE_TASK_ID:
				return TaskTable.ENTRY_CONTENT_TYPE;
			default:
				throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int matcher = mUriMatcher.match(uri);

		String tableName;
		switch (matcher) {
			case URI_CODE_NOTE:
				tableName = NoteTable.TABLE_NAME;
				break;
			case URI_CODE_TASK:
				tableName = TaskTable.TABLE_NAME;
				break;

			default:
				throw new IllegalArgumentException("Failed to insert, Unknown Uri: " + uri
						+ " matcher" + matcher);
		}

		SQLiteDatabase db = getWritableDatabase();

		Uri retUri = null;
		ContentValues initValues = values != null ? new ContentValues(values) : new ContentValues();
		
		long currentMillis = System.currentTimeMillis();
		
		initValues.put(AppBaseColumns.CREATE_AT, currentMillis);
		initValues.put(AppBaseColumns.MODIFIED_AT, currentMillis);
		
		long rowId = db.insert(tableName, null, initValues);
		if (rowId > 0) {
			retUri = ContentUris.withAppendedId(
					NoteTable.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(retUri, null);
		}

		return retUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int matcher = mUriMatcher.match(uri);
		SQLiteDatabase db = getWritableDatabase();
		int count = 0;
		switch (matcher) {
			case URI_CODE_NOTE:
				count = db.delete(NoteTable.TABLE_NAME, selection,
						selectionArgs);
				break;
			case URI_CODE_NOTE_ID:
				count = db.delete(NoteTable.TABLE_NAME,
						NoteTable._ID
								+ "="
								+ ContentUris.parseId(uri)
								+ (TextUtils.isEmpty(selection) ? ""
								: (" AND " + selection)), selectionArgs);
			case URI_CODE_TASK:
				count = db.delete(TaskTable.TABLE_NAME, selection,
						selectionArgs);
				break;
			case URI_CODE_TASK_ID:
				count = db.delete(TaskTable.TABLE_NAME,
						NoteTable._ID
								+ "="
								+ ContentUris.parseId(uri)
								+ (TextUtils.isEmpty(selection) ? ""
								: (" AND " + selection)), selectionArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown Uri: " + uri
						+ " Matcher : " + matcher);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {
		String tableName;
		
		switch (mUriMatcher.match(uri)) {
			case URI_CODE_NOTE:
				tableName = NoteTable.TABLE_NAME;
				break;
			case URI_CODE_NOTE_ID:
				tableName = NoteTable.TABLE_NAME;
				selection = NoteTable._ID
						+ "="
						+ ContentUris.parseId(uri)
						+ (TextUtils.isEmpty(selection) ? "" : (" AND " + selection));
				break;
			case URI_CODE_TASK:
				tableName = TaskTable.TABLE_NAME;
				break;
			case URI_CODE_TASK_ID:
				tableName = TaskTable.TABLE_NAME;
				selection = TaskTable._ID
						+ "="
						+ ContentUris.parseId(uri)
						+ (TextUtils.isEmpty(selection) ? "" : (" AND " + selection));
				break;


			default:
				throw new IllegalArgumentException("Unknown Uri: " + uri);
		}

		SQLiteDatabase db = getWritableDatabase();
		ContentValues initValues = values == null ? new ContentValues()
				: new ContentValues(values);

		initValues.put(AppBaseColumns.MODIFIED_AT, System.currentTimeMillis());
		
		int count = db.update(tableName, initValues, selection,
				selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
						String[] selectionArgs, String sortOrder) {
		String orderBy = null;
		String table = null;
		int matcher = mUriMatcher.match(uri);
		switch (matcher) {
			case URI_CODE_NOTE:
				if (TextUtils.isEmpty(sortOrder)) {
					orderBy = NoteTable.DEFAULT_SORT_ORDER;
				} else {
					orderBy = sortOrder;
				}
				table = NoteTable.TABLE_NAME;
				break;
			case URI_CODE_NOTE_ID:
				if (TextUtils.isEmpty(selection)) {
					selection = NoteTable._ID + "=" + ContentUris.parseId(uri);
				} else {
					selection = selection + " AND " + NoteTable._ID + "="
							+ ContentUris.parseId(uri);
				}
				if (TextUtils.isEmpty(sortOrder)) {
					orderBy = NoteTable.DEFAULT_SORT_ORDER;
				} else {
					orderBy = sortOrder;
				}
				table = NoteTable.TABLE_NAME;
				break;
			case URI_CODE_TASK:
				if (TextUtils.isEmpty(sortOrder)) {
					orderBy = TaskTable.DEFAULT_SORT_ORDER;
				} else {
					orderBy = sortOrder;
				}
				table = TaskTable.TABLE_NAME;
				break;
			case URI_CODE_TASK_ID:
				if (TextUtils.isEmpty(selection)) {
					selection = TaskTable._ID + "=" + ContentUris.parseId(uri);
				} else {
					selection = selection + " AND " + TaskTable._ID + "="
							+ ContentUris.parseId(uri);
				}
				if (TextUtils.isEmpty(sortOrder)) {
					orderBy = TaskTable.DEFAULT_SORT_ORDER;
				} else {
					orderBy = sortOrder;
				}
				table = TaskTable.TABLE_NAME;
				break;
			default:
				throw new IllegalArgumentException("Unknown Uri: " + uri
						+ " matcher" + matcher);
		}
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(table, projection, selection, selectionArgs, null,
				null, orderBy);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if (uri == null || values == null) return 0;

		SQLiteDatabase db = null;
		int numValues = 0;
		
		try {
			db = getWritableDatabase();
			
			db.beginTransaction(); //开始事务
			// 数据库操作
			numValues = values.length;
			for (int i = 0; i < numValues; i++) {
				insert(uri, values[i]);
			}
			db.setTransactionSuccessful(); // 别忘了这句 Commit
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction(); // 结束事务
		}

		return numValues;
	}

}
