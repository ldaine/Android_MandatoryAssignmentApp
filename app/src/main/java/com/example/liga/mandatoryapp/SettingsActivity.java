package com.example.liga.mandatoryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    Firebase ref = new Firebase(Constants.FIREBASE_URL);
    String oldEmail;
    String newEmail;
    String password;
    String user_id = ref.getAuth().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //The name chosen below is important - it should match
        //the name in the MainActivity
        PreferenceManager manager = getPreferenceManager();
        manager.setSharedPreferencesName(Constants.SHARED_PREF_NAME);
        //Adding the layout from the xml file
        oldEmail = manager.getSharedPreferences().getString(Constants.KEY_PREF_EMAIL, "");
        password = manager.getSharedPreferences().getString(Constants.KEY_PREF_PASSWORD, "");

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
                    editor.commit();
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