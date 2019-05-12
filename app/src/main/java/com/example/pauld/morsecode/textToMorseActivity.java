package com.example.pauld.morsecode;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.PointerIcon;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * textToMorseActivity.java
 *      The purpose of this class is to populate the screen with information/tools for translating text to MorseCode.
 *      The user simply selects the TextEdit object at the bottom of the screen and enters text they wish to see displayed in Morse.
 *      Once the user clicks the Generate Morse function the Activity will begin outputting Morse feedback based on the settings.
 *
 *      To accomplish this the MorseBrain is instantiated with the relevant TextView elements and Driver class.
 *      The onClick listener of the Generate Morse function simply calls the outputMorse function of the MorseBrain.
 *
 *      @author George Faraj B00638341
 */
public class textToMorseActivity extends AppCompatActivity {

    private Button btn_generate;
    private TextView txt_currentWord,txt_currentMorse,txt_currentLetter;
    private EditText txt_inputText;
    private ProgressBar emptyBar;
    private MorseBrain brain;
    private Driver outputDriver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_morse);

        btn_generate = findViewById(R.id.btn_generate);
        txt_inputText = findViewById(R.id.txt_inputText);
        txt_currentWord = findViewById(R.id.txt_currentWord);
        txt_currentMorse = findViewById(R.id.txt_currentMorse);
        txt_currentLetter = findViewById(R.id.txt_currentLetter);
        emptyBar = findViewById(R.id.emptyBar);

        outputDriver = new Driver( (Vibrator) this.getSystemService(VIBRATOR_SERVICE),
                                    (CameraManager) getSystemService(Context.CAMERA_SERVICE),
                                    getApplicationContext());

        brain = new MorseBrain(getApplicationContext(),txt_currentMorse,txt_currentLetter,
                txt_currentWord,emptyBar,outputDriver);

        txt_currentWord.setText("Word");
        txt_currentLetter.setText("W");
        txt_currentMorse.setText("•--");

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brain.OutputMorse(txt_inputText.getText().toString().toUpperCase());
            }
        });
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
