package com.mdareports.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.google.analytics.tracking.android.EasyTracker;
import com.mdareports.R;

public class OldSettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_settings);
	}
	
	@Override
	protected void onStart() {		
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	@Override
	protected void onStop() {	
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	
		



}