package com.madareports.db;

public class MyRecord {

	public int id;
	public String title;
	public String description;

	public MyRecord() {

	}

	public MyRecord(int id, String title, String description) {
		this.title = title;
		this.description = description;
	}
	public MyRecord(int id) {
		this.title = "Title#" + id;
		this.description = "Decription#" + id;
	}
	
}
