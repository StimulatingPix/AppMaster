/**
 * 
 */
package com.taa.android.app.master;

import java.util.Date;

import org.simpleframework.xml.Element;

import android.graphics.drawable.Drawable;

/**
 * @author Android
 *
 *	class App
 *
 */
@Element(name="app")

public class App implements Comparable <App> {
	@Element
	public String name;
	@Element
	public String packagename;	
	@Element(required=false)
	public String strDateTimeInstall;
	@Element(required=false)
	public String strDateTimeLastUpdate;
	public Drawable icon;
    Date DateTimeInstall;
    Date DateTimeLastUpdate;
    long FirstInstallTime;
    long LastUpdateTime;	
	
	public Boolean selected;
	
	public String getName() {
		  return name;
	}

	public void setName(String name) {
		  this.name = name;
	}

	public String getPackageName() {
		  return packagename;
	}

	public void setPackageName(String packagename) {
		  this.packagename = packagename;
	}
	
	public boolean isSelected() {
		  return selected;
	}

	public void setSelected(boolean selected) {
		  this.selected = selected;
	}	
	   @Override
	    public boolean equals(Object o) {
	        if (this == o) {
	            return true;
	        }
	        if (!(o instanceof App)) {
	            return false;
	        }

	        App that = (App) o;
	        return name.equals(that.name);
	    }
	    @Override
	    public int hashCode() {
	        int result;
	        result = 31 * (name != null ? name.hashCode() : 0);
	        return result;
	    }
	    
		@Override
		public int compareTo(App app) {
			// TODO Auto-generated method stub
			return 0;
		}
}
