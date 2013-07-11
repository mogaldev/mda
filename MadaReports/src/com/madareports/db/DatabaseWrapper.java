package com.madareports.db;

import java.util.List;

import android.content.Context;
import com.madareports.db.models.Report;
import com.madareports.utils.Logger;

public class DatabaseWrapper {
	private String TAG = Logger.makeLogTag(getClass());

	private static DatabaseWrapper instance;
	private DbHelper helper;

	public static DatabaseWrapper getInstance(Context ctx) {
		if (instance == null) {
			instance = new DatabaseWrapper(ctx);
		}
		return instance;
	}

	private DatabaseWrapper(Context ctx) {
		helper = new DbHelper(ctx);
	}

	public List<Report> getAllReports() {
		List<Report> reports = null;
		try {
			reports = helper.getReportsDao().queryForAll();
			// TODO remove
			if (reports.size() < 1){
				// fill with random items
				int numOfRecords = 4;
				for (int i = 0; i < numOfRecords; i++) {
					helper.getReportsDao().create(new Report(i));
				}
				
			}
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return reports;
	}
}