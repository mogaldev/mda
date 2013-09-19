package com.mdareports.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.mdareports.R;

public class OldSettingsActivity extends PreferenceActivity {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_settings);

	}
}