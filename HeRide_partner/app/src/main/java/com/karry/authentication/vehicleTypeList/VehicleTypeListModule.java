package com.karry.authentication.vehicleTypeList;


import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class VehicleTypeListModule {
    @ActivityScoped
    @Binds
    abstract Activity getActivity(VehicleTypeListActivity vehicleTypeListActivity);

    @ActivityScoped
    @Binds
    abstract VehicleTypeListContract.VehicleTypeListView  getVeiw(VehicleTypeListActivity vehicleTypeListActivity);

    @ActivityScoped
    @Binds
    abstract VehicleTypeListContract.VehicleTypeListPresenter getPresenter(VehicleTypeListPresenter vehicleTypeListPresenter);
}
