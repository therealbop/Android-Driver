package com.karry.twilio_call;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ClientActivityDaggerModule
{
    @ActivityScoped
    @Binds
    abstract ClientActivityContract.ClientPresenter provideClientPresenter(ClientActivityPresenter presenter);

    @ActivityScoped
    @Binds
    abstract ClientActivityContract.ClientView provideClientView(ClientActivity activity);

}
