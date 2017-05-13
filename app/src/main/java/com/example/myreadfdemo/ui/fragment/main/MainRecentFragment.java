package com.example.myreadfdemo.ui.fragment.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.example.myreadfdemo.R;
import com.example.myreadfdemo.ui.adapter.RecentFragNaviAdapter;
import com.example.myreadfdemo.ui.widget.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainRecentFragment extends Fragment {

    public static final String TAG = MainRecentFragment.class.getSimpleName();
	

    private FragmentNavigator mNavigator;

    private TabLayout tabLayout;
	

    public MainRecentFragment() {
        // Required empty public constructor
    }

	public static Fragment newInstance() {
		Fragment fragment = new MainRecentFragment();
		Bundle args = new Bundle();
		//args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigator = new FragmentNavigator(getChildFragmentManager(), new RecentFragNaviAdapter(), R.id.childContainer);
        mNavigator.setDefaultPosition(0);
        mNavigator.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setOnTabItemClickListener(new TabLayout.OnTabItemClickListener() {
            @Override
            public void onTabItemClick(int position, View view) {
                setCurrentTab(position);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCurrentTab(mNavigator.getCurrentPosition());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigator.onSaveInstanceState(outState);
    }

    public void setCurrentTab(int position) {
        mNavigator.showFragment(position);
        tabLayout.select(position);
    }


    
}
