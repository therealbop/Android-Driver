package com.karry.app.meterBooking;


import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DriverMeterDaggerModule {

    @ActivityScoped
    @Binds
    abstract Activity activity(DriverMeterActivity driverMeterActivity);

    @ActivityScoped
    @Binds
    abstract DriverMeterContract.DriverMeterPresenter getPresenter(DriverMeterPresenter driverMeterPresenter);

    @ActivityScoped
    @Binds
    abstract DriverMeterContract.DriverMeterView  getView(DriverMeterActivity driverMeterActivity);


}
