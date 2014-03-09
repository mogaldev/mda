package com.mdareports.ui.activities.details;

import com.mdareports.R;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MadaPagerAdapter extends FragmentPagerAdapter {

	private Context context;
	
	public MadaPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new TreatmentsToReportFragment();
		case 1:
			return new TechInfoFragment();
		default:
			return new GeneralInfoFragment();
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return this.context.getString(R.string.fragment_treatments_to_report_title);
		case 1:
			return this.context.getString(R.string.fragment_tech_info_title);
		default:
			return this.context.getString(R.string.fragment_general_info_general_info_tab_title);
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

}
