package com.madareports.ui.activities.detailactivity;

import com.actionbarsherlock.app.SherlockFragment;
import com.madareports.db.models.Report;

public abstract class FragmentDetailActivity extends SherlockFragment {

	@Override
	public void onPause() {
		super.onPause();
		saveCurrentReport();
	}

	protected Report getCurrentReport() {
		DetailActivity activity = (DetailActivity) getActivity();
		return activity.getCurrentReport();
	}

	public abstract void saveCurrentReport();

	public abstract void refreshDataWithCurrentReport();
}
