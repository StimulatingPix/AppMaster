package com.taa.android.app.master;

import java.util.ArrayList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Android
 *
 *		Activity class to manage applications
 *
 */
public class ApplicationsActivity extends FragmentActivity implements AppSortDialogFragment.NoticeDialogListener , AppSortNameDialogFragment.NoticeDialogListener
{

	private AppsHelper mAppsHelper;
	private LibraryAndroid mLibAndroid;
	private LibraryActivity mLibActivity;
	private LibraryPreferences mLibPrefs;
	private LibraryUtils mLibUtils;
	
    private static ArrayList<App> mApps;
    private static App mApp;
    private static String mStrAppCount;
    private static String mAppSort;
    
    private static final int MATCH_DEFAULT_ONLY = 655536;
    private static final int GET_UNINSTALLED_PACKAGES = 8192;
    private static final int INTENT_UNINSTALL = 10;
    
    
    private Context mContext;
    private ResolveInfo mResolveInfo;
    private PackageManager mPackageManager;
    private PackageInfo mPackageInfo;
	private FragmentManager mFragmentManager;
    private AppSortDialogFragment mSortDialog;
    private AppSortNameDialogFragment mSortNameDialog;
    private ApplicationsListPortraitAdapter mAppsPortraitAdapter;
    private ApplicationsListLandscapeAdapter mAppsLandscapeAdapter;
    private View mView;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this; 
		mLibAndroid = new LibraryAndroid();
		mLibActivity = new LibraryActivity(mContext, "ApplicationsActivity" );
		mLibPrefs = new LibraryPreferences(mContext);
        mLibPrefs.getPrefs();		
		mLibUtils = new LibraryUtils(mContext);
		mLibUtils.getDevice();
		
		mAppsHelper = new AppsHelper(mContext);
				
		
		mStrAppCount = this.getString(R.string.txt_apps_count);

		mFragmentManager = getSupportFragmentManager();
        mPackageManager = this.getPackageManager();

		setContentView(R.layout.activity_applications);
		
		mApps = mAppsHelper.loadApplications();
		mApps = mAppsHelper.sortApplications("name_asc");

		displayCount(mAppsHelper.getAppsListCount());

		
        ListView mListView = (ListView) findViewById(R.id.listViewApplications);
        
        if ( mLibUtils.mDevice.misPortrait ) {
        	mAppsPortraitAdapter = new ApplicationsListPortraitAdapter( ApplicationsActivity.this , mApps);
        	mListView.setAdapter(mAppsPortraitAdapter);
        } else {
        	mAppsLandscapeAdapter = new ApplicationsListLandscapeAdapter( ApplicationsActivity.this , mApps);
        	mListView.setAdapter(mAppsLandscapeAdapter);        	
        }
        
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
            	
            	mApp = mApps.get(position);
            	               
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                
                mView = inflater.inflate(R.layout.app_action, null);
    	        	                       
