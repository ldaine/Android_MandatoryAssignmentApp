package com.example.liga.mandatoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShoppingListOverviewActivity extends AppCompatActivity {

    //properties
    ArrayAdapter<List> adapterShoppingListArray;
    ArrayList<List> shoppingListArray = new ArrayList<List>();
    Firebase listRef;

    int shoppingListCheckedItemPosition;

    //methods
    public ArrayAdapter getShoppingListArrayAdapter() {
        return adapterShoppingListArray;
    }

    //method owerride
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_overview);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            ArrayList list = savedInstanceState.getParcelableArrayList("savedShoppingListArray");
            if (list != null) {
                shoppingListArray = list;
            }
        }
        //listRef = new Firebase("https://shoppingappbaaa2016.firebaseio.com/shoppingList");

        final EditText inputNewList = (EditText) findViewById(R.id.inputNewList);
        final Button goToMainButton = (Button) findViewById(R.id.goToMain);
        final Button addListButton = (Button) findViewById(R.id.addListButton);
        final ListView ShoppingListView = (ListView) findViewById(R.id.shoppingListArrayView);

        adapterShoppingListArray = new ArrayAdapter<List>(this,
        android.R.layout.simple_list_item_checked, shoppingListArray);

        assert ShoppingListView != null;
        ShoppingListView.setAdapter(adapterShoppingListArray);

        ShoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //shoppingListCheckedItemPosition = ShoppingListView.getCheckedItemPosition();
                //latestRemovedBagItem = bag.get(latestRemovedBagItemPosition); //save a copy of the deleted item
                Intent intent = new Intent(ShoppingListOverviewActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", id);
                intent.putExtra("greetingBundle", bundle);
                intent.putExtra("message", "Hi there");
                intent.putExtra("shoppingListCheckedItemPosition", position);

                startActivity(intent);
                //bag.remove(latestRemovedBagItemPosition); //remove item
            }
        });


        //setting listener functions
        assert addListButton != null;
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the value from edit field
                assert inputNewList != null;
                String listName = inputNewList.getText().toString();

                List listItem = new List(listName, new ArrayList<Product>()); //name and q are from the input fields from the user of course.
                Product p = new Product(listName, 3, "peaces");
                listItem.addProduct(p);
                //Date date = new Date();
                // Generate a reference to a new location and add some data using push()
                //Map<String, String> map = new HashMap<String, String>();
                //map.put("name", listName);
                //map.put("dateCreated", date.toString());
                listRef.push().setValue(listItem);

                //listRef.push().setValue(listItem);

                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getShoppingListArrayAdapter().notifyDataSetChanged();
                inputNewList.setText("");

                //adding the element to the list
                //shoppingListArray.add(new List(listName));


            }
        });



        goToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingListOverviewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        /*
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        */
    }

    @Override
    protected void onStart() {
        super.onStart();

        final EditText inputNewList = (EditText) findViewById(R.id.inputNewList);
        final Button goToMainButton = (Button) findViewById(R.id.goToMain);
        final Button addListButton = (Button) findViewById(R.id.addListButton);
        final ListView ShoppingListView = (ListView) findViewById(R.id.shoppingListArrayView);

        listRef = new Firebase("https://shoppingappbaaa2016.firebaseio.com/shoppingList");

        //FirebaseListAdapter<List> shoppinglistFirebaseAdapter = new FirebaseListAdapter<List>(this, List.class, R.id.shoppingListArrayView, listRef) {
        //    @Override
        //    protected void populateView(View view, List list, int i) {
        //        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        //        textView.setText(list.toString());
        //    }
        //};
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("savedShoppingListArray", shoppingListArray);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        this.shoppingListArray = savedState.getParcelableArrayList("savedShoppingListArray");

    }

}
