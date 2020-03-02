package com.karry.app.meterBooking.address;


import android.app.Activity;

import com.karry.adapter.PlaceAutoCompleteAdapter;
import com.karry.dagger.ActivityScoped;
import com.karry.network.NetworkErrorDialog;
import com.karry.utility.AppTypeFace;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaceAutoCompleteAdapterModule {

    @Provides
    @ActivityScoped
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter(Activity context, AddressSelectContract.Presenter presenter)
    {
        return new PlaceAutoCompleteAdapter(context,presenter);
    }

    @Provides
    @ActivityScoped
    NetworkErrorDialog networkErrorDialog(AppTypeFace appTypeFace, AddressSelectionActivity activity){
        return new NetworkErrorDialog(activity,appTypeFace);
    }
}
