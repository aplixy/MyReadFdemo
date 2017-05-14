package com.example.myreadfdemo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.myreadfdemo.R;
import com.example.myreadfdemo.ui.fragment.EditNoteFragment;
import com.xinyu.xylibrary.utils.AndroidUtils;

/**
 * Created by lixinyu on 2017/5/14.
 */

public class EditNoteActivity extends AppCompatActivity {

	private Fragment mFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_common_container);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		mFragment = EditNoteFragment.newInstance();
		mFragment.setArguments(AndroidUtils.intentToFragmentArguments(getIntent()));
		getSupportFragmentManager().beginTransaction().replace(R.id.activity_common_container_root, mFragment).commit();
	}

	public static void start(Context context, String title, long id){
		Intent intent = new Intent(context, EditNoteActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("id", id);
		context.startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
