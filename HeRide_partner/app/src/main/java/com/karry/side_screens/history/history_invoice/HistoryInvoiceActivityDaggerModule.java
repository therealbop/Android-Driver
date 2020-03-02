package com.karry.side_screens.history.history_invoice;


import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class HistoryInvoiceActivityDaggerModule
{
    @ActivityScoped
    @Binds
    abstract HistoryInvoiceContract.HistoryInvoiceView provideHistoryinvoice(HistoryInvoiceActivity activity);

    @ActivityScoped
    @Binds
    abstract HistoryInvoiceContract.HistoryInvoicePresenter provideHistoryinvoicePresenter(HistoryInvoiceActivityPresenter presenter);
}
