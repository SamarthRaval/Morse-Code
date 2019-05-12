/**
 * Lessons.java
 *      The purpose of this screen is to provide the user with a
 *      list of lessons to complete and generate the lesson screen.
 *
 *      @TomSmith B00694293
 * */

package com.example.pauld.morsecode;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Lessons extends AppCompatActivity {
    private final int NUMBER_OF_LESSONS = 10;
    private ArrayList<String> lessonsList;
    private String[] lessonStrings;
    private boolean[] lessonsCompletedByUser;
    private ListView lessonListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        initiateLessonArray();
        lessonsList = new ArrayList<String>();
        lessonListView = findViewById(R.id.lessonListView);

        for(int i = 0; i < NUMBER_OF_LESSONS; i++){
            String lessonName = "Lesson " + String.valueOf(i+1);
            lessonsList.add(lessonName);
        }

        UserInfo user = new UserInfo();

        user.getCompletedLessons(new UserInfoListener() {
            @Override
            public void onArrayFetched(boolean[] lessonsCompleted) {
                lessonsCompletedByUser = lessonsCompleted;
                // This arrayadapter is used to populate the list's display
                ArrayAdapter<String> listPopulator = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, lessonsList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView t = (TextView) view.findViewById(android.R.id.text1);
                        t.setTextColor(Color.BLACK);
                        if(lessonsCompletedByUser[position]) {
                            t.setBackgroundColor(Color.GREEN);
                            t.setText(t.getText() + "\t\t\t\tCompleted");
                        }else{
                            t.setBackgroundColor(16514043);
                        }

                        return view;
                    }
                };
                // Set the list to display the adapter.
                lessonListView.setAdapter(listPopulator);
            }
        });

        lessonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view;
                Intent intent=new Intent(getApplicationContext(),TrainingScreen.class);
                // Hand the lesson activity the lesson plan
                intent.putExtra("LESSON_STRING", lessonStrings[i]);
                // Hand the lesson activity the lesson number
                intent.putExtra("LESSON_NUMBER", String.valueOf(i));
                startActivity(intent);
            }
        });
    }

    private void initiateLessonArray(){
        lessonStrings = new String[NUMBER_OF_LESSONS];
        lessonStrings[0] = getResources().getString(R.string.CommonAlphabetLesson);
        lessonStrings[1] = getResources().getString(R.string.FullAlphabetLesson);
        lessonStrings[2] = getResources().getString(R.string.ShortNumberLesson);
        lessonStrings[3] = getResources().getString(R.string.VeryShortWordLesson1);
        lessonStrings[4] = getResources().getString(R.string.VeryShortWordLesson2);
        lessonStrings[5] = getResources().getString(R.string.ShortWordLesson);
        lessonStrings[6] = getResources().getString(R.string.MediumWordLesson1);
        lessonStrings[7] = getResources().getString(R.string.MediumWordLesson2);
        lessonStrings[8] = getResources().getString(R.string.LongNumberLesson);
        lessonStrings[9] = getResources().getString(R.string.LongWordLesson);
    }
}
