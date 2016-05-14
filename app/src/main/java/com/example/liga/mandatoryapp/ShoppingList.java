package com.example.liga.mandatoryapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Liga on 12-05-2016.
 */
public class ShoppingList implements Parcelable {
    private String name;
    private Date date = new Date();
    private Map<String, Product> products;
    //constructors
    public ShoppingList(){
    }

    public ShoppingList(String name){
        this.name = name;
        this.products = new HashMap<String, Product>();
    };

    //interface methods
    public void setName(String name){
        this.name = name;
    }
    public void setDate(Date date){
        this.date = date;
    }

    public void addProduct(String name, Product product){

        this.products.put(name, product);
    }

    public void setProducts(Map<String, Product> products){

        this.products = products;
    }

    public String getName(){
        return this.name;
    }

    public Date getDate(){
        return this.date;
    }

    public Map<String, Product> getProducts(){
        return this.products;
    }


    //for lists
    @Override
    public String toString() {
        if(this.name != null && this.products != null){
            return this.name + " (" + String.valueOf(this.products.size()) + ")";
        } else if (this.name != null) {
            return this.name + " (0)";
        }
        return "Error";
    }

    //for orientation change
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        //dest.writeList(products);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };

    // "De-parcel object
    public ShoppingList(Parcel in) {
        name = in.readString();

        //products = new ArrayList<Product>();
        //products = in.readArrayList(Product.class.getClassLoader());
    }
}
