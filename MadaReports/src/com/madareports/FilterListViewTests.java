package com.madareports;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.madareports.ui.activities.BaseActivity;
import com.madareports.ui.activities.SettingsActivity;
import com.madareports.ui.reportslist.ReportsFilterTextWatcher;
import com.madareports.ui.reportslist.ReportsListAdapter;

public class FilterListViewTests extends BaseActivity {

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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.reportslist_activity_action_bar, menu);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.filter_list_view_tests_menu_delete_all_reports:
				//DatabaseWrapper.getInstance(this).deleteAllReports();
				return true;
			case R.id.reportslist_activity_menu_settings:
				MoveTo(SettingsActivity.class);
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
