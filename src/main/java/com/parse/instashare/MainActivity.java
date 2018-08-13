/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.instashare;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signup_mode = true;
    TextView change_signup_mode;

    public void showUserList(){

        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() ==KeyEvent.ACTION_DOWN ){

            signUp(view);
        }

        return false;
    }


    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.change_signup){
            Button signup_butt = (Button) findViewById(R.id.signup_button);

            if(signup_mode){
               signup_mode = false;
               signup_butt.setText("Login");
               change_signup_mode.setText("or, Sign Up");
            }else {
                signup_mode = true;
                signup_butt.setText("Sign Up");
                change_signup_mode.setText("or, Login");
            }
        }else if (view.getId() == R.id.backgroud_relative || view.getId()==R.id.logo_image) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }



    public void signUp (View view){

        EditText username = (EditText) findViewById(R.id.username_edit);
        EditText password = (EditText) findViewById(R.id.password_edit);

        if(username.getText().toString().matches("") || password.getText().toString().matches("")){

            Toast.makeText(this, "A Username and password are required",Toast.LENGTH_LONG).show();
        }else {

            if (signup_mode) {

                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("SignUp", "Siccessfull");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } else {

                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (user != null){
                            Log.d("SignUp","Login Successful");
                            showUserList();
                        }else {
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }


    }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();

      setContentView(R.layout.activity_main);

    setTitle("InstaShare");

    RelativeLayout backgroud_relative = (RelativeLayout) findViewById(R.id.backgroud_relative);
    ImageView logo = (ImageView) findViewById(R.id.logo_image);

    backgroud_relative.setOnClickListener(this);
    logo.setOnClickListener(this);

      change_signup_mode = (TextView) findViewById(R.id.change_signup);
      change_signup_mode.setOnClickListener(this);

      EditText password = (EditText) findViewById(R.id.password_edit);
      password.setOnKeyListener(this);

      if(ParseUser.getCurrentUser() != null){
          showUserList();
      }


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}

