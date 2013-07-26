package com.madareports;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.madareports.db.DatabaseWrapper;
import com.madareports.ui.reportslist.ReportsFilterTextWatcher;
import com.madareports.ui.reportslist.ReportsListAdapter;
import com.madareports.utils.Logger;

public class FilterListViewTests extends Activity {
	private String TAG = Logger.makeLogTag(getClass());
	private ReportsListAdapter reportsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_list_view_tests);

		ListView lv = (ListView) findViewById(R.id.listView);
		EditText txtSearch = (EditText) findViewById(R.id.editTxt);

		reportsAdapter = new ReportsListAdapter(this,
				R.layout.unread_reports_list_item);
		lv.setAdapter(reportsAdapter);

		// enable the 'real-time' filtering on the edit text
		txtSearch.addTextChangedListener(new ReportsFilterTextWatcher(
				reportsAdapter));

		// button used for debug
		((Button) findViewById(R.id.btnTests))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						DatabaseWrapper.getInstance(v.getContext()).DeleteAllReports();
					}
				});

		DatabaseWrapper.getInstance(this).setRandomReadOrUnread();
	}

}
