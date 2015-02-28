package com.taa.android.app.master;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnSharedPreferenceChangeListener {
	
	/////////////////////////////////////
	//	Main Activity for the application	
	/////////////////////////////////////
	
	private static final int INTENT_APPS = 10;
	private static final int INTENT_CONFIGURE = 20;
	private static final int INTENT_HELP = 30;
	private static final int INTENT_LAUNCHER = 40;
	private static final int INTENT_ORGANISE = 50;
	private static final int INTENT_RELEASE = 60;
	private static final int INTENT_SETTINGS = 70;
	private static final int INTENT_STORE = 80;

	private static LibraryAndroid mLibAndroid;
	private static LibraryApplication mLibApplication;
	private static LibraryActivity mLibActivity;
	private static LibraryPreferences mLibPrefs;
	private static LibraryUtils mLibUtils;
	private static LibraryDialogFragment mLibDialogFragment;
	
	private CategoriesHelper mCategoriesHelper;
	private ArrayList<Category> mCategoryList;
	
	private Context mContext;
	private String mAppPath;
	private String mAppError;
	private ListView mListView;
	private CategoriesAdapter mCategoriesAdapter;

	private ActionBar mActionBar;
		
	@SuppressLint("NewApi")
	private void initActivity()
	{
//
//		Use of action bar will only work after API 11
//
		mContext = this;

		mLibAndroid = new LibraryAndroid();
		mLibApplication = new LibraryApplication(mContext);
		mLibActivity = new LibraryActivity(mContext, "Main Activity" );
		mLibPrefs = new LibraryPreferences(mContext);
		mLibUtils = new LibraryUtils(mContext);

	    mLibPrefs.mPrefs.registerOnSharedPreferenceChangeListener(this);

	    mLibPrefs.getPrefs();

		mLibUtils.getDevice();

		mAppPath = this.getString(R.string.app_path);
	
		mCategoriesHelper = new CategoriesHelper(mContext,mAppPath);
		mCategoryList = new ArrayList<Category>();

		if ( mLibAndroid.getmAndroidRuntime() >= Build.VERSION_CODES.HONEYCOMB) {
			mActionBar = this.getActionBar();
			mLibActivity.SetActionBarSubTitle(mLibPrefs, mActionBar);
		} else {
			this.setTitle(mLibActivity.SetTitle(mLibPrefs));
		}
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        initActivity();
        
        setContentView(R.layout.activity_main);
        
       if (!checkFolder()) {
        	
        	StartActivity(INTENT_RELEASE);
        } else {
        	new AppsSaverTask().execute(this);
        	new RegisterAppsTask().execute(this);
        	
        	listCategories();
        }	        
    }

    private boolean checkFolder()
    {
//
//		Check if the application folder exists on the SD card
//
    	File mAppFolder = new File(mAppPath);
    	
    	if ( mAppFolder.exists() ) {
    		return(true);
    	} else {
    		return(false);
    	}
    		
    }
    private void listCategories()
    {
    boolean loadfromAssets; 
    
    if ( mLibPrefs.getmRestore() ) {
    	loadfromAssets = true;
    } else {
    	loadfromAssets = false;
    }
    
       mListView = (ListView) findViewById(R.id.lv_categories);
    
       mCategoryList = mCategoriesHelper.loadConfig(loadfromAssets);
       
       if ( mCategoryList != null ) {
    	   mCategoriesAdapter = new CategoriesAdapter( MainActivity.this , mCategoryList );
    	        
    	   mListView.setAdapter(mCategoriesAdapter);    	
    	   this.setItemClickListener();
       }	else {
    	   mAppError = this.getString(R.string.err_load_config);
    	   mLibActivity.LongToast(mAppError);
       }
        
    }

    private void setItemClickListener()
    {
        // Item Click Listener for the List View
        
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
            	
                // Getting the Container Layout of the ListView
                RelativeLayout relativeLayoutParent = (RelativeLayout) container;
  
                // Getting the Category Name TextView
                TextView tvCategoryName = (TextView) relativeLayoutParent.getChildAt(1);

                // Update the Profile in Shared Preferences
                
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
	   			SharedPreferences.Editor editor = preferences.edit();
	                    
                editor.putString("category", tvCategoryName.getText().toString() );
                      
                editor.commit();
                setCategoriesChanged();

                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                adb.setTitle("Set Category");
                adb.setMessage("Category " + tvCategoryName.getText().toString() );
                adb.setPositiveButton("OK", null);
                adb.show();                
                                
            }
        };
        
        // Setting the item click listener for the List View
        
        mListView.setOnItemClickListener(itemClickListener);    	
    }
    private void setCategoriesChanged()
    {
	    if ( mCategoryList != null ) 
	    {
	    	   mCategoriesAdapter = new CategoriesAdapter( MainActivity.this , mCategoryList );
	    	   mListView.setAdapter(mCategoriesAdapter);    	   
	    }
    	
    }
	public void setPrefsAppCountChanged()
	{
		setCategoriesChanged();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
//    	
// Inflate the menu; this adds items to the action bar if it is present.
//
    	getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//
// Action before menu is displayed
//    	
    	if ( mLibAndroid.getmAndroidRuntime() < Build.VERSION_CODES.HONEYCOMB ) {
            MenuItem item;
            item = menu.findItem(R.id.menu_item_configure);
            item.setEnabled(false);    		
    	}
        
        return true;
    }    	
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
//
// 		executed when menu item is selected
//
    	switch (item.getItemId())
    	{
	    	case R.id.menu_item_launch:
	    	{
	    		StartActivity(INTENT_LAUNCHER);
	    		return(true);
	    	}
	    	case R.id.menu_item_apps:
	    	{
	    		StartActivity(INTENT_APPS);
	    		return(true);
	    	}
	    	case R.id.menu_item_configure:
	    	{
	    		StartActivity(INTENT_CONFIGURE);	  		  
	    		return(true);
	    	}
	    	case R.id.menu_item_organise:
	    	{
	    		StartActivity(INTENT_ORGANISE);	  		  
	    		return(true);
	    	}
	    	case R.id.menu_item_store:
	    	{	
	    		StartActivity(INTENT_STORE);	  		  	  		  		   
	    		return(true);
	    	}
	    	case R.id.menu_item_about:
	    	{	
	    		ShowAbout();	  		   
	    		return(true);
	    	}
	    	case R.id.menu_item_settings:
	    	{
	    		StartActivity(INTENT_SETTINGS);	  		  
	    		return(true);
	    	}
	    	case R.id.menu_item_help:
	    	{
	    		StartActivity(INTENT_HELP);	  		  
	    		return(true);
	    	}	
    	}
		return(false);
    }
    
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
//
//		Change shared preferences
//		
		mLibPrefs.changePrefs(key);
		if (key.equals("appcount")) {
			this.setPrefsAppCountChanged();
		}
		if (key.equals("category")) {
			if ( mLibAndroid.getmAndroidRuntime() >= Build.VERSION_CODES.HONEYCOMB) {
				mLibActivity.SetActionBarSubTitle(mLibPrefs, mActionBar);
			} else {
				this.setTitle(mLibActivity.SetTitle( mLibPrefs));
			}			
		}
	}
    
	private void ShowAbout()
	{
		mLibApplication.SetAbout();
		
        mLibDialogFragment = LibraryDialogFragment.newInstance( mLibApplication.mAboutMessage , mLibApplication.mAboutTitle );
        mLibDialogFragment.show(getSupportFragmentManager(), "AboutDialog");
		
	}
	
	private void StartActivity(int requestCode)
	{
		if ( requestCode == INTENT_RELEASE ) {
			
			Intent mIntent=new Intent(MainActivity.this,ReleaseActivity.class);
			startActivityForResult(mIntent, INTENT_RELEASE );		
		}

		if ( requestCode == INTENT_LAUNCHER ) {
			
			Intent mIntent = new Intent( MainActivity.this , LauncherActivity.class );
			startActivityForResult( mIntent, INTENT_LAUNCHER );
			// Finish the Main Activity
			finish();
		}
		
		if ( requestCode == INTENT_APPS ) {
			
			Intent mIntent = new Intent( MainActivity.this , ApplicationsActivity.class );
			startActivityForResult( mIntent, INTENT_APPS );		
		}

		if ( requestCode == INTENT_CONFIGURE ) {
			
			Intent mIntent = new Intent( MainActivity.this , ConfigureActivity.class );
			startActivityForResult( mIntent, INTENT_CONFIGURE );		
		}

		if ( requestCode == INTENT_ORGANISE ) {
			
			Intent mIntent = new Intent( MainActivity.this , OrganiseActivity.class );
			startActivityForResult( mIntent, INTENT_ORGANISE );		
		}

		if ( requestCode == INTENT_STORE ) {
			
			Intent mIntent = new Intent( MainActivity.this , StoreActivity.class );
			startActivityForResult( mIntent, INTENT_STORE );		
		}

		if ( requestCode == INTENT_SETTINGS ) {
			
			Intent mIntent = new Intent( MainActivity.this , LibrarySettingsActivity.class );
			startActivityForResult( mIntent, INTENT_SETTINGS );		
		}
		
		if ( requestCode == INTENT_HELP ) {
			
			Intent mIntent = new Intent( MainActivity.this , LibraryWebActivity.class);
			mIntent.putExtra("Url", mLibApplication.getmAppUrlHelp());
			
			startActivityForResult( mIntent , INTENT_HELP );					
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//	    	
		//	Get the result from an intent
		//	    	
		if (requestCode == INTENT_RELEASE) {

			mLibPrefs.getPrefs();

			if ( !mLibPrefs.getmRestore() ) {

				listCategories();
				
			} else {

				finish();
			}
		}
		if (requestCode == INTENT_CONFIGURE ) {

			mLibPrefs.getPrefs();
			setCategoriesChanged();
		}
		if (requestCode == INTENT_ORGANISE ) {
			setCategoriesChanged();			
		}
	}    	
	
}
