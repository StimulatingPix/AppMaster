package com.taa.android.app.master;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Android
 * 
 *		Activity to configure which applications are available in each category
 *
 */
@SuppressLint("NewApi")
//		Activity uses drag and drop features which are only available after API 11
public class ConfigureActivity extends Activity implements OnItemSelectedListener {

	private LibraryApplication mLibApplication;
	private LibraryActivity mLibActivity;
	private LibraryPreferences mLibPrefs;
	
	private static String mAppPath;

    private static final int GET_META_DATA = 128;
	
	private LinearLayout targetLayout;
	private ListView listApps;
	private ListView listTarget;
	
	MyDragEventListener myDragEventListener = new MyDragEventListener();
//
//		Installed Applications
//	
    private static ArrayList<App> mAppList;
    private static int mAppCount;
	private static String [] mAppNames = new String[0];
	private static ApplicationInfo mAppInfo;
	private static String mAppFilename = "applications.xml";
//
//		General
//	
	private Context mContext;
    
//
//		Categories
//
	private static ArrayList<Category> mCategoryList;
	private	static Category mCategory;
	private static int mCategoryCount;
	private static int mCategoryIndex;
//		Category names	
	private static String [] mCategoryNames = new String[0];
//		Applications in category
	private ArrayList<App> mCategoryApps;
	
	private List<String> droppedList;
	private ArrayAdapter<String> droppedAdapter;
	private ArrayAdapter<String> mCategoriesAdapter;
	private AppListAdapter mAppListAdapter;
	
	
	private AppsHelper mAppsHelper;
	private CategoriesHelper mCategoriesHelper;
	
    private ActionBar mActionBar;
    private String mFilename;
    private String mErrAppInCategory;
    private String mErrAppNotLoaded;
    
    @SuppressLint("NewApi")
	private void initActivity()
    {
//    	
//		Use of action bar only work after API 11
//
    	mContext = this;
//		mLibAndroid = new LibraryAndroid();
		mLibApplication = new LibraryApplication(mContext);
		mLibActivity = new LibraryActivity(mContext, "Configure Activity");
		mLibPrefs = new LibraryPreferences(mContext);
//		mLibUtils = new LibraryUtils(mContext);
		
		mAppPath = mLibApplication.getmAppPath();
				
        mLibPrefs.getPrefs();
        
		mAppsHelper = new AppsHelper( mContext , mAppPath );
		mCategoriesHelper = new CategoriesHelper( mContext , mAppPath );
		
		
		mActionBar = getActionBar();    	
		mLibActivity.SetActionBarSubTitle(mLibPrefs, mActionBar);

    }

    private void loadApplications() 
    {
 
//
//		Loads the installed applications in mAppList.
//
//		If the file applications.xml exists on the SD card load the application	from the file
//		Other wise load the applications from installed applications
//
		if ( mAppsHelper.FileExists(mAppFilename) ) {
			mAppList = mAppsHelper.LoadAppsFromFile(mAppFilename);
			if ( mLibPrefs.displayDiagnostics()) {
				mLibActivity.ShowToast("loadApplications from " + mAppFilename);
			}
		} else {
			mAppList = mAppsHelper.loadApplications();
			if ( mLibPrefs.displayDiagnostics()) {
				mLibActivity.ShowToast("loadApplications from installed applications");
			}			
		}
		
    	mAppList = mAppsHelper.sortApplications("name_asc");
    	mAppCount = mAppList.size();
    	mAppNames = new String[mAppCount];

    	for ( int i=0; i < mAppCount; i++ ) {
    		App mApp = mAppList.get(i);
    		mAppNames[i] = mApp.name;
    	}
    }
    
	private void loadCategory(Category category) 
	{
//
//		Loads the Category
//
		mFilename = category.filename;
		
		mCategoryApps = new ArrayList<App>();
		mCategoryApps.clear();
		if ( droppedList == null ) {
			droppedList = new ArrayList<String>();
		}		
		droppedList.clear();
		droppedAdapter.notifyDataSetChanged();

		loadCategoryApps(category.filename);
		
	}    
	
