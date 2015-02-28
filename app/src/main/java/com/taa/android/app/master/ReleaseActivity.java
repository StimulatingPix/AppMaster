package com.taa.android.app.master;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

/**
 * @author Android
 *
 *		This activity is executed when the application is first run
 *		or when the shared preferences restore to factory is set
 *
 *		It creates a AppMaster folder
 *
 *		loads the confige.xml from Assets
 *
 *		Loads each apps.xml from Assets and saves to the AppLauncher folder
 *
 */

public class ReleaseActivity extends FragmentActivity implements ReleaseDialogFragment.NoticeDialogListener {

	private static LibraryAndroid mLibAndroid;
	private static LibraryApplication mLibApplication;
	private static LibraryActivity mLibActivity;
	private static LibraryPreferences mLibPrefs;
	private static LibraryUtils mLibUtils;
	
	private final static String mNewLine = System.getProperty("line.separator");
	
	private static String mAppPath;
	private static String mRunType;
	private String mMessage;
	
//	Context	
	Context mContext;
	
//	Folders

	private File mFoldersPath;
	private String mFoldersBasePath;
	private String mFoldersAppPath;
	private String mFoldersSevereError;
	private String mFoldersSuccess;
	
//	Release
	
	private StringBuffer mLog;
	private TextView mInstallLog;
	private String mCreateFolderStatus;
	private String mInstallStatus;
	private String mAppName;
	private String mAppDetails;
	private String mAppVersion;
	private TextView mtvAppDetails;
	private TextView mtvInstallStatus;
	private TextView mtvFolderDetails;
	private TextView mtvFolderStatus;
	private String mFilename;
	
//	Status
	private String mStatusWorking;
	private String mStatusComplete;
	private String mStatusFailed;
	
//	Categories
    private static Categories mCategories;
    private static int mCategoryCount;
	
//	Apps
	private static ArrayList<App> mAppsList = new ArrayList<App>();
	
	private AppsHelper mAppsHelper;
	private CategoriesHelper mCategoriesHelper;
	private ReleaseDialogFragment mDialog;
	private FragmentManager mFragmentManager;
	
	private void getDetails() {
		
		mAppName = this.getResources().getString(R.string.app_name);
		mAppVersion = this.getResources().getString(R.string.app_version);
		mAppDetails = mAppName + " " + mAppVersion;
	}
	
	private void getFolders() {

		mFoldersBasePath = this.getResources().getString(R.string.folders_base_path);
		mFoldersAppPath = this.getResources().getString(R.string.folders_app_path);
		mFoldersSevereError = this.getResources().getString(R.string.folders_severe_error);
		mFoldersSuccess = this.getResources().getString(R.string.folders_success);
		
	}
	
	private void getStatus() {
		mStatusComplete = this.getResources().getString(R.string.install_status_complete);
		mStatusWorking = this.getResources().getString(R.string.install_status_working);
		mStatusFailed = this.getResources().getString(R.string.install_status_failed);
	}
		
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//
//		Executed when activity is created
//
        mContext = this;

        mLibAndroid = new LibraryAndroid(); 
        mLibApplication = new LibraryApplication(mContext);
        mLibActivity = new LibraryActivity(mContext,"Release Activity");
        mLibPrefs = new LibraryPreferences(mContext);
		mLibUtils = new LibraryUtils(mContext);

		mAppPath = mLibApplication.getmAppPath();
        mLibPrefs.getPrefs();
        mLibUtils.getDevice();
        
        getDetails();
        
        getFolders();
                
        getStatus();
        
		setContentView(R.layout.activity_release);
		
		mtvAppDetails = ( TextView ) findViewById(R.id.tv_install_app_details);
		mtvInstallStatus = ( TextView ) findViewById(R.id.tv_install_app_status);
		mtvFolderDetails = ( TextView ) findViewById(R.id.tv_install_folders_details);
		mtvFolderStatus = ( TextView ) findViewById(R.id.tv_install_folders_status);
		
		mInstallLog = (TextView) findViewById(R.id.et_install_log);
		
		mtvAppDetails.setText(mAppDetails);

		GetRunType();
		
		if ( mRunType.equals("new") ) {
			this.mMessage = this.getResources().getString(R.string.install_new_message);
		}
		if ( mRunType.equals("update") ) {
			this.mMessage = this.getResources().getString(R.string.install_update_message);
		}
		
//
//		Display confirmation dialog
//
		mFragmentManager = getSupportFragmentManager();
		
	    mDialog = new ReleaseDialogFragment();
	    mDialog.SetMessage(this.mMessage);
	    
