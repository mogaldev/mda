package com.madareports.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.madareports.db.models.Region;
import com.madareports.db.models.Report;
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
			List<String> allSql = new ArrayList<String>();
			switch (oldVersion) {
			case 1:
				// allSql.add("alter table AdData add column `new_col` VARCHAR");
				// allSql.add("alter table AdData add column `new_col2` VARCHAR");
			}
			for (String sql : allSql) {
				db.execSQL(sql);
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			throw new RuntimeException(e);
		}

	}

	public Dao<Report, Integer> getReportsDao() {
		if (reportsDao == null) {
			try {
				reportsDao = getDao(Report.class);
			} catch (Exception e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
		return reportsDao;
	}

	public Dao<Region, Integer> getRegions() {
		if (regionsDao == null) {
			try {
				regionsDao = getDao(Region.class);
			} catch (Exception e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
		return regionsDao;

	}

}