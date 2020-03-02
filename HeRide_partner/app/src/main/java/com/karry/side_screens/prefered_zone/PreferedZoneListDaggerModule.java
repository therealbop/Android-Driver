package com.karry.side_screens.prefered_zone;


import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PreferedZoneListDaggerModule {



    @ActivityScoped
    @Binds
    abstract PreferedZoneListContract.PreferedZoneView getView(PreferedZoneListActivity preferedZoneListActivity);

    @ActivityScoped
    @Binds
    abstract PreferedZoneListContract.PreferedZonePresenter getPresenter(PreferedZoneListPresenter preferedZoneListPresenter);
}
