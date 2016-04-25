package com.example.liga.mandatoryapp;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.design.widget.Snackbar;


public class MainActivity extends AppCompatActivity {

    //fields
    private final String TAG = "ActivityLifeCycle";

    //Properties
    ArrayAdapter<Product> adapterProduct;
    ListView listView;
    ArrayList<Product> bag = new ArrayList<Product>();

    Product latestAddedBagItem = new Product("", 0, "");
    Product latestRemovedBagItem = new Product("", 0, "");
    int latestRemovedBagItemPosition;


    //methods
    public ArrayAdapter getMyAdapter()
    {
        return adapterProduct;
    }

    //method owerride
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate() called");

        if(savedInstanceState!= null){

            Product product = savedInstanceState.getParcelable("savedLatestBagItem");
            Product removedItemProduct = savedInstanceState.getParcelable("savedLatestRemovedBagItem");
            Log.d("DEBUG", product.name);
            if(product != null){
                latestAddedBagItem = product;
            }
            if(removedItemProduct != null){
                latestRemovedBagItem = removedItemProduct;
            }

            ArrayList list = savedInstanceState.getParcelableArrayList("savedBag");
            if (list!=null){
                bag = list;
            }
        }

        //getting view elements
        final Button inputButton = (Button) findViewById(R.id.inputButton);
        final Button deleteButton = (Button) findViewById(R.id.deleteButton);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        final EditText editItem = (EditText) findViewById(R.id.inputItem);
        final EditText editAmount = (EditText) findViewById(R.id.inputAmount);
        final TextView textView = (TextView) findViewById(R.id.outputText);
        final TextView textRemoveView = (TextView) findViewById(R.id.outputRemovedText);
        final ListView listView = (ListView) findViewById(R.id.list);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerAmount);

        ArrayAdapter<CharSequence> adapterSpinnerAmount = ArrayAdapter.createFromResource(this,
                R.array.measurments_array,
                android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterSpinnerAmount);

        //adding values to view
        textView.setText(latestAddedBagItem.toString());
        textRemoveView.setText(latestRemovedBagItem.toString());

        adapterProduct =  new ArrayAdapter<Product>(this,
                android.R.layout.simple_list_item_checked, bag );

        listView.setAdapter(adapterProduct);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //setting listener functions
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the value from edit field
                latestAddedBagItem.name = editItem.getText().toString();


                try {
                    latestAddedBagItem.quantity = Integer.parseInt(editAmount.getText().toString());
                    Log.d("DEBUG editAmount ", editAmount.getText().toString());
                } catch (NumberFormatException e) {
                    latestAddedBagItem.quantity = 0;
                    Log.d("DEBUG editAmount: ", "not a number");
                }

                if(spinner.getSelectedItemPosition() == 0){
                    latestAddedBagItem.measurment = "pcs";
                } else {
                    latestAddedBagItem.measurment = (String) spinner.getSelectedItem();
                }
                //adding the element to the list
                bag.add(new Product(latestAddedBagItem.name, latestAddedBagItem.quantity, latestAddedBagItem.measurment));

                //set the new value in the text field
                textView.setText(latestAddedBagItem.toString());



                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();

                editItem.setText("");
                editAmount.setText("");
                spinner.setSelection(0);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View parent = listView;
                Snackbar snackbar = Snackbar
                        .make(parent, "Item is deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                bag.add(latestRemovedBagItemPosition,latestRemovedBagItem);
                                getMyAdapter().notifyDataSetChanged();
                                Snackbar snackbar = Snackbar.make(parent, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });

                latestRemovedBagItemPosition = listView.getCheckedItemPosition();
                latestRemovedBagItem = bag.get(latestRemovedBagItemPosition); //save a copy of the deleted item

                bag.remove(latestRemovedBagItemPosition); //remove item

                textRemoveView.setText(latestRemovedBagItem.name);

                getMyAdapter().notifyDataSetChanged(); //notify view

                snackbar.show();

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmDialogFragment dialog = new ConfirmDialogFragment() {
                    @Override
                    protected void positiveClick() {
                        //Here we override the methods and can now
                        //do something
                        latestAddedBagItem = new Product("", 0, "");
                        latestRemovedBagItem = new Product("", 0, "");

                        textView.setText(latestAddedBagItem.name);
                        textRemoveView.setText(latestRemovedBagItem.name);

                        bag.clear();
                        getMyAdapter().notifyDataSetChanged();

                        //make toast
                        String message = "List Cleared";
                        Context context = getApplicationContext();
                        CharSequence text = message;
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast =  Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    @Override
                    protected void negativeClick() {
                        //Here we override the method and can now do something
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "action cancelled", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                };
                //Here we show the dialog
                //The tag "MyFragement" is not important for us.
                dialog.show(getFragmentManager(), "MyFragment");
            }
        });
    }

    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        Log.i(TAG, "onSaveInstanceState");

        outState.putParcelable("savedLatestBagItem", latestAddedBagItem);
        outState.putParcelable("savedLatestRemovedBagItem", latestRemovedBagItem);
        outState.putParcelableArrayList("savedBag", bag);
    }

    protected void onRestoreInstanceState(Bundle savedState){
        super.onRestoreInstanceState(savedState);
        Log.i(TAG, "onRestoreInstanceState");

        this.latestAddedBagItem = savedState.getParcelable("savedLatestBagItem");
        this.latestRemovedBagItem = savedState.getParcelable("savedLatestRemovedBagItem");
        this.bag = savedState.getParcelableArrayList("savedBag");

    }



}
