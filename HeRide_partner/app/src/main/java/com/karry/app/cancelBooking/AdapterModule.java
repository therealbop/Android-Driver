package com.karry.app.cancelBooking;


import android.content.Context;

import com.karry.adapter.BookingCancelRVA;
import com.karry.dagger.ActivityScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class AdapterModule {

    @Provides
    @ActivityScoped
    BookingCancelRVA imageUploadRVA(Context context)
    {
        return new BookingCancelRVA(context);
    }
}
