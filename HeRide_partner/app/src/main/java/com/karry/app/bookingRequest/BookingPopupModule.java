package com.karry.app.bookingRequest;

import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 26-09-2017.
 */

@Module
public abstract class BookingPopupModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(BookingPopUpActivity activity);

    @Binds
    @ActivityScoped
    abstract   BookingPopUpContract.BookingPoUpView getView(BookingPopUpActivity view);

    @Binds
    @ActivityScoped
    abstract  BookingPopUpContract.BookingPopUpPresenter getPresenter(BookingPopUpPresenter presenter);


}