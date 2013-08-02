package com.madareports;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.madareports.db.DatabaseWrapper;
import com.madareports.ui.activities.BaseActivity;
import com.madareports.ui.activities.RegionActivity;
import com.madareports.ui.activities.TreatmentsActivity;
import com.madareports.ui.reportslist.ReportsFilterTextWatcher;
import com.madareports.ui.reportslist.ReportsListAdapter;
import com.madareports.utils.Logger;

public class FilterListViewTests extends BaseActivity {
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
						DatabaseWrapper.getInstance(v.getContext())
								.deleteAllReports();
			}
				});
		
		// button for adding regions
		((Button) findViewById(R.id.btnRegion)).setOnClickListener(getMoveToClickListener(RegionActivity.class));
		
		// button for adding treatments
		((Button) findViewById(R.id.btnTreatment)).setOnClickListener(getMoveToClickListener(TreatmentsActivity.class));
	}

}
