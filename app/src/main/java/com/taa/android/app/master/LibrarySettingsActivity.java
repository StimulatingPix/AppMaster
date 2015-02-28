/**
 * 
 */
package com.taa.android.app.master;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

/**
 * @author Android
 *
 *		Library Activity to manage settings
 *
 */

public class LibrarySettingsActivity extends PreferenceActivity {
	
	private static String mAppPath;

    private static ArrayList<Category> mCategoryList;
    private	static Category mCategory;
    private static int mCount;
	private static CategoriesHelper mCategoriesHelper;
	private static LibraryAndroid mLibAndroid;
	private static LibraryPreferences mLibPrefs;
	private static Context mContext;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) 
	 	{
	        super.onCreate(savedInstanceState);
	        
	        mAppPath = this.getString(R.string.app_path);
	        mCategoriesHelper = new CategoriesHelper( this , mAppPath );
	        mLibPrefs = new LibraryPreferences( this );
			mLibPrefs.getPrefs();
	        
	        mLibAndroid = new LibraryAndroid();
	        if ( mLibAndroid.getmAndroidRuntime() >= Build.VERSION_CODES.HONEYCOMB ) {
	        	LoadSimplePreferenceFragment();
	        } else {
	        	LoadSimplePreferences();
	        }
	    }

	 	@SuppressWarnings("deprecation")
		private void LoadSimplePreferences()
	 	{
			addPreferencesFromResource(R.xml.pref_simple);
//
//	Load the categories 
//
			
	        final ListPreference listPreference = (ListPreference) findPreference("category");
	        setListPreferenceData(listPreference);

	        listPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	            @Override
	            public boolean onPreferenceClick(Preference preference) {

	                setListPreferenceData(listPreference);
	                return false;
	            }
	        });				

			// Bind the summaries of List preferences to their values

	        bindPreferenceSummaryToValue(findPreference("category"));	 		
	 	}
	 	
	    /**
	     * Populate the activity with the top-level headers.
	     */
//	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//		@Override
//	    public void onBuildHeaders(List<Header> target) {
//	        loadHeadersFromResource(R.xml.pref_headers, target);
//	    }
	    
		/**
		 * A preference value change listener that updates the preference's summary
		 * to reflect its new value.
		 */
		private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object value) {
				String stringValue = value.toString();

				if (preference instanceof ListPreference) {
					// For list preferences, look up the correct display value in
					// the preference's 'entries' list.
					ListPreference listPreference = (ListPreference) preference;
					int index = listPreference.findIndexOfValue(stringValue);

					// Set the summary to reflect the new value.
					preference
							.setSummary(index >= 0 ? listPreference.getEntries()[index]
									: null);

				} else if (preference instanceof RingtonePreference) {
					// For ringtone preferences, look up the correct display value
					// using RingtoneManager.
					if (TextUtils.isEmpty(stringValue)) {
						// Empty values correspond to 'silent' (no ringtone).
						preference.setSummary(R.string.silent);

					} else {
						Ringtone ringtone = RingtoneManager.getRingtone(
								preference.getContext(), Uri.parse(stringValue));

						if (ringtone == null) {
							// Clear the summary if there was a lookup error.
							preference.setSummary(null);
						} else {
							// Set the summary to reflect the new ringtone display
							// name.
							String name = ringtone
									.getTitle(preference.getContext());
							preference.setSummary(name);
						}
					}

				} else {
					// For all other preferences, set the summary to the value's
					// simple string representation.
					preference.setSummary(stringValue);
				}
				return true;
			}
		};
	    
		/**
		 * Binds a preference's summary to its value. More specifically, when the
		 * preference's value is changed, its summary (line of text below the
		 * preference title) is updated to reflect the value. The summary is also
		 * immediately updated upon calling this method. The exact display format is
		 * dependent on the type of preference.
		 * 
		 * @see #sBindPreferenceSummaryToValueListener
		 */
		private static void bindPreferenceSummaryToValue(Preference preference) {
			// Set the listener to watch for value changes.
			preference
					.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

			// Trigger the listener immediately with the preference's
			// current value.
			sBindPreferenceSummaryToValueListener.onPreferenceChange(
					preference,
					PreferenceManager.getDefaultSharedPreferences(
							preference.getContext()).getString(preference.getKey(),
							""));
		}
	    
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void LoadSimplePreferenceFragment() {

			// Load the preferences in the simple fragment 

	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new SimplePreferenceFragment())
	                .commit();	
		}
		
		/**
		 * This fragment shows all the preferences.
		 */
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public static class SimplePreferenceFragment extends PreferenceFragment 
		{
			@Override
			public void onCreate(Bundle savedInstanceState) 
			{
				super.onCreate(savedInstanceState);
				addPreferencesFromResource(R.xml.pref_simple);
//
//		Load the categories 
//
				mContext = this.getActivity();
		        mAppPath = this.getString(R.string.app_path);
		        mCategoriesHelper = new CategoriesHelper( mContext , mAppPath );
		        mLibPrefs = new LibraryPreferences( mContext );
				mLibPrefs.getPrefs();
				
		        final ListPreference listPreference = (ListPreference) findPreference("category");
		        setListPreferenceData(listPreference);

		        listPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
		            @Override
		            public boolean onPreferenceClick(Preference preference) {

		                setListPreferenceData(listPreference);
		                return false;
		            }
		        });				

				// Bind the summaries of List preferences to their values

		        bindPreferenceSummaryToValue(findPreference("category"));
			}
		}
		
	    protected static void setListPreferenceData(ListPreference lp) {
//
// 		load the categories into the list preference data
//
	        boolean loadfromAssets; 
	        
	        if ( mLibPrefs.getmRestore() ) {
	        	loadfromAssets = true;
	        } else {
	        	loadfromAssets = false;
	        }
	        
	        mCategoryList = mCategoriesHelper.loadConfig(loadfromAssets);	    	
			mCount = mCategoryList.size();

            ArrayList<String> mNames = new ArrayList<String>(mCount);
            
		    for (int i = 0; i < mCount; i++) {	
		    	mCategory = mCategoryList.get(i);
		    	mNames.add(mCategory.name);
		    }
		    
		    final CharSequence[] mEntries = mNames.toArray(new CharSequence[mNames.size()]);
		    
            lp.setEntries(mEntries);
            lp.setEntryValues(mEntries);
    }		
}
