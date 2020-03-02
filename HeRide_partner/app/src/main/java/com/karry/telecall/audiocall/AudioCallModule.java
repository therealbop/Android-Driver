package com.karry.telecall.audiocall;

import com.karry.dagger.ActivityScoped;
import com.karry.telecall.callservice.AudioCallService;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class AudioCallModule {

    @Binds
    @ActivityScoped
    abstract AudioCallContract.View provideAudioCallService(AudioCallService audioCallService);

    @Binds
    @ActivityScoped
    abstract AudioCallContract.Presenter provideAudioCallPresenter(AudioCallPresenter audioCallPresenter);
}