package com.karry.side_screens.wallet;



import android.content.Context;

import com.karry.data.source.local.PreferenceHelperDataSource;
import com.heride.partner.R;
import com.karry.network.NetworkStateHolder;
import com.karry.network.NetworkService;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class WalletActivityPresenter implements WalletActivityContract.WalletPresenter
{
    private final String TAG = "WalletDetailsProvider";
    private WalletActivityContract.WalletView walletView;

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject Context mContext;


    @Inject
    WalletActivityPresenter()
    {

    }

    @Override
    public String getCurrencySymbol()
    {
        return preferenceHelperDataSource.getCurrencySymbol();
    }


    @Override
    public void getWalletLimits()
    {
//        if (networkStateHolder.isConnected()) {
            walletView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getWalletLimits(
                    preferenceHelperDataSource.getSessionToken(),
                    preferenceHelperDataSource.getLanguage());

            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            Utility.printLog(TAG + " getWalletLimits onNext: " + value.code());
                            switch (value.code()) {
                                case 200:

                                    String responseString = null;
                                    try {
                                        responseString = value.body().string();
                                        handleresponse(responseString);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Utility.printLog(TAG + " getWalletLimits onNext: "+responseString);

                                    break;
                                case 410:
                                    break;

                                default:
                                    walletView.walletDetailsApiErrorViewNotifier(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Utility.printLog(TAG + "getWalletLimits error: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            walletView.hideProgressDialog();
                        }
                    });
    }


    private void handleresponse(String responseString) throws JSONException {

        Utility.printLog(TAG + "getWalletLimits error: " + responseString);
        JSONObject profileObject = new JSONObject(responseString);
        JSONObject dataObject = profileObject.getJSONObject("data");
        String balance = dataObject.getString("walletBalance");
        String softLimit = dataObject.getString("walletSoftLimit");
        String hardLimit = dataObject.getString("walletHardLimit");
        String currencySymbol=dataObject.getString("currencySymbol");
        VariableConstant.CURRENCY = currencySymbol;

        walletView.setBalanceValues(currencySymbol+" "+balance, currencySymbol+" "+hardLimit
                , currencySymbol+" "+softLimit,currencySymbol);

        String currencyAbbr=dataObject.getString("currencyAbbr");
        if(currencyAbbr.matches("1"))
            preferenceHelperDataSource.setWalletBal("( ".concat(currencySymbol.concat(" ").concat(balance)).concat(" )"));
        else
            preferenceHelperDataSource.setWalletBal("( ".concat(balance.concat(" ").concat(currencySymbol)).concat(" )"));

    }

    @Override
    public void checkAmount(String amount)
    {
        if(amount==null || amount.equals(""))
            walletView.showAddBalanceNotifier();
        else if(Double.parseDouble(amount)>0 )
            walletView.enableButton();

    }

    @Override
    public void rechargeWallet(String amount)
    {
        Utility.printLog("the amount is : "+amount);
        if(preferenceHelperDataSource.getDefaultCardDetails()!=null) {
            walletView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.rechargeWallet(
                    preferenceHelperDataSource.getSessionToken(),
                    preferenceHelperDataSource.getLanguage(),
                    preferenceHelperDataSource.getDefaultCardDetails().getId(), amount);   //card_1CAvIg2876tVKl2MlPy06DBq

            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            Utility.printLog(TAG + " RechargeWallet onNext: " + value.code());
                            switch (value.code()) {
                                case 200:
                                    try {
                                        String responseString = value.body().string();
                                        handleresponse(responseString);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    try {
                                        Utility.printLog(TAG + " RechargeWallet errorr : " +value.errorBody().string() );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    walletView.walletDetailsApiErrorViewNotifier(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Utility.printLog(TAG + "RechargeWallet error: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            walletView.hideProgressDialog();
                        }
                    });


        }else {
            Utility.BlueToast(mContext,mContext.getResources().getString(R.string.selectPayment));
        }
    }

    @Override
    public void getLastCardNo()
    {
        if(preferenceHelperDataSource.getDefaultCardDetails()!=null) {
            Utility.printLog("MyCardNumber"+preferenceHelperDataSource.getDefaultCardDetails().getLast4());
           walletView.setCard(preferenceHelperDataSource.getDefaultCardDetails().getLast4(),
                    preferenceHelperDataSource.getDefaultCardDetails().getBrand());
       }
       else
           walletView.setNoCard();
    }

    @Override
    public void attachView(WalletFragment walletFragment)
    {
        this.walletView=walletFragment;
    }

    @Override
    public void detachview()
    {
        this.walletView=null;
    }
}
