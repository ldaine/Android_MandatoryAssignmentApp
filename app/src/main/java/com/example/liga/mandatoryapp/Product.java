package com.example.liga.mandatoryapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Liga on 04-04-2016.
 */
public class Product implements Parcelable {
    String name;
    int quantity;

    public Product(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
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