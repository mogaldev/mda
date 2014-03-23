package com.mdareports.ui.fragments.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.mdareports.R;
import com.mdareports.ui.activities.ReportLocationActivity;
import com.mdareports.ui.activities.details.DetailsActivity;
import com.mdareports.utils.DeviceInfoUtils;

public class GeneralInfoFragment extends BaseDetailFragment {

	private EditText reportIdEditText;
	private EditText addressEditText;
	private EditText descriptionEditText;
	private EditText notesEditText;
	private CheckBox isReportedCheckBox;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_general_info, container,
				false);
	}

	@Override
	public void onStart() {
		super.onStart();

		getActivity().findViewById(R.id.btnPinLocation).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(v.getContext(),
								ReportLocationActivity.class);
						i.putExtra(ReportLocationActivity.REPORT_ID_EXTRA,
								getCurrentReport().getId());
						startActivity(i);
					}
				});

		// set the report id edit text
		reportIdEditText = (EditText) getActivity().findViewById(
				R.id.reportIdEditText);
		reportIdEditText.setText(String.valueOf(getCurrentReport()
				.getReportId()));

		// set the address edit text
		addressEditText = (EditText) getActivity().findViewById(
				R.id.addressEditText);
		addressEditText.setText(getCurrentReport().getAddress());

		// set the description edit text
		descriptionEditText = (EditText) getActivity().findViewById(
				R.id.descriptionEditText);
		descriptionEditText.setText(getCurrentReport().getDescription());

		// set the notes edit text
		notesEditText = (EditText) getActivity().findViewById(
				R.id.notesEditText);
		notesEditText.setText(getCurrentReport().getNotes());

		// set the is read check box
		isReportedCheckBox = (CheckBox) getActivity().findViewById(
				R.id.isReportedCheckBox);
				
		OnCheckedChangeListener isReportedCheckedChangedListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				int resId = (isChecked ? R.drawable.green_checked_icon
						: R.drawable.exclamation_basic_yellow);
				
				// TODO: maybe use isRtl method instead (implement one)
				if (DeviceInfoUtils.isCurrentLanguageHebrew(buttonView
						.getContext())) {
					// set drawable left
					isReportedCheckBox.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
				} else {
					// set drawable right
					isReportedCheckBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
				}

				
			}
		};	
		isReportedCheckBox.setChecked(getCurrentReport().isReported());
		// first call to onChecked change to initialize the icon
		isReportedCheckedChangedListener.onCheckedChanged(isReportedCheckBox, getCurrentReport().isReported());
		isReportedCheckBox.setOnCheckedChangeListener(isReportedCheckedChangedListener);			
	}

	@Override
	public void onResume() {	
		super.onResume();		
		
		// report information (like the address) might be changed in
		// the map activity so re-load it from the DB and refresh the fields
		((DetailsActivity)getActivity()).loadCurrentReportFromDb(getCurrentReport().getId());		
		refreshDataWithCurrentReport();
	}
	
	@Override
	public void saveCurrentReport() {
		getCurrentReport().setReportId(
				Integer.valueOf(reportIdEditText.getText().toString()));
		getCurrentReport().setAddress(addressEditText.getText().toString());
		getCurrentReport().setDescription(
				descriptionEditText.getText().toString());
		getCurrentReport().setNotes(notesEditText.getText().toString());
		getCurrentReport().setReported(isReportedCheckBox.isChecked());
	}

	@Override
	public void refreshDataWithCurrentReport() {
		reportIdEditText.setText(String.valueOf(getCurrentReport()
				.getReportId()));
		addressEditText
				.setText(getCurrentReport().getAddress());
		descriptionEditText.setText(String.valueOf(getCurrentReport()
				.getDescription()));
		notesEditText.setText(getCurrentReport().getNotes());
		isReportedCheckBox.setChecked(getCurrentReport().isReported());
	}

	@Override
	public int getTabTitleResourceId() {
		return R.string.fragment_general_info_general_info_tab_title;
	}

}
