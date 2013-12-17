package com.mdareports.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.mdareports.R;

public class AboutUsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);

		// Get the action bar and set it up
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		getImageButton(R.id.imgEmailUs).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// send email to our account
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"MoGal.Development@gmail.com"});			
				try {
				    startActivity(Intent.createChooser(i, "Send email..."));
				} catch (android.content.ActivityNotFoundException ex) {
					writeShortTimeMessage(R.string.about_us_no_email_clients_installed);				    
				}
			}
		});

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
