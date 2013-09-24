package com.mdareports.ui.custom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mdareports.R;
import com.mdareports.utils.Logger;

/**
 * 
 *
 */
public class EditableSeekField extends RelativeLayout {
	private String TAG = Logger.makeLogTag(getClass());

	private TextView tvLabel;
	private SeekBar seekBarValue;
	private TextView tvValue;
	private ImageView imgEdit;

	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public EditableSeekField(Context context, AttributeSet attrs) {
		super(context, attrs);

		// inflate the layout
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.technical_editable_seek_field, this, true);

		// set the views members
		tvLabel = (TextView) findViewById(R.id.tvLabelEditSeekField);
		seekBarValue = (SeekBar) findViewById(R.id.seekEditSeekField);
		tvValue = (TextView) findViewById(R.id.tvValueEditSeekField);
		imgEdit = (ImageView) findViewById(R.id.imgEditSeekField);

		// bind the text-view to change on each seek-bar change
		seekBarValue.setEnabled(false);
		seekBarValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvValue.setText(String.valueOf(progress));
			}
		});

		// initialize using information from the attributes
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.EditableSeekField, 0, 0);
		try {
			initialize(a.getString(R.styleable.EditableSeekField_fieldLabel),
					a.getInteger(R.styleable.EditableSeekField_maxValue, 0),
					a.getResourceId(
							R.styleable.EditableSeekField_positiveButtonText,
							R.string.ok), a.getResourceId(
							R.styleable.EditableSeekField_negativeButtonText,
							R.string.cancel));
		} finally {
			a.recycle();
		}

	}

	/**
	 * 
	 * @param label
	 * @param maxProgress
	 * @param resStrIdPositiveButton
	 * @param resStrIdNegativeButton
	 */
	public void initialize(final String label, int maxProgress,
			final int resStrIdPositiveButton, final int resStrIdNegativeButton) {
		// set the views according to the inputed parameters
		tvLabel.setText(label);
		seekBarValue.setMax(maxProgress);

		// set the edit image click handler
		imgEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditableSeekFieldDialog myLayout = new EditableSeekFieldDialog(
						getContext(), null);

				myLayout.setValue(seekBarValue.getProgress());
				myLayout.setSeekbarMax(seekBarValue.getMax());

				myLayout.openDialog(getContext(), label,
						resStrIdPositiveButton, resStrIdNegativeButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								seekBarValue.setProgress(myLayout.getValue());
							}
						});
			}
		});
	}

	public void setValue(int value) {
		seekBarValue.setProgress(value); // the textview will be updated
											// automatically
	}

	public int getValue() {
		return seekBarValue.getProgress();
	}
}
