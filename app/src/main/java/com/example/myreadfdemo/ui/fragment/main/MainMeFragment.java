package com.example.myreadfdemo.ui.fragment.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myreadfdemo.R;
import com.xinyu.xylibrary.utils.SharedPrefUtils;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainMeFragment extends Fragment {

    public static final String TAG = MainMeFragment.class.getSimpleName();

    public static final String EXTRA_TEXT = "extra_text";

    private static final int MOCK_LOAD_DATA_DELAYED_TIME = 2000;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private WeakRunnable mRunnable = new WeakRunnable(this);

    private String mText;

    //private TextView tvText;

    private ProgressBar progressBar;

    public static Fragment newInstance() {
        MainMeFragment fragment = new MainMeFragment();
        Bundle bundle = new Bundle();
        //bundle.putString(EXTRA_TEXT, text);
        fragment.setArguments(bundle);
        return fragment;
    }

    public MainMeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mText = getArguments().getString(EXTRA_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //tvText = (TextView) view.findViewById(R.id.tvText);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_note_list_progressbar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            loadData();
        } else {
            mText = savedInstanceState.getString(EXTRA_TEXT);
            bindData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_TEXT, mText);
    }

    @Override
    public void onDestroyView() {
        sHandler.removeCallbacks(mRunnable);
        //tvText = null;
        progressBar = null;
        super.onDestroyView();
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void bindData() {
        boolean isLogin = SharedPrefUtils.isLogin(getActivity());
        //tvText.setText(mText + "\n" + "Login:" + isLogin);
    }

    /**
     * mock load date
     */
    private void loadData() {
        showProgressBar(true);
        sHandler.postDelayed(mRunnable, MOCK_LOAD_DATA_DELAYED_TIME);
    }

    private static class WeakRunnable implements Runnable {

        WeakReference<MainMeFragment> mMainFragmentReference;

        public WeakRunnable(MainMeFragment mainFragment) {
            this.mMainFragmentReference = new WeakReference<MainMeFragment>(mainFragment);
        }

        @Override
        public void run() {
            MainMeFragment mainFragment = mMainFragmentReference.get();
            if (mainFragment != null && !mainFragment.isDetached()) {
                mainFragment.showProgressBar(false);
                mainFragment.bindData();
            }
        }
    }
}
