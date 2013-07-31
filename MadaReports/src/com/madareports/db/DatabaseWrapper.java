package com.madareports.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.madareports.db.codetables.CodeTables;
import com.madareports.db.codetables.ICodeTable;
import com.madareports.db.models.Region;
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

	public void notifyDatabaseChanged() {
		for (DbChangedNotifier listener : listeners) {
			listener.DbChanged();
		}
	}

	public void setDbChangedListener(DbChangedNotifier listener) {
		listeners.add(listener);
	}

	// ////////////////////////////
	// Reports Functions
	// ////////////////////////////

	public List<Report> getAllReports() {
		List<Report> reports = null;
		try {
			// ownerDao.queryBuilder().orderByRaw("Name COLLATE NOCASE").query();
			reports = helper.getReportDao().queryForAll();
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return reports;
	}

	public void deleteAllReports() {
		try {
			helper.getReportDao().delete(getAllReports());
			notifyDatabaseChanged();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public void AddReport(Report report) {
		try {
			helper.getReportDao().create(report);
			notifyDatabaseChanged();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public int countUnreadReports() {
		Dao<Report, Integer> dao = helper.getReportDao();
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
		List<Report> list = getAllReports();
		for (Report report : list) {
			report.setWatched(new Random().nextBoolean());
			try {
				helper.getReportDao().update(report);
			} catch (SQLException e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
	}
	
	public Report getReportById(int reportId) {
		Report reportToReturn = null;
		try {
			reportToReturn = helper.getReportDao().queryForId(reportId);
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return reportToReturn;
	}
	
	public void updateReport(Report report) {
		try {
			helper.getReportDao().update(report);
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	// ////////////////////////////
	// End of Reports Functions
	// ////////////////////////////


	// ////////////////////////////
	// Code Tables Functions
	// ////////////////////////////

	@SuppressWarnings("unchecked")
	public List<ICodeTable> getAll(CodeTables table) {
		List<ICodeTable> records = null;
		try {
			switch (table) {
				case Regions:
					records = (List<ICodeTable>) (List<?>) helper.getRegionDao().queryForAll();
					break;
				case Treatments:
					records = (List<ICodeTable>) (List<?>) helper.getTreatmentDao().queryForAll();
					break;
				default:
					break;
			}
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return records;
	}
	
	public List<Region> getAllRegions() {
		List<Region> regions = null;
		try {
			regions = helper.getRegionDao().queryForAll();
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return regions;
	}

	public void addCodeTableRecord(ICodeTable record, CodeTables table) {
		try {
			switch (table) {
			case Regions:
				helper.getRegionDao().create((Region) record);
				break;
			case Treatments:

				break;
			default:
				break;
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public boolean deleteCodeTableRecord(ICodeTable record, CodeTables table) {
		try {
			switch (table) {
			case Regions:
				helper.getRegionDao().delete((Region) record);
				break;
			case Treatments:

				break;
			default:
				return false;
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	public void updateCodeTableRecord(ICodeTable record, CodeTables table) {
		try {
			switch (table) {
			case Regions:
				helper.getRegionDao().update((Region) record);
				break;
			case Treatments:

				break;
			default:
				break;
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}


	// ////////////////////////////
	// End of Code Tables Functions
	// ////////////////////////////
}