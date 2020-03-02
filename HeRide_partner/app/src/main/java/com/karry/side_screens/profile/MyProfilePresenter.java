package com.karry.side_screens.profile;


import android.content.Context;

import com.google.gson.Gson;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.authentication.login.model.LanguagesPojo;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.heride.partner.BuildConfig;
import com.heride.partner.R;
import com.karry.side_screens.profile.profile_model.ProfileData;
import com.karry.utility.DataParser;
import com.karry.utility.TextUtil;
import com.karry.utility.Upload_file_AmazonS3;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.LOGIN;
import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;

/**
 * <h1>MyProfilePresenter</h1>
 * <p>svg_profile fragment which implements the Profile</p>
 */
public class MyProfilePresenter implements MyProfileContract.Presenter{


    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject NetworkService networkService;
    @Inject CompositeDisposable compositeDisposable;
    @Inject Context mActivity;
    @Inject Upload_file_AmazonS3 amazonS3;
    @Inject Gson gson;
    @Inject @Named(LOGIN) ArrayList<LanguagesList> languagesLists = new ArrayList<>();
    @Inject MyProfilePresenter() {
    }

    MyProfileContract.View view;

    @Override
    public void getProfileDetails()
    {
        view.setLanguage(preferenceHelperDataSource.getLanguageSettings().getName(),false);
        view.setProfileImage(preferenceHelperDataSource.getProfilePic());

        view.startProgressBar();
        Utility.printLog("MYProfile responseee : "+preferenceHelperDataSource.getLanguage()+"\n"+
                preferenceHelperDataSource.getSessionToken());

        Observable<Response<ResponseBody>> responseObservable = networkService.getProfile(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getSessionToken());

        responseObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        Utility.printLog("MYProfile responseee : "+value);
                        view.stopProgressBar();
                        try {
                            switch (value.code())
                            {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:

                                    ProfileData profileData=gson.fromJson(DataParser.fetchDataString(value),ProfileData.class);
                                    preferenceHelperDataSource.setProfilePic(profileData.getDriverDetail().getProfilePic());
                                    view.setProfileImage(preferenceHelperDataSource.getProfilePic());
                                    view.setProfileDetails(profileData);

                                    view.driverPreferenceDataForAdapter(profileData.getDriverDetail().getDriverPreferencesArr());
                                    view.vehiclePreferenceDataForAdapter(profileData.getVehicleDetail().getVehiclePreferencesArr());

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferenceHelperDataSource.getFcmTopic();
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    view.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    JSONObject jsonObject=new JSONObject(value.errorBody().string());
                                    Utility.printLog();
                                    view.onFailure(jsonObject.getString("message"));
                                    Utility.printLog("MYProfile response : "+jsonObject.toString());
                                    break;
                            }

                        }catch (Exception e)
                        {
                            /*view.onFailure();*/
                            if(MyProfileFrag.profileFrag)
                                view.stopProgressBar();
                            e.printStackTrace();
                            Utility.printLog("MYProfile response : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(MyProfileFrag.profileFrag)
                            view.stopProgressBar();
                        view.onFailure();
                        Utility.printLog("MYProfile response : onError :"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if(MyProfileFrag.profileFrag)
                            view.stopProgressBar();
                    }
                });
    }

    @Override
    public void validateField(String firstName, String lastName, String profilePic)
    {
        if(TextUtil.isEmpty(firstName))
        {
            view.onFirstNameError();
        }
        else if(TextUtil.isEmpty(lastName))
        {
            view.onLastNameError();
        }
        else
        {
            updateProfileDetails(firstName,lastName,profilePic);
        }
    }

