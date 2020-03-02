package com.karry.authentication.vehiclelist;

import android.app.Activity;

import com.google.gson.Gson;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferencesHelper;
import com.heride.partner.R;
import com.karry.network.NetworkService;
import com.karry.pojo.SigninDriverVehicle;
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

import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;

/**
 * <h1>VehicleListPresenter</h1>
 * <p>Implementation of thr Vehicle list Activity</p>
 */

public class VehicleListPresenter implements VehicleListContract.VehicleListPresenter {

    @Inject   VehicleListContract.VehicleListView view;
    @Inject   PreferencesHelper preferenceHelper;
    @Inject   Activity context;
    @Inject   NetworkService networkService;
    @Inject   Gson gson;

    private ArrayList<SigninDriverVehicle>list=new ArrayList<>();

    @Inject
    VehicleListPresenter() {
        VariableConstant.VECHICLESELECTED = false;
    }

    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        view.setTitle();
    }

    /*@Override
    public void getList() {

        list.clear();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(preferenceHelper.getVehicles());

            for (int i = 0; i < jsonArray.length(); i++) {
                SigninDriverVehicle signinDriverVehicle = gson.fromJson(jsonArray.getString(i), SigninDriverVehicle.class);
                list.add(signinDriverVehicle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        view.setListData(list);
        view.notifyAdapter();
    }*/

    @Override
    public void confirmOnclick() {
        if (VariableConstant.VECHICLESELECTED) {
            setDefaultVehicle();
        } else {
            Utility.BlueToast(context,context.getResources().getString(R.string.selectVechicle));
        }
    }

    @Override
    public void logoutOnclick() {
//        view.logout(preferenceHelper,networkService);
        logoutApi();

    }

    /**
     * <h1>logoutApi</h1>
     * <p>the API call for Logout</p>
     */
    private void logoutApi()
    {
        view.showProgress();
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
                                view.onSuccesLogout();
                                Utility.printLog("logoutApi : "+jsonObject.toString());
                            }else if(value.code()==RESPONSE_UNAUTHORIZED) {
                                VariableConstant.FCM_TOPIS = preferenceHelper.getFcmTopic();
                                VariableConstant.MQTT_TOPICS = preferenceHelper.getMqttTopic();
                                LanguagesList languagesList = preferenceHelper.getLanguageSettings();
                                preferenceHelper.clearSharedPredf();
                                preferenceHelper.setLanguageSettings(languagesList);
                                view.goToLogin(DataParser.fetchErrorMessage(value));
                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onFailure(jsonObject.getString("message"));
                                Utility.printLog("logoutApi : "+jsonObject.toString());
                            }


                        }catch (Exception e)
                        {
                            Utility.printLog("logoutApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgress();
                    }
                });
    }

    /**
     * <h1>setDefaultVehicle</h1>
     * <p>API call for select the vehicle</p>
     */
    private void setDefaultVehicle(){
        if(view!=null)
            view.showProgress();

        Observable<Response<ResponseBody>> defaultVehicle=networkService.defaultVehicle(
                preferenceHelper.getSessionToken(),
                preferenceHelper.getLanguage(),
                /*preferenceHelper.getDriverID(),*/
                VariableConstant.VECHICLEID
                /*VariableConstant.VECHICLE_TYPE_ID,*/
                /*null*/);

        Utility.printLog(" token : "+preferenceHelper.getSessionToken()+"\n"+
                " Master Id : "+preferenceHelper.getDriverID()+"\n"+
                " VECHICLE ID : "+VariableConstant.VECHICLEID+"\n"+
                " VECHICLE_TYPE_ID : "+VariableConstant.VECHICLE_TYPE_ID);

        defaultVehicle.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                preferenceHelper.setLoginResponseData(null);
                                jsonObject=new JSONObject(value.body().string());

                                Utility.printLog("default vehicle response1 : "+jsonObject.toString());
                                if(jsonObject.getString("data")!=null){
                                    jsonObject=new JSONObject(jsonObject.getString("data"));
                                    VehicleTypes vehicleTypes = gson.fromJson(jsonObject.toString(),VehicleTypes.class);
                                    Utility.printLog("default vehicle response1 : "+vehicleTypes.getVehicleTyps().size());
                                    preferenceHelper.setVehicleTypeData(vehicleTypes);
                                    if(vehicleTypes.getVehicleTyps().size()>1){
                                        view.onSuccess(jsonObject.toString());
                                    }
                                    else {
                                        preferenceHelper.setIsDriverLoggedIn(true);
                                        view.onSuccessMain();
                                    }
                                }



                                /*view.onSuccess();*/

                            } else if(value.code()==RESPONSE_UNAUTHORIZED) {
                                VariableConstant.FCM_TOPIS = preferenceHelper.getFcmTopic();
                                VariableConstant.MQTT_TOPICS = preferenceHelper.getMqttTopic();
                                LanguagesList languagesList = preferenceHelper.getLanguageSettings();
                                preferenceHelper.clearSharedPredf();
                                preferenceHelper.setLanguageSettings(languagesList);
                                view.goToLogin(DataParser.fetchErrorMessage(value));
                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onError(jsonObject.getString("message"));
                            }

                        }catch (Exception e)
                        {
                            Utility.printLog("defaultVehicle : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });

    }

}
