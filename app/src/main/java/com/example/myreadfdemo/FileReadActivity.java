package com.example.myreadfdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myreadfdemo.utils.MyFileReader;

import java.io.File;

public class FileReadActivity extends AppCompatActivity {


	private TextView tv;
	
	//private String filePath = "mnt/sdcard/bendan.xlsx";
	//private String filePath = "mnt/sdcard/test.doc";
	private String filePath;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_read);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		tv = (TextView) findViewById(R.id.file_read_textview);
		
		filePath = getIntent().getStringExtra("filePath");
		
		setTitle(new File(filePath).getName());

		String content = MyFileReader.getInstance().read(filePath);
		tv.setText(content);
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
