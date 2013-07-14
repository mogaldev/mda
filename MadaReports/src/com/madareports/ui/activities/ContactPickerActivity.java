package com.madareports.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;

import com.madareports.utils.Logger;

public class ContactPickerActivity extends BaseActivity {
	private final String TAG = Logger.makeLogTag(getClass());
	private static final int PICK_CONTACT_REQUEST = 1; // The request code

	private void pickContact() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
				Uri.parse("content://contacts"));
		pickContactIntent
				.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show
																				// user
																				// only
		// contacts w/ phone
		// numbers
		startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pickContact();
	}

	private String getString(Cursor cursor, String columnName) {
		return cursor.getString(cursor.getColumnIndex(columnName));
	}

	private boolean hasPhoneNumber(Cursor cursor) {
		return getString(cursor, Contacts.HAS_PHONE_NUMBER).equals("1");
	}

	private void performQueryOnSeperateThread(final Uri contactUri) {
		Logger.LOGE(TAG, "performQueryOnSeperateThread");
		/*
		 * new Thread(new Runnable() { public void run() {
		 */
		// We only need the NUMBER column, because there will be only
		// one row in the result
		String[] projection = { Phone.DISPLAY_NAME, Phone.NUMBER,
				Contacts.HAS_PHONE_NUMBER };

		// Perform the query on the contact to get the NUMBER column
		// We don't need a selection or sort order (there's only one
		// result for the given URI)
		// CAUTION: The query() method should be called from a separate
		// thread to avoid blocking
		// your app's UI thread. (For simplicity of the sample, this
		// code doesn't do that.)
		// Consider using CursorLoader to perform the query.
		Cursor cursor = getContentResolver().query(contactUri, projection,
				null, null, null);
		boolean foundNumberFlag = false;
		while (cursor.moveToNext()) {
			if (hasPhoneNumber(cursor)) {
				String number = getString(cursor, Phone.NUMBER);
				String name = getString(cursor, Phone.DISPLAY_NAME);
				foundNumberFlag = true;

				Logger.LOGE(TAG, name + " ; " + number);
				Logger.LOGE(TAG, "HasePhoneNumber = " + hasPhoneNumber(cursor));
			}
		}

		Logger.LOGE(TAG, "Found number: " + foundNumberFlag);

		/*
		 * } }).start();
		 */
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request it is that we're responding to
		if (requestCode == PICK_CONTACT_REQUEST) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// Get the URI that points to the selected contact
				Uri contactUri = data.getData();
				performQueryOnSeperateThread(contactUri);
				// Do something with the phone number...

			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Logger.LOGE(TAG, "onStop");
		// MoveTo(SettingsActivity.class);
		// finish();
	}
}
