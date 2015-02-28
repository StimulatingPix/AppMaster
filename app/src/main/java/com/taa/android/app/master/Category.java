/**
 * 
 */
package com.taa.android.app.master;

import org.simpleframework.xml.Element;

/**
 * @author Android
 *
 */

@Element(name="category")

public class Category implements Comparable <Category> {
	
	@Element
	public String name;
	@Element
	public String description;	
	@Element
	public String filename;
	@Element
	public String label;
	@Element
	public String icon;
	@Element
	public String type;
	@Element(required=false)
	public String url;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Category)) {
			return false;
		}

		Category that = (Category) o;
		return name.equals(that.name);
	}
	
	@Override
	public int hashCode() {
		int result;
		result = 31 * (name != null ? name.hashCode() : 0);
		return result;
	}
	    
	@Override
	public int compareTo(Category category) {
		// TODO Auto-generated method stub
		return 0;
	}
}
