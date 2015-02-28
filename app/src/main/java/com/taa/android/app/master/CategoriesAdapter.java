/**
 * 
 */
package com.taa.android.app.master;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Android
 *
 *		Categories Adapter class
 *
 *		Used in MainActivity to list Categories
 *
 */

public class CategoriesAdapter extends ArrayAdapter<Category> 
{
	private LayoutInflater minflater;
	private ArrayList<Category> mCategoriesList;
	private LibraryApplication mLibApplication;
	private LibraryPreferences mLibPrefs;
	private LibraryUtils mLibUtils;
	private AppsHelper mAppsHelper;
	private String mAppPath;
	
	private Assets mAssets;
	private Bitmap mBitmap;
	
	private final static int VISIBLE = 0;
	private final static int INVISIBLE = 4;
	
	
        public CategoriesAdapter(Activity context, ArrayList<Category> categorieslist) 
        {
            super(context, R.layout.category_list, categorieslist);
            minflater = context.getLayoutInflater();
            this.mCategoriesList = categorieslist;	            
            mAssets = new Assets(context);
            mLibApplication = new LibraryApplication(context);
            mAppPath = mLibApplication.getmAppPath();
            mLibPrefs = new LibraryPreferences(context);
            mLibPrefs.getPrefs();
            mLibUtils = new LibraryUtils(context);
            mAppsHelper = new AppsHelper(context,mAppPath);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
            final Category category = mCategoriesList.get(position);

            if (convertView == null) {
                convertView = minflater.inflate(R.layout.category_list, parent, false);
            }
            
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_category_icon);
            
            mBitmap = mAssets.GetImage(category.icon);
            imageView.setImageBitmap(mBitmap);

            final TextView mTitle = (TextView) convertView.findViewById(R.id.tv_category_name);
            mTitle.setText(category.name);
            final TextView mDescription = (TextView) convertView.findViewById(R.id.tv_category_description);
            mDescription.setText(category.description);
            final TextView mtvAppCount = (TextView) convertView.findViewById(R.id.tv_category_app_count );

            if ( mLibPrefs.getmAppcount() ) 
            {
	            int mAppCount = mAppsHelper.GetAppCount(category.filename);
	            String mstrAppCount = mLibUtils.IntegerToString(mAppCount);
		        mtvAppCount.setText(mstrAppCount);	            	
	            
            	mtvAppCount.setVisibility(VISIBLE);
            } else {
            	mtvAppCount.setVisibility(INVISIBLE);
            }
            
            return convertView;
        }
   }