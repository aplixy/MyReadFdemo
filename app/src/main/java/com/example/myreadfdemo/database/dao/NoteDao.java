package com.example.myreadfdemo.database.dao;

import android.content.Context;

import com.example.myreadfdemo.database.entity.NoteEntity;
import com.example.myreadfdemo.database.operator.NoteOperator;
import com.example.myreadfdemo.database.table.NoteTable;
import com.xinyu.xylibrary.database.BaseDao;

import java.util.List;

/**
 * Created by lixinyu on 2017/5/12.
 */

public class NoteDao extends BaseDao<NoteEntity> {
	
	private NoteOperator mOperator;


	public NoteDao(Context context) {
		super(context);
		
		mOperator = NoteOperator.getInstance();
	}

	@Override
	public long insert(NoteEntity noteEntity) {
		return mOperator.insert(noteEntity);
	}

	@Override
	public long update(NoteEntity noteEntity) {
		String selection = NoteTable._ID + "=?";
		String[] selectionArgs = {noteEntity.id + ""};
		
		return mOperator.update(mOperator.entityToContentValues(noteEntity), selection, selectionArgs);
	}

	@Override
	public long deleteById(long id) {

		String selection = NoteTable._ID + "=?";
		String[] selectionArgs = {id + ""};

		return mOperator.delete(selection, selectionArgs);
	}

	@Override
	public NoteEntity queryById(long id) {
		String selection = NoteTable._ID + "=?";
		String[] selectionArgs = {id + ""};

		return mOperator.query(selection, selectionArgs);
	}

	@Override
	public List<NoteEntity> queryAll(String orderby, int limit) {
		return mOperator.query(null, null, orderby, limit);
	}

	@Override
	public List<NoteEntity> queryAll(int limit) {
		return queryAll(NoteTable.MODIFIED_AT + " DESC", limit);
	}

	@Override
	public List<NoteEntity> queryAll() {
		return mOperator.query(null, null, NoteTable.MODIFIED_AT + " DESC");
	}

	@Override
	public int getCount(String selection, String[] selectionArgs) {
		return mOperator.getCount(null, null);
	}
}
