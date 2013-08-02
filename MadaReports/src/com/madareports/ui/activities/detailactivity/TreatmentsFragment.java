package com.madareports.ui.activities.detailactivity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.models.Treatment;
import com.madareports.db.models.TreatmentsToReports;
import com.madareports.utils.Logger;

public class TreatmentsFragment extends FragmentDetailActivity {
	
	// Lists of the Treatments of the currentReport and of the Treatments that are note connected the the currentReport
	List<Treatment> allCurrentReportTreatments;
	List<Treatment> allOtherTreatmentsToReports;
	
	// Lists of TreatmentsToReports for adding to the DB or deleting from the DB when the user click on the save button
	List<TreatmentsToReports> treatmentsIdToAdd;
	List<TreatmentsToReports> treatmentsIdToDelete;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    // Initialize the list of the treatments to show to the user
	    // This initialization is for the first time the fragment created --> when the user enter to the DetailActivity
	    // afterwards, onCreate is not called, just the onCreateView-->onStart-->etc.
	    allCurrentReportTreatments = getAllCurrentReportTreatments();
	    allOtherTreatmentsToReports = getAllOtherTreatmentsToReports();
	    
	    // Initialize the TreatmentsToReports lists
	    treatmentsIdToAdd = new ArrayList<TreatmentsToReports>();
	    treatmentsIdToDelete = new ArrayList<TreatmentsToReports>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logger.LOGD("TreatmentsFragment", "onCreateView");
		return inflater.inflate(R.layout.fragment_treatments_to_report, container, false);
	}

	@Override
	public void onStart() {
		Logger.LOGD("TreatmentsFragment", "onStart");
		super.onStart();
	
		// Init the listView of all the treatments
		ListView allTreatmentsListView = (ListView) getActivity().findViewById(R.id.all_treatments_list);
		final ArrayAdapter<Treatment> allTreatmentsListViewAdapter = getArrayAdapter(allOtherTreatmentsToReports);
		allTreatmentsListView.setAdapter(allTreatmentsListViewAdapter);

		// Init the listView of treatments of the current report
		ListView treatmentsOfReportListView = (ListView) getActivity().findViewById(R.id.treatments_of_report);
		final ArrayAdapter<Treatment> treatmentsOfReportListViewAdapter = getArrayAdapter(allCurrentReportTreatments);
		treatmentsOfReportListView.setAdapter(treatmentsOfReportListViewAdapter);

		// Init the onItemClickListeners
		allTreatmentsListView.setOnItemClickListener(getAllTreatmentsListViewOnClickListener());
		treatmentsOfReportListView.setOnItemClickListener(getTreatmentsOfReportListViewOnClickListener());
	}
	
	/**
	 * Find all the Treatments of the currentReport 
	 * @return List of all the Treatments of the currentReport
	 */
	private List<Treatment> getAllCurrentReportTreatments() {
	    int currentReportId = getCurrentReport().getId();
	    List<Treatment> treatmentsByReportId = DatabaseWrapper.getInstance(getActivity()).getTreatmentsByReportId(currentReportId);
	    
	    return treatmentsByReportId;
    }

	/**
	 * Find all the Treatments that are not connected to the currentReport
	 * @return List of all the Treatments that are not connected to the currentReport
	 */
	private List<Treatment> getAllOtherTreatmentsToReports() {
		return DatabaseWrapper.getInstance(getActivity()).getAllOtherTreatments(getCurrentReport().getId());
	}

	private OnItemClickListener getAllTreatmentsListViewOnClickListener() {
		return new OnItemClickListener() {
			@SuppressWarnings("unchecked")
            @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Find the selected Treatment
				Treatment selectedTreatment = (Treatment) parent.getItemAtPosition(position);

				// Create new row in TREATMENTS_TO_REPORTS table
				TreatmentsToReports treatmentsToReports = new TreatmentsToReports(
				        getCurrentReport(), selectedTreatment);
				// Add the TreatmentsToReports row to the add list
				treatmentsIdToAdd.add(treatmentsToReports);

				
				ListView allTreatmentsListView = (ListView) getActivity().findViewById(R.id.all_treatments_list);
				ArrayAdapter<Treatment> allTreatmentsListViewAdapter = (ArrayAdapter<Treatment>) allTreatmentsListView.getAdapter();
				ListView treatmentsOfReportListView = (ListView) getActivity().findViewById(R.id.treatments_of_report);
				ArrayAdapter<Treatment> treatmentsOfReportListViewAdapter = (ArrayAdapter<Treatment>) treatmentsOfReportListView.getAdapter();

				// update UI				
				allTreatmentsListViewAdapter.remove(selectedTreatment);
				treatmentsOfReportListViewAdapter.add(selectedTreatment);
			}
		};
	}
	
	private OnItemClickListener getTreatmentsOfReportListViewOnClickListener() {
		return new OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Find the selected Treatment
				Treatment selectedTreatment = (Treatment) parent.getItemAtPosition(position);

				// Find the treatmentToReport row in the DB
				List<TreatmentsToReports> treatmentsToReportsByReportAndTreatmentId = DatabaseWrapper.getInstance(getActivity()).getTreatmentsToReportsByReportAndTreatmentId(getCurrentReport().getId(),
				                                                                                                                                                              selectedTreatment.getId());
				
				ListView allTreatmentsListView = (ListView) getActivity().findViewById(R.id.all_treatments_list);
				ArrayAdapter<Treatment> allTreatmentsListViewAdapter = (ArrayAdapter<Treatment>) allTreatmentsListView.getAdapter();
				ListView treatmentsOfReportListView = (ListView) getActivity().findViewById(R.id.treatments_of_report);
				ArrayAdapter<Treatment> treatmentsOfReportListViewAdapter = (ArrayAdapter<Treatment>) treatmentsOfReportListView.getAdapter();

				// update UI
				treatmentsOfReportListViewAdapter.remove(selectedTreatment);
				allTreatmentsListViewAdapter.add(selectedTreatment);

				// Foreach loop because if there are two similar rows by mistake
				for (TreatmentsToReports currentTreatmentsToReports : treatmentsToReportsByReportAndTreatmentId) {
					// Add the TreatmentsToReports row to the delete list
					treatmentsIdToDelete.add(currentTreatmentsToReports);
				}
			}
		};
	}
	
	/**
	 * Create {@link ArrayAdapter} to the ListView in the tab.
	 * @param treatments List of Treatments for the adapter
	 * @return {@link ArrayAdapter} to the ListViews in the tab
	 */
	private ArrayAdapter<Treatment> getArrayAdapter(List<Treatment> treatments) {
		return new ArrayAdapter<Treatment>(
		        getActivity(),
		        android.R.layout.simple_list_item_1,
		        treatments);
	}

	@Override
	public void saveCurrentReport() {
		/**
		 * this method is not called because in the onPause callback we don't
		 * need to save a thing because we have:	
		 * 		List<TreatmentsToReports> treatmentsIdToAdd; 
		 * 		List<TreatmentsToReports> treatmentsIdToDelete;
		 * in order to know what to add and what to delete.
		 * 
		 * when the user click on the save button the DetailActivity call saveTreatments method
		 */
	}

    @Override
	public void refreshDataWithCurrentReport() {
	    allCurrentReportTreatments = getAllCurrentReportTreatments();
	    allOtherTreatmentsToReports = getAllOtherTreatmentsToReports();
	    treatmentsIdToAdd = new ArrayList<TreatmentsToReports>();
	    treatmentsIdToDelete = new ArrayList<TreatmentsToReports>();
	    
		// Init the listView of all the treatments
		ListView allTreatmentsListView = (ListView) getActivity().findViewById(R.id.all_treatments_list);
		final ArrayAdapter<Treatment> allTreatmentsListViewAdapter = getArrayAdapter(allOtherTreatmentsToReports);
		allTreatmentsListView.setAdapter(allTreatmentsListViewAdapter);

		// Init the listView of treatments of the current report
		ListView treatmentsOfReportListView = (ListView) getActivity().findViewById(R.id.treatments_of_report);
		final ArrayAdapter<Treatment> treatmentsOfReportListViewAdapter = getArrayAdapter(allCurrentReportTreatments);
		treatmentsOfReportListView.setAdapter(treatmentsOfReportListViewAdapter);

		// Init the onItemClickListeners
		allTreatmentsListView.setOnItemClickListener(getAllTreatmentsListViewOnClickListener());
		treatmentsOfReportListView.setOnItemClickListener(getTreatmentsOfReportListViewOnClickListener());
	}
    
    /**
     * Save the {@link TreatmentsToReports} Rows in the DB
     * This method is called from the {@link DetailActivity} when the user click on the save button. </br>
     */
    public void saveTreatments() {
    	for (TreatmentsToReports currTreatmentsToReports : treatmentsIdToAdd) {
	        DatabaseWrapper.getInstance(getActivity()).createTreatmentToReport(currTreatmentsToReports);
        }
    	
    	for (TreatmentsToReports currTreatmentsToReports : treatmentsIdToDelete) {
	        DatabaseWrapper.getInstance(getActivity()).deleteTreatmentToReport(currTreatmentsToReports);
        }
    }	
}