	private void loadCategoryApps(String filename) 
	{
//
//		Load the applications from the category file
//		
		PackageManager manager = getPackageManager();

		ArrayList<App> mAppsLoaded = new ArrayList<App>();
		
		mAppsLoaded = mAppsHelper.LoadFromFile(filename);
		
		if ( mAppsLoaded != null ) {
			
			int mCount = mAppsLoaded.size();
			
		    for (int i = 0; i < mCount; i++) 
		    {	
		    	App mApp = mAppsLoaded.get(i);
		    	
		    		try {
						mAppInfo = manager.getApplicationInfo(mApp.packagename,GET_META_DATA);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 	    		
			    	mApp.icon = manager.getApplicationIcon(mAppInfo);
			    	mApp.setSelected(false);
		    		droppedList.add(mApp.name);
		    		droppedAdapter.notifyDataSetChanged();
		    		mCategoryApps.add(mApp);
		    }
		    
		} else {
			mLibActivity.LongToast(mErrAppNotLoaded + filename );
		}
		
	}	
    private void loadConfig() 
    {
//
//		Load the categories from config.xml file
//
    	boolean loadfromAssets; 

    	if ( mLibPrefs.getmRestore() ) {
    		loadfromAssets = true;
    	} else {
    		loadfromAssets = false;
    	}

    	mCategoryList = mCategoriesHelper.loadConfig(loadfromAssets);
    	mCategoryCount = mCategoryList.size();
    	mCategoryNames = new String[mCategoryCount];

    	for (int i = 0; i < mCategoryCount; i++) {	
    		mCategory = mCategoryList.get(i);
    		mCategoryNames[i] = mCategory.name;
    	}
    }
    
    private void loadStrings()
    {
		mErrAppInCategory = mContext.getString(R.string.err_app_in_category);
		mErrAppNotLoaded = mContext.getString(R.string.err_app_not_loaded);
    }
    
	private void saveCategoryApps()
	{
		mAppsHelper.SetListToApps(mCategoryApps);
		mAppsHelper.SaveToFile(mFilename);
		
	}    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initActivity();
        
		loadStrings();
		
		setContentView(R.layout.activity_configure);
		
		targetLayout = (LinearLayout)findViewById(R.id.targetlayout);
        listApps = (ListView)findViewById(R.id.applicationlist);
        listTarget = (ListView)findViewById(R.id.applist);		
//
//		load installed applications
//        
		loadApplications();

        mAppListAdapter = new AppListAdapter( this, mAppList);
        listApps.setAdapter(mAppListAdapter);
//
//		load the categories in config.xml
//		
		loadConfig();
//
//		display the categories in the spinner control
//
		
		Spinner mSpinner = (Spinner) findViewById(R.id.spinner_categories);
		mSpinner.setOnItemSelectedListener(this);
		
		mCategoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCategoryNames);
		
		mSpinner.setAdapter(mCategoriesAdapter);
//
//		set the current category
//
		
		mCategoryIndex = mCategoriesAdapter.getPosition(mLibPrefs.getmCategory());
		mSpinner.setSelection(mCategoryIndex);
        
//               
//		set the click listener to initiate the drag
//
        
        listApps.setOnItemLongClickListener(listAppsItemLongClickListener);
        listApps.setOnDragListener(myDragEventListener);
        targetLayout.setOnDragListener(myDragEventListener);

//
//		list of applications in a category
//        
        droppedList = new ArrayList<String>();
        droppedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, droppedList);
        listTarget.setAdapter(droppedAdapter);
 		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
//		
// 		Inflate the menu; this adds items to the action bar if it is present.
//		
		getMenuInflater().inflate(R.menu.configure, menu);
		return true;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
