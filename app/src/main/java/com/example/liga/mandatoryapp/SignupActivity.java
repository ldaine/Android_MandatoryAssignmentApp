package com.example.liga.mandatoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    protected EditText passwordEditText;
    protected EditText emailEditText;
    protected Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        passwordEditText = (EditText)findViewById(R.id.passwordField);
        emailEditText = (EditText)findViewById(R.id.emailField);
        signUpButton = (Button)findViewById(R.id.signupButton);

        final Firebase ref = new Firebase(Constants.FIREBASE_URL);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = passwordEditText.getText().toString().trim();
                final String email = emailEditText.getText().toString().trim();

                //password = password.trim();
                //email = email.trim();

                if (password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // signup - creating user
                    ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {

                            //confirmation for signup
                            //after clicking ok, the user is logged in.
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            builder.setMessage(R.string.signup_success)
                                    .setPositiveButton(R.string.login_button_label, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            //write email in preferences
                                            SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString(Constants.KEY_PREF_EMAIL, email);
                                            editor.putString(Constants.KEY_PREF_PASSWORD, password);
                                            editor.commit();

                                            //auto login
                                            ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                                                @Override
                                                public void onAuthenticated(AuthData authData) {
                                                    // Authenticated successfully with payload authData
                                                    //creating the data object in firebase
                                                    Map<String, Object> map = new HashMap<String, Object>();
                                                    map.put("createDate", new Date());
                                                    map.put("email", email);
                                                    ref.child("users").child(authData.getUid()).setValue(map);

                                                    //redirecting to Main Activity
                                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onAuthenticationError(FirebaseError firebaseError) {
                                                    // Authenticated failed with error firebaseError
                                                    //Raise Alert Error
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                                    builder.setMessage(firebaseError.getMessage())
                                                            .setTitle(R.string.login_error_title)
                                                            .setPositiveButton(android.R.string.ok, null);
                                                    AlertDialog dialog = builder.create();
                                                    dialog.show();
                                                }
                                            });
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            builder.setMessage(firebaseError.getMessage())
                                    .setTitle(R.string.signup_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
            }
        });
    }
}
