package com.madareports.ui.activities.detailactivity;

import com.actionbarsherlock.app.SherlockFragment;
import com.madareports.db.models.Report;

public abstract class FragmentDetailActivity extends SherlockFragment {

	private Report currentReport;

	@Override
	public void onPause() {
	    super.onPause();
	    postChangesOnCurrentReport();
	}
	
	protected Report getCurrentReport() {
		if (currentReport == null) {
			DetailActivity activity = (DetailActivity) getActivity();
			currentReport = activity.getCurrentReport();
		}

		return currentReport;
	}
	
	public abstract void postChangesOnCurrentReport();
	public abstract void refreshDataWithCurrentReport();
}
