package com.karry.side_screens.home_fragment.invoice;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.heride.partner.BuildConfig;
import com.heride.partner.R;
import com.karry.pojo.invoice.BookingDetailsPojo;
import com.karry.utility.DataParser;
import com.karry.utility.MyImageHandler;
import com.karry.utility.TextUtil;
import com.karry.utility.Upload_file_AmazonS3;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.METERBOOKING_INVOICE_DATA;
import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;
import static com.karry.utility.VariableConstant.SIGNATURE_UPLOAD;

/**
 * <h1>InvoicePresenter</h1>
 * <p>the class which implement the Invoice</p>
 */
class InvoicePresenter implements InvoiceContract.InvoicePresenter {

    @Inject InvoiceContract.InvoiceView invoiceView;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferencesHelper;
    @Inject Context context;
    @Inject Gson gson;
    @Inject RxNetworkObserver rxNetworkObserver;
    @Inject NetworkStateHolder networkStateHolder;
    private String bid;
    private BookingDetailsPojo bookingDetailsPojo;
    @Inject CompositeDisposable compositeDisposable;


    private Bitmap signBitmap;
    private File takenNewSignature;
    private String newSignatureName;
    private String signatureUrl="";
    /*@Inject Upload_file_AmazonS3 amazonS3;*/


    @Inject
    InvoicePresenter() {
    }

    @Override
    public void getmeterBookingInvoiceData(Bundle data) {
        Utility.printLog("the meterBookingInvoiceData is : "+ data.getString(METERBOOKING_INVOICE_DATA));

        bid = data.getString(METERBOOKING_INVOICE_DATA);
        getBookingDetails(bid);
    }

    /**
     * <h1>getBookingDetails</h1>
     * <p>API call for get the Booking details</p>
     * @param bid : booking id
     */
    private void getBookingDetails(String bid){

        invoiceView.showProgress();

        Observable<Response<ResponseBody>> BookingDetails = networkService.getBookingDetail
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage(),
                        bid);

        BookingDetails.subscribeOn(Schedulers.newThread())
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
                                    Utility.printLog("getBookingDetail success response is  : \n" +resp);

                                    bookingDetailsPojo  = gson.fromJson(resp,BookingDetailsPojo.class);
                                    preferencesHelper.setBookingStatus(bookingDetailsPojo.getData().getStatus());
                                    invoiceView.setValues(bookingDetailsPojo);
                                    String meterBookingEnable = bookingDetailsPojo.getData().getInvoice().getIsMeterBooking();
                                    if(meterBookingEnable.matches(VariableConstant.FALSE))
                                        invoiceView.setRideBookingView();
                                    else
                                        invoiceView.setMeterBookingView();

//                                    checkPaymentType(bookingDetailsPojo.getData().getInvoice().getPaymentType(), bookingDetailsPojo.getData().isCorporateBooking());
                                    checkPaymentType();
                                    getTotal();

