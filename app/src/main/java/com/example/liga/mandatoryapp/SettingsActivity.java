package com.example.liga.mandatoryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    Firebase ref = new Firebase(Constants.FIREBASE_URL);
    String oldEmail;
    String newEmail;
    String password;
    String user_id = ref.getAuth().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager manager = getPreferenceManager();
        manager.setSharedPreferencesName(Constants.SHARED_PREF_NAME);
        oldEmail = manager.getSharedPreferences().getString(Constants.KEY_PREF_EMAIL, "");
        password = manager.getSharedPreferences().getString(Constants.KEY_PREF_PASSWORD, "");

        //Adding the layout from the xml file
        addPreferencesFromResource(R.xml.prefs);
    }
    //listener to change the email and password when the preferences are for email and password are changed
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferenceManager manager = getPreferenceManager();
        if (key.equals(Constants.KEY_PREF_EMAIL)) {
            //SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
            newEmail = manager.getSharedPreferences().getString(Constants.KEY_PREF_EMAIL, "");
            Log.d("PREFS oldEmail", oldEmail);
            Log.d("PREFS newEmail", newEmail);
            Log.d("PREFS password", password);

            //change email in Firebase user Account
            ref.changeEmail(oldEmail, password, newEmail, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    Log.d("FIREBASE", "EMAIL WAS CHANGED");
                    ref.child("users").child(user_id).child("email").setValue(newEmail);
                    oldEmail = newEmail;
                }
                @Override
                public void onError(FirebaseError firebaseError) {
                    Log.d("FIREBASE ERROR", firebaseError.toString());

                    //change the email in preferences back
                    SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constants.KEY_PREF_EMAIL, oldEmail);
                    editor.apply();

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage("The Email could not be changed")
                            .setTitle("Error")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            Log.d("PREFS", "change email here");
        } else if (key.equals(Constants.KEY_PREF_PASSWORD)){
            //change password in Firebase user Account
            Log.d("PREFS", "change password here");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}