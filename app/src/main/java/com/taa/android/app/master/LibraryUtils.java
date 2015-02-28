/**
 * 
 */
package com.taa.android.app.master;

import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author Android
 * 
 * 		Library class of Utility functions
 *
 */
public class LibraryUtils {
	
	private Context mContext;
				
//	Device
	public LibraryDevice mDevice;
	
	public LibraryUtils (Context context ) 
	{		
		mContext = context;
	}
	
	
	public void getDevice() {
//
//		Get device information
//		
		WindowManager mWindow = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display mDisplay = mWindow.getDefaultDisplay();
		mDevice = new LibraryDevice( mContext, mDisplay , mContext.getResources().getConfiguration());
		
	}	
	

	@SuppressLint("UseValueOf")
	public String IntegerToString (int i) {
//
//		Converts an integer to String
//
		String s = new Integer(i).toString();
		
		return(s);
	}
	
	@SuppressLint("UseValueOf")
	public String BooleanToString(boolean b)
	{
//
//		Converts a boolean to string
//		
		String s = new Boolean(b).toString();
		return (s);
		
	}

	public Boolean StringToBoolean(String s)
	{
//
//		Convert a string to boolean
//		
		boolean b = Boolean.valueOf(s);
		return (b);
	}
	
	public String ToStrDateTime(long time) 
	{
//
//		Converts long Time into String Date Time
//		
	    Date mDateTime = new Date(time);
	    String mstrDateTime = mDateTime.toString();
	    return (mstrDateTime);
	}
	   	
}
