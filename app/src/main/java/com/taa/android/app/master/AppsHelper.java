/**
 * 
 */
package com.taa.android.app.master;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.widget.Toast;

/**
 * @author Android
 *
 *		Helper Class for applications
 *
 */
public class AppsHelper {
	
	private Context mContext;
	private static final int GET_CONFIGURATIONS = 16384;
//	private static final int GET_META_DATA = 128;

    private PackageInfo mPackageInfo;
    private LibraryAndroid mLibAndroid;
    private LibraryToast mLibToast;
    private LibraryUtils mLibUtils;

	ArrayList<App> mAppList;
    private Apps mApps;
    private int mCount;
    
    private Assets mAssets;
    private String mXML;
    private String mAppPath;
    private String mAppError;
    
    public AppsHelper(Context context)
    {
    	mContext = context;
    	mLibAndroid = new LibraryAndroid();
    	mLibToast = new LibraryToast(mContext);
    	mLibUtils = new LibraryUtils(mContext);
    	mApps = new Apps();
    	mAppList = new ArrayList<App>();    	
    }
    
    public AppsHelper(Context context , String apppath) 
    {
    	mContext = context;
    	mAppPath = apppath;
       	mLibAndroid = new LibraryAndroid();
    	mLibToast = new LibraryToast(mContext);
    	mLibUtils = new LibraryUtils(mContext);
    	mApps = new Apps();
    	mAppList = new ArrayList<App>();    	
    }
    
    public boolean FileExists(String filename)
    {
//
//		Checks if there is a  file on the sd card
//
    	File sdcardFile = new File(mAppPath + filename );

		if ( sdcardFile.exists() ) {
			return(true);
		} else {
			return(false);
		}
    	
    }
    
    public int GetAppCount(String filename)
    {
//
//		Gets the count of applications in a category
//
    	
    	mAppList = LoadFromFile(filename);

    	if ( mAppList != null ) {
    	   	mCount = mAppList.size();
        	return(this.mCount);   		
    	}
     	return(0);
    }
    
    public int getAppsCount()
    {
//
//	Gets the count of applications
//
		mCount = mApps.Count();
		return(mCount);
    }
    
    public int getAppsListCount()
    {
//
//	Gets the count of applications
//
		mCount = mAppList.size();
		return(mCount);
    }

	@SuppressLint("NewApi") 
	public Apps loadApps() 
	{
/**
 * 
 * 		Loads the list of installed applications into Apps.
 * 
 **/
		
		PackageManager manager = mContext.getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);

