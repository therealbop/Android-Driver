package com.karry.app.splash;


import com.karry.dagger.ActivityScoped;
import com.karry.network.NetworkErrorDialog;
import com.karry.utility.AppTypeFace;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashScreenUtilModule {

    @Provides
    @ActivityScoped
    NetworkErrorDialog networkErrorDialog(AppTypeFace appTypeFace, SplashScreenActivity activity){
        return new NetworkErrorDialog(activity,appTypeFace);
    }
}
