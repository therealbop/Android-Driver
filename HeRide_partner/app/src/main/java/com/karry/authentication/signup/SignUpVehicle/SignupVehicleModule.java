package com.karry.authentication.signup.SignUpVehicle;

import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by embed on 19/1/18.
 */

@Module
public abstract class SignupVehicleModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(SignupVehicleActivity signupVehicleActivity);

    @ActivityScoped
    @Binds
    abstract SignupVehicleContract.SignupVehicleView getView(SignupVehicleActivity signupVehicleActivity);

    @ActivityScoped
    @Binds
    abstract SignupVehicleContract.SignupVehiclePresenter getPresenter(SignupVehiclePresenter signupVehiclePresenter);
}
