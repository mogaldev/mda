package com.mdareports.ui.activities;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.mdareports.ui.custom.CustomAdapterSearchView;
import com.mdareports.ui.fragments.details.ReportLocationMapFragment;
import com.mdareports.utils.DeviceInfoUtils;
import com.mdareports.utils.GeocodingUtils;
import com.mdareports.utils.Logger;

public class ReportLocationActivity extends BaseActivity {
	private CustomAdapterSearchView searchView;
	private ReportLocationMapFragment reportMapFragment;
	int currentReportId;

	public static final String REPORT_ID_EXTRA = "REPORT_ID_EXTRA";

	/**
	 * For the voice search handling
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			searchView.setText(query);

			// TODO: remove
			Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
			Logger.LOGE("handleIntent", "action search: " + query);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		onNewIntent(getIntent());
	}

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.report_location, menu);

		// initializing the search action view
		MenuItem item = menu.findItem(R.id.reportLocation_activity_menu_search);

		searchView = (CustomAdapterSearchView) MenuItemCompat
				.getActionView(item);

		searchView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Address address = ((PlacesAutoCompleteAdapter) parent
						.getAdapter()).getAddressItem(position);
				reportMapFragment.zoomIntoLocation(new LatLng(address
						.getLatitude(), address.getLongitude()));
			}
		});

		if (DeviceInfoUtils.hasHoneycomb()) {
			if (searchView != null) {
				searchView
						.setSearchableInfo(((SearchManager) getSystemService(SEARCH_SERVICE))
								.getSearchableInfo(getComponentName()));
				searchView.setQueryRefinementEnabled(true);
			}
		}

		searchView.setAdapter(new PlacesAutoCompleteAdapter(this,
				android.R.layout.simple_dropdown_item_1line));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
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
			return true;

		case R.id.reportLocation_activity_menu_search:
			if (!DeviceInfoUtils.hasHoneycomb()) {
				startSearch(null, false, Bundle.EMPTY, false);
			}
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
			report.setAddress(getAddressToDisplay(address));
		}

		// update the database
		db.updateReport(report);
	}

	private String getAddressToDisplay(Address address) {
		
		// check if the feature name is numeric
		if (address.getFeatureName().replace('-', '0').matches("-?\\d+(\\.\\d+)?")) {

			return address.getLocality() + "," + address.getThoroughfare();
		} else {
			return address.getFeatureName();
		}

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
			if (resultList == null)
				return 0;
			else
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
