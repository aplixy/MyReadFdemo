
package com.xinyu.xylibrary.database;

import android.content.Context;

import java.util.List;

/**
 * 数据库表操作类
 * 
 * @author chenys
 */
public abstract class BaseDao<T extends Object> {

    protected Context mContext;

    public BaseDao(Context context) {
        mContext = context;
    }

    /**
     * 新增
     * 
     * @param t
     * @return
     */
    public abstract long insert(T t);

    /**
     * 更新
     * 
     * @param t
     */
    public abstract long update(T t);

    /**
     * 删除
     * 
     * @return
     */
    public abstract long deleteById(long id);

    /**
     * 查询
     * 
     * @return
     */
    public abstract T queryById(long id);

    /**
     * 查询
     * 
     * @param orderby
     * @param limit
     * @return
     */
    public abstract List<T> queryAll(String orderby, int limit);

    /**
     * 查询
     * 
     * @param limit
     * @return
     */
    public abstract List<T> queryAll(int limit);

    /**
     * 查询
     * 
     * @return
     */
    public abstract List<T> queryAll();

    

    /**
     * 获取数量
     * 
     * @param selection
     * @param selectionArgs
     * @return
     */
    public abstract int getCount(String selection, String[] selectionArgs);
}
