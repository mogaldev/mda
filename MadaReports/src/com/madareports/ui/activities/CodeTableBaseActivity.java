package com.madareports.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.madareports.R;
import com.madareports.db.DatabaseWrapper;
import com.madareports.db.codetables.CodeTables;
import com.madareports.db.codetables.ICodeTable;

public abstract class CodeTableBaseActivity extends BaseActivity {
	private ListView lstRecords;
	protected int resIdDialogTitle; // the title for the dialogs of this code
									// table
	protected CodeTables table; // the enum represents the table

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_code_table);

		lstRecords = (ListView) findViewById(R.id.lstRecords);
		lstRecords.setOnItemClickListener(itemClickHandler);

		// display all the records
		displayRecords();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.codetable_activity_action_bar, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.codetable_activity_menu_add:
			addRecord();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Create location button's on click listener it will pop up the locations
	 * add form
	 */
	private void addRecord() {
		final Context context = this;

		// build the dialog
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View formElementsView = inflater.inflate(
				R.layout.dialog_code_table_form, null, false);

		// the alert dialog
		new AlertDialog.Builder(context)
				.setView(formElementsView)
				.setTitle(resIdDialogTitle)
				.setPositiveButton(
						R.string.code_table_dialog_add_positive_button,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								// get the content from the edit text
								String content = ((EditText) formElementsView
										.findViewById(R.id.txtContent))
										.getText().toString();

								// add the new record
								DatabaseWrapper.getInstance(context)
										.createCodeTableRecord(
												generateRecord(content), table);

								// display the records
								((CodeTableBaseActivity) context)
										.displayRecords();

								// tell the user it was added
								Toast.makeText(context,
										R.string.code_table_dialog_add_success,
										Toast.LENGTH_SHORT).show();

								dialog.cancel();
							}

						}).show();
	}

	/**
	 * Create new record according to the inputed content
	 * 
	 * @param content
	 *            - the content to be added
	 * @return model object that implements the {@link ICodeTable} interface
	 */
	protected abstract ICodeTable generateRecord(String content);

	/**
	 * Display the records in the list view
	 */
	private void displayRecords() {
		// set the new adapter with the updated records
		lstRecords.setAdapter(new ArrayAdapter<ICodeTable>(this,
				android.R.layout.simple_list_item_1, DatabaseWrapper
						.getInstance(this).getAll(table)));
	}

	/**
	 * Pops up dialog for edit existing record
	 * 
	 * @param record
	 *            - the record to be edited
	 * @param context
	 *            - used for the database operation and the dialog builder
	 */
	private void openEditDialog(final ICodeTable record, final Context context) {
		// inflate the edit form
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View formView = inflater.inflate(R.layout.dialog_code_table_form,
				null, false);

		final EditText txtContent = (EditText) formView
				.findViewById(R.id.txtContent);
		txtContent.setText(record.getContent());

		// the alert dialog with our form
		new AlertDialog.Builder(context)
				.setView(formView)
				.setTitle(resIdDialogTitle)
				.setPositiveButton(
						R.string.code_table_dialog_edit_positive_button,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// update the record
								record.setContent(txtContent.getText()
										.toString());
								DatabaseWrapper.getInstance(context)
										.updateCodeTableRecord(record, table);

								Toast.makeText(
										context,
										R.string.code_table_dialog_edit_success,
										Toast.LENGTH_SHORT).show();

								// display the records
								((CodeTableBaseActivity) context)
										.displayRecords();

								dialog.cancel();
							}
						}).show();
	}

	/**
	 * Handles item click. pop ups dialog with options for edit and delete.
	 * Treats also the requested operation from the user.
	 */
	private final OnItemClickListener itemClickHandler = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> myAdapter, View myView,
				int myItemInt, long mylng) {

			final Context context = myView.getContext();

			// get the selected item
			final ICodeTable selectedItem = (ICodeTable) (lstRecords
					.getItemAtPosition(myItemInt));

			// show the options in a dialog
			new AlertDialog.Builder(context)
					.setTitle(resIdDialogTitle)
					.setItems(R.array.code_table_dialog_options,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {

									switch (item) {

									case 0: // edit
										openEditDialog(selectedItem, context);
										break;

									case 1: // delete
										if (DatabaseWrapper
												.getInstance(context)
												.deleteCodeTableRecord(
														selectedItem, table)) {
											Toast.makeText(
													context,
													R.string.code_table_dialog_delete_success,
													Toast.LENGTH_SHORT).show();

											// display the records
											((CodeTableBaseActivity) context)
													.displayRecords();
										}
									default:
										break;
									}
									dialog.dismiss();
								}
							}).show();

		}
	};
}
