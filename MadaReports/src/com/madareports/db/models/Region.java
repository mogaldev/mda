package com.madareports.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.madareports.db.codetables.ICodeTable;

@DatabaseTable(tableName = "REGIONS")
public class Region implements ICodeTable {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String region;
	
	/**
	 * String representation for the list-view display
	 */
	@Override
	public String toString() {
		return getContent();
	}
	
	// Setters & Getters
	public Region() {
		
	}

	public Region(String region) {
		setRegion(region);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String getContent() {
		return getRegion();
	}

	@Override
	public void setContent(String content) {
		setRegion(content);		
	}
}
