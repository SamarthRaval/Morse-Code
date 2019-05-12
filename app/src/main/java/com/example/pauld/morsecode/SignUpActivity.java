package com.example.pauld.morsecode;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * SignUpActivity.java
 *      The purpose of this class is to populate the screen with information/tools regarding registration for first time users.
 *      Upon successful account creation in the activity ends and the user returns to the landing page.
 *      If somehow a user who is already has an account tries to create a new account with the same credentials or invalid credentials an error message is displayed.
 *
 * References (Also mentioned in the README.md):
 *      https://firebase.google.com/docs/android/setup
 *
 *      @author George Faraj B00638341
 */

public class SignUpActivity extends AppCompatActivity {

    private Button btn_signUp;
    private EditText edit_email ,edit_pass;
    private String password,email;
    private FirebaseAuth userAuth;
    private FirebaseDatabase firebaseDBInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //User Init
        userAuth = FirebaseAuth.getInstance();

        //Database Init
        firebaseDBInstance = FirebaseDatabase.getInstance();

        btn_signUp = findViewById(R.id.btn_signUp);
        edit_email = findViewById(R.id.edit_email);
        edit_pass = findViewById(R.id.edit_pass);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edit_email.getText().toString();
                password = edit_pass.getText().toString();
                if(email.isEmpty() || password.isEmpty() ){
                    if(email.isEmpty()){
                        edit_email.setHint("Enter a valid email");
                        edit_email.setHintTextColor(getColor(R.color.colorAccent));
                    }
                    if(password.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Password field shoudn't be empty",Toast.LENGTH_SHORT).show();
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

                // https://firebase.google.com/docs/auth/android/password-auth
                userAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = userAuth.getCurrentUser();
                                    DatabaseReference userData = firebaseDBInstance.getReference("users").child(user.getUid());
                                    userData.child("ID").setValue(user.getUid());
                                    userData.child("lessonsCompleted").child("0").setValue(0);
                                    userData.child("lessonsCompleted").child("1").setValue(0);
                                    userData.child("lessonsCompleted").child("2").setValue(0);
                                    userData.child("lessonsCompleted").child("3").setValue(0);
                                    userData.child("lessonsCompleted").child("4").setValue(0);
                                    userData.child("lessonsCompleted").child("5").setValue(0);
                                    userData.child("lessonsCompleted").child("6").setValue(0);
                                    userData.child("lessonsCompleted").child("7").setValue(0);
                                    userData.child("lessonsCompleted").child("8").setValue(0);
                                    userData.child("lessonsCompleted").child("9").setValue(0);
                                    Toast.makeText(SignUpActivity.this, "Account Created! You are now logged in.",Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("SINGLETAG", "createUserWithEmail:failure");
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
