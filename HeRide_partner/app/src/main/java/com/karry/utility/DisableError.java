package com.karry.utility;

import android.view.View;

/**
 * Created by ads on 11/05/17.
 * <h2>DisableError</h2>
 * <p>this interface use for disable error message showing on text input layouts</p>
 */

public interface DisableError {
    /**
     * <h2>setDisableError</h2>
     * <p>this method is used to give callback to the view
     * to disabling the error that is currently showing</p>
     *
     * @param view which is on focused and user is working on it
     */
    void setDisableError(View view);

    void enableSighUp();

    void disableSighUp();

}
