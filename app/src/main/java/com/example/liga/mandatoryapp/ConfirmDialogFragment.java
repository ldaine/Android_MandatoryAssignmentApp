package com.example.liga.mandatoryapp;

/**
 * Created by Liga on 04-04-2016.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


//You can pretty much reuse this class in your own project
//if you want you can modify some of the text shown below.
//of course if it was for a multilingual app you would put
//the actual text that is now hardcoded inside the strings.xml file
public class ConfirmDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Here we create a new dialogbuilder;
        AlertDialog.Builder alert = new AlertDialog.Builder(
                getActivity());
        alert.setTitle("Confirmation");
        alert.setMessage("Are you sure?");
        alert.setPositiveButton("Yes", pListener);
        alert.setNegativeButton("No", nListener);

        return alert.create();
    }

    //This is our positive listener for when the user presses
    //the yes button
    DialogInterface.OnClickListener pListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            // these will be executed when user click Yes button
            positiveClick();
        }
    };

    //This is our negative listener for when the user presses
    //the no button.
    DialogInterface.OnClickListener nListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            // these will be executed when user click No button
            negativeClick();
        }
    };

    //These two methods are empty, because they will
    //be overridden
    protected void positiveClick()
    {

    }
    protected void negativeClick()
    {

    }
}