                                    newSignatureName = bookingDetailsPojo.getData().getBookingId() + ".jpg";
                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    invoiceView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    String err=value.errorBody().string();
                                    Utility.printLog("getBookingDetail error response is  : \n" +err);
                                    invoiceView.apiFailure(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        invoiceView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        invoiceView.hideProgress();
                    }
                });
    }


    @Override
    public void completeInvoice(String email, String rating) {

        if(bookingDetailsPojo.getData().getInvoice().getIsMeterBooking().matches(VariableConstant.TRUE)){
            if(email!=null && !email.matches("")){
                if (TextUtil.emailValidation(email))
                    sendMeterBookingEmail(email);
                else
                    invoiceView.apiFailure(context.getResources().getString(R.string.invalidEmail));
            }
            else {
                invoiceView.finishActivity();
            }
        }else {

            Utility.printLog("the rating is : "+rating);
            sendPassengerReview(rating);

            /*if(bookingDetailsPojo.getData().getInvoice().getPaymentType().matches("1") && bookingDetailsPojo.getData().getStatus().matches("15")){
                //if the booking payme is in card
                bookingStatusRideCard(AppConstants.BookingStatus.Completed,rating);
            }else {
                //if the booking is in cash
                Utility.printLog("the rating is : "+rating);
                sendPassengerReview(rating);
            }*/

        }
    }

    @Override
    public void getTotal() {
        if(bookingDetailsPojo.getData().getCurrencyAbbr().matches("1"))
            invoiceView.setTotal(bookingDetailsPojo.getData().getCurrencySbl().concat(" ").concat(bookingDetailsPojo.getData().getInvoice().getTotal()));
        else
            invoiceView.setTotal(bookingDetailsPojo.getData().getInvoice().getTotal().concat(" ").concat(bookingDetailsPojo.getData().getCurrencySbl()));
    }


    /**
     * <h1>sendMeterBookingEmail</h1>
     * <p>the API for send the invoice to passenger Email ID for the meterBooking</p>
     * @param email : passenger email id
     */
    private void sendMeterBookingEmail(String email){

        invoiceView.showProgress();
        Observable<Response<ResponseBody>> meterBookingEmail = networkService.sendMeterBookingEmail
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage(),
                        bid,
                        email);

        meterBookingEmail.subscribeOn(Schedulers.newThread())
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
                                    Utility.printLog("sendMeterBookingEmail success response is  : \n" +resp);
                                    invoiceView.finishActivity();
                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    invoiceView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    String err=value.errorBody().string();
                                    Utility.printLog("sendMeterBookingEmail error response is  : \n" +err);
                                    invoiceView.apiFailure(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        invoiceView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        invoiceView.hideProgress();
                    }
                });
    }


    /**
     * <h1>sendPassengerReview</h1>
     * <p>the API for send the review of passenger for the RideBooking</p>
     * @param rating : Rating for passenger
     */
    private void sendPassengerReview(String rating){

        invoiceView.showProgress();
        Observable<Response<ResponseBody>> reviewForRideBooking = networkService.reviewForRideBooking
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage(),
                        bid,
                        Double.parseDouble(rating),
                        signatureUrl);

        reviewForRideBooking.subscribeOn(Schedulers.newThread())
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
                                    Utility.printLog("reviewForRideBooking success response is  : \n" +resp);
                                    invoiceView.finishActivity();
                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    invoiceView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    String err=value.errorBody().string();
                                    JSONObject jsonObject = new JSONObject(err);
                                    Utility.printLog("reviewForRideBooking error response is  : \n" );
                                    invoiceView.apiFailure(jsonObject.getString("message"));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        invoiceView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        invoiceView.hideProgress();
                    }
                });
    }

    /**
     * <h1>checkPaymentType</h1>
     * <p>check the payment type and set the View</p>
     * @param type : payment type
     * @param isCorporateBooking : boolean value for check corporate booking
     */
    private void checkPaymentType(String type, boolean isCorporateBooking)
    {
        if(type.equals("1"))
            invoiceView.setCardPaymentType();
        else if(type.equals("2"))
            invoiceView.setCashPaymentType();
        else if(type.equals("3")) {
            invoiceView.setCreditPaymentType();
            if(isCorporateBooking)
                invoiceView.setCorporateBookng();
        }

    }

    /**
     * <h1>checkPaymentType</h1>
     * <p></p>
     */
    private void checkPaymentType(){

        String walletAmount = context.getResources().getString(R.string.walet);
        String cardAmount = context.getResources().getString(R.string.card);
        String cashAmount = context.getResources().getString(R.string.cash);
        String corporateWallet = context.getResources().getString(R.string.corporateWalet);
        String currencySymbol = bookingDetailsPojo.getData().getCurrencySbl();
        String open = " ( ";
        String close = " ) ";
        if(bookingDetailsPojo.getData().getCurrencyAbbr().matches("1"))
        {
            walletAmount = walletAmount.concat(open).concat(currencySymbol).concat(" ").concat(bookingDetailsPojo.getData().getPaymentMethod().getWalletTransaction()).concat(close);
            cardAmount = cardAmount.concat(open).concat(currencySymbol).concat(" ").concat(bookingDetailsPojo.getData().getPaymentMethod().getCardDeduct()).concat(close);
            cashAmount = cashAmount.concat(open).concat(currencySymbol).concat(" ").concat(bookingDetailsPojo.getData().getPaymentMethod().getCashCollected()).concat(close);
            corporateWallet = corporateWallet.concat(open).concat(currencySymbol).concat(" ").concat(bookingDetailsPojo.getData().getInvoice().getTotal()).concat(close);
        }else {
            walletAmount = walletAmount.concat(open).concat(bookingDetailsPojo.getData().getPaymentMethod().getWalletTransaction()).concat(" ").concat(currencySymbol).concat(close);
            cardAmount = cardAmount.concat(open).concat(bookingDetailsPojo.getData().getPaymentMethod().getCardDeduct()).concat(" ").concat(currencySymbol).concat(close);
            cashAmount = cashAmount.concat(open).concat(bookingDetailsPojo.getData().getPaymentMethod().getCashCollected()).concat(" ").concat(currencySymbol).concat(close);
            corporateWallet = cashAmount.concat(open).concat(bookingDetailsPojo.getData().getInvoice().getTotal()).concat(" ").concat(currencySymbol).concat(close);
        }


        if(Double.parseDouble(bookingDetailsPojo.getData().getPaymentMethod().getWalletTransaction())>0){
            invoiceView.enableWallet(walletAmount);
        }else {
            invoiceView.disableWallet();
        }

        if(Double.parseDouble(bookingDetailsPojo.getData().getPaymentMethod().getCardDeduct())>0){
            invoiceView.enableCard(cardAmount);
        }else {
            invoiceView.disableCard();
        }

        if(Double.parseDouble(bookingDetailsPojo.getData().getPaymentMethod().getCashCollected())>0){
            invoiceView.enableCash(cashAmount);
        }else {
            invoiceView.disableCash();
        }

        if(bookingDetailsPojo.getData().getPaymentMethod().isCorporateBooking()){
            invoiceView.enableCorporateWallet(corporateWallet);
            invoiceView.disableCash();
            invoiceView.disableCard();
            invoiceView.disableWallet();
        }else {
            invoiceView.disableCorporateBooking();
        }


    }

    @Override
    public void subscribeNetworkObserver()
    {
        Observer<NetworkStateHolder> observer = new Observer<NetworkStateHolder>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                compositeDisposable.add(d);
            }
            @Override
            public void onNext(NetworkStateHolder value)
            {
                Utility.printLog(""+" network not avalable "+value.isConnected());
                try {
                    if(value.isConnected())
                        invoiceView.networkAvailable();
                    else
                        invoiceView.networkNotAvailable();
                }catch (Exception e){
                    Utility.printLog(""+ Arrays.toString(e.getStackTrace()));
                }

            }
            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
            }
            @Override
            public void onComplete()
            {}
        };
        rxNetworkObserver.subscribeOn(Schedulers.io());
        rxNetworkObserver.observeOn(AndroidSchedulers.mainThread());
        rxNetworkObserver.subscribe(observer);
    }

    @Override
    public void clearComposite() {
        compositeDisposable.clear();
    }

    @Override
    public void refresh() {
        invoiceView.clearSignature();
    }

    @Override
    public void onBackPressSign() {

        if(signatureUrl.matches(""))
            invoiceView.hideSignature(signBitmap, false);
        else
            invoiceView.hideSignature(signBitmap, true);
    }

    @Override
    public void onSigned(Bitmap signBitmap) {
        this.signBitmap=signBitmap;
        signatureUrl="";
    }

    @Override
    public void onSignatureApprove() {
        if(signBitmap!=null){
            createFile();
            uploadToAmazon();
        }

    }

    @Override
    public void openSignaturePad() {
        invoiceView.showSignature();
    }

    private void createFile() {
        MyImageHandler myImageHandler = MyImageHandler.getInstance();
        File dir = myImageHandler.getAlbumStorageDir(context, VariableConstant.SIGNATURE_PIC_DIR, true);
        takenNewSignature = new File(dir, newSignatureName);
        try {
            saveBitmapToJPG(signBitmap, takenNewSignature);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    private void uploadToAmazon(){
        invoiceView.showProgress();


        Upload_file_AmazonS3 amazonS3 = Upload_file_AmazonS3.getInstance(context);
        final String imageUrl = BuildConfig.AMAZON_BASE_URL+BuildConfig.BUCKET_NAME+"/"+ SIGNATURE_UPLOAD +"/"+takenNewSignature.getName().replace(" ","");
        amazonS3.Upload_data(BuildConfig.BUCKET_NAME,
                SIGNATURE_UPLOAD +"/"+takenNewSignature.getName().replace(" ",""),
                takenNewSignature, new Upload_file_AmazonS3.Upload_CallBack() {
                    @Override
                    public void sucess(String url) {
                        invoiceView.hideProgress();
                        Log.d("UploadUrl", imageUrl);
                        signatureUrl=imageUrl;
                        invoiceView.onSignatureApprove(signBitmap);

                    }

                    @Override
                    public void sucess(String url, String type) {
                        invoiceView.hideProgress();
                        Log.d("UploadUrl 1", url);
                        signatureUrl=url;
                        invoiceView.onSignatureApprove(signBitmap);
                    }

                    @Override
                    public void error(String errormsg) {
                        invoiceView.hideProgress();
                        Log.d("URL", "error");
                    }
                });
    }


}
