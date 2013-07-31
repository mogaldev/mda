package com.madareports.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.madareports.db.codetables.ICodeTable;

@DatabaseTable(tableName = "TREATMENT")
public class Treatment implements ICodeTable {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String treatment;

	public Treatment() {

	}
	
	public Treatment(String treatment) {
		setTreatment(treatment);
	}

	// Setters & Getters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTreatment() {
		return this.treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	/**
	 * String representation for the list-view display
	 */
	@Override
	public String toString() {
		return getContent();
	}

	public String getContent() {
		return getTreatment();
	}

	@Override
	public void setContent(String content) {
		setTreatment(content);
	}

}
