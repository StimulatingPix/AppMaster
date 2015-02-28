/**
 * 
 */
package com.taa.android.app.master;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

/**
 * @author Android
 *
 *	Apps class
 */
@Element(name="apps")

public class Apps {
	
	private int mCount;
	private int mIndex;
	private App mApp;
	@ElementList(entry="app", inline=true , required=false )
	public List<App> apps = new ArrayList<App>();

	public Apps () {
		this.mCount = 0;
		this.mIndex = 0;
		this.apps.clear();
	}
	
	public void Add(App a) {
//
// 	Add a app to the list
//		
		mIndex = this.Count();
		apps.add(mIndex, a);
	}
	
	public int Count() {
//
//	get the count of apps
//		
		mCount = apps.size();
		return (mCount);
	}
	
	public void Remove(int index) {
//
//	Remove a app from the list		
//
		mApp = apps.remove(index);	
	}

	public App get(int position) {
//
// Get an application
//
		mApp = apps.get(position);
		return (mApp);
	}

}
