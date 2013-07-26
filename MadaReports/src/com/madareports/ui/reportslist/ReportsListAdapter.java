package com.madareports.ui.reportslist;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.madareports.R;
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

	static int counter = 0; // TODO remove

	@Override
	public int getCount() {
		return reportsList.size();
	}

	public void resetData() {
		Logger.LOGE(TAG, "reset data");
		reportsList = originalReportsList;
	}

	/**
	 * Generate the view that represents the list. Choose the layout that should
	 * be displayed according to the {@link Report}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// if (convertView != null)
		// return convertView;

		View itemView = null;
		try {
			// get the report that this item represents
			Report report = reportsList.get(position);

			// inflate the layout
			LayoutInflater li = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// choose the layout to be inflated according to the report
			// properties
			// TODO: change isWatch to isRead.
			if (report.isWatched()) {
				itemView = li.inflate(R.layout.read_reports_list_item, null);
				setUnreadItemView(itemView, report);
			} else { // unread mode

				itemView = li.inflate(R.layout.unread_reports_list_item, null);
				setUnreadItemView(itemView, report);
			}
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}

		return itemView;
	}

	private void setUnreadItemView(View view, Report report) {
		// initialize the views members
		TextView tvId = (TextView) view.findViewById(R.id.tvReportId);
		TextView tvReceivedAt = (TextView) view.findViewById(R.id.tvReceivedAt);
		TextView tvDescription = (TextView) view
				.findViewById(R.id.tvReportDescription);

		// set the values into the views
		tvId.setText(report.getId() + "#");
		tvReceivedAt.setText(report.getReceivedAt().toString());
		tvDescription.setText(report.getDescription(),
				TextView.BufferType.SPANNABLE);
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