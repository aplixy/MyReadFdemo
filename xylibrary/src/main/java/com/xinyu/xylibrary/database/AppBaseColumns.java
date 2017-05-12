
package com.xinyu.xylibrary.database;

import android.provider.BaseColumns;

/**
 * 描述:基本字段
 * 
 * @author chenys
 * @since 2013-7-23 下午12:14:43
 */
public interface AppBaseColumns extends BaseColumns {

    /**
     * 创建时间
     */
    public static final String CREATE_AT = "createAt";

    /**
     * 修改时间
     */
    public static final String MODIFIED_AT = "modifiedAt";
	
	String BASE_MIME_TYPE_MULTIPLE = "vnd.android.cursor.dir/";
	String BASE_MIME_TYPE_IDENTIFY = "vnd.android.cursor.item/";

}
