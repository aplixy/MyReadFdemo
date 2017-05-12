
package com.example.myreadfdemo.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 描述: 电台数据
 * 
 * @author gongzhenjie
 * @since 2013年9月12日 下午1:08:50
 */
public class NoteEntity implements Parcelable, Serializable {
	
	private static final long serialVersionUID = 1054963327763880830L;

    public long id;
	public String title;
	public String summary;
	public long createAt;
	public long modifyAt;
	public String path;
	public String imgUrls;
	public String attachUrls;
	public String cloudId;
    
    

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeLong(this.createAt);
        dest.writeLong(this.modifyAt);
        dest.writeString(this.path);
        dest.writeString(this.imgUrls);
        dest.writeString(this.attachUrls);
        dest.writeString(this.cloudId);
    }

    public static final Parcelable.Creator<NoteEntity> CREATOR = new Parcelable.Creator<NoteEntity>() {

        @Override
        public NoteEntity createFromParcel(Parcel source) {
            NoteEntity noteEntity = new NoteEntity();
            noteEntity.id = source.readLong();
            noteEntity.title = source.readString();
            noteEntity.summary = source.readString();
            noteEntity.createAt = source.readLong();
            noteEntity.modifyAt = source.readLong();
            noteEntity.path = source.readString();
            noteEntity.imgUrls = source.readString();
            noteEntity.attachUrls = source.readString();
            noteEntity.cloudId = source.readString();
            return noteEntity;
        }

        @Override
        public NoteEntity[] newArray(int size) {
            return new NoteEntity[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if(o == null ){
            return false;
        }
        if(!(o instanceof NoteEntity)){
            return false;
        }
        
        return this.id == ((NoteEntity) o).id;
    }

    
}
