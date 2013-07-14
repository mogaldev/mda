package com.madareports.ui.customviews;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class ContactPickerPreference extends DialogPreference {

	/**
	 * Constructors
	 */
	public ContactPickerPreference(Context ctxt) {
		this(ctxt, null);
	}

	public ContactPickerPreference(Context ctxt, AttributeSet attrs) {
		this(ctxt, attrs, 0);
	}

	public ContactPickerPreference(Context ctxt, AttributeSet attrs,
			int defStyle) {
		super(ctxt, attrs, defStyle);
	}

	/**
	 * Initialize the time picker
	 */
	/*
	 * @Override protected View onCreateDialogView() { picker = new
	 * TimePicker(getContext()); picker.setIs24HourView(true);
	 * 
	 * return picker; }
	 */

	/**
	 * Loading the values from the preferences to the time picker
	 */
	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);

		/*// get the interval according the key
		SettingsManager sm = SettingsManager.getInstance(getContext());
		int intervalInMinutes = sm.getInterval(getKey());

		// convert the minutes to hours and minutes in array
		int[] pieces = TimeConversions
				.getTimeInHoursAndMinutes(intervalInMinutes);

		picker.setCurrentHour(pieces[0]);
		picker.setCurrentMinute(pieces[1]);*/
		
		
		
	}

	/**
	 * Saving the values in case of positive result
	 */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		/*if (positiveResult) {
			SettingsManager sm = SettingsManager.getInstance(getContext());
			sm.setInterval(getKey(), picker.getCurrentHour(),
					picker.getCurrentMinute());
		}*/
	}

}