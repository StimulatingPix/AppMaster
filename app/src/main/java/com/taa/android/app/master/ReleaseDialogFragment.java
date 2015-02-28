/**
 * 
 */
package com.taa.android.app.master;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author Android
 *
 */
public class ReleaseDialogFragment extends DialogFragment {
	
	private static View mView;
	private String mMesssage;
	

	 /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event Call backs.
     * Each method passes the DialogFragment in case the host needs to query it. */
    
	public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    
	/**
	 * 	Constructor method
	 */
	public ReleaseDialogFragment ()
	{

	}
		// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			// Verify that the host activity implements the callback interface
			try {
				// Instantiate the NoticeDialogListener so we can send events to the host
				mListener = (NoticeDialogListener) activity;
			} catch (ClassCastException e) {
				// The activity doesn't implement the interface, throw exception
				throw new ClassCastException(activity.toString()
						+ " must implement NoticeDialogListener");
			}
		}    

	    /** The system calls this only when creating the layout in a dialog. */
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
            // Build the dialog and set up the button click handlers
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
	        // Pass null as the parent view because its going in the dialog layout
            // also prevents exception error
            
            mView = inflater.inflate(R.layout.release, null);
	        	        
            builder.setView(mView);
            
            builder.setTitle(R.string.install_title);
            builder.setMessage(this.mMesssage);
            
            builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Send the positive button event back to the host activity
                    mListener.onDialogPositiveClick(ReleaseDialogFragment.this);
                }
            });
            builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Send the negative button event back to the host activity
                    mListener.onDialogNegativeClick(ReleaseDialogFragment.this);
                }
            });
            AlertDialog dialog = builder.create();
            
	        return dialog;
	    }
	    
	    public void SetMessage(String Message)
	    {
	    	this.mMesssage = Message;
	    }
}
