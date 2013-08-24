package com.madareports.ui.activities.details;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.madareports.R;
import com.madareports.utils.rangeseekbar.RangeSeekBar;
import com.madareports.utils.rangeseekbar.RangeSeekBar.OnRangeSeekBarChangeListener;

public class TechInfoFragment extends FragmentDetailActivity {

	private SeekBar pulseView;
	private TextView pulseValue;
	private SeekBar sugarView;
	private TextView sugarValue;
	private SeekBar breathView;
	private TextView breathValue;
	private RangeSeekBar<Integer> bloodPressureView;
	private TextView bloodPressureValue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tech_info, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		// set the pulse views
		pulseView = (SeekBar) getActivity().findViewById(R.id.pulseView);
		pulseView.setProgress(getCurrentReport().getPulse());
		pulseValue = (TextView) getActivity().findViewById(R.id.pulseValue);
		persistSeekBarToTextView(pulseView, pulseValue);

		// set the sugar views
		sugarView = (SeekBar) getActivity().findViewById(R.id.sugarView);
		sugarView.setProgress(getCurrentReport().getSugar());
		sugarValue = (TextView) getActivity().findViewById(R.id.sugarValue);
		persistSeekBarToTextView(sugarView, sugarValue);

		// set the breath views
		breathView = (SeekBar) getActivity().findViewById(R.id.breathView);
		breathView.setProgress(getCurrentReport().getBreath());
		breathValue = (TextView) getActivity().findViewById(R.id.breathValue);
		persistSeekBarToTextView(breathView, breathValue);
		
		// set the blood pressure views
		bloodPressureView = new RangeSeekBar<Integer>(getResources().getInteger(R.integer.min_blood_pressure), getResources().getInteger(R.integer.max_blood_pressure), getActivity());
		RelativeLayout thisLayout = (RelativeLayout) getActivity().findViewById(R.id.bloodPressureLayout);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		        getResources().getInteger(R.integer.blood_pressure_view_width_dimension),
		        getResources().getInteger(R.integer.blood_pressure_view_height_dimension));
		params.setMargins(0, 25, 0, 20);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		thisLayout.addView(bloodPressureView, params);
		bloodPressureValue = (TextView) getActivity().findViewById(R.id.bloodPressureValue);
		bloodPressureValue.setText(bloodPressureView.getSelectedMinValue() + ", " + bloodPressureView.getSelectedMaxValue());
		bloodPressureView.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				bloodPressureValue.setText(String.format("%d - %d", minValue, maxValue));
		}});
		
		
	}

	/**
	 * Add a {@link OnSeekBarChangeListener} to the {@link TextView}</br>
	 * and when the {@link SeekBar} is changed, also change the text in the {@link TextView}
	 * @param seekBar {@link SeekBar} to listen to
	 * @param changingTextView The {@link TextView} that suppose to listen to the {@link SeekBar}
	 */
	private void persistSeekBarToTextView(final SeekBar seekBar, final TextView changingTextView) {
		changingTextView.setText(String.valueOf(seekBar.getProgress()));
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				changingTextView.setText(String.valueOf(progress));
			}
		});
		
		// Set the onclick listener on the textView to open the dialog
		changingTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Check what is the title that suppose to show up
				String title = null;
				switch (seekBar.getId()) {
					case R.id.pulseView:
						title = getString(R.string.pulse);
						break;
					case R.id.breathView:
						title = getString(R.string.breath);
						break;
					case R.id.sugarView:
						title = getString(R.string.sugar);
						break;
				}
				
				// Open the dialog with the correct title
				openDialogForTextView(title, seekBar);
			}
		});
	}
	
	private void openDialogForTextView(String title, final SeekBar seekBarFromFragment) {
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View formView = inflater.inflate(R.layout.dialog_seekbar, null, false);
		
		// Get the SeekBar view from the dialog view
		final SeekBar dialogSeekBarView = (SeekBar) formView.findViewById(R.id.dialogSeekBarView);
		dialogSeekBarView.setMax(seekBarFromFragment.getMax());
		dialogSeekBarView.setProgress(seekBarFromFragment.getProgress());
		
		// Get the TextView from the dialog view
		final TextView dialogTextView = (TextView) formView.findViewById(R.id.dialogTextView);
		dialogTextView.setText(String.valueOf(dialogSeekBarView.getProgress()));
		
		// Set OnSeekBarChange listener to the dialogSeekBarView
		dialogSeekBarView.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				dialogTextView.setText(String.valueOf(progress));
			}
		});
		
		new AlertDialog.Builder(getActivity())
		.setView(formView)
		.setTitle(title)				
		.setPositiveButton(R.string.tech_info_fragment_dialog_positive_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				seekBarFromFragment.setProgress(dialogSeekBarView.getProgress());
			}
		})
		.setNegativeButton(R.string.tech_info_fragment_dialog_negative_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
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
