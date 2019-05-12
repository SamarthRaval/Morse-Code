package com.example.pauld.morsecode;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * LoginActivity.java
 *      The purpose of this class is to populate the screen with information regarding login for returning users.
 *      Additionally it links to the SignUpActivity, terminating itself so that first time users may create an account.
 *      It performs standard input checking prior to submission, alerting the user if something is missing.
 *      Upon successful sign in the activity ends and the user returns to the landing page.
 *      If somehow a user who is already logged in were to reach this page it would terminate, post a message, and redirect to the prior activity.
 *
 * References (Also mentioned in the README.md):
 *      https://firebase.google.com/docs/android/setup
 *
 *      @author George Faraj B00638341
 */

public class LoginActivity extends AppCompatActivity {

    private Button btn_login,btn_register;
    private EditText edit_email ,edit_pass;
    private String password,email;
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //User Init
        userAuth = FirebaseAuth.getInstance();

        if(userAuth.getCurrentUser() != null ){
            Toast.makeText(getApplicationContext(),"You are already logged in!",Toast.LENGTH_SHORT).show();
            finish();
        }

        btn_login = findViewById(R.id.btn_login);
        edit_email = findViewById(R.id.edit_email);
        edit_pass = findViewById(R.id.edit_pass);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edit_email.getText().toString();
                password = edit_pass.getText().toString();
                email = edit_email.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    if(email.isEmpty()){
                        edit_email.setHint("Enter a valid email");
                        edit_email.setHintTextColor(getColor(R.color.colorAccent));
                    }
                    if(password.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Password field shouldn't be empty",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(),"Email address is not valid",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length() < 6){
                    Toast.makeText(getApplicationContext(),"Password should be at least 6 characters long",Toast.LENGTH_SHORT).show();
                    return;
                }

                userAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("SINGLETAG", "signInWithEmail:success");
                                    Toast.makeText(getApplicationContext(), "Login Successful! You are now logged in.",Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Log.d("SINGLETAG", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
