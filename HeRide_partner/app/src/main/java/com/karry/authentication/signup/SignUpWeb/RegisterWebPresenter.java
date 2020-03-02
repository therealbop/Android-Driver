package com.karry.authentication.signup.SignUpWeb;

import android.content.Context;

import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.utility.Utility;

import javax.inject.Inject;

/**
 * <h1>RegisterWebPresenter</h1>
 * <p>the Implementation for the Web Activity</p>
 */
public class RegisterWebPresenter implements RegisterWebContract.RegisterWebPresenter{

    @Inject RegisterWebContract.RegisterWebView registerWebView;
    @Inject PreferenceHelperDataSource  preferenceHelperDataSource;
    @Inject Context context;
    @Inject RegisterWebPresenter(){
    }

    @Override
    public void setActionBar() {
        Utility.printLog("language support is Register : "+preferenceHelperDataSource.getLanguageSettings().getCode());
        registerWebView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        registerWebView.setTitle();
    }

    @Override
    public void webPageLoaded(boolean loaded) {
        if(loaded) {
            registerWebView.hideProgress();
            registerWebView.enableActivityTxt();
        }
        else
            registerWebView.showProgress();

    }

    @Override
    public void success() {
        registerWebView.startNextActivity();
    }

    @Override
    public void checklanguageLocale() {
        if (preferenceHelperDataSource.getLanguageSettings() == null)
            preferenceHelperDataSource.setLanguageSettings(new LanguagesList("en","English", 0));
        else
            Utility.changeLanguageConfig(preferenceHelperDataSource.getLanguageSettings().getCode(),context);

    }
}
