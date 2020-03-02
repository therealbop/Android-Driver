package com.karry.authentication.signup.SignUpPersonal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.karry.authentication.signup.SignUpData;
import com.karry.countrypic.Country;
import com.karry.countrypic.countrypic.CountryPicker;
import com.karry.data.source.local.PreferencesHelper;
import com.heride.partner.BuildConfig;
import com.heride.partner.R;
import com.karry.manager.location.LocationCallBack;
import com.karry.manager.location.LocationManagerCallback;
import com.karry.manager.location.RxLocationObserver;
import com.karry.network.NetworkService;
import com.karry.pojo.Signup.Gender;
import com.karry.pojo.Signup.StateData;
import com.karry.pojo.Signup.StateResponse;
import com.karry.utility.DataParser;
import com.karry.utility.TextUtil;
import com.karry.utility.Upload_file_AmazonS3;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import io.reactivex.observers.DisposableObserver;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

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

/**
 * <h1>SignupPersonalPresenter</h1>
 * <p>the implementation of the Sign Up Personal Data</p>
 */
public class SignupPersonalPresenter implements SignUpPersonalContract.SignUpPersonalPresenter,
        LocationManagerCallback.CallBacks, LocationCallBack {

    private static final String TAG = SignupPersonalPresenter.class.getSimpleName();
    @Inject SignUpPersonalContract.SignUpPersonalView signUpPersonalView;
    @Inject NetworkService networkService;
    @Inject SignUpPersonalModel signUpPersonalCheck;
    @Inject Context context;
    @Inject SignupPersonalActvity mActivity;
    @Inject PreferencesHelper preferencesHelper;
    @Inject Gson gson;
    private static int minLength,maxLenght;
    private RxLocationObserver rxLocationObserver;

    private ArrayList<StateData> stateList = new ArrayList<>();
    private ArrayList<Gender> genderList;

    @Inject
    SignupPersonalPresenter(RxLocationObserver rxLocationObserver) {
        this.rxLocationObserver=rxLocationObserver;
        getLatLng();
    }

    @Override
    public void setActionBar() {
        signUpPersonalView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        signUpPersonalView.setTitle();
    }

    @Override
    public void hideKeyboardAndClearFocus() {
        signUpPersonalView.hideSoftKeyboard();
        signUpPersonalView.clearFocus();
    }

    @Override
    public void showKeyboard() {
        signUpPersonalView.showSoftKeyboard();
    }

    @Override
    public void addListenerForCountry(CountryPicker countryPicker, Context context) {
        countryPicker.setListener((name, code, dialCode,flagDrawableResID, minLength, maxLength) ->
                signUpPersonalView.onGettingOfCountryInfo(getFlagResId(code,context),
                        dialCode,minLength,maxLength ,false));
    }


    /**
     * <h1>getFlagResId</h1>
     * <p>used for return the countryFlag</p>
     * @param drawable :
     * @param context :
     * @return : countryFlag
     */
    private int getFlagResId(String drawable, Context context) {
        try {
            return context.getResources()
                    .getIdentifier("flag_" + drawable.toLowerCase(Locale.ENGLISH), "drawable",
                            context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void validateFname(String fname) {
        if(TextUtil.isEmpty(fname)){
            signUpPersonalView.invalidFName();
            signUpPersonalCheck.setFname(false);
        }
        else {
            signUpPersonalView.validFName();
            signUpPersonalCheck.setFname(true);
        }

    }


    @Override
    public void validatePhone(String countryCode, String phone, int min, int max) {
//        signUpPersonalCheck.setPhone(false);


        if(!TextUtil.isEmpty(phone))
        {


            String phoneNumberE164Format = countryCode.concat(phone);
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumberE164Format, null);
                boolean isValid = phoneUtil.isValidNumber(phoneNumberProto); // returns true if valid
/* &&  ( phoneUtil.getNumberType(phoneNumberProto)== PhoneNumberUtil.PhoneNumberType.MOBILE|| phoneUtil.getNumberType(phoneNumberProto)== PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE) */

                if (isValid ) {
                    signUpPersonalView.PhoneLengthValid();

                } else {
                    signUpPersonalView.PhoneLengthInValid();
                }

            }catch (NumberParseException e){

            }

        }
        else {
            signUpPersonalView.phoneEmptyError();
        }






            /*  if(!TextUtil.isEmpty(phone))
        {
            Utility.printLog("the max and min len : "+min+" , "+max+" , "+TextUtil.phoneNumberLengthValidation(phone, minLength, maxLenght));
            if(TextUtil.phoneNumberLengthValidation(phone, min, max)){
                signUpPersonalView.PhoneLengthValid();
            }
            else {
                signUpPersonalView.PhoneLengthInValid();
            }
        }
        else {
            signUpPersonalView.phoneEmptyError();
        }*/

    }

    @Override
    public void validateAPIPhone(String phone, String countryCode) {

        signUpPersonalView.showProgress();
        Observable<Response<ResponseBody>> phoneNumberValidation=networkService.phoneNumberValidation(
                preferencesHelper.getLanguage(),countryCode, phone);

        phoneNumberValidation.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        signUpPersonalView.hideProgress();
                        try {

                            switch (value.code())
                            {
                                case 200:
                                    signUpPersonalCheck.setPhone(true);
                                    signUpPersonalView.phoneAPIValide();
                                    break;

                                case 412:
                                case 406:
                                case 500:
                                case 400:
                                    signUpPersonalCheck.setPhone(false);
                                    signUpPersonalView.phoneAPIInValide(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
//                                    signUpPersonalCheck.setPhone(false);
                                    break;
                            }

                            Utility.printLog("phoneNumberValidation : "+value.toString());

                        }catch (Exception e)
                        {
                            signUpPersonalCheck.setPhone(false);
                            Utility.printLog("phoneNumberValidation : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        signUpPersonalCheck.setPhone(false);
                        signUpPersonalView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        signUpPersonalView.hideProgress();
                    }
                });
    }

    @Override
    public void validateEmailFormat(String email) {
        signUpPersonalCheck.setEmail(false);
        if(!TextUtil.isEmpty(email))
        {
            if(TextUtil.emailValidation(email)){
                signUpPersonalView.emailFormatValid();
            }
            else {
                signUpPersonalView.emailFormatInValid();
            }
        }
        else {
            signUpPersonalView.emailEmptyError();
        }
    }

    @Override
    public void validateAPIEmail(String email) {
        Utility.printLog("Email is : "+email);
        signUpPersonalView.showProgress();

        Observable<Response<ResponseBody>> emailValidation=networkService.emailValidation(
                preferencesHelper.getLanguage(),email);

        emailValidation.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        signUpPersonalView.hideProgress();
                        try {

                            JSONObject jsonObject;
                            Utility.printLog("emailValidation : "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    signUpPersonalCheck.setEmail(true);
                                    jsonObject=new JSONObject(value.body().string());
                                    signUpPersonalView.emailAPIValid();
                                    break;

                                default:
                                    signUpPersonalCheck.setEmail(false);
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    signUpPersonalView.emailAPIInValid(jsonObject.getString("message"));
                                    break;
                            }
                            Utility.printLog("emailValidation : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            signUpPersonalCheck.setEmail(false);
                            Utility.printLog("emailValidation : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        signUpPersonalCheck.setEmail(false);
                        signUpPersonalView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        signUpPersonalView.hideProgress();
                    }
                });

    }



    @Override
    public void getCountryInfo(CountryPicker countryPicker)
    {
        Country country = countryPicker.getUserCountryInfo(mActivity);
        minLength=country.getMinDigits();
        maxLenght=country.getMaxDigits();
        signUpPersonalView.onGettingOfCountryInfo(country.getFlag(),country.getDialCode(),country.getMinDigits(),country.getMaxDigits(), true);
    }

    @Override
    public void cropImage(Intent data) {
        String path = data.getStringExtra(CropImage.IMAGE_PATH);
        if(path!=null) {
            if(Build.VERSION.SDK_INT>=N)
                VariableConstant.newProfileImageUri = FileProvider.getUriForFile(mActivity, context.getPackageName(),VariableConstant.newFile);
            else
                VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
            if (Utility.isNetworkAvailable(context)) {
                amzonUpload(new File(path),context,VariableConstant.PROFILE_PIC);
            } else {
                Utility.BlueToast(context, context.getString(R.string.no_network));
            }

        }
    }

    @Override
    public void validatePass(String password) {
        if(TextUtil.isEmpty(password)){
            signUpPersonalCheck.setPassword(false);
            signUpPersonalView.passwordEmpty();
        }
        else
            signUpPersonalView.passwordNotEmpty();
    }

    @Override
    public void validatePass(String password, String confirmPass) {
        if(TextUtil.isEmpty(password) || TextUtil.isEmpty(confirmPass))
        {
            signUpPersonalCheck.setPassword(false);
            if(TextUtil.isEmpty(password)){
                signUpPersonalView.passwordEmpty();
            }

            if(TextUtil.isEmpty(confirmPass)){
                signUpPersonalView.confirmPasswordEmpty();
            }
        }
        else {
            signUpPersonalView.passwordNotEmpty();
            signUpPersonalView.confirmPasswordNotEmpty();

            if(password.matches(confirmPass))
            {
                signUpPersonalCheck.setPassword(true);
                signUpPersonalView.passMatching();
            }
            else {
                signUpPersonalCheck.setPassword(false);
                signUpPersonalView.passNotMatching();
            }

        }
    }

    @Override
    public void validateAllFieldsFlags() {

        Utility.printLog("the validation completed : "+signUpPersonalCheck.isFname()+" "
                +signUpPersonalCheck.isPhone()+" "
                +signUpPersonalCheck.isEmail()+" "
                +signUpPersonalCheck.isPassword()+" "+signUpPersonalCheck.isState()+" "+signUpPersonalCheck.isImageUploaded());

        if(signUpPersonalCheck.isFname() &&
                signUpPersonalCheck.isPhone() &&
                signUpPersonalCheck.isEmail() &&
                signUpPersonalCheck.isPassword() &&
                signUpPersonalCheck.isState() &&
                signUpPersonalCheck.isGender()/*&& signUpPersonalCheck.isImageUploaded()*/){
            signUpPersonalView.enableNext();
        }
        else {
            signUpPersonalView.disableNext();
        }
    }

    @Override
    public void amzonUpload(File file, Context context, String Bucket_folder) {
        signUpPersonalView.showProgress();

        Utility.printLog("the profile url is : "+file.getName().replace(" ",""));
        Upload_file_AmazonS3 amazonS3 = Upload_file_AmazonS3.getInstance(context);
        final String imageUrl = BuildConfig.AMAZON_BASE_URL+BuildConfig.BUCKET_NAME+"/"+ Bucket_folder +"/"+file.getName().replace(" ","");
        amazonS3.Upload_data(BuildConfig.BUCKET_NAME, Bucket_folder +"/"+file.getName().replace(" ",""), file, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String url)
            {
                signUpPersonalCheck.setImageUploaded(true);
                validateAllFieldsFlags();
                signUpPersonalView.hideProgress();
                signUpPersonalView.amazonUploadSuccess(imageUrl);

            }

            @Override
            public void sucess(String url, String type) {
                signUpPersonalCheck.setImageUploaded(true);
                validateAllFieldsFlags();
                signUpPersonalView.hideProgress();
                signUpPersonalView.amazonUploadSuccess(url);
            }

            @Override
            public void error(String errormsg)
            {
                signUpPersonalCheck.setImageUploaded(false);
                signUpPersonalView.hideProgress();
                Log.d("baby","error");
            }
        });
    }

    @Override
    public void stateCheck() {
        if (stateList != null && stateList.size() > 0) {
            signUpPersonalView.getStateList(stateList);
        } else {
            stateApi();
        }
    }

    /**
     * <h1>stateApi</h1>
     * <p>the Api call for fetch the list of state</p>
     */
    private void stateApi()
    {
        signUpPersonalView.showProgress();
        Observable<Response<ResponseBody>> city=networkService.state(
                preferencesHelper.getLanguage());

        city.observeOn(AndroidSchedulers.mainThread())
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
                                    StateResponse stateResponse = gson.fromJson(jsonObject.toString(), StateResponse.class);
                                    for(int i=0;i<stateResponse.getData().size();i++)
                                    {
                                        StateData stateData=new StateData();
                                        stateData.setState(stateResponse.getData().get(i));
                                        stateData.setSelected(false);
                                        stateList.add(stateData);
                                    }
                                    signUpPersonalView.getStateList(stateList);
                                    break;

                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    break;
                            }
                            Utility.printLog("State : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("city : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        signUpPersonalView.hideProgress();
                    }
                });
    }

    @Override
    public void onStateSelected(Intent data) {
        if (stateList != null) {
            stateList.clear();
        }
        boolean selector=true;
        stateList = (ArrayList<StateData>) data.getSerializableExtra(DATA);
        for (StateData stateData : stateList) {
            if (stateData.isSelected()) {
                selector=false;
                signUpPersonalCheck.setState(true);
                signUpPersonalView.setState(stateData.getState());
            }
        }
        if(selector)
        {
            signUpPersonalCheck.setState(true);
            signUpPersonalView.setState(stateList.get(0).getState());
        }
    }

    @Override
    public void genderCheck() {

        if (genderList != null && genderList.size() > 0) {
            signUpPersonalView.getGenderList(genderList);
        } else {
            this.genderList = new ArrayList<>();
            String[] genderList = context.getResources().getStringArray(R.array.gender);
            for (String aGenderList : genderList) {
                Gender genderData = new Gender();
                genderData.setGender(aGenderList);
                this.genderList.add(genderData);
            }
            signUpPersonalView.getGenderList(this.genderList);
        }
    }

    @Override
    public void onGenderSelected(Intent data) {
        genderList = (ArrayList<Gender>) data.getSerializableExtra(DATA);
        for (Gender genderData : genderList) {
            if (genderData.isSelected()) {
                signUpPersonalView.setGender(genderData.getGender());
                signUpPersonalCheck.setGender(true);
            }
        }
    }

    @Override
    public void validateAndStartActivity(String fName, String lName, String phone,
                                         String email, String password, String referralCode,
                                         String countryCode, String profilePic, String state,
                                         String dob, String gender) {

        if(profilePic!=null && !profilePic.isEmpty()){
            if(dob!=null && !dob.isEmpty()){
                SignUpData signUpData = new SignUpData();
                signUpData.setFname(fName);
                signUpData.setLname(lName);
                signUpData.setPhone(phone);
                signUpData.setEmail(email);
                signUpData.setPasswod(password);
                signUpData.setReferalCode(referralCode);
                signUpData.setCountryCode(countryCode);
                signUpData.setProfilePic(profilePic);
                signUpData.setState(state);
                signUpData.setDob(dob);

                String[] genderList = context.getResources().getStringArray(R.array.gender);

                for (int i = 0;i<genderList.length;i++) {

                    Utility.printLog("the gender is : "+genderList[i]+"  ,"+gender);

                    if(genderList[i].matches(gender)) {
                        int gender_value = i+1;
                        signUpData.setGender(String.valueOf(gender_value));
                        Utility.printLog("the gender is : "+signUpData.getGender()+"  ,"+String.valueOf(gender_value));
                    }

                }
                if(referralCode.isEmpty())
                    signUpPersonalView.startVehicleSignUp(signUpData);
                else {
                    referralCodeAPI(referralCode,signUpData);
                }
            }
            else
                signUpPersonalView.dobError();

        }
        else
            signUpPersonalView.profilePicError();

    }

    @Override
    public void referralCodeValidation(String refCode) {
        if(refCode.isEmpty()) {
            signUpPersonalCheck.setRefCode(true);
            signUpPersonalView.validReferralCode();
        }
        else
            referralCodeAPI(refCode,null);
    }

    @Override
    public void refCodeEmptyValidation(String refCode) {
        if(refCode.isEmpty()) {
            signUpPersonalCheck.setRefCode(true);
            signUpPersonalView.validReferralCode();
        }
    }


    private void validData(SignUpData signUpData)
    {
        if(signUpData.getReferalCode().isEmpty())
            signUpPersonalView.startVehicleSignUp(signUpData);
        else if(!signUpPersonalCheck.isRefCode())
            signUpPersonalView.invalidReferralCode(context.getResources().getString(R.string.refErr));
    }

    @Override
    public void getCurrentLocation() {
        //locationProvider.startLocation(this);
    }

    @Override
    public void disposeObservables() {
        //locationProvider.stopLocationUpdates();
    }

    /**
     * <h1>referralCodeAPI</h1>
     * <p>API referal code check</p>
     * @param refCode : referral code
     */
    public void referralCodeAPI(String refCode,SignUpData signUpData){
        signUpPersonalView.showProgress();
        Utility.printLog("referralCodeValidation : "+preferencesHelper.getCurrLatitude()+","+preferencesHelper.getCurrLongitude());
        Observable<Response<ResponseBody>> referralCodeValidation=networkService.referralCodeValidation(
                preferencesHelper.getLanguage(),refCode,
                preferencesHelper.getCurrLatitude(),preferencesHelper.getCurrLongitude());

        referralCodeValidation.observeOn(AndroidSchedulers.mainThread())
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
                                    signUpPersonalCheck.setRefCode(true);
                                    jsonObject=new JSONObject(value.body().string());
                                    signUpPersonalView.validReferralCode();

                                    if(signUpData!=null){
                                        signUpPersonalView.startVehicleSignUp(signUpData);
                                    }

                                    break;

                                default:
                                    signUpPersonalCheck.setRefCode(false);
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    signUpPersonalView.invalidReferralCode(context.getResources().getString(R.string.refErr));
                                    break;
                            }
                            Utility.printLog("referralCodeValidation : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("referralCodeValidation : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog("referralCodeValidation : error :"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        signUpPersonalView.hideProgress();
                    }
                });
    }


    /**
     * <h1>getLatLng</h1>
     * <p>for subscribe the location </p>
     */
    @SuppressLint("CheckResult")
    private void getLatLng(){
        RxLocationObserver.getInstance().getObservable().subscribe(new DisposableObserver<Location>() {
            @Override
            public void onNext(Location location)
            {
                Utility.printLog(TAG+" onNext location oberved  "+location.getLatitude()
                    +" "+location.getLongitude());
                preferencesHelper.setCurrLatitude(String.valueOf(location.getLatitude()));
                preferencesHelper.setCurrLongitude(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void onUpdateLocation(Location location) {

    }

    @Override
    public void locationMsg(String error) {

    }

    @Override
    public void onLocationServiceDisabled(Status status) {

    }
}
