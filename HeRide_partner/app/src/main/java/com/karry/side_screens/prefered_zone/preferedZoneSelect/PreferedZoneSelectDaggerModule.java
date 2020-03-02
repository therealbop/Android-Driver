package com.karry.side_screens.prefered_zone.preferedZoneSelect;


import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PreferedZoneSelectDaggerModule {


    @ActivityScoped
    @Binds
    abstract PreferedZoneSelectContract.PreferedZoneSelectView getView(PreferedZoneSelectActivity preferedZoneSelectActivity);

    @ActivityScoped
    @Binds
    abstract PreferedZoneSelectContract.PreferedZoneSelectPresenter getPresenter(PreferedZoneSelectPresenter preferedZoneSelectPresenter);
}
