package com.karry.app.splash;


import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SplashScreenDaggerModule {

    @ActivityScoped
    @Binds
    abstract Activity activity(SplashScreenActivity splashScreenActivity);

    @ActivityScoped
    @Binds
    abstract SplashScreenContract.SplashScreenView getView(SplashScreenActivity splashScreenActivity);

    @ActivityScoped
    @Binds
    abstract SplashScreenContract.SplashScreenPresenter getPresenetr(SplashScreenPresenter splashScreenPresenter);
}
