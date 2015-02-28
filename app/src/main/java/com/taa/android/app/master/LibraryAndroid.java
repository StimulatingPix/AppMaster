/**
 * 
 */
package com.taa.android.app.master;

import android.os.Build;

/**
 * @author Android
 * 
 * 		Library class for Android
 *
 */
public class LibraryAndroid {

	private String mAndroidRelease;
	private int mAndroidRuntime;	

	public LibraryAndroid() 
	{
		setmAndroidRelease(Build.VERSION.RELEASE);
		setmAndroidRuntime(Build.VERSION.SDK_INT);	
	}
	
	public String getmAndroidRelease() {
		return mAndroidRelease;
	}

	public void setmAndroidRelease(String mAndroidRelease) {
		this.mAndroidRelease = mAndroidRelease;
	}

	public int getmAndroidRuntime() {
		return mAndroidRuntime;
	}

	public void setmAndroidRuntime(int mAndroidRuntime) {
		this.mAndroidRuntime = mAndroidRuntime;
	}	
}
