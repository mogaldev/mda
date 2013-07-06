package com.madareports.db;

import java.util.ArrayList;

public class FakeDb {
	private ArrayList<MyRecord> list;

	public FakeDb() {
		list = new ArrayList<MyRecord>();

		int numOfRecords = 4;
		for (int i = 0; i < numOfRecords; i++) {
			list.add(new MyRecord(i));
		}
	}

	public ArrayList<MyRecord> getRecords() {
		return list;
	}

	public ArrayList<MyRecord> getRecords(String filter) {
		ArrayList<MyRecord> filteredList = new ArrayList<MyRecord>();

		for (MyRecord myRecord : list) {
			if (myRecord.title.contains(filter)
					|| myRecord.description.contains(filter)) {
				filteredList.add(myRecord);
			}
		}

		return filteredList;
	}

}
