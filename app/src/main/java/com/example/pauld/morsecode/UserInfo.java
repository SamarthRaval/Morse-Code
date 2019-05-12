package com.example.pauld.morsecode;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * UserInfo.java
 *      The purpose of this class is to provide other classes with access to users information.
 *      Classes calling this one do not need to perform any checks for user login, merely use the public functions provided.
 *      Ultimately this class is completely modular, other activities don't need to know anything about the database.
 *
 * References (Also mentioned in the README.md):
 *      https://firebase.google.com/docs/android/setup
 *
 *      @author George Faraj B00638341
 */

public class UserInfo {
    private FirebaseUser user;
    private FirebaseAuth userAuth;
    private FirebaseDatabase firebaseDBInstance;
    private boolean[] lessonsCompleted;

    public UserInfo(){
        firebaseDBInstance = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance();
        user = userAuth.getCurrentUser();
    }

    /**
     * Public method getCompletedLessons(UserInfoListener callback)
     *  If the user is not logged in an array of false booleans is sent to the callback
     *  otherwise the array is populated with booleans representing which lessons have been completed.
     *
     *  To access the array the developer must override the onArrayFetched of the callback interface.
     *
     */
    public void getCompletedLessons(final UserInfoListener callback) {
        lessonsCompleted = new boolean[10];
        if (user == null) {
            for(int i = 0; i < 10; i++)
                lessonsCompleted[i] = false;
            callback.onArrayFetched(lessonsCompleted);
            return;
        }
        firebaseDBInstance.getReference("users").child(user.getUid()).child("lessonsCompleted").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    lessonsCompleted[Integer.valueOf(d.getKey())] = (1 == Integer.valueOf(d.getValue().toString()));
                }
                callback.onArrayFetched(lessonsCompleted);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Public method setCompleted(lessonIndex)
     *  If there is a user logged in then this function will set the zero indexed lesson number
     *   found in the users section of the database to true.
     */
    public void setCompleted(int lessonIndex){
        if (user != null) {
            firebaseDBInstance.getReference("users").child(user.getUid()).child("lessonsCompleted").child(String.valueOf(lessonIndex)).setValue(1);
        }
    }
}
