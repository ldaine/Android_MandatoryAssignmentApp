package com.example.liga.mandatoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.Button;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    //fields
    private final String TAG = "ActivityLifeCycle";

    //Properties
    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> bag = new ArrayList<String>();

    String latestAddedBagItem = "";
    String latestRemovedBagItem = "";

    //methods
    public ArrayAdapter getMyAdapter()
    {
        return adapter;
    }

    //method owerride
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate() called");

        if(savedInstanceState!= null){

            String str = savedInstanceState.getString("savedLatestBagItem");
            String removedItemStr = savedInstanceState.getString("savedLatestRemovedBagItem");
            Log.d("DEBUG", str);
            if(str != null){
                latestAddedBagItem = str;
            }
            if(removedItemStr != null){
                latestRemovedBagItem = removedItemStr;
            }

            ArrayList list = savedInstanceState.getStringArrayList("savedBag");
            if (list!=null){
                bag = list;
            }
        }

        //getting view elements
        final Button inputButton = (Button) findViewById(R.id.inputButton);
        final Button deleteButton = (Button) findViewById(R.id.deleteButton);
        final EditText editText = (EditText) findViewById(R.id.inputText);
        final TextView textView = (TextView) findViewById(R.id.outputText);
        final TextView textRemoveView = (TextView) findViewById(R.id.outputRemovedText);
        final ListView listView = (ListView) findViewById(R.id.list);

        //adding values to view
        textView.setText(latestAddedBagItem);
        textRemoveView.setText(latestRemovedBagItem);

        adapter =  new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, bag );

        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //setting listener functions
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the value from edit field
                latestAddedBagItem = editText.getText().toString();
                //adding the element to the list
                bag.add(latestAddedBagItem);

                //set the new value in the text field
                textView.setText(latestAddedBagItem);

                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();

                editText.setText("");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = listView.getCheckedItemPosition();
                latestRemovedBagItem = bag.get(index);

                textRemoveView.setText(latestRemovedBagItem);

                bag.remove(index);

                getMyAdapter().notifyDataSetChanged();

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

        outState.putString("savedLatestBagItem", latestAddedBagItem);
        outState.putString("savedLatestRemovedBagItem", latestRemovedBagItem);
        outState.putStringArrayList("savedBag", bag);
    }

    protected void onRestoreInstanceState(Bundle savedState){
        super.onRestoreInstanceState(savedState);
        Log.i(TAG, "onRestoreInstanceState");

        this.latestAddedBagItem = savedState.getString("savedLatestBagItem");
        this.latestRemovedBagItem = savedState.getString("savedLatestRemovedBagItem");
        this.bag = savedState.getStringArrayList("savedBag");

    }

}
