package com.karry.bookingFlow;


import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RideBookingDaggerModule
{
    @ActivityScoped
    @Binds
    abstract RideBookingContract.RideBookingView getView(RideBookingActivity activity);

    @ActivityScoped
    @Binds
    abstract RideBookingContract.RideBookingPresenter getPresenter(RideBookingPresenter presenter);


}
