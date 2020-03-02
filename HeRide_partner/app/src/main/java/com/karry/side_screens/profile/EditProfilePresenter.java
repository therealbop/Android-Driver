package com.karry.side_screens.profile;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;

import com.karry.countrypic.Country;
import com.karry.countrypic.CountryPickerListener;
import com.karry.countrypic.countrypic.CountryPicker;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.karry.utility.DataParser;
import com.karry.utility.TextUtil;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

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


public class EditProfilePresenter implements EditProfileContract.Presenter {

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject NetworkService networkService;
    @Inject CompositeDisposable compositeDisposable;
    @Inject Context context;
    @Inject EditProfileContract.View view;
    private int maxPhoneLength=0;
    private int type = 0;

    @Inject
    EditProfilePresenter() {
    }

    @Override
    public void setType(int type) {
        this.type = type;

        switch (type)
        {
            case 1:
                view.onMobileSelection();
                break;

            case 2:
                view.onEmailSelection();
                break;

            case 3:
                view.onPasswordSelection();
                break;
        }
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
    public void validateField(String field, String countryCode, boolean forApiCall)
    {
        if(type == 1)
        {
            if(field.length() != maxPhoneLength)
            {
                view.disableClick();
            }
            else
            {
                view.enableClick();
                if(forApiCall)
                {
                    updateProfileDetails(countryCode,field);
                }
            }
        }
        else
        {
            if(!TextUtil.emailValidation(field))
            {
                view.disableClick();
            }
            else
            {
                view.enableClick();
                if(forApiCall)
                {
                    updateProfileDetails(null,field);
                }
            }
        }
    }

    @Override
    public void validatePasswordField(String oldPassword, String newPassword, String confirmPassword, boolean forApiCall) {

        if(TextUtil.isEmpty(oldPassword))
            view.disableClick();
        else if(TextUtil.isEmpty(newPassword))
            view.enableClick33();
//        else if(newPassword.matches(preferenceHelperDataSource.getPassword()))
//            view.disableClick();
        else if(!confirmPassword.equals(newPassword))
            view.enableClick66();
        else {
            view.enableClick();
            if(forApiCall)
            {
                updateProfileDetails(oldPassword,newPassword);
            }
        }
    }

    @Override
    public void setSeekBarProgress(SeekBar seekBar, int progress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", progress);
        animation.setDuration(500); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void onDestroyView() {
        compositeDisposable.clear();
    }

    private void updateProfileDetails(String field1, String field2)
    {
        view.startProgressBar();
        Observable<Response<ResponseBody>> responseObservable;

        switch (type)
        {
            case 1:
                responseObservable = networkService.updatePhoneNumber(
                        preferenceHelperDataSource.getLanguage(),
                        preferenceHelperDataSource.getSessionToken(),
                        field1,
                        field2);
                break;

            case 2:
                responseObservable  = networkService.updateEmail(
                        preferenceHelperDataSource.getLanguage(),
                        preferenceHelperDataSource.getSessionToken(),
                        field2);
                break;

            case 3:
                responseObservable  = networkService.updatePassword(
                        preferenceHelperDataSource.getLanguage(),
                        preferenceHelperDataSource.getSessionToken(),
                        field1,field2);
                break;

            default:
                responseObservable  = networkService.updatePassword(
                        preferenceHelperDataSource.getLanguage(),
                        preferenceHelperDataSource.getSessionToken(),
                        field1,field2);
                break;
        }

        responseObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        VariableConstant.IS_PROFILE_EDITED = true;

                        try {
                            switch (value.code())
                            {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:

                                    if(type == 1)
                                    {
                                        String userId = DataParser.fetchSidFromData(value);
                                        view.onSuccessPhoneValidation(userId);
                                    }
                                    else
                                    {
                                        String msg = DataParser.fetchSuccessMessage(value);
                                        view.onSuccessUpdate(msg);
                                        if(type == 3)
                                            preferenceHelperDataSource.setPassword(field2);
                                    }

                                    break;

                                default:

                                    view.onFailure(DataParser.fetchErrorMessage(value));
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
                        view.onFailure();
                        Utility.printLog("MYProfile response : onError :"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.stopProgressBar();
                    }
                });
    }
}
