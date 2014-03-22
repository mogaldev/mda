package com.mdareports.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mdareports.R;

public class HomeFragment extends BaseFragment {
	GoogleMap map;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

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

		if (map != null) {
			
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

			map.setMyLocationEnabled(true);
			Marker hamburg = map.addMarker(new MarkerOptions().position(
					new LatLng(-100, 50)).title("Hamburg"));
			Marker kiel = map.addMarker(new MarkerOptions()
					.position(new LatLng(-100, 120))
					.title("Kiel")
					.snippet("Kiel is cool")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));

			map.setOnMapLongClickListener(new OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng point) {
					map.addMarker(new MarkerOptions()
							.position(point)
							.title("You are here")
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
				}
			});
		}

		return rootView;
	}

}
