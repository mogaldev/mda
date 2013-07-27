package com.madareports.ui.activities;

import com.madareports.R;
import com.madareports.db.codetables.CodeTables;
import com.madareports.db.codetables.ICodeTable;
import com.madareports.db.models.Region;

public class RegionActivity extends CodeTableBaseActivity {

	public RegionActivity() {
		super.resIdDialogTitle = R.string.code_table_dialog_region_title;
		super.table = CodeTables.Regions;
	}

	@Override
	protected ICodeTable generateRecord(String content) {
		return new Region(content);
	}

}
