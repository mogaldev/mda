//package com.madareports.ui.activities;
//
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.res.Resources;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.MenuItem.OnMenuItemClickListener;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.FrameLayout;
//import android.widget.PopupMenu;
//
//import com.madareports.R;
//
//public abstract class MadaBaseActivity extends BaseActivity implements
//		onTabChangedListener, OnMenuItemClickListener {
//
//	private int menuButtonSelectedColor;
//	private int menuButtonUnSelectedColor;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		// because the switch widget there is need to use R.style.AppThemeLight
//		// or R.style.AppThemeDark defined in the values/styles.xml file
//		// setTheme(R.style.MeatOrMilkDark);
//
//		// set all the activities lock in portrait mode
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//		// cache the color for not getting them each button click
//		Resources r = getResources();
//		menuButtonSelectedColor = r
//				.getColor(R.color.menu_button_background_color_selected);
//		menuButtonUnSelectedColor = r
//				.getColor(R.color.menu_button_background_color_unselected);
//
//	}
//
//	/**
//	 * determine the application status (foreground/background)
//	 */
//	@Override
//	protected void onStart() {
//		super.onStart();
//		boolean isRunning = EatingServiceManager.getInstance(this).isRunning();
//		Log.d("onStart-isRunning", isRunning + "");
//		try {
//			MenuFragment menu = (MenuFragment) getSupportFragmentManager()
//					.findFragmentById(R.id.footer_menu_fragment);
//			menu.setOnTabChangedListener(this);
//			menu.registerForContextMenu(findViewById(R.id.menu_unknown));
//		} catch (NullPointerException e) {
//			Log.e(this.getClass().getName(),
//					"No footer_menu_fragment exsits, please add include layout footer.xml");
//		}
//
//		// create the admob for each activity
//		// you can remove the admob from an activity: use removeAdMob()
//
//		// create the admob view to be inserted
//		AdView adView = (AdView) findViewById(R.id.admob);
//		// check if the screen has the adMob component
//		if (adView != null) {
//			AdRequest adRequest = new AdRequest();
//			// adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
//			// to get the device id in the LogCat
//			// adRequest.addTestDevice("TEST_DEVICE_ID");
//			// moshe
//			adRequest.addTestDevice("78981482D2382FC8E235C886E7E8F517");
//			// gal
//			adRequest.addTestDevice("0BA4FE808CA15A0621E3DB999999615A");
//
//			Log.d("Is Test Device", adRequest.isTestDevice(this) + "");
//
//			// Initiate a generic request to load it with an ad
//			adView.loadAd(adRequest);
//		}
//	}
//
//	/**
//	 * determine the application status (foreground/background)
//	 */
//	@Override
//	protected void onStop() {
//		super.onStop();
//		Log.d("onStop-isRunning", EatingServiceManager.getInstance(this)
//				.isRunning() + "");
//		// android.util.Log.d("BASE_ACTVTY", getClass().getName());
//	}
//
//	@Override
//	protected void MoveTo(Class<?> actvtyClass, Integer flags) {
//		if (actvtyClass.getName().equals(getSettingsActivityClass().getName())
//				|| actvtyClass.getName().equals(
//						SoundsWebViewActivity.class.getName())
//				|| actvtyClass.getName().equals(
//						MeatOrMilkPreferenceActivity.class.getName())) {
//			flags = Intent.FLAG_ACTIVITY_NO_ANIMATION;
//		}
//
//		super.MoveTo(actvtyClass, flags);
//	}
//
//	public void MoveTo(Class<?> actvtyClass) {
//		MoveTo(actvtyClass, Intent.FLAG_ACTIVITY_NO_ANIMATION
//				| Intent.FLAG_ACTIVITY_NO_HISTORY);
//	}
//
//	// /////////////////////////////////////////
//	// --- Tabs Menu ---
//	// /////////////////////////////////////////
//
//	/**
//	 * Methods for managing the selection of buttons and mark it by its
//	 * background color
//	 */
//	private void unSelectAllMenuButtons() {
//		setMenuButtonUnSelected(R.id.menu_ringtone_button_id);
//		setMenuButtonUnSelected(R.id.menu_products_button_id);
//		setMenuButtonUnSelected(R.id.menu_giftapp_button_id);
//	}
//
//	protected void setMenuButtonSelected(int menuButtonResId) {
//		findViewById(menuButtonResId).setBackgroundColor(
//				menuButtonSelectedColor);
//	}
//
//	private void setMenuButtonUnSelected(int menuButtonResId) {
//		findViewById(menuButtonResId).setBackgroundColor(
//				menuButtonUnSelectedColor);
//	}
//
//	@Override
//	public void onTabChanged(View v) {
//		Class<?> actvtyClass = null;
//		int id = v.getId();
//
//		// unselect all the menu button. the relevant button will be marked by
//		// its activity
//
//		switch (id) {
//		case R.id.menu_ringtone_button_id:
//			actvtyClass = SoundsWebViewActivity.class;
//			unSelectAllMenuButtons();
//			break;
//		case R.id.menu_products_button_id:
//			actvtyClass = ProductsWebViewActivity.class;
//			unSelectAllMenuButtons();
//			break;
//		case R.id.menu_giftapp_button_id:
//			actvtyClass = GiftAppWebviewActivity.class;
//			unSelectAllMenuButtons();
//			break;
//		case R.id.menu_unknown:
//			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//				openSharePopupMenu(v);
//			} else {
//				openShareContextMenu(v);
//			}
//			break;
//		default:
//			break;
//		}
//
//		if (actvtyClass != null) {
//			Log.e("MoveToMenu", actvtyClass.toString());
//			MoveTo(actvtyClass, Intent.FLAG_ACTIVITY_NO_ANIMATION
//					| Intent.FLAG_ACTIVITY_NO_HISTORY);
//		}
//
//	}
//
//	private void openSharePopupMenu(View v) {
//		PopupMenu popup = new PopupMenu(this, v);
//		MenuInflater inflater = popup.getMenuInflater();
//		popup.setOnMenuItemClickListener(this);
//		inflater.inflate(R.menu.mom_menu, popup.getMenu());
//		popup.show();
//	}
//
//	@Override
//	public boolean onMenuItemClick(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.sub_menu_share_app_id:
//			// set the share action for api 14 and above
//			// ((ShareActionProvider)item.getActionProvider()).setShareIntent(getDefaultShareIntent());
//
//			// better use this to set the share action because it's for api
//			// below 14
//			startActivity(Intent.createChooser(getDefaultShareIntent(),
//					getResources().getString(R.string.sub_menu__share_app)));
//			return true;
//		case R.id.sub_menu_settings:
//			MoveTo(getSettingsActivityClass());
//			return true;
//
//		case R.id.sub_menu_like_us:
//			goToUrl("http://www.facebook.com");
//			return true;
//
//		case R.id.sub_menu_rate_us:
//			goToUrl("market://details?id=" + "meat.or.milk");
//			return true;
//
//		case R.id.sub_menu_get_ringtone:
//			MoveTo(SoundsWebViewActivity.class);
//			return true;
//
//		case R.id.sub_menu_info:
//			MoveTo(AboutWebviewActivity.class);
//			return true;
//
//		default:
//			return super.onContextItemSelected(item);
//		}
//	}
//
//	private void openShareContextMenu(View v) {
//		openContextMenu(v);
//	}
//
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.mom_menu, menu);
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.sub_menu_share_app_id:
//			// set the share action for api 14 and above
//			// ((ShareActionProvider)item.getActionProvider()).setShareIntent(getDefaultShareIntent());
//
//			// better use this to set the share action because it's for api
//			// below 14
//			startActivity(Intent
//					.createChooser(getDefaultShareIntent(), "Share"));
//			return true;
//		case R.id.sub_menu_settings:
//			MoveTo(getSettingsActivityClass(),
//					Intent.FLAG_ACTIVITY_NO_ANIMATION
//							| Intent.FLAG_ACTIVITY_NO_HISTORY);
//			return true;
//		default:
//			return super.onContextItemSelected(item);
//		}
//	}
//
//	private Intent getDefaultShareIntent() {
//		Intent shareIntent = new Intent();
//		shareIntent.setAction(android.content.Intent.ACTION_SEND);
//		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//		putExtraOnShareIntent(shareIntent);
//		return shareIntent;
//	}
//
//	/**
//	 * put the extra on the shareIntent. override this method to put other extra
//	 * on the share intent
//	 * 
//	 * @param shareIntent
//	 *            the share intent. you need to call <b>setType</b> on this
//	 *            Intent, and call <b>putExtra(Intent.EXTRA_XXX,
//	 *            "String value")</b> also
//	 */
//	protected void putExtraOnShareIntent(Intent shareIntent) {
//		shareIntent.setType("text/plain");
//		// Add data to the intent,
//		// the receiving app will decide what to do with it.
//		Resources r = getResources();
//		shareIntent.putExtra(Intent.EXTRA_SUBJECT,
//				r.getString(R.string.share_title));
//		shareIntent.putExtra(Intent.EXTRA_TEXT,
//				r.getString(R.string.share_message));
//	}
//
//	// /////////////////////////////////////////
//	// --- End of Tabs Menu ---
//	// /////////////////////////////////////////
//
//	// /////////////////////////////////////////
//	// --- Header TimerFragment ---
//	// /////////////////////////////////////////
//
//	/**
//	 * set the TimerFragment in a FrameLayout if the EatingService is running if
//	 * the service isn't running this FrameLayout is GONE
//	 * 
//	 * @param headerFrameLayoutId
//	 *            the id of the FrameLayout that suppose to contain the
//	 *            TimerFragment
//	 */
//	protected void setTimerFragmentHeader(int headerFrameLayoutId) {
//		// the TimerFragment suppose to be on the top of the screen.
//		// the activity layout should contain a FrameLayout that suppose to
//		// contain the
//		// TimerFragment
//
//		// check if the FrameLayout exists in the activity
//		try {
//			FrameLayout frameLayout = (FrameLayout) findViewById(headerFrameLayoutId);
//
//			// Check if the FrameLayout is not null
//			if (frameLayout == null) {
//				// If the FrameLayout is null throwing exception and catch it
//				// after
//				throw (new Exception());
//			}
//		} catch (Exception e) {
//			Log.e("Header TimerFragment",
//					"The Layout that suppose to contain the TimerFragment does not "
//							+ "exists in the activity. try to call "
//							+ "setTimeFragmentHeader in other callback "
//							+ "(such as onStart())", e);
//
//			// do nothing, return...
//			return;
//		}
//
//		Fragment fragment = null;
//
//		// Check if the timer is runnning
//		if (EatingServiceManager.getInstance(this).isRunning()) {
//			// if so, create a FragmentTransaction that will set the the
//			// TimerFragment in the FrameLayout
//			fragment = new TimerFragment();
//		} else {
//			// If the service manager isn't running, show the TitleFragment
//			fragment = new TitleFragment();
//		}
//
//		// Replace the FrameLayout by the Fragment
//		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
//				.beginTransaction();
//		fragmentTransaction.replace(headerFrameLayoutId, fragment);
//		fragmentTransaction.commit();
//	}
//
//	// /////////////////////////////////////////
//	// --- End of Header TimerFragment ---
//	// /////////////////////////////////////////
//
//	// /////////////////////////////////////////
//	// --- URL redirection ---
//	// /////////////////////////////////////////
//
//	/**
//	 * Set the link to the company web site on the inputed view.
//	 * 
//	 * @param resId
//	 *            - the view's resource id to link
//	 */
//	protected void setOnClickLinkToWebSite(int resId) {
//		String url = "https://www.google.co.il/#hl=iw&site=&source=hp&q=Kosher+Time&oq=Kosher+Time&gs_l=hp.3..0i19l3j0i30i19l7.1187.2998.0.3239.11.11.0.0.0.0.190.1254.3j8.11.0...0.0...1c.1.4.hp.IBrI638XWPU&bav=on.2,or.r_gc.r_pw.r_cp.&bvm=bv.42768644,d.Yms&fp=93f35139895aa7d4&biw=1527&bih=862";
//		setOnClickLink(resId, url);
//	}
//
//	/***
//	 * Set the link to the inputed url on a specific view.
//	 * 
//	 * @param resId
//	 *            - the view's resource id to link
//	 * @param url
//	 *            - the url for the redirection
//	 */
//	private void setOnClickLink(int resId, final String url) {
//		setOnClickLink(findViewById(resId), url);
//	}
//
//	private void setOnClickLink(View view, final String url) {
//		view.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				goToUrl(url);
//			}
//		});
//	}
//
//	/**
//	 * Redirecting the application to a specific url
//	 * 
//	 * @param url
//	 *            - the url to move to
//	 */
//	private void goToUrl(String url) {
//		Uri uri = Uri.parse(url);
//		startActivity(new Intent(Intent.ACTION_VIEW, uri));
//	}
//
//	// /////////////////////////////////////////
//	// --- End of URL redirection ---
//	// /////////////////////////////////////////
//
//	// /////////////////////////////////////////
//	// --- Help Methods ---
//	// /////////////////////////////////////////
//
//	public static int getNewTaskFlags() {
//		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//			return Intent.FLAG_ACTIVITY_CLEAR_TASK
//					| Intent.FLAG_ACTIVITY_NEW_TASK;
//		}
//		return Intent.FLAG_ACTIVITY_NEW_TASK;
//	}
//
//	private Class<?> getSettingsActivityClass() {
//		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//			return SettingsActivity.class;
//		}
//		return MeatOrMilkPreferenceActivity.class;
//	}
//
//	// /////////////////////////////////////////
//	// --- End of Help Methods ---
//	// /////////////////////////////////////////
//}
