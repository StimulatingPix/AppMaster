package com.taa.android.app.master;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * @author Android
 *
 *		Activity to organise which applications are available in each category
 *		Uses features of the ActionBar which are only available from API 11
 */
public class OrganiseActivity extends FragmentActivity implements OnItemSelectedListener , AppSortNameDialogFragment.NoticeDialogListener , AppsRemoveDialogFragment.NoticeDialogListener
{
	private static LibraryAndroid mLibAndroid;
	private static LibraryActivity mLibActivity;
	private static LibraryApplication mLibApplication;
	private static LibraryPreferences mLibPrefs;
	
	private String mNewLine = System.getProperty("line.separator");

	private static String mAppPath;

	private static final int GET_META_DATA = 128;
	//
//	Installed Applications
//
	private static ArrayList<App> mAppList;
	private static int mAppCount;
	private static String [] mAppNames = new String[0];
	private static ApplicationInfo mAppInfo;
	private static String mAppFilename = "applications.xml";
	
//
//	Applications in each category
//
	private static ArrayList<App> mAppCategoryList;
	private static int mAppCategoryCount;
//
//General
//
	private AppsSelectAdapter mAppCategoryAdapter;
	private ActionBar mActionBar;
	private String mFilename;
	private Context mContext;
	private ListView mListApps;
	private String mAppName;
	private String mErrAppInCategory;
//
//	Categories
//

	private static ArrayList<Category> mCategoryList;
	private	static Category mCategory;
	private static int mCategoryCount;
	private static int mCategoryIndex;

	private static String [] mCategoryNames = new String[0];

	private ArrayAdapter<String> mCategoriesAdapter;
	private ArrayAdapter<String> mAppsAdapter;
	
	private AppsHelper mAppsHelper;
	private CategoriesHelper mCategoriesHelper;
	private Spinner mSpinnerCategories;
	private Spinner mSpinnerApps;
	
	private FragmentManager mFragmentManager;

	private AppSortNameDialogFragment mSortDialog;
	private AppsRemoveDialogFragment mRemoveDialog;
	private StringBuffer mRemove;

	private String mDialogTag;
	private static String mAppSort;

	@SuppressLint("NewApi")
	private void initActivity()
	{
		mContext = this;
		
		mLibAndroid = new LibraryAndroid();
		mLibApplication = new LibraryApplication(mContext);
		mLibActivity = new LibraryActivity(mContext, "Organise Activity");
		mLibPrefs = new LibraryPreferences(mContext);
//		mLibUtils = new LibraryUtils(mContext);
		
		mAppPath = mLibApplication.getmAppPath();
				
        mLibPrefs.getPrefs();

		
		mFragmentManager = getSupportFragmentManager();
        
		mAppsHelper = new AppsHelper( mContext , mAppPath );
		mCategoriesHelper = new CategoriesHelper( mContext , mAppPath );

		if ( mLibAndroid.getmAndroidRuntime() >= Build.VERSION_CODES.HONEYCOMB ) {
	        mActionBar = getActionBar();	
			mLibActivity.SetActionBarSubTitle(mLibPrefs, mActionBar);			
		}
        loadStrings();
	}
	
	private void loadApplications() 
	{
//
//		Loads the installed applications in mAppList.
//
//		If the file applications.xml exists on the SD card load the application	from the file
//		Other wise load the applications from installed applications
//
		if ( mAppsHelper.FileExists(mAppFilename) ) {
			mAppList = mAppsHelper.LoadFromFile(mAppFilename);
			if ( mLibPrefs.displayDiagnostics()) {
				mLibActivity.ShowToast("loadApplications from " + mAppFilename);
			}
		} else {
			mAppList = mAppsHelper.loadApplications();
			if ( mLibPrefs.displayDiagnostics()) {
				mLibActivity.ShowToast("loadApplications from installed applications");
			}			
		}
		
		mAppList = mAppsHelper.sortApplications("name_asc");
		mAppCount = mAppList.size();
		mAppNames = new String[mAppCount+1];
			mAppNames[0] = "Add Application";
		for ( int i=0; i < mAppCount; i++ ) {
			App mApp = mAppList.get(i);
			mAppNames[i+1] = mApp.name;
		}
	}

	private void loadCategory(Category category) 
	{
//
//		Loads the category
//
		mFilename = category.filename;
		

		loadCategoryApps(mFilename);
		
	}

