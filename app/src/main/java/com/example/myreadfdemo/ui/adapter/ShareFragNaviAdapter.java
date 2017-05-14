package com.example.myreadfdemo.ui.adapter;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.example.myreadfdemo.ui.fragment.NoteListFragment;
import com.example.myreadfdemo.ui.fragment.TaskListFragment;

/**
 * Created by aspsine on 16/4/3.
 */
public class ShareFragNaviAdapter implements FragmentNavigatorAdapter {

    public static final String[] TABS = {"NormalNote", "TaskNote"};
	

    @Override
    public Fragment onCreateFragment(int position) {
		if (position == 0) {
			return NoteListFragment.newInstance();
		} else {
			return TaskListFragment.newInstance("share");
		}
    }

    @Override
    public String getTag(int position) {
        return "Share" + TABS[position];
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
