package com.karry.authentication.signup.SignUpVehicle;

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
import com.karry.network.CarApiService;
import com.karry.network.NetworkService;
import com.karry.pojo.Signup.City;
import com.karry.pojo.Signup.CityResponse;
import com.karry.pojo.Signup.ColorData;
import com.karry.pojo.Signup.MakeData;
import com.karry.pojo.Signup.MakeDataPojo;
import com.karry.pojo.Signup.MakeModelPojo;
import com.karry.pojo.Signup.ModelData;
import com.karry.pojo.Signup.ModelDataPojo;
import com.karry.pojo.Signup.PreferenceData;
import com.karry.pojo.Signup.Services;
import com.karry.pojo.Signup.TypeAndSpecialitiesData;
import com.karry.pojo.Signup.TypeAndSpecialitiesPojo;
import com.karry.pojo.Signup.YearData;
import com.karry.utility.DataParser;
import com.karry.utility.TextUtil;
import com.karry.utility.Upload_file_AmazonS3;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import eu.janmuller.android.simplecropimage.CropImage;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.os.Build.VERSION_CODES.N;
import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.SIGNUP_DATA;

/**
 * <h1>SignupVehiclePresenter</h1>
 * <p>Implementation for the Vehicle SignUp</p>
 */
public class SignupVehiclePresenter implements SignupVehicleContract.SignupVehiclePresenter {

    private ArrayList<City> cityArrayList=new ArrayList<>();
    private ArrayList<TypeAndSpecialitiesData> typeAndSpecialitiesData = new ArrayList<>();
    private ArrayList<Services> services;

    private ArrayList<MakeData> makeModelData = new ArrayList<>();
    private ArrayList<ModelData> models = new ArrayList<>();

    private String selectedCityID;
    //    private String selectedTypeID;
    private String selectedServiceID;
    private String selectedVehMakeID;
//    private String selectedVehModelID;

    private ArrayList<ColorData> colorArrayList = new ArrayList<>();
    private ArrayList<YearData> yearList = new ArrayList<>();
    private String yearSelected;
    private SignUpData signUpData;
    private int serviceType = 2;

    @Inject SignupVehicleContract.SignupVehicleView signupVehicleView;
    @Inject NetworkService networkService;
    @Inject CarApiService carApiService;
    @Inject SignupVehModel signupVehModelCheck;
    @Inject PreferencesHelper preferencesHelper;
    @Inject Context context;
    @Inject SignupVehicleActivity signupVehicleActivity;
    @Inject Gson gson;

    @Inject
    SignupVehiclePresenter() {
    }

    @Override
    public void getPersonalData(Bundle mBundle) {
        signUpData = (SignUpData) mBundle.getSerializable(SIGNUP_DATA);
    }

    @Override
    public void setActionBar() {
        signupVehicleView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        signupVehicleView.setTitle();
    }

    @Override
    public void cityOnClick() {
        if (cityArrayList != null && cityArrayList.size() > 0) {
            signupVehicleView.getCitylist(cityArrayList);
        } else {
            cityApi(signUpData.getState());
        }
    }

