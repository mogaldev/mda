package com.mdareports.utils;

import android.app.Activity;
import android.view.View;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.targets.ViewTarget;

public class HelpUtils {

	public static void showHelp(View view, int resIdTitle, int resIdDescription, Activity activity) {
		// custom configuration can be here
		//  ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions(); 		
		ShowcaseView.insertShowcaseView(new ViewTarget(view), activity, resIdTitle,
				resIdDescription);
	}
	
	public static void showHelp(int resIdView, int resIdTitle, int resIdDescription, Activity activity) {
		View view = activity.findViewById(resIdView);
		if (view != null)
			showHelp(view, resIdTitle, resIdDescription, activity);
	}

}
