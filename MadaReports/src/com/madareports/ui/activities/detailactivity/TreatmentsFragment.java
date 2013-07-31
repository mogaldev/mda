package com.madareports.ui.activities.detailactivity;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.models.Treatment;
import com.madareports.db.models.TreatmentsToReports;

public class TreatmentsFragment extends FragmentDetailActivity {

	ListView allTreatmentsListView;
	ListView treatmentsOfReportListView;
	ImageButton removeTreatmentButton;
	ImageButton addTreatmentButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_treatments_to_report, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		
		// Init the listView of all the treatments
		allTreatmentsListView = (ListView) getActivity().findViewById(R.id.all_treatments_list);
		final ArrayAdapter<Treatment> allTreatmentsListViewAdapter = new ArrayAdapter<Treatment>(
		        getActivity(),
		        android.R.layout.simple_list_item_1,
		        DatabaseWrapper.getInstance(getActivity()).getAllOtherTreatments(getCurrentReport().getId()));
		allTreatmentsListView.setAdapter(allTreatmentsListViewAdapter);
		
		// Init the listView of treatments of the current report
		treatmentsOfReportListView = (ListView) getActivity().findViewById(R.id.treatments_of_report);
		final ArrayAdapter<Treatment> treatmentsOfReportListViewAdapter = new ArrayAdapter<Treatment>(getActivity(), android.R.layout.simple_list_item_1, getAllCurrentReportTreatments());
		treatmentsOfReportListView.setAdapter(treatmentsOfReportListViewAdapter);

		allTreatmentsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Find the selected Treatment
				Treatment selectedTreatment = (Treatment) parent.getItemAtPosition(position);
				
				// Create new row in TREATMENTS_TO_REPORTS table
				TreatmentsToReports treatmentsToReports = new TreatmentsToReports(
				        getCurrentReport(), selectedTreatment);
				DatabaseWrapper.getInstance(getActivity()).addTreatmentToReport(treatmentsToReports);
				
				// update UI
				allTreatmentsListViewAdapter.remove(selectedTreatment);
				treatmentsOfReportListViewAdapter.add(selectedTreatment);
			}
		});
		
		treatmentsOfReportListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Find the selected Treatment
				Treatment selectedTreatment = (Treatment) parent.getItemAtPosition(position);

				// Find the treatmentToReport row in the DB
				List<TreatmentsToReports> treatmentsToReportsByReportAndTreatmentId = DatabaseWrapper.getInstance(getActivity()).getTreatmentsToReportsByReportAndTreatmentId(getCurrentReport().getId(), selectedTreatment.getId());

				// update UI
				treatmentsOfReportListViewAdapter.remove(selectedTreatment);
				allTreatmentsListViewAdapter.add(selectedTreatment);
				
				// Foreach loop because if there are two similar rows by mistake
				for (TreatmentsToReports currentTreatmentsToReports : treatmentsToReportsByReportAndTreatmentId) {
					// Delete the treatmentToReport row
					DatabaseWrapper.getInstance(getActivity()).deleteTreatmentToReport(currentTreatmentsToReports);   
                }
			}
		});
	}
	
	private List<Treatment> getAllCurrentReportTreatments() {
	    int currentReportId = getCurrentReport().getId();
	    List<Treatment> treatmentsByReportId = DatabaseWrapper.getInstance(getActivity()).getTreatmentsByReportId(currentReportId);
	    
	    return treatmentsByReportId;
    }

	@Override
	public void save() {}

}
