package com.example.myreadfdemo.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.example.myreadfdemo.R;
import com.example.myreadfdemo.broadcast.BroadcastManager;
import com.example.myreadfdemo.office.render.IBaseOfficeRender;
import com.example.myreadfdemo.office.render.RenderFactory;
import com.example.myreadfdemo.utils.NoteSaveHelper;
import com.xinyu.xylibrary.ui.fragment.BaseRxFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditNoteFragment extends BaseRxFragment {

    public static final String TAG = EditNoteFragment.class.getSimpleName();


	
	private EditText mEtTitle;
	private EditText mEtContent;
	
	private String mTitle;
	private String mContent;
	private long mId;
	
	private String mOfficeFilePath;
	private IBaseOfficeRender mOfficeRender;

	public static Fragment newInstance() {
		EditNoteFragment fragment = new EditNoteFragment();
		return fragment;
	}

	public EditNoteFragment() {
		// Required empty public constructor
	}

	// ==================================

	@Override
	protected int getLayoutRes() {
		return R.layout.fragment_edit_note;
	}

	@Override
	protected void getArgs() {
		Bundle args = getArguments();
		if (null != args) {
			mTitle = args.getString("title", "");
			mId = args.getLong("id", 0);
			mOfficeFilePath = args.getString("officeFilePath");
		}
	}

	@Override
	protected void findViews(View view) {
		mEtTitle = findViewById(R.id.fragment_edit_note_et_title);
		mEtContent = findViewById(R.id.fragment_edit_note_et_content);
	}

	@Override
	protected void initWidget() {
		
	}

	@Override
	protected void setListener() {
		
	}

	// =================================


	@Override
	protected void firstLoadDataBackground() {
		if (null != mOfficeFilePath && mOfficeFilePath.length() > 0) {
			mOfficeRender = RenderFactory.getRender(mOfficeFilePath);
		} else {
			mContent = NoteSaveHelper.getInstance().readNote(mId);
		}
	}

	@Override
	protected void bindData() {
		if (null == mOfficeRender) {
			mEtTitle.setText(mTitle);
			mEtContent.setText(mContent);
		} else {
			mOfficeRender.render(mEtContent);
			CharSequence content = mEtContent.getText();
			mEtTitle.setText(content.subSequence(0, Math.min(20, content.length())));
		}
	}

	@Override
	protected void showProgressBar(boolean show) {
		
	}

	@Override
	public void onStop() {
		super.onStop();
		save();

		BroadcastManager.sendRefreshNoteListBroadcast(mActivity);
	}
	
	private void save() {
		NoteSaveHelper.getInstance().saveNote(mId, mEtTitle.getText().toString(), mEtContent.getText().toString());
	}
	
}
