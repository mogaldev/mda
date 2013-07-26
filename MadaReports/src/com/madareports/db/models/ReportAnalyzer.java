package com.madareports.db.models;

/**
 * analyze the received message text. All the parsing logic should appear only
 * here.
 */
public class ReportAnalyzer {// TODO turn this class logic into regular
								// expression
	private String messageBody; // the received message as it came
	private String strippedMessageBody; // the message without the irrelevant parts
	private final String[] partsToBeRemoved = { "�.�.",
			"*�� ����� �� ������ ����� ���� �������" };

	// for debugging
	public static String buildFakeMessage() {
		final String prefixId = "#12";
		final String reportContent = "��� 61 ���� 45\\����� ����� �� ����    �.�. ��' ��� ���� ������ ����� ����.*�� ����� �� ������ ����� ���� �������";

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
		return strippedMessageBody.replace("#", "").replace("��� 61 ", "").replaceAll("  ", " ").replaceFirst("  ", "\n");
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
