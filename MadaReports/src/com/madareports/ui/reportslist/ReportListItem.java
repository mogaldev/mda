package com.madareports.ui.reportslist;

import com.madareports.R;
import com.madareports.db.MyRecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

// TODO maybe can remove the inheritance
public class ReportListItem extends RelativeLayout {
	private TextView tvId;
	private TextView tvTitle;
	private TextView tvDescription;

	public ReportListItem(Context context) {
		super(context);

		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.report_list_view_item, this, true);

		this.tvId = (TextView) findViewById(R.id.tvReportId);
		this.tvTitle = (TextView) findViewById(R.id.tvTitle);
		this.tvDescription = (TextView) findViewById(R.id.tvDescription);
	}

	public void set(MyRecord record) {
		tvId.setText(record.id);
		tvTitle.setText(record.title);
		tvDescription.setText(record.description, TextView.BufferType.SPANNABLE);
	}

}