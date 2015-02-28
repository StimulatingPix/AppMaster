/**
 * 
 */
package com.taa.android.app.master;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

/**
 * @author Android
 *
 *	Library class for android device
 *
 */
public class LibraryDevice {

	public Context mContext;
	public Display mDisplay;
	public Configuration mConfiguration;
	
	public int mOrientation;
	public float mXDPI;
	public float mYDPI;
	
	public int mWidthPX;
	public int mHeightPX;
	
	public Point mPoint;
	
	public boolean misPortrait;
	public boolean misLandscape;
	
	public String mTag = "Device ";
	public String mMessage;
	
	public LibraryDevice (Context context,Display display,Configuration config )
	{
		mContext = context;
		mDisplay = display;
		mConfiguration = config;

		DisplayMetrics metrics = new DisplayMetrics();
		mDisplay.getMetrics(metrics);
		
		mWidthPX = metrics.widthPixels;
		mHeightPX = metrics.heightPixels;
		mXDPI = metrics.xdpi;
		mYDPI = metrics.ydpi;
		
		mOrientation = config.orientation;
		if (mOrientation == Configuration.ORIENTATION_UNDEFINED) 
		{	
			if ( mWidthPX < mHeightPX ) 
			{
				mOrientation = Configuration.ORIENTATION_PORTRAIT;
			} else 
			{
				mOrientation = Configuration.ORIENTATION_LANDSCAPE;
				misPortrait = false;
				misLandscape = true;
			}
		}
		if ( mOrientation == Configuration.ORIENTATION_PORTRAIT )
		{
			misPortrait = true;
			misLandscape = false;			
		}
		if ( mOrientation == Configuration.ORIENTATION_LANDSCAPE )
		{
			misPortrait = false;
			misLandscape = true;			
		}
	}
		
	public void Display()
	{
		mMessage = mTag + " Orientation " + mOrientation + 
							" Width " + mWidthPX + " PX " +
							" Height " + mHeightPX + " PX " +
							" X " + mXDPI + " DPI " +
							" Y " + mYDPI + " DPI ";
						
		
    	Toast.makeText( mContext , mMessage , Toast.LENGTH_LONG).show();
		
	}	
}
