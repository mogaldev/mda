package com.madareports.ui.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import com.madareports.R;
import com.madareports.ui.fragments.SettingsFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Display the fragment instead of the FrameLayout in the
		// activity_settings layout
		getFragmentManager().beginTransaction()
				.replace(R.id.settings_frame_layout, new SettingsFragment())
				.commit();
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
}