package com.karry.authentication.forgotpassword;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;


import com.karry.countrypic.Country;
import com.karry.countrypic.CountryPickerListener;
import com.karry.countrypic.countrypic.CountryPicker;
import com.karry.data.source.local.PreferencesHelper;
import com.karry.network.NetworkService;
import com.heride.partner.R;
import com.karry.utility.TextUtil;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONObject;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * <h1>ForgotPasswordPresenter</h1>
 * <p>Implementation for the Forgot Password Activity </p>
 */
public class ForgotPasswordPresenter implements  ForgotPasswordContract.Presenter{

    @Inject  ForgotPasswordContract.View view;
    @Inject  NetworkService networkService;
    @Inject  CompositeDisposable compositeDisposable;
    @Inject  Context context;
    @Inject  PreferencesHelper preferencesHelper;

    private int maxPhoneLength;

    @Inject ForgotPasswordPresenter() {
    }

    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        view.setTitle();
    }

    @Override
    public void getCountryInfo(CountryPicker countryPicker)
    {
        Country country = countryPicker.getUserCountryInfo(context);
        maxPhoneLength = country.getMaxDigits();
        view.onGettingOfCountryInfo(country.getFlag(),country.getDialCode(),country.getMaxDigits()
                , true);
    }

    @Override
    public void setCountryPicker(CountryPicker countryPicker) {
        countryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,  int flagDrawableResID,int minLength, int maxLength) {
                maxPhoneLength = maxLength;
                view.onGettingOfCountryInfo(getFlagResId(code,context),dialCode,maxLength ,false);
            }
        });
    }

    private int getFlagResId(String drawable, Context context) {
        try {
            return context.getResources().getIdentifier("flag_" + drawable.toLowerCase(Locale.ENGLISH), "drawable", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void validatePhone(String countryCode, String phoneNumber, boolean forApiCall) {
        if(phoneNumber.length() != maxPhoneLength)
        {
            view.disableClick();
        }
        else
        {
            view.enableClick();
            if(forApiCall)
            {
                forgotPasswordApi(1,countryCode,phoneNumber);
            }
        }
    }

    @Override
    public void validateEmail(String email, boolean forApiCall) {
        if(!TextUtil.emailValidation(email))
        {
            view.disableClick();
        }
        else
        {
            view.enableClick();
            if(forApiCall)
            {
                forgotPasswordApi(2,null,email);
            }
        }
    }

    @Override
    public void setSeekBarProgress(SeekBar seekBar, int progress)
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", progress);
        animation.setDuration(500); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void onDestroyView() {
        compositeDisposable.clear();
    }

    private void forgotPasswordApi(final int verifyType, final String countryCode, final String mob_mail){

        if(Utility.isNetworkAvailable(context)) {

            view.startProgressBar();
            Observable<Response<ResponseBody>> forgotPass = networkService.forgotPassword(preferencesHelper.getLanguage(),
                    countryCode,
                    mob_mail,
                    verifyType);

            forgotPass.observeOn(AndroidSchedulers.mainThread())
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
                                switch (value.code()) {
                                    case VariableConstant.RESPONSE_CODE_SUCCESS:
                                        jsonObject = new JSONObject(value.body().string());
                                        if (verifyType == 1) {
                                            JSONObject jsonObject_data = new JSONObject(jsonObject.getString("data"));
                                            String userId = jsonObject_data.getString("sid");
                                            view.onSuccessPhoneValidation(countryCode, mob_mail, userId);
                                        } else {
                                            view.onSuccessEmailValidation(jsonObject.getString("message"));
                                        }
                                        break;

                                    default:
                                        jsonObject = new JSONObject(value.errorBody().string());
                                        view.onFailure(jsonObject.getString("message"), context.getResources().getString(R.string.message));
                                        break;
                                }
                                Utility.printLog("forgotPasswordApi : " + jsonObject.toString());
                            } catch (Exception e) {
                                Utility.printLog("forgotPasswordApi : Catch :" + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.stopProgressBar();
                        }

                        @Override
                        public void onComplete() {
                            view.stopProgressBar();
                        }
                    });
        }else {
            view.onFailure(context.getResources().getString(R.string.no_network),context.getResources().getString(R.string.alert));
        }

    }
}
