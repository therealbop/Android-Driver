package com.karry.side_screens.wallet.changeCard;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ChangeCardDaggerModule {


    @Binds
    @ActivityScoped
    abstract ChangeCardContract.View provideView(ChangeCardActivity changeCardActivity);

    @Binds
    @ActivityScoped
    abstract ChangeCardContract.Presenter providePresenter(ChangeCardPresenter changeCardPresenter);
}
