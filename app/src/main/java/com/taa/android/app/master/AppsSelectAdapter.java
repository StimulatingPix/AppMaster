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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * @author Android
 *
 */
public class AppsSelectAdapter extends ArrayAdapter<App> {
	
 	private ArrayList<App> list;
 	private LayoutInflater inflator;

  	public AppsSelectAdapter(Activity context, ArrayList<App> list) 
	{
  		super(context, R.layout.app_select, list);
  		this.list = list;
  		inflator = context.getLayoutInflater();
 	}

  	@Override
 	public View getView(int position, View convertView, ViewGroup parent) 
	{

   		ViewHolder holder = null;
  		if (convertView == null) {
   			convertView = inflator.inflate(R.layout.app_select, null);
   			holder = new ViewHolder();
   			
//            holder.icon = (ImageView) convertView.findViewById(R.id.iv_sphere_icon);
   			holder.name = (TextView) convertView.findViewById(R.id.tv_app_name);
//            holder.desc = (TextView) convertView.findViewById(R.id.tv_sphere_desc);
   			holder.chk = (CheckBox) convertView.findViewById(R.id.cb_app_select);
   			holder.chk
     			.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

       		@Override
      		public void onCheckedChanged(CompoundButton view, boolean isChecked) {
       				int getPosition = (Integer) view.getTag();
       				list.get(getPosition).setSelected(view.isChecked());

       		}
     		});
   		convertView.setTag(holder);
 //  		convertView.setTag(R.id.iv_sphere_icon, holder.icon);
   		convertView.setTag(R.id.tv_app_name, holder.name);
 //  		convertView.setTag(R.id.tv_sphere_desc, holder.desc);
   		convertView.setTag(R.id.cb_app_select, holder.chk);
  		} else {
   			holder = (ViewHolder) convertView.getTag();
  		}
  		holder.chk.setTag(position);
//        holder.icon.setImageResource(R.drawable.sphere);   			
   		holder.name.setText(list.get(position).getName());
//   		holder.desc.setText(list.get(position).getDesc());
  		holder.chk.setChecked(list.get(position).isSelected());

   		return convertView;
 	}

  	static class ViewHolder {
//  	protected ImageView icon;
  	protected TextView name;
//  	protected TextView desc;
  	protected CheckBox chk;
 	}
}
