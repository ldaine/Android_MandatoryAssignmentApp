package com.example.liga.mandatoryapp;

import android.app.Application;
import android.content.Intent;

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


        if (new Firebase(Constants.FIREBASE_URL).getAuth() == null) {
            loadLoginView();
        }

    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
