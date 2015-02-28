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
 * 		Applications Adapter used in the Launcher Activity
 *
 */
public class LauncherAppAdapter extends ArrayAdapter<App> 
{

	
	private LayoutInflater minflater;
	private ArrayList<App> mList;

	public LauncherAppAdapter(Activity context, ArrayList<App> AppsList) {
		super(context, R.layout.launcher_list, AppsList);
		minflater = context.getLayoutInflater();
		this.mList = AppsList;	            

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final App app = mList.get(position);

		if (convertView == null) {
			convertView = minflater.inflate(R.layout.launcher_list, parent, false);
		}

		final ImageView mIcon = (ImageView) convertView.findViewById(R.id.iv_launcher_icon);
		final TextView mName = (TextView) convertView.findViewById(R.id.tv_launcher_name);

		mIcon.setImageDrawable(app.icon);
		mName.setText(app.name);	            	

		return convertView;
	}
}	