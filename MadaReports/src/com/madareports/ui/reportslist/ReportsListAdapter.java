package com.madareports.ui.reportslist;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.madareports.R;
import com.madareports.db.FakeDb;
import com.madareports.db.MyRecord;

public class ReportsListAdapter extends ArrayAdapter<MyRecord> {
	FakeDb db;
	private Context context;

	public ReportsListAdapter(Context context, int textViewResourceId,
			List<MyRecord> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		db = new FakeDb();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View recordView = inflater.inflate(R.layout.report_list_view_item, parent, false);
		// TODO save list locally or find another way. it is very inefficeint
		((ReportListItem)recordView).set(db.getRecords().get(position));
		return recordView;
	}
}