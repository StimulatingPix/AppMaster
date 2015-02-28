/**
 * 
 */
package com.taa.android.app.master;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import android.graphics.drawable.Drawable;

/**
 * @author Android
 * 
 *		Application which is in the registry
 */
@Element(name="registeredapp")
public class RegisteredApp implements Comparable <RegisteredApp>
{
	@Element
	public String name;
	@Element
	public String packagename;	
	@Element(required=false)
	public String strDateTimeInstall;
	@Element(required=false)
	public String strDateTimeLastUpdate;
	public Drawable icon;
    public Date DateTimeInstall;
    public Date DateTimeLastUpdate;
    public long FirstInstallTime;
    public long LastUpdateTime;	
	
	public Boolean selected;

	private int mCategoryCount;
	private int mCategoryIndex;
	private Category mCategory;
	
	@ElementList(entry="categories", inline=true)
	public List<Category> categories = new ArrayList<Category>();

	public RegisteredApp () {
		
		this.mCategoryCount = 0;
		this.mCategoryIndex = 0;
		this.categories.clear();
	}
	
	public void Add(Category c) {
//
// 	Add a category to the list
//		
		mCategoryIndex = this.Count();
		categories.add(mCategoryIndex, c);
	}
	
	public int Count() {
//
//	get the count of categories
//		
		mCategoryCount = categories.size();
		return (mCategoryCount);
	}
	
	public void Remove(int index) {
//
//	Remove a category from the list		
//
		mCategory = categories.remove(index);	
	}

	public Category get(int position) {
		// TODO Auto-generated method stub
		mCategory = categories.get(position);
		return (mCategory);
	}
	
	public String getCategoryFilename(String name) 
	{
//
//		returns the filename of a category
//		
		this.Count();
		
		for (int i=0; i < this.mCategoryCount; i++  ) {
			this.mCategory = this.get(i);
			if ( this.mCategory.name.equals(name)) {
				return(this.mCategory.filename);
			}
		}
		return (null);
	}	
	
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
	public int compareTo(RegisteredApp registeredapp) {
		// TODO Auto-generated method stub
		return 0;
	}
}
