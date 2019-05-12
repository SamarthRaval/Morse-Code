package com.example.pauld.morsecode;

/**
 * UserInfoListener.java
 *
 *      Since UserInfo requires onEvent listeners and is meant to be fully modular this class was created.
 *      It simply provides other developers with a callback function to the array of lessons completed of the user.
 *      To use it the developer must instantiate a new interface when calling UserInfo.getLessonsCompleted and override the onArrayFetched function.
 *
 *      @author George Faraj B00638341
 */
public interface UserInfoListener {
    void onArrayFetched(boolean[] lessonsCompleted);
}
