package com.karry.side_screens.invite;

import com.karry.data.source.local.PreferenceHelperDataSource;

import javax.inject.Inject;

/**
 * <h1>InviteFragmentPresenter</h1>
 * <p>class for the BookingPopUpPresenter Implementation of the InviteFragment</p>
 */
public class InviteFragmentPresenter implements InviteFragmentContract.InviteFragPresenter {

    private InviteFragmentContract.InviteFragmentView inviteFragmentView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    InviteFragmentPresenter() {
    }

    @Override
    public void attachView(Object view) {
        inviteFragmentView = (InviteFragmentContract.InviteFragmentView) view;
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
