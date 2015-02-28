/**
 * 
 */
package com.taa.android.app.master;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author Android
 *
 */
public class RegisterAppsTask extends AsyncTask<Context,Void,Registry>
{
	private LibraryToast mLibToast;
	private LibraryPreferences mLibPrefs;
	private Context mContext;
	private Registry mRegistry;
	private int mAppCount;
	private int mCategoryCount;
	
	protected Registry doInBackground(Context... context) 
	{
//
//		Get the installed applications
//
		mContext = context[0];

		mLibToast = new LibraryToast(mContext);
		mLibPrefs = new LibraryPreferences(mContext);
		mRegistry = new Registry(mContext);
		mRegistry.ReadConfig();
		mRegistry.RegisterApps();
		
		return mRegistry;
	}
	
	protected void onPostExecute(Registry registry) 
	{
		mCategoryCount = registry.CountCategories();
		mAppCount = registry.Count();
		
		mLibPrefs.getPrefs();
		if ( mLibPrefs.displayDiagnostics() ) {
			mLibToast.Toast("RegisterAppsTask complete - Applications " + mAppCount + " categories " + mCategoryCount , Toast.LENGTH_LONG);			
		}
		
		if ( mAppCount > 0 ) {
			mRegistry.SaveRegistry();
		}
	}	
}
