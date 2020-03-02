package com.karry.side_screens.bankDetails;

import org.json.JSONObject;

/**
 * Created by murashid on 29-Aug-17.
 */

public class BankBottomSheetPresenter implements BankBottomSheetModel.BankBottomSheetModelImplem {

    private BankBottomSheetPresenterImplem bankBottomSheetPresenterImplem;
    private BankBottomSheetModel bankBottomSheetModel;
    BankBottomSheetPresenter(BankBottomSheetPresenterImplem bankBottomSheetPresenterImplem)
    {
        this.bankBottomSheetPresenterImplem = bankBottomSheetPresenterImplem;
        this.bankBottomSheetModel = new BankBottomSheetModel(this);
    }

    void makeDefault(String token, JSONObject jsonObject)
    {
        bankBottomSheetPresenterImplem.startProgressBar();
        bankBottomSheetModel.setDefaultAccount(token,jsonObject);
    }

    void deleteAccount(String token, JSONObject jsonObject)
    {
        bankBottomSheetPresenterImplem.startProgressBar();
        bankBottomSheetModel.deleteBankAccount(token,jsonObject);
    }


    @Override
    public void onFailure(String failureMsg) {
        bankBottomSheetPresenterImplem.stopProgressBar();
        bankBottomSheetPresenterImplem.onFailure(failureMsg);
    }

    @Override
    public void onFailure() {
        bankBottomSheetPresenterImplem.stopProgressBar();
        bankBottomSheetPresenterImplem.onFailure();
    }

    @Override
    public void onSuccess(String msg) {
        bankBottomSheetPresenterImplem.stopProgressBar();
        bankBottomSheetPresenterImplem.onSuccess(msg);
    }

    interface BankBottomSheetPresenterImplem
    {
        void startProgressBar();
        void stopProgressBar();
        void onFailure(String msg);
        void onFailure();
        void onSuccess(String msg);
    }
}
