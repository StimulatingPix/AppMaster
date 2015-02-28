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
public class AppsSaverTask extends AsyncTask<Context,Void,Integer>
{
	private String mAppPath;
	private Context mContext;
	private AppsHelper mAppsHelper;
	private LibraryToast mLibToast;
	private LibraryPreferences mLibPrefs;
	
	private Apps mApps;
	private int mCount;
	@Override
	protected Integer doInBackground(Context... context) 
	{
//
//		Get the installed applications
//
		mContext = context[0];

		mAppPath = mContext.getString(R.string.app_path);
		mLibToast = new LibraryToast(mContext);
		mLibPrefs = new LibraryPreferences(mContext);
		mAppsHelper = new AppsHelper(mContext,mAppPath);
		mApps = mAppsHelper.loadApps();
		mCount = mApps.Count();
		
		return mCount;
	}
	
	protected void onPostExecute(Integer count) 
	{
		
		mLibPrefs.getPrefs();
		if ( mLibPrefs.displayDiagnostics() ) {
			mLibToast.Toast("AppsSaverTask complete - Applications " + count, Toast.LENGTH_LONG);			
		}
		
		if ( count > 0 ) {
			mAppsHelper.SaveToFile("applications.xml");
		}
	}
}
