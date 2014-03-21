package com.mdareports.ui.fragments.details;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.mdareports.R;

public class GeneralInfoFragment extends BaseDetailFragment {

	private EditText reportIdEditText;	
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
		
		// set the address edit text
		addressEditText = (EditText) getActivity().findViewById(R.id.addressEditText);
		addressEditText.setText(getCurrentReport().getAddress());

		// set the description edit text
		descriptionEditText = (EditText) getActivity().findViewById(R.id.descriptionEditText);
		descriptionEditText.setText(getCurrentReport().getDescription());

		// set the notes edit text
		notesEditText = (EditText) getActivity().findViewById(R.id.notesEditText);
		notesEditText.setText(getCurrentReport().getNotes());
		
		// set the is read check box
		isReportedCheckBox = (CheckBox) getActivity().findViewById(R.id.isReportedCheckBox);
		isReportedCheckBox.setChecked(getCurrentReport().isReported());
		
		getActivity().findViewById(R.id.showOriginalMessageButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showOriginalMessageDialog();
			}
		});
	}
	
	public void showOriginalMessageDialog() {
		final Context context = getActivity();

		// build the dialog layout
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View originalMessageDialogView = inflater.inflate(R.layout.dialog_original_message,
		                                               null, false);

		TextView originalMessageTextView = (TextView) originalMessageDialogView.findViewById(R.id.originalMessageTextView);
		originalMessageTextView.setText(getCurrentReport().getOriginalMessage());
		
		// the alert dialog
		new AlertDialog.Builder(context).setView(originalMessageDialogView).setTitle(getString(R.string.fragment_general_info_title_dialog_original_message)).show();
	}

	@Override
	public void saveCurrentReport() {
		getCurrentReport().setReportId(Integer.valueOf(reportIdEditText.getText().toString()));		
		getCurrentReport().setAddress(addressEditText.getText().toString());
		getCurrentReport().setDescription(descriptionEditText.getText().toString());
		getCurrentReport().setNotes(notesEditText.getText().toString());
		getCurrentReport().setReported(isReportedCheckBox.isChecked());
	}
	
	@Override
	public void refreshDataWithCurrentReport() {
		reportIdEditText.setText(String.valueOf(getCurrentReport().getReportId()));		
		addressEditText.setText(String.valueOf(getCurrentReport().getAddress()));
		descriptionEditText.setText(String.valueOf(getCurrentReport().getDescription()));
		notesEditText.setText(String.valueOf(getCurrentReport().getNotes()));
		isReportedCheckBox.setChecked(getCurrentReport().isReported());
	}

	@Override
	public int getTabTitleResourceId() {
		return R.string.fragment_general_info_general_info_tab_title;
	}

}
