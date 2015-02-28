/**
 * 
 */
package com.taa.android.app.master;

import android.content.Context;

/**
 * @author Android
 * 
 * 		Library class for application
 *
 */
public class LibraryApplication {

	private Context mContext;
	private LibraryAndroid mLibAndroid;
	
	private String mAppAbout;
	private String mAppDescription;
	private String mAppFlavour;
	private String mAppPath;
	private String mAppTitle;
	private String mAppUrl;
	private String mAppUrlHelp;
	private String mAppVersion;
	private String mAppVminimum;
	private String mAppVtarget;
	private String mAppVplatform;
	
	private String mNewLine = System.getProperty("line.separator");
	
	public String mAboutMessage;
	public String mAboutTitle;
	
	public LibraryApplication(Context context)
	{
		mContext = context;
		mLibAndroid = new LibraryAndroid();
		
		setmAppAbout(mContext.getString(R.string.app_about));
		setmAppFlavour(mContext.getString(R.string.app_flavour));
		setmAppPath(mContext.getString(R.string.app_path));
		setmAppUrl(mContext.getString(R.string.app_url));
		setmAppUrlHelp(mContext.getString(R.string.app_url_help));
		setmAppVersion(mContext.getString(R.string.app_version));			
		setmAppVminimum(mContext.getString(R.string.app_android_vmin));
		setmAppVtarget(mContext.getString(R.string.app_android_vtar));
		setmAppVplatform(mContext.getString(R.string.app_android_vplt));

//		mAppDescription = mContext.getString(R.string.app_description1) + mNewLine + mNewLine;
//		mAppDescription += mContext.getString(R.string.app_description2) + mNewLine + mNewLine;
//		mAppDescription += mContext.getString(R.string.app_description3) + mNewLine + mNewLine;
//		mAppDescription += mContext.getString(R.string.app_description4) + mNewLine + mNewLine;
//		mAppDescription += mContext.getString(R.string.app_description5) + mNewLine + mNewLine;
//		mAppDescription += mContext.getString(R.string.app_description6) + mNewLine + mNewLine;
//		mAppDescription += mContext.getString(R.string.app_description7) + mNewLine + mNewLine;
		
	}
	
	public void SetAbout() {

		mAboutMessage = mAppVersion + "  - " + mAppFlavour + mNewLine + mNewLine +
						mAppVminimum + mNewLine + mAppVtarget + mNewLine + 
						mAppVplatform + " " + mLibAndroid.getmAndroidRelease() + mNewLine + mNewLine;		
	
		mAboutTitle = mAppAbout;
		
	}
	
	public String getmAppAbout() {
		return mAppAbout;
	}

	public void setmAppAbout(String mAppAbout) {
		this.mAppAbout = mAppAbout;
	}

	public String getmAppDescription() {
		return mAppDescription;
	}

	public void setmAppDescription(String mAppDescription) {
		this.mAppDescription = mAppDescription;
	}

	public String getmAppFlavour() {
		return mAppFlavour;
	}

	public void setmAppFlavour(String mAppFlavour) {
		this.mAppFlavour = mAppFlavour;
	}

	public String getmAppPath() {
		return mAppPath;
	}

	public void setmAppPath(String mAppPath) {
		this.mAppPath = mAppPath;
	}
	
	public String getmAppTitle() {
		return mAppTitle;
	}

	public void setmAppTitle(String mAppTitle) {
		this.mAppTitle = mAppTitle;
	}

	public String getmAppUrl() {
		return mAppUrl;
	}

	public String getmAppUrlHelp() {
		return mAppUrlHelp;
	}

	public void setmAppUrlHelp(String mAppUrlHelp) {
		this.mAppUrlHelp = mAppUrlHelp;
	}

	public void setmAppUrl(String mAppUrl) {
		this.mAppUrl = mAppUrl;
	}

	public String getmAppVersion() {
		return mAppVersion;
	}

	public void setmAppVersion(String mAppVersion) {
		this.mAppVersion = mAppVersion;
	}

	public String getmAppVminimum() {
		return mAppVminimum;
	}

	public void setmAppVminimum(String mAppVminimum) {
		this.mAppVminimum = mAppVminimum;
	}

	public String getmAppVtarget() {
		return mAppVtarget;
	}

	public void setmAppVtarget(String mAppVtarget) {
		this.mAppVtarget = mAppVtarget;
	}

	public String getmAppVplatform() {
		return mAppVplatform;
	}

	public void setmAppVplatform(String mAppVplatform) {
		this.mAppVplatform = mAppVplatform;
	}	
}
