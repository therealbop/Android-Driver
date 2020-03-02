package com.karry.side_screens.bankDetails.bankStripeAccountAdd;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.heride.partner.BuildConfig;
import com.heride.partner.R;
import com.karry.network.NetworkService;
import com.karry.pojo.bank.StripeDetailsPojo;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCountryData;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCountryList;
import com.karry.utility.TextUtil;
import com.karry.utility.Upload_file_AmazonS3;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONObject;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.inject.Inject;

import eu.janmuller.android.simplecropimage.CropImage;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;
import static com.karry.utility.VariableConstant.STRIPE_DATA;

/**
 * <h1>BankStripeAddPresenter</h1>
 * <p>the class for the Implementation of Add new Stripe Account.</p>
 */
public class BankStripeAddPresenter implements BankStripeAddContract.BankStripeAddPresenter{

    @Inject BankStripeAddContract.BankStripeAddView bankStripeAddView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject NetworkService networkService;
    @Inject CompositeDisposable compositeDisposable;
    @Inject Context context;
    @Inject Gson gson;

    ArrayList<ConnectAccountCountryList> countryList = new ArrayList<>();
    String selectedCountryID;

    private String ipAddress="";

    @Inject
    BankStripeAddPresenter() {
    }

    /**********************************************************************************************/
    @Override
    public void setActionBar() {
        bankStripeAddView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        bankStripeAddView.setTitle();
    }

