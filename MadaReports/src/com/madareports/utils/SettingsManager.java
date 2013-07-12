package com.madareports.utils;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.madareports.R;

/**
 * Wrapper for the SharedPreferenceAdaper for easily get the saved values.
 * Implements the Single-tone design pattern.
 */
public class SettingsManager {
	private static SettingsManager instance;
	private Context cntx;

	/**
	 * Get instance of the class. implements the singleton design pattern.
	 * 
	 * @return instance of the class to work with
	 */
	public static SettingsManager getInstance(Context context) {
		if (instance == null) {
			instance = new SettingsManager(context);
		}
		return instance;
	}

	/**
	 * Constructs the session manager object with context. The context should be
	 * the activity that holds the operations.
	 */
	private SettingsManager(Context context) {
		this.cntx = context;

		PreferenceManager.setDefaultValues(cntx, R.xml.preferences_settings,
				false);
	}

	/**
	 * Getting the URI of the chosen ringtone. if no ringtone has been chosen
	 * the ALARM_ALERT is returned
	 */
	public android.net.Uri getRingtoneUri() {
		String defaultRingtone = RingtoneManager.getDefaultUri(
				RingtoneManager.TYPE_ALARM).toString();

		boolean exists = PreferenceManager.getDefaultSharedPreferences(
				this.cntx).contains(
				cntx.getResources().getString(
						R.string.prefrences_key_ringtone_uri));

		Log.e("getRingtoneUri-IsExists", exists + "");

		String valueFromPref = SharedPreferenceAdapter.getInstance(cntx)
				.readString(R.string.prefrences_key_ringtone_uri,
						defaultRingtone);
		Log.e("getRingtoneUri", valueFromPref);
		return Uri.parse(valueFromPref);
	}

	public void setRingtoneUri(Uri ringtoneUri) {
		Log.e("setRingtoneUri", ringtoneUri.toString());
		SharedPreferenceAdapter.getInstance(cntx).writeString(
				R.string.prefrences_key_ringtone_uri, ringtoneUri.toString());
	}

	/**
	 * Whether to perform vibration when the time is up
	 */
	public boolean isVibrateEnabled() {
		return SharedPreferenceAdapter.getInstance(cntx).readBoolean(
				R.string.prefrences_key_vibrate_enabled);
	}

	/**
	 * Whether to play sound when the time is up. in case that this option
	 * disabled the getRingtoneUri() if irrelevant
	 */
	public boolean isSoundEnabled() {
		return SharedPreferenceAdapter.getInstance(cntx).readBoolean(
				R.string.prefrences_key_sound_enabled);
	}

}