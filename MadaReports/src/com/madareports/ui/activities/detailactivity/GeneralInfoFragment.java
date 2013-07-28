package com.madareports.ui.activities.detailactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.madareports.R;
import com.madareports.db.models.Report;

public class GeneralInfoFragment extends SherlockFragment {

	private Report currentReport;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_general_info, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		
		// set the report id edit text
		EditText reportIdEditText = (EditText) getActivity().findViewById(R.id.reportIdEditText);
		reportIdEditText.setText(String.valueOf(getCurrentReport().getId()));

		// set the address edit text
		EditText addressEditText = (EditText) getActivity().findViewById(R.id.addressEditText);
		addressEditText.setText(getCurrentReport().getAddress());

		// set the notes edit text
		EditText notesEditText = (EditText) getActivity().findViewById(R.id.notesEditText);
		notesEditText.setText(getCurrentReport().getNotes());
		
		// set the is watched check box
		CheckBox isWatchedCheckBox = (CheckBox) getActivity().findViewById(R.id.isWatchedCheckBox);
		isWatchedCheckBox.setChecked(getCurrentReport().isWatched());
	}

	private Report getCurrentReport() {
		if (currentReport == null) {
			DetailActivity activity = (DetailActivity) getActivity();
			currentReport = activity.getCurrentReport();
		}

		return currentReport;
	}

}
