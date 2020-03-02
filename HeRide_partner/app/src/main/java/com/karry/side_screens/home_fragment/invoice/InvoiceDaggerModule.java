package com.karry.side_screens.home_fragment.invoice;


import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class InvoiceDaggerModule {

    @ActivityScoped
    @Binds
    abstract Activity activity(InvoiceActivity invoiceActivity);

    @ActivityScoped
    @Binds
    abstract InvoiceContract.InvoicePresenter getPresenter(InvoicePresenter invoicePresenter);

    @ActivityScoped
    @Binds
    abstract InvoiceContract.InvoiceView getView(InvoiceActivity invoiceActivity);

}
