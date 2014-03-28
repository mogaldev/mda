package com.mdareports.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mdareports.R;
import com.mdareports.utils.billing.BillingUtils;

public class HomeFragment extends BaseFragment {
	BillingUtils billingUtils;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Activity activity = getActivity();
		if (resultCode == activity.RESULT_OK) {
			Toast.makeText(activity, "Thank you!", Toast.LENGTH_LONG).show();

			if (billingUtils != null)
				billingUtils.consumePurchasedItems(data);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		billingUtils = new BillingUtils(getActivity());
		billingUtils.bind();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_home,
				container, false);

		Button btnBuy = (Button) rootView.findViewById(R.id.btnBuy);
		btnBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (billingUtils != null)
					billingUtils.buy("abc");
			}
		});

		return rootView;
	}

}
