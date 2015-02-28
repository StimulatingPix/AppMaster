/**
 * 
 */
package com.taa.android.app.master;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;

/**
 * @author Android
 *
 *		Categories class
 *
 */

public class Categories {
	
	private int mCount;
	private int mIndex;
	private Category mCategory;
	
	@ElementList(entry="category", inline=true)
	public List<Category> categories = new ArrayList<Category>();

	public Categories () {
		
		this.mCount = 0;
		this.mIndex = 0;
		this.categories.clear();
	}
	
	public void Add(Category c) {
//
// 	Add a category to the list
//		
		mIndex = this.Count();
		categories.add(mIndex, c);
	}
	
	public int Count() {
//
//	get the count of categories
//		
		mCount = categories.size();
		return (mCount);
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
	
	public String getFilename(String name) {
//
//		returns the filename of a category
//		
		this.Count();
		
		for (int i=0; i < this.mCount; i++  ) {
			this.mCategory = this.get(i);
			if ( this.mCategory.name.equals(name)) {
				return(this.mCategory.filename);
			}
		}
		return (null);
	}
	
}
