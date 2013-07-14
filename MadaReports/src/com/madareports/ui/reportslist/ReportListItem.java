package com.madareports.ui.reportslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.madareports.R;
import com.madareports.db.models.Report;
import com.madareports.utils.Logger;

/**
 * Represents item in the reports list view
 * 
 */
public class ReportListItem extends RelativeLayout {// TODO maybe can remove the
													// inheritance
	private String TAG = Logger.makeLogTag(getClass());
	private TextView tvId;
	private TextView tvTitle;
	private TextView tvDescription;

	/**
	 * Constructs the item
	 * 
	 * @param context
	 *            - context for getting the layout inflater
	 * @param report
	 *            - the report that this item represents
	 */
	public ReportListItem(Context context, Report report) {
		super(context);

		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.report_list_view_item, this, true);

		// initialize the views members
		this.tvId = (TextView) findViewById(R.id.tvReportId);
		this.tvTitle = (TextView) findViewById(R.id.tvTitle);
		this.tvDescription = (TextView) findViewById(R.id.tvDescription);

		set(report);
	}

	/**
	 * Populate the item with an inputed report
	 * 
	 * @param report
	 *            - the report to get the information from
	 */
	private void set(Report report) {
		tvId.setText("" + report.getId());
		tvTitle.setText(report.getTitle());
		tvDescription.setText(report.getDescription(),
				TextView.BufferType.SPANNABLE);

	}

}