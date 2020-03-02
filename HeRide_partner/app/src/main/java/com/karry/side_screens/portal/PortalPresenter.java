package com.karry.side_screens.portal;


import android.content.Context;

import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferencesHelper;
import com.heride.partner.BuildConfig;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import javax.inject.Inject;

public class PortalPresenter implements PortalContract.PortalPresenter{


    @Inject PortalContract.PortalView portalView;
    @Inject PreferencesHelper preferencesHelper;
    @Inject Context context;

    @Inject
    PortalPresenter() {
    }

    @Override
    public void setActionBar() {
        portalView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        portalView.setTitle();
    }

    @Override
    public void setPortalURL() {
        String portalURL  = BuildConfig.PORTAL_URL.concat(preferencesHelper.getMid().concat("&").concat(preferencesHelper.getLanguage()));
        Utility.printLog("the portal url is :   "+portalURL);
        portalView.loadPortalWeb(portalURL);
    }

    @Override
    public void webPageLoaded(boolean loaded) {
        if(loaded)
            portalView.hideProgress();
        else
            portalView.showProgress();
    }

    @Override
    public void checklanguageLocale() {
        if (preferencesHelper.getLanguageSettings() == null)
            preferencesHelper.setLanguageSettings(new LanguagesList("en","English", 0));
        else
            Utility.changeLanguageConfig(preferencesHelper.getLanguageSettings().getCode(),context);

    }
}
