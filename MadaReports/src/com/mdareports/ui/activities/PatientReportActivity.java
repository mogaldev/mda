package com.mdareports.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.mdareports.R;
import com.mdareports.utils.Logger;
import com.mdareports.utils.SettingsManager;

public class PatientReportActivity extends BaseActivity {

	private final String TAG = Logger.makeLogTag(this.getClass());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_report);

		// Get the action bar and set it up
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		getButton(R.id.btnSendReport).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String reportMsg = getReportMessage();

				// send the report in the native SMS application
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.setData(Uri.parse("sms:"
						+ SettingsManager.getInstance(v.getContext())
								.getSpatialTelephonyCenterNumber()));
				sendIntent.putExtra("sms_body", reportMsg);

				startActivity(sendIntent);

				Toast.makeText(v.getContext(), reportMsg, Toast.LENGTH_SHORT)
						.show();
			}
		});

	}

	private String getContent(int resId) {
		return getEditText(resId).getText().toString();
	}

	private String getReportMessage() {
		String result = "";
		String visa, address, commitment, sum, form, code, firstName, familyName;

		// get the values from the fields
		visa = getContent(R.id.txtPatientReportVisa);
		code = getContent(R.id.txtPatientReportCode);
		commitment = getContent(R.id.txtPatientReportCommitment);
		form = getContent(R.id.txtPatientReportForm);
		sum = getContent(R.id.txtPatientReportSum);
		address = getContent(R.id.txtPatientReportAddress);
		familyName = getContent(R.id.txtPatientReportFamilyName);
		firstName = getContent(R.id.txtPatientReportFirstName);

		// set the values in the report
		if (visa != "")
			result += "ויזה:" + visa + ", ";

		result += "התחייבות:" + commitment + ", ";
		result += "מספר טופס:" + form + ", ";
		result += "קוד:" + code + ", ";
		result += "סכום:" + sum + ", ";
		result += "רחוב:" + address + ", ";
		result += "שם פרטי:" + firstName + ", ";
		result += "משפחה:" + familyName;

		return result;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
