package com.karry.side_screens.wallet.add_card;


import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AddCardActivityModule
{

    @ActivityScoped
    @Binds
    abstract AddCardActivityContract.AddCardview provideAddCardview(AddCardActivity activity);

    @ActivityScoped
    @Binds
    abstract AddCardActivityContract.AddCardPresenter provideAddCardPresenter(AddCardActivityPresenter presenter);
}
