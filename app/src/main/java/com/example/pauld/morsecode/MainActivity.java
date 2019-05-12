package com.example.pauld.morsecode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * MainActivity.java
 *
 *  The purpose of this activity is to act as a landing page for the application.
 *  It provides access to all key features via buttons which on click will redirect to the appropriate activity.
 *
 * References (Also mentioned in the README.md):
 *      https://firebase.google.com/docs/android/setup
 *
 *  @author Samarth Raval B00812673
 */

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth userAuth;
    private View.OnClickListener loginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_lessons, btn_txt_to_morse, btn_morse_to_txt, btn_settings;

        userAuth = FirebaseAuth.getInstance();

        btn_lessons      = findViewById(R.id.btn_main_lessons);
        btn_txt_to_morse = findViewById(R.id.btn_main_text_to_morse);
        btn_morse_to_txt = findViewById(R.id.btn_main_morse_to_text);
        btn_settings     = findViewById(R.id.btn_main_settings);

        //On Button pressed LOGIN Screen will open
        loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        };

        //On Button pressed LESSONS Screen will open
        btn_lessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Lessons.class);
                startActivity(intent);
            }
        });

        //On Button pressed TEXT TO MORSE Screen will open
        btn_txt_to_morse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), textToMorseActivity.class);
                startActivity(intent);
            }
        });

        //On Button pressed MORSE TO TEXT Screen will open
        btn_morse_to_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), FreestyleInput.class);
                startActivity(intent);
            }
        });

        //On Button pressed SETTINGS Screen will open
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        user = userAuth.getCurrentUser();
        Button btn_login = findViewById(R.id.btn_main_login);
        if( user == null){
            btn_login.setOnClickListener(loginListener);
        }
        else{
            btn_login.setText("Logout");
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    userAuth.signOut();
                    Toast.makeText(getApplicationContext(),"You have logged out!",Toast.LENGTH_SHORT).show();
                    b.setText("Login");
                    b.setOnClickListener(loginListener);
                }
            });
        }
    }
}