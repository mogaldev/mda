package com.madareports.ui.reportslist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.DbChangedNotifier;
import com.madareports.db.models.Report;
import com.madareports.ui.activities.details.DetailsActivity;
import com.madareports.utils.Logger;


public class ReportsListAdapter extends BaseAdapter implements Filterable, 
		DbChangedNotifier {
	private String TAG = Logger.makeLogTag(getClass());
	private Context context;
	private ArrayList<Report> reportsList;
	private ArrayList<Report> originalReportsList;
	LayoutInflater mInflater;

	public ReportsListAdapter(Context context, List<Report> reports) {
		this.context = context;
		this.reportsList = (ArrayList<Report>) reports;
		this.originalReportsList = this.reportsList;
		this.mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		DatabaseWrapper.getInstance(context).setDbChangedListener(this);
	}

	public ReportsListAdapter(Context context) {
		this(context, DatabaseWrapper.getInstance(context).getAllReports());
	}
	
	public Context getContext() {
		return this.context;
	}

	public void resetData() {
		reportsList = originalReportsList;
	}

	/**
	 * Generate the view that represents the list. Choose the layout that should
	 * be displayed according to the {@link Report}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = null;
		try {
			// get the report that this item represents
			final Report report = reportsList.get(position);

			// choose the layout to be inflated according to the report
			// properties
			if (report.isRead()) {
				itemView = mInflater.inflate(R.layout.read_reports_list_item, null);
				initItemView(itemView, report);
			} else { // unread mode
				itemView = mInflater.inflate(R.layout.unread_reports_list_item, null);
				initItemView(itemView, report);
			}
			
			itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), DetailsActivity.class);
					intent.putExtra(DetailsActivity.REPORT_ID_EXTRA, report.getId());
					v.getContext().startActivity(intent);
				}
			});
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}

		return itemView;
	}

	@SuppressLint("SimpleDateFormat")
    private void initItemView(View view, Report report) {
		// initialize the views members
		TextView tvId = (TextView) view.findViewById(R.id.tvReportId);
		TextView tvReceivedAt = (TextView) view.findViewById(R.id.tvReceivedAt);
		TextView tvDescription = (TextView) view
				.findViewById(R.id.tvReportDescription);

		// set the values into the views
		tvId.setText(report.getReportId() + "#");
		tvReceivedAt.setText(new SimpleDateFormat("E dd-MM-yyyy hh:mm").format(report.getReceivedAt()));
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
				String combined = report.getReportId() + ", "
						+ report.getDescription();

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

	@Override
	public int getCount() {
		return reportsList.size();
	}

	@Override
	public void DbChanged() {
		this.originalReportsList = (ArrayList<Report>) DatabaseWrapper
				.getInstance(getContext()).getAllReports();
		resetData();
		notifyDataSetChanged();
	}

	public List<String> getToShareStringOfReports() {
		List<String> returnList = new ArrayList<String>();
		for (Report report : reportsList) {
	        returnList.add(report.toShareString(getContext()));
        }
		
		return returnList;
	}

	@Override
    public Object getItem(int position) {
		return this.originalReportsList.get(position);
    }

	@Override
    public long getItemId(int position) {
		return this.originalReportsList.get(position).getId();
    }
}