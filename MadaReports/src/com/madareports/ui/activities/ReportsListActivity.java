package com.madareports.ui.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.ui.reportslist.ReportsFilterTextWatcher;
import com.madareports.ui.reportslist.ReportsListAdapter;

public class ReportsListActivity extends BaseActivity {

	private ReportsListAdapter reportsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports_list_view);
		
		ListView lv = (ListView) findViewById(R.id.listView);
	
		reportsAdapter = new ReportsListAdapter(this,
				R.layout.unread_reports_list_item);
		lv.setAdapter(reportsAdapter);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.reportslist_activity_action_bar, menu);

		/*
		 * Initializing the search action view
		 */
		MenuItem item = menu.findItem(R.id.reportslist_activity_menu_search);
		EditText txt = (EditText)item.getActionView();
		
		// enable the 'real-time' filtering on the edit text
		txt.addTextChangedListener(new ReportsFilterTextWatcher(
				reportsAdapter));
		
		return true;
	}
	
	private Intent getShareIntent() {
		StringBuffer shareString = new StringBuffer();
		List<String> reportsOnScreen = reportsAdapter.getToShareStringOfReports();
		for (String currentReport : reportsOnScreen) {
	        shareString.append(currentReport);
	        shareString.append("\n\n");
        }
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareString.toString());
		shareIntent.setType("text/plain");

		return shareIntent;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.reportslist_activity_menu_settings:
				MoveTo(SettingsActivity.class);
				return true;
			case R.id.reportslist_activity_menu_delete_all:
				DatabaseWrapper.getInstance(this).deleteAllReports();
				return true;
			case R.id.reportslist_activity_menu_share:
				ShareActionProvider shareActionProvider = (ShareActionProvider) item.getActionProvider();
				shareActionProvider.setShareIntent(getShareIntent());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
