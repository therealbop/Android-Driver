package com.karry.authentication.forgotpassword;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.heride.partner.R;
import com.karry.utility.AppTypeFace;
import com.karry.utility.AutoReadSms;
import com.karry.utility.DialogHelper;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karry.utility.VariableConstant.COUNTRY_CODE;
import static com.karry.utility.VariableConstant.MOBILE;
import static com.karry.utility.VariableConstant.TRIGGER;
import static com.karry.utility.VariableConstant.USER_ID;


/**************************************************************************************************/
public class OTPVerificationActivity extends DaggerAppCompatActivity  implements OTPVerificationContract.View {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_verify_txt) TextView tv_verify_txt;
    @BindView(R.id.tv_verify_msg) TextView tv_verify_msg;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.tv_resendcode) TextView tv_resendcode;
    @BindView(R.id.tvTimer)TextView tvTimer;
    @BindView(R.id.et_otp1)EditText et_otp1;
    @BindView(R.id.et_otp2)EditText et_otp2;
    @BindView(R.id.et_otp3)EditText et_otp3;
    @BindView(R.id.et_otp4)EditText et_otp4;
    @BindView(R.id.ll_first)LinearLayout ll_first;
    @BindView(R.id.progressBar)ProgressBar progressBar;
    @BindView(R.id.seek_bar_button)SeekBar seek_bar_button;
    @BindView(R.id.sv_signup)ScrollView sv_signup;

    @BindString(R.string.verify_msg1) String msg1;
    @BindString(R.string.verify_msg2) String msg2;
    @BindString(R.string.message)String message;
    @BindString(R.string.OK)String ok;
    @BindString(R.string.verify_mob)String verify_mob;

    @Inject OTPVerificationContract.Presenter presenter;
    @Inject AppTypeFace appTypeFace;

    private AutoReadSms autoReadSms;


    @BindColor(R.color.white) int white;
    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindColor(R.color.colorPrimaryDark) int colorPrimaryDark;
    @BindColor(R.color.gray_heading) int gray_heading;
    @BindColor(R.color.black1) int black1;

    private String mobile = "", countryCode = "", userId = "";
    private int trigger = 0; // 1 = > singup 2 ==> ForgotPassword 3==> ChangePhoneNumber


    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        ButterKnife.bind(this);

        initializeViews();
        presenter.startTimer(60);

        autoReadSms = new AutoReadSms() {
            @Override
            protected void onSmsReceived(String str) {
                presenter.onSmsReceived(userId, str, trigger);
            }
        };
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);
        this.registerReceiver(autoReadSms, intentFilter);
    }

    /**********************************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     *<h1>initializeViews</h1>
     * <p>initilize the Views</p[>
     */
    private void initializeViews() {
        Intent intent = getIntent();
        countryCode = intent.getStringExtra(COUNTRY_CODE);
        mobile = intent.getStringExtra(MOBILE);
        userId = intent.getStringExtra(USER_ID);
        trigger = intent.getIntExtra(TRIGGER, 0);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());

        iv_search.setVisibility(View.GONE);
        tv_title.setText(verify_mob);
        tv_verify_txt.setTypeface(appTypeFace.getPro_narMedium());

        tv_verify_msg.setTypeface(appTypeFace.getPro_News());
        tvTimer.setTypeface(appTypeFace.getPro_News());
        tv_verify_msg.setText(msg1 + " " + countryCode + " " + mobile + " " + msg2);
        tv_resendcode.setTypeface(appTypeFace.getPro_News());

        et_otp1.setTypeface(appTypeFace.getPro_narMedium());
        et_otp2.setTypeface(appTypeFace.getPro_narMedium());
        et_otp3.setTypeface(appTypeFace.getPro_narMedium());
        et_otp4.setTypeface(appTypeFace.getPro_narMedium());
        et_otp1.requestFocus();

        sv_signup.postDelayed(() -> sv_signup.fullScroll(View.FOCUS_DOWN), 1000);
    }


    @OnTextChanged({ R.id.et_otp1, R.id.et_otp2, R.id.et_otp3, R.id.et_otp4})
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("test", "onTextChanged: ");
        try {
            View et;
            if(count > before)
            {
                et= getCurrentFocus().focusSearch(View.FOCUS_RIGHT);
            }
            else
            {
                et= getCurrentFocus().focusSearch(View.FOCUS_LEFT);
            }

            if(et instanceof EditText)
            {
                et.requestFocus();
            }


            /*validateOTP();*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnTextChanged({ R.id.et_otp1, R.id.et_otp2, R.id.et_otp3, R.id.et_otp4})
    public void afterTextChanged(Editable s) {

        Log.d("test", "afterTextChanged: ");
        //validateOTP();
    }

    /**
     * <h1>validateOTP</h1>
     * <p>inform to presenter to check the OTP</p>
     */
    private void validateOTP() {
        String otp = et_otp1.getText().toString()+et_otp2.getText().toString()+et_otp3.getText().toString()+et_otp4.getText().toString();
        presenter.validateOtp(seek_bar_button,userId,otp,trigger);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    /**********************************************************************************************/
    @OnClick({R.id.tv_verify_txt,R.id.tv_resendcode})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_verify_txt:
                validateOTP();
                break;

            case R.id.tv_resendcode:
                presenter.resendOtp(userId,trigger);
                break;
        }
    }

    @Override
    public void enableResendButton() {
        tv_resendcode.setTextColor(black1);
        tv_resendcode.setEnabled(true);
    }

    @Override
    public void disableResendButton() {
        tv_resendcode.setEnabled(false);
        tv_resendcode.setTextColor(gray_heading);
    }

    @Override
    public void setWhiteColorForAction() {
        tv_verify_txt.setTextColor(white);
    }

    @Override
    public void setDarkColorForAction() {
        tv_verify_txt.setTextColor(colorPrimaryDark);
    }

    @Override
    public void setColorPrimayForAction() {
        tv_verify_txt.setTextColor(colorPrimary);
    }

    @Override
    public void startProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String msg,String title) {
        DialogHelper.customAlertDialog(this,message,msg,ok);
    }

    @Override
    public void onSuccessOtpVerified(String msg) {
        //DialogHelper.customAlertDialog(this,message,msg,ok);

        switch (trigger)
        {
            case 1:
                DialogHelper.customAlertDialogSignupSuccess(this,message,msg,ok);
                break;

            case 2:
                /*finish();*/
                Intent intentChangePassword = new Intent(OTPVerificationActivity.this, ChangePasswordActivity.class);
                intentChangePassword.putExtra(MOBILE, mobile);
                intentChangePassword.putExtra(COUNTRY_CODE, countryCode);
                intentChangePassword.putExtra(USER_ID, userId);
                startActivity(intentChangePassword);
                break;

            case 3:
                finish();
                break;
        }

    }

    @Override
    public void onSuccessResendOtp(String msg) {
        DialogHelper.customAlertDialog(this,message,msg,ok);
    }

    @Override
    public void setTimerText(String text) {
        tvTimer.setText(text);
    }

    @Override
    public void setOtp(String otp) {
        et_otp1.setText(String.valueOf(otp.charAt(0)));
        et_otp2.setText(String.valueOf(otp.charAt(1)));
        et_otp3.setText(String.valueOf(otp.charAt(2)));
        et_otp4.setText(String.valueOf(otp.charAt(3)));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.onDestroyView();
        try{
            unregisterReceiver(autoReadSms);
        }catch (Exception e){
        }
    }
}
