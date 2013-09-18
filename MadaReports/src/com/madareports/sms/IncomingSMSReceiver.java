package com.madareports.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.models.Report;
import com.madareports.db.reports.ReportAnalyzer;
import com.madareports.utils.ApplicationUtils;
import com.madareports.utils.NotificationsManager;
import com.madareports.utils.SettingsManager;

public class IncomingSMSReceiver extends BroadcastReceiver {
	static StringBuilder bufferedMsg = new StringBuilder();
	static boolean isFinishReceiving = true;

	private void raiseMessage(String smsMessageBody, long timestampMillies,
			Context context) {
		Report report = new Report(context, smsMessageBody, timestampMillies);

		// add the report to the database
		DatabaseWrapper dbWrpr = DatabaseWrapper.getInstance(context);
		dbWrpr.createReport(report);

		// notify about the new report with the number of unread reports
		String formattedString = String.format(
				context.getString(R.string.notification_d_new_messages),
				dbWrpr.countUnreadReports());

		if (!ApplicationUtils.isApplicationInForeground(context)) {
			NotificationsManager.getInstance(context)
					.raiseSmsReceivedNotification(formattedString,
							report.getDescription());
		}
	}

	/**
	 * Check if the message came from the relevant sender
	 * 
	 * @param smsMsg
	 *            - the message to be checked
	 * @return True if the message is relevant and should be treated, False
	 *         otherwise
	 */
	boolean isRelevantSms(String smsMessgeBody) {
		// TODO: check by the message structure. should be from private number
		// (check if it could be detected) with specific scheme.
		// return true;
		return ReportAnalyzer.isRelevantMessage(smsMessgeBody);
	}

	/**
	 * Receiving the incoming message, filter the relevant SMS ones and raise
	 * them for treatment
	 */
	public void onReceive(Context context, Intent intent) {
		// check if the incoming message is SMS
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			isFinishReceiving = true;

			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");

				// reassemble the PDUs into array of SMS messages
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++)
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

				StringBuilder messageBodyBuilder = new StringBuilder();

				// iterate the SMS messages that was received
				for (SmsMessage message : messages) {
					String msgBody = message.getMessageBody();

					if (isMultipartMessage(msgBody)) {
						bufferedMsg
								.append(getMsgWithoutMultipartPrefix(msgBody));

						if (isEndMultipartMessage(msgBody)) {
							messageBodyBuilder.append(bufferedMsg);
							bufferedMsg = new StringBuilder();
							isFinishReceiving = true;
						} else {
							isFinishReceiving = false;
						}
					} else {
						messageBodyBuilder.append(msgBody);
					}
				}

				if (isFinishReceiving) {
					String messageBody = messageBodyBuilder.toString();
					// check if the message is relevant and pass it on
					if (isRelevantSms(messageBody)) {
						raiseMessage(messageBody,
								messages[0].getTimestampMillis(), context);

						if (SettingsManager.getInstance(context)
								.getAbortBroadcast()) {
							abortBroadcast();
						}
					}
				}
			}
		}
	}

	/**
	 * Strip out the multipart prefix from an inputed message
	 * 
	 * @param msg
	 *            - the message to get the information from
	 * @return copy of the message without the multipart prefix
	 */
	private static String getMsgWithoutMultipartPrefix(String msg) {
		int indexOfFirstSpace = msg.indexOf(' ');
		return msg.substring(indexOfFirstSpace + 1, msg.length() - 1);
	}

	/**
	 * Check if the message is the last one in the multi-part sequence. Based on
	 * the x/y strtcture the x should be equal to y in the last message
	 * 
	 * @param msg
	 *            - the message to be checked. must contains "x/y " at the
	 *            beginning.
	 * @return True if it is the last message, False otherwise
	 */
	private static boolean isEndMultipartMessage(String msg) {
		int indexOfSlash = msg.indexOf('/');
		return msg.substring(0, indexOfSlash).equals(
				msg.substring(indexOfSlash + 1, msg.indexOf(' ')));
	}

	/**
	 * Checks if the message is multi-part. The detection depends on the
	 * structure "x/y ". so we will pass to this function sub string of the
	 * message's body from the beginning until the first space/
	 * 
	 * @param msgUntilFirstSpace
	 *            the prefix of the message body (until the first space)
	 * @return True if the message is multi-part, False otherwise
	 */
	private static boolean isMultipartMessage(String msgUntilFirstSpace) {
		// should start with int/int (space)
		return msgUntilFirstSpace.matches("\\d/\\d .+");
	}

}