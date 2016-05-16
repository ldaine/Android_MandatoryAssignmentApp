package com.example.liga.mandatoryapp;

import android.app.ActionBar;
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
    private String userId;
    private String key; //the product key for the view
    String firebaseShoppingListUrl; //[firebaseRootConectionString]/<key>
    String firebaseProductListConnectionString; //[firebaseRootConectionString]/<key>/products

    private Firebase mRef = new Firebase(Constants.FIREBASE_URL);
    Firebase productListDetailsRef; //The shopping list object (name, date, product list)
    Firebase productListRef; //the product list
    Button buttonProductAdd; //add button
    Button buttonProductDelete; //remove button
    EditText inputProductName; //name of the new product
    EditText inputProductAmount; ///amount of the new product
    ListView listProductsView; //product list
    Spinner spinnerProductMeasure; //measurment drop down field
    ArrayAdapter<CharSequence> adapterSpinnerProductMeasure; //adapter for the measurment drop down - adding values from array
    FirebaseListAdapter<Product> productListFirebaseAdapter; //firebase UI adapter for product list
    Product newProduct; //new product object
    Product deletedProduct; //copy of just deleted object - used in undo

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        //trying to get the userId
        try {
            userId = mRef.getAuth().getUid();
        } catch (Exception e) {
            loadLoginView();
        }

        Intent intent = getIntent();
        if(intent != null){
            key = intent.getStringExtra("key");
        }

        if (savedInstanceState != null) {

            String savedKey = savedInstanceState.getString("key");
            assert savedKey != null;
            key = savedKey;
        }

        getSupportActionBar().setTitle(key);

        /*Firebase initialization*/
        firebaseShoppingListUrl = Constants.FIREBASE_URL + "/users/" + userId + "/lists/" + key;
        productListRef = new Firebase(firebaseShoppingListUrl);

        //firebaseConectionString = Constants.FIREBASE_URL + key;
        firebaseProductListConnectionString = firebaseShoppingListUrl + "/products";
        productListDetailsRef = new Firebase(firebaseShoppingListUrl);
        productListRef = new Firebase(firebaseProductListConnectionString);


        /*View bindings*/
        buttonProductAdd = (Button) findViewById(R.id.buttonProductAdd);
        buttonProductDelete = (Button) findViewById(R.id.buttonProductDelete);
        inputProductName = (EditText) findViewById(R.id.inputProductName);
        inputProductAmount = (EditText) findViewById(R.id.inputProductAmount);
        listProductsView = (ListView) findViewById(R.id.listProducts);
        spinnerProductMeasure = (Spinner) findViewById(R.id.spinnerProductMeasure);

        //Setting Spinner Adapter
        //adding measurement drop down picker to the view
        adapterSpinnerProductMeasure = ArrayAdapter.createFromResource(this,
                R.array.measurments_array,
                android.R.layout.simple_spinner_dropdown_item);

        assert spinnerProductMeasure != null;
        spinnerProductMeasure.setAdapter(adapterSpinnerProductMeasure);

        //Setting Firebase Adapter
        productListFirebaseAdapter = new FirebaseListAdapter<Product>(this, Product.class, android.R.layout.simple_list_item_checked, productListRef) {
            @Override
            protected void populateView(View view, Product product, int i) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(product.toString());
            }
        };

        //setting listView config
        assert listProductsView != null;
        listProductsView.setAdapter(productListFirebaseAdapter);
        listProductsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //setting listener functions
        assert buttonProductAdd != null;
        buttonProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the value from edit field
                assert inputProductName != null;
                newProduct = new Product();

                String productName = inputProductName.getText().toString();
                if (productName.equals("")) {
                    newProduct.setName("unknown");
                } else {
                    newProduct.setName(inputProductName.getText().toString());
                }

                try {
                    newProduct.setQuantity(Integer.parseInt(inputProductAmount.getText().toString()));
                    //Log.d("DEBUG editAmount ", editAmount.getText().toString());
                } catch (NumberFormatException e) {
                    newProduct.setQuantity(0);
                    //Log.d("DEBUG editAmount: ", "not a number");
                }

                try {
                    newProduct.setQuantity(Integer.parseInt(inputProductAmount.getText().toString()));
                    //Log.d("DEBUG editAmount ", editAmount.getText().toString());
                } catch (NumberFormatException e) {
                    newProduct.setQuantity(0);
                    //Log.d("DEBUG editAmount: ", "not a number");
                }

                if (spinnerProductMeasure.getSelectedItemPosition() == 0) {
                    newProduct.setMeasurment("pcs");
                } else {
                    newProduct.setMeasurment((String) spinnerProductMeasure.getSelectedItem());
                }

                //pushing the new list to firebase
                productListRef.push().setValue(newProduct);

                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                productListFirebaseAdapter.notifyDataSetChanged();

                inputProductName.setText("");
                inputProductAmount.setText("");
                spinnerProductMeasure.setSelection(0);
            }
        });

        //REMOVE ITEM
        assert buttonProductDelete != null;
        buttonProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get position
                int position = listProductsView.getCheckedItemPosition();
                //make copy
                deletedProduct = productListFirebaseAdapter.getItem(position);

                Snackbar snackbar = Snackbar
                        .make(listProductsView, "Item is deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //add back to firebase
                                productListRef.push().setValue(deletedProduct);
                                Snackbar snackbar = Snackbar.make(listProductsView, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });

                //delete item
                productListFirebaseAdapter.getRef(position).removeValue();
                //show the undo snackbar
                snackbar.show();
            }
        });
    }

    //setting the menu in action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //We we set that we want to use the xml file
        //under the menu directory in the resources and
        // that we want to use the specific file called "main.xml"
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    //handling actions on action bar or overflow menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_list_delete:
                callCofirmDeleteList(ShoppingListActivity.this);
                return true;
            case R.id.item_clear:
                callCofirmClearList();
                return true;
            case R.id.item_share:
                String productList = convertListToString();
                setShareIntent(productList);
                return true;
            case R.id.item_settings:
                //Here we create a new activity and we instruct the
                //Android system to start it
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 1);
                return true;
            case R.id.item_logout:
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

    //function to get preferences values and display as Toast (temp)
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

    //functions to handle orientation change
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key", key);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        this.key = savedState.getString("key");

    }

    //function to start Share activity
    private void setShareIntent(String product){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); //MIME type
        intent.putExtra(Intent.EXTRA_TEXT, product); //add the text to t
        startActivity(intent);
    }

    //function to convert product list to a string
    public String convertListToString() {
        String result = "";
        for (int i = 0; i<productListFirebaseAdapter.getCount();i++)
        {
            Product p = productListFirebaseAdapter.getItem(i);
            result += p.toString();
            result += "\n";
        }
        return result;
    }

    //function to call the Confirmation Fragment
    protected void callCofirmClearList(){
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
        bundle.putString("title", getResources().getString(R.string.confirm_clear_list_heading));
        bundle.putString("message", getResources().getString(R.string.confirm_clear_list_message));
        bundle.putString("confirm", getResources().getString(R.string.confirm_btn_yes));
        bundle.putString("reject", getResources().getString(R.string.confirm_btn_no));
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
                productListDetailsRef.removeValue();
                ((Activity) context).finish();
            }

            @Override
            protected void negativeClick() {
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
        bundle.putString("confirm", getResources().getString(R.string.confirm_btn_yes));
        bundle.putString("reject", getResources().getString(R.string.confirm_btn_no));
        dialog.setArguments(bundle);
        //Here we show the dialog
        //The tag "MyFragement" is not important for us.
        dialog.show(getFragmentManager(), "MyFragment");
    }

    //load login Activity
    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