    @Override
    public void postConnectAccountAPI(String Fname, String Lname, String[] DOB, String PersonalId, String Address, String City,
                                      String State, String PostalCode, String DocumentURL, String ipAddress) {

        ipAddress = ipAddress.replace("localhost/","");

        /*if(Integer.parseInt(DOB[0]) < 10)
        {
            DOB[0] = "0"+DOB[0];
        }

        if(Integer.parseInt(DOB[1]) < 10)
        {
            DOB[1] = "0"+DOB[1];
        }*/

        bankStripeAddView.showProgress();
        io.reactivex.Observable<Response<ResponseBody>> getConnectAccount=networkService.postConnectAccount(
                preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getGmail(),
                City,
                selectedCountryID,
                Address,
                PostalCode,
                State,
                DOB[0],
                DOB[1],
                DOB[2],
                Fname,
                Lname,
                DocumentURL,
                PersonalId,
                Utility.date(),
                ipAddress);

        Log.d("test   ", "postConnectAccountAPI: " +City +"\n"+
                "US" +"\n"+
                Address +"\n"+
                PostalCode +"\n"+
                State +"\n"+
                DOB[0] +"\n"+
                DOB[1] +"\n"+
                DOB[2] +"\n"+
                Fname +"\n"+
                Lname +"\n"+
                DocumentURL +"\n"+
                PersonalId +"\n"+
                Utility.date() +"\n"+
                ipAddress
        );
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
                                    jsonObject=new JSONObject(value.body().string());
                                    Utility.printLog("getConnectAccount : "+value.body().string());
                                    bankStripeAddView.addStripeSuccess(jsonObject.getString("message"));
                                    break;

                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    Utility.BlueToast(context, jsonObject.getString("message"));
                                    Utility.printLog("getConnectAccount : "+value.errorBody().string());
                                    break;
                            }

                        }catch (Exception e)
                        {
                            Utility.printLog("getConnectAccount : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog("the api error is : "+e.getMessage());
                        bankStripeAddView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        bankStripeAddView.hideProgress();
                    }
                });
    }

    @Override
    public void cropImage(Intent data) {

        String path = data.getStringExtra(CropImage.IMAGE_PATH);
        if(path!=null) {
            VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
            if (Utility.isNetworkAvailable(context)) {
                amzonUpload(new File(path),context,VariableConstant.BANK_DOC);
            } else {
                Utility.BlueToast(context, context.getString(R.string.no_network));
            }

        }

    }

    @Override
    public void amzonUpload(File file, Context context, String Bucket_folder) {
        bankStripeAddView.showProgress();

        Upload_file_AmazonS3 amazonS3 = Upload_file_AmazonS3.getInstance(context);
        final String imageUrl = BuildConfig.AMAZON_BASE_URL+BuildConfig.BUCKET_NAME+"/"+ Bucket_folder +"/"+file.getName();
        amazonS3.Upload_data(BuildConfig.BUCKET_NAME, Bucket_folder +"/"+file.getName(), file, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String url)
            {
                bankStripeAddView.hideProgress();
                bankStripeAddView.amazonUploadSuccess(imageUrl);
                preferenceHelperDataSource.setStripImage(imageUrl);

            }

            @Override
            public void sucess(String url, String type) {
                bankStripeAddView.hideProgress();
                bankStripeAddView.amazonUploadSuccess(url);
            }

            @Override
            public void error(String errormsg)
            {
                bankStripeAddView.hideProgress();
                Log.d("baby","error");
            }
        });
    }

    @Override
    public void validateData(String Fname, String Lname, String DOB,
                             String PersonalId, String Address, String City,
                             String State, String PostalCode, String DocumentURL) {
        if(TextUtil.isEmpty(Fname)){
            bankStripeAddView.editTextErr("Fname");
        }else if(TextUtil.isEmpty(Lname)){
            bankStripeAddView.editTextErr("Lname");
        }else if(TextUtil.isEmpty(DOB)){
            bankStripeAddView.editTextErr("DOB");
        }else if(TextUtil.isEmpty(PersonalId)){
            bankStripeAddView.editTextErr("PersonalID");
        }/*else if(TextUtil.isEmpty(PersonalId)){
            bankStripeAddView.editTextErr("PersonalIDLength");
        }*/else if(TextUtil.isEmpty(Address)){
            bankStripeAddView.editTextErr("Address");
        }else if(TextUtil.isEmpty(City)){
            bankStripeAddView.editTextErr("City");
        }else if(TextUtil.isEmpty(State)){
            bankStripeAddView.editTextErr("State");
        }else if(TextUtil.isEmpty(PostalCode)){
            bankStripeAddView.editTextErr("PostalCode");
        }else if(TextUtil.isEmpty(DocumentURL)){
            bankStripeAddView.editTextErr("DocumentImage");
        }else if(selectedCountryID==null){
            bankStripeAddView.editTextErr("CountryID");
        }else {
            bankStripeAddView.editTextErr("default");
        }
    }

    @Override
    public void fetchIP() {
        new findIpAddress().execute();
    }

    @Override
    public void hideKeyboardAndClearFocus() {
        bankStripeAddView.hideSoftKeyboard();
    }

    @Override
    public void accountCountryAPI() {

        if (countryList != null && countryList.size() > 0) {
            bankStripeAddView.setCountryListforSelect(countryList);
        } else {
            bankStripeAddView.showProgress();
            io.reactivex.Observable<Response<ResponseBody>> getConnectAccount=networkService.getConnectAccountCountry(
                    preferenceHelperDataSource.getSessionToken(),
                    preferenceHelperDataSource.getLanguage());

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
                                        jsonObject=new JSONObject(value.body().string());
                                        Utility.printLog("getConnectAccountCountry : "+value.body().string());
                                        ConnectAccountCountryData connectAccountCountryData = gson.fromJson(jsonObject.get("data").toString(),ConnectAccountCountryData.class);
                                        bankStripeAddView.setCountryListforSelect(connectAccountCountryData.getCountryList());
                                        break;

                                    default:
                                        jsonObject=new JSONObject(value.errorBody().string());
                                        Utility.BlueToast(context, jsonObject.getString("message"));
                                        Utility.printLog("getConnectAccountCountry : "+value.errorBody().string());
                                        break;
                                }

                            }catch (Exception e)
                            {
                                Utility.printLog("getConnectAccount : Catch :"+e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Utility.printLog("the api error is : "+e.getMessage());
                            bankStripeAddView.hideProgress();
                        }

                        @Override
                        public void onComplete() {
                            bankStripeAddView.hideProgress();
                        }
                    });
        }
    }



    @Override
    public void onCountrySelected(Intent data) {
        if (countryList != null) {
            countryList.clear();

        }
        countryList = (ArrayList<ConnectAccountCountryList>) data.getSerializableExtra(DATA);
        for (ConnectAccountCountryList connectAccountCountry : countryList) {
            if (connectAccountCountry.isSelected()) {

                bankStripeAddView.setCountryText(connectAccountCountry.getCountry(),connectAccountCountry.getCountryCode());
                selectedCountryID=connectAccountCountry.getCountryCode();
                /*validateToNext();*/

            }

        }
    }


    @Override
    public void getData(Bundle bundle) {
        try {
            if(bundle!=null) {
                StripeDetailsPojo stripeDetailsPojo = (StripeDetailsPojo) bundle.getSerializable(STRIPE_DATA);
                Utility.printLog("the gmail iss : " + stripeDetailsPojo.getEmail());
                bankStripeAddView.setText(stripeDetailsPojo);
            }
        }catch (Exception e){

        }


    }

    private class findIpAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here
            try {
                return InetAddress.getLocalHost().toString();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "007";
            }
        }

        @Override
        protected void onPostExecute(String message) {
            bankStripeAddView.getIpAddress(message);
        }
    }


}
