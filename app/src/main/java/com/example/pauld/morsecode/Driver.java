package com.example.pauld.morsecode;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Vibrator;

/**
 * Driver.java
 *      The purpose of this class is to provide other classes with access to the forms of output available to the device
 *      Namely the light feedback (phone's front facing flash), haptic feedback (device's vibrator), and sound feedback (using the phones sound system).
 *
 * References (Also mentioned in the README.md):
 *      https://progur.com/2016/12/how-to-create-8-bit-music-on-android.html
 *      https://developer.android.com/reference/android/hardware/camera2/CameraManager
 *
 *      @author George Faraj B00638341
 */

/*
    Note on testing:
    To ensure that the output could be set to on and off quickly the following loop was used.

    for(int i = 0;i<5;i++){
        try {
            d.on();
            Log.d("SOUND","ON"+i);
            Thread.sleep(100);
            d.off();
            Log.d("SOUND","OFF"+i);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    It was called on one of the dummy activities created for low level function testing.
*/

public class Driver {

    private Vibrator vibrateHandler;
    private AudioTrack soundHandler;
    private CameraManager lightHandler;
    private byte soundData[];
    private SettingsSingleton desiredSettings;

    public Driver(Vibrator vibratorService, CameraManager systemService, Context applicationContext) {
        desiredSettings = SettingsSingleton.getInstance();
        vibrateHandler = vibratorService;
        generateSound(desiredSettings.getFrequency(), 1);
        soundHandler = new AudioTrack(AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_OUT_DEFAULT,
                AudioFormat.ENCODING_PCM_8BIT, soundData.length,
                AudioTrack.MODE_STATIC
        );
        soundHandler.write(soundData, 0, soundData.length);
        if( applicationContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            lightHandler = systemService;
        }
        else{
            lightHandler = null;
        }
    }

    /**
     * Public method on()
     *  This method will start playing the tone generated, turn on the flashlight and start the haptic feedback.
     *  Whether these components turn on or not is based on the phones capabilities and the options selected in the SettingSingleton.
     */
    public void on(){
        if(SettingsSingleton.getInstance().getHapticEnabled()){
            vibrateHandler.vibrate(999999999 );
        }

        if(soundHandler.getPlayState() != AudioTrack.PLAYSTATE_PLAYING && SettingsSingleton.getInstance().getSoundEnabled()){
            soundHandler.play();
        }

        if( lightHandler != null && SettingsSingleton.getInstance().getLightEnabled()){
            try{
                String cameraId = lightHandler.getCameraIdList()[0];
                lightHandler.setTorchMode(cameraId, true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Public method off()
     *  This method turn off the phone's flashlight, stop the phone from vibrating and mute sound being generated.
     *  Note: the pause() -> flush() -> stop() -> write() flow of the soundHandler ensures that the sound stops immediately and starts exactly as it did before.
     *  This is necessary to keep other classes from needing to re-instantiate the Driver object whenever the preferred frequency settings are changed.
     */
    public void off(){
        vibrateHandler.cancel();

        if(soundHandler.getPlayState() == AudioTrack.PLAYSTATE_PLAYING){
           soundHandler.pause();
           soundHandler.flush();
           soundHandler.stop();
           generateSound(SettingsSingleton.getInstance().getFrequency(), 1);
           soundHandler.write(soundData, 0, soundData.length);
        }

        if( lightHandler != null){
            try{
                String cameraId = lightHandler.getCameraIdList()[0];
                lightHandler.setTorchMode(cameraId, false);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void generateSound(int frequency, int duration) {
        int sampleRate = 44100;
        soundData = new byte[sampleRate * duration];
        for (int i = 0; i < soundData.length; i++) {
            byte sample = (byte) ( Math.sin(2 * Math.PI * frequency * i / sampleRate) * 255);
            soundData[i] = sample;
        }
    }
}
