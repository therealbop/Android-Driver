package com.karry.telecall.SlideLayout;

/**
 * Created by moda on 08/11/17.
 */

public interface ISlideChangeListener {

    void onSlideStart(SlideLayout slider);

    void onSlideChanged(SlideLayout slider, float percentage);

    void onSlideFinished(SlideLayout slider, boolean done);

}