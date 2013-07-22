package com.madareports;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import com.madareports.ui.reportslist.ReportsListAdapter;
import com.madareports.utils.Logger;

public class FilterListViewTests extends Activity {
	private String TAG = Logger.makeLogTag(getClass());
	private ReportsListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_list_view_tests);

		ListView lv = (ListView) findViewById(R.id.listView);
		mAdapter = new ReportsListAdapter(this, R.layout.reports_list_item);
		lv.setAdapter(mAdapter);

		EditText inputSearch = (EditText) findViewById(R.id.editTxt);

		// Filter callbacks
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int start, int before,
					int count) {
				ReportsListAdapter adptr = FilterListViewTests.this.mAdapter;

				if (count < before) {
					// We're deleting char so we need to reset the adapter data
					adptr.resetData();
				}
				// When user changed the Text
				adptr.getFilter().filter(cs);
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

		});
	}
}
