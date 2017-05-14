package com.example.myreadfdemo.ui.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myreadfdemo.R;
import com.example.myreadfdemo.database.dao.NoteDao;
import com.example.myreadfdemo.database.entity.NoteEntity;
import com.example.myreadfdemo.ui.bean.NoteListBean;
import com.xinyu.xylibrary.ui.fragment.BaseFragment;
import com.xinyu.xylibrary.ui.widget.EndlessRecyclerOnScrollListener;
import com.xinyu.xylibrary.ui.widget.RecycleViewDivider;
import com.xinyu.xylibrary.utils.DateUtils;

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
	
	private List<NoteListBean> mDatas = new ArrayList<>();
	private List<NoteEntity> mDbData;
	
	private NoteDao mDao;

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
	}

	@Override
	protected void initWidget() {
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		mRecyclerAdapter = new RecyclerAdapter();
		mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL));
		mRecyclerView.setAdapter(mRecyclerAdapter);

		mDao = new NoteDao(mActivity);
	}

	@Override
	protected void setListener() {
		mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
			@Override
			public void onLoadMore(int currentPage) {

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
		if (null != mDbData) {
			for (NoteEntity entity : mDbData) {
				if (null == entity) continue;
				
				NoteListBean bean = new NoteListBean();
				bean.title = entity.title;
				bean.date = DateUtils.yyyymmddFormat(entity.modifyAt);
				bean.summary = entity.summary;
				
				mDatas.add(bean);
			}
		}

		mRecyclerAdapter.notifyDataSetChanged();
	}

	@Override
	protected void showProgressBar(boolean show) {
		mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
	}


	class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
					mActivity).inflate(R.layout.item_note_list, parent,
					false));
			return holder;
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, int position) {
			NoteListBean bean = mDatas.get(position);
			if (null == bean) return;
			
			holder.tvTitle.setText(bean.title);
			holder.tvDate.setText(bean.date);
			holder.tvSummary.setText(bean.summary);
		}

		@Override
		public int getItemCount() {
			return mDatas.size();
		}

		class MyViewHolder extends RecyclerView.ViewHolder {
			TextView tvTitle;
			//ImageView ivPreview;
			TextView tvDate;
			TextView tvSummary;

			public MyViewHolder(View view) {
				super(view);
				tvTitle = (TextView) view.findViewById(R.id.item_note_tv_title);
				//ivPreview = (ImageView) view.findViewById(R.id.item_note_iv_preview);
				tvDate = (TextView) view.findViewById(R.id.item_note_tv_date);
				tvSummary = (TextView) view.findViewById(R.id.item_note_tv_summary);
			}
		}
	}
}
