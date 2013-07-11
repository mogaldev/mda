package com.madareports;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.madareports.ui.reportslist.ReportsListAdapter;
import com.madareports.utils.Logger;

public class FilterListViewTests extends Activity {
	private String TAG = Logger.makeLogTag(getClass());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_list_view_tests);
		Logger.LOGE(TAG, "onCreate");
		ListView lv = (ListView)findViewById(R.id.listView);
		lv.setAdapter(new ReportsListAdapter(this,
				R.layout.report_list_view_item));
/*		setListAdapter(new ReportsListAdapter(this,
				R.layout.report_list_view_item));*/
	}

}
