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

import com.google.ads.AdView;
import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.ui.reportslist.ReportsFilterTextWatcher;
import com.mdareports.ui.reportslist.ReportsListAdapter;
import com.mdareports.utils.DeviceInfoUtils;
import com.mdareports.utils.FontTypeFaceManager;
import com.mdareports.utils.FontTypeFaceManager.CustomFonts;
import com.mdareports.utils.NotificationsManager;
import com.mdareports.utils.SettingsManager;

public class ReportsListActivity extends BaseActivity {

	private ReportsListAdapter reportsAdapter;
	private ReportsFilterTextWatcher reportsFilterTextWatcher;
	private ListView listView;
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports_list_view);

		// Find the listView in the activity's layout for future use
		listView = (ListView) findViewById(R.id.listView);
		setListEmptyView();

		// Find the AdView in the activity's layout Load the AdView with an ad
		// request
		adView = (AdView) findViewById(R.id.adView);

		/**
		 * We can "Edit" the AdView from the code, with the AdRequest object.
		 * When the loadAdOnCreate=false in the com.google.ads.AdView in the
		 * layout, we have to initialize the AdView from the code, with the
		 * AdRequest Object. When the loadAdOnCreate=true, we don't have to edit
		 * the AdRequest from the code
		 */
		// Create the AdRequest and add parameters to it
		// AdRequest adRequest = new AdRequest();
		// adRequest.addTestDevice(AdRequest.TEST_EMULATOR); // All emulators
		// adRequest.addTestDevice("8E940041CA56DCF10D0F5218CD835405"); // Gal's
		// Device id
		// adRequest.addTestDevice("1672CE270A7CDA8B7CA4AFCBF2E5A0D8"); //
		// Moshe's Device id
		// adView.loadAd(adRequest);
	}

	private void setListEmptyView() {
		View empty = getLayoutInflater().inflate(
				R.layout.empty_report_list_item, null, false);

		// set the view's text font
		FontTypeFaceManager
				.getInstance(this)
				.setFont(
						(TextView) empty.findViewById(R.id.emptyViewLabel),
						DeviceInfoUtils.isCurrentLanguageHebrew(this) ? CustomFonts.YoavRegular
								: CustomFonts.RobotoThin);

		addContentView(empty, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listView.setEmptyView(empty);
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
	public void onDestroy() {
		// Handle the AdView
		if (adView != null) {
			adView.destroy();
		}

		super.onDestroy();
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
								reportsAdapter);
						txt.addTextChangedListener(reportsFilterTextWatcher);

						return true;
					}

					@Override
					public boolean onMenuItemActionCollapse(MenuItem item) {
						// Erase the text in the Search EditText in the
						// ActionBar
						txt.setText("");

						// Remove the TextWatcher
						txt.removeTextChangedListener(reportsFilterTextWatcher);

						return true;
					}
				});

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
		shareString.append(SettingsManager.getInstance(this)
				.getVolunteerSignature());

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
			// The ShareActionProvider of sherlock is not working well on
			// Android 2.3.5 (i checked it on Galaxy 2)
			// So here we will use the default ShareIntent
			Intent sendIntent = getShareIntent();
			startActivity(Intent
					.createChooser(
							sendIntent,
							getString(R.string.reportslist_activity_action_bar_share_message)));
			return true;

		case R.id.reportslist_activity_menu_patient_report:
			MoveTo(PatientReportActivity.class);
			return true;

		case R.id.reportslist_activity_menu_about_us:
			MoveTo(AboutUsActivity.class);
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
