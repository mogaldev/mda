package com.madareports.ui.activities;

import com.madareports.R;
import com.madareports.db.codetables.CodeTables;
import com.madareports.db.codetables.ICodeTable;
import com.madareports.db.models.Treatment;

public class TreatmentsActivity extends CodeTableBaseActivity {

	public TreatmentsActivity() {
		super.resIdDialogTitle = R.string.code_table_dialog_treatment_title;
		super.table = CodeTables.Treatments;
	}

	@Override
	protected ICodeTable generateRecord(String content) {
		return new Treatment(content);
	}

}
