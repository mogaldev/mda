package com.mdareports.ui.activities.details;

import com.mdareports.ui.fragments.details.GeneralInfoFragment;
import com.mdareports.ui.fragments.details.TechInfoFragment;
import com.mdareports.ui.fragments.details.TreatmentsToReportFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MadaPagerAdapter extends FragmentPagerAdapter {

	private FragmentDetailActivity[] array = new FragmentDetailActivity[3];
	
	public MadaPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		
		// Init the tabs with the Fragments of Mda
		array[0] = new TreatmentsToReportFragment();
		array[1] = new TechInfoFragment();
		array[2] = new GeneralInfoFragment();
	}

	@Override
	public Fragment getItem(int position) {
		return array[position];
	}

	@Override
	public int getCount() {
		return 3;
	}

}
