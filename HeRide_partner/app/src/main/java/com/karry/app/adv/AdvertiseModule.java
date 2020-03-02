package com.karry.app.adv;

import com.karry.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>AdvertiseModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 05-03-2018.
 */
@Module
public abstract class AdvertiseModule
{
    @Binds
    @ActivityScoped
    abstract AdvertiseContract.View provideAdView(AdvertiseActivity advertiseActivity);

    @Binds
    @ActivityScoped
    abstract AdvertiseContract.Presenter provideAdPresenter(AdvertisePresenter advertisePresenter);
}
