package com.mdareports.utils;

import android.app.Activity;
import android.content.Context;

import com.google.analytics.tracking.android.EasyTracker;

public class MdaAnalytics {
	
	private static EasyTracker getEasyTrackerInstance(Context context) {
		return EasyTracker.getInstance(context);
	}
	
	public static void activityStart(Activity activity) {
		getEasyTrackerInstance(activity).activityStart(activity);
	}
	
	public static void activityStop(Activity activity) {
		getEasyTrackerInstance(activity).activityStop(activity);
	}
}
