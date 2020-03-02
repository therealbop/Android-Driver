package com.karry.authentication.signup.SignUpPersonal;

import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by embed on 17/1/18.
 */

@Module
public abstract class SignupPersonalDaggerModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(SignupPersonalActvity signupPersonalActvity);

    @ActivityScoped
    @Binds
    abstract SignUpPersonalContract.SignUpPersonalPresenter getPresenter(SignupPersonalPresenter signupPersonalPres);

    @ActivityScoped
    @Binds
    abstract SignUpPersonalContract.SignUpPersonalView getView(SignupPersonalActvity signupPersonalActvity);


}
