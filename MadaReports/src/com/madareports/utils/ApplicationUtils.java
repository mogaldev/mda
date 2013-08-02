package com.madareports.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

public class ApplicationUtils {
	
	/**
	 * Check if the current application is in the foreground
	 * @param appContext
	 * @return
	 */
	public static boolean isApplicationInForeground(Context appContext) {
		ActivityManager activityManager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> services = activityManager
	            .getRunningTasks(Integer.MAX_VALUE);
	    boolean isActivityFound = false;

	    if (services.get(0).topActivity.getPackageName().toString()
	            .equalsIgnoreCase(appContext.getPackageName().toString())) {
	        isActivityFound = true;
	    }

	    return isActivityFound;
	}

}
