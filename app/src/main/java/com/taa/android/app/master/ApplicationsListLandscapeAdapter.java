package com.taa.android.app.master;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Build;
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
public class ApplicationsListLandscapeAdapter extends ArrayAdapter<App> 
{
	private LibraryAndroid mLibAndroid;
	private final LayoutInflater minflater;
	private ArrayList<App> mApps;

	private final static int INVISIBLE = 4;
 	
    public ApplicationsListLandscapeAdapter(Activity activity, ArrayList<App> apps) 
    {
         super(activity, R.layout.application_list_landscape, apps);	
         minflater = activity.getLayoutInflater();
         this.mApps = apps;
         mLibAndroid = new LibraryAndroid(); 
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final App mApp = mApps.get(position);

        if (convertView == null) {
            convertView = minflater.inflate(R.layout.application_list_landscape, parent, false);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_icon);
        imageView.setImageDrawable(mApp.icon);
        final TextView mName = (TextView) convertView.findViewById(R.id.tv_title);
        mName.setText(mApp.name);
        final TextView mPackage = (TextView) convertView.findViewById(R.id.tv_package);
        mPackage.setText(mApp.packagename);
        if ( mLibAndroid.getmAndroidRuntime() >= Build.VERSION_CODES.GINGERBREAD ) {
            final TextView mInstalled = (TextView) convertView.findViewById(R.id.tv_installed);
            mInstalled.setText(mApp.strDateTimeInstall);
            final TextView mUpdated = (TextView) convertView.findViewById(R.id.tv_lastupdated);
            mUpdated.setText(mApp.strDateTimeLastUpdate);        	
        } else {
            final TextView mInstalled = (TextView) convertView.findViewById(R.id.tv_installed);
            final TextView mUpdated = (TextView) convertView.findViewById(R.id.tv_lastupdated);
            final TextView mInstalledLabel = (TextView) convertView.findViewById(R.id.tv_label_installed);
            final TextView mUpdatedLabel = (TextView) convertView.findViewById(R.id.tv_label_lastupdated);
        	mInstalled.setVisibility(INVISIBLE);
        	mUpdated.setVisibility(INVISIBLE);
        	mInstalledLabel.setVisibility(INVISIBLE);
        	mUpdatedLabel.setVisibility(INVISIBLE);
        }
        return convertView;
    }	
}

