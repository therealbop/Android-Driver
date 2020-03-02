package com.karry.authentication.forgotpassword;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;

import com.karry.data.source.local.PreferencesHelper;
import com.karry.network.NetworkService;
import com.heride.partner.R;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * <h1>OTPVerificationPresenter</h1>
 * <p>the Implementation for the OTP verification.</p>
 */
public class OTPVerificationPresenter implements OTPVerificationContract.Presenter{

    @Inject OTPVerificationContract.View view;
    @Inject NetworkService networkService;
    @Inject CompositeDisposable compositeDisposable;
    @Inject Context context;
    @Inject PreferencesHelper preferencesHelper;

    @Inject OTPVerificationPresenter() {
    }

    @Override
    public void startTimer(int i) {
        view.disableResendButton();
        final long finalTime = i;
        CountDownTimer countDownTimer = new CountDownTimer(finalTime * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                int barVal = (int) seconds;
                view.setTimerText("00:" + String.format("%02d", seconds % 60));
            }

            public void onFinish() {
                view.setTimerText("");
                view.enableResendButton();
            }
        }.start();
    }

    @Override
    public void onSmsReceived(String usedId, String msg, int trigger) {
        Pattern pattern = Pattern.compile("(\\d{4}\\s)");
        Matcher matcher = pattern.matcher(msg);
        String OTP = "";
        if (matcher.find()) {
            OTP = matcher.group(1);  // 4 digit number
        }
        if(OTP.matches("") || OTP.matches(null))
        {
            OTP = msg.substring(Math.max(msg.length() - 4, 0));
        }
        verifyOtp(usedId, OTP, trigger);

        if(OTP.length() > 3)
        {
            view.setOtp(OTP);
        }
    }

    @Override
    public void validateOtp(SeekBar seekBar,String userId, String otp, int trigger) {

        Log.d("test", "validateOtp: "+otp.length());

        if(otp.length() == 1)
        {
            setSeekBarProgress(seekBar,25);
            view.setColorPrimayForAction();;
        }
        else if(otp.length() == 2)
        {
            setSeekBarProgress(seekBar,50);
            view.setDarkColorForAction();
        }
        else if(otp.length() == 3)
        {
            setSeekBarProgress(seekBar,75);
            view.setWhiteColorForAction();
        }
        else if (otp.length() == 4)
        {
            setSeekBarProgress(seekBar,100);
            view.setWhiteColorForAction();
            verifyOtp(userId, otp,trigger);
        }
    }

    public void setSeekBarProgress(SeekBar seekBar, int progress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", progress);
        animation.setDuration(500); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void verifyOtp(String userId,String otp, int trigger)
    {
        view.startProgressBar();
        Observable<Response<ResponseBody>> verifyOtp;

        switch (trigger)
        {
            case 1:
                verifyOtp =  networkService.verifyPhoneNumber(preferencesHelper.getLanguage(), otp, userId);
                break;

            case 2:
                verifyOtp = networkService.verifyOtp(preferencesHelper.getLanguage(), otp, userId,trigger);
                break;

            case 3:
                verifyOtp = networkService.verifyOtp(preferencesHelper.getLanguage(), otp, userId,trigger);
                break;

            default:
                verifyOtp = networkService.verifyOtp(preferencesHelper.getLanguage(), otp, userId,trigger);
                break;
        }

        verifyOtp.observeOn(AndroidSchedulers.mainThread())
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
                            switch (value.code())
                            {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    jsonObject=new JSONObject(value.body().string());
                                    view.onSuccessOtpVerified(jsonObject.getString("message"));
                                    break;

                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    view.onFailure(jsonObject.getString("message"),context.getResources().getString(R.string.message));
                                    break;
                            }

                            Utility.printLog("verifyOtp : "+jsonObject.toString());
                        }catch (Exception e)
                        {
                            Utility.printLog("verifyOtp : Catch :"+e.getMessage());
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
    }

    @Override
    public void resendOtp(String userId,int trigger) {
        if(Utility.isNetworkAvailable(context)) {
            view.disableResendButton();
            view.startProgressBar();
            if (trigger == 3) {
                userId = preferencesHelper.getSessionToken();
            }

            Utility.printLog("the userId is : " + userId);
            Observable<Response<ResponseBody>> sendOtp = networkService.sendOtp(preferencesHelper.getLanguage(), userId, trigger);
            sendOtp.observeOn(AndroidSchedulers.mainThread())
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
                                        view.onSuccessResendOtp(jsonObject.getString("message"));
                                        startTimer(60);
                                        break;

                                    default:
                                        view.enableResendButton();
                                        jsonObject = new JSONObject(value.errorBody().string());
                                        view.onFailure(jsonObject.getString("message"), context.getResources().getString(R.string.message));
                                        break;
                                }
                            } catch (Exception e) {
                                Utility.printLog("sendOtp : Catch :" + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.stopProgressBar();
                            view.enableResendButton();
                        }

                        @Override
                        public void onComplete() {
                            view.stopProgressBar();
                        }
                    });
        }else {
            view.onFailure(context.getResources().getString(R.string.no_network), context.getResources().getString(R.string.alert));
        }
    }

    @Override
    public void onDestroyView() {
        compositeDisposable.clear();
    }

}
