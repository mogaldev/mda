package com.mdareports.utils;

import android.app.Activity;

import com.google.analytics.tracking.android.EasyTracker;

public class MdaAnalytics {
	
	public static void activityStart(Activity activity) {
		EasyTracker.getInstance(activity).activityStart(activity);
	}
	
	public static void activityStop(Activity activity) {
		EasyTracker.getInstance(activity).activityStop(activity);
	}
}
