package com.mdareports.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.mdareports.R;

public class DonateUsFragment extends BaseFragment {
	private RatingBar ratingBarDonation;
	private TextView tvDonateSum;
	private ImageView imgGoodRate;
	private ImageView imgBadRate;
	private int[] donationSums = new int[] { 1, 2, 4, 5, 10 };
	private int previousDonationSum;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		changeTitle(R.string.title_donate_us);
		
		final View rootView = inflater.inflate(R.layout.fragment_donate_us,
				container, false);

		imgGoodRate = (ImageView) rootView.findViewById(R.id.imgGoodRate);
		imgBadRate = (ImageView) rootView.findViewById(R.id.imgBadRate);
		tvDonateSum = (TextView) rootView.findViewById(R.id.tvDonateSum);
		ratingBarDonation = (RatingBar) rootView
				.findViewById(R.id.ratingBarDonation);

		previousDonationSum = (int) ratingBarDonation.getRating();

		ratingBarDonation
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {

						ImageView animatedImg = (previousDonationSum > rating) ? imgBadRate
								: imgGoodRate;

						previousDonationSum = (int) rating;
						tvDonateSum.setText(donationSums[(int) rating - 1]
								+ "$");

						tvDonateSum.startAnimation(AnimationUtils
								.loadAnimation(getActivity(), R.anim.bounce));
						animatedImg.startAnimation(AnimationUtils
								.loadAnimation(getActivity(),
										R.anim.abc_fade_out));
					}
				});

		ratingBarDonation.setRating(ratingBarDonation.getMax());
		
		return rootView;
	}

}
