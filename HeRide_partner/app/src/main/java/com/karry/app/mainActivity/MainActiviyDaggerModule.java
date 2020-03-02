package com.karry.app.mainActivity;

import android.app.Activity;
import androidx.fragment.app.FragmentManager;

import com.karry.side_screens.history.HistoryContract;
import com.karry.side_screens.history.HistoryFragment;
import com.karry.side_screens.history.HistoryPresenter;
import com.karry.side_screens.bankDetails.BankDetailsFragContract;
import com.karry.side_screens.bankDetails.BankDetailsFragPresenter;
import com.karry.side_screens.bankDetails.BankDetailsFragment;
import com.karry.dagger.ActivityScoped;
import com.karry.dagger.FragmentScoped;
import com.karry.side_screens.home_fragment.HomeFragment;
import com.karry.side_screens.home_fragment.HomeFragmentContract;
import com.karry.side_screens.home_fragment.HomeFragmentPresnter;
import com.karry.side_screens.invite.InviteFragment;
import com.karry.side_screens.invite.InviteFragmentContract;
import com.karry.side_screens.invite.InviteFragmentPresenter;
import com.karry.side_screens.live_chat.LiveChatFragment;
import com.karry.side_screens.live_chat.LiveChatFragmentContract;
import com.karry.side_screens.live_chat.LiveChatFragmentPresenter;
import com.karry.side_screens.profile.MyProfileContract;
import com.karry.side_screens.profile.MyProfileFrag;
import com.karry.side_screens.profile.MyProfilePresenter;
import com.karry.side_screens.support.SupportFragment;
import com.karry.side_screens.support.SupportFragmentContract;
import com.karry.side_screens.support.SupportFragmentPresenter;
import com.karry.side_screens.wallet.WalletActivityContract;
import com.karry.side_screens.wallet.WalletActivityPresenter;
import com.karry.side_screens.wallet.WalletFragment;


import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by embed on 5/2/18.
 */

@Module (includes = MainActivityUtilModule.class)
public abstract class MainActiviyDaggerModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(MainActivity mainActivity);

    @ActivityScoped
    @Binds
    abstract MainActivityContract.MainActivityPresenter getPresenter(MainActivityPresenter mainActivityPresenter);

    @ActivityScoped
    @Binds
    abstract MainActivityContract.MainActivityView getView(MainActivity mainActivity);


    /**********************************************************************************************/
    @Provides
    @ActivityScoped
    static FragmentManager provideFragmentManager(MainActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyProfileFrag provideProfileFrag();

    @Binds
    @ActivityScoped
    abstract MyProfileContract.Presenter provideMyProfilePresenter(MyProfilePresenter presenter);

    @Binds
    @ActivityScoped
    abstract MyProfileContract.View providePresenter(MyProfileFrag profileFrag);

    /**********************************************************************************************/
    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomeFragment getHomeFragment();

    @Binds
    @ActivityScoped
    abstract HomeFragmentContract.HomeFragmentPresenter getHomePresenter(HomeFragmentPresnter presnter);

    /**********************************************************************************************/
    @FragmentScoped
    @ContributesAndroidInjector
    abstract InviteFragment inviteFragment();

    @Binds
    @ActivityScoped
    abstract InviteFragmentContract.InviteFragPresenter getInvitePresenter(InviteFragmentPresenter inviteFragmentPresenter);

    /**********************************************************************************************/
    @FragmentScoped
    @ContributesAndroidInjector
    abstract SupportFragment supportFragment();

    @Binds
    @ActivityScoped
    abstract SupportFragmentContract.SupportFragPresenter getSupportPresenter(SupportFragmentPresenter supportFragmentPresenter);

    /**********************************************************************************************/

    @FragmentScoped
    @ContributesAndroidInjector
    abstract BankDetailsFragment bankListFragment();

    @Binds
    @ActivityScoped
    abstract BankDetailsFragContract.BankDetailsFragPresenter getBankDetailsPresenter(BankDetailsFragPresenter bankDetailsFragPresenter);

    /**********************************************************************************************/

    @FragmentScoped
    @ContributesAndroidInjector
    abstract WalletFragment walletFragment();

    @Binds
    @ActivityScoped
    abstract WalletActivityContract.WalletPresenter getWalletFragPresenter(WalletActivityPresenter walletFragmentPresenter);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HistoryFragment historyFragment();

    @ActivityScoped
    @Binds
    abstract HistoryContract.HistoryPresenter presenter(HistoryPresenter historyPresenter);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LiveChatFragment liveChatFragment();

    @Binds
    @ActivityScoped
    abstract LiveChatFragmentContract.LiveChatPresenter getLiveChatPresenter(LiveChatFragmentPresenter liveChatFragmentPresenter);


}
