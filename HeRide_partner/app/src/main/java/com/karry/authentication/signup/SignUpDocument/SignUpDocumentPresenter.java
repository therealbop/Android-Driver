package com.karry.authentication.signup.SignUpDocument;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.google.gson.Gson;
import com.karry.authentication.signup.SignUpData;
import com.karry.data.source.local.PreferencesHelper;
import com.heride.partner.BuildConfig;
import com.heride.partner.R;
import com.karry.network.NetworkService;
import com.karry.utility.Upload_file_AmazonS3;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import eu.janmuller.android.simplecropimage.CropImage;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.os.Build.VERSION_CODES.N;
import static com.karry.utility.VariableConstant.DEVICE_TYPE;
import static com.karry.utility.VariableConstant.SIGNUP_DATA;


public class SignUpDocumentPresenter implements SignUpDocumentContract.SignUpDocumentPresenter {

    @Inject SignUpDocumentContract.SignUpDocumentView signUpDocumentView;
    @Inject NetworkService networkService;
    @Inject CompositeDisposable compositeDisposable;
    @Inject Context context;
    @Inject PreferencesHelper preferencesHelper;
    @Inject SignUpDocumentActivity signUpDocumentActivity;
    @Inject Gson gson;

    private boolean isPrivacyPolicy=false;

    private SignUpData signUpData;

    @Inject
    SignUpDocumentPresenter() {
    }

    @Override
    public void getVehicleData(Bundle mBundle) {
        signUpData = (SignUpData) mBundle.getSerializable(SIGNUP_DATA);
    }

    /**********************************************************************************************/
    @Override
    public void setActionBar() {
        signUpDocumentView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        signUpDocumentView.setTitle();
    }

