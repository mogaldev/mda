package com.madareports.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.madareports.R;

public class OldSettingsActivity extends PreferenceActivity {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_settings);

	}
}