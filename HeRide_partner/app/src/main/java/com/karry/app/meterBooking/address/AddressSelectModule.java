package com.karry.app.meterBooking.address;

import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>AddressSelectModule</h1>
 * This class is used to provide classed needed for the screen to dagger
 *@author 3Embed
 * @since on 20-01-2018.
 */
@Module
public abstract class AddressSelectModule
{
    @ActivityScoped
    @Binds
    abstract Activity  activity(AddressSelectionActivity addressSelectionActivity);

    @ActivityScoped
    @Binds
    abstract AddressSelectContract.Presenter providePresenter(AddressSelectPresenter addressSelectPresenter);

    @ActivityScoped
    @Binds
    abstract AddressSelectContract.View provideView(AddressSelectionActivity selectionActivity);
}
