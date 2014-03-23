package com.mdareports.ui.activities;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.db.models.Report;
import com.mdareports.ui.custom.ArrayAdapterSearchView;
import com.mdareports.ui.fragments.details.ReportLocationMapFragment;
import com.mdareports.utils.GeocodingUtils;
import com.mdareports.utils.Logger;

public class ReportLocationActivity extends BaseActivity {
	private ReportLocationMapFragment reportMapFragment;
	int currentReportId;

	public static final String REPORT_ID_EXTRA = "REPORT_ID_EXTRA";

//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		final String queryAction = intent.getAction();
//		if (Intent.ACTION_SEARCH.equals(queryAction)) {
//			this.handleIntent(intent);
//		} else if (Intent.ACTION_VIEW.equals(queryAction)) {
//			this.doView(intent);
//		}
//	}
//
//	public void doView(Intent intent) {
//		Logger.LOGE("action view", " suggestion ");
//
//	}
//
//	private void handleIntent(Intent intent) {
//		Toast.makeText(this, "Inside handleIntent", Toast.LENGTH_SHORT).show();
//		Logger.LOGE("handleIntent", "HERE");
//		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//			String query = intent.getStringExtra(SearchManager.QUERY);
//			// doSearch(query);
//			Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
//			Logger.LOGE("handleIntent", "action search: " + query);
//		}
//
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_location_map);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		currentReportId = getIntent().getExtras().getInt(REPORT_ID_EXTRA);

		reportMapFragment = (ReportLocationMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.reportLocationMap);

		displayReportLocationOnMap();

		//handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.report_location, menu);

		// initializing the search action view
		MenuItem item = menu.findItem(R.id.reportLocation_activity_menu_search);
		final ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) MenuItemCompat
				.getActionView(item);

		searchView.setAdapter(new PlacesAutoCompleteAdapter(this,
				android.R.layout.simple_list_item_1));


		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.reportLocation_activity_menu_save:
			saveReportLocation();

			// Make Toast to the user
			Toast.makeText(this,
					getString(R.string.detail_activity_report_saved),
					Toast.LENGTH_SHORT).show();
			return true;

		case R.id.reportLocation_activity_menu_sync:
			 displayReportLocationOnMap();
			//startVoiceRecognitionActivity();
			return true;

		case R.id.reportLocation_activity_menu_delete:
			reportMapFragment.removeMarker();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void displayReportLocationOnMap() {
		DatabaseWrapper db = DatabaseWrapper.getInstance(this);
		Report report = db.getReportById(currentReportId);

		reportMapFragment.markerReportLocation(report.getLocation());
		// reportMapFragment.zoomIntoLocation(report.getLocation());
		reportMapFragment.zoomIntoCurrentLocation();
	}

	private void saveReportLocation() {
		DatabaseWrapper db = DatabaseWrapper.getInstance(this);
		Report report = db.getReportById(currentReportId);

		// set the location information
		report.setLocation(reportMapFragment.getCurrentLocation());
		LatLng location = reportMapFragment.getCurrentLocation();

		Address address = GeocodingUtils.getSingleAddreesByLocation(this,
				location, false);
		if (address != null) {
			report.setAddress(address.getFeatureName());
		}

		// update the database
		db.updateReport(report);
	}

	private static final int REQUEST_CODE = 1234;

	/**
	 * Called with the activity is first created.
	 */

	private boolean hasVoiceRecognition() {
		return (getPackageManager().queryIntentActivities(
				new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0).size() > 0);
	}

	/**
	 * Fire an intent to start the voice recognition activity.
	 */
	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"Voice recognition Demo...");
		startActivityForResult(intent, REQUEST_CODE);
	}

	/**
	 * Handle the results from the voice recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			// Populate the wordsList with the String values the recognition
			// engine thought it heard
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			String temp = "";
			for (String s : matches) {
				temp += s + "\n";
			}
			Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/************************
	 * Auto Complete Adapter
	 ************************/
	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
			implements Filterable {

		final int MAX_RESULTS = 5;
		private ArrayList<Address> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index).getFeatureName();
		}

		public Address getAddressItem(int index) {
			return resultList.get(index);
		}

		private void fetchResults(String input) {
			resultList = (ArrayList<Address>) GeocodingUtils.getAddresses(
					getContext(), input, false, MAX_RESULTS);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {

						fetchResults(constraint.toString());

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

}