	    mDialog.show( mFragmentManager , "ReleaseDialog" );	
       			
	}
	
	@Override
    public void onDialogPositiveClick(DialogFragment dialog) {
//
//		User click OK
//    	
    	Install();
    }
    
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
//
//		User click Cancel
//
    	finish();
    }
    
    
	private void Install()
	{
//
//		This method performs the install
//
		mLog = new StringBuffer();
		
		mAppsHelper = new AppsHelper( mContext , mAppPath );
		mCategoriesHelper = new CategoriesHelper( mContext , mAppPath );
		
		Log("Install","Starting");
		Log("Android Build",mLibAndroid.getmAndroidRelease());
		
		mtvAppDetails.setText(mAppDetails);
		
		
		mInstallStatus = mStatusWorking;
		mtvInstallStatus.setText(mInstallStatus);
		
		if ( mRunType.equals("new") ) 
		{

			mCreateFolderStatus = mStatusWorking;
			mtvFolderStatus.setText(mCreateFolderStatus);
			
			Log("Create Folders","Starting");
			
			if ( CreateFolders() ) {
				mCreateFolderStatus = mStatusComplete;
				mtvFolderStatus.setText(mCreateFolderStatus);
				Log("Create Folders","Completed");			
			}
				else 
			{
				mCreateFolderStatus = mStatusFailed;
				mtvFolderStatus.setText(mCreateFolderStatus);
				mInstallStatus = mStatusFailed;
				mtvInstallStatus.setText(mInstallStatus);
				Log("Create Folders","Failed");
				InstallFailed();
			}
		}

		Log("Copy Categories","Starting");
			
		if ( CopyCategories() ) 
		{
				Log("Copy Categoriess","Completed");
				Log("Update Status","Starting");		
				if ( UpdateStatus() ) {
					Log("Update Status","Completed");		
					InstallComplete();
				} else {
					Log("Update Status","Failed");		
					InstallFailed();	
				}				
				
		} else {
				Log("Copy Categories","Failed");
				InstallFailed();
		}
			
						
	}

	private void InstallComplete()
	{
		mInstallStatus = mStatusComplete;
		mtvInstallStatus.setText(mInstallStatus);
		
		Log("Install","Completed");
		mLibActivity.LongToast("Install Completed");
	}
	
	private void InstallFailed()
	{
		mInstallStatus = mStatusFailed;
		mtvInstallStatus.setText(mInstallStatus);
		
		Log("Install","Failed");
		mLibActivity.LongToast("Install Failed");		
	}

	private void Log(String function,String status)
	{
		
		mLog.append(function + " : " + status + mNewLine);
		mInstallLog.setText(mLog);
	}
	
	private void LogCategory(String string)
	{
		
		mLog.append(string + mNewLine);
		mInstallLog.setText(mLog);
	}
	
	private String GetRunType()
	{
		mFoldersPath = new File(mFoldersBasePath,mFoldersAppPath);

		mtvFolderDetails.setText(mFoldersPath.toString());

		if ( mFoldersPath.exists() ) {
			mRunType = "update";
		} else {
			mRunType = "new";
		}
		return (mRunType);
		
	}
	
	private boolean CreateFolders()
	{
		
		
		if ( CreateFolder() ) {
			Log("Create Folders", mFoldersSuccess );
			return (true);			

		} else {
			Log("Create Folders", mFoldersSevereError );			
			return (true);
		}
		
	}

	private Boolean CreateFolder() {
//		
//	Method to create new folder
//		
		if ( mFoldersPath.mkdir() ) {
			Log("Create Folder","Success");			
			return (true);
		}
		else {			
			Log("Create Folder","Failed");			
			return (false);
		}
	}
	

	
	private boolean CopyCategories()
	{
		mContext = this;
		
		loadConfig();
		
		
	    for (int i = 0; i < mCategoryCount; i++) {	
	    	Category mCategory = mCategories.get(i);
	    	CopyCategory(mCategory);
	    }		
	    
	    saveConfig();
	    
		return (true);
	}
	
	private boolean loadConfig() {

		mCategories = mCategoriesHelper.LoadFromXML();
		
		if ( mCategories != null ) {
			mCategoryCount = mCategories.Count();
			return (true);
		} else {
			return (false);
		}
			
	}
	
	private void saveConfig() {
		
		mCategoriesHelper.SetCategories(mCategories);
	    mCategoriesHelper.SaveToFile();
		
	}

	private boolean CopyCategory(Category category)
	{
		
		mFilename = category.filename;
		
		if ( loadApps(mFilename) ) {

			if ( mRunType.equals("new"))
			{
				saveApps(mFilename);
				LogCategory("Copy category " + category.name);
				
			}
			if ( mRunType.equals("update"))
			{
				if ( !mAppsHelper.FileExists(mFilename) )
				{
					saveApps(mFilename);
					LogCategory("Copy category " + category.name);					
				}
			}
		}
		
		return(true);
	}
		
	private boolean loadApps(String fileName) {
//
//		Load the Applications from the category file
//
		mAppsList = mAppsHelper.LoadFromXML(fileName);
		
		if ( mAppsList != null ) {

		    return(true);
		}
		else {
			return(false);
		}
	}

	private void saveApps(String filename) {
//		
//		Save the Applications to the category file
//		

		mAppsHelper.SetListToApps(mAppsList);
	    mAppsHelper.SaveToFile(filename);
		
	}
		
	private boolean UpdateStatus()
	{
//		
// Method to update shared preferences to set restore to false
//		
		try {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
	        
			editor.putBoolean( "restore" , false );
	        editor.commit();
		
		} catch ( Exception e) {
			return(false);
		}
        		
		return (true);
	}
}
