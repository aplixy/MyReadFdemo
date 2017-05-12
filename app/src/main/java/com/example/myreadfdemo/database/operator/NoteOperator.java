
package com.example.myreadfdemo.database.operator;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.myreadfdemo.MyApplication;
import com.example.myreadfdemo.database.entity.NoteEntity;
import com.example.myreadfdemo.database.table.NoteTable;
import com.xinyu.xylibrary.database.BaseDbOperator;

import java.util.ArrayList;
import java.util.List;

public class NoteOperator extends BaseDbOperator<NoteEntity> {

    public NoteOperator(Context context) {
        super(context);
    }

    private static NoteOperator noteOperator;

    public static synchronized NoteOperator getInstance() {
        if (noteOperator == null) {
            Context context = MyApplication.getApplication();
            if (context == null) {
                throw new IllegalArgumentException("context is null!");
            }
            noteOperator = new NoteOperator(context);
        }
        return noteOperator;
    }

    @Override
    public long insert(NoteEntity note) {
        ContentValues cv = entityToContentValues(note);
		if (null == cv) return -1;
		
        Uri uri = mContext.getContentResolver().insert(NoteTable.CONTENT_URI, cv);
        if (uri != null) {
            return ContentUris.parseId(uri);
        }
        return -1;
    }

    @Override
    public long update(ContentValues cv, String selection, String[] selectionArgs) {
        return mContext.getContentResolver().update(NoteTable.CONTENT_URI, cv, selection,
                selectionArgs);
    }

    @Override
    public long delete(String selection, String[] selectionArgs) {
        return mContext.getContentResolver().delete(NoteTable.CONTENT_URI, selection,
                selectionArgs);
    }

    @Override
    public ArrayList<NoteEntity> query(String selection, String[] selectionArgs, String orderby) {
        return query(selection, selectionArgs, orderby, 0);
    }

    @Override
    public ArrayList<NoteEntity> query(String selection, String[] selectionArgs, String orderby,
									   int limit) {
        Uri uri = NoteTable.CONTENT_URI;
        if (limit > 0) {
            uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
        }
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, selectionArgs, orderby);
        ArrayList<NoteEntity> notes = new ArrayList<NoteEntity>();
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                notes.add(cursorToEntity(cursor));
            }
            cursor.close();
        }
        return notes;
    }

    @Override
    public NoteEntity query(String selection, String[] selectionArgs) {
        List<NoteEntity> radios = query(selection, selectionArgs, NoteTable.MODIFIED_AT
				+ " DESC ");
        if (radios != null && radios.size() > 0) {
            return radios.get(0);
        }
        return null;
    }

    @Override
    public int getCount(String selection, String[] selectionArgs) {
        String[] projection = {
                " count(*) "
        };
        Cursor cursor = mContext.getContentResolver().query(NoteTable.CONTENT_URI, projection, selection, selectionArgs, NoteTable.DEFAULT_SORT_ORDER);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

	@Override
	public NoteEntity cursorToEntity(Cursor cursor) {
		if (null == cursor) return null;

		NoteEntity note = new NoteEntity();

		note.id = cursor.getLong(cursor.getColumnIndexOrThrow(NoteTable._ID));
		note.title = cursor.getString(cursor.getColumnIndexOrThrow(NoteTable.TITLE));
		note.summary = cursor.getString(cursor.getColumnIndexOrThrow(NoteTable.SUMMARY));
		note.createAt = cursor.getLong(cursor.getColumnIndexOrThrow(NoteTable.CREATE_AT));
		note.modifyAt = cursor.getLong(cursor.getColumnIndexOrThrow(NoteTable.MODIFIED_AT));
		note.path = cursor.getString(cursor.getColumnIndexOrThrow(NoteTable.PATH));
		note.imgUrls = cursor.getString(cursor.getColumnIndexOrThrow(NoteTable.IMG_URLS));
		note.attachUrls = cursor.getString(cursor.getColumnIndexOrThrow(NoteTable.ATTACH_URLS));
		note.cloudId = cursor.getString(cursor.getColumnIndexOrThrow(NoteTable.CLOUD_ID));
		
		return note;
	}

	@Override
	public ContentValues entityToContentValues(NoteEntity note) {
		if (null == note) return null;

		ContentValues cv = new ContentValues();
		
		if (note.id > 0) cv.put(NoteTable._ID, note.id);

		cv.put(NoteTable.TITLE, note.title);
		cv.put(NoteTable.SUMMARY, note.summary);
		cv.put(NoteTable.PATH, note.path);
		cv.put(NoteTable.IMG_URLS, note.imgUrls);
		cv.put(NoteTable.ATTACH_URLS, note.imgUrls);
		cv.put(NoteTable.CLOUD_ID, note.cloudId);
		
		return cv;
	}
}