                adb.setView(mView);
                adb.setTitle("Application Action");
                adb.show();                
                                
            }
        };        
        // Setting the item click listener for the List View
        
        mListView.setOnItemClickListener(itemClickListener);
                
	}
	
	private void displayCount(int count)
	{
//
//		Display the count of applications
//	
		TextView mTextCount = (TextView) findViewById(R.id.tv_count);
		mTextCount.setText( mStrAppCount + " " + count );		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
//		
// Inflate the menu; this adds items to the action bar if it is present.
//
		getMenuInflater().inflate(R.menu.applications, menu);
		return true;
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//
// Action before menu is displayed
//    	
    	if ( mLibAndroid.getmAndroidRuntime() < Build.VERSION_CODES.HONEYCOMB ) {
            MenuItem item;
            item = menu.findItem(R.id.actionSort);
            item.setTitle("Sort");    		
    	}
        
        return true;
    }    	
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
//		
// executed when menu item is selected
//
		switch (item.getItemId())
		{
			case R.id.actionSort:
			{
				MenuSortApps();
				return(true);
			}
		}
		return (false);
	}

	private void MenuSortApps()
	{
		if ( mLibAndroid.getmAndroidRuntime() >= Build.VERSION_CODES.GINGERBREAD ) {
			mSortDialog = new AppSortDialogFragment();
			mSortDialog.show( mFragmentManager , "AppSortDialog" );			   			
		} else {
			mSortNameDialog = new AppSortNameDialogFragment();
			mSortNameDialog.show( mFragmentManager , "AppSortNameDialog" );			   			
		}

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) 
	{
//		
// 		Sort the applications
//		
		if ( mLibAndroid.getmAndroidRuntime() >= Build.VERSION_CODES.GINGERBREAD ) {
			mAppSort = mSortDialog.getSortBy();			
		} else {
			mAppSort = mSortNameDialog.getSortBy();
		}
	    
	    if ( mAppSort != null ) {
	    	mApps = mAppsHelper.sortApplications(mAppSort);
	    	notifyAdapterChange();
	    }
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
//
//		Sort cancelled do not do anything
//		
		
	}
	
	private void notifyAdapterChange()
	{
    	if (mLibUtils.mDevice.misPortrait) {
    		mAppsPortraitAdapter.notifyDataSetChanged();
    	} else {
    		mAppsLandscapeAdapter.notifyDataSetChanged();	    		
    	}		
	}
	
	@SuppressLint("InlinedApi") 
	public void AppActionDetails(View view)
	{
		if ( mLibPrefs.displayDiagnostics() ) 
		{

			mLibActivity.ShowToast("App package " + mApp.packagename);
		}

//
//			This will not work for API 8
//			
		if ( mLibAndroid.getmAndroidRuntime() > Build.VERSION_CODES.FROYO ) {
			try 
			{
				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", mApp.packagename, null));	   
				mResolveInfo = mPackageManager.resolveActivity(intent, MATCH_DEFAULT_ONLY);

				if ( null != mResolveInfo ) {
					this.startActivity(intent);				
					
				} else {
		        	errorAppActionDetails();				
				}
			}
			
	        catch (ActivityNotFoundException e)
	        {
	        	errorAppActionDetails();
	        }								
		}
	}
	
	private void errorAppActionDetails() 
	{

			mLibActivity.ShowToast("Unable to view package " + mApp.packagename);

	}
	
	public void AppActionRun(View view)
	{
		if ( mLibPrefs.displayDiagnostics() ) {
			mLibActivity.ShowToast("Run package " + mApp.packagename);
		}
		try
        {
        	Intent intent = mPackageManager.getLaunchIntentForPackage(mApp.packagename);

        	if (null != intent) {
        		this.startActivity(intent);
        	} else {
        		
            	errorAppActionRun();        		
        	}
        }

        catch (ActivityNotFoundException e)
        {
        	errorAppActionRun();
        }		
	}
	
	private void errorAppActionRun() {

		mLibActivity.ShowToast("Unable to run package " + mApp.packagename);
		
	}
	
	public void AppActionUninstall(View view)
	{
		if ( mLibPrefs.displayDiagnostics() ) {
			mLibActivity.ShowToast("Uninstall to package " + mApp.packagename);
		}
		
		try {

			Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", mApp.packagename, null));	   
			mResolveInfo = mPackageManager.resolveActivity(intent, MATCH_DEFAULT_ONLY);

			if (null != mResolveInfo) {
				this.startActivityForResult(intent, INTENT_UNINSTALL);
				
			} else {
	        	errorAppActionUninstall();				
			}
		}
        catch (ActivityNotFoundException e)
        {
        	errorAppActionUninstall();
        }		

	}

	private void errorAppActionUninstall() {
		mLibActivity.ShowToast("Unable to uninstall to package " + mApp.packagename);
	}

	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    	
//	Get the result from an intent
//	    	
	        if (requestCode == INTENT_UNINSTALL) {

	    		if ( mLibPrefs.displayDiagnostics() ) {
	    			mLibActivity.ShowToast("onActivityResult " + resultCode);
	    		}
	    		
                try {
					mPackageInfo = mPackageManager.getPackageInfo(mApp.packagename, GET_UNINSTALLED_PACKAGES );
					
				} catch (NameNotFoundException e) {
					// Package UnInstalled
//					if ( mPackageInfo.packageName.equals(mApp.packagename)) 
//					{
						final int count = this.removePackage(mApp.packagename);
						notifyAdapterChange();
						displayCount(count);
//					}
				}	        	
	        }

	    }

	   private int removePackage(String packagename) 
	   {
//
//		Remove an App from Apps list with the supplied package name
//		Return the new Apps size count
		   
		   int count = mApps.size();

		   for (int i = 0; i < count; i++) 
		   {		   
			   App mApp = mApps.get(i);
			   if ( mApp.packagename.equals(packagename) ) {
				   mApp = mApps.remove(i);
	    			mLibActivity.LongToast("App " + mApp.name + " Uninstalled" );
					break;
			   }
		   }
		   count = mApps.size();
		   return(count);
	   }
}