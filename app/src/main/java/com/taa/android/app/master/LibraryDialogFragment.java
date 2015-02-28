/**
 * 
 */
package com.taa.android.app.master;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Android
 *
 * Library class to display dialog with title message and OK button
 * 
 */
public class LibraryDialogFragment extends DialogFragment {
	
	private String mMessage;
	private String mTitle;


    public static LibraryDialogFragment newInstance(String Message, String Title) {
        LibraryDialogFragment mFragment = new LibraryDialogFragment();
        Bundle args = new Bundle();
        args.putString("Message", Message);
        args.putString("Title", Title);
        mFragment.setArguments(args);
        return mFragment;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mMessage = getArguments().getString("Message");
        mTitle = getArguments().getString("Title");
    	
    return new AlertDialog.Builder(getActivity())
    		.setTitle(mTitle)
    		.setMessage(mMessage)
    		.setPositiveButton(R.string.txt_ok,
    				new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int whichButton) {
                		}    			
    				}
    		)
    		.create();
    }	

}
