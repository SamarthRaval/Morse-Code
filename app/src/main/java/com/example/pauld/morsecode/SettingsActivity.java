package com.example.pauld.morsecode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

/**
 * SettingsActivity.java
 *
 *  The purpose of this activity is to provide tools to the user to modify the applications behaviour.
 *  Modifications include turning off haptic, sound and/or light feedback as well as change the sound frequency and pauses between dots and dashes.
 *  This is the only class that will use the SettingSingleton Setters.
 *
 *  @author Dinesh Sai Bayireddi B00791584
 */

public class SettingsActivity extends AppCompatActivity {
    // Local variables
    private SettingsSingleton s;
    private SeekBar iSpeed,frequency;
    private Switch light,haptic,sound;

    @Override
    protected void onResume(){
        super.onResume();
        iSpeed = findViewById(R.id.touchSpeed);
        frequency = findViewById(R.id.frequencySeekBar);
        haptic = findViewById(R.id.haptic);
        sound = findViewById(R.id.sound);
        light = findViewById(R.id.light);

        s = SettingsSingleton.getInstance();

        frequency.setProgress(s.getFrequency()/40 - 11);
        iSpeed.setProgress(s.getInputSpeed());
        haptic.setChecked(s.getHapticEnabled());
        sound.setChecked(s.getSoundEnabled());
        light.setChecked(s.getLightEnabled());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        haptic = findViewById(R.id.haptic);
        sound = findViewById(R.id.sound);
        light = findViewById(R.id.light);
        iSpeed = findViewById(R.id.touchSpeed);
        frequency = findViewById(R.id.frequencySeekBar);
        s = SettingsSingleton.getInstance();

        //check switch and seekbar states on settings activity
        // and set global variables in settings singleton
        haptic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    s.setHapticEnabled(true);
                }
                else{
                    s.setHapticEnabled(false);
                }
                Log.v("hapticEnabled=",""+s.hapticEnabled);

            }
        });

        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    s.setSoundEnabled(true);
                }
                else{
                    s.setSoundEnabled(false);
                }
                Log.v("soundEnabled=",""+s.soundEnabled);

            }
        });

        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    s.setLightEnabled(true);
                }
                else{
                    s.setLightEnabled(false);
                }

                Log.v("lightEnabled=",""+s.lightEnabled);

            }
        });

        iSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                s.setInputSpeed(i);
                Log.v("input speed=",""+s.inputSpeed);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        frequency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                s.setFrequency(440+i*40);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}