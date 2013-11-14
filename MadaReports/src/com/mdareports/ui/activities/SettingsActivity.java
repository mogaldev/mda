package com.mdareports.ui.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.view.MenuItem;
import com.mdareports.R;
import com.mdareports.ui.fragments.SettingsFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Set the home button in the action bar as enabled
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// Display the fragment instead of the FrameLayout in the
		// activity_settings layout
		getFragmentManager().beginTransaction()
				.replace(R.id.settings_frame_layout, new SettingsFragment())
				.commit();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
