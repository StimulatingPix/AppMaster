/**
 * 
 */
package com.taa.android.app.master;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Android
 *
 *		Library class to handle toasts
 *
 */
public class LibraryToast {

	private Context mContext;
	
	public LibraryToast(Context context)
	{
		mContext = context;
	}
	
	public void Toast(String message,int length)
	{
		switch (length) {
			case Toast.LENGTH_LONG:
				Toast.makeText(mContext, message , Toast.LENGTH_LONG).show();
				break;
			case Toast.LENGTH_SHORT:
				Toast.makeText(mContext, message , Toast.LENGTH_SHORT).show();
		}
		
	}
}
