package com.karry.bookingFlow;

import com.karry.dagger.ActivityScoped;
import com.karry.bookingFlow.model.LocationModel;
import com.karry.manager.location.LocationManager;
import com.karry.network.NetworkErrorDialog;
import com.karry.utility.AppTypeFace;

import dagger.Module;
import dagger.Provides;


@Module
public class RideBookingUtilityModule
{
    @Provides
    @ActivityScoped
    LocationManager provideLocationManager(RideBookingActivity activity){return new LocationManager(activity);}

    @Provides
    @ActivityScoped
    LocationModel provideHomeActivityModel() { return new LocationModel();}

    @Provides
    @ActivityScoped
    NetworkErrorDialog networkErrorDialog(AppTypeFace appTypeFace, RideBookingActivity activity){
        return new NetworkErrorDialog(activity,appTypeFace);
    }
}
