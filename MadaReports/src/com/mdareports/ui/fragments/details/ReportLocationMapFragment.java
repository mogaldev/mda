package com.mdareports.ui.fragments.details;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mdareports.R;

public class ReportLocationMapFragment extends BaseDetailFragment {
	private GoogleMap map;
	private Marker currentMarker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_report_location_map,
				container, false);

		map = ((SupportMapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		AutoCompleteTextView autoCompView = (AutoCompleteTextView) rootView
				.findViewById(R.id.from);
		autoCompView.setThreshold(1); // minimum 1 character
		autoCompView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(),
				R.layout.autocomplete_map_item));
		autoCompView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				String str = (String) adapterView.getItemAtPosition(position);
				Toast.makeText(view.getContext(), str, Toast.LENGTH_SHORT)
						.show();
			}
		});

		// autoCompleteTv.setAdapter(new
		// PlacesAutoCompleteAdapter(getActivity(),
		// android.R.layout.simple_dropdown_item_1line));

		if (map != null) {

			map.setMyLocationEnabled(true);

			map.setOnMapLongClickListener(new OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng point) {

					// put marker
					putReportLocationMarker(point);
				}
			});
		}
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		refreshDataWithCurrentReport();

		myGetLocationTest();

	}

	@Override
	public void saveCurrentReport() {
		getCurrentReport().setLocation(currentMarker.getPosition());
	}

	@Override
	public void refreshDataWithCurrentReport() {
		putReportLocationMarker(getCurrentReport().getLocation());
		if (currentMarker != null) {
			// zoom the camera into the marker location
			zoomIntoLocation(currentMarker.getPosition());
		}
	}

	private void myGetLocationTest() {
		Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocationName(
					"ילין פתח תקווה", 5);
			String add = "";
			if (addresses.size() > 0) {

				map.addMarker(new MarkerOptions()
						.position(
								new LatLng(addresses.get(0).getLatitude(),
										addresses.get(0).getLongitude()))
						.title("YALIN")
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
				// String coords = "geo:" +
				// String.valueOf(addresses.get(0).getLatitude()) + "," +
				// String.valueOf(addresses.get(0).getLongitude());
				// Intent i = new
				// Intent(android.content.Intent.ACTION_VIEW,
				// Uri.parse(coords));
				// startActivity(i);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void putReportLocationMarker(LatLng location) {

		// clear previous marker if exists
		if (currentMarker != null) {
			currentMarker.remove();
		}

		// add the new marker with the inputed location
		currentMarker = map.addMarker(new MarkerOptions().position(location)
				.title("You are here")
				// TODO: change title
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	}

	private void zoomIntoLocation(LatLng location) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomIn());
		// Zoom out to zoom level 10, animating with a duration of 2 seconds.
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
	}

	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
			implements Filterable {
		private ArrayList<String> resultList;
		private static final String LOG_TAG = "ExampleApp";

		private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
		private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
		private static final String OUT_JSON = "/json";

		private static final String API_KEY = "AIzaSyAduPo4dXLOVPgySvD-2yHqal8nF2KsTbc"; // TODO:
																							// put
																							// in
																							// strings
																							// xml
																							// file

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		private ArrayList<String> autocomplete(String input) {
			ArrayList<String> resultList = null;

			HttpURLConnection conn = null;
			StringBuilder jsonResults = new StringBuilder();
			try {
				StringBuilder sb = new StringBuilder(PLACES_API_BASE
						+ TYPE_AUTOCOMPLETE + OUT_JSON);
				sb.append("?sensor=false&key=" + API_KEY);
				sb.append("&components=country:uk");
				sb.append("&input=" + URLEncoder.encode(input, "utf8"));

				URL url = new URL(sb.toString());
				conn = (HttpURLConnection) url.openConnection();
				InputStreamReader in = new InputStreamReader(
						conn.getInputStream());

				// Load the results into a StringBuilder
				int read;
				char[] buff = new char[1024];
				while ((read = in.read(buff)) != -1) {
					jsonResults.append(buff, 0, read);
				}
			} catch (MalformedURLException e) {
				Log.e(LOG_TAG, "Error processing Places API URL", e);
				return resultList;
			} catch (IOException e) {
				Log.e(LOG_TAG, "Error connecting to Places API", e);
				return resultList;
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}

			try {
				// Create a JSON object hierarchy from the results
				JSONObject jsonObj = new JSONObject(jsonResults.toString());
				JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

				Log.e("JSON", jsonObj.toString());

				// Extract the Place descriptions from the results
				resultList = new ArrayList<String>(predsJsonArray.length());
				for (int i = 0; i < predsJsonArray.length(); i++) {
					resultList.add(predsJsonArray.getJSONObject(i).getString(
							"description"));
				}
			} catch (JSONException e) {
				Log.e(LOG_TAG, "Cannot process JSON results", e);
			}

			return resultList;
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
			Geocoder geoCoder = new Geocoder(getActivity(), /*Locale.getDefault()*/new Locale("he"));
			try {
				List<Address> addresses = geoCoder.getFromLocationName(input, MAX_RESULTS);
						
				for (Address address : addresses) {
					String add = "";
					add = /*address.getAddressLine(0) + ";" +*/ address.getFeatureName();
					results.add(add);
				}
				
//				if (addresses.size() > 0) {
//					
//					map.addMarker(new MarkerOptions()
//							.position(
//									new LatLng(addresses.get(0).getLatitude(),
//											addresses.get(0).getLongitude()))
//							.title("YALIN")
//							.icon(BitmapDescriptorFactory
//									.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//					// String coords = "geo:" +
//					// String.valueOf(addresses.get(0).getLatitude()) + "," +
//					// String.valueOf(addresses.get(0).getLongitude());
//					// Intent i = new
//					// Intent(android.content.Intent.ACTION_VIEW,
//					// Uri.parse(coords));
//					// startActivity(i);
//				}
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
						// Retrieve the autocomplete results.
						//resultList = autocomplete(constraint.toString());
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

	
	
	@Override
	public int getTabTitleResourceId() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
