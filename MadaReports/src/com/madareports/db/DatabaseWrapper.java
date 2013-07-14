package com.madareports.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
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
			if (reports.size() < 1) {
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

	public void AddReport(Report report) {
		try {
			helper.getReportsDao().create(report);
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public int getNumOfReports(){		
		Dao<Report, Integer> dao = helper.getReportsDao();
		try {
			return (int)dao.countOf(dao.queryBuilder().setCountOf(true).where().eq("isWatched", "1").prepare());
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return -1;
		}
	}

}