	private void loadCategoryApps(String filename) 
	{
//
//		Load the applications from the category file
//		
		PackageManager manager = getPackageManager();

		ArrayList<App> mAppsLoaded = mAppsHelper.LoadFromFile(filename);
		
		mAppCategoryCount = mAppsLoaded.size();
		mAppCategoryList = new ArrayList<App> (mAppCategoryCount);
		
	    for (int i = 0; i < mAppCategoryCount; i++) 
	    {	
	    	App mApp = mAppsLoaded.get(i);
	    	
	    		try {
					mAppInfo = manager.getApplicationInfo(mApp.packagename,GET_META_DATA);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	    		
		    	mApp.icon = manager.getApplicationIcon(mAppInfo);
		    	mApp.setSelected(false);
	    		mAppCategoryList.add(mApp);
	    }
	}
	
	private void loadConfig() 
	{
//
//		Load the categories from config.xml file
//
		boolean loadfromAssets; 

		if ( mLibPrefs.getmRestore() ) {
			loadfromAssets = true;
		} else {
			loadfromAssets = false;
		}

		mCategoryList = mCategoriesHelper.loadConfig(loadfromAssets);
		mCategoryCount = mCategoryList.size();
		mCategoryNames = new String[mCategoryCount];

		for (int i = 0; i < mCategoryCount; i++) {	
			mCategory = mCategoryList.get(i);
			mCategoryNames[i] = mCategory.name;
		}
	}
	
    private void loadStrings()
    {
		mErrAppInCategory = mContext.getString(R.string.err_app_in_category);
    }
	
	private void saveCategoryApps() 
	{
//
//	Save the applications for a category
//	
		mAppsHelper.SetListToApps(mAppCategoryList);
		mAppsHelper.SaveToFile(mFilename);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	
		initActivity();
        
        setContentView(R.layout.activity_organise);
        
		loadConfig();
//
//		display the categories in the spinner control
//
		
		mSpinnerCategories = (Spinner) findViewById(R.id.org_spinner_categories);		
		mCategoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCategoryNames);		
		mSpinnerCategories.setAdapter(mCategoriesAdapter);
		mSpinnerCategories.setOnItemSelectedListener(this);
		
		loadApplications();
		
		mSpinnerApps = (Spinner) findViewById(R.id.org_spinner_apps);
//		mAppsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item , mAppNames);
		mAppsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item , mAppNames);
		mSpinnerApps.setAdapter(mAppsAdapter);
//		mSpinnerApps.setSelected(false);
		mSpinnerApps.setOnItemSelectedListener(this);
		
		
//
//		set the current category
//
		mCategoryIndex = mCategoriesAdapter.getPosition(mLibPrefs.getmCategory());
		mSpinnerCategories.setSelection(mCategoryIndex);
		
		mListApps = (ListView) findViewById(R.id.org_applist);
		mLibPrefs.setPrefs( "category" , mLibPrefs.getmCategory());
		
		mLibActivity.SetActionBarSubTitle(mLibPrefs, mActionBar);

		Category mCategory = mCategoryList.get(mCategoryIndex);

		DisplayCategoryApps(mCategory);
		
	}

	private void DisplayCategoryApps(Category category)
	{
		loadCategory(category);
		
		mAppCategoryAdapter = new AppsSelectAdapter( OrganiseActivity.this , mAppCategoryList);
	
		mListApps.setAdapter(mAppCategoryAdapter);
			
	}
	
	private void SetCategory(int position)
	{
		mLibPrefs.setmCategory(mCategoryNames[position]);
		mLibPrefs.setPrefs( "category" , mLibPrefs.getmCategory());
		mLibActivity.SetActionBarSubTitle(mLibPrefs, mActionBar);

		Category mCategory = mCategoryList.get(position);
			
		DisplayCategoryApps(mCategory);
		
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
//		Spinner item selected
		switch ( parent.getId())
		{
			case R.id.org_spinner_apps:		if ( position > 0 ){
												addAppToCategory(position);
											}
												break;
			case R.id.org_spinner_categories: 	SetCategory(position);
												break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) 
	{
	//	Spinner no item selected 
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
//
// Inflate the menu; this adds items to the action bar if it is present.
//
		getMenuInflater().inflate(R.menu.organise, menu);
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
  	  			case R.id.itemAppsSort:
  	  			{
  	  				showDialogAppsSort();
  	  				
  	  				return(true);
  	  			}
  	  			case R.id.itemAppsRemove:
  	  			{
  	  				showDialogAppsRemove();
  	  				
  	  				return(true);
  	  			}
	
  	  }
			return(false);
    }
		
		private void showDialogAppsSort()
		{
			if ( mLibPrefs.displayDiagnostics())  {
				mLibActivity.ShowToast("showDialogAppsSort");
			}
		
//			This did not work threw exception in PhoneWindow.requestFeature(int) at line 229
					    
		    mSortDialog = new AppSortNameDialogFragment();
	        
		    
		    mSortDialog.show( mFragmentManager , "SortDialog" );	
	       	
		}
		
		private void showDialogAppsRemove() 
		{
			if ( mLibPrefs.displayDiagnostics())  {
				mLibActivity.ShowToast("showDialogAppsRemove ");
			}

			
//			This did not work threw exception in PhoneWindow.requestFeature(int) at line 229
					    
		    mRemoveDialog = new AppsRemoveDialogFragment();
	        showRemoveApps();
	        
		    mRemoveDialog.SetRemove(mRemove.toString());
		    
		    mRemoveDialog.show( mFragmentManager , "RemoveDialog" );	
	       				
		}	

		private void showRemoveApps() {
			
		    mRemove = new StringBuffer();
			
		    
		    for(int i=0;i<mAppCategoryList.size();i++)
		    {
		    	App mApp = mAppCategoryList.get(i);
		    	if( mApp.isSelected() ){
				      mRemove.append(mApp.getName());
				      mRemove.append(mNewLine);
		    	}
		    }
		}
		
		private void RemoveDialogOK() {
			RemoveApps();
		}
		