		if (apps != null) 
		{
			final int count = apps.size();

			mApps = new Apps();
			
			for (int i = 0; i < count; i++) 
			{
				App mApp = new App();
				ResolveInfo info = apps.get(i);

				mApp.name = info.loadLabel(manager).toString();
				mApp.packagename = info.activityInfo.packageName;
				mApp.icon = info.activityInfo.loadIcon(manager);
				try {
					mPackageInfo = manager.getPackageInfo(mApp.packagename, GET_CONFIGURATIONS );
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mPackageInfo != null ) {
					if ( mLibAndroid.getmAndroidRuntime() > Build.VERSION_CODES.FROYO )
					{
						mApp.FirstInstallTime = mPackageInfo.firstInstallTime;
						mApp.LastUpdateTime = mPackageInfo.lastUpdateTime;
						mApp.DateTimeInstall = new Date(mApp.FirstInstallTime);
						mApp.DateTimeLastUpdate = new Date(mApp.LastUpdateTime);
						mApp.strDateTimeInstall = mLibUtils.ToStrDateTime(mApp.FirstInstallTime);
						mApp.strDateTimeLastUpdate = mLibUtils.ToStrDateTime(mApp.LastUpdateTime);
						
					}
				}
				mApps.Add(mApp);
			}
		}
		return(mApps);
	}    
    
	@SuppressLint("NewApi") 
	public ArrayList<App> loadApplications() 
	{
/**
 * 
 * 		Loads the list of installed applications into ArrayList <App>.
 * 
 **/
		
		PackageManager manager = mContext.getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);

		if (apps != null) 
		{
			final int count = apps.size();

			
			mAppList = new ArrayList<App>(count);
			
			mAppList.clear();

			for (int i = 0; i < count; i++) 
			{
				App mApp = new App();
				ResolveInfo info = apps.get(i);

				mApp.name = info.loadLabel(manager).toString();
				mApp.packagename = info.activityInfo.packageName;
				mApp.icon = info.activityInfo.loadIcon(manager);
				try {
					mPackageInfo = manager.getPackageInfo(mApp.packagename, GET_CONFIGURATIONS );
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mPackageInfo != null ) {
					if ( mLibAndroid.getmAndroidRuntime() > Build.VERSION_CODES.FROYO )
					{
						mApp.FirstInstallTime = mPackageInfo.firstInstallTime;
						mApp.LastUpdateTime = mPackageInfo.lastUpdateTime;
						mApp.DateTimeInstall = new Date(mApp.FirstInstallTime);
						mApp.DateTimeLastUpdate = new Date(mApp.LastUpdateTime);
						mApp.strDateTimeInstall = mLibUtils.ToStrDateTime(mApp.FirstInstallTime);
						mApp.strDateTimeLastUpdate = mLibUtils.ToStrDateTime(mApp.LastUpdateTime);
						
					}
				}
				mAppList.add(mApp);
			}
		}
		return(mAppList);
	}
	
	@SuppressLint("NewApi")
	public ArrayList<App> LoadAppsFromFile(String filename)
	{
//
//			Read a applications from file on SD card
//
		PackageManager manager = mContext.getPackageManager();

		Serializer serial = new Persister();

		File sdcardFile = new File(mAppPath + filename );

		mApps = new Apps();

		try {
			serial.read( mApps , sdcardFile);

		} catch (Exception e) {
			mAppError = "File " + sdcardFile.toString() + " error " + e.getMessage();
			mLibToast.Toast( mAppError, Toast.LENGTH_LONG );

			e.printStackTrace();
		}    	
		if ( mApps != null ) 
		{
			mCount = mApps.Count();
			mAppList = new ArrayList<App>(mCount);

			for (int i=0; i < mCount; i++ ) 
			{
				App mApp = mApps.get(i);
				
//				try {
//					ApplicationInfo mAppinfo = manager.getApplicationInfo( mApp.packagename, GET_META_DATA );
//				} catch (NameNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				try {
					mApp.icon = manager.getApplicationIcon(mApp.packagename);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				mApp.icon = mAppinfo.activityInfo.loadIcon(manager);
				try {
					mPackageInfo = manager.getPackageInfo(mApp.packagename, GET_CONFIGURATIONS );
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mPackageInfo != null ) {
					if ( mLibAndroid.getmAndroidRuntime() > Build.VERSION_CODES.FROYO )
					{
						mApp.FirstInstallTime = mPackageInfo.firstInstallTime;
						mApp.LastUpdateTime = mPackageInfo.lastUpdateTime;
						mApp.DateTimeInstall = new Date(mApp.FirstInstallTime);
						mApp.DateTimeLastUpdate = new Date(mApp.LastUpdateTime);
						mApp.strDateTimeInstall = mLibUtils.ToStrDateTime(mApp.FirstInstallTime);
						mApp.strDateTimeLastUpdate = mLibUtils.ToStrDateTime(mApp.LastUpdateTime);
						
					}
				}				
				mAppList.add(mApp);
			}
			return(mAppList);
		}
		return(null);
	}
	   
    public ArrayList<App> LoadFromXML(String filename)
    {
//
//		Read a categories applications from Assets into an XML String
//    	
  
    	mAssets = new Assets(mContext);
	
    	mXML = mAssets.Read(filename);
//
//		Parse the XMl file into the Apps class
//    	
    	
    	Serializer serial = new Persister();
	
    	Apps apps = new Apps();
    	
    	try 	
    	{	
    		apps = serial.read(Apps.class, mXML);
    	}
		catch (Exception e) {
					e.printStackTrace();
		}
    	
    	if ( apps != null ) {
    		mCount = apps.Count();
    		mAppList = new ArrayList<App>(mCount);
    		for ( int i=0; i<mCount; i++ ) {
    			App mApp = apps.get(i);
    			mAppList.add(mApp);
    		}
    		return(mAppList);
    	}
    	return (null);
    }
    
    public ArrayList<App> LoadFromFile(String filename)
    {
//
//		Read a categories applications from file on SD card
//
    	
	   	Serializer serial = new Persister();
	   	
    	File sdcardFile = new File(mAppPath + filename );
    	
    	mApps = new Apps();
    	
    	try {
				serial.read( mApps , sdcardFile);
				
			} catch (Exception e) {
				mAppError = "File " + sdcardFile.toString() + " error " + e.getMessage();
				mLibToast.Toast( mAppError, Toast.LENGTH_LONG );
							
				e.printStackTrace();
			}    	
    	if ( mApps != null ) 
    	{
    		mCount = mApps.Count();
        	mAppList = new ArrayList<App>(mCount);
    		
    		for (int i=0; i < mCount; i++ ) {
    			App mApp = mApps.get(i);
    			mAppList.add(mApp);
    		}
        	return(mAppList);
    	}
    	return(null);
    }

	public boolean SaveToFile(String filename)
	{
//
//		Save a Categories Applications to a file on SD card
//		
		boolean fileCreated = false;
		
	   	Serializer serial = new Persister();
	   	
    	File sdcardFile = new File(mAppPath + filename );

    	try {
    		fileCreated = sdcardFile.createNewFile();
    	} catch (Exception e ) {
				mAppError = "File " + sdcardFile.toString() + " error " + e.getMessage();
				mLibToast.Toast( mAppError, Toast.LENGTH_LONG );
				e.printStackTrace();    		
    	}
    	
    	try {
				serial.write( mApps , sdcardFile);
				
			} catch (Exception e) {
				mAppError = e.getMessage();
				mLibToast.Toast( mAppError, Toast.LENGTH_LONG );
				e.printStackTrace();
			}
    	return(fileCreated);
	}
	
	public void SetApps(Apps apps)
	{
		mApps = apps;
	}
	
	public void SetAppList(ArrayList<App> list)
	{
		mAppList = list;
	}
	
	public void SetListToApps(ArrayList<App> list) 
	{

		mCount = list.size();
		
		mApps = new Apps();
		for (int i=0 ; i < mCount; i++ ) {
			App mApp = list.get(i);
			mApps.Add(mApp);
		}
		
	}
	
	public ArrayList<App> sortApplications (String field) 
	{
		final Comparator<App> NAME_ORDER_ASC = 
				new Comparator<App>() {
			public int compare(App a1, App a2) 
			{
				return a1.name.compareTo(a2.name);
			}	
		};
		final Comparator<App> NAME_ORDER_DESC = 
				new Comparator<App>() {
			public int compare(App a1, App a2) 
			{
				return a2.name.compareTo(a1.name);
			}	
		};
		final Comparator<App> DATE_INSTALL_ASC = 
				new Comparator<App>() {
			public int compare(App a1, App a2) 
			{
				return a2.DateTimeInstall.compareTo(a1.DateTimeInstall);
			}	
		};		   
		final Comparator<App> DATE_INSTALL_DESC = 
				new Comparator<App>() {
			public int compare(App a1, App a2) 
			{
				return a1.DateTimeInstall.compareTo(a2.DateTimeInstall);
			}	
		};		   
		final Comparator<App> DATE_UPDATE_ASC = 
				new Comparator<App>() {
			public int compare(App a1, App a2) 
			{
				return a2.DateTimeLastUpdate.compareTo(a1.DateTimeLastUpdate);
			}	
		};		   
		final Comparator<App> DATE_UPDATE_DESC = 
				new Comparator<App>() {
			public int compare(App a1, App a2) 
			{
				return a1.DateTimeLastUpdate.compareTo(a2.DateTimeLastUpdate);
			}	
		};		   
		if ( field.equals("name_asc")) {

			Collections.sort(mAppList, NAME_ORDER_ASC);
		}
		if ( field.equals("name_desc")) {

			Collections.sort(mAppList, NAME_ORDER_DESC);
		}
		if ( field.equals("install_asc")) {

			Collections.sort(mAppList, DATE_INSTALL_ASC);
		}
		if ( field.equals("install_desc")) {

			Collections.sort(mAppList, DATE_INSTALL_DESC);
		}
		if ( field.equals("update_asc")) {

			Collections.sort(mAppList, DATE_UPDATE_ASC);
		}
		if ( field.equals("update_desc")) {

			Collections.sort(mAppList, DATE_UPDATE_DESC);
		}
		return(mAppList);
	}	
}
