package com.madareports.ui.activities.details;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.models.Report;
import com.madareports.ui.activities.BaseActivity;
import com.madareports.utils.Logger;

public class DetailsActivity extends BaseActivity {

	private final String TAG = Logger.makeLogTag(this.getClass());
	public static final String REPORT_ID_EXTRA = "REPORT_ID_EXTRA";	
	// Instance of the report of this DetailActivity
	private Report sentReport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//  Get the sent report from the intent and set watched to the report
		sentReport = getReportFromIntent();
		
		// Set watched on the report and update it
		sentReport.setRead(true);
		DatabaseWrapper.getInstance(this).updateReport(sentReport);

		// Get the action bar and set it up
		ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		
		// Set navigation mode
		supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab treatmentsTab = supportActionBar.newTab();
		treatmentsTab.setText(getString(R.string.treatments));
		treatmentsTab.setTabListener(new TabListener<TreatmentsFragment>(
				this, TreatmentsFragment.class.getName(),
				TreatmentsFragment.class));
		supportActionBar.addTab(treatmentsTab);

		ActionBar.Tab techInfoTab = supportActionBar.newTab();
		techInfoTab.setText(getString(R.string.tech_info));
		techInfoTab.setTabListener(new TabListener<TechInfoFragment>(
				this, TechInfoFragment.class.getName(),
				TechInfoFragment.class));
		supportActionBar.addTab(techInfoTab);

		// Init all the tabs in the DetailActivity
		ActionBar.Tab generalInfotab = supportActionBar.newTab();
		generalInfotab.setText(getString(R.string.general_info));
		generalInfotab.setTabListener(new TabListener<GeneralInfoFragment>(
				this, GeneralInfoFragment.class.getName(),
				GeneralInfoFragment.class));
		supportActionBar.addTab(generalInfotab, true);
	}

	/**
	 * Get the id of the report that was sent from the intent.
	 * then find this report in the DB
	 * @return Report object that was find from the DB
	 */
	private Report getReportFromIntent() {
		int id = getIntent().getExtras().getInt(REPORT_ID_EXTRA);
		DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(this);
		Report reportById = databaseWrapper.getReportById(id);

		return reportById;
	}
	
	/**
	 * Getter to the instance of the report in this activity
	 * @return The report of this {@link DetailsActivity}
	 */
	public Report getCurrentReport() {
		return sentReport;
	}
	
	/**
	 * Setter to the instance of the report in this activity. <br>
	 * Only the {@link DetailsActivity} can use this setter
	 * @param report
	 */
	private void setCurrentReport(Report report) {
		sentReport = report;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.details_activity_action_bar, menu);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.detail_activity_menu_save:
				// Save the data of the report from all the tab's fragments to the instance of the currentReport of this activity
				saveCurrentReport();
				
				// Update the saved currentReport in the DataBase
				DatabaseWrapper.getInstance(this).updateReport(getCurrentReport());
				
				// Make Toast to the user
				Toast.makeText(this , getString(R.string.report_saved), Toast.LENGTH_SHORT).show();
				return true;
			case R.id.detail_activity_menu_sync:
				// Get the unchanged Report from the DataBase
				Report currentReportFromDataBase = DatabaseWrapper.getInstance(this).getReportById(getCurrentReport().getId());
				
				// Set the found Report from the DataBase as the currentReport 
				setCurrentReport(currentReportFromDataBase);
				
				// Refresh all the tabs in the activity
				rollbackCurrentReport();
				return true;
			case R.id.detail_activity_menu_delete:
				DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(this);
				if (DatabaseWrapper.getInstance(this).deleteReport(getCurrentReport()))
				{
					databaseWrapper.deleteTreatmentsToReportByReportId(getCurrentReport().getId());
					writeShortTimeMessage(R.string.deleted_successfuly);					
					finish();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Get all the information from all the fragments in this {@link DetailsActivity} by calling the method: "saveCurrentReport" in each fragment</br>
	 * !!IMPORTANT: When adding a tab that use the currentReport of this activity, </br></br>
	 * please extend from the {@link FragmentDetailActivity} abstract class. 
	 */
	public void saveCurrentReport() {
		// try to get each fragment because maybe not all the fragment were loaded
		try {
			GeneralInfoFragment generalInfoFragment = (GeneralInfoFragment) getSupportFragmentManager().findFragmentByTag(GeneralInfoFragment.class.getName());
			generalInfoFragment.saveCurrentReport();
		} catch (Exception e) {}
		try {
			TechInfoFragment techInfoFragment = (TechInfoFragment) getSupportFragmentManager().findFragmentByTag(TechInfoFragment.class.getName());
			techInfoFragment.saveCurrentReport();
		} catch (Exception e) {}
		try {
			TreatmentsFragment treatmentFragment = (TreatmentsFragment) getSupportFragmentManager().findFragmentByTag(TreatmentsFragment.class.getName());
			treatmentFragment.saveCurrentReport();
			treatmentFragment.saveTreatments();
		} catch (Exception e) {}
	}
	
	/**
	 * Rollback the data in the fragments in this DetailActivity.</br></br>
	 * !!IMPORTANT: When adding a tab that use the currentReport of this activity, </br>
	 * please extend from the {@link FragmentDetailActivity} abstract class. 
	 */
	public void rollbackCurrentReport() {
		// try to get each fragment because maybe not all the fragment were
		// loaded
		try {
			GeneralInfoFragment generalInfoFragment = (GeneralInfoFragment) getSupportFragmentManager().findFragmentByTag(GeneralInfoFragment.class.getName());
			generalInfoFragment.refreshDataWithCurrentReport();
		} catch (Exception e) {
			Logger.LOGW(TAG, "The Fragment " + GeneralInfoFragment.class.getName() +
			                 " was not created");
		}
		try {
			TechInfoFragment techInfoFragment = (TechInfoFragment) getSupportFragmentManager().findFragmentByTag(TechInfoFragment.class.getName());
			techInfoFragment.refreshDataWithCurrentReport();
		} catch (Exception e) {
			Logger.LOGW(TAG, "The Fragment " + TechInfoFragment.class.getName() +
			                 " was not created");
		}
		try {
			TreatmentsFragment treatmentFragment = (TreatmentsFragment) getSupportFragmentManager().findFragmentByTag(TreatmentsFragment.class.getName());
			treatmentFragment.refreshDataWithCurrentReport();
		} catch (Exception e) {
			Logger.LOGW(TAG, "The Fragment " + TreatmentsFragment.class.getName() +
			                 " was not created");
		}
	}
	
	
}