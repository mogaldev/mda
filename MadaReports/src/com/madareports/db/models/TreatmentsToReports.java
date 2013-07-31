package com.madareports.db.models;

import com.j256.ormlite.field.DatabaseField;

public class TreatmentsToReports {
	
	public final static String REPORT_COLUMN_NAME = "REPORT_ID";
	public final static String TREATMENT_COLUMN_NAME = "TREATMENT_ID";
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreign=true, columnName=REPORT_COLUMN_NAME)
	private Report report;
	@DatabaseField(foreign=true, columnName=TREATMENT_COLUMN_NAME)
	private Treatment treatment;
	
	public TreatmentsToReports() {
		
	}
	
	public TreatmentsToReports(Report report, Treatment treatment) {
		this.report = report;
		this.treatment = treatment;
	}
	
	// Setters and Getters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}
}