//		private void RemoveDialogCancel() {
//			
//		}
		
		private void RemoveApps() {

			for(int i=0;i<mAppCategoryList.size();i++)
		    {
		    	App mApp = mAppCategoryList.get(i);
		    	if( mApp.isSelected() ){
				    mAppCategoryList.remove(i);
//				    mApps.Remove(i);
				    i=i-1;
					mAppCategoryAdapter.notifyDataSetChanged();
		    	}
		    }
			saveCategoryApps();
		}
		

	    // The dialog fragment receives a reference to this Activity through the
	    // Fragment.onAttach() callback, which it uses to call the following methods
	    // defined by the NoticeDialogFragment.NoticeDialogListener interface
	    @Override
	    public void onDialogPositiveClick(DialogFragment dialog) {
	        // User touched the OK button
	    	mDialogTag = dialog.getTag();
	    	
			if ( mLibPrefs.displayDiagnostics())  {
				mLibActivity.ShowToast(" Click OK " + mDialogTag);
			}
	    		    	
	        if ( mDialogTag.equals("SortDialog") ) {
	        	SortDialogOK();
	        }
	        if ( mDialogTag.equals("RemoveDialog") ) {
	        	RemoveDialogOK();
	        }
	    }
	    
		@Override
		public void onDialogNegativeClick(DialogFragment dialog) {
			// TODO Auto-generated method stub
			
		}
		
	    private void SortDialogOK() {
	    	mAppSort = mSortDialog.getSortBy();
		    
		    if ( mAppSort != null ) {
		    	sortApplications(mAppSort);
		    	mAppCategoryAdapter.notifyDataSetChanged();
		    	saveCategoryApps();
		    }
				    	
	    }
	    
		private void sortApplications (String field) 
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

			if ( field.equals("name_asc")) {

				Collections.sort(mAppList, NAME_ORDER_ASC);
			}
			if ( field.equals("name_desc")) {

				Collections.sort(mAppList, NAME_ORDER_DESC);
			}

		}
		
		private boolean addAppToCategory(int position)
		{
//				
//		Add the application to the category
//
			mAppName = mAppNames[position];
			
			if ( !checkAppInCategory(mAppName) ) 
			{
				App mApp = getApp(mAppName);
				mApp.selected = false;
				mAppCategoryList.add(mApp);
				Collections.sort(mAppCategoryList);
				mAppCategoryAdapter.notifyDataSetChanged();
				
				mAppsHelper.SetAppList(mAppCategoryList);
				mAppCategoryList = mAppsHelper.sortApplications("name_asc");
				saveCategoryApps();
				return true;
			}
			else
			{
				mLibActivity.LongToast(mErrAppInCategory);
				return false;
			}

		}

	   private App getApp(String name)
	   {
//
//			get the App with supplied name in list of installed applications
//
		   if ( mAppList != null ) {
			   
			   mAppCount = mAppList.size();
			   
			   for ( int i=0; i < mAppCount; i++ ) {
				   App mApp = mAppList.get(i);
				   if (mApp.name.equals(name) ){
					   return (mApp);
				   }
			   }
				   
		   }
		   return null;
	   }
		   
	   private boolean checkAppInCategory(String name)
	   {			   
//
//		Checks if the Application is in the Category
//
		   boolean mInCategory = false;
		   
		   if ( mAppCategoryList != null ) {
			   
			   mAppCount = mAppCategoryList.size();
			   
			   for ( int i=0; i < mAppCount; i++ ) {
				   App mApp = mAppCategoryList.get(i);
				   if (mApp.name.equals(name) ) {
					   mInCategory = true;
					   break;
				   }
			   }
		   }
		   
		   return (mInCategory);
	   }		
}
