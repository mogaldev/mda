package com.mdareports.ui.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.ui.reportslist.ReportsFilterTextWatcher;
import com.mdareports.ui.reportslist.ReportsListAdapter;
import com.mdareports.utils.DeviceInfoUtils;
import com.mdareports.utils.NotificationsManager;
import com.mdareports.utils.SettingsManager;

public class ReportsListActivity extends BaseActivity {

	private ReportsListAdapter reportsAdapter;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports_list_view);
		listView = (ListView) findViewById(R.id.listView);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		reportsAdapter = new ReportsListAdapter(this);
		listView.setAdapter(reportsAdapter);

		// Just remove the SMS Received Notification
		NotificationsManager.getInstance(this).removeSmsReceivedNotification();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.reportslist_activity_action_bar, menu);

		// initializing the search action view
		MenuItem item = menu.findItem(R.id.reportslist_activity_menu_search);
		EditText txt = (EditText) item.getActionView();

		// enable the 'real-time' filtering on the edit text
		txt.addTextChangedListener(new ReportsFilterTextWatcher(reportsAdapter));

		return true;
	}

	private Intent getShareIntent() {
		StringBuffer shareString = new StringBuffer();
		List<String> reportsOnScreen = reportsAdapter
				.getToShareStringOfReports();
		for (String currentReport : reportsOnScreen) {
			shareString.append(currentReport);
			shareString.append("\n\n");
		}
		shareString.append(SettingsManager.getInstance(this).getVolunteerSignature());
		
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
			if (DeviceInfoUtils.hasHoneycomb()) {
				MoveTo(SettingsActivity.class);
			} else { 
				MoveTo(OldSettingsActivity.class);
			}			
			return true;
		case R.id.reportslist_activity_menu_delete_all:
			handleDelete(this);
			return true;
		case R.id.reportslist_activity_menu_share:
			// The ShareActionProvider of sherlock is not wirking well on Android 2.3.5 (i checked it on Galaxy 2)
			// So here we will use the default ShareIntent
			Intent sendIntent = getShareIntent();
			startActivity(Intent.createChooser(sendIntent, getString(R.string.reportslist_activity_action_bar_share_message)));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void handleDelete(final Context context) {
		// show the options in a dialog
		new AlertDialog.Builder(this)
				.setMessage(R.string.detail_activity_dialog_delete_are_you_sure)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								if (item == DialogInterface.BUTTON_POSITIVE) {
									// perform the delete
									if (DatabaseWrapper.getInstance(context)
											.deleteAllReports()) {

										writeShortTimeMessage(R.string.code_table_deleted_successfuly);
									}
								}
								dialog.dismiss();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

}
