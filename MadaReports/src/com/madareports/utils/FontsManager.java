package com.madareports.utils;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Utility for working with custom fonts. Currently the only way is to copy the
 * font to the asset folder (in our case to asset/fonts folder). This singleton
 * class manage caching for not creating the font from the asset every time.
 * 
 */
public class FontsManager {

	private static FontsManager instance;
	private AssetManager assetManager;
	private final Hashtable<String, Typeface> fontsCache;
	private Typeface defaultFont;

	// Fonts from the asset
	public static final String FONT_MYRIADPRO = "MYRIADPRO-REGULAR.OTF";
	public static final String FONT_SEGOEPR = "segoepr.ttf";

	private FontsManager(Context context, String defaultFontName) {
		fontsCache = new Hashtable<String, Typeface>();
		assetManager = context.getAssets();
		defaultFont = getFont(assetManager, defaultFontName);
	}

	public synchronized static FontsManager getInstance(Context context) {
		return getInstance(context, FONT_MYRIADPRO);
	}

	public synchronized static FontsManager getInstance(Context context,
			String defaultFontName) {
		if (instance == null)
			instance = new FontsManager(context, defaultFontName);
		return instance;
	}

	/**
	 * Getting the typeface of the wanted font.
	 * 
	 * @param manager
	 *            - the context.getAsset()
	 * @param name
	 *            - the font to be applied on the inputed text views. should be
	 *            one of the constants of this class.
	 * @return the typeface to be set to the widget
	 */
	private Typeface getFont(AssetManager manager, String name) {
		synchronized (fontsCache) {
			if (!fontsCache.containsKey(name)) {
				fontsCache.put(name,
						Typeface.createFromAsset(manager, "fonts/" + name));
			}
			Typeface font = fontsCache.get(name);
			return (font == null ? defaultFont : font);
		}
	}

	/**
	 * Set the default font to the inputed text views
	 * 
	 * @param activity
	 *            - the activity the view stands within.
	 * @param fontName
	 *            - the font to be applied on the inputed text views. should be
	 *            one of the constants of this class.
	 * 
	 * @param ids
	 *            - the resource ids of the text views that should be applied
	 *            with this font type face
	 */
	public void setFont(Activity activity, int... ids) {
		setFont(resIdsToTextViews(activity, ids));
	}

	public void setFont(Activity activity, String fontName, int... ids) {
		setFont(fontName, resIdsToTextViews(activity, ids));
	}

	/**
	 * Converts the resource ids to array of textviews.
	 * 
	 * @param activity
	 *            - the activity the view stands within.
	 * @param ids
	 *            - the resource ids of the text views that should be applied
	 *            with this font type face
	 * @return
	 */
	private TextView[] resIdsToTextViews(Activity activity, int... ids) {
		TextView[] tvs = new TextView[ids.length];

		// convert the ids to text views
		for (int i = 0; i < ids.length; i++) {
			tvs[i] = (TextView) activity.findViewById(ids[i]);
		}

		return tvs;
	}

	/**
	 * Set the default font to the inputed text views
	 * 
	 * @param fontName
	 *            - the font to be applied on the inputed text views. should be
	 *            one of the constants of this class.
	 * 
	 * @param tvs
	 *            - the text views that should be applied with this font type
	 *            face
	 */
	public void setFont(String fontName, TextView... tvs) {
		setFont(getFont(assetManager, fontName), tvs);
	}

	public void setFont(TextView... tvs) {
		setFont(defaultFont, tvs);
	}

	private void setFont(Typeface tf, TextView... tvs) {
		for (TextView tv : tvs) {
			tv.setTypeface(tf);
		}
	}

	/**
	 * Apply the custom font to the view group and its child elements. should be
	 * called on the root view if we want all the widgets on the screen
	 * (android.R.id.content can be helpful in some scenarios. Currently apply
	 * only on textviews but easily can be extends to other widgets in case of
	 * need.
	 */
	private void applyCustomFont(ViewGroup list, Typeface customTypeface) {
		int numOfChilds = list.getChildCount();
		for (int i = 0; i < numOfChilds; i++) {
			View view = list.getChildAt(i);
			if (view instanceof ViewGroup) {
				applyCustomFont((ViewGroup) view, customTypeface);
			} else if (view instanceof TextView) {
				((TextView) view).setTypeface(customTypeface);
			}
		}
	}

	/**
	 * Set the wanted font the this view group and all of its children
	 * 
	 * @param viewGroup
	 *            - the view group to be set
	 * @param fontName
	 *            - the font to be applied on the inputed text views. should be
	 *            one of the constants of this class.
	 */
	public void setFont(ViewGroup viewGroup, String fontName) {
		applyCustomFont(viewGroup, getFont(assetManager, fontName));
	}

	public void setFont(ViewGroup viewGroup) {
		applyCustomFont(viewGroup, defaultFont);
	}

}
