package com.karry.side_screens.home_fragment.invoice;

import android.content.Context;

import com.karry.dagger.ActivityScoped;
import com.karry.network.NetworkErrorDialog;
import com.karry.utility.AppTypeFace;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;



@Module
public class InvoiceDialogModule {

    @Provides
    @ActivityScoped
    InvoiceDialogHelper invoiceDialog()
    {
        return new InvoiceDialogHelper();
    }

    @Provides
    @ActivityScoped
    @Named("SpecialChargeRVA")
    SpecialChargeRVA specialChargeRVA(Context context)
    {
        return new SpecialChargeRVA(context);
    }

    @Provides
    @ActivityScoped
    NetworkErrorDialog networkErrorDialog(AppTypeFace appTypeFace, InvoiceActivity activity){
        return new NetworkErrorDialog(activity,appTypeFace);
    }
}
