package com.example.liga.mandatoryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

public class ShoppingListActivity extends AppCompatActivity {

    /*Declaring attributes*/
    String key; //the product key for the view
    String firebaseRootConectionString = "https://shoppingappbaaa2016.firebaseio.com/shoppingList/";
    String firebaseConectionString;
    String firebaseProductListConnectionString;
    Firebase productListDetailsRef;
    Firebase productListRef;
    Button addProductButton;
    Button deleteButton;
    EditText addProductInput;
    EditText editAmount;
    ListView productListView;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapterSpinnerAmount;
    FirebaseListAdapter<Product> productListFirebaseAdapter;
    Product newProduct;
    Product deletedProduct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        //getActionBar().setHomeButtonEnabled(true); //this means we can click "home"

        Intent intent = getIntent();
        if(intent != null){
            key = intent.getStringExtra("key");
        }

        if (savedInstanceState != null) {

            String savedKey = savedInstanceState.getString("key");
            assert savedKey != null;
            key = savedKey;
        }

        /*Firebase initialization*/
        Firebase.setAndroidContext(this);
        firebaseConectionString = firebaseRootConectionString + key;
        firebaseProductListConnectionString = firebaseConectionString + "/products";
        productListDetailsRef = new Firebase(firebaseConectionString);
        productListRef = new Firebase(firebaseProductListConnectionString);


        /*View bindings*/
        addProductButton = (Button) findViewById(R.id.inputButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        addProductInput = (EditText) findViewById(R.id.inputItem);
        editAmount = (EditText) findViewById(R.id.inputAmount);
        productListView = (ListView) findViewById(R.id.list);
        spinner = (Spinner) findViewById(R.id.spinnerAmount);

        //adding amount drop down picker to the view
        adapterSpinnerAmount = ArrayAdapter.createFromResource(this,
                R.array.measurments_array,
                android.R.layout.simple_spinner_dropdown_item);

        assert spinner != null;
        spinner.setAdapter(adapterSpinnerAmount);

        productListFirebaseAdapter = new FirebaseListAdapter<Product>(this, Product.class, android.R.layout.simple_list_item_checked, productListRef) {
            @Override
            protected void populateView(View view, Product product, int i) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(product.toString());
            }
        };

        //setting listView config
        assert productListView != null;
        productListView.setAdapter(productListFirebaseAdapter);
        productListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //setting listener functions
        assert addProductButton != null;
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the value from edit field
                assert addProductInput != null;
                newProduct = new Product();

                String productName = addProductInput.getText().toString();
                if (productName.equals("")) {
                    newProduct.setName("unknown");
                } else {
                    newProduct.setName(addProductInput.getText().toString());
                }

                try {
                    newProduct.setQuantity(Integer.parseInt(editAmount.getText().toString()));
                    //Log.d("DEBUG editAmount ", editAmount.getText().toString());
                } catch (NumberFormatException e) {
                    newProduct.setQuantity(0);
                    //Log.d("DEBUG editAmount: ", "not a number");
                }

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

                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                productListFirebaseAdapter.notifyDataSetChanged();