    /**********************************************************************************************/
    @Override
    public void amzonUpload(File file, Context context, String Bucket_Folder) {

        signUpDocumentView.showProgress();

        Upload_file_AmazonS3 amazonS3 = Upload_file_AmazonS3.getInstance(context);
        final String imageUrl = BuildConfig.AMAZON_BASE_URL+BuildConfig.BUCKET_NAME+"/"+ Bucket_Folder +"/"+file.getName().replace(" ","");
        amazonS3.Upload_data(BuildConfig.BUCKET_NAME, Bucket_Folder +"/"+file.getName().replace(" ",""), file, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String url)
            {
                signUpDocumentView.hideProgress();
                signUpDocumentView.amazonUploadSuccess(imageUrl);

            }

            @Override
            public void sucess(String url, String type) {
                signUpDocumentView.hideProgress();
                Utility.printLog("babyy" +url);
            }

            @Override
            public void error(String errormsg)
            {
                Log.d("baby","error");
            }
        });
    }

    @Override
    public String getDocumentString(ArrayList<String> DocumentList) {
        String documentList="";
        for(int i=0;i<DocumentList.size();i++)
        {
            documentList = documentList+DocumentList.get(i);
            if((DocumentList.size()-1)>i)
                documentList = documentList+",";
        }
        Utility.printLog("the document List is : "+documentList);
        return documentList;
    }

    @Override
    public void signUpAPI(String al_licence_img_front,String al_licence_img_back, String al_vehicle_reg, String al_vehicle_insurance,
                          String al_police_clearance, String al_inspection_report, String al_goods_insurance,
                          String al_children_card,
                          String driverLicenseDate,
                          String motorInsuImageDate,
                          String registrationDate,
                          String policeClearanceDate,
                          String inspectionReportDate,
                          String goodsInTransitDate,
                          String workWithChildrenDate) {
        String signUpDatastring = gson.toJson(signUpData);
        Utility.printLog("the signUp data is :: "+signUpDatastring +"\n gender issss:::::::"+signUpData.getGender());

        signUpDocumentView.showProgress();
        double[] latlong = Utility.getLocation(context);

        Observable<Response<ResponseBody>> makeModel=networkService.signUp(
                preferencesHelper.getLanguage(),
                signUpData.getFname(),
                signUpData.getLname(),
                signUpData.getEmail(),
                signUpData.getPasswod(),
                signUpData.getCountryCode(),
                signUpData.getPhone(),
                signUpData.getCity(),
                latlong[0],
                latlong[1],
                signUpData.getProfilePic(),
                Utility.getDeviceId(context),
                DEVICE_TYPE,
                Build.MANUFACTURER,
                Build.MODEL,
                null,
                null,
                null,
                BuildConfig.VERSION_NAME,
                null,
                signUpData.getReferalCode(),
                null,
                signUpData.getService(),
                signUpData.getPlateNo(),
                signUpData.getVehiclePic(),
                null,
                signUpData.getType(),
                signUpData.getMake(),
                signUpData.getModel(),
                null,
                null,
                al_vehicle_insurance.toString(),
                null,
                al_vehicle_reg.toString(),
                null,
                al_police_clearance.toString(),
                al_inspection_report.toString(),
                al_goods_insurance.toString(),
                al_children_card.toString(),
                al_licence_img_front,
                al_licence_img_back,
                1.0,
                signUpData.getState(),
                signUpData.getDob(),
                signUpData.getYear(),
                signUpData.getColor(),
                signUpData.getGender(),
                driverLicenseDate,
                motorInsuImageDate,
                registrationDate,
                policeClearanceDate,
                inspectionReportDate,
                goodsInTransitDate,
                workWithChildrenDate,
                signUpData.getDriverBookingPreference(),
                signUpData.getVehicleBookingPreference());

        makeModel.observeOn(AndroidSchedulers.mainThread())
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
                                case 200:
                                    jsonObject=new JSONObject(value.body().string());
                                    String data = jsonObject.getString("data");
                                    JSONObject jsonObjectdata = new JSONObject(data);
                                    signUpDocumentView.signUpSuccess(jsonObjectdata.getString("driverId")
                                            ,signUpData.getPhone(),signUpData.getCountryCode());
                                    Utility.printLog("SignUpAPI : "+jsonObjectdata.getString("driverId"));
                                    break;

                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    Utility.toastMessage(context,jsonObject.getString("message"));
                                    Utility.printLog("SignUpAPI ....  "+jsonObject.getString("message"));
                                    break;
                            }

                            Utility.printLog("SignUpAPI : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("SignUpAPI : Catch :"+e.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        signUpDocumentView.hideProgress();
                    }
                });
    }

    @Override
    public void isPrivacypolicyAccepted(boolean isAccepted)
    {
        if(!isAccepted)
            signUpDocumentView.disableNext();
        else{
            isPrivacyPolicy=true;
            signUpDocumentView.checkBoxHandler();
        }

    }


    @Override
    public void compositeDisposableClear() {
        compositeDisposable.clear();
    }

    @Override
    public void cropImage(Intent data,String bucketFolder) {
        String path = data.getStringExtra(CropImage.IMAGE_PATH);
        if(path!=null) {
            if(Build.VERSION.SDK_INT>=N)
                VariableConstant.newProfileImageUri = FileProvider.getUriForFile(signUpDocumentActivity, signUpDocumentActivity.getPackageName(),VariableConstant.newFile);
            else
                VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
            if (Utility.isNetworkAvailable(context)) {
                amzonUpload(new File(path),context,bucketFolder);
            } else {
                Utility.BlueToast(context, context.getString(R.string.no_network));
            }
        }
    }

    @Override
    public void validateDocument(int count_licence_img, int count_vehicle_reg, int count_vehicle_insurance, int count_police_clearance, int count_inspection_report, int count_goods_insurance, int count_children_card,
                                 String date_licence, String date_police, String date_children, String date_registartion, String date_insurance, String date_inspection, String date_goodsInsurance) {

        if(count_licence_img>1 && !date_licence.isEmpty() &&
                count_vehicle_reg>0 && !date_registartion.isEmpty() && count_police_clearance>0 &&!date_police.isEmpty() &&
                count_vehicle_insurance>0 && !date_insurance.isEmpty()&& isPrivacyPolicy){

            signUpDocumentView.enableNext();
        }
        else
            signUpDocumentView.disableNext();
    }

    @Override
    public void setWebUrl(String from) {
        String url = BuildConfig.PRIVACY_POLICY.concat(preferencesHelper.getLanguage()).concat("_privacyPolicy.php");
        signUpDocumentView.openWebView(url);
    }

}
