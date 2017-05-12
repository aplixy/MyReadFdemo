package com.example.myreadfdemo.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Created by lixinyu on 2017/5/12.
 */

public class TableHelper {

	/**
	 * 检查数据库中某个表是否存在
	 *
	 * @param db        数据库
	 * @param tableName 表名
	 * @return
	 */
	public static boolean checkTableExists(SQLiteDatabase db, String tableName) {
		Cursor cursor = db.rawQuery(
				"select count(*) from sqlite_master where type='table' and name="
						+ "'" + tableName + "'", null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					cursor.close();
					return true;
				}
			}
			cursor.close();
		}
		return false;
	}

	/**
	 * 检查表中某列是否存在
	 *
	 * @param db
	 * @param tableName  表名
	 * @param columnName 列名
	 * @return
	 */
	public static boolean checkColumnExists(SQLiteDatabase db,
											 String tableName, String columnName) {
		boolean result = false;
		Cursor cursor = null;

		try {
			cursor = db.rawQuery(
					"select * from sqlite_master where name = ? and sql like ?",
					new String[]{tableName, "%" + columnName + "%"});
			result = null != cursor && cursor.moveToFirst();
		} catch (Exception e) {

		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}

	public static void addColumn(SQLiteDatabase db, String table, String newColumn,
						   String dataType, String defaultValue) {
		String sql = null;
		if (TextUtils.isEmpty(defaultValue)) {
			sql = String.format("ALTER TABLE %s ADD COLUMN %s %s", table,
					newColumn, dataType);
		} else {
			sql = String.format("ALTER TABLE %s ADD COLUMN %s %s DEFAULT %s",
					table, newColumn, dataType, defaultValue);
		}
		db.execSQL(sql);
	}

	public static void addColumn(SQLiteDatabase db, String table, String newColumn,
						   String dataType) {
		addColumn(db, table, newColumn, dataType, null);
	}
}
