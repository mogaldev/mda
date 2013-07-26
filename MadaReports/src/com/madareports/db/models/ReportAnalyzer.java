package com.madareports.db.models;

/**
 * analyze the received message text. All the parsing logic should appear only
 * here.
 */
public class ReportAnalyzer {// TODO turn this class logic into regular
								// expression
	private String messageBody; // the received message as it came
	private String strippedMessageBody; // the message without the irrelevant parts
	private final String[] partsToBeRemoved = { "ת.ד.",
			"*על המידע חל חיסיון רפואי ואין להעבירו" };

	// for debugging
	public static String buildFakeMessage() {
		final String prefixId = "#12";
		final String reportContent = "אמב 61 כביש 45\\עטרות למחלף בן ציון    ת.ד. פצ' קלה רוכב אופנוע שנפגע מרכב.*על המידע חל חיסיון רפואי ואין להעבירו";

		return prefixId + " " + reportContent;
	}

	public ReportAnalyzer(String messageBody) {
		this.messageBody = messageBody;
		this.strippedMessageBody = getStrippedMessage();
	}

	public int getId() {
		return Integer.parseInt(messageBody.substring(1,
				messageBody.indexOf(" ")));
	}
		
	
	public String getDisplayContent(){
		return strippedMessageBody.replace("#", "").replace("אמב 61 ", "").replaceAll("  ", " ").replaceFirst("  ", "\n");
	}

	/*
	 * The address is after the 3th whitespace until the multiple whitespaces
	 * // TODO check about case that there is no ambulance information
	 */
	

	private String getStrippedMessage() {
		String messageBodyCopy = messageBody;
		for (String part : partsToBeRemoved) {
			messageBodyCopy = messageBodyCopy.replace(part, "");
		}
		return messageBodyCopy;
	}
}