    @Override
    public void attachView(Object view) {
        this.view = (MyProfileContract.View) view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void logout() {
        logoutApi();
    }

    /**
     * <h1>logoutApi</h1>
     * <p>Logout API</p>
     */
    private void logoutApi()
    {
        if(MyProfileFrag.profileFrag)
            view.startProgressBar();
        Observable<Response<ResponseBody>> logoutApi=networkService.logout(preferenceHelperDataSource.getLanguage(),preferenceHelperDataSource.getSessionToken());
        logoutApi.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        view.stopProgressBar();
                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                VariableConstant.FCM_TOPIS = preferenceHelperDataSource.getFcmTopic();
                                LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                preferenceHelperDataSource.clearSharedPredf();
                                preferenceHelperDataSource.setLanguageSettings(languagesList);
                                view.onSuccesLogout();

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onFailure(jsonObject.getString("message"));
                            }
                            Utility.printLog("logoutApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("logoutApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(MyProfileFrag.profileFrag)
                            view.stopProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        if(MyProfileFrag.profileFrag)
                            view.stopProgressBar();
                    }
                });
    }

    private void updateProfileDetails(String firstName, String lastName, String profilePic)
    {
        view.startProgressBar();

        Observable<Response<ResponseBody>> responseObservable = networkService.updateProfile(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getSessionToken(),
                firstName,lastName,profilePic);

        responseObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        JSONObject jsonObject;
                        Utility.printLog("MYProfile responseee : "+value);
                        try {
                            switch (value.code())
                            {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    jsonObject=new JSONObject(value.body().string());
                                    preferenceHelperDataSource.setMyName(firstName+" "+ lastName);
                                    preferenceHelperDataSource.setProfilePic(profilePic);
                                    view.onSuccessProfileUpdate(jsonObject.getString("message"));
                                    break;

                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    Utility.printLog();
                                    view.onFailure(jsonObject.getString("message"));
                                    Utility.printLog("MYProfile response : "+jsonObject.toString());
                                    break;
                            }

                        }catch (Exception e)
                        {
                            view.onFailure();
                            e.printStackTrace();
                            Utility.printLog("MYProfile response : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(MyProfileFrag.profileFrag)
                            view.stopProgressBar();
                        view.onFailure();
                        Utility.printLog("MYProfile response : onError :"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if(MyProfileFrag.profileFrag)
                            view.stopProgressBar();
                    }
                });
    }

    @Override
    public void uploadToAmazon(File file) {

        String Bucket_folder = VariableConstant.PROFILE_PIC;
        final String imageUrl = BuildConfig.AMAZON_BASE_URL+ VariableConstant.BUCKET_NAME+"/"+ Bucket_folder +"/"+file.getName().replace(" ","");
        Utility.printLog("MYPROFILE Before upload"+imageUrl);
        view.onImageUploadedResult(imageUrl);

        amazonS3.Upload_data(VariableConstant.BUCKET_NAME, Bucket_folder +"/"+file.getName().replace(" ",""), file, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String url)
            {
                Utility.printLog("MYPROFILE "+url);
            }

            @Override
            public void sucess(String url, String type) {
                Utility.printLog("MYPROFILE "+url);
            }

            @Override
            public void error(String errormsg)
            {
                Utility.printLog("MYPROFILE "+errormsg);
            }
        });
    }

    @Override
    public void showKeyboard() {
        view.showSoftKeyboard();
    }

    @Override
    public void checkRTL() {
        if(preferenceHelperDataSource.getLanguageSettings()!=null &&
                preferenceHelperDataSource.getLanguageSettings().getLangDirection()!=0){
            view.supportRTLpencil();
        }else {
            view.notSupportRTLpencil();
        }
    }


    @Override
    public void getLanguages() {
        if(Utility.isNetworkAvailable(mActivity)) {
            view.startProgressBar();

            Observable<Response<ResponseBody>> getLanguages = networkService.getLanguages();

            getLanguages.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            view.stopProgressBar();

                            try {
                                switch (value.code()) {
                                    case VariableConstant.RESPONSE_CODE_SUCCESS:

                                        LanguagesPojo languagesListModel = gson.fromJson(value.body().string(),LanguagesPojo.class);
                                        languagesLists.clear();
                                        languagesLists.addAll(languagesListModel.getData());
                                        boolean isLanguage = false;
                                        for(LanguagesList languagesList : languagesLists)
                                        {
                                            if(preferenceHelperDataSource.getLanguageSettings().getCode().equals(languagesList.getCode()))
                                            {
                                                isLanguage = true;
                                                view.setLanguageDialog(languagesLists.indexOf(languagesList));
                                                break;
                                            }
                                        }
                                        if(!isLanguage)
                                            view.setLanguageDialog(-1);


                                        /*Utility.printLog("getLanguages : " +resp);
                                        LanguagesPojo languagesPojo = gson.fromJson(resp, LanguagesPojo.class);
                                        if(languagesPojo.getData().size()>0){
                                            view.setLanguageDialog(languagesPojo.getData());
                                        }*/
                                        break;

                                    default:
                                        view.onFailure(DataParser.fetchErrorMessage(value), mActivity.getResources().getString(R.string.message));
                                        break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Utility.printLog("getLanguages : Catch :" + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.stopProgressBar();
                            Utility.printLog("getLanguages : onError :" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            view.onFailure(mActivity.getResources().getString(R.string.no_network), mActivity.getResources().getString(R.string.alert));
        }

    }

    @Override
    public void languageChanged(String langCode, String langName, int dir) {
        preferenceHelperDataSource.setLanguage(langCode);
        preferenceHelperDataSource.setLanguageSettings(new LanguagesList(langCode,langName, dir));
        view.setLanguage(langName,true);
    }
}
