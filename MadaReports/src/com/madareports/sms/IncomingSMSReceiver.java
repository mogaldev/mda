package com.madareports.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.madareports.FilterListViewTests;
import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.models.Report;
import com.madareports.utils.Logger;
import com.madareports.utils.NotificationsManager;

public class IncomingSMSReceiver extends BroadcastReceiver {
	private final String TAG = Logger.makeLogTag(getClass());

	private void raiseMessage(SmsMessage smsMsg, Context context) {		
		Report report = new Report(smsMsg);
		// add the report to the database
		DatabaseWrapper dbWrpr = DatabaseWrapper.getInstance(context);
		dbWrpr.AddReport(report);

		// notify about the new report with the number of unread reports
		String formattedString = String.format(
				context.getString(R.string.notification_d_new_messages),
				dbWrpr.countUnreadReports());
		NotificationsManager.getInstance(context).raiseNotification(
				formattedString, report.getDescription());

	}

	/**
	 * Check if the message came from the relevant sender
	 * 
	 * @param smsMsg
	 *            - the message to be checked
	 * @return True if the message is relevant and should be treated, False
	 *         otherwise
	 */
	boolean isRelevantSms(SmsMessage smsMsg) {
		final String madaSender = "1234"; // TODO: replace with the real sender
											// number. Check about getting the
											// number from settings

		
		Logger.LOGE(TAG, "Display: " + smsMsg.getDisplayOriginatingAddress() + "; Regular: " + smsMsg.getOriginatingAddress());
		
		// TODO: check by the message structure. should be from private number
		// (check if it could be detected) with specific scheme.
		return (smsMsg.getOriginatingAddress().equals(madaSender));
	}

	// TODO: remove - only for testing. the move method should be implement
	// inside the notification.
	private void MoveToMainActivity(Context context) {
		Intent intent = new Intent(context, FilterListViewTests.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * Receiving the incoming message, filter the relevant SMS ones and raise
	 * them for treatment
	 */
	public void onReceive(Context context, Intent intent) {
		// check if the incoming message is SMS
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");

				// reassemble the PDUs into array of SMS messages
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++)
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

				// iterate the SMS messages that was received
				for (SmsMessage message : messages) {
					String msg = message.getMessageBody();

					// check if the message is relevant and pass it on
					if (isRelevantSms(message)) {
						raiseMessage(message, context);
					}

					// abortBroadcast();
				}
			}
		}
	}
}