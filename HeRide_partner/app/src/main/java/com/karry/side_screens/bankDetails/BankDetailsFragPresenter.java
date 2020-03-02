package com.karry.side_screens.bankDetails;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.karry.pojo.bank.StripeDetailsPojo;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;

import org.json.JSONObject;


import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.RESPONSE_CODE_NO_STRIPE_ACCOUNT_FOUND;
import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;

/**
 * <h1>BankDetailsFragPresenter</h1>
 * <p>BookingPopUpPresenter Class for Bank Details Fragment</p>
 */
public class BankDetailsFragPresenter implements BankDetailsFragContract.BankDetailsFragPresenter{

    @Inject  PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject  NetworkService networkService;
    @Inject  CompositeDisposable compositeDisposable;
    @Nullable
    private BankDetailsFragContract.BankDetailsFragView bankDetailsFragView;
    private StripeDetailsPojo stripeDetailsPojo;

    @Inject
    public BankDetailsFragPresenter() {
    }



    @Override
    public void attachView(Object view) {
        bankDetailsFragView  = (BankDetailsFragContract.BankDetailsFragView) view;
    }

    @Override
    public void detachView() {
        bankDetailsFragView = null;
    }

    @Override
    public void getConnectAccount() {
        Utility.printLog(""+preferenceHelperDataSource.getSessionToken());
        bankDetailsFragView.showProgress();
        io.reactivex.Observable<Response<ResponseBody>> getConnectAccount=networkService.getConnectAccount(
                preferenceHelperDataSource.getSessionToken(),preferenceHelperDataSource.getLanguage());

        getConnectAccount.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            JSONObject jsonObject;

                            switch (value.code())
                            {
                                case RESPONSE_CODE_SUCCESS:
                                    String responseString = value.body().string();
                                    jsonObject=new JSONObject(responseString);
                                    Utility.printLog("getConnectAccount : "+responseString);
                                    jsonObject = new JSONObject(jsonObject.getString("data"));

                                    if (jsonObject.has("legal_entity")) {
                                        stripeDetailsPojo = new Gson().fromJson(jsonObject.toString(), StripeDetailsPojo.class);
                                        bankDetailsFragView.sripeAccAddSuccess(stripeDetailsPojo);
                                        return;
                                    }
                                    break;

                                case RESPONSE_CODE_NO_STRIPE_ACCOUNT_FOUND:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    Utility.printLog("getConnectAccount No Stripe Added: "+value.toString());
                                    bankDetailsFragView.showAddStipe(jsonObject.getString("message"));
                                    break;

                                default:
                                    bankDetailsFragView.onFailure(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            Utility.printLog("getConnectAccount : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog("the api error is : "+e.getMessage());
                        bankDetailsFragView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        bankDetailsFragView.hideProgress();
                    }
                });
    }

    @Override
    public void compositeDisposableClear() {
        compositeDisposable.clear();
    }



    @Override
    public void updateStripe() {
        if (stripeDetailsPojo!=null){
            assert bankDetailsFragView != null;
            bankDetailsFragView.openUpdateStripe(stripeDetailsPojo);
        }
    }

    @Override
    public void addNewBank() {
        assert bankDetailsFragView != null;
        bankDetailsFragView.openActivityForBankAdd(stripeDetailsPojo.getLegal_entity().getAddress().getCountry());
    }
}
