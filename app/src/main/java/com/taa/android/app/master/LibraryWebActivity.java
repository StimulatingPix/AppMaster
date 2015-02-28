package com.taa.android.app.master;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Android
 * 
 * 		Library class to display a web view
 * 
 */
public class LibraryWebActivity extends Activity {
		
	private static LibraryActivity mLibActivity;
	private static LibraryPreferences mLibPrefs;
	
	private static String mUrl;
	private static String mError;
	
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
//		
// executed when the activity is created
//
		super.onCreate(savedInstanceState);
		
		mLibActivity = new LibraryActivity( this , "Web Activity" );
		mLibPrefs = new LibraryPreferences( this );
		mLibPrefs.getPrefs();
		mError = getString(R.string.err_no_internet);
//
// get the Extras passed via the Intent
//		
		Intent sender=getIntent();
        mUrl=sender.getExtras().getString("Url");
       
		setContentView(R.layout.activity_library_web);
		
        if ( mLibPrefs.displayDiagnostics() ) 
        {
        	mLibActivity.ShowToast("Url " + mUrl);
        }
        
        if (LibraryNetStatus.getInstance(this).isOnline(this)) 
        {
        	ShowUrl(mUrl);		
        } else {     		
        	mLibActivity.ShowToast(mError);
        }             
	}
	
	
	@SuppressLint("SetJavaScriptEnabled") private boolean ShowUrl(String url) 
	{
//
// 		method to load the URL via the web view
//		
	    mWebView = (WebView) findViewById(R.id.webview);
        mWebView.clearCache(true);
	    mWebView.setWebViewClient(new mWebViewClient());

        mWebView.getSettings().setJavaScriptEnabled(true);
                       
        mWebView.loadUrl(url);
        
    	return true;
    }


	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }            
    
    
    private class mWebViewClient extends WebViewClient 
    {
    	@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
        	return true;
        }
    }    

}
