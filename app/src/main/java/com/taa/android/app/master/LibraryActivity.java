/**
 * 
 */
package com.taa.android.app.master;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

/**
 * @author Android
 *
 *		Library class for Activity
 *
 */
public class LibraryActivity {

	private Context mContext;
	
	private LibraryToast mLibToast;
	private LibraryAndroid mLibAndroid;
	
	private String mTag;
	private String mMessage;
	private CharSequence mActionBarSubTitle;
	private CharSequence mTitle;
	private String mAppTitle;

	private void loadStrings()
    {
		mAppTitle = mContext.getString(R.string.app_title);
    }
	
	public LibraryActivity(Context context,String tag)
	{
		mContext = context; 
		mTag = tag;
		mLibAndroid = new LibraryAndroid();
		mLibToast = new LibraryToast(mContext);
		loadStrings();
	}

	@SuppressLint("NewApi")
	public void SetActionBarSubTitle(LibraryPreferences mLibPrefs, ActionBar mActionBar) 
	{
//
//		Use of action bar will only work after API 11
//
		if ( mLibAndroid.getmAndroidRuntime() >= Build.VERSION_CODES.HONEYCOMB ) {
			mLibPrefs.getPrefs();
			
			mActionBarSubTitle = mLibPrefs.getmCategory();        
	        mActionBar.setSubtitle(mActionBarSubTitle);			
		}
	}
	
	public CharSequence SetTitle(LibraryPreferences mLibPrefs)
	{
//
//		Set the Activity Title
//		
		mTitle = mAppTitle + " " + mLibPrefs.getmCategory();
		return(mTitle);
	}
	
	public void ShowToast(String message)
	{
		mMessage = mTag + " " + message;
		mLibToast.Toast(mMessage, Toast.LENGTH_LONG);
	}
	
	public void ShortToast(String message)
	{
		mMessage = message;
		mLibToast.Toast(mMessage, Toast.LENGTH_SHORT);
	}
	
	public void LongToast(String message)
	{
		mMessage = message;
		mLibToast.Toast(mMessage, Toast.LENGTH_LONG);
	}

	public String getmTag() {
		return mTag;
	}
	
	public void setmTag(String mTag) {
		this.mTag = mTag;
	}
	
	public String getmMessage() {
		return mMessage;
	}
	
	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}
	

	
}
