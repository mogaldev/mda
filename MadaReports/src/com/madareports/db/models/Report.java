package com.madareports.db.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.reports.ReportAnalyzer;
import com.madareports.db.reports.ReportIllustrator;
import com.madareports.utils.ApplicationUtils;

@DatabaseTable(tableName = Report.TABLE_NAME)
public class Report {

	public static final String TABLE_NAME = "reports";
	public static final String REGION_ID_COLUMN_NAME = "regionId";
	public static final String ID_COLUMN_NAME = "id";
	public static final String IS_READ_COLUMN_NAME = "isRead";	

	@DatabaseField(generatedId = true, columnName = ID_COLUMN_NAME)
	private int id;
	@DatabaseField
	private int reportId;
	@DatabaseField
	private String address;
	@DatabaseField
	private String description;
	@DatabaseField(foreign = true, columnName = REGION_ID_COLUMN_NAME)
	private Region region;
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
	private boolean isRead; // if the user saw this report. NOTE: Dont change
							// the variable name. the countNewReports() rely
							// on this name right now.
	@DatabaseField
	private Date receivedAt; // TODO: added. update in specifications.
	@DatabaseField
	private String originalMessage;

	public Report(Context ctx, String messageBody, long timesptamp) {
		// TODO: currently simulate the description manually
		messageBody = new ReportIllustrator().getFakeReport();
		ReportAnalyzer rprtAnlzr = new ReportAnalyzer(messageBody);

		// get the id from the message
		setReportId(rprtAnlzr.getId());
		setDescription(rprtAnlzr.getDescription()); // TODO cut the message body
		setAddress(rprtAnlzr.getAddress());

		// set random things for debugging
		Random rnd = new Random();
		setReceivedAt(new Date(timesptamp));
		setRegion(DatabaseWrapper.getInstance(ctx).getAllRegions().get(0));
		setPulse(rnd.nextInt(100));
		setBreath(rnd.nextInt(100));
		setSugar(rnd.nextInt(100));
		setMinBloodPressure(rnd.nextInt(50));
		setMaxBloodPressure(rnd.nextInt(50) + 51);
		setRead(false);
	}

	public Report() {
		super();
	}

	/**
	 * Get string representation for sharing
	 * 
	 * @param context
	 *            - context for getting the string resources
	 * @return string represents the report for the share description
	 */
	public String toShareString(Context context) {
		String result;

		// get the resources for the strings
		Resources resources = context.getResources();

		result = "#" + getReportId() + " : ";
		result += resources.getString(R.string.address) + ": " + getAddress() + "\n";
		result += resources.getString(R.string.description) + ": " + getDescription() +
		          "\n";
		result += resources.getString(R.string.region) + ": " + DatabaseWrapper.getInstance(context).getRegionById(getRegion().getId()).getRegion() +
		          "\n";
		result += new SimpleDateFormat("E dd-MM-yyyy hh:mm").format(getReceivedAt()).toString() +
		          "\n";
		// technical details
		result += resources.getString(R.string.pulse) + ": " + getPulse() + "\n";
		result += resources.getString(R.string.blood_pressure) + ": " +
		          getMinBloodPressure() + " \\ " + getMaxBloodPressure() + "\n";
		result += resources.getString(R.string.sugar) + ": " + getSugar() + "\n";
		result += resources.getString(R.string.notes) + ": " + getNotes() + "\n";

		return result;
	}

	// Setters & Getters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public String getAddress() {
		return ApplicationUtils.NVL(address);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return ApplicationUtils.NVL(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
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
		return ApplicationUtils.NVL(notes);
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

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public Date getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(Date receivedAt) {
		this.receivedAt = receivedAt;
	}

	public String getOriginalMessage() {
		return ApplicationUtils.NVL(originalMessage);
	}

	public void setOriginalMessage(String originalMessage) {
		this.originalMessage = originalMessage;
	}

}
