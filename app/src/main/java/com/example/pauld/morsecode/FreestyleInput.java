/**
 * FreestyleInput.java
 *      The purpose of this class is to provide the backend for the freestyle input
 *      page. This class utilizes a morseBrain to handle the morse processing, and
 *      simply provides the UI.
 *
 *      @TomSmith B00694293
 * */

package com.example.pauld.morsecode;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;

public class FreestyleInput extends AppCompatActivity {

    private TextView totalInput, predictedInput, predictedMorse;
    private Button resetButton, addLetterButton, resetCurrentInput, morseDictionary;
    private ProgressBar progressbar;
    private View inputButton;
    private double screenHeight, screenWidth;
    private final double screenReferenceHeight = 2076;
    private final double screenReferenceWidth = 1080;

    private MorseBrain brain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freestyleinput);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        totalInput = findViewById(R.id.freestyle_current_total_input);
        predictedInput = findViewById(R.id.freestyle_current_predicted_letter);
        predictedMorse = findViewById(R.id.freestyle_current_morse_input);

        resetButton = findViewById(R.id.freestyle_screen_reset_button);
        inputButton = findViewById(R.id.freestyle_screen_button);
        addLetterButton = findViewById(R.id.freestyle_add_letter_btn);
        resetCurrentInput = findViewById(R.id.freestyle_reset_input_btn);
        progressbar = findViewById(R.id.freestyle_progress_bar);
        morseDictionary = findViewById(R.id.morse_help);

        formatSizes();

        Driver feedbackDriver = new Driver( (Vibrator) this.getSystemService(VIBRATOR_SERVICE),(CameraManager) getSystemService(Context.CAMERA_SERVICE),getApplicationContext());
        brain = new MorseBrain(this, predictedMorse, predictedInput, totalInput, progressbar, feedbackDriver);
        brain.ElectricShock();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brain.ResetCurrentChar();
            }
        });

        inputButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        brain.StartInput();
                        break;
                    case MotionEvent.ACTION_UP:
                        brain.EndInput();
                        break;
                }
                return true;
            }
        });

        addLetterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brain.SignalCharEnd();
            }
        });

        resetCurrentInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brain.ElectricShock();
            }
        });

        morseDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MorseDictionaryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void formatSizes(){
        double heightScalar = screenHeight / screenReferenceHeight;
        double widthScalar = screenWidth / screenReferenceWidth;

        // adjust the sizes of buttons

        double tempHeight = ((double)inputButton.getLayoutParams().height) * heightScalar;
        double tempWidth = ((double)inputButton.getLayoutParams().width) * widthScalar;
        if(tempHeight > tempWidth)
            tempHeight = tempWidth;
        else
            tempWidth = tempHeight;

        inputButton.getLayoutParams().height = (int)tempHeight;
        inputButton.getLayoutParams().width = (int)tempWidth;

        tempHeight = ((double)resetButton.getLayoutParams().height) * heightScalar;
        tempWidth = ((double)resetButton.getLayoutParams().width) * widthScalar;

        if(tempHeight > tempWidth)
            tempHeight = tempWidth;
        else
            tempWidth = tempHeight;

        resetButton.getLayoutParams().height = (int)tempHeight;
        resetButton.getLayoutParams().width = (int)tempWidth;
    }

    @Override
    protected void onResume(){
        brain.RefreshBrain();
        super.onResume();
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
}