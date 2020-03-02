package com.karry.authentication.vehicleTypeList;

import android.content.Context;

import com.karry.dagger.ActivityScoped;

import dagger.Module;
import dagger.Provides;


@Module
public class VehicleTypeListAdapterModule {
    @Provides
    @ActivityScoped
    VehicleTypeListRCA provideVechicleListRVA(Context context)
    {
        return new VehicleTypeListRCA(context);
    }
}
