package com.madareports.db.models;

import java.util.Date;

import android.telephony.SmsMessage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Report {

	@DatabaseField
	private String title;
	@DatabaseField
	private String description;

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String address;
	// TODO: add region
	@DatabaseField
	private int pulse;
	@DatabaseField
	private int minBloodPressure;
	@DatabaseField
	private int maxBloodPressure;
	@DatabaseField
	private int breath;
	@DatabaseField
	private int sugar;
	@DatabaseField
	private String notes;
	@DatabaseField
	private boolean isReported; // if the user report this record on the website
	@DatabaseField
	private boolean isWatched; // if the user saw this report. NOTE: Dont change
								// the variable name. the countNewReports() rely
								// on this name right now.
	@DatabaseField
	private Date receivedAt; // TODO: added. update in specifications.

	public Report(String messageBody, long timesptamp) {
		// TODO: currently simulate the description manually
		messageBody = new ReportIllustrator().getFakeReport();
		ReportAnalyzer rprtAnlzr = new ReportAnalyzer(messageBody);

		// get the id from the message
		setId(rprtAnlzr.getId());
		setDescription(rprtAnlzr.getDisplayContent()); // TODO cut the message
														// body
		setReceivedAt(new Date(timesptamp));

	}

	public Report() {
		super();
	}

	// Setters & Getters

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPulse() {
		return pulse;
	}

	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

	public int getMinBloodPressure() {
		return minBloodPressure;
	}

	public void setMinBloodPressure(int minBloodPressure) {
		this.minBloodPressure = minBloodPressure;
	}

	public int getMaxBloodPressure() {
		return maxBloodPressure;
	}

	public void setMaxBloodPressure(int maxBloodPressure) {
		this.maxBloodPressure = maxBloodPressure;
	}

	public int getBreath() {
		return breath;
	}

	public void setBreath(int breath) {
		this.breath = breath;
	}

	public int getSugar() {
		return sugar;
	}

	public void setSugar(int sugar) {
		this.sugar = sugar;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isReported() {
		return isReported;
	}

	public void setReported(boolean isReported) {
		this.isReported = isReported;
	}

	public boolean isWatched() {
		return isWatched;
	}

	public void setWatched(boolean isWatched) {
		this.isWatched = isWatched;
	}

	public Date getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(Date receivedAt) {
		this.receivedAt = receivedAt;
	}

}
