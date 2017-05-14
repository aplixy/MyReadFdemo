package com.example.myreadfdemo.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myreadfdemo.Action;
import com.example.myreadfdemo.R;
import com.example.myreadfdemo.broadcast.BroadcastManager;
import com.example.myreadfdemo.database.dao.NoteDao;
import com.example.myreadfdemo.database.entity.NoteEntity;
import com.example.myreadfdemo.ui.activity.EditNoteActivity;
import com.xinyu.xylibrary.ui.fragment.BaseFragment;
import com.xinyu.xylibrary.ui.widget.EndlessRecyclerOnScrollListener;
import com.xinyu.xylibrary.ui.widget.RecycleViewDivider;
import com.xinyu.xylibrary.utils.DateUtils;
import com.xinyu.xylibrary.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends BaseFragment {

	public static final String TAG = NoteListFragment.class.getSimpleName();

	private RecyclerView mRecyclerView;
	private RecyclerAdapter mRecyclerAdapter;
	
	private ProgressBar mProgressBar;
	
	private List<NoteEntity> mDatas = new ArrayList<>();
	private List<NoteEntity> mDbData;
	
	private NoteDao mDao;
	
	private RefreshListReceiver mRefreshReceiver;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;

	public static Fragment newInstance() {
		NoteListFragment fragment = new NoteListFragment();
		return fragment;
	}

	public NoteListFragment() {
		// Required empty public constructor
	}

	// ==================================

	@Override
	protected int getLayoutRes() {
		return R.layout.fragment_main;
	}

	@Override
	protected void getArgs() {

	}

	@Override
	protected void findViews(View view) {
		mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_note_list_recyclerview);
		mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_note_list_progressbar);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_note_list_swiperefreshlayout);
	}

	@Override
	protected void initWidget() {
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		mRecyclerAdapter = new RecyclerAdapter();
		mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL));
		mRecyclerView.setAdapter(mRecyclerAdapter);

		mDao = new NoteDao(mActivity);

		mRefreshReceiver = new RefreshListReceiver();
		BroadcastManager.register(mActivity, mRefreshReceiver, Action.REFRESH_NOTE_LIST);
	}

	@Override
	protected void setListener() {
		mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
			@Override
			public void onLoadMore(int currentPage) {

			}
		});

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadData();
			}
		});
	}

	// =================================


	@Override
	protected void firstLoadData() {
		mDbData = mDao.queryAll();
	}

	@Override
	protected void bindData() {
		mSwipeRefreshLayout.setRefreshing(false);
		if (null != mDbData) {
			mDatas.clear();
			mDatas.addAll(mDbData);
		}

		mRecyclerAdapter.notifyDataSetChanged();
	}

	@Override
	protected void showProgressBar(boolean show) {
		mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mRefreshReceiver) BroadcastManager.unregister(mActivity, mRefreshReceiver);
	}

	class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements View.OnClickListener {

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
					mActivity).inflate(R.layout.item_note_list, parent,
					false));
			return holder;
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, int position) {
			NoteEntity entity = mDatas.get(position);
			if (null == entity) return;
			
			holder.tvTitle.setText(entity.title);
			holder.tvDate.setText(DateUtils.yyyymmddFormat(entity.modifyAt));
			holder.tvSummary.setText(entity.summary);
			
			holder.rootView.setTag(position);
		}

		@Override
		public int getItemCount() {
			return mDatas.size();
		}

		@Override
		public void onClick(View v) {
			int id = v.getId();
			
			if (id == R.id.item_note_root) {
				int position = (int) v.getTag();
				
				NoteEntity entity = mDatas.get(position);
				long noteId = entity == null ? 0 :entity.id;
				

				EditNoteActivity.start(mActivity, entity.title, noteId);
			}
		}

		class MyViewHolder extends RecyclerView.ViewHolder {
			View rootView;
			
			TextView tvTitle;
			//ImageView ivPreview;
			TextView tvDate;
			TextView tvSummary;

			public MyViewHolder(View view) {
				super(view);
				rootView = view.findViewById(R.id.item_note_root);
				tvTitle = (TextView) view.findViewById(R.id.item_note_tv_title);
				//ivPreview = (ImageView) view.findViewById(R.id.item_note_iv_preview);
				tvDate = (TextView) view.findViewById(R.id.item_note_tv_date);
				tvSummary = (TextView) view.findViewById(R.id.item_note_tv_summary);

				rootView.setOnClickListener(RecyclerAdapter.this);
			}
		}
	}
	
	private class RefreshListReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Logger.i("onReceive");
			loadData();
		}
	}
}
