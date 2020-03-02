package com.karry.telecall.SlideLayout;

/**
 * Created by moda on 08/11/17.
 */

import android.graphics.Rect;

public interface ISlidingData {

    /**
     * Get touch down X pos
     */
    int getStartX();

    /**
     * Get touch down Y pos
     */
    int getStartY();

    /**
     * Get child view's rectangle when sliding was started
     */
    Rect getChildStartRect();

    /**
     * Get SlideLayout size
     */
    Dimen getParentDimen();

    /**
     * Notify SlideLayout's listeners that the progress was changed
     */
    void publishOnSlideChanged(float percentage);
}