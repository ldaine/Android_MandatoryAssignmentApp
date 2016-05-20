package com.example.liga.mandatoryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager manager = getPreferenceManager();
        //The name chosen below is important - it should match
        //the name in the MainActivity
        manager.setSharedPreferencesName(Constants.SHARED_PREF_NAME);
        //Adding the layout from the xml file
        addPreferencesFromResource(R.xml.prefs);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Constants.KEY_PREF_EMAIL)) {
            Preference emailPref = findPreference(key);
            SharedPreferences shared = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
            Log.d("PREFS", emailPref.getContext().toString());
            // Set summary to be the user-description for the selected value
            //connectionPref.setSummary(sharedPreferences.getString(key, ""));
        } else if (key.equals(Constants.KEY_PREF_PASSWORD)){

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