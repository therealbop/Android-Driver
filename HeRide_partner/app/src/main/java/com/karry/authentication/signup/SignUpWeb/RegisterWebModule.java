package com.karry.authentication.signup.SignUpWeb;

import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by embed on 17/1/18.
 */
@Module
public abstract class RegisterWebModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(RegisterWebActivity registerWebActivity);

    @ActivityScoped
    @Binds
    abstract RegisterWebContract.RegisterWebPresenter getPresenter(RegisterWebPresenter registerWebPres);

    @ActivityScoped
    @Binds
    abstract RegisterWebContract.RegisterWebView getView(RegisterWebActivity registerWebPres);
}
