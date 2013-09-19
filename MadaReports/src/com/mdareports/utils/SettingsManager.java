package com.mdareports.utils;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mdareports.R;

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
	 * Get instance of the SharedPreferenceAdapter
	 */
	private SharedPreferenceAdapter getAdapterInstance() {
		return SharedPreferenceAdapter.getInstance(cntx);
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

		String valueFromPref = getAdapterInstance().readString(
				R.string.prefrences_key_ringtone_uri, defaultRingtone);
		Log.e("getRingtoneUri", valueFromPref);
		return Uri.parse(valueFromPref);
	}

	public void setRingtoneUri(Uri ringtoneUri) {
		Log.e("setRingtoneUri", ringtoneUri.toString());
		getAdapterInstance().writeString(R.string.prefrences_key_ringtone_uri,
				ringtoneUri.toString());
	}

	/**
	 * Whether to perform vibration when the time is up
	 */
	public boolean isVibrateEnabled() {
		return getAdapterInstance().readBoolean(
				R.string.prefrences_key_vibrate_enabled);
	}

	/**
	 * Whether to play sound when the time is up. in case that this option
	 * disabled the getRingtoneUri() if irrelevant
	 */
	public boolean isSoundEnabled() {
		return getAdapterInstance().readBoolean(
				R.string.prefrences_key_sound_enabled);
	}

	/**
	 * Save the information of the reports sender (m.d.a specific number)
	 * 
	 * @param displayName
	 *            - The name of the reports sender in the the contacts
	 * @param numbers
	 *            - The numbers of the sender separated by ';'. should be only
	 *            one number but this was made for treating the case of multiple
	 *            report senders
	 */
	public void setReportsSender(String displayName, String numbers) {
		SharedPreferenceAdapter spAdapter = getAdapterInstance();
		spAdapter.writeString(R.string.prefrences_key_sender_name, displayName);
		spAdapter.writeString(R.string.prefrences_key_sender_numbers,
				displayName);
	}

	/**
	 * Get the display name of the reports sender
	 */
	public String getReportsSenderName() {
		return getAdapterInstance().readString(
				R.string.prefrences_key_sender_name);
	}

	/**
	 * Check if the inputed number is included in the reports-sender phone
	 * numbers
	 * 
	 * @param number
	 *            - the number to be checked
	 * @return True if the reports-sender own this numbers, False otherwise
	 */
	public boolean isReportsSenderNumber(String number) {
		String[] numbers = getAdapterInstance().readString(
				R.string.prefrences_key_sender_numbers).split(";");
		if (numbers.length > 0) {
			for (String phoneNumber : numbers) {
				if (android.telephony.PhoneNumberUtils.compare(phoneNumber,
						number))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if the application should abort the broadcast when the phone getting SMS
	 * @return true if should abort, otherwise false
	 */
	public boolean getAbortBroadcast() {
		return getAdapterInstance().readBoolean(R.string.prefrences_key_abort_broadcast, false);
	}
	
	public String getBufferedMessage() {
		String readString = getAdapterInstance().readString("bufferedMessage", "");
		readString = readString == null ? "" : readString;
		return readString;
	}
	
	public void setBufferedMessage(String value) {
		getAdapterInstance().writeString("bufferedMessage", value);
	}

}