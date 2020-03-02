package com.karry.authentication.forgotpassword;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;


import com.karry.data.source.local.PreferencesHelper;
import com.karry.network.NetworkService;
import com.heride.partner.R;
import com.karry.utility.TextUtil;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONObject;

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
 * <h1>ChangePasswordPresenter</h1>
 * <p>which implement the change password</p>
 */
public class ChangePasswordPresenter implements ChangePasswordContract.Presenter {

    @Inject ChangePasswordContract.View view;
    @Inject NetworkService networkService;
    @Inject CompositeDisposable compositeDisposable;
    @Inject Context context;
    @Inject PreferencesHelper preferencesHelper;
    @Inject ChangePasswordPresenter() {
    }

    @Override
    public void validatePassword(String userId, String pass, String confPass) {
        if(TextUtil.isEmpty(pass))
        {
            view.invalidPassword();
        }
        else if(!pass.equals(confPass)) {
            view.enableHalfClick();
            view.invalidConformPassword();
        }
        else
        {
            view.enableClick();
            if(!TextUtil.isEmpty(userId))
            {
                updatePasswordApi(userId,pass);
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

    private void updatePasswordApi(String userId, String password){
        if(Utility.isNetworkAvailable(context)) {
            view.startProgressBar();
            Observable<Response<ResponseBody>> updatePassword = networkService.password(preferencesHelper.getLanguage(), password, userId);

            updatePassword.observeOn(AndroidSchedulers.mainThread())
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
                                        view.onSuccess(jsonObject.getString("message"));
                                        break;

                                    default:
                                        jsonObject = new JSONObject(value.errorBody().string());
                                        view.onFailure(context.getResources().getString(R.string.message),jsonObject.getString("message"));
                                        break;
                                }
                                Utility.printLog("passwordApi : " + jsonObject.toString());

                            } catch (Exception e) {
                                Utility.printLog("passwordApi : Catch :" + e.getMessage());
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
            view.onFailure(context.getResources().getString(R.string.alert), context.getResources().getString(R.string.no_network));
        }
    }
}
