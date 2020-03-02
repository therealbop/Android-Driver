package com.karry;

/**
 * <h1>BasePresenter</h1>
 * This class is used to provide the base presenter for main activity
 * @author  3Embed
 * @since on 12-12-2017.
 */

public interface BasePresenter<T> {

    /**
     * <h2>attachView</h2>
     * Binds presenter with a view when resumed. The Presenter will perform initialization here.
     * @param view the view associated with this presenter
     */
    void attachView(T view);

    /**
     * <h2>detachView</h2>
     * Drops the reference to the view when destroyed
     */
    void detachView();
}