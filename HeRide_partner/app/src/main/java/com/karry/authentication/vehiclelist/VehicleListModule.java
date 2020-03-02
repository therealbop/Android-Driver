package com.karry.authentication.vehiclelist;

import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class VehicleListModule {
    @ActivityScoped
    @Binds
    abstract Activity getActivity(VehicleListActivity activity);

    @Binds
    @ActivityScoped
    abstract VehicleListContract.VehicleListView getView(VehicleListActivity view);


    @Binds
    @ActivityScoped
    abstract VehicleListContract.VehicleListPresenter getPresenter(VehicleListPresenter presenter);

}


