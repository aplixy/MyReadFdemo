package com.example.myreadfdemo.ui.adapter;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.example.myreadfdemo.ui.fragment.ContactsFragment;
import com.example.myreadfdemo.ui.fragment.MainFragment;

/**
 * Created by aspsine on 16/3/31.
 */
public class FragmentAdapter implements FragmentNavigatorAdapter {

    private static final String TABS[] = {"Recent", "Note", "Share", "Me"};

    @Override
    public Fragment onCreateFragment(int position) {
        if (position == 0 || position == 2){
            return ContactsFragment.newInstance(position);
        }
        return MainFragment.newInstance(TABS[position]);
    }

    @Override
    public String getTag(int position) {
        if (position == 1) {
            return ContactsFragment.TAG;
        }
        return MainFragment.TAG + TABS[position];
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
