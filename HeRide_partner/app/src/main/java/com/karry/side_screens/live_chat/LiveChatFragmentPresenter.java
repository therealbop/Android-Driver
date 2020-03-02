package com.karry.side_screens.live_chat;

import com.karry.data.source.local.PreferenceHelperDataSource;

import javax.inject.Inject;

/**
 * <h1>LiveChatFragmentPresenter</h1>
 * <p>class for the BookingPopUpPresenter Implementation of the LiveChatFragment</p>
 */
public class LiveChatFragmentPresenter implements LiveChatFragmentContract.LiveChatPresenter {

    private LiveChatFragmentContract.LiveChatFragmentView inviteFragmentView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    LiveChatFragmentPresenter() {
    }

    @Override
    public void attachView(Object view) {
        inviteFragmentView = (LiveChatFragmentContract.LiveChatFragmentView) view;
    }

    @Override
    public void detachView() {
        inviteFragmentView = null;
    }

    @Override
    public String  getInviteCode()
    {
        return preferenceHelperDataSource.getRefereralCode();
    }
}