// 		Spinner item selected
		mLibPrefs.setmCategory(mCategoryNames[position]);
		mLibPrefs.setPrefs( "category" , mLibPrefs.getmCategory());
		mCategory = mCategoryList.get(position);
	
		mLibActivity.SetActionBarSubTitle(mLibPrefs, mActionBar);
		
		loadCategory(mCategory);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) 
	{
//		Spinner no item selected 
		
	}
	
    OnItemLongClickListener listAppsItemLongClickListener = new OnItemLongClickListener()
    {
//
//		OnLongClickListener for the applications list 
//
    	
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
		{
			
			TextView mtvAppName = (TextView) view.findViewById(R.id.tv_app_name);
			String mAppName = (String) mtvAppName.getText();
			mAppNames[position] = mAppName;
			
			//Selected item is passed as item in dragData
			ClipData.Item item = new ClipData.Item(mAppNames[position]);
			
			String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
			ClipData dragData = new ClipData((CharSequence)view.getTag(), 
					clipDescription,
					item);
			DragShadowBuilder myShadow = new MyDragShadowBuilder(view);
//
//			Start the drag operation
//
			view.startDrag(dragData,			//ClipData
                    myShadow,					//View.DragShadowBuilder 
                    mAppNames[position],		//Object myLocalState
                    0);							//flags
						
			return true;
		}
	};

		private static class MyDragShadowBuilder extends View.DragShadowBuilder 
		{
//			
//		Draw shadow on drag operation
//			
			private static Drawable shadow;
			
			public MyDragShadowBuilder(View v) {
				super(v);
				shadow = new ColorDrawable(Color.LTGRAY);
	        }
			
			@Override
	        public void onProvideShadowMetrics (Point size, Point touch){
	            int width = getView().getWidth();
	            int height = getView().getHeight();

	            shadow.setBounds(0, 0, width, height);
	            size.set(width, height);
	            touch.set(width / 2, height / 2);
	        }

	        @Override
	        public void onDrawShadow(Canvas canvas) {
	            shadow.draw(canvas);
	        }
			
		}
		
		protected class MyDragEventListener implements View.OnDragListener {
//
//		Handle the drag event
//			
			@Override
			public boolean onDrag(View view, DragEvent event) 
			{
				final int action = event.getAction();
				
				switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					//All involved view accept ACTION_DRAG_STARTED for MIMETYPE_TEXT_PLAIN
	                if (event.getClipDescription()
	                		.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
	                {
	                	return true;	//Accept
	                }else{
	                	return false;	//reject
	                }
				case DragEvent.ACTION_DRAG_ENTERED:
					return true;
				case DragEvent.ACTION_DRAG_LOCATION:
					return true;
				case DragEvent.ACTION_DRAG_EXITED:
					return true;
				case DragEvent.ACTION_DROP:
					// Gets the item containing the dragged data
	                ClipData.Item item = event.getClipData().getItemAt(0);
					
					//If apply only if drop on buttonTarget
					if(view == targetLayout){
						String droppedItem = item.getText().toString();
						addAppToCategory(droppedItem);
					}else{
						return false;
					}
	                
	                
				case DragEvent.ACTION_DRAG_ENDED:
					if (event.getResult()){
//						commentMsg += v.getTag()	+ " : ACTION_DRAG_ENDED - success.\n";
//						comments.setText(commentMsg);
					} else {
//						commentMsg += v.getTag()	+ " : ACTION_DRAG_ENDED - fail.\n";
//						comments.setText(commentMsg);
					};
	                return true;
				default:	//unknown case
//					commentMsg += v.getTag()	+ " : UNKNOWN !!!\n";
//					comments.setText(commentMsg);
					return false;

				}
			}	
		}

		private boolean addAppToCategory(String name)
		{
//				
//		Add the dragged application to the category
//				
			if ( !checkAppInCategory(name) ) 
			{
				droppedList.add(name);
				Collections.sort(droppedList);
				droppedAdapter.notifyDataSetChanged();
				App mApp = getApp(name);
				mCategoryApps.add(mApp);
				mAppsHelper.SetAppList(mCategoryApps);
				mCategoryApps = mAppsHelper.sortApplications("name_asc");
				saveCategoryApps();
				return true;
			}
			else
			{
				mLibActivity.LongToast(mErrAppInCategory);
				return false;
			}

		}

	   private App getApp(String name)
	   {
//
//			get the App with supplied name in list of installed applications
//
		   if ( mAppList != null ) {
			   
			   mAppCount = mAppList.size();
			   
			   for ( int i=0; i < mAppCount; i++ ) {
				   App mApp = mAppList.get(i);
				   if (mApp.name.equals(name) ){
					   return (mApp);
				   }
			   }
				   
		   }
		   return null;
	   }
		   
	   private boolean checkAppInCategory(String name)
	   {			   
//
//		Checks if the Application is in the Category
//
		   boolean mInCategory = false;
		   
		   if ( mCategoryApps != null ) {
			   
			   mAppCount = mCategoryApps.size();
			   
			   for ( int i=0; i < mAppCount; i++ ) {
				   App mApp = mCategoryApps.get(i);
				   if (mApp.name.equals(name) ) {
					   mInCategory = true;
					   break;
				   }
			   }
		   }
		   
		   return (mInCategory);
	   }
}
