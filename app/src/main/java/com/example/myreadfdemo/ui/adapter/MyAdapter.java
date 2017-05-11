package com.example.myreadfdemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myreadfdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixinyu on 2017/5/9.
 */

public class MyAdapter extends BaseAdapter {
	private Context mContext;
	private List<ListItem> mDatas;
	
	public MyAdapter(Context context, List<ListItem> datas) {
		this.mContext = context;
		if (null == datas) datas = new ArrayList<>();
		this.mDatas = datas;
	}
	
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (null == convertView) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout, null);
			vh.ivIcon = (ImageView) convertView.findViewById(R.id.item_iv_icon);
			vh.tvName = (TextView) convertView.findViewById(R.id.item_tv_name);
			vh.tvPath = (TextView) convertView.findViewById(R.id.item_tv_path);
			
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		ListItem item = mDatas.get(position);
		if (null != item) {
			vh.ivIcon.setImageResource(item.image);
			vh.tvName.setText(item.name);
			vh.tvPath.setText(item.path);
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		ImageView ivIcon;
		TextView tvName;
		TextView tvPath;
	}
}
