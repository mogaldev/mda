package com.mdareports.utils;

import android.app.Activity;

import com.google.analytics.tracking.android.EasyTracker;

public class MdaAnalytics {
	
	private static EasyTracker getEasyTrackerInstance() {
		return EasyTracker.getInstance();
	}
	
	public static void activityStart(Activity activity) {
		getEasyTrackerInstance().activityStart(activity);
	}
	
	public static void activityStop(Activity activity) {
		getEasyTrackerInstance().activityStop(activity);
	}
}
