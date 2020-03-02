package com.karry.app.cancelBooking;


import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CancelReasonModule {

    @ActivityScoped
    @Binds
    abstract Activity activity(CancelReasonActivity cancelReasonActivity);

    @ActivityScoped
    @Binds
    abstract CancelReasonContract.CancelReasonPresenter getPresenter(CancelReasonPresenter cancelReasonPresenter);

    @ActivityScoped
    @Binds
    abstract CancelReasonContract.CancelReasonView getView(CancelReasonActivity cancelReasonActivity);

}
