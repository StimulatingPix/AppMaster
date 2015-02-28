/**
 * 
 */
package com.taa.android.app.master;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * @author Android
 * 
 *		Library class to determine network status
 *
 */
public class LibraryNetStatus {
	
	private static LibraryNetStatus instance = new LibraryNetStatus();
	ConnectivityManager connectivityManager;
	NetworkInfo wifiInfo, mobileInfo;
	static Context context;
	boolean connected = false;
	static LibraryToast mLibToast;
	
    public static LibraryNetStatus getInstance(Context ctx) 
    {
        context = ctx;
        mLibToast = new LibraryToast(context);
        return instance;
    }
    
    public Boolean isOnline(Context con) 
    {
    	try {
    	        connectivityManager = (ConnectivityManager) con
    	                        .getSystemService(Context.CONNECTIVITY_SERVICE);


    	        NetworkInfo networkInfo = connectivityManager
    	                        .getActiveNetworkInfo();
    	        connected = networkInfo != null
    	                        && networkInfo.isAvailable() && networkInfo.isConnected();
    	        return connected;

    		} catch (Exception e) {
    	        mLibToast.Toast("Error determining network status", Toast.LENGTH_LONG );
    		}

    	return connected;
    }    			
}
