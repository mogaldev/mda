package com.mdareports.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mdareports.R;
import com.mdareports.sms.SmsSender;
import com.mdareports.ui.custom.patient.report.PatientReportField;

public class PatientReportFragment extends BaseFragment {
		
	private View rootView; // cached root view
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		final View rootView = inflater.inflate(R.layout.activity_patient_report,
				container, false);
		
		rootView.findViewById(R.id.btnSendReport).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String reportMsg = getReportMessage();

				// send the report in the native SMS application
				SmsSender.send(v.getContext(), reportMsg);

				Toast.makeText(v.getContext(), reportMsg, Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		// cache the root view for re-using
		this.rootView = rootView;
		
		return rootView;
	}
	
	private String getContent(int resId) {
		return ((PatientReportField) rootView.findViewById(resId)).getContent();
	}

	private String getReportMessage() {
		String result = "";
		String visa, commitment, sum, form, code, firstName, familyName;

		// get the values from the fields
		visa = getContent(R.id.prFieldVisa);
		commitment = getContent(R.id.prFieldCommitment);
		sum = getContent(R.id.prFieldSum);
		form = getContent(R.id.prFieldForm);
		code = getContent(R.id.prFieldCode);
		familyName = getContent(R.id.prFieldFamilyName);
		firstName = getContent(R.id.prFieldFirstName);

		// set the values in the report
		if (visa != "")
			result += "ויזה:" + visa + ", ";

		result += "התחייבות:" + commitment + ", ";
		result += "סכום:" + sum + ", ";
		result += "טופס:" + form + ", ";
		result += "קוד:" + code + ", ";
		result += "שם פרטי:" + firstName + ", ";
		result += "משפחה:" + familyName;

		return result;
	}
	
	
}
