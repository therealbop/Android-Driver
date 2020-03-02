package com.karry.authentication.vehicleTypeList;


import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferencesHelper;
import com.karry.network.NetworkService;
import com.karry.pojo.VehicleTypeList;
import com.karry.pojo.VehicleTypes;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.LOGIN_VEHICLE_TYPE_LIST;
import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;

class VehicleTypeListPresenter implements VehicleTypeListContract.VehicleTypeListPresenter {

    @Inject VehicleTypeListContract.VehicleTypeListView vehicleTypeListView;
    @Inject PreferencesHelper preferenceHelper;
    @Inject Activity context;
    @Inject NetworkService networkService;
    @Inject Gson gson;

    private VehicleTypes vehicleTypes;

    @Inject
    public VehicleTypeListPresenter() {
    }

    @Override
    public void getVehicleTypeList(Intent intent) {

        String data = intent.getStringExtra(LOGIN_VEHICLE_TYPE_LIST);
        VehicleTypes vehicleTypes = gson.fromJson(data,VehicleTypes.class);
        Utility.printLog("default vehicle Type : "+vehicleTypes.getVehicleTyps().size());
        if(vehicleTypes.getVehicleTyps().size()>0){
            ArrayList<VehicleTypeList> listDataReverse = new ArrayList<>();

            for(int i = vehicleTypes.getVehicleTyps().size();i>0;i--){
                listDataReverse.add(vehicleTypes.getVehicleTyps().get(i-1));
            }
            vehicleTypeListView.setListData(listDataReverse);
        }

    }

    @Override
    public void setActionBar() {
        vehicleTypeListView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        vehicleTypeListView.setTitle();
    }

    @Override
    public void confirmOnclick(ArrayList<String> al_vehicleType) {
        if(al_vehicleType.size()>0){
            String vehicleTypeId = "";
            for(int i=0;i<al_vehicleType.size();i++){
                vehicleTypeId+=al_vehicleType.get(i);
                vehicleTypeId+=",";

            }
            vehicleTypeId = vehicleTypeId.substring(0, vehicleTypeId.length() - 1);
            Utility.printLog("the vehicle type id id :"+vehicleTypeId);
            optInVehicleTypesApi(vehicleTypeId);
        }
    }

    @Override
    public void logoutOnclick() {
        logoutApi();
    }

    /**
     * <h1>logoutApi</h1>
     * <p>the API call for Logout</p>
     */
    private void logoutApi()
    {
        vehicleTypeListView.showProgress();
        Observable<Response<ResponseBody>> logoutApi=networkService.logout(preferenceHelper.getLanguage(),preferenceHelper.getSessionToken());
        logoutApi.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                VariableConstant.FCM_TOPIS = preferenceHelper.getFcmTopic();
                                VariableConstant.MQTT_TOPICS = preferenceHelper.getMqttTopic();
                                LanguagesList languagesList = preferenceHelper.getLanguageSettings();
                                preferenceHelper.clearSharedPredf();
                                preferenceHelper.setLanguageSettings(languagesList);
                                vehicleTypeListView.onSuccesLogout();
                                Utility.printLog("logoutApi : "+jsonObject.toString());

                            }else if(value.code()==RESPONSE_UNAUTHORIZED) {
                                VariableConstant.FCM_TOPIS = preferenceHelper.getFcmTopic();
                                VariableConstant.MQTT_TOPICS = preferenceHelper.getMqttTopic();
                                LanguagesList languagesList = preferenceHelper.getLanguageSettings();
                                preferenceHelper.clearSharedPredf();
                                preferenceHelper.setLanguageSettings(languagesList);
                                vehicleTypeListView.goToLogin(DataParser.fetchErrorMessage(value));
                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                vehicleTypeListView.onFailure(jsonObject.getString("message"));
                                Utility.printLog("logoutApi : "+jsonObject.toString());
                            }


                        }catch (Exception e)
                        {
                            Utility.printLog("logoutApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        vehicleTypeListView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        vehicleTypeListView.hideProgress();
                    }
                });
    }

    /**
     * <h1>optInVehicleTypesApi</h1>
     * <p>the API call for the vehicle type selected</p>
     */
    private void optInVehicleTypesApi(String vehicleTypeID)
    {
        vehicleTypeListView.showProgress();
        Observable<Response<ResponseBody>> logoutApi=networkService.optInVehicleTypes(preferenceHelper.getSessionToken(),preferenceHelper.getLanguage(),vehicleTypeID);
        logoutApi.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                preferenceHelper.setIsDriverLoggedIn(true);
                                preferenceHelper.setVehicleTypeData(null);
                                jsonObject=new JSONObject(value.body().string());
                                vehicleTypeListView.onSuccess();
                                Utility.printLog("optInVehicleTypesApi : "+jsonObject.toString());

                            }else if(value.code()==RESPONSE_UNAUTHORIZED) {
                                VariableConstant.FCM_TOPIS = preferenceHelper.getFcmTopic();
                                VariableConstant.MQTT_TOPICS = preferenceHelper.getMqttTopic();
                                LanguagesList languagesList = preferenceHelper.getLanguageSettings();
                                preferenceHelper.clearSharedPredf();
                                preferenceHelper.setLanguageSettings(languagesList);
                                vehicleTypeListView.goToLogin(DataParser.fetchErrorMessage(value));
                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                vehicleTypeListView.onFailure(jsonObject.getString("message"));
                                Utility.printLog("optInVehicleTypesApi : "+jsonObject.toString());
                            }


                        }catch (Exception e)
                        {
                            Utility.printLog("optInVehicleTypesApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        vehicleTypeListView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        vehicleTypeListView.hideProgress();
                    }
                });
    }
}
