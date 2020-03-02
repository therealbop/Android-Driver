package com.karry.side_screens.live_chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karry.app.mainActivity.MainActivity;
import com.karry.dagger.ActivityScoped;
import com.heride.partner.R;
import com.livechatinc.inappchat.ChatWindowConfiguration;
import com.livechatinc.inappchat.ChatWindowView;
import com.livechatinc.inappchat.models.NewMessageModel;


import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;


/**
 * <h1>LiveChatFragment</h1>
 * <p>the is the class for Invite Fragment</p>
 */
@ActivityScoped
public class LiveChatFragment extends DaggerFragment implements LiveChatFragmentContract.LiveChatFragmentView,ChatWindowView.ChatWindowEventsListener{


    private Unbinder unbinder;

    @Inject
    LiveChatFragmentPresenter liveChatFragmentPresenter;
    @BindString(R.string.LIVE_CHAT_LICENCE_NUMBER) String LIVE_CHAT_LICENCE_NUMBER;
    @BindString(R.string.app_name) String appName;
    private ChatWindowView fullScreenChatWindow;
    private ChatWindowConfiguration configuration;
    @Inject
    public LiveChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.livechat_fragment, container, false);

        unbinder = ButterKnife.bind(this,rootView);
        liveChatFragmentPresenter.attachView(this);
        ((MainActivity)getActivity()).setFragmentRefreshListener(this::onResume);
        return rootView;
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        liveChatFragmentPresenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onChatWindowVisibilityChanged(boolean b) {

    }

    @Override
    public void onNewMessage(NewMessageModel newMessageModel, boolean b) {

    }

    @Override
    public void onStartFilePickerActivity(Intent intent, int i) {

    }

    @Override
    public boolean handleUri(Uri uri) {
        return false;
    }
}
