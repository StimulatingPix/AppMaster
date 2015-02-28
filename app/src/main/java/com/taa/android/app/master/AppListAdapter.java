
package com.taa.android.app.master;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @author Android
 * 
 * 		App List Adapter used in the Configure Activity
 *
 */
public class AppListAdapter extends ArrayAdapter<App> 
{

	private final LayoutInflater minflater;
	private ArrayList<App> mApps;
	
     public AppListAdapter(Activity activity, ArrayList<App> apps) 
     {

         super(activity, R.layout.application_list, apps);
         minflater = activity.getLayoutInflater();
         this.mApps = apps;
    	 
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) 
     {
         final App app = mApps.get(position);

         if (convertView == null) {
             convertView = minflater.inflate(R.layout.app_list, parent, false);
         }

         final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_app_icon);
         imageView.setImageDrawable(app.icon);
         final TextView mName = (TextView) convertView.findViewById(R.id.tv_app_name);
         mName.setText(app.name);

         return convertView;
     }
}
