package com.example.liga.mandatoryapp;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Liga on 12-05-2016.
 */
public class ShoppingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

    }
}
