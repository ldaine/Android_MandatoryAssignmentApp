package com.example.liga.mandatoryapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Liga on 12-05-2016.
 */
public class List implements Parcelable {
    private String name;
    private Date date = new Date();
    private ArrayList<Product> products;
    //constructors
    public List(){
    }

    public List(String name, ArrayList<Product> products){
        this.name = name;
        this.products = products;
    }

    //interface methods
    public void setName(String name){
        this.name = name;
    }

    public void addProduct(Product product){
        this.products.add(product);
    }

    public String getName(){
        return this.name;
    }

    public String getDate(){
        return this.date.toString();
    }

    public ArrayList<Product> getProducts(){
        return this.products;
    }


    //for lists
    @Override
    public String toString() {
        return this.name  + " [" + this.products.size() + "]";
    }

    //for orientation change
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(products);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public List[] newArray(int size) {
            return new List[size];
        }
    };

    // "De-parcel object
    public List(Parcel in) {
        name = in.readString();

        products = new ArrayList<Product>();
        products = in.readArrayList(Product.class.getClassLoader());
    }
}
