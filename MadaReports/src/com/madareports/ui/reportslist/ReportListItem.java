package com.madareports.ui.reportslist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
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
	private TextView tvDescription;
	private TextView tvReceivedAt;
	private ImageView imgIcon;

	/**
	 * Constructs the item
	 * 
	 * @param context
	 *            - context for getting the layout inflater
	 * @param report
	 *            - the report that this item represents
	 */
	public ReportListItem(Context context, Report report) {
		this(context);
		set(report);
	}

	public ReportListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ReportListItem(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
	}

	public ReportListItem(Context context) {
		super(context);

		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.reports_list_item, this, true);

		// initialize the views members
		this.tvId = (TextView) findViewById(R.id.tvReportId);
		this.tvReceivedAt = (TextView) findViewById(R.id.tvReceivedAt);
		this.tvDescription = (TextView) findViewById(R.id.tvReportDescription);
	}

	/**
	 * Populate the item with an inputed report
	 * 
	 * @param report
	 *            - the report to get the information from
	 */
	private void set(Report report) {
		tvId.setText(report.getId() + "#");
		tvReceivedAt.setText(report.getReceivedAt().toString());
		tvDescription.setText(report.getDescription(),
				TextView.BufferType.SPANNABLE);
		//tvReceivedAt.setText("Hello world");//report.getReceivedAt().toString()
		/*tvDescription.setText(report.getDescription(),
				TextView.BufferType.SPANNABLE);*/

	}

}