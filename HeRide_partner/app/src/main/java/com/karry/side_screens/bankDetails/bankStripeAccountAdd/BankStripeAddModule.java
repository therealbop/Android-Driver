package com.karry.side_screens.bankDetails.bankStripeAccountAdd;

import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class BankStripeAddModule {

    @ActivityScoped
    @Binds
    abstract Activity activity(BankStripeAddActivity bankStripeAddActivity);

    @ActivityScoped
    @Binds
    abstract BankStripeAddContract.BankStripeAddPresenter getPresenter(BankStripeAddPresenter bankStripeAddPresenter);

    @ActivityScoped
    @Binds
    abstract BankStripeAddContract.BankStripeAddView getView(BankStripeAddActivity bankStripeAddActivity);
}
