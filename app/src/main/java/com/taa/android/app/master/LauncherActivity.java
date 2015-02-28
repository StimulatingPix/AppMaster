package com.taa.android.app.master;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * @author Android
 *
 *		Activity class to launch applications
 */
public class LauncherActivity extends Activity implements OnClickListener, OnItemSelectedListener , AdapterView.OnItemClickListener {

	private static LibraryAndroid mLibAndroid;
	private static LibraryActivity mLibActivity;
	private static LibraryApplication mLibApplication;
	private static LibraryPreferences mLibPrefs;
	private static LibraryUtils mLibUtils;
		
    private static String mFilename;
    private static String mAppPath;
    private String mErrAppNotLoaded;
    private boolean mAppLoaded;
    
    private static final int GET_META_DATA = 128;
    private static final int INTENT_MAIN = 100;
    
//
//	Categories
//    
    private static ArrayList<Category> mCategoryList;
	private static String [] mCategoryNames = new String[0];
    private static int mCategoryCount;
	private static int mCategoryIndex;    
    private static Category mCategory;
//
//	Apps
//    
    private static ArrayList<App> mAppsList;
    private static int mAppCount;
    
    private static Context mContext;
    
    private ApplicationInfo mAppInfo;
    private AppsHelper mAppsHelper;
    private CategoriesHelper mCategoriesHelper;

    private LauncherAppAdapter mAppsAdapter;
    private ArrayAdapter<String> mCategoriesAdapter;
    private Spinner mSpinner;
    private GridView mGridApp;
    private PackageManager mManager;
    private ImageButton mIBClose;
    private ImageButton mIBLaunch;
    
