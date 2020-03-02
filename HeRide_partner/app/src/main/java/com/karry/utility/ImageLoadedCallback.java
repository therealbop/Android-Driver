package com.karry.utility;

import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;

/**
 * <p>Image loading Callback</p>
 * callback class for progress bar for
 * image loading
 * after image load setting progress bar visibility gone
 *
 * @author embed
 */
public class ImageLoadedCallback implements Callback {
    ProgressBar progressBar;

    public ImageLoadedCallback(ProgressBar progBar) {
        progressBar = progBar;
    }

    @Override
    public void onSuccess() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onError(Exception e) {

    }


}