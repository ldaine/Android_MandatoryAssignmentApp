package com.example.liga.mandatoryapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String name;
    private int quantity;
    private String measurement;

    public Product(){
    }

    public Product(String name, int quantity, String measurement){
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setQuantity(int q){
        this.quantity = q;
    }
    public void setMeasurement(String m){
        this.measurement = m;
    }

    public String getName(){
        return this.name;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public String getMeasurement(){
        return this.measurement;
    }

    @Override
    public String toString() {
        if(this.name != null){
            return this.name + ", " + this.quantity + " " + this.measurement;
        }
        return "Error";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeString(measurement);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // "De-parcel object
    public Product(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
    }
}