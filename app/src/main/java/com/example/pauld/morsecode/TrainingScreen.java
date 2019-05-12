/**
 * TrainingScreen.java
 *      The training screen provides the user with the screen which
 *      they perform lessons on. These lesson plans are input through the intent
 *      and are space separated. Utilizes a morse brain for the backend morse
 *      processing.
 *
 *      @author TomSmith B00694293
 * */

package com.example.pauld.morsecode;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

public class TrainingScreen extends AppCompatActivity{
    private String lessonString, currentWordStr, currentLetterStr, lessonID;
    private StringTokenizer lessonTokens;
    private TextView currentWord, currentLetter, currentLetterMorse, currentInputLetter, currentInputLetterMorse;
    private View morseInputButton;
    private Button morseResetButton;
    private boolean newWord, blockInput, completedLesson;
    private int wordCharIndex;
    private double screenHeight, screenWidth;
    private final double screenReferenceHeight = 2076;
    private final double screenReferenceWidth = 1080;
    private MorseBrain brain;
    private ProgressBar progressBar;
    private Driver feedbackDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_screen);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        // Link the TextViews/Buttons
        currentWord = findViewById(R.id.current_training_word);
        currentLetter = findViewById(R.id.current_training_letter);
        currentLetterMorse = findViewById(R.id.current_training_letter_morse_code);
        currentInputLetter = findViewById(R.id.current_predicted_letter);
        currentInputLetterMorse = findViewById(R.id.current_morse_input);
        morseInputButton = findViewById(R.id.training_screen_button);
        morseResetButton = findViewById(R.id.training_screen_reset_button);
        progressBar = findViewById(R.id.training_progress_bar);
        blockInput = false;
        completedLesson = false;
        TextView tempTextView = findViewById(R.id.temptextview);

        formatSizes();

        feedbackDriver = new Driver( (Vibrator) this.getSystemService(VIBRATOR_SERVICE),
                  (CameraManager) getSystemService(Context.CAMERA_SERVICE),getApplicationContext());
        brain = new MorseBrain(this, currentInputLetterMorse, currentInputLetter, tempTextView, progressBar, feedbackDriver);

        brain.ElectricShock();

        Intent intent = getIntent();

        lessonString = intent.getStringExtra("LESSON_STRING");
        lessonID = intent.getStringExtra("LESSON_NUMBER");
        if(lessonString.isEmpty())
            lessonString = "DEFAULT INPUT LESSON";
        lessonString = lessonString.toUpperCase();
        // Tokenize the lesson by spaces.
        lessonTokens = new StringTokenizer(lessonString, " ");
        gotoNextWord(); // Get first word

        morseInputButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(blockInput || completedLesson)
                    return true;
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        brain.StartInput();
                        break;
                    case MotionEvent.ACTION_UP:
                        brain.EndInput();
                        checkInput();
                        break;
                }
                return true;
            }
        });

        morseResetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                resetButtonInput();
            }
        });

    }

    // Loads in the next word in display and updates the first letter input.
    private void gotoNextWord(){
        // Check if there are any tokens left.
        if(!lessonTokens.hasMoreTokens()){
            lessonCompleted();
            return;
        }
        currentWordStr = lessonTokens.nextToken();
        newWord = true;
        gotoNextLetter();
    }

    private void gotoNextLetter(){
        if(newWord){
            wordCharIndex = 0;
            newWord = false;
        }
        if(wordCharIndex == currentWordStr.length()){
            wordCompleted();
            return;
        }
        currentLetterStr = currentWordStr.substring(wordCharIndex, wordCharIndex + 1);
        currentLetter.setText(currentLetterStr);
        SpannableString underlinedString = new SpannableString(currentWordStr);
        underlinedString.setSpan(new UnderlineSpan(), wordCharIndex, wordCharIndex+1, 0);
        wordCharIndex++;
        currentWord.setText(underlinedString);
        currentLetterMorse.setText(getMorseString(currentLetterStr));
        resetButtonInput();
    }

    private void checkInput(){
        if(currentInputLetter.getText().equals(currentLetterStr)){
            blockInput = true;
            final Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    gotoNextLetter();
                    resetButtonInput();
                    blockInput = false;
                }
            }, 225);
        }
    }

    private void wordCompleted(){
        gotoNextWord();
    }

    private void resetButtonInput(){
        brain.ElectricShock();
    }

    private void lessonCompleted(){
        completedLesson = true;
        currentLetter.setText("Lesson");
        currentLetterMorse.setText("Complete!");
        currentWord.setText("");

        // Set the lesson to completed
        UserInfo user = new UserInfo();
        user.setCompleted(Integer.valueOf(lessonID));
        resetButtonInput();
    }

    private String getMorseString(String character){
        String retString = MorseCodeStandards.GiveLetterGetMorse(character);
        if(retString.equals("~")){
            return "ERROR";
        }
        return retString;
    }

    private void formatSizes(){
        double heightScalar = screenHeight / screenReferenceHeight;
        double widthScalar = screenWidth / screenReferenceWidth;

        // adjust the sizes of buttons

        double tempHeight = ((double)morseInputButton.getLayoutParams().height) * heightScalar;
        double tempWidth = ((double)morseInputButton.getLayoutParams().width) * widthScalar;
        if(tempHeight > tempWidth)
            tempHeight = tempWidth;
        else
            tempWidth = tempHeight;

        morseInputButton.getLayoutParams().height = (int)tempHeight;
        morseInputButton.getLayoutParams().width = (int)tempWidth;

        tempHeight = ((double)morseResetButton.getLayoutParams().height) * heightScalar;
        tempWidth = ((double)morseResetButton.getLayoutParams().width) * widthScalar;

        if(tempHeight > tempWidth)
            tempHeight = tempWidth;
        else
            tempWidth = tempHeight;

        morseResetButton.getLayoutParams().height = (int)tempHeight;
        morseResetButton.getLayoutParams().width = (int)tempWidth;
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        brain.EndOutput();
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        brain.EndOutput();
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        brain.EndOutput();
        super.onPause();
    }

    @Override
    protected void onResume(){
        brain.RefreshBrain();
        super.onResume();
    }

}
