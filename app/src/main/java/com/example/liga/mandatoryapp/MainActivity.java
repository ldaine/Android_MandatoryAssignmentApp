package com.example.liga.mandatoryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import com.firebase.ui.FirebaseListAdapter;


public class MainActivity extends AppCompatActivity {

    //properties
    private String userId;

    //there is no need for array list and array list adapter. The firebase UI is handling everything for us.
    private Firebase mRef = new Firebase(Constants.FIREBASE_URL);
    private Firebase listRef;
    FirebaseListAdapter<ShoppingList> shoppinglistFirebaseAdapter;

    EditText inputShoppingListName;
    ImageButton buttonShoppingListAdd;
    ListView listShoppingListView;

    //method owerride
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Authentication based on tutorial
        //Credit: http://www.sitepoint.com/creating-a-cloud-backend-for-your-android-app-using-firebase/
        if (mRef.getAuth() == null) {
            loadLoginView();
        }

        //trying to get the userId
        try {
            userId = mRef.getAuth().getUid();
        } catch (Exception e) {
            loadLoginView();
        }

        setActionBarTitle();

        /*set bindings*/
        inputShoppingListName = (EditText) findViewById(R.id.inputShoppingListName);
        buttonShoppingListAdd = (ImageButton) findViewById(R.id.buttonShoppingListAdd);
        listShoppingListView = (ListView) findViewById(R.id.listShoppingList);

        String shoppingListUrl = Constants.FIREBASE_URL + "/users/" + userId + "/lists";
        listRef = new Firebase(shoppingListUrl);

        shoppinglistFirebaseAdapter = new FirebaseListAdapter<ShoppingList>(this, ShoppingList.class, android.R.layout.two_line_list_item, listRef) {
            @Override
            protected void populateView(View view, ShoppingList list, int i) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(list.toString());
            }
        };

        listShoppingListView.setAdapter(shoppinglistFirebaseAdapter);

        listShoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = shoppinglistFirebaseAdapter.getRef(position).getKey();
                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                intent.putExtra("key", key);

                startActivity(intent);
            }
        });

        //setting listener functions
        assert buttonShoppingListAdd != null;
        buttonShoppingListAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the value from edit field
                assert inputShoppingListName != null;
                String listName = inputShoppingListName.getText().toString();

                //if no list name was entered
                if (listName.isEmpty()) {
                    //raise alert
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.list_input_error_message)
                            .setTitle(R.string.list_input_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //creating new list item
                    ShoppingList listItem = new ShoppingList(listName);

                    //pushing the new list to firebase
                    listRef.push().setValue(listItem);

                    //reset the input field
                    inputShoppingListName.setText("");
                }
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

    //Here we put the code for what should happen in the app once
    //the user selects one of our menu items (regardless of whether
    //it is in the actionbar or in the overflow menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings:
                //Here we create a new activity and we instruct the
                //Android system to start it
                Intent intent = new Intent(this, SettingsActivity.class);

                //we can use this, if we need to know when the user exists our preference screens
                startActivityForResult(intent, 1);
                return true;
            case R.id.item_logout:
                mRef.unauth();
                loadLoginView();
                return true;
        }

        return false; //we did not handle the event
    }

    @Override
    protected void onResume() {
        setActionBarTitle();
        super.onResume();
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setActionBarTitle(){
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        String prefName = prefs.getString(Constants.KEY_PREF_NAME, "");
        String title;
        if(prefName.equals("")){
            title = "My Lists";
        } else {
            title = prefName + "'s Lists";
        }
        getSupportActionBar().setTitle(title);
    }

}
