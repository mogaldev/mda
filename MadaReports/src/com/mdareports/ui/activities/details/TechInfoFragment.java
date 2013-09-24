package com.mdareports.ui.activities.details;

import android.app.Activity;
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

import com.mdareports.R;
import com.mdareports.db.models.Report;
import com.mdareports.ui.custom.EditableSeekField;
import com.mdareports.utils.ApplicationUtils;
import com.mdareports.utils.DeviceInfoUtils;
import com.mdareports.utils.rangeseekbar.RangeSeekBar;
import com.mdareports.utils.rangeseekbar.RangeSeekBar.OnRangeSeekBarChangeListener;

public class TechInfoFragment extends FragmentDetailActivity {

	private EditableSeekField esFieldPulse;
	private EditableSeekField esFieldSugar;
	private EditableSeekField esFieldBreath;

	private RangeSeekBar<Integer> bloodPressureView;

	private TextView bloodPressureValue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tech_info, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		// get the container activity
		Activity activity = getActivity();
		// get the current displayed report
		Report currentReport = getCurrentReport();

		// set the fields members
		esFieldPulse = (EditableSeekField) activity
				.findViewById(R.id.esfieldPulse);
		esFieldSugar = (EditableSeekField) activity
				.findViewById(R.id.esfieldSugar);
		esFieldBreath = (EditableSeekField) activity
				.findViewById(R.id.esfieldBreath);

		// TODO: move all this blood pressure shit from here.

		// set the blood pressure views
		bloodPressureView = new RangeSeekBar<Integer>(getResources()
				.getInteger(R.integer.min_blood_pressure), getResources()
				.getInteger(R.integer.max_blood_pressure), activity);

		// Init the layout of the ploodPressureView
		RelativeLayout thisLayout = (RelativeLayout) activity
				.findViewById(R.id.bloodPressureLayout);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ApplicationUtils.getDpiFromInteger(
						getResources().getInteger(
								R.integer.blood_pressure_view_width_dimension),
						activity),
				ApplicationUtils
						.getDpiFromInteger(
								getResources()
										.getInteger(
												R.integer.blood_pressure_view_height_dimension),
								activity));
		params.setMargins(0, 25, 0, 20);
		if (DeviceInfoUtils.getDeviceLanguage(activity).equals("iw")) {
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		} else {
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
		thisLayout.addView(bloodPressureView, params);

		// Init the bloodPressureValue view by the bloodPressureView
		bloodPressureValue = (TextView) activity
				.findViewById(R.id.bloodPressureValue);
		bloodPressureValue.setText(bloodPressureView.getSelectedMinValue()
				+ ", " + bloodPressureView.getSelectedMaxValue());
		bloodPressureView
				.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {
						bloodPressureValue.setText(String.format("%d - %d",
								minValue, maxValue));
					}
				});
		bloodPressureValue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openDialogForBloodPressure(
						getString(R.string.fragment_tech_info_blood_pressure),
						bloodPressureView, bloodPressureValue);
			}
		});

		// load the details from the report into the fields
		refreshDataWithCurrentReport();
	}

	/**
	 * Add a {@link OnSeekBarChangeListener} to the {@link TextView}</br> and
	 * when the {@link SeekBar} is changed, also change the text in the
	 * {@link TextView}
	 * 
	 * @param seekBar
	 *            {@link SeekBar} to listen to
	 * @param changingTextView
	 *            The {@link TextView} that suppose to listen to the
	 *            {@link SeekBar}
	 */

	private void openDialogForBloodPressure(String title,
			final RangeSeekBar<Integer> bloodPressureViewFromFragemnt,
			final TextView bloodPressureTextValueFromFragment) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View formView = inflater.inflate(
				R.layout.dialog_seekbar_blood_pressure, null, false);

		// set the blood pressure views
		final RangeSeekBar<Integer> newBloodPressureView = new RangeSeekBar<Integer>(
				getResources().getInteger(R.integer.min_blood_pressure),
				getResources().getInteger(R.integer.max_blood_pressure),
				getActivity());
		newBloodPressureView.setSelectedMinValue(bloodPressureViewFromFragemnt
				.getSelectedMinValue());
		newBloodPressureView.setSelectedMaxValue(bloodPressureViewFromFragemnt
				.getSelectedMaxValue());

		// Init the layout of the ploodPressureView
		RelativeLayout thisLayout = (RelativeLayout) formView
				.findViewById(R.id.bloodPressureDialogLayout);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, getResources()
						.getInteger(
								R.integer.blood_pressure_view_height_dimension));
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		thisLayout.addView(newBloodPressureView, params);

		// Init the bloodPressureValue view by the bloodPressureView
		final TextView newBloodPressureText = (TextView) formView
				.findViewById(R.id.dialogBloodPressureTextView);
		newBloodPressureText.setText(bloodPressureTextValueFromFragment
				.getText());
		newBloodPressureView
				.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {
						newBloodPressureText.setText(String.format("%d - %d",
								minValue, maxValue));
					}
				});

		new AlertDialog.Builder(getActivity())
				.setView(formView)
				.setTitle(title)
				.setPositiveButton(
						R.string.fragment_tech_info_dialog_positive_button,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								bloodPressureViewFromFragemnt
										.setSelectedMinValue(newBloodPressureView
												.getSelectedMinValue());
								bloodPressureViewFromFragemnt
										.setSelectedMaxValue(newBloodPressureView
												.getSelectedMaxValue());
								bloodPressureTextValueFromFragment
										.setText(newBloodPressureText.getText());
							}
						})
				.setNegativeButton(
						R.string.fragment_tech_info_dialog_negative_button,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).show();
	}

	@Override
	public void saveCurrentReport() {
		// get the current displayed report
		Report currentReport = getCurrentReport();

		currentReport.setPulse(esFieldPulse.getValue());
		currentReport.setSugar(esFieldSugar.getValue());
		currentReport.setBreath(esFieldBreath.getValue());

		getCurrentReport().setMinBloodPressure(
				bloodPressureView.getSelectedMinValue());
		getCurrentReport().setMaxBloodPressure(
				bloodPressureView.getSelectedMaxValue());
	}

	@Override
	public void refreshDataWithCurrentReport() {
		// get the current displayed report
		Report currentReport = getCurrentReport();

		esFieldPulse.setValue(currentReport.getPulse());
		esFieldSugar.setValue(currentReport.getSugar());
		esFieldBreath.setValue(currentReport.getBreath());

		bloodPressureView.setSelectedMinValue(getCurrentReport()
				.getMinBloodPressure());
		bloodPressureView.setSelectedMaxValue(getCurrentReport()
				.getMaxBloodPressure());
	}

}
