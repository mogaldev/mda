package com.madareports.ui.activities.details;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.models.Region;

public class GeneralInfoFragment extends FragmentDetailActivity {

	private EditText reportIdEditText;
	private Spinner regionSpinner;
	private EditText addressEditText;
	private EditText descriptionEditText;
	private EditText notesEditText;
	private CheckBox isReportedCheckBox; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_general_info, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		
		// set the report id edit text
		reportIdEditText = (EditText) getActivity().findViewById(R.id.reportIdEditText);
		reportIdEditText.setText(String.valueOf(getCurrentReport().getReportId()));
		
		// set the regions spinner
		List<Region> allRegions = DatabaseWrapper.getInstance(getActivity()).getAllRegions();
		ArrayAdapter<Region> dataAdapter = new ArrayAdapter<Region>(getActivity(), android.R.layout.simple_spinner_item, allRegions);
		regionSpinner = (Spinner) getActivity().findViewById(R.id.regionsSpinner);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		regionSpinner.setAdapter(dataAdapter);
		regionSpinner.setSelection(findRegionPositionForReport(allRegions,
		                                                       getCurrentReport().getRegion().getId()));
		
		// set the address edit text
		addressEditText = (EditText) getActivity().findViewById(R.id.addressEditText);
		addressEditText.setText(getCurrentReport().getAddress());

		// set the description edit text
		descriptionEditText = (EditText) getActivity().findViewById(R.id.descriptionEditText);
		descriptionEditText.setText(getCurrentReport().getDescription());

		// set the notes edit text
		notesEditText = (EditText) getActivity().findViewById(R.id.notesEditText);
		notesEditText.setText(getCurrentReport().getNotes());
		
		// set the is watched check box
		isReportedCheckBox = (CheckBox) getActivity().findViewById(R.id.isWatchedCheckBox);
		isReportedCheckBox.setChecked(getCurrentReport().isReported());
	}
	
	/**
	 * Find the position of a {@link Region} in a list of {@link Region} by a {@link Region} ID
	 * @param allRegions List of Region
	 * @param regionId Id of the {@link Region} to find in the list
	 * @return Position of the {@link Region} in the List
	 */
	private int findRegionPositionForReport(List<Region> allRegions, Integer regionId) {
		int index = 0;
		for (Region region : allRegions) {
			if (region.getId() == regionId) {
				break;
			} else {
				index++;
			}
		}

		return index;
	}

	@Override
	public void saveCurrentReport() {
		getCurrentReport().setReportId(Integer.valueOf(reportIdEditText.getText().toString()));
		getCurrentReport().setRegion((Region) regionSpinner.getSelectedItem());
		getCurrentReport().setAddress(addressEditText.getText().toString());
		getCurrentReport().setDescription(descriptionEditText.getText().toString());
		getCurrentReport().setNotes(notesEditText.getText().toString());
		getCurrentReport().setReported(isReportedCheckBox.isChecked());
	}
	
	@Override
	public void refreshDataWithCurrentReport() {
		reportIdEditText.setText(String.valueOf(getCurrentReport().getReportId()));
		regionSpinner.setSelection(findRegionPositionForReport(DatabaseWrapper.getInstance(getActivity()).getAllRegions(),
		                                                       getCurrentReport().getRegion().getId()));
		addressEditText.setText(String.valueOf(getCurrentReport().getAddress()));
		descriptionEditText.setText(String.valueOf(getCurrentReport().getDescription()));
		notesEditText.setText(String.valueOf(getCurrentReport().getNotes()));
		isReportedCheckBox.setChecked(getCurrentReport().isReported());
	}

}
