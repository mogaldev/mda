package com.mdareports.ui.fragments.details;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdareports.R;
import com.mdareports.db.models.Report;
import com.mdareports.ui.activities.details.FragmentDetailActivity;
import com.mdareports.ui.custom.bloodpressure.BloodPressureField;
import com.mdareports.ui.custom.single.EditableSeekField;

public class TechInfoFragment extends FragmentDetailActivity {

	private EditableSeekField esFieldPulse;
	private EditableSeekField esFieldSugar;
	private EditableSeekField esFieldBreath;
	private BloodPressureField bloodPressureField;

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

		// set the fields members
		esFieldPulse = (EditableSeekField) activity
				.findViewById(R.id.esfieldPulse);
		esFieldSugar = (EditableSeekField) activity
				.findViewById(R.id.esfieldSugar);
		esFieldBreath = (EditableSeekField) activity
				.findViewById(R.id.esfieldBreath);

		bloodPressureField = (BloodPressureField) activity
				.findViewById(R.id.bloodPressureField);

		// load the details from the report into the fields
		refreshDataWithCurrentReport();

	}

	@Override
	public void saveCurrentReport() {
		// get the current displayed report
		Report currentReport = getCurrentReport();

		currentReport.setPulse(esFieldPulse.getValue());
		currentReport.setSugar(esFieldSugar.getValue());
		currentReport.setBreath(esFieldBreath.getValue());

		currentReport.setMinBloodPressure(bloodPressureField.getLowValue());
		currentReport.setMaxBloodPressure(bloodPressureField.getHighValue());

	}

	@Override
	public void refreshDataWithCurrentReport() {
		// get the current displayed report
		Report currentReport = getCurrentReport();

		esFieldPulse.setValue(currentReport.getPulse());
		esFieldSugar.setValue(currentReport.getSugar());
		esFieldBreath.setValue(currentReport.getBreath());

		bloodPressureField.setValues(currentReport.getMaxBloodPressure(),
				currentReport.getMinBloodPressure());

	}

}
