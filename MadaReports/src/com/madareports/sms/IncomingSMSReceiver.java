package com.madareports.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.madareports.MainActivity;
import com.madareports.R;
import com.madareports.utils.Logger;
import com.madareports.utils.NotificationsManager;

public class IncomingSMSReceiver extends BroadcastReceiver {
	private final String TAG = Logger.makeLogTag(getClass());

	public IncomingSMSReceiver() {
	}

	// TODO: this not use the listeners. check what is better
	private void raiseMessage(String msg, Context context) {
		String formattedString = String.format(
				context.getString(R.string.notification_d_new_messages), 5);
		NotificationsManager.getInstance(context).raiseNotification(
				formattedString, msg);

	}

	// TODO: remove - only for testing. the move method should be implement
	// inside the notification.
	private void MoveToMainActivity(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * Receiving the incoming message, filter the relevant SMS ones and raise
	 * them for treatment
	 */
	public void onReceive(Context context, Intent intent) {
		Logger.LOGE(TAG, "OnReceive called");

		// check if the incoming message is SMS
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			SmsManager sms = SmsManager.getDefault();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");

				// build the SMS messages array for the received message
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++)
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

				// iterate the sms messages that was received
				for (SmsMessage message : messages) {
					String msg = message.getMessageBody();
					String to = message.getOriginatingAddress();

					// TODO: perform filter according to the caller number
					// inform others about the incoming message
					Logger.LOGE(TAG, "Raising message");

					// treat individual message
					raiseMessage(msg, context);

					// TODO: insert the message to the database and promote
					// notification
					// insert the message to the database
					/*
					 * DatabaseFactory.getSmsDatabase(_context)
					 * .insertMessageReceived( message, msg + " -> MyTime: " +
					 * System.currentTimeMillis());
					 */
					// abortBroadcast();
				}
			}
		}
	}
}