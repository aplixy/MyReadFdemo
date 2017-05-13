package com.example.myreadfdemo.ui.adapter;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.example.myreadfdemo.ui.fragment.main.MainMeFragment;
import com.example.myreadfdemo.ui.fragment.main.MainNoteBookFragment;
import com.example.myreadfdemo.ui.fragment.main.MainRecentFragment;
import com.example.myreadfdemo.ui.fragment.main.MainShareFragment;

/**
 * Created by aspsine on 16/3/31.
 */
public class FragmentAdapter implements FragmentNavigatorAdapter {

    private static final String TABS[] = {"Recent", "Note", "Share", "Me"};
	
	private Fragment[] fragments = {
			MainRecentFragment.newInstance(),
			MainNoteBookFragment.newInstance(),
			MainShareFragment.newInstance(),
			MainMeFragment.newInstance(),
	};

    @Override
    public Fragment onCreateFragment(int position) {
        return fragments[position];
    }

    @Override
    public String getTag(int position) {
        return "Main" + TABS[position];
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
