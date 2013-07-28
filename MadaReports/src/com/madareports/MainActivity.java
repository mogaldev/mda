package com.madareports;

import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.madareports.ui.activities.BaseActivity;
import com.madareports.utils.Logger;

//TODO: DB aspect - add isViewed for each report to indicate whether the report has been seen
public class MainActivity extends BaseActivity {
	private final String TAG = Logger.makeLogTag(getClass());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_action_bar, menu);
		
		menu.addSubMenu("gal madar");
		
		return true;
	}

}
