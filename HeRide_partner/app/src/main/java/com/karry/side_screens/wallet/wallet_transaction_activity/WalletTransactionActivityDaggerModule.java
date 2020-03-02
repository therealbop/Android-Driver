package com.karry.side_screens.wallet.wallet_transaction_activity;


import com.karry.dagger.ActivityScoped;
import com.karry.dagger.FragmentScoped;


import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class WalletTransactionActivityDaggerModule
{
    @Binds
    @ActivityScoped
    abstract WalletTransactionContract.WalletTrasactionView provideWalletTransactionView(WalletTransActivity transActivity);

    @Binds
    @ActivityScoped
    abstract WalletTransactionContract.WalletTransactionPresenter provideWalletTransPresnter(WalletTransactionActivityPresenter transactionActivityPresenter);

    @ContributesAndroidInjector
    @FragmentScoped
    abstract WalletTransactionsFragment provideWalletListFragment();

}