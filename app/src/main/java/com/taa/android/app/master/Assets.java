/**
 * 
 */
package com.taa.android.app.master;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

/**
 * @author Android
 *
 *	Class to access Assets
 *
 */
public class Assets {

	private Context mContext;
	private AssetManager mBoss;
	private String mTag;
	private String mMessage;
	
	public Assets(Context c) {
		
		mContext = c;
		mBoss = mContext.getAssets();
		
	}
	
	public Bitmap GetImage(String fileName) {
		Bitmap mBitmap;
		mBitmap = null;
        try {
            InputStream open = mBoss.open(fileName);
            mBitmap = BitmapFactory.decodeStream(open);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (mBitmap);
	}
	
	public String Read(String fileName) {
		// Read from file and return as string

	    InputStream mInput;
		String mString = null;

	  try {
		  	mInput = mBoss.open(fileName);

	          int size = mInput.available();

	          byte[] buffer = new byte[size];

	          mInput.read(buffer);

	          mInput.close();

	          // convert byte buffer into a string

	          mString = new String(buffer);

	  } catch (IOException e) {

	   // TODO Auto-generated catch block
			mMessage = mTag + " Read " + fileName + " file not found ";
			Toast.makeText(mContext , mMessage , Toast.LENGTH_LONG).show();

	   e.printStackTrace();

	  }

		return (mString);		
	}
}
