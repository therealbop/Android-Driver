package com.karry.telecall.incommingcalls;


import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Ali on 10/29/2018.
 */
@Module
public interface IncomingCallDaggerModule
{
    @ActivityScoped
    @Binds
    IncomingCallContract.Presenter InComingPresenters(InComingCallPresenter presenter);

    @ActivityScoped
    @Binds
    IncomingCallContract.View InComingView(IncomingCallScreen presenter);
}
