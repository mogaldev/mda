package com.madareports.ui.reportslist;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.models.Report;
import com.madareports.utils.Logger;

public class ReportsListAdapter extends ArrayAdapter<Report> {
	private String TAG = Logger.makeLogTag(getClass());
	private ArrayList<Report> reportsList;
	private ArrayList<Report> originalReportsList;
	private Context context;

	public ReportsListAdapter(Context context, int textViewResourceId,
			List<Report> reports) {
		super(context, textViewResourceId);
		this.context = context;
		this.reportsList = (ArrayList<Report>) reports;
		this.originalReportsList = this.reportsList;
	}

	public ReportsListAdapter(Context context, int textViewResourceId) {
		this(context, textViewResourceId, (ArrayList<Report>) DatabaseWrapper
				.getInstance(context).getAllReports());
	}

	static int counter = 0;

	@Override
	public int getCount() {
		return reportsList.size();
	}

	public void resetData() {
		Logger.LOGE(TAG, "reset data");
		reportsList = originalReportsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Logger.LOGE(TAG, "getView is called : #" + counter++);
		ReportListItem item = null;
		try {
			item = new ReportListItem(context, reportsList.get(position));
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}

		return item;
	}

	// TODO: check about more efficient filter mechanism
	@SuppressLint("DefaultLocale")
	@Override
	public Filter getFilter() {
		return new Filter() {

			private boolean isPassedFilterTest(Report report,
					CharSequence constraint) {
				String combined = report.getId() + ", " + report.getTitle()
						+ ", " + report.getDescription();

				return combined.toLowerCase().contains(
						constraint.toString().toLowerCase());
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				// We implement here the filter logic
				if (constraint == null || constraint.length() == 0) {
					// No filter implemented we return all the list
					results.values = originalReportsList;
					results.count = originalReportsList.size();
				} else {
					// We perform filtering operation
					List<Report> filteredList = new ArrayList<Report>();

					for (Report r : reportsList) {
						if (isPassedFilterTest(r, constraint))
							filteredList.add(r);
					}

					results.values = filteredList;
					results.count = filteredList.size();

				}
				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				reportsList = (ArrayList<Report>) results.values;
				notifyDataSetChanged();
			}
		};
	}

}