                addProductInput.setText("");
                editAmount.setText("");
                spinner.setSelection(0);
            }
        });

        //REMOVE ITEM
        assert deleteButton != null;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get position
                int position = productListView.getCheckedItemPosition();
                //make copy
                deletedProduct = productListFirebaseAdapter.getItem(position);

                Snackbar snackbar = Snackbar
                        .make(productListView, "Item is deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //add back to firebase
                                productListRef.push().setValue(deletedProduct);
                                Snackbar snackbar = Snackbar.make(productListView, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });


                //delete item
                productListFirebaseAdapter.getRef(position).removeValue();

                snackbar.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //We we set that we want to use the xml file
        //under the menu directory in the resources and
        // that we want to use the specific file called "main.xml"
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setShareIntent(String product){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); //MIME type
        intent.putExtra(Intent.EXTRA_TEXT, product); //add the text to t
        startActivity(intent);
    }

    public String convertListToString()
    {
        String result = "";
        for (int i = 0; i<productListFirebaseAdapter.getCount();i++)
        {
            Product p = productListFirebaseAdapter.getItem(i);
            result += p.toString();
            result += "\n";
        }
        return result;
    }


    //Here we put the code for what should happen in the app once
    //the user selects one of our menu items (regardless of whether
    //it is in the actionbar or in the overflow menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Toast.makeText(this, "Application icon clicked!",
                        Toast.LENGTH_SHORT).show();
                return true; //return true, means we have handled the event
            case R.id.item_about:
                Toast.makeText(this, "About item clicked!", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.item_list_delete:
                //Toast.makeText(this, "I want to delete List", Toast.LENGTH_SHORT)
                        //.show();
                callCofirmDeleteList(ShoppingListActivity.this);
                return true;
            case R.id.item_delete:
                callCofirmClearList();
                return true;
            case R.id.item_help:
                Toast.makeText(this, "Help item clicked!", Toast.LENGTH_SHORT)
                        .show();
                return true;

            case R.id.item_share:
                String productList = convertListToString();
                setShareIntent(productList);
                return true;


            case R.id.item_settings:
                //Here we create a new activity and we instruct the
                //Android system to start it
                Intent intent = new Intent(this, SettingsActivity.class);

                //we can use this, if we need to know when the user exists our preference screens
                startActivityForResult(intent, 1);
                return true;
        }

        return false; //we did not handle the event
    }

    //This will be called when other activities in our application
    //are finished.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1) //exited our preference screen
        {
            Toast toast =
                    Toast.makeText(getApplicationContext(), "back from preferences", Toast.LENGTH_LONG);
            toast.setText("back from our preferences");
            toast.show();
            //here you could put code to do something.......
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getPreferences(View v) {

        //We read the shared preferences from the
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String gender = prefs.getString("gender", "");
        boolean soundEnabled = prefs.getBoolean("sound", false);

        Toast.makeText(
                this,
                "Email: " + email + "\nGender: " + gender + "\nSound Enabled: "
                        + soundEnabled, Toast.LENGTH_SHORT).show();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key", key);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        this.key = savedState.getString("key");

    }

    //function to call the Confirmation Fragment
    protected void callCofirmClearList(){

        //final Firebase productListRef = new Firebase(firebaseRootConectionString + key + "/products");

        ConfirmDialogFragment dialog = new ConfirmDialogFragment() {
            @Override
            protected void positiveClick() {
                //Here we override the methods and can now
                productListRef.removeValue();
                //getMyAdapter().notifyDataSetChanged();

                //make toast
                String message = "List Cleared";
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, message, duration);
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

        //set the confirm window message text
        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.confirm_delete_heading));
        bundle.putString("message", getResources().getString(R.string.confirm_delete_message));
        bundle.putString("confirm", getResources().getString(R.string.confirm_delete_btn_yes));
        bundle.putString("reject", getResources().getString(R.string.confirm_delete_btn_no));
        dialog.setArguments(bundle);
        //Here we show the dialog
        //The tag "MyFragement" is not important for us.
        dialog.show(getFragmentManager(), "MyFragment");
    }

    //function to call the Confirmation Fragment
    protected void callCofirmDeleteList(final Context context){


        ConfirmDialogFragment dialog = new ConfirmDialogFragment() {
            @Override
            protected void positiveClick() {
                //Here we override the methods and can now
                productListDetailsRef.removeValue();
                //Intent intent = new Intent(context, ShoppingListOverviewActivity.class);
                //startActivity(intent);
                ((Activity) context).finish();

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

        //set the confirm window message text
        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.confirm_delete_heading));
        bundle.putString("message", getResources().getString(R.string.confirm_delete_message));
        bundle.putString("confirm", getResources().getString(R.string.confirm_delete_btn_yes));
        bundle.putString("reject", getResources().getString(R.string.confirm_delete_btn_no));
        dialog.setArguments(bundle);
        //Here we show the dialog
        //The tag "MyFragement" is not important for us.
        dialog.show(getFragmentManager(), "MyFragment");
    }

}
