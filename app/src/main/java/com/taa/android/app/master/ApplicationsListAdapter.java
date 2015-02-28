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
 * 		Applications List Adapter used in the Applications Activity
 *
 */
public class ApplicationsListAdapter extends ArrayAdapter<App> 
{

	private final LayoutInflater minflater;
	private ArrayList<App> mApps;

 	
    public ApplicationsListAdapter(Activity activity, ArrayList<App> apps) 
    {
         super(activity, R.layout.application_list, apps);	
         minflater = activity.getLayoutInflater();
         this.mApps = apps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final App mApp = mApps.get(position);

        if (convertView == null) {
            convertView = minflater.inflate(R.layout.application_list, parent, false);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_icon);
        imageView.setImageDrawable(mApp.icon);
        final TextView mName = (TextView) convertView.findViewById(R.id.tv_title);
        mName.setText(mApp.name);
        final TextView mPackage = (TextView) convertView.findViewById(R.id.tv_package);
        mPackage.setText(mApp.packagename);
        final TextView mInstalled = (TextView) convertView.findViewById(R.id.tv_installed);
        mInstalled.setText(mApp.strDateTimeInstall);
        final TextView mUpdated = (TextView) convertView.findViewById(R.id.tv_lastupdated);
        mUpdated.setText(mApp.strDateTimeLastUpdate);

        return convertView;
    }	
}

