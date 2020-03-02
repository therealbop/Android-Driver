package com.karry.side_screens.history.history_invoice;


import android.content.Context;

import com.google.gson.Gson;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.heride.partner.R;
import com.karry.side_screens.history.new_model.AppointmentData;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;


public class HistoryInvoiceActivityPresenter  implements HistoryInvoiceContract.HistoryInvoicePresenter{

    @Inject HistoryInvoiceContract.HistoryInvoiceView historyInvoiceView;
    @Inject
    NetworkService networkService;
    @Inject
    PreferenceHelperDataSource preferencesHelper;
    @Inject Context context;
    private ArrayList<HelpDataModel> helpDataModel;
    @Inject ArrayList<HelpDataModel> helpDataModels;
    @Inject Gson gson;

    AppointmentData appointmentData ;
    @Inject
    HistoryInvoiceActivityPresenter() {
    }

    public void isMinFare(String isMinFeeApplied,String status)
    {
        if(status!=null && status.equals("4")||status.equals("5"))
            historyInvoiceView.setCancelationFee();
        else if(isMinFeeApplied!=null && isMinFeeApplied.equals("1"))
            historyInvoiceView.hideFeeDetails();
        else
            historyInvoiceView.showFeeDetails();
    }

    public void isDropLacationEmpty(String droplocation)
    {
        if(droplocation!=null && "".equals(droplocation))
            historyInvoiceView.hideDropLocation();

    }


    public void checkPaymentType(String type,boolean isCorporateBooking)
    {
        switch (type) {
            case "1":
                historyInvoiceView.setCardPaymentType();
                break;
            case "2":
                historyInvoiceView.setCashPaymentType();
                break;
            case "3":
                historyInvoiceView.setCreditPaymentType();
                if (isCorporateBooking)
                    historyInvoiceView.setCorporatePaymentType();

                break;
        }

    }

    @Override
    public void checkPaymentType(AppointmentData appointmentData){

        this.appointmentData = appointmentData;

        String walletAmount =context .getResources().getString(R.string.walet);
        String cardAmount = context.getResources().getString(R.string.card);
        String cashAmount = context.getResources().getString(R.string.cash);
        String corporateWallet = context.getResources().getString(R.string.corporateWalet);
        String currencySymbol = appointmentData.getCurrencySbl();
        String open = " ( ";
        String close = " ) ";
        if(appointmentData.getCurrencyAbbr().matches("1"))
        {
            walletAmount = currencySymbol.concat(" ").concat(appointmentData.getPaymentMethod().getWalletTransaction());
            cardAmount = currencySymbol.concat(" ").concat(appointmentData.getPaymentMethod().getCardDeduct());
            cashAmount =currencySymbol.concat(" ").concat(appointmentData.getPaymentMethod().getCashCollected());
            corporateWallet = currencySymbol.concat(" ").concat(appointmentData.getInvoice().getTotal());
        }else {
            walletAmount = appointmentData.getPaymentMethod().getWalletTransaction().concat(" ").concat(currencySymbol);
            cardAmount = appointmentData.getPaymentMethod().getCardDeduct().concat(" ").concat(currencySymbol);
            cashAmount = appointmentData.getPaymentMethod().getCashCollected().concat(" ").concat(currencySymbol);
            corporateWallet = appointmentData.getInvoice().getTotal().concat(" ").concat(currencySymbol);
        }


        if(Double.parseDouble(appointmentData.getPaymentMethod().getWalletTransaction())>0){
            historyInvoiceView.enableWallet(walletAmount);
        }else {
            historyInvoiceView.disableWallet();
        }

        if(Double.parseDouble(appointmentData.getPaymentMethod().getCardDeduct())>0){
            historyInvoiceView.enableCard(cardAmount);
        }else {
            historyInvoiceView.disableCard();
        }

        if(Double.parseDouble(appointmentData.getPaymentMethod().getCashCollected())>0){
            historyInvoiceView.enableCash(cashAmount);
        }else {
            historyInvoiceView.disableCash();
        }

        if(Double.parseDouble(appointmentData.getPaymentMethod().getWalletTransaction())<=0
                && Double.parseDouble(appointmentData.getPaymentMethod().getCardDeduct())<=0
                && Double.parseDouble(appointmentData.getPaymentMethod().getCashCollected())<=0){
            historyInvoiceView.disablePayment();
        }

        if(appointmentData.getPaymentMethod().isCorporateBooking()){
            historyInvoiceView.enableCorporateWallet(corporateWallet);
            historyInvoiceView.disableCash();
            historyInvoiceView.disableCard();
            historyInvoiceView.disableWallet();
        }else {
            historyInvoiceView.disableCorporateBooking();
        }


    }

    @Override
    public void checkAppCommission(AppointmentData appointmentData) {
        if(Double.parseDouble(appointmentData.getInvoice().getAppCom())==0 && Double.parseDouble(appointmentData.getInvoice().getMasEarning())==0){
            historyInvoiceView.hideAppCommision();
        }
    }

    @Override
    public void getHelpDetails() {
        historyInvoiceView.showProgress();

        double status = 1;
        if(appointmentData.getStatus().matches("12"))
            status = 2;


        Observable<Response<ResponseBody>> getHelpDetails = networkService.getHelpText
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage(),status);

        getHelpDetails.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            switch (value.code()) {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("getHelpText success response is  : \n" +resp);
                                    HelpModel helpModel = gson.fromJson(resp,HelpModel.class);
                                    helpDataModel = helpModel.getData();

                                    if(helpDataModel.size()>0)
                                    {
                                        helpDataModel.get(0).setBid(appointmentData.getBookingId());
                                        helpDataModels.addAll(helpDataModel);
                                        historyInvoiceView.setHelpDetailsList();
                                    }

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    historyInvoiceView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    String err=value.errorBody().string();
                                    Utility.printLog("getBookingDetail error response is  : \n" +err);
                                    historyInvoiceView.apiFailure(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        historyInvoiceView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        historyInvoiceView.hideProgress();
                    }
                });
    }

    @Override
    public boolean checkhelpCenter() {
        return preferencesHelper.getIsHelpCenterEnable();
    }

}
