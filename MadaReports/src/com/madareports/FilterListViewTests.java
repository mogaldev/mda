package com.madareports;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.madareports.db.DatabaseWrapper;
import com.madareports.db.models.Report;
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

		// DatabaseWrapper.getInstance(this).DeleteAllReports();
		DatabaseWrapper.getInstance(this).setRandomReadOrUnread();
	}

	
}
