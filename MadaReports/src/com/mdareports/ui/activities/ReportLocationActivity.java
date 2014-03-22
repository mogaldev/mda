package com.mdareports.ui.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
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

import com.mdareports.R;
import com.mdareports.ui.custom.ArrayAdapterSearchView;

public class ReportLocationActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_report_location_map);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.report_location_activity_action_bar, menu);
		
		// initializing the search action view
		MenuItem item = menu.findItem(R.id.reportLocation_activity_menu_search);		
		final ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) MenuItemCompat
				.getActionView(item);
		
		searchView.setAdapter(new PlacesAutoCompleteAdapter(this,
				R.layout.autocomplete_map_item));	
		
		searchView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(view.getContext(),
						parent.getItemAtPosition(position).toString(),
						Toast.LENGTH_SHORT).show();
			}
		});

		return true;
	}

	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
			implements Filterable {

		private ArrayList<String> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		private ArrayList<String> getResults(String input) {
			ArrayList<String> results = new ArrayList<String>();

			final int MAX_RESULTS = 5;
			Geocoder geoCoder = new Geocoder(getContext(), /*
															 * Locale.getDefault(
															 * )
															 */new Locale("he"));
			try {
				List<Address> addresses = geoCoder.getFromLocationName(input,
						MAX_RESULTS);

				for (Address address : addresses) {
					String add = "";
					add = /* address.getAddressLine(0) + ";" + */address
							.getFeatureName();
					results.add(add);
				}

				// if (addresses.size() > 0) {
				//
				// map.addMarker(new MarkerOptions()
				// .position(
				// new LatLng(addresses.get(0).getLatitude(),
				// addresses.get(0).getLongitude()))
				// .title("YALIN")
				// .icon(BitmapDescriptorFactory
				// .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
				// // String coords = "geo:" +
				// // String.valueOf(addresses.get(0).getLatitude()) + "," +
				// // String.valueOf(addresses.get(0).getLongitude());
				// // Intent i = new
				// // Intent(android.content.Intent.ACTION_VIEW,
				// // Uri.parse(coords));
				// // startActivity(i);
				// }
			} catch (IOException e) {

				e.printStackTrace();
			}

			return results;
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						resultList = getResults(constraint.toString());

						Log.e("results:", "" + resultList.size());

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
