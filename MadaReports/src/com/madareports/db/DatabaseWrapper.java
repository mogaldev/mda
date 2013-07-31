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
import com.madareports.db.models.Treatment;
import com.madareports.db.models.TreatmentsToReports;
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

	public void addReport(Report report) {
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
	
	public Report getReportById(int id) {
		Report reportToReturn = null;
		try {
			reportToReturn = helper.getReportDao().queryForId(id);
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
	
	public List<Report> getReportByRegionId(Integer regionId) {
		List<Report> allReports = new ArrayList<Report>();
		try {
			allReports = helper.getReportDao().queryBuilder().where().eq(Report.REGION_ID_COLUMN_NAME, regionId).query();
        } catch (SQLException e) {
        	Logger.LOGE(TAG, e.getMessage());
        }
		
		return allReports;
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
	
	public List<Treatment> getAllTreatments() {
		List<Treatment> treatments = null;
		try {
			treatments = helper.getTreatmentDao().queryForAll();
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return treatments;
	}
	
	public void addCodeTableRecord(ICodeTable record, CodeTables table) {
		try {
			switch (table) {
			case Regions:
				helper.getRegionDao().create((Region) record);
				break;
			case Treatments:
				helper.getTreatmentDao().create((Treatment) record);
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
					if (!getReportByRegionId(((Region) record).getId()).isEmpty()) {
						helper.getRegionDao().delete((Region) record);
					} else {
						throw new SQLException(
						        "this region is foriegn key at some Reports");
					}
					break;
				case Treatments:
					if (!getTreatmentsToReportsByTreatmentId(((Treatment) record).getId()).isEmpty()) {
						helper.getTreatmentDao().delete((Treatment) record);
					} else {
						throw new SQLException(
						        "this treatment is foriegn key in some Reports");
					}
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
				helper.getTreatmentDao().update((Treatment) record);
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}
	
	public List<Treatment> getTreatmentsByReportId(Integer reportId) {
		List<Treatment> treatments = new ArrayList<Treatment>();
		try {
			List<TreatmentsToReports> treatmentsToReportsByReportId = getTreatmentsToReportsByReportId(reportId);
			List<Integer> TreatmentsId = new ArrayList<Integer>();
			for (TreatmentsToReports currentTreatmentsToReports : treatmentsToReportsByReportId) {
				TreatmentsId.add(currentTreatmentsToReports.getTreatment().getId());
            }
	        treatments = helper.getTreatmentDao().queryBuilder().where().in(Treatment.TREATMENT_ID_COLUMN_NAME, TreatmentsId).query();
        } catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
        }
		
		return treatments;
	}
	
	public List<Treatment> getAllOtherTreatments(Integer reportKey) {
		List<Treatment> treatments = new ArrayList<Treatment>();
		try {
			List<TreatmentsToReports> treatmentsToReports = helper.getTreatmentsToReportsDao().queryBuilder().where().in(TreatmentsToReports.REPORT_COLUMN_NAME, reportKey).query();
			List<Integer> treatmentsId = new ArrayList<Integer>();
			for (TreatmentsToReports currentTreatmentsToReports : treatmentsToReports) {
				treatmentsId.add(currentTreatmentsToReports.getTreatment().getId());
            }
			treatments = helper.getTreatmentDao().queryBuilder().where().notIn(Treatment.TREATMENT_ID_COLUMN_NAME, treatmentsId).query();
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return treatments;
	}
	
	public List<TreatmentsToReports> getTreatmentsToReportsByTreatmentId(Integer treatmentId) {
		List<TreatmentsToReports> treatmentsToReports = new ArrayList<TreatmentsToReports>();
        try {
    		// Get all the reports id's of this treatment
	        treatmentsToReports = helper.getTreatmentsToReportsDao().queryBuilder().where().eq(TreatmentsToReports.TREATMENT_COLUMN_NAME, treatmentId).query();
        } catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
        }

		return treatmentsToReports;
	}
	
	public List<TreatmentsToReports> getTreatmentsToReportsByReportId(Integer reportId) {
		List<TreatmentsToReports> treatmentsToReports = new ArrayList<TreatmentsToReports>();
        try {
    		// Get all the treatments id's of this report
	        treatmentsToReports = helper.getTreatmentsToReportsDao().queryBuilder().where().eq(TreatmentsToReports.REPORT_COLUMN_NAME, reportId).query();
        } catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
        }

		return treatmentsToReports;
	}
	
	public List<TreatmentsToReports> getTreatmentsToReportsByReportAndTreatmentId(Integer reportId, Integer TreatmentId) {
		List<TreatmentsToReports> treatmentsToReports = new ArrayList<TreatmentsToReports>();
        try {
    		// Get all the treatments id's of this report
        		List<TreatmentsToReports> TreatmentToReport = helper.getTreatmentsToReportsDao().queryBuilder().where().eq(TreatmentsToReports.REPORT_COLUMN_NAME, reportId).and().eq(TreatmentsToReports.TREATMENT_COLUMN_NAME, TreatmentId).query();
        } catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
        }

		return treatmentsToReports;
	}
	
	public void addTreatmentToReport(TreatmentsToReports treatmentsToReports) {
		try {
			// Check if there is no similar row
			List<TreatmentsToReports> query = helper.getTreatmentsToReportsDao().queryBuilder().where().eq(TreatmentsToReports.REPORT_COLUMN_NAME,
			                                                                                               treatmentsToReports.getReport().getId()).and().eq(TreatmentsToReports.TREATMENT_COLUMN_NAME,
			                                                                                                                                                 treatmentsToReports.getTreatment().getId()).query();

			// if there is no row similar, create one
			if (query.isEmpty()) {
				helper.getTreatmentsToReportsDao().create(treatmentsToReports);
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
        }
	}
	
	public void addTreatmentToReport(Report report, Treatment treatment) {
		TreatmentsToReports treatmentsToReports = new TreatmentsToReports(report, treatment);
		addTreatmentToReport(treatmentsToReports);
	}
	
	public void deleteTreatmentToReport(TreatmentsToReports treatmentsToReports) {
		try {
			helper.getTreatmentsToReportsDao().delete(treatmentsToReports);
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	// ////////////////////////////
	// End of Code Tables Functions
	// ////////////////////////////
}