    /**
     * <h1>cityApi</h1>
     * <p>which call the API for the list of City.</p>
     * @param state : the state pass for get the list of city contain.
     */
    private void cityApi(String state){
        signupVehicleView.showProgress();
        Observable<Response<ResponseBody>> city=networkService.city(
                preferencesHelper.getLanguage(),state);

        city.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        signupVehicleView.hideProgress();
                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                CityResponse cityResponse = gson.fromJson(jsonObject.toString(), CityResponse.class);

                                signupVehicleView.getCitylist(cityResponse.getData());

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());

                            }
                            Utility.printLog("city : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("city : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        signupVehicleView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        signupVehicleView.hideProgress();
                    }
                });
    }

    @Override
    public void onCitySelected(Intent data) {
        if (cityArrayList != null) {
            cityArrayList.clear();

        }
        cityArrayList = (ArrayList<City>) data.getSerializableExtra(DATA);
        for (City city : cityArrayList) {
            if (city.isSelected()) {

                if(selectedCityID!=null && !selectedCityID.matches(city.get_id())){
                    signupVehicleView.clearTypeService();
                    clearTypeServiceID();
                }
                signupVehModelCheck.setCheckCity(true);
                signupVehicleView.setCityText(city.getCity(),city.get_id());
                selectedCityID=city.get_id();
                validateToNext();

            }

        }
    }

    @Override
    public void typeOnClick() {
        if (typeAndSpecialitiesData != null && typeAndSpecialitiesData.size() > 0) {
            signupVehicleView.getTypelist(typeAndSpecialitiesData);
        } else {
            vehicleTypeAndSpecialitiesAPI();
        }
    }

    private void vehicleTypeAndSpecialitiesAPI(){
        Observable<Response<ResponseBody>> vehicleTypeAndSpecialities=networkService.vehicleTypeAndSpecialities(
                preferencesHelper.getLanguage(), selectedCityID);

        vehicleTypeAndSpecialities.observeOn(AndroidSchedulers.mainThread())
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
                                TypeAndSpecialitiesPojo typeAndSpecialitiesPojo = gson.fromJson(jsonObject.toString(), TypeAndSpecialitiesPojo.class);

                                signupVehicleView.getTypelist(typeAndSpecialitiesPojo.getData());

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());

                            }
                            Utility.printLog("vehicleTypeAndSpecialities : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("vehicleTypeAndSpecialities : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void preferencesAPI(String type_ID){
        Utility.printLog("  selectedCityID "+selectedCityID+"\n"+type_ID+"\n"+serviceType);
        signupVehicleView.showProgress();
        Observable<Response<ResponseBody>> preferences=networkService.preferences(
                preferencesHelper.getLanguage(), selectedCityID, type_ID, serviceType );

        preferences.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        signupVehicleView.hideProgress();

                        try {
                            JSONObject jsonObject;

                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                PreferenceData preferenceData = gson.fromJson(jsonObject.get("data").toString(),PreferenceData.class);

                                signupVehicleView.driverPreferenceDataForAdapter(preferenceData.getDriverPreferencesArr());
                                signupVehicleView.vehiclePreferenceDataForAdapter(preferenceData.getVehiclePreferences());


                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());

                            }
                            Utility.printLog("preferences : "+jsonObject.toString());
                            Utility.printLog("preferences : "+serviceType);

                        }catch (Exception e)
                        {
                            Utility.printLog("preferences : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        signupVehicleView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        signupVehicleView.hideProgress();
                    }
                });


    }


    @Override
    public void onTypeSelected(Intent data, String Type_ID) {
        if (typeAndSpecialitiesData != null) {
            typeAndSpecialitiesData.clear();
            services = new ArrayList<>();
        }
        typeAndSpecialitiesData = (ArrayList<TypeAndSpecialitiesData>) data.getSerializableExtra(DATA);
        for (TypeAndSpecialitiesData typeAndSpecialitiesData : typeAndSpecialitiesData) {
            if (typeAndSpecialitiesData.isSelected()) {

                signupVehModelCheck.setCheckType(true);
                signupVehicleView.setTypeText(typeAndSpecialitiesData.getName(),typeAndSpecialitiesData.getId());
                services.addAll(typeAndSpecialitiesData.getServices());
                validateToNext();

                if(services.size()>0)
                    autoServiceSelect(services,Type_ID);


            }
        }
    }


    /**
     * <h1>autoServiceSelect</h1>
     * <p>this mehod is used for select the first service from the list, when select the Type.</p>
     * @param service : which is the list of services providing for the Type which selected.
     */
    private void autoServiceSelect(ArrayList<Services> service,String type_ID)
    {

        /*preferencesAPI(type_ID);*/

        if(service.size()>0) {
            signupVehModelCheck.setCheckService(true);
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray;
            serviceType = Integer.parseInt(service.get(0).getServiceType());


            try {
                jsonObject.put("serviceName", service.get(0).getServiceName());
                jsonObject.put("serviceType", Integer.parseInt(service.get(0).getServiceType()));
                jsonObject.put("serviceId", service.get(0).getServiceId());
                jsonObject.put("isMeterBookingEnable", service.get(0).isMeterBookingEnable());
                jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                selectedServiceID = service.get(0).toString();
                signupVehicleView.setServiceText(services.get(0).getServiceName(), jsonArray.toString());
                validateToNext();

            } catch (JSONException e) {
                Utility.printLog("caught : " + e.getMessage());
            }
        }
    }

    @Override
    public void serviceOnClick() {
        if (services != null && services.size() > 1) {
            signupVehicleView.getServicelist(services);
        }
    }

    @Override
    public void onServiceSelected(Intent data,String Type_ID) {

        if (data != null) {
            services.clear();
        }

        JSONArray jsonArray = new JSONArray();
        String selectedServiceName="";

        services = (ArrayList<Services>) data.getSerializableExtra(DATA);
        for (Services services : services) {
            if (services.isSelected()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("serviceName", services.getServiceName());
                    jsonObject.put("serviceType", Integer.parseInt(services.getServiceType()));
                    jsonObject.put("serviceId", services.getServiceId());
                    jsonObject.put("isMeterBookingEnable", services.isMeterBookingEnable());
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    Utility.printLog("caught : " + e.getMessage());
                }
                if(selectedServiceName.isEmpty()) {
                    serviceType = Integer.parseInt(services.getServiceType());
                    selectedServiceName = services.getServiceName();
                }
                else {
                    serviceType = 3;
                    selectedServiceName = selectedServiceName.concat(",").concat(services.getServiceName());
                }

                preferencesAPI(Type_ID);
                signupVehModelCheck.setCheckService(true);
                selectedServiceID=services.toString();
                Utility.printLog("the selected service is : "+ selectedServiceName);
                Utility.printLog("the selected service isss : "+ jsonArray.toString());

            }
        }

        signupVehicleView.setServiceText(selectedServiceName,jsonArray.toString());
        validateToNext();
    }

    @Override
    public void yearOnClick() {
        if (yearList != null && yearList.size() > 0) {
            signupVehicleView.getYearList(yearList);
        } else {
            yearAPI();
        }
    }


    @Override
    public void onYearSelected(Intent data) {
        if (yearList != null) {
            yearList.clear();
            makeModelData = new ArrayList<>();
            yearChanged();
        }

        yearList = (ArrayList<YearData>) data.getSerializableExtra(DATA);
        for (YearData yearList : yearList) {
            if (yearList.isSelected()) {

                if(yearSelected!=null && !yearSelected.matches(yearList.getYear())){
                    signupVehicleView.clearMakeModel();
                    yearChanged();
                }

                yearSelected = yearList.getYear();
                signupVehModelCheck.setCheckYear(true);
                signupVehicleView.setYear(yearList.getYear());
            }

        }
    }

    @Override
    public void colorOnClick() {
        String[] colorList = context.getResources().getStringArray(R.array.colors);
        for (String aColorList : colorList) {
            ColorData colorData = new ColorData();
            colorData.setColor(aColorList);
            colorArrayList.add(colorData);
        }
        signupVehicleView.getColorList(colorArrayList);
    }

    @Override
    public void onColorSelected(Intent data) {

        colorArrayList = (ArrayList<ColorData>) data.getSerializableExtra(DATA);
        for (ColorData colorData : colorArrayList) {
            if (colorData.isSelected()) {
                signupVehModelCheck.setCheckColor(true);
                signupVehicleView.setColor(colorData.getColor());
                validateToNext();
            }
        }
    }

    @Override
    public void makeOnClick(String yearSelected) {
        if (makeModelData != null && makeModelData.size() > 0) {
            signupVehicleView.getMakelist(makeModelData);
        } else {
//            makeModelAPI();
            makeAPI(yearSelected);
        }
    }

    public void makeModelAPI(){
        signupVehicleView.showProgress();
        Observable<Response<ResponseBody>> makeModel=networkService.makeModel(
                preferencesHelper.getLanguage());

        makeModel.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        signupVehicleView.hideProgress();
                        try {
                            JSONObject jsonObject;

                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                MakeModelPojo makeModelPojo = gson.fromJson(jsonObject.toString(), MakeModelPojo.class);
//                                signupVehicleView.getMakelist(makeModelPojo.getData());

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());

                            }
                            Utility.printLog("makeModel : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("makeModel : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        signupVehicleView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        signupVehicleView.hideProgress();
                    }
                });
    }

    @Override
    public void onmakeSelected(Intent data) {
        if (makeModelData != null) {
            makeModelData.clear();
            models = new ArrayList<>();
        }
        makeModelData = (ArrayList<MakeData>) data.getSerializableExtra(DATA);
        for (MakeData makeModelData : makeModelData) {
            if (makeModelData.isSelected()) {
                if(selectedVehMakeID!=null && !selectedVehMakeID.matches(makeModelData.getMakeName())){
                    signupVehicleView.clearModel();
                    clearModelID();
                }
                signupVehModelCheck.setCheckMake(true);
                signupVehicleView.setMakeText(makeModelData.getMakeName(),makeModelData.getMakeId());
                selectedVehMakeID=makeModelData.getMakeId();
                validateToNext();
            }

        }
    }

    @Override
    public void modelOnClick(String year, String make) {

        if (models != null && models.size() > 0) {
            signupVehicleView.getModellist(models);
        } else {
            modelAPI(year,make);
        }

        if (models != null && models.size() > 0) {
            signupVehicleView.getModellist(models);
        }
    }

    @Override
    public void onModelSelected(Intent data) {
        if (data != null) {
            models.clear();
        }
        models = (ArrayList<ModelData>) data.getSerializableExtra(DATA);
        for (ModelData model : models) {
            if (model.isSelected()) {
                signupVehModelCheck.setCheckModel(true);
                signupVehicleView.setModelText(model.getModelName(),model.getModelId());
                validateToNext();

            }
        }
    }

    @Override
    public void validateToNext() {
        Utility.printLog("the vehicle validation : " +
                signupVehModelCheck.isCheckPlate()+" " +
                signupVehModelCheck.isCheckCity()+" "+
                signupVehModelCheck.isCheckType()+" "+
                signupVehModelCheck.isCheckService()+" "+
                signupVehModelCheck.isCheckMake()+" "+
                signupVehModelCheck.isCheckModel()+" "+
                signupVehModelCheck.isCheckYear());

        if(signupVehModelCheck.isCheckPlate() &&
                signupVehModelCheck.isCheckCity() &&
                signupVehModelCheck.isCheckType() &&
                signupVehModelCheck.isCheckService() &&
                signupVehModelCheck.isCheckMake() &&
                signupVehModelCheck.isCheckModel() &&
                signupVehModelCheck.isCheckYear()&&
                signupVehModelCheck.isCheckColor() /*&& signupVehModelCheck.isCheckvehicleImage()*/){
            signupVehicleView.enableNext();
        }else {
            signupVehicleView.disableNext();
        }
    }

    @Override
    public void validatePlateNo(String plateNo) {
        if(!TextUtil.isEmpty(plateNo))
        {
            validatePlateApi(plateNo);
        }
        else {
            signupVehModelCheck.setCheckPlate(false);
            signupVehicleView.InvalidPlateNo();
        }
    }

    public void validatePlateApi(String plateNo)
    {
        signupVehicleView.showProgress();
        Observable<Response<ResponseBody>> make=networkService.platNumberValidation(preferencesHelper.getLanguage(),
                plateNo);

        make.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        switch (value.code())
                        {
                            case 200:
                                signupVehModelCheck.setCheckPlate(true);
                                signupVehicleView.validPlateNo();
                                break;
                            default:
                                signupVehModelCheck.setCheckPlate(false);
                                signupVehicleView.inValidPlateNoAPI(DataParser.fetchErrorMessage(value));
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        signupVehModelCheck.setCheckPlate(false);
                        signupVehicleView.InvalidPlateNo();

                    }

                    @Override
                    public void onComplete() {
                        signupVehicleView.hideProgress();
                    }
                });
    }

    @Override
    public void amzonUpload(File file, Context context, String Bucket_folder) {
        signupVehicleView.showProgress();

        Upload_file_AmazonS3 amazonS3 = Upload_file_AmazonS3.getInstance(context);
        final String imageUrl = BuildConfig.AMAZON_BASE_URL+BuildConfig.BUCKET_NAME+"/"+ Bucket_folder +"/"+file.getName().replace(" ","");
        amazonS3.Upload_data(BuildConfig.BUCKET_NAME, Bucket_folder +"/"+file.getName().replace(" ",""), file, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String url)
            {
                signupVehModelCheck.setCheckvehicleImage(true);
                validateToNext();
                signupVehicleView.hideProgress();
                signupVehicleView.amazonUploadSuccess(imageUrl);

            }

            @Override
            public void sucess(String url, String type) {
                signupVehModelCheck.setCheckvehicleImage(true);
                validateToNext();
                signupVehicleView.hideProgress();
                Utility.printLog("babyy" +url);
            }

            @Override
            public void error(String errormsg)
            {
                signupVehModelCheck.setCheckvehicleImage(false);
                validateToNext();
                signupVehicleView.hideProgress();
                Log.d("baby","error");
            }
        });
    }

    /**
     * <h1>clearTypeServiceID</h1>
     * <p>if the city change then need to set the type and service selection false.</p>
     */
    private void clearTypeServiceID(){
        signupVehModelCheck.setCheckType(false);
        signupVehModelCheck.setCheckService(false);
    }

    /**
     * <h1>clearModelID</h1>
     * <p>which clear the model if change the vehicle Make </p>
     */
    private void clearModelID()
    {
        signupVehModelCheck.setCheckModel(false);
    }

    /**
     * <h1>yearAPI</h1>
     * <p>which is the API call for find the list of year that the vehicle make start and till the present year.
     * depends on the response setting the list of year.</p>
     */
    private void yearAPI()
    {
        signupVehicleView.showProgress();
//        Observable<Response<ResponseBody>> year=carApiService.year();
        Observable<Response<ResponseBody>> year=networkService.getYears(preferencesHelper.getLanguage());

        year.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            String res_year = value.body().string();
                            Utility.printLog("year response : "+res_year);
                            JSONObject jsonObject=new JSONObject(res_year);
                            YearData temp_yearData = gson.fromJson(jsonObject.toString(), YearData.class);

                            switch (temp_yearData.getData().size()){

                                case 0:
                                    break;
                                case 1:
                                    YearData yearData = new YearData();
                                    yearData.setYear(temp_yearData.getData().get(0));
                                    yearData.setSelected(false);
                                    yearList.add(yearData);
                                    break;
                                default:
                                    for (int i = 0; i < temp_yearData.getData().size(); i++) {
                                        YearData yearData_default = new YearData();
                                        yearData_default.setYear(temp_yearData.getData().get(i));
                                        yearData_default.setSelected(false);
                                        yearList.add(yearData_default);
                                    }

                                    break;
                            }


                            signupVehicleView.getYearList(yearList);


                        }catch (Exception e)
                        {
                            Utility.printLog("Year : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        signupVehicleView.hideProgress();
                    }
                });
    }

    /**
     * <h1>makeAPI</h1>
     * <p>which is the Vehicle Make API call for get the list of vehicle make in the year.</p>
     * @param year : which is the String value selected in the year list. which pass to complete the url API.
     */
    private void makeAPI(String year)
    {
        signupVehicleView.showProgress();
        Observable<Response<ResponseBody>> make=networkService.getMake(preferencesHelper.getLanguage()
                ,year);

        make.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {

                            String result = value.body().string();
                            MakeDataPojo makeDataPojo = gson.fromJson(result, MakeDataPojo.class);

                            signupVehicleView.getMakelist(makeDataPojo.getData());

                        } catch (Exception e) {
                            Utility.printLog("Year : Catch :" + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        signupVehicleView.hideProgress();
                    }
                });
    }

    /**
     * <h1>modelAPI</h1>
     * <p>which is the method for get the Vehicle Model list in depend the year and make. </p>
     * @param year :
     * @param make : year and make is paases to complete the API Url.
     */
    private void modelAPI(String year, String make)
    {
        signupVehicleView.showProgress();
        Observable<Response<ResponseBody>> model=networkService.getModel(preferencesHelper.getLanguage(),
                year,make);

        model.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            String result = value.body().string();
                            ModelDataPojo modelDataPojo = gson.fromJson(result, ModelDataPojo.class);

                            signupVehicleView.getModellist(modelDataPojo.getData());

                        }catch (Exception e)
                        {
                            Utility.printLog("modelAPI : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        signupVehicleView.hideProgress();
                    }
                });
    }

    /**
     * <h1>yearChanged</h1>
     * <p>if the selected year change then need to change the make and model, with the value false</p>
     */
    private void yearChanged(){
        signupVehModelCheck.setCheckMake(false);
        signupVehModelCheck.setCheckModel(false);
    }

    @Override
    public void cropImage(Intent data) {
        String path = data.getStringExtra(CropImage.IMAGE_PATH);
        if(path!=null) {
//            VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
            if(Build.VERSION.SDK_INT>=N)
                VariableConstant.newProfileImageUri = FileProvider.getUriForFile(signupVehicleActivity, signupVehicleActivity.getPackageName(),VariableConstant.newFile);
            else
                VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);

            if (Utility.isNetworkAvailable(context)) {
                amzonUpload(new File(path),context,VariableConstant.VEHICLE_PIC);
            } else {
                Utility.BlueToast(context, context.getString(R.string.no_network));
            }

        }
    }
    @Override
    public void hideKeyboardAndClearFocus() {
        signupVehicleView.hideSoftKeyboard();
    }

    @Override
    public void validateAndStartActivity(String plateNo, String city_ID, String type_ID,
                                         String service_ID, String make_ID, String model_ID,
                                         String selectedYear, String selectedColor, String vehicle_pic_url) {
        if(!vehicle_pic_url.isEmpty()){
            Utility.printLog("the make and model id is : "+make_ID+" "+model_ID);

            signUpData.setPlateNo(plateNo);
            signUpData.setCity(city_ID);
            signUpData.setType(type_ID);
            signUpData.setService(service_ID);
            signUpData.setMake(make_ID);
            signUpData.setModel(model_ID);
            signUpData.setYear(selectedYear);
            signUpData.setColor(selectedColor);
            signUpData.setVehiclePic(vehicle_pic_url);

            plateNoValidateAPI(plateNo);
        }
        else
            signupVehicleView.vehicleImgErr();

    }

    @Override
    public void setDriverBookingPreferenceList(String bookingPreference) {
        signUpData.setDriverBookingPreference(bookingPreference);
        Utility.printLog("getDriverBookingPreference  :  "+signUpData.getDriverBookingPreference());
    }

    @Override
    public void setVehicleBookingPreferenceList(String bookingPreference) {
        signUpData.setVehicleBookingPreference(bookingPreference);
        Utility.printLog("getVehicleBookingPreference  :  "+signUpData.getVehicleBookingPreference());
    }

    /**
     * <h1>plateNoValidateAPI</h1>
     * <p>the API check for the Plate No validation</p>
     * @param plateNo : plate Number
     */
    private void plateNoValidateAPI(String plateNo){
        signupVehicleView.showProgress();
        Observable<Response<ResponseBody>> plateNoValidate=networkService.platNumberValidation(
                preferencesHelper.getLanguage(),plateNo);

        plateNoValidate.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            JSONObject jsonObject;

                            switch (value.code()){
                                case 200:
                                    jsonObject=new JSONObject(value.body().string());
                                    signupVehicleView.validPlateNo();
                                    signupVehicleView.startDocumentSignUp(signUpData);
                                    break;

                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    signupVehicleView.inValidPlateNoAPI(jsonObject.getString("message"));
                                    break;
                            }
                            Utility.printLog("plateNoValidateAPI : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("plateNoValidateAPI : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        signupVehicleView.hideProgress();
                    }
                });
    }

}
