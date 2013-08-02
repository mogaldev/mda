package com.madareports.ui.activities.detailactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.madareports.R;

public class TechInfoFragment extends FragmentDetailActivity {

	private SeekBar pulseView;
	private TextView pulseValue;
	private SeekBar sugarView;
	private TextView sugarValue;
	private SeekBar breathView;
	private TextView breathValue;

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
		pulseValue = (TextView) getActivity().findViewById(R.id.pulseValue);
		persistSeekBarToTextView(pulseView, pulseValue);

		// set the sugar edit text
		sugarView = (SeekBar) getActivity().findViewById(R.id.sugarView);
		sugarView.setProgress(getCurrentReport().getSugar());
		sugarValue = (TextView) getActivity().findViewById(R.id.sugarValue);
		persistSeekBarToTextView(sugarView, sugarValue);

		// set the breath edit text
		breathView = (SeekBar) getActivity().findViewById(R.id.breathView);
		breathView.setProgress(getCurrentReport().getBreath());
		breathValue = (TextView) getActivity().findViewById(R.id.breathValue);
		persistSeekBarToTextView(breathView, breathValue);
	}

	/**
	 * Add a {@link OnSeekBarChangeListener} to the {@link TextView}</br>
	 * and when the {@link SeekBar} is changed, also change the text in the {@link TextView}
	 * @param seekBar {@link SeekBar} to listen to
	 * @param changingTextView The {@link TextView} that suppose to listen to the {@link SeekBar}
	 */
	private void persistSeekBarToTextView(SeekBar seekBar, final TextView changingTextView) {
		changingTextView.setText(String.valueOf(seekBar.getProgress()));
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				changingTextView.setText(String.valueOf(progress));
			}
		});
	}

	@Override
	public void saveCurrentReport() {
		 getCurrentReport().setPulse(pulseView.getProgress());
		 getCurrentReport().setSugar(sugarView.getProgress());
		 getCurrentReport().setBreath(breathView.getProgress());
	}
	
	@Override
	public void refreshDataWithCurrentReport() {
		 pulseView.setProgress(getCurrentReport().getPulse());
		 sugarView.setProgress(getCurrentReport().getSugar());
		 breathView.setProgress(getCurrentReport().getBreath());
	}

}
