package com.madareports.ui.activities.detailactivity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.models.Report;
import com.madareports.ui.activities.BaseActivity;

public class DetailActivity extends BaseActivity {

	public static final String REPORT_ID_EXTRA = "REPORT_ID_EXTRA";
	private Report sentReport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sentReport = getReportFromIntent();

		ActionBar supportActionBar = getSupportActionBar();

		supportActionBar.setDisplayHomeAsUpEnabled(true);
		supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab generalInfotab = supportActionBar.newTab();
		generalInfotab.setText(getString(R.string.general_info));
		generalInfotab.setTabListener(new TabListener<GeneralInfoFragment>(
				this, GeneralInfoFragment.class.getName(),
				GeneralInfoFragment.class));
		supportActionBar.addTab(generalInfotab);

		ActionBar.Tab techInfoTab = supportActionBar.newTab();
		techInfoTab.setText(getString(R.string.tech_info));
		techInfoTab.setTabListener(new TabListener<TechInfoFragment>(
				this, TechInfoFragment.class.getName(),
				TechInfoFragment.class));
		supportActionBar.addTab(techInfoTab);

		ActionBar.Tab treatmentsTab = supportActionBar.newTab();
		treatmentsTab.setText(getString(R.string.treatments));
		treatmentsTab.setTabListener(new TabListener<GeneralInfoFragment>(
				this, GeneralInfoFragment.class.getName(),
				GeneralInfoFragment.class));
		supportActionBar.addTab(treatmentsTab);
	}

	private Report getReportFromIntent() {
		int reportId = getIntent().getExtras().getInt(REPORT_ID_EXTRA);
		DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(this);
		Report reportById = databaseWrapper.getReportById(reportId);

		return reportById;
	}

	public Report getCurrentReport() {
		return sentReport;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.detail_activity_action_bar, menu);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.menu_save:
				saveCurrentReport();
				DatabaseWrapper.getInstance(this).updateReport(getCurrentReport());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void saveCurrentReport() {
		// try to get each fragment because not all the fragment were loaded
		try {
			GeneralInfoFragment generalInfoFragment = (GeneralInfoFragment) getSupportFragmentManager().findFragmentByTag(GeneralInfoFragment.class.getName());
			generalInfoFragment.save();
		} catch (Exception e) {}
		try {
			TechInfoFragment techInfoFragment = (TechInfoFragment) getSupportFragmentManager().findFragmentByTag(TechInfoFragment.class.getName());
			techInfoFragment.save();
		} catch (Exception e) {}
	}

}