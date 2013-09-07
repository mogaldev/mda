package com.madareports.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.madareports.db.models.Region;
import com.madareports.db.models.Report;
import com.madareports.db.models.Treatment;
import com.madareports.db.models.TreatmentsToReports;
import com.madareports.utils.Logger;

public class DbHelper extends OrmLiteSqliteOpenHelper {
	private String TAG = Logger.makeLogTag(getClass());

	// name of the database file for your application
	private static final String DATABASE_NAME = "MadaReportsDB.sqlite";

	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	// the dao's to access the tables
	private Dao<Report, Integer> reportsDao = null;
	private Dao<Region, Integer> regionsDao = null;
	private Dao<Treatment, Integer> treatmentDao = null;
	private Dao<TreatmentsToReports, Integer> treatmentsToReports = null;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			// Create here the database tables
			TableUtils.createTable(connectionSource, Report.class);
			TableUtils.createTable(connectionSource, Region.class);
			TableUtils.createTable(connectionSource, Treatment.class);
			TableUtils.createTable(connectionSource, TreatmentsToReports.class);

			// Set some Regions for debugging
			getRegionDao().create(new Region("מרכז"));
			getRegionDao().create(new Region("צפון"));
			getRegionDao().create(new Region("דרום"));

			// Set some Treatments for debugging
			/*
			 * getTreatmentDao().create(new Treatment("חית עין"));
			 * getTreatmentDao().create(new Treatment("הנשמה"));
			 * getTreatmentDao().create(new Treatment("טיפול 10,000"));
			 */

		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			TableUtils.createTableIfNotExists(connectionSource, Report.class);
			TableUtils.createTableIfNotExists(connectionSource, Region.class);
			TableUtils.createTableIfNotExists(connectionSource, Treatment.class);
			TableUtils.createTableIfNotExists(connectionSource, TreatmentsToReports.class);
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}

	}

	public Dao<Report, Integer> getReportDao() {
		if (reportsDao == null) {
			try {
				reportsDao = getDao(Report.class);
			} catch (Exception e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
		return reportsDao;
	}

	public Dao<Region, Integer> getRegionDao() {
		if (regionsDao == null) {
			try {
				regionsDao = getDao(Region.class);
			} catch (Exception e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
		return regionsDao;
	}

	public Dao<Treatment, Integer> getTreatmentDao() {
		if (treatmentDao == null) {
			try {
				treatmentDao = getDao(Treatment.class);
			} catch (Exception e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
		return treatmentDao;
	}

	public Dao<TreatmentsToReports, Integer> getTreatmentsToReportsDao() {
		if (treatmentsToReports == null) {
			try {
				treatmentsToReports = getDao(TreatmentsToReports.class);
			} catch (Exception e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
		return treatmentsToReports;
	}
}