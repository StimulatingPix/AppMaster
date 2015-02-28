/**
 * 
 */
package com.taa.android.app.master;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Android
 *
 *		Launcher Spinner Adapter used in the Launcher Activity
 */
public class LauncherSpinnerAdapter extends ArrayAdapter<Category> {
	
	private final LayoutInflater minflater;
	private ArrayList<Category> mCategoryList;

 	
    public LauncherSpinnerAdapter(Activity activity, ArrayList<Category> categoryList ) 
    {
        super(activity, R.layout.spinner_item_category, categoryList);
         minflater = activity.getLayoutInflater();
         this.mCategoryList = categoryList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        final Category mCategory = mCategoryList.get(position);

        if (convertView == null) {
            convertView = minflater.inflate(R.layout.spinner_item_category, parent, false);
        }

        final TextView mCategoryName = (TextView) convertView.findViewById(R.id.tv_spinner_item_category_name);
        mCategoryName.setText(mCategory.name);

        return convertView;
    }

//	public int getPosition(Category mCategory) {
//		// TODO Auto-generated method stub
//		return mCategoryList.indexOf(mCategory);
//	}	
}
