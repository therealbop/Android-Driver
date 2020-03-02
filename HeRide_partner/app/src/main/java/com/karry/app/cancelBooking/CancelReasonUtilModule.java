package com.karry.app.cancelBooking;


import com.karry.dagger.ActivityScoped;
import com.karry.network.NetworkErrorDialog;
import com.karry.utility.AppTypeFace;

import dagger.Module;
import dagger.Provides;

@Module
public class CancelReasonUtilModule {

    @Provides
    @ActivityScoped
    NetworkErrorDialog networkErrorDialog(AppTypeFace appTypeFace, CancelReasonActivity activity){
        return new NetworkErrorDialog(activity,appTypeFace);
    }
}
