package com.madareports.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.madareports.db.models.Report;
import com.madareports.utils.Logger;

public class DatabaseWrapper {
	private String TAG = Logger.makeLogTag(getClass());
	private static DatabaseWrapper instance;
	private DbHelper helper;
	List<DbChangedNotifier> listeners;

	public static DatabaseWrapper getInstance(Context ctx) {
		if (instance == null) {
			instance = new DatabaseWrapper(ctx);
		}
		return instance;
	}

	private DatabaseWrapper(Context ctx) {
		helper = new DbHelper(ctx);
		listeners = new ArrayList<DbChangedNotifier>();
	}

	public void notifyDatabaseChanged(){
		for (DbChangedNotifier listener : listeners) {
			listener.DbChanged();
		}
	}
	
	public void setDbChangedListener(DbChangedNotifier listener){
		listeners.add(listener);
	}
	
	// ////////////////////////////
	// Reports Functions
	// ////////////////////////////
	
	public List<Report> getAllReports() {
		List<Report> reports = null;
		try {
			// ownerDao.queryBuilder().orderByRaw("Name COLLATE NOCASE").query();
			reports = helper.getReportsDao().queryForAll();
			// TODO remove
			if (reports.size() < 1) {
				// fill with random items
				int numOfRecords = 4;
				for (int i = 0; i < numOfRecords; i++) {
					// helper.getReportsDao().create(new Report(i));
				}
			}
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return reports;
	}

	public void DeleteAllReports() {
		try {
			helper.getReportsDao().delete(getAllReports());
			notifyDatabaseChanged();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public void AddReport(Report report) {
		try {
			helper.getReportsDao().create(report);
			notifyDatabaseChanged();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public int countUnreadReports() {
		Dao<Report, Integer> dao = helper.getReportsDao();
		try {
			// TODO find different way, not using hard-coded column name
			return (int) dao.countOf(dao.queryBuilder().setCountOf(true)
					.where().eq("isWatched", false).prepare());
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return -1;
		}
	}

	public void setRandomReadOrUnread() {
		Random rnd = new Random();

		List<Report> list = getAllReports();
		for (Report report : list) {
			report.setWatched(new Random().nextBoolean());
			try {
				helper.getReportsDao().update(report);
			} catch (SQLException e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
	}

	// ////////////////////////////
	// End of Reports Functions
	// ////////////////////////////
}