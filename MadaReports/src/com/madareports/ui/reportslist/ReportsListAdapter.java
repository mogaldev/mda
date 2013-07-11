package com.madareports.ui.reportslist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.MyRecord;
import com.madareports.db.models.Report;
import com.madareports.utils.Logger;

public class ReportsListAdapter extends ArrayAdapter<MyRecord> {
	private String TAG = Logger.makeLogTag(getClass());
	private ArrayList<Report> reportsList;
	private Context context;

	public ReportsListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
		initReportsList();
	}
	
	@Override
	public int getCount() {
		return reportsList.size();
	}

	private void initReportsList() {
		// get the reports from the db
		reportsList = (ArrayList<Report>) DatabaseWrapper.getInstance(context)
				.getAllReports();
		Logger.LOGE(TAG, "reportsList.size: #" + reportsList.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Logger.LOGE(TAG, "getView is called for item: #" + position);
		ReportListItem item = new ReportListItem(context, reportsList.get(position));
		return item;
	}
		

}