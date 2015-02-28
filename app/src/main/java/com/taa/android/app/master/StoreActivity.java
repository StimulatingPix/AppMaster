package com.taa.android.app.master;

import java.util.ArrayList;


import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author Android
 *
 *		Activity to display the application category in the Google Play Store
 *
 */
public class StoreActivity extends Activity implements OnItemSelectedListener {

	private CategoriesHelper mCategoriesHelper;
	private LibraryActivity mLibActivity;
	private LibraryApplication mLibApplication;
	private LibraryPreferences mLibPrefs;

	private Context mContext;
	private Spinner mSpinnerStore;
	private WebView mWebViewStore;
	private String mAppPath;
	private String mAppError;
	
	private ArrayAdapter<String> mAdapter;
	private ArrayList<Category> mCategoriesList;
	private static String [] mCategoryNames = new String[0];
	private static String [] mCategoryUrls = new String[0];
	private int mCategoryCount;
	private int mCategoryIndex;
	private boolean doubleBackToExitPressedOnce;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		mContext = StoreActivity.this;
		mLibActivity = new LibraryActivity(mContext,"Store Activity");
		mLibApplication = new LibraryApplication(mContext);
		mLibPrefs = new LibraryPreferences(mContext);
		mLibPrefs.getPrefs();
		mAppPath = mLibApplication.getmAppPath();
		
		mCategoriesHelper = new CategoriesHelper(mContext,mAppPath);
		
		
		setContentView(R.layout.activity_store);
		
		mSpinnerStore = (Spinner)this.findViewById(R.id.spinnerStore);
		mWebViewStore = (WebView)this.findViewById(R.id.webviewStore);
        mWebViewStore.getSettings().setJavaScriptEnabled(true);
		
		try {
			loadCategories();
			
		} catch ( Exception e ) 
		{
	    	   mAppError = this.getString(R.string.err_load_config);
	    	   mLibActivity.LongToast(mAppError);
		}
	}

    private void loadCategories()
    {
//
//		Load the categories into the spinner control
//    	
        boolean loadfromAssets; 
        
        if ( mLibPrefs.getmRestore() ) {
        	loadfromAssets = true;
        } else {
        	loadfromAssets = false;
        }
        
    	mCategoriesList = new ArrayList<Category>();
    	mCategoriesList = mCategoriesHelper.loadConfig(loadfromAssets);

    	if ( mCategoriesList != null ) 
    	{
    		mCategoryCount = mCategoriesList.size();
    		
    		mCategoryNames = new String[mCategoryCount];
    		mCategoryUrls = new String[mCategoryCount];

    		for ( int i=0; i<mCategoryCount; i++ ) {
    			Category mCategory = mCategoriesList.get(i);
    			mCategoryNames[i] = mCategory.name;
    			mCategoryUrls[i] = mCategory.url;
    		}
    		
    		
    		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCategoryNames);
    		
    		mSpinnerStore.setAdapter(mAdapter); 
//
//    		set the current category
//
    		
    		mCategoryIndex = mAdapter.getPosition(mLibPrefs.getmCategory());
    		mSpinnerStore.setSelection(mCategoryIndex);    		
    		mSpinnerStore.setOnItemSelectedListener(this);

    	}	else {
    		mAppError = this.getString(R.string.err_load_config);
    		mLibActivity.LongToast(mAppError);
    	}
        
    }	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store, menu);
		return true;
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
// 		Spinner item selected
		Category mCategory = mCategoriesList.get(position);
		mAppError = getString(R.string.err_no_internet);

        if ( mLibPrefs.displayDiagnostics() ) 
        {
        	mLibActivity.ShowToast("Url " + mCategory.url);
        }
        
        if (LibraryNetStatus.getInstance(this).isOnline(this)) 
        {
        	ShowUrl(mCategory.url);		
        } else {     		
        	mLibActivity.ShowToast(mAppError);
        }             		

	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean ShowUrl(String url) 
	{
//
// 		method to load the URL via the web view
//		
        
	    mWebViewStore.setWebViewClient(new mWebViewClient());
                       
        mWebViewStore.loadUrl(url);
        
    	return(false);
    }
	
	@Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        mLibActivity.ShortToast("Please click BACK again to exit");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
             doubleBackToExitPressedOnce=false;   

            }
        }, 2000);
    } 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebViewStore.canGoBack()) {
			mWebViewStore.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
    private class mWebViewClient extends WebViewClient 
    {
    	@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	return false;
        }
    }    

}


