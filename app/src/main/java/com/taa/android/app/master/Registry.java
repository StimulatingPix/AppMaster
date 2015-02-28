/**
 * 
 */
package com.taa.android.app.master;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
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
 * 			
 *		Registry class
 *
 *		Registry of applications and which categories they are in
 *
 */
@Element(name="registry")
public class Registry {

	private static final int GET_CONFIGURATIONS = 16384;
	
	private int mApplicationCount;
	private int mApplicationIndex;
	private int mCategoryCount;
	private int mCategoryAppCount;
	
	private RegisteredApp mApplication;
	private Apps mCategoryApps;
	private Categories mCategories;
	private Category mCategory;
	private String mFilename;
	private String mConfigFilename;
	private String mAppPath;
	private String mAppError;
	private Context mContext;
    private PackageInfo mPackageInfo;
    private PackageManager mPackageManager;
    private LibraryAndroid mLibAndroid;
    private LibraryToast mLibToast;
    private LibraryUtils mLibUtils;
    
	@ElementList(entry="registeredapps", inline=true , required=false )
	public List<RegisteredApp> mRegisteredApps = new ArrayList<RegisteredApp>();

	public Registry (Context context) 
	{
		this.mApplicationCount = 0;
		this.mApplicationIndex = 0;
		this.mRegisteredApps.clear();
		this.mFilename = "registry.xml";
		this.mConfigFilename = "config.xml";
		this.mContext = context;
		this.mLibAndroid = new LibraryAndroid();
		this.mLibToast = new LibraryToast(this.mContext);
		this.mLibUtils = new LibraryUtils(this.mContext);
		this.mAppPath = mContext.getString(R.string.app_path);
		this.mCategories = new Categories();

	}
	
	public void Add(RegisteredApp ra) {
//
// 	Add a application to the registry
//		
		mApplicationIndex = this.Count();
		mRegisteredApps.add(mApplicationIndex, ra);
	}
	
	public int Count() {
//
//	get the count of applications
//		
		mApplicationCount = mRegisteredApps.size();
		return (mApplicationCount);
	}
	
	public void Remove(int index) {
//
//	Remove an application from the registry		
//
		mApplication = mRegisteredApps.remove(index);	
	}

	public RegisteredApp get(int position) {
//		
//  Get an application from the registry
//		
		mApplication = mRegisteredApps.get(position);
		return (mApplication);
	}
	
	public String getFilename() {
//
//	Get the registry filename
//		
		return (mFilename);
	}
	
	public int CountCategories(){
//
//		Get the Count of Categories
//		
		return (mCategories.Count());
	}
	
	public RegisteredApp FindCategoriesForApp(RegisteredApp rApp )
	{
//
//		Find the categories an Application is in 
//		
		this.mCategoryCount = mCategories.Count();
		
		for ( int i=0; i< this.mCategoryCount; i++ )
		{
			this.mCategory = this.mCategories.get(i);
			this.ReadCategoryApps(mCategory.filename);
			if ( FindAppinCategory(rApp) ) {
				rApp.Add(this.mCategory);
			}
				
		}
		
		return(rApp);
	}
	
	public boolean FindAppinCategory(RegisteredApp rApp )
	{
		boolean mAppFound = false;
		
		this.mCategoryAppCount = this.mCategoryApps.Count();
		
		for ( int index=0; index<this.mCategoryAppCount ; index++ ) 
		{
			App mApp = this.mCategoryApps.get(index);
			if ( rApp.packagename.equals(mApp.packagename))
			{
				mAppFound = true;
				break;
			}
		}
		return( mAppFound ); 
	
	}
	
	public void ReadCategoryApps(String filename)
	{
//
//		Read the applications for a category
//
		Serializer serial = new Persister();

		File sdcardFile = new File(mAppPath + filename );

		this.mCategoryApps = new Apps();
		
		try {
			serial.read( mCategoryApps , sdcardFile);

		} catch (Exception e) {
			mAppError = "File " + sdcardFile.toString() + " error " + e.getMessage();
			mLibToast.Toast( mAppError, Toast.LENGTH_LONG );

			e.printStackTrace();
		}    			
	}
	
	public void ReadConfig()
	{
/**
 *		Read the Categories from config.xml  
 */
	   	Serializer serial = new Persister();
	   	
    	File sdcardFile = new File(mAppPath + mConfigFilename );

    	try {
				serial.read( this.mCategories , sdcardFile);
				
			} catch (Exception e) {
							
				e.printStackTrace();
			}    	   	
    }		

	@SuppressLint("NewApi")
	public void RegisterApps() 
	{
/**
 * 
 * 		Loads the list of installed applications.
 * 
 **/
		
		mPackageManager = mContext.getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = mPackageManager.queryIntentActivities(mainIntent, 0);

		if (apps != null) 
		{
			final int count = apps.size();

			
			for (int i = 0; i < count; i++) 
			{
				mApplication = new RegisteredApp();
				ResolveInfo info = apps.get(i);

				mApplication.name = info.loadLabel(mPackageManager).toString();
				mApplication.packagename = info.activityInfo.packageName;
				mApplication.icon = info.activityInfo.loadIcon(mPackageManager);
				try {
					mPackageInfo = mPackageManager.getPackageInfo(mApplication.packagename, GET_CONFIGURATIONS );
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mPackageInfo != null ) {
					if ( mLibAndroid.getmAndroidRuntime() > Build.VERSION_CODES.FROYO )
					{
						mApplication.FirstInstallTime = mPackageInfo.firstInstallTime;
						mApplication.LastUpdateTime = mPackageInfo.lastUpdateTime;
						mApplication.DateTimeInstall = new Date(mApplication.FirstInstallTime);
						mApplication.DateTimeLastUpdate = new Date(mApplication.LastUpdateTime);
						mApplication.strDateTimeInstall = mLibUtils.ToStrDateTime(mApplication.FirstInstallTime);
						mApplication.strDateTimeLastUpdate = mLibUtils.ToStrDateTime(mApplication.LastUpdateTime);
						
					}
				}
				mApplication = this.FindCategoriesForApp(mApplication);
				this.Add(mApplication);
			}
		}
	}
	
	public boolean SaveRegistry()
	{
//
//		Save a Categories Applications to a file on SD card
//		
		boolean fileCreated = false;
		
	   	Serializer serial = new Persister();
	   	
    	File sdcardFile = new File(mAppPath + mFilename );

    	try {
    		fileCreated = sdcardFile.createNewFile();
    	} catch (Exception e ) {
				mAppError = "File " + sdcardFile.toString() + " error " + e.getMessage();
				mLibToast.Toast( mAppError, Toast.LENGTH_LONG );
				e.printStackTrace();    		
    	}
    	
    	try {
				serial.write( this , sdcardFile);
				
			} catch (Exception e) {
				mAppError = e.getMessage();
				mLibToast.Toast( mAppError, Toast.LENGTH_LONG );
				e.printStackTrace();
			}
    	return(fileCreated);
	}	
}
