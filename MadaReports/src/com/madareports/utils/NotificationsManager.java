package com.madareports.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

import com.madareports.MainActivity;
import com.madareports.R;

public class NotificationsManager {
	private Context context;
	private static NotificationsManager instance;
	private final int NOTIFICATION_ID = 1;

	/**
	 * Get instance of the class. implements the singleton design pattern.
	 * 
	 * @return instance of the class to work with
	 */
	public static NotificationsManager getInstance(Context context) {
		if (instance == null) {
			instance = new NotificationsManager(context);
		}
		return instance;
	}

	/**
	 * Constructs the session manager object with context. The context should be
	 * the activity that holds the operations.
	 */
	private NotificationsManager(Context context) {
		this.context = context;
	}

	private void performNotification(Notification notification,
			int notificationId) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(notificationId, notification);
	}

	// TODO: move this function to BaseActivity
	public int getNewTaskFlags() {
		if (DeviceInfoUtils.hasHoneycomb()) {
			return Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK;
		}
		return Intent.FLAG_ACTIVITY_NEW_TASK;
	}

	private void removeNotification(int notificationId) {
		NotificationManager nManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nManager.cancel(notificationId);
	}

	/**
	 * Removing the permanent notification that represents the running of the
	 * application
	 */
	public void removeNotification() {
		removeNotification(NOTIFICATION_ID);
	}

	/**
	 * Raising notification that indicates that the time is up
	 */
	public void raiseNotification(String title, String description) {
		Resources resources = context.getResources();

		Notification notification = createNotification(NOTIFICATION_ID, true,
				title, description, R.drawable.ic_launcher,
				MainActivity.class);

		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// TODO: check in the settings weather to make sound and vibration or
		// not
		// treat the sound and vibration
		/*
		 * SettingsManager sm = SettingsManager.getInstance(context);
		 * android.util.Log.e("isSoundEnabled", "" + sm.isSoundnabled()); if
		 * (sm.isSoundnabled()) { notification.sound =
		 * SettingsManager.getInstance(context) .getRingtoneUri(); } if
		 * (sm.isVibrateEnabled()) { Vibrator vibrator = (Vibrator) context
		 * .getSystemService(Context.VIBRATOR_SERVICE); vibrator.vibrate(500);
		 * // long[] pattern = {0L,100L,250L,1000L,250L,500L}; //
		 * vibrator.vibrate(pattern,2); }
		 */

		performNotification(notification, NOTIFICATION_ID);

	}

	private Notification createNotification(int notificationId,
			boolean nonRemovable, String title, String content, int iconId,
			Class<?> moveToactvty) {

		// Build notification
		NotificationCompat.Builder ncBuilder = new NotificationCompat.Builder(
				context);
		ncBuilder.setContentTitle(title);
		ncBuilder.setContentText(content);
		ncBuilder.setSmallIcon(iconId);
		// ncBuilder.setContentIntent(createPendingIntent(moveToactvty));
		ncBuilder.setOngoing(nonRemovable); // no removable notification = true

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, moveToactvty);
		// creating a pendingIntent
		// the flags mean that the current task will be cleared and
		resultIntent.setFlags(getNewTaskFlags());
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
				0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		ncBuilder.setContentIntent(resultPendingIntent);

		return ncBuilder.build();
	}

}
