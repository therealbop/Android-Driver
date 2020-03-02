package com.karry.authentication.vehiclelist;

import android.content.Context;

import com.karry.dagger.ActivityScoped;

import dagger.Module;
import dagger.Provides;


@Module
public class AdapterModule
{
    @Provides
    @ActivityScoped
    VehicleListRCA provideVechicleListRVA(Context context)
    {
        return new VehicleListRCA(context);
    }
}
