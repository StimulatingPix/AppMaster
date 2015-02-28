/**
 * 
 */
package com.taa.android.app.master;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * @author Android
 *
 *		Library class to handle preferences
 *
 */
public class LibraryPreferences {

	private Context mContext;
	private LibraryToast mLibToast;
	private LibraryUtils mLibUtils;
	
	public SharedPreferences mPrefs;
	
//	
//	Shared preferences
//	
	private String mCategory;
	private Boolean mDiagnostics;
	private Boolean mRestore;
	private Boolean mAppcount;
	private Boolean mLaunch;
	private Boolean mLogging;
	
	private static String mPrefsStringValue;
	
	public LibraryPreferences (Context context)
	{
		mContext = context;
		mLibUtils = new LibraryUtils(mContext);
		mLibToast = new LibraryToast(mContext);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);

	}

	public boolean displayDiagnostics()
	{
		return (mDiagnostics);
	}
	
	public void changePrefs(String key)
	{
//
//		Change Shared Preferences
//
		
		if ( key.equals("category") )
		{
			this.setmCategory(mPrefs.getString(key, "" ));
			mPrefsStringValue = this.getmCategory() ;
		}	
		
		if ( key.equals("diagnostics") )
		{
			this.setmDiagnostics(mPrefs.getBoolean(key, false));
			mPrefsStringValue = mLibUtils.BooleanToString( this.getmDiagnostics() );
		}
		
		if ( key.equals("appcount") )
		{
			this.setmAppcount(mPrefs.getBoolean(key, false));
			mPrefsStringValue = mLibUtils.BooleanToString( this.getmAppcount() );		
		}
		
		if ( key.equals("restore") )
		{
			this.setmRestore(mPrefs.getBoolean(key, false));
			mPrefsStringValue = mLibUtils.BooleanToString( this.getmRestore() );
		}		


		if ( this.displayDiagnostics() ) 
		{
			mLibToast.Toast("Preference change " + key + " value " + mPrefsStringValue, Toast.LENGTH_LONG );
		}
		
	}
	
	public void getPrefs() 
	{	        	        
//
//		Get Shared Preferences
//		
			setmDiagnostics(mPrefs.getBoolean("diagnostics", false));
	        setmRestore(mPrefs.getBoolean("restore", false));
	        setmAppcount(mPrefs.getBoolean("appcount", false));
	        setmLaunch(mPrefs.getBoolean("launch", false));
	        setmLogging(mPrefs.getBoolean("logging", false));
	        setmCategory(mPrefs.getString("category", ""));		
	        
	}
	
	public void setPrefs(String key,String value)
	{
//
//		Set shared preferences
//
		try {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
			SharedPreferences.Editor editor = preferences.edit();
	    
			if ( key.equals("appcount") ) {
				editor.putString( key , value );
		        editor.commit();				
			}
			if ( key.equals("logging") ) {
				editor.putString( key , value );
		        editor.commit();				
			}
			if ( key.equals("category") ) {
				editor.putString( key , value );
		        editor.commit();				
			}
			if ( key.equals("diagnostics") ) {
				editor.putBoolean( key , mLibUtils.StringToBoolean(value) );
		        editor.commit();				
			}
			if ( key.equals("restore") ) {
				editor.putBoolean( key , mLibUtils.StringToBoolean(value) );
		        editor.commit();				
			}
		
		} catch ( Exception e) {
			mLibToast.Toast("Error setting shared preferences", Toast.LENGTH_LONG);
		}
	}

	public Boolean getmRestore() {
		return mRestore;
	}

	public void setmRestore(Boolean mRestore) {
		this.mRestore = mRestore;
	}

	public String getmCategory() {
		return mCategory;
	}

	public void setmCategory(String mCategory) {
		this.mCategory = mCategory;
	}

	public Boolean getmDiagnostics() {
		return mDiagnostics;
	}

	public void setmDiagnostics(Boolean mDiagnostics) {
		this.mDiagnostics = mDiagnostics;
	}

	public Boolean getmAppcount() {
		return mAppcount;
	}

	public void setmAppcount(Boolean mAppcount) {
		this.mAppcount = mAppcount;
	}

	public Boolean getmLaunch() {
		return mLaunch;
	}

	public void setmLaunch(Boolean mLaunch) {
		this.mLaunch = mLaunch;
	}
	
	public Boolean getmLogging() {
		return mLogging;
	}

	public void setmLogging(Boolean mLogging) {
		this.mLogging = mLogging;
	}	
	
}
