package com.karry.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by embed on 29/12/16.
 */
public class Slider extends SeekBar {
    private Drawable mThumb;
    private SliderProgressCallback sliderProgressCallback;

    public Slider(Context context) {
        super(context);
    }

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        mThumb = thumb;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (event.getX() >= mThumb.getBounds().left
                    && event.getX() <= mThumb.getBounds().right
                    && event.getY() <= mThumb.getBounds().bottom
                    && event.getY() >= mThumb.getBounds().top) {

                super.onTouchEvent(event);
            } else {
                return false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            int i = getProgress();
            if (sliderProgressCallback != null) {
                if (i > 65) {
                    setProgress(100);
                    sliderProgressCallback.onSliderProgressChanged(100);
                } else {
                    setProgress(0);
                    sliderProgressCallback.onSliderProgressChanged(0);
                }
                if(i==100)
                    setProgress(0);

            }
            return false;
        } else {
            super.onTouchEvent(event);
        }
        return true;
    }

    public void setSliderProgressCallback(SliderProgressCallback sliderProgressCallback) {
        this.sliderProgressCallback = sliderProgressCallback;
    }

    public interface SliderProgressCallback {
        void onSliderProgressChanged(int progress);
    }
}
