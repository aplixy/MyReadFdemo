
package com.xinyu.xylibrary.database;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 描述:抽象ContentProvider，实现了数据库创建功能
 * 
 * @author chenys
 * @since 2013-7-23 上午11:47:26
 */
public abstract class BaseContentProvider extends ContentProvider {

    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return mDatabaseHelper != null;
    }

    /**
     * 获取可读数据库
     * 
     * @return
     */
    protected SQLiteDatabase getReadableDatabase() {
        // 读取数据库时可能出现数据库损坏，这时重新解压读取
        SQLiteDatabase readDatabase = null;
        try {
            readDatabase = mDatabaseHelper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readDatabase;
    }

    /**
     * 获取可读写数据库
     * 
     * @return
     */
    protected SQLiteDatabase getWritableDatabase() {
        return mDatabaseHelper.getWritableDatabase();
    }

    /**
     * 返回数据库名称
     * 
     * @return
     */
    protected abstract String getDatabaseName();

    /**
     * 返回数据库版本号
     * 
     * @return
     */
    protected abstract int getDatabaseVersion();

    /**
     * 数据库创建成功，可以在这个回调里去创建数据表
     */
    protected abstract void onDatabaseCreate(SQLiteDatabase db);

    /**
     * 数据库升级
     * 
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    protected abstract void onDatabaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, BaseContentProvider.this.getDatabaseName(), null, getDatabaseVersion());
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            onDatabaseCreate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onDatabaseUpgrade(db, oldVersion, newVersion);
        }

        // 4.0以上系统在数据库从高降到低时，会强制抛出异常，通过重写这个方法，可以解决问题
        @SuppressLint("Override")
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // 虽然没调用到，但要保留本函数
        }

    }

}
