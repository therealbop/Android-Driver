package com.karry.side_screens.prefered_zone;


import android.content.Context;

import com.karry.adapter.PreferenceZoneListAdapter;
import com.karry.dagger.ActivityScoped;
import com.karry.utility.AppTypeFace;

import dagger.Module;
import dagger.Provides;


@Module
public class PreferedZoneModule {

    @Provides
    @ActivityScoped
    PreferenceZoneListAdapter preferenceZoneListAdapter(Context context, AppTypeFace appTypeFace)
    {
        return new PreferenceZoneListAdapter(context,appTypeFace);
    }
}
