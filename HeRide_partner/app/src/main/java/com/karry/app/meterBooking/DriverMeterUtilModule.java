package com.karry.app.meterBooking;

import com.karry.dagger.ActivityScoped;
import com.karry.manager.location.LocationManager;
import com.karry.network.NetworkErrorDialog;
import com.karry.utility.AppTypeFace;

import dagger.Module;
import dagger.Provides;


@Module
public class DriverMeterUtilModule {

    @Provides
    @ActivityScoped
    LocationManager provideLocationManager(DriverMeterActivity activity){return new LocationManager(activity);}

    @Provides
    @ActivityScoped
    NetworkErrorDialog networkErrorDialog(AppTypeFace appTypeFace, DriverMeterActivity activity){
        return new NetworkErrorDialog(activity,appTypeFace);
    }
}
