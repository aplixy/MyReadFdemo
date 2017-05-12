package com.example.myreadfdemo.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.example.myreadfdemo.Action;
import com.example.myreadfdemo.R;
import com.example.myreadfdemo.broadcast.BroadcastManager;
import com.example.myreadfdemo.database.dao.NoteDao;
import com.example.myreadfdemo.database.entity.NoteEntity;
import com.example.myreadfdemo.ui.adapter.FragmentAdapter;
import com.aspsine.fragmentnavigator.widget.BottomNavigatorView;
import com.example.myreadfdemo.ui.widget.MidBtnBottomNaviView;
import com.example.myreadfdemo.utils.SharedPrefUtils;
import com.xinyu.xylibrary.utils.Logger;

import java.util.List;


public class MainActivity extends AppCompatActivity implements BottomNavigatorView.OnBottomNavigatorViewItemClickListener {

    private static final int DEFAULT_POSITION = 0;

    private FragmentNavigator mNavigator;

    private MidBtnBottomNaviView bottomNavigatorView;

    private MenuItem mLoginMenu;

    private MenuItem mLogoutMenu;
	
	private NoteDao mNoteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentAdapter(), R.id.container);
        mNavigator.setDefaultPosition(DEFAULT_POSITION);
        mNavigator.onCreate(savedInstanceState);

        bottomNavigatorView = (MidBtnBottomNaviView) findViewById(R.id.bottomNavigatorView);
        if (bottomNavigatorView != null) {
            bottomNavigatorView.setOnBottomNavigatorViewItemClickListener(this);
        }

        setCurrentTab(mNavigator.getCurrentPosition());

        BroadcastManager.register(this, mLoginStatusChangeReceiver, Action.LOGIN, Action.LOGOUT);
		
		//addTestData();
		//queryTestData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mLoginMenu = menu.findItem(R.id.action_login);
        mLogoutMenu = menu.findItem(R.id.action_logout);
        toggleMenu(SharedPrefUtils.isLogin(this));
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigator.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_exception:
                startActivity(new Intent(this, ExceptionActivity.class));
                return true;
            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.action_logout:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.unregister(this, mLoginStatusChangeReceiver);
        super.onDestroy();
    }

    @Override
    public void onBottomNavigatorViewItemClick(int position, View view) {
		if (position == -1) {
			Toast.makeText(this, "添加", Toast.LENGTH_SHORT).show();
		} else {
			setCurrentTab(position);
		}
        
    }

    private void logout(){
        SharedPrefUtils.logout(this);
        BroadcastManager.sendLogoutBroadcast(this, 1);
    }

    private void onUserLogin(int position) {
        if (position == -1) {
            resetAllTabsAndShow(mNavigator.getCurrentPosition());
        } else {
            resetAllTabsAndShow(position);
        }
        toggleMenu(true);
    }

    private void onUserLogout(int position) {
        if (position == -1) {
            resetAllTabsAndShow(mNavigator.getCurrentPosition());
        } else {
            resetAllTabsAndShow(position);
        }
        toggleMenu(false);
    }

    private void setCurrentTab(int position) {
        mNavigator.showFragment(position);
        bottomNavigatorView.select(position);
    }

    private void resetAllTabsAndShow(int position){
        mNavigator.resetFragments(position, true);
        bottomNavigatorView.select(position);
    }

    private void toggleMenu(boolean login) {
        if (login) {
            mLoginMenu.setVisible(false);
            mLogoutMenu.setVisible(true);
        } else {
            mLoginMenu.setVisible(true);
            mLogoutMenu.setVisible(false);
        }
    }

    private BroadcastReceiver mLoginStatusChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                int position = intent.getIntExtra("EXTRA_POSITION", -1);
                if (action.equals(Action.LOGIN)) {
                    onUserLogin(position);
                } else if (action.equals(Action.LOGOUT)) {
                    onUserLogout(position);
                }
            }
        }
    };
    
    
    
    private void addTestData() {
		if (null == mNoteDao) mNoteDao = new NoteDao(this);

		NoteEntity noteEntity = new NoteEntity();
		noteEntity.path = "/storage/emulated/0/aaa.txt";
		noteEntity.title = "test";
		
		long id = mNoteDao.insert(noteEntity);

		Logger.d("insert db id--->" + id);
		
	}
	
	private void queryTestData() {
		if (null == mNoteDao) mNoteDao = new NoteDao(this);
		
		List<NoteEntity> list = mNoteDao.queryAll();
		
		if (null != list && list.size() > 0) {
			for (NoteEntity noteEntity :
					list) {
				if (null != noteEntity) {
					Logger.i("id--->" + noteEntity.id);
					Logger.v("path--->" + noteEntity.path);
					Logger.w("title--->" + noteEntity.title);
				}
			}
			
			
		}
	}
}
