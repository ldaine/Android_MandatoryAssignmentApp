package com.example.liga.mandatoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import com.firebase.ui.FirebaseListAdapter;

import java.util.List;


public class ShoppingListOverviewActivity extends AppCompatActivity {

    //properties
    //there is no need for array list and array list adapter. The firebase UI is handling everything for us.
    Firebase listRef = new Firebase("https://shoppingappbaaa2016.firebaseio.com/shoppingList");
    FirebaseListAdapter<ShoppingList> shoppinglistFirebaseAdapter;

    //method owerride
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_overview);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //for orientation change
        //if (savedInstanceState != null) {
            //ArrayList list = savedInstanceState.getParcelableArrayList("savedShoppingListArray");
            //if (list != null) {
                //shoppingListArray = list;
            //}
        //}

        //Layout elements
        final EditText inputNewList = (EditText) findViewById(R.id.inputNewList);
        final Button addListButton = (Button) findViewById(R.id.addListButton);
        final ListView ShoppingListView = (ListView) findViewById(R.id.shoppingListArrayView);

        shoppinglistFirebaseAdapter = new FirebaseListAdapter<ShoppingList>(this, ShoppingList.class, android.R.layout.two_line_list_item, listRef) {
            @Override
            protected void populateView(View view, ShoppingList list, int i) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(list.toString());
            }
        };

        ShoppingListView.setAdapter(shoppinglistFirebaseAdapter);

        ShoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = shoppinglistFirebaseAdapter.getRef(position).getKey();
                //Log.i("position", String.valu);
                Intent intent = new Intent(ShoppingListOverviewActivity.this, ShoppingListActivity.class);
                intent.putExtra("key", key);

                startActivity(intent);
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

                //creating new list item
                ShoppingList listItem = new ShoppingList(listName);
                Product p1 = new Product("apple", 2, "peaces");
                Product p2 = new Product("banana", 3, "peaces");
                //listItem.addProduct(p1);
                //listItem.addProduct(p2);

                //pushing the new list to firebase
                listRef.push().setValue(listItem);

                //reset the input field
                inputNewList.setText("");
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

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("savedShoppingListArray", shoppingListArray);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        //this.shoppingListArray = savedState.getParcelableArrayList("savedShoppingListArray");

    }

}
