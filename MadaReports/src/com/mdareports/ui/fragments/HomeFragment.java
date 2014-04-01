package com.mdareports.ui.fragments;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.google.android.gms.maps.GoogleMap;
import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;

public class HomeFragment extends BaseFragment {
	GoogleMap map;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		changeTitle(R.string.app_name);
		
		final View rootView = inflater.inflate(R.layout.fragment_home,
				container, false);

		// Acquire a reference to the system Location Manager
//		LocationManager locationManager = (LocationManager) getActivity()
//				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		// LocationListener locationListener = new LocationListener() {
		// public void onLocationChanged(Location location) {
		// // Called when a new location is found by the network location
		// provider.
		// //makeUseOfNewLocation(location);
		// //map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));
		// }
		//
		// public void onStatusChanged(String provider, int status, Bundle
		// extras) {}
		//
		// public void onProviderEnabled(String provider) {}
		//
		// public void onProviderDisabled(String provider) {}
		// };

		// Register the listener with the Location Manager to receive location
		// updates
		// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		// 0, 0, locationListener);

//		map = ((SupportMapFragment) getFragmentManager().findFragmentById(
//				R.id.map)).getMap();
//
//		if (map != null) {
//			
//			Criteria criteria = new Criteria();
//			String bestProvider = locationManager.getBestProvider(criteria, false);
//			Location loc = locationManager.getLastKnownLocation(bestProvider);
//
//			if (loc != null) {
//				Log.e("getLastKnownLocation", "Location: " + loc.toString());
//
//				// map.setMyLocationEnabled(true);
//
//				map.moveCamera(CameraUpdateFactory.newLatLngZoom(
//						new LatLng(loc.getLatitude(), loc.getLongitude()), 2));
//			}
//			
//
//			map.setMyLocationEnabled(true);
//			Marker hamburg = map.addMarker(new MarkerOptions().position(
//					new LatLng(-100, 50)).title("Hamburg"));
//			Marker kiel = map.addMarker(new MarkerOptions()
//					.position(new LatLng(-100, 120))
//					.title("Kiel")
//					.snippet("Kiel is cool")
//					.icon(BitmapDescriptorFactory
//							.fromResource(R.drawable.ic_launcher)));
//
//			map.setOnMapLongClickListener(new OnMapLongClickListener() {
//				@Override
//				public void onMapLongClick(LatLng point) {
//					map.addMarker(new MarkerOptions()
//							.position(point)
//							.title("You are here")
//							.icon(BitmapDescriptorFactory
//									.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//				}
//			});
//		}
		
		DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(getActivity());
		Resources resources = getResources();
		int countAllReports = 100;
		int countUnreadReports = 50;
		int countUnreportedReports = 20;

		// Init the bars and the points
		ArrayList<Bar> points = new ArrayList<Bar>();
		Bar allReportsBar = new Bar();
		allReportsBar.setColor(resources.getColor(R.color.bar_color_all_reports));
		allReportsBar.setName(resources.getString(R.string.bar_title_all));
		allReportsBar.setValue(countAllReports);
		Bar unreadReportsBar = new Bar();
		unreadReportsBar.setColor(resources.getColor(R.color.bar_color_unread_reports));
		unreadReportsBar.setName(resources.getString(R.string.bar_title_unread));
		allReportsBar.setValue(countUnreadReports);
		Bar unreportedBar = new Bar();
		unreportedBar.setColor(resources.getColor(R.color.bar_color_unreported_reports));
		unreportedBar.setName(resources.getString(R.string.bar_title_unreported));
		allReportsBar.setValue(countUnreportedReports);
		
		// Add the points of the bars
		points.add(allReportsBar);
		points.add(unreadReportsBar);
		points.add(unreportedBar);

		// Set the bars in the Graph object
		BarGraph g = (BarGraph)rootView.findViewById(R.id.graph);
		g.setBars(points);

		
		return rootView;
	}

}
