package com.karry.side_screens.wallet.wallet_transaction_activity;


import android.content.Context;

import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.heride.partner.R;
import com.karry.network.NetworkStateHolder;
import com.karry.network.NetworkService;
import com.karry.side_screens.wallet.wallet_transaction_activity.model.TransctionsItem;
import com.karry.side_screens.wallet.wallet_transaction_activity.model.WalletTransDataPojo;
import com.karry.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class WalletTransactionActivityPresenter implements WalletTransactionContract.WalletTransactionPresenter
{
    private final String TAG = "WalletTransProvider";

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject Context mContext;
    @Inject WalletTransactionContract.WalletTrasactionView trasactionView;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject Gson gson;

    @Inject  CompositeDisposable compositeDisposable;

    @Inject
    WalletTransactionActivityPresenter()
    {


    }

    /**
     * <h2>initLoadTransactions</h2>
     * <p> method to init the getTransactionsHistory() api call if network connectivity is there </p>
     * @param : true if is from load more option
     * @param isFromOnRefresh: true if it is to refresh
     */
    public void initLoadTransactions(int txn_type,boolean isFromOnRefresh,int pageIndex)
    {
//        if( networkStateHolder.isConnected())
//        {
        if(!isFromOnRefresh)
        {
//                trasactionView.showProgressDialog(mContext.getString(R.string.pleaseWait));
            getTransactionHistory(txn_type,pageIndex);
        }

//        }
//        else
//        {
//            trasactionView.noInternetAlert();
//        }
    }


    @Override
    public void showToastNotifier(String msg, int duration)
    {
        trasactionView.showToast(msg, duration);
    }


    /**
     * <h>get Wallet History</h>
     * <p>this method is using to get the Wallet history data</p>
     */
    private void getTransactionHistory(int txn_type,int pageIndex)
    {

        trasactionView.showProgressDialog(mContext.getString(R.string.pleaseWait));
        Observable<Response<ResponseBody>> request = networkService.getWalletTransactionIndex(
                preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage(),
                String.valueOf(txn_type),String.valueOf(pageIndex));

        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value)
                    {
                        try {
                            Utility.printLog(TAG + " getWalletTrans onNext: " + value.code());

                            switch (value.code()) {
                                case 200:
                                    JSONObject jsonObject=new JSONObject(value.body().string());

                                    Utility.printLog(TAG + " getWalletTrans onNext: " + value.body().string());
                                    handleResponse(jsonObject.toString(),txn_type);
                                    break;
                                default:
                                    break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog(TAG+ "getWalletTrans error: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        trasactionView.hideProgressDialog();

                    }
                });
    }


    /**
     * <h>Response Handler</h>
     * <p>this method is using to  handle the Server Response</p>
     * @param response server response
     * @param txn_type
     */
    private void handleResponse(String response, int txn_type)
    {

        try {

            WalletTransDataPojo walletTransactionsModel = gson.fromJson(response, WalletTransDataPojo.class);
            if (txn_type==3) {
                trasactionView.setAllTransactionsAL(walletTransactionsModel.getData().getTransctions());
            }
            else if (txn_type==2) {
                trasactionView.setCreditTransactionsAL(walletTransactionsModel.getData().getTransctions());
            }
            else if (txn_type==1) {
                trasactionView.setDebitTransactionsAL(walletTransactionsModel.getData().getTransctions());
            }
        }catch (ArithmeticException e){

        }
    }

}