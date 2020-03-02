package com.karry.telecall.SlideLayout;

/**
 * Created by moda on 08/11/17.
 */

public interface ISlideListener {

    /**
     * Called when user have done or interrupted the sliding
     * @param done - true if the sliding is done
     */
    void onSlideDone(SlideLayout slider, boolean done);

}