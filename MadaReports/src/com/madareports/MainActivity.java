package com.madareports;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.madareports.utils.Logger;

//TODO: DB aspect - add isViewed for each report to indicate whether the report has been seen
public class MainActivity extends Activity  {
	private final String TAG = Logger.makeLogTag(getClass());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// tests:
		String msg = String.format(
				this.getString(R.string.notification_d_new_messages), 5);
		((TextView) findViewById(R.id.tvHelloWorld)).setText(msg);
	}




}
