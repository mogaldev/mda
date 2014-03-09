package com.mdareports.ui.activities.details;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;

public class MadaTabListener implements TabListener {
	
	ViewPager viewPager;
	
    public MadaTabListener(ViewPager viewPager) {
    	this.viewPager = viewPager;
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    	this.viewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
}
