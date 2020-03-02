package com.karry.side_screens.portal;



import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PortalDaggerModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(PortalActivity portalActivity);

    @ActivityScoped
    @Binds
    abstract PortalContract.PortalView getView(PortalActivity portalActivity);

    @ActivityScoped
    @Binds
    abstract PortalContract.PortalPresenter getPresenter(PortalPresenter portalPresenter);

}
