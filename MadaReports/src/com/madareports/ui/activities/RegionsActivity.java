package com.madareports.ui.activities;

import com.madareports.R;
import com.madareports.db.codetables.ICodeTable;
import com.madareports.db.models.Region;

public class RegionsActivity extends CodeTableBaseActivity {

	public RegionsActivity() {
		super.resIdDialogTitle = R.string.code_table_dialog_region_title;
	}

	@Override
	protected ICodeTable generateRecord(String content) {
		return new Region(content);
	}

}
