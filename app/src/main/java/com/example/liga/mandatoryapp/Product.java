package com.example.liga.mandatoryapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Liga on 04-04-2016.
 */
public class Product implements Parcelable {
    String name;
    int quantity;
    String measurment;

    public Product(String name, int quantity, String measurment){
        this.name = name;
        this.quantity = quantity;
        this.measurment = measurment;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.quantity + " " + this.measurment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeString(measurment);
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