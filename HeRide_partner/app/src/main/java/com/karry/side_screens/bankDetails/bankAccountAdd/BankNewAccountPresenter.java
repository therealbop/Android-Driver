package com.karry.side_screens.bankDetails.bankAccountAdd;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCurrencyList;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCurrencyListSelection;
import com.karry.utility.TextUtil;
import com.karry.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;

/**
 * <h1>BankNewAccountPresenter</h1>
 * <p>implementation class for Add new Bank Account</p>
 */
public class BankNewAccountPresenter implements BankNewAccountContract.BankNewAccountPresenter{

    @Inject BankNewAccountContract.BankNewAccountView bankNewAccountView;
    @Inject Context context;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject NetworkService networkService;
    @Inject Gson gson;

    private String selectedCurrency;
    private String selectedCountry;


    private ArrayList<ConnectAccountCurrencyListSelection> currencylist = new ArrayList<>();

    @Inject
    public BankNewAccountPresenter() {
    }

    @Override
    public void setActionBar() {
        bankNewAccountView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        bankNewAccountView.setTitle();
    }

    @Override
    public void validateData(String Name, String AccountNo, String RoutingNo) {
        if(TextUtil.isEmpty(Name)){
            bankNewAccountView.editTextErr("Name");
        }else if(TextUtil.isEmpty(AccountNo)){
            bankNewAccountView.editTextErr("AccountNo");
        }else if(TextUtil.isEmpty(RoutingNo)){
            bankNewAccountView.editTextErr("RoutingNo");
        }else if(selectedCurrency==null){
            bankNewAccountView.editTextErr("selectedCurrency");
        }else {
            bankNewAccountView.editTextErr("default");
        }
    }

    @Override
    public void externalAccountAPI(String Name, String AccountNo, String RoutingNo) {
        bankNewAccountView.showProgress();
        io.reactivex.Observable<Response<ResponseBody>> postExternalAccount=networkService.postExternalAccount(
                preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getGmail(),AccountNo,RoutingNo,Name,selectedCountry,selectedCurrency);

        postExternalAccount.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            JSONObject jsonObject;

                            switch (value.code())
                            {
                                case RESPONSE_CODE_SUCCESS:
                                    jsonObject=new JSONObject(value.body().string());
                                    Utility.printLog("postExternalAccount : "+value.body().string());
                                    bankNewAccountView.externalAccountAPISuccess(jsonObject.getString("message"));
                                    break;

                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    Utility.BlueToast(context, jsonObject.getString("message"));
                                    Utility.printLog("postExternalAccount : "+value.errorBody().string());

                                    break;
                            }

                        }catch (Exception e)
                        {
                            Utility.printLog("postExternalAccount : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog("the api error is : "+e.getMessage());
                        bankNewAccountView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        bankNewAccountView.hideProgress();
                    }
                });

    }

    @Override
    public void accountCurrencyAPI(String countryID) {

        selectedCountry = countryID;
        if (currencylist != null && currencylist.size() > 0) {
            bankNewAccountView.setCurrencyListforSelect(currencylist);
        } else {
            bankNewAccountView.showProgress();
            io.reactivex.Observable<Response<ResponseBody>> getAccountCurrency=networkService.getAccountCurrency(
                    preferenceHelperDataSource.getSessionToken(),
                    preferenceHelperDataSource.getLanguage(),
                    countryID);

            getAccountCurrency.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            try {
                                JSONObject jsonObject;

                                switch (value.code())
                                {
                                    case RESPONSE_CODE_SUCCESS:
                                        jsonObject=new JSONObject(value.body().string());
                                        Utility.printLog("getAccountCurrency : "+value.body().string());
                                        ConnectAccountCurrencyList connectAccountCurrencyList = gson.fromJson(jsonObject.get("data").toString(),ConnectAccountCurrencyList.class);

                                        currencylist = new ArrayList<>();

                                        if(connectAccountCurrencyList.getCurrency().size()>1) {
                                            for (int i = 0; i < connectAccountCurrencyList.getCurrency().size(); i++) {

                                                ConnectAccountCurrencyListSelection connectAccountCurrencyListSelection = new ConnectAccountCurrencyListSelection();
                                                connectAccountCurrencyListSelection.setCurrencyID(connectAccountCurrencyList.getCurrency().get(i));
                                                connectAccountCurrencyListSelection.setSelected(false);

                                                currencylist.add(connectAccountCurrencyListSelection);
                                            }

                                            bankNewAccountView.setCurrencyListforSelect(currencylist);
                                        }else {
                                            bankNewAccountView.setCurrencyText(connectAccountCurrencyList.getCurrency().get(0));
                                            selectedCurrency=connectAccountCurrencyList.getCurrency().get(0);
                                        }
                                            break;

                                    default:
                                        jsonObject=new JSONObject(value.errorBody().string());
                                        Utility.BlueToast(context, jsonObject.getString("message"));
                                        Utility.printLog("getAccountCurrency : "+value.errorBody().string());
                                        break;
                                }

                            }catch (ArithmeticException e)
                            {
                                Utility.printLog("getConnectAccount : Catch :"+e.getMessage());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Utility.printLog("the api error is : "+e.getMessage());
                            bankNewAccountView.hideProgress();
                        }

                        @Override
                        public void onComplete() {
                            bankNewAccountView.hideProgress();
                        }
                    });
        }
    }

    @Override
    public void onCurrencySelected(Intent data) {
        if (currencylist != null) {
            currencylist.clear();

        }
        currencylist = (ArrayList<ConnectAccountCurrencyListSelection>) data.getSerializableExtra(DATA);
        for (ConnectAccountCurrencyListSelection connectAccountCurrencyListSelection : currencylist) {
            if (connectAccountCurrencyListSelection.isSelected()) {

                bankNewAccountView.setCurrencyText(connectAccountCurrencyListSelection.getCurrencyID());
                selectedCurrency=connectAccountCurrencyListSelection.getCurrencyID();
                /*validateToNext();*/

            }

        }
    }
}
