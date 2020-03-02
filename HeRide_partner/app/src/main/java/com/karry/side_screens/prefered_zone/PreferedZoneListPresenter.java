package com.karry.side_screens.prefered_zone;


import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferencesHelper;
import com.karry.network.NetworkService;
import com.karry.pojo.AreaZone.AreaZone;
import com.karry.pojo.AreaZone.AreaZoneDataZones;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;
import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;

public class PreferedZoneListPresenter implements PreferedZoneListContract.PreferedZonePresenter{

    @Inject   PreferedZoneListContract.PreferedZoneView preferedZoneView;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject   Context context;
    @Inject
    NetworkService networkService;
    private ArrayList<List<LatLng>> Polygon = new ArrayList<>();
    private AreaZone areaZone;

    @Inject
    PreferedZoneListPresenter(){

    }

    @Override
    public void setActionBar() {
        preferedZoneView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        preferedZoneView.setTitle();
        getAreaZone();
    }


    @Override
    public void getAreaZone() {
        preferedZoneView.showProgress();

        Observable<Response<ResponseBody>> getAreaZone = networkService.getAreaZone(
                preferencesHelper.getSessionToken(),
                preferencesHelper.getLanguage(),
                String.valueOf(preferencesHelper.getCurrLatitude()),
                String.valueOf(preferencesHelper.getCurrLongitude()));

        getAreaZone.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            switch (value.code()) {

                                case RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("getAreaZone success response is  : \n" +resp);
                                    Gson gson = new Gson();

                                    areaZone = gson.fromJson(resp,AreaZone.class);
                                    Utility.printLog("getAreaZone success response is  : \n" +areaZone.getData().getAreaZones().size());
                                    preferedZoneView.zoneListForAdapter(areaZone);


                                    boolean checkflag = false;
                                    for(int i = 0;i<(areaZone.getData().getAreaZones().size());i++){
                                        if(areaZone.getData().getAreaZones().get(i).isPrefredZone()) {
                                            checkflag = true;
                                            break;
                                        }else{
                                            checkflag = false;
                                        }
                                    }

                                    if(checkflag){
                                        preferedZoneView.enableReset();
                                    }else {
                                        preferedZoneView.disableReset();
                                    }

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    preferedZoneView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    Utility.BlueToast(context, DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(preferedZoneView!=null)
                            preferedZoneView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(preferedZoneView!=null )
                            preferedZoneView.hideProgress();
                    }
                });
    }

    @Override
    public void checkPreferenceCheck(AreaZoneDataZones areaZoneDataZones, int position) {
        /*if(position!=-1){
            if( areaZone.getData().getAreaZones().get(position).isPrefredZone())
                areaZone.getData().getAreaZones().get(position).setPrefredZone(false);
            else
                areaZone.getData().getAreaZones().get(position).setPrefredZone(true);

            PreferedZoneListActivity.selectedPosition = -1;
            preferedZoneView.areaZoneRefresh(areaZone);
        }*/

        areaZone.getData().getAreaZones().get(position).setPrefredZone(areaZoneDataZones.isPrefredZone());
    }

    private String findAreaZoneId(){


        final String[] areaZoneId = {""};
        for(int i = 0;i<(areaZone.getData().getAreaZones().size());i++){
            if(areaZone.getData().getAreaZones().get(i).isPrefredZone()) {
                if(!areaZoneId[0].matches(""))
                    areaZoneId[0] = areaZoneId[0].concat(",");
                areaZoneId[0] = areaZoneId[0].concat(areaZone.getData().getAreaZones().get(i).getId());
            }
        }
        Utility.printLog("the prefernce list is : "+areaZoneId[0]);

        return areaZoneId[0];
    }

    @Override
    public void patchAreaZone(String preferenceType) {


        String areaZoneId = findAreaZoneId();

        preferedZoneView.showProgress();

        Observable<Response<ResponseBody>> getAreaZone = networkService.patchPreferedAreaZone(
                preferencesHelper.getLanguage(),
                preferencesHelper.getSessionToken(),
                areaZoneId,
                preferenceType);

        getAreaZone.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            switch (value.code()) {

                                case RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("getAreaZone success response is  : \n" +resp);
                                    preferedZoneView.preferenceZoneSeletionSuccess();


                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    preferedZoneView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    Utility.BlueToast(context, DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(preferedZoneView!=null)
                            preferedZoneView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(preferedZoneView!=null )
                            preferedZoneView.hideProgress();
                    }
                });
    }

    @Override
    public void validatePreferenceZone() {
        boolean checkflag = false;
        for(int i = 0;i<(areaZone.getData().getAreaZones().size());i++){
            if(areaZone.getData().getAreaZones().get(i).isPrefredZone()) {
                checkflag = true;
                break;
            }else{
                checkflag = false;
            }
        }

        if(checkflag){
            preferedZoneView.zoneSelected();
        }else {
            preferedZoneView.emptyZoneSelected();
        }
    }

    @Override
    public void deleteMasterPreferedareazone() {

        preferedZoneView.showProgress();

        Observable<Response<ResponseBody>> getAreaZone = networkService.deletePreferedAreaZone(
                preferencesHelper.getLanguage(),
                preferencesHelper.getSessionToken());

        getAreaZone.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            switch (value.code()) {

                                case RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("deletePreferedAreaZone success response is  : \n" +resp);
                                    preferedZoneView.preferenceZoneSeletionSuccess();


                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    preferedZoneView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    Utility.BlueToast(context, DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(preferedZoneView!=null)
                            preferedZoneView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(preferedZoneView!=null )
                            preferedZoneView.hideProgress();
                    }
                });
    }
}
