package com.madareports.ui.activities.detailactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.madareports.R;

public class TechInfoFragment extends FragmentDetailActivity {

	private SeekBar pulseView;
	private SeekBar sugarView;
	private SeekBar breathView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tech_info, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		// set the pulse edit text
		pulseView = (SeekBar) getActivity().findViewById(R.id.pulseView);
		pulseView.setProgress(getCurrentReport().getPulse());

		// set the sugar edit text
		sugarView = (SeekBar) getActivity().findViewById(R.id.sugarView);
		sugarView.setProgress(getCurrentReport().getSugar());

		// set the breath edit text
		breathView = (SeekBar) getActivity().findViewById(R.id.breathView);
		breathView.setProgress(getCurrentReport().getBreath());
	}

	@Override
	public void save() {
		 getCurrentReport().setPulse(pulseView.getProgress());
		 getCurrentReport().setSugar(sugarView.getProgress());
		 getCurrentReport().setBreath(breathView.getProgress());
	}

}
