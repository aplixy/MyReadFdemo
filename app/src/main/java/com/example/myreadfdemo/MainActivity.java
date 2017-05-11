package com.example.myreadfdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myreadfdemo.utils.FileUtils;
import com.yxp.permission.util.lib.PermissionUtil;
import com.yxp.permission.util.lib.callback.PermissionResultCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private ListView mListView;
	private List<ListItem> mDatas;
	private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		findVies();
		init();
		setListener();
    }

	private void findVies() {
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swiperefreshlayout);
		mListView = (ListView) findViewById(R.id.main_listview);
	}

	private void init() {
		PermissionUtil.getInstance().request(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
				new PermissionResultCallBack() {
					@Override
					public void onPermissionGranted() {
						// 当所有权限的申请被用户同意之后,该方法会被调用
						mSwipeRefreshLayout.setRefreshing(true);
						loadData();
					}

					@Override
					public void onPermissionGranted(String... strings) {
						// 返回此次申请中通过的权限列表
					}

					@Override
					public void onPermissionDenied(String... permissions) {
						// 当权限申请中的某一个或多个权限,被用户曾经否定了,并确认了不再提醒时,也就是权限的申请窗口不能再弹出时,该方法将会被调用
					}

					@Override
					public void onRationalShow(String... permissions) {
						// 当权限申请中的某一个或多个权限,被用户否定了,但没有确认不再提醒时,也就是权限窗口申请时,但被否定了之后,该方法将会被调用.
					}
				});

	}

	private void setListener() {
		mListView.setOnItemClickListener(this);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadData();
			}
		});
	}

	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ListItem item = mDatas.get(position);
		if (null != item) {
			Intent intent = new Intent(this, FileReadActivity.class);
			intent.putExtra("filePath", item.filePath);
			startActivity(intent);
		}
	}
	
	private void loadData() {
		
		FileUtils.getSuffixesFile(new String[]{"doc", "docx"}, new FileUtils.FileResultListener() {
			@Override
			public void onResult(List<File> files) {
				if (null == files || files.size() == 0) {
					files = new ArrayList<File>();
					FileUtils.copySingleAssetsToDst(MainActivity.this, "test_assets.doc", FileUtils.APP_PATH + "test_assets.doc");
					FileUtils.copySingleAssetsToDst(MainActivity.this, "test_assets.docx", FileUtils.APP_PATH + "test_assets.docx");

					files.add(new File(FileUtils.APP_PATH + "test_assets.doc"));
					files.add(new File(FileUtils.APP_PATH + "test_assets.docx"));
				}

				FileUtils.copySingleAssetsToDst(MainActivity.this, "test_assets3.doc", FileUtils.APP_PATH + "test_assets3.doc");
				FileUtils.copySingleAssetsToDst(MainActivity.this, "test_assets2.docx", FileUtils.APP_PATH + "test_assets2.docx");

				files.add(new File(FileUtils.APP_PATH + "test_assets3.doc"));
				files.add(new File(FileUtils.APP_PATH + "test_assets2.docx"));
				
				mDatas = new ArrayList<ListItem>();
				for (File file : files) {
					if (null == file) continue;

					ListItem item = new ListItem();
					item.name = file.getName();
					item.path = file.getParent();
					if (null != item.name) {
						String name = item.name.toLowerCase();
						if (name.endsWith("doc")) {
							item.image = R.drawable.word2003;
						} else if (name.endsWith("docx")) {
							item.image = R.drawable.word2007;
						}
					}

					item.filePath = file.getAbsolutePath();

					mDatas.add(item);
				}

				mAdapter = new MyAdapter(MainActivity.this, mDatas);
				mListView.setAdapter(mAdapter);

				mSwipeRefreshLayout.setRefreshing(false);
			}
		});
	}
}
