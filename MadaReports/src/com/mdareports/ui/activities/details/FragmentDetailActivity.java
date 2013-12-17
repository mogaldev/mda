package com.mdareports.ui.activities.details;

import android.support.v4.app.Fragment;

import com.mdareports.db.models.Report;

/**
 * Abstract class that the fragment that suppose to be tabs in the
 * {@link DetailsActivity} should extend from if</br> 
 */
public abstract class FragmentDetailActivity extends Fragment {

	@Override
	public void onPause() {
		super.onPause();
		// call saveCurrentReport so the data about the report from the fragment will not destroy
		saveCurrentReport();
	}

	/**
	 * Call the current report of the DetailActivity
	 * @return The Report of the DetailActivity
	 */
	protected Report getCurrentReport() {
		DetailsActivity activity = (DetailsActivity) getActivity();
		return activity.getCurrentReport();
	}

	/**
	 * Called in onPause and when the user click on the save button in the {@link DetailsActivity}
	 */
	public abstract void saveCurrentReport();

	/**
	 * Called when the user presses on the refresh button in the {@link DetailsActivity}
	 */
	public abstract void refreshDataWithCurrentReport();
}