    private void initActivity()
    {
		mContext = this;
		mLibAndroid = new LibraryAndroid();
		mLibApplication = new LibraryApplication(mContext);
		mLibActivity = new LibraryActivity(mContext, "Launcher Activity");
		mLibPrefs = new LibraryPreferences(mContext);
		mLibUtils = new LibraryUtils(mContext);
		
		mLibUtils.getDevice();
		
		mAppPath = mLibApplication.getmAppPath();
				
        mLibPrefs.getPrefs();
        
		mAppsHelper = new AppsHelper( mContext , mAppPath );
		mCategoriesHelper = new CategoriesHelper( mContext , mAppPath );
		
		loadStrings();
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
    
	private void loadCategoryApps(Category category) 
	{
//
//		Load the applications in a Category
//
		mFilename = category.filename;
		

		mManager = getPackageManager();

		ArrayList<App> mAppsLoaded = new ArrayList<App>();
		mAppsLoaded = mAppsHelper.LoadFromFile(mFilename);
		
		if ( mAppsLoaded != null ) {
			
			mAppCount = mAppsLoaded.size();
			mAppsList = new ArrayList<App>(mAppCount);
		    for (int i = 0; i < mAppCount; i++) 
		    {	
		    	App mApp = mAppsLoaded.get(i);
		    	mAppLoaded = true;
		    		try {
						mAppInfo = mManager.getApplicationInfo(mApp.packagename,GET_META_DATA);
					} catch (NameNotFoundException e) {
						mLibActivity.LongToast(mErrAppNotLoaded + mApp.packagename );
						mAppLoaded = false;
					}
		 	    	if ( mAppLoaded ) {
				    	mApp.icon = mManager.getApplicationIcon(mAppInfo);
				    	mApp.setSelected(false);
			    		mAppsList.add(mApp);		 	    		
		 	    	}
		    }
		    
		} else {
			mLibActivity.LongToast(mErrAppNotLoaded + mFilename );
		}
	}
	
    private void loadStrings()
    {
		mErrAppNotLoaded = mContext.getString(R.string.err_app_not_loaded);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	
		super.onCreate(savedInstanceState);
		
		initActivity();
				
		loadConfig();
				
		if ( mLibUtils.mDevice.misPortrait ) {
			setContentView(R.layout.activity_launcher_portrait);
		} else	{
			setContentView(R.layout.activity_launcher_landscape);			
		}
			
		if ( mLibPrefs.displayDiagnostics() ) {
			mLibUtils.mDevice.Display();
			mLibActivity.ShowToast(mLibAndroid.getmAndroidRelease());
		}
		
//		Text View display the current category
//		mtvCategory = (TextView) findViewById(R.id.tv_category);
//		Spinner display the list of categories		
		mSpinner = (Spinner) findViewById(R.id.spinner_launch);
		
//		Grid View display the applications in a category		
		mGridApp = (GridView) findViewById(R.id.gridApps );
//		Button controls
		mIBClose = (ImageButton) findViewById(R.id.ib_close);
		mIBLaunch = (ImageButton) findViewById(R.id.ib_launch);
		mIBClose.setOnClickListener(this);
		mIBLaunch.setOnClickListener(this);
		
//
//		display the categories in the spinner control
//
		displayCategories();

	}
	

	
	private void displayApps() 
	{
//
//		display the applications in the grid
//
		if ( mLibUtils.mDevice.misPortrait ) {
			mGridApp.setNumColumns(3);
		} else {
			mGridApp.setNumColumns(5);			
		}
		mAppsAdapter = new LauncherAppAdapter(this, mAppsList);
		
	    mGridApp.setAdapter(mAppsAdapter);
	    
        mGridApp.setOnItemClickListener(this);				
	}
	
	private void displayCategories() 
	{
//
//		display the categories in the spinner
//
		mCategoriesAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_category, mCategoryNames);
		
		mSpinner.setAdapter(mCategoriesAdapter);
//
//		set the current category
//		
		mCategory = getCategory(mLibPrefs.getmCategory());
		mCategoryIndex = getCategoryIndex(mLibPrefs.getmCategory());
		
		mCategoryIndex = mCategoriesAdapter.getPosition(mCategoryNames[mCategoryIndex]);
		
		mSpinner.setSelection(mCategoryIndex);

		mSpinner.setOnItemSelectedListener(this);
		
		DisplayCategory(mCategoryIndex);	
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
//		
// 		Category selected in the spinner control
//
		DisplayCategory(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) 
	{
//		Spinner no item selected 
		
	}	
//	private void displayTitle()
//	{
//		mtvCategory.setText(mLibPrefs.getmCategory());		
//	}
	
	private void DisplayCategory(int position)
	{
		mLibPrefs.setmCategory(mCategoryNames[position]);
		mLibPrefs.setPrefs("category" , mLibPrefs.getmCategory());
		
		Category mCategory = mCategoryList.get(position);
		
		mFilename = mCategory.filename;

//		displayTitle();
		loadCategoryApps(mCategory);
		displayApps();
	}
	
	private Category getCategory(String name)
	{
//
//		Gets the category with supplied name
//
		for (int i=0; i < mCategoryCount; i++ )
		{
			mCategory = mCategoryList.get(i);
			if (mCategory.name.equals(name)) {
				return (mCategory);
			}
		}
		return(null);
	}

	private int getCategoryIndex(String name)
	{
//
//		Gets the category with supplied name
//
		for (int i=0; i < mCategoryCount; i++ )
		{
			mCategory = mCategoryList.get(i);
			if (mCategory.name.equals(name)) {
				return (i);
			}
		}
		return(0);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
//		
// 		Inflate the menu; this adds items to the action bar if it is present.
//
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
//		   
//		onItemClick for the application grid - Launch the application
//

		App app = (App) parent.getItemAtPosition(position);

		PackageManager pm = this.getPackageManager();

		try
		{
			Intent mIntent = pm.getLaunchIntentForPackage(app.packagename);

			if (null != mIntent)
				this.startActivity(mIntent);
		}

		catch (ActivityNotFoundException e)
		{
			mLibActivity.LongToast("Unable to launch " + app.packagename );
		}
	}
	   
	@Override
	public void onClick(View view) 
	{
//
//		OnClick Listener for launch button		
//		
		switch(view.getId())
		 {
		 	case  R.id.ib_close:
		 	{
		 		finish();
		 		break;
		 	}
		   	case  R.id.ib_launch:
		   	{ 
				LaunchIntent();
		   		break;
		   	}

		 } 		
	}

	private void LaunchIntent()
	{
		//
		//		Launch intent
		//		   
		Intent mIntent = new Intent( LauncherActivity.this , MainActivity.class );
		startActivityForResult( mIntent, INTENT_MAIN );		
		finish();
	}
}
