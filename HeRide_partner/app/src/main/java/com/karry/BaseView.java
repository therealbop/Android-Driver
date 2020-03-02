package com.karry;

/**
 * Created by DELL on 28-12-2017.
 */

public interface BaseView<T> {

    void networkError(String message);

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
