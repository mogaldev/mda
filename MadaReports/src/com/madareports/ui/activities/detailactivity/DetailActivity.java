package com.madareports.ui.activities.detailactivity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
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
		techInfoTab.setTabListener(new TabListener<GeneralInfoFragment>(
				this, GeneralInfoFragment.class.getName(),
				GeneralInfoFragment.class));
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
		inflater.inflate(R.menu.main_action_bar, menu);

		return true;
	}

}