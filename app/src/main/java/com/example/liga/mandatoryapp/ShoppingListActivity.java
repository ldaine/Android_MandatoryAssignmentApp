package com.example.liga.mandatoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        String item = "key: " + key;
        //Firebase connection
        String firebaseConectionString = "https://shoppingappbaaa2016.firebaseio.com/shoppingList/" + key;
        item = firebaseConectionString;
        final Firebase productListDetailsRef = new Firebase(firebaseConectionString);
        String firebaseProductListConnectionString = firebaseConectionString + "/products";
        final Firebase productListRef = new Firebase(firebaseProductListConnectionString);

        final FirebaseListAdapter<Product> productListFirebaseAdapter;



        if (savedInstanceState != null) {

            //on orientation change
        }

        //getting view elements
        final Button addProductButton = (Button) findViewById(R.id.inputButton);
        final Button deleteButton = (Button) findViewById(R.id.deleteButton);
        final ImageButton clearButton = (ImageButton) findViewById(R.id.clearButton);
        final EditText addProductInput = (EditText) findViewById(R.id.inputItem);
        final EditText editAmount = (EditText) findViewById(R.id.inputAmount);
        final TextView textView = (TextView) findViewById(R.id.outputText);
        final TextView textRemoveView = (TextView) findViewById(R.id.outputRemovedText);
        final ListView productListView = (ListView) findViewById(R.id.list);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerAmount);
        final TextView testingView = (TextView) findViewById(R.id.testing);

        testingView.setText(String.valueOf(firebaseProductListConnectionString));

        //adding amount drop down picker to the view
        ArrayAdapter<CharSequence> adapterSpinnerAmount = ArrayAdapter.createFromResource(this,
                R.array.measurments_array,
                android.R.layout.simple_spinner_dropdown_item);

        assert spinner != null;
        spinner.setAdapter(adapterSpinnerAmount);

        productListFirebaseAdapter = new FirebaseListAdapter<Product>(this, Product.class, android.R.layout.two_line_list_item, productListRef) {
            @Override
            protected void populateView(View view, Product product, int i) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(product.toString());
            }
            //ChildEventListener childEventListener = new
        };

        productListView.setAdapter(productListFirebaseAdapter);

        //setting listener functions
        assert addProductButton != null;
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the value from edit field
                assert addProductInput != null;
                String productName = addProductInput.getText().toString();



                //setting deleted item for UNDO
                //this should be in delete button listener
                Product productToBeDeleted = new Product();
                try {
                    assert editAmount != null;
                    productToBeDeleted.setQuantity(Integer.parseInt(editAmount.getText().toString()));
                    //Log.d("DEBUG editAmount ", editAmount.getText().toString());
                } catch (NumberFormatException e) {
                    productToBeDeleted.setQuantity(0);
                    //Log.d("DEBUG editAmount: ", "not a number");
                }

                if (spinner.getSelectedItemPosition() == 0) {
                    productToBeDeleted.setMeasurment("pcs");
                } else {
                    productToBeDeleted.setMeasurment((String) spinner.getSelectedItem());
                }

                //end of this should be in delete button listener

                //setting deleted item for UNDO
                //this should be in delete button listener
                Product newProduct = new Product();

                newProduct.setName(addProductInput.getText().toString());

                try {
                    newProduct.setQuantity(Integer.parseInt(editAmount.getText().toString()));
                    //Log.d("DEBUG editAmount ", editAmount.getText().toString());
                } catch (NumberFormatException e) {
                    newProduct.setQuantity(0);
                    //Log.d("DEBUG editAmount: ", "not a number");
                }

                if (spinner.getSelectedItemPosition() == 0) {
                    newProduct.setMeasurment("pcs");
                } else {
                    newProduct.setMeasurment((String) spinner.getSelectedItem());
                }

                //pushing the new list to firebase
                productListRef.push().setValue(newProduct);

                //adding the element to the list
                //set the new value in the text field
                textView.setText("");


                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                productListFirebaseAdapter.notifyDataSetChanged();

                addProductInput.setText("");
                editAmount.setText("");
                spinner.setSelection(0);
            }
        });


    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("savedShoppingListArray", shoppingListArray);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        //this.shoppingListArray = savedState.getParcelableArrayList("savedShoppingListArray");

    }
}
