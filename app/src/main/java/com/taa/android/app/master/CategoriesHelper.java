/**
 * 
 */
package com.taa.android.app.master;

import java.io.File;
import java.util.ArrayList;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;

/**
 * @author Android
 *
 *		Categories Helper class
 *
 */

public class CategoriesHelper {
	
	private Context mContext;
	private String mAppPath;
	
    private Categories mCategories;
    private ArrayList<Category> mCategoriesList = new ArrayList<Category>();
    private Category mCategory;
    
    private int mCount;
    
    private Assets mAssets;
    private String mXML;
    private String mFileName = "config.xml";	

    public CategoriesHelper(Context context, String path ) {
    	
    	mContext = context;
    	mAppPath = path;
    }
    
	public ArrayList<Category> loadConfig(boolean loadfromAssets) 
	{
		if ( loadfromAssets ) {
			mCategories = LoadFromXML();
		} else {
			mCategories = LoadFromFile();
		}
		
		if ( mCategories != null ) {
			mCount = mCategories.Count();
			 
	        mCategoriesList = new ArrayList<Category>(mCount);
			
			mCategoriesList.clear();
			
		    for (int i = 0; i < mCount; i++) 
		    {	
		    	mCategory = mCategories.get(i);
		    	mCategoriesList.add(mCategory);
		    }
		    return(mCategoriesList);			
		}
		return(null);
	}    
    
    public Categories LoadFromXML()
    {
//
//		Read the Categories.xml in Assets into an XMl string
//
    	
    	mAssets = new Assets(mContext);
	
    	mXML = mAssets.Read(mFileName);

//
//		Parse the XML file into the profiles class
//
    	
    	Serializer serial = new Persister();
	
    	try 	
    	{	
    		mCategories = serial.read(Categories.class, mXML);
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return (mCategories);
    }
    
    public Categories LoadFromFile()
    {
//
//		Load the Categories from config.xml on the SDcard
//    	
	   	Serializer serial = new Persister();
	   	
    	File sdcardFile = new File(mAppPath + mFileName );

    	Categories mCategories = new Categories();

    	try {
				serial.read( mCategories , sdcardFile);
				
			} catch (Exception e) {
							
				e.printStackTrace();
			}    	
    	return(mCategories);
    }
    
	public void SaveToFile()
	{
//
//		Save the Categories to config.XML on the SDCard
//		
	   	Serializer serial = new Persister();
	   	
    	File sdcardFile = new File(mAppPath + mFileName );

    	try {
				serial.write( mCategories , sdcardFile);
				
			} catch (Exception e) {
							
				e.printStackTrace();
			}
	}
	
	public void SetCategories(Categories categories)
	{
		mCategories = categories;
	}
}
