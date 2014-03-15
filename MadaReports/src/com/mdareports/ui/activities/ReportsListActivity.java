package com.mdareports.ui.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.ui.fragments.reportslists.AllReportsFragment;
import com.mdareports.ui.fragments.reportslists.BaseReportsListFragment;
import com.mdareports.ui.fragments.reportslists.ReportsListsFilters;
import com.mdareports.ui.fragments.reportslists.UnreadReportsFragment;
import com.mdareports.ui.fragments.reportslists.UnreportedReportsFragment;
import com.mdareports.ui.reportslist.ReportsFilterTextWatcher;
import com.mdareports.ui.reportslist.ReportsListAdapter;
import com.mdareports.utils.DeviceInfoUtils;
import com.mdareports.utils.FontTypeFaceManager;
import com.mdareports.utils.FontTypeFaceManager.CustomFonts;
import com.mdareports.utils.NotificationsManager;
import com.mdareports.utils.SettingsManager;

public class ReportsListActivity extends BaseActivity {

	private ReportsFilterTextWatcher reportsFilterTextWatcher;
	public static final String REPORTS_LIST_ARGS = "REPORTS_LIST_ARGS";
	private static BaseReportsListFragment currentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports_list);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		currentFragment = getFragmentToDisplay();
		if (currentFragment != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.reportsListFrgmtContainer, currentFragment)
					.commit();
		}

	}

	private BaseReportsListFragment getFragmentToDisplay() {
		Intent intent = getIntent();

		if (intent != null && intent.hasExtra(REPORTS_LIST_ARGS)) {
			ReportsListsFilters filter = ReportsListsFilters.values()[intent
					.getIntExtra(REPORTS_LIST_ARGS, -1)];

			if (filter == ReportsListsFilters.All) {
				return new AllReportsFragment();
			} else if (filter == ReportsListsFilters.Unread) {
				return new UnreadReportsFragment();
			} else if (filter == ReportsListsFilters.Unreported) {
				return new UnreportedReportsFragment();
			}
		}

		return currentFragment;
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Just remove the SMS Received Notification
		NotificationsManager.getInstance(this).removeSmsReceivedNotification();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.reportslist_activity_action_bar, menu);

		// TODO: fix!!! with v7 support library
		// initializing the search action view
		MenuItem item = menu.findItem(R.id.reportslist_activity_menu_search);
		final EditText txt = (EditText) MenuItemCompat.getActionView(item);

		MenuItemCompat.setOnActionExpandListener(item,
				new OnActionExpandListener() {

					@Override
					public boolean onMenuItemActionExpand(MenuItem item) {
						txt.requestFocus();
						txt.setHint(R.string.action_bars_search_view_hint);
						txt.setEms(20);

						// enable the 'real-time' filtering on the edit text
						reportsFilterTextWatcher = new ReportsFilterTextWatcher(
								currentFragment.getAdapter());
						txt.addTextChangedListener(reportsFilterTextWatcher);

						return true;
					}

					@Override
					public boolean onMenuItemActionCollapse(MenuItem item) { // Erase
																				// the
																				// text
																				// in
																				// the
																				// Search
																				// EditText
																				// in
																				// the
																				// //
																				// ActionBar
						txt.setText("");

						// Remove the TextWatcher
						txt.removeTextChangedListener(reportsFilterTextWatcher);

						return true;
					}
				});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.reportslist_activity_menu_settings:
			if (DeviceInfoUtils.hasHoneycomb()) {
				// TODO MoveTo(SettingsActivity.class);
			} else {
				MoveTo(OldSettingsActivity.class);
			}
			return true;
		case R.id.reportslist_activity_menu_delete_all:
			handleDelete(this);
			return true;

		case R.id.reportslist_activity_menu_share:
			// The ShareActionProvider of sherlock is not working well on
			// Android 2.3.5 (i checked it on Galaxy 2)
			// So here we will use the default ShareIntent
			Intent sendIntent = currentFragment.getShareIntent();
			startActivity(Intent
					.createChooser(
							sendIntent,
							getString(R.string.reportslist_activity_action_bar_share_message)));
			return true;

		case R.id.reportslist_activity_menu_patient_report:
			MoveTo(PatientReportActivity.class);
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
									currentFragment.removeDisplayedReportes();
								}
								dialog.dismiss();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

}
