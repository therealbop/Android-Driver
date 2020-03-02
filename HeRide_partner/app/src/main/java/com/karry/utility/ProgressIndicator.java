package com.karry.utility;

/**
 * Created by ads on 13/05/17.
 */

public interface ProgressIndicator {
    /**
     * <h2>showProgress</h2>
     * <p>use to show progress bar in activity while loading
     * to the user</p>
     */
    void showProgress();

    /**
     * <h2>hideProgress</h2>
     * <p>use to hide progress bar in activity while loading
     * to the user</p>
     */
    void hideProgress();
}
