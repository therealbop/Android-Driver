package com.karry.authentication.forgotpassword;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


import com.karry.countrypic.countrypic.CountryPicker;
import com.heride.partner.R;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karry.utility.VariableConstant.COUNTRY_CODE;
import static com.karry.utility.VariableConstant.MOBILE;
import static com.karry.utility.VariableConstant.TRIGGER;
import static com.karry.utility.VariableConstant.USER_ID;

public class ForgotPasswordActivity extends DaggerAppCompatActivity implements ForgotPasswordContract.View{

    @BindView(R.id.tv_forgot_next) TextView tv_forgot_next;
    @BindView(R.id.et_forgot_mob) EditText et_forgot_mob;
    @BindView(R.id.tv_country_code) TextView tv_country_code;
    @BindView(R.id.flag) ImageView flag;
    @BindView(R.id.ll_first) LinearLayout sv_signup;
    @BindView(R.id.tv_forgot_msg) TextView tv_forgot_msg;
    @BindView(R.id.tv_forgot_phoneno) TextView tv_forgot_phoneno;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.rgForgotPass) RadioGroup rg_WorkGroup;
    @BindView(R.id.rbPhone) RadioButton rbPhone;
    @BindView(R.id.rbEmail) RadioButton rbEmail;
    @BindView(R.id.seek_bar_button) SeekBar seek_bar_button;

    @BindString(R.string.forgotPassword) String title;
    @BindString(R.string.Countrypicker) String countrypicker;
    @BindString(R.string.message)String message;
    @BindString(R.string.OK)String OK;
    @BindString(R.string.forgotMsgMob)String forgotMsgMob;
    @BindString(R.string.phone_number)String phone_number;
    @BindString(R.string.Email)String Email;
    @BindString(R.string.forgotMsgEmail)String forgotMsgEmail;

    @BindColor(R.color.white) int white;
    @BindColor(R.color.colorPrimary) int colorPrimary;

    @Inject ForgotPasswordContract.Presenter presenter;
    @Inject AppTypeFace appTypeFace;
    @Inject CountryPicker mCountryPicker;

    private int phoneMaxLength= 10;

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);
        initializeViews();
        presenter.setActionBar();
    }

    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     */
    private void initializeViews() {
        tv_forgot_msg.setTypeface(appTypeFace.getPro_News());
        tv_forgot_phoneno.setTypeface(appTypeFace.getPro_News());
        et_forgot_mob.setTypeface(appTypeFace.getPro_News());
        et_forgot_mob.setHint(phone_number);
        tv_forgot_next.setTypeface(appTypeFace.getPro_narMedium());
        tv_country_code.setTypeface(appTypeFace.getPro_News());
        presenter.getCountryInfo(mCountryPicker);
    }

    @OnCheckedChanged({R.id.rbEmail, R.id.rbPhone})
    public void onRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if(checked) {
            switch (button.getId()) {
                case R.id.rbPhone:
                    onMobileSelection();
                    break;
                case R.id.rbEmail:
                    onEmailSelection();
                    break;
            }
        }
    }

    public void onEmailSelection() {
        et_forgot_mob.setText("");
        et_forgot_mob.setHint(Email);
        tv_forgot_msg.setText(forgotMsgEmail);
        et_forgot_mob.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        flag.setVisibility(View.GONE);
        tv_country_code.setVisibility(View.GONE);

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(50);
        et_forgot_mob.setFilters(fArray);
    }

    public void onMobileSelection() {
        et_forgot_mob.setText("");
        et_forgot_mob.setHint(phone_number);
        tv_forgot_msg.setText(forgotMsgMob);
        et_forgot_mob.setInputType(InputType.TYPE_CLASS_NUMBER);
        flag.setVisibility(View.VISIBLE);
        tv_country_code.setVisibility(View.VISIBLE);

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(phoneMaxLength);
        et_forgot_mob.setFilters(fArray);
    }

    @Override
    public void initActionBar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
        }

        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        iv_search.setVisibility(View.GONE);

        presenter.setActionBarTitle();
    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    /**********************************************************************************************/
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


    @OnTextChanged({ R.id.et_forgot_mob})
    public void afterTextChanged(Editable editable)
    {
        if(rbEmail.isChecked())
        {
            presenter.validateEmail(et_forgot_mob.getText().toString(), false);
        }
        else
        {
            presenter.validatePhone(tv_country_code.getText().toString(), et_forgot_mob.getText().toString(), false);
        }
    }


    @OnClick({R.id.tv_forgot_next,R.id.flag, R.id.tv_country_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forgot_next:
                if(rbEmail.isChecked())
                {
                    presenter.validateEmail(et_forgot_mob.getText().toString(), true);
                }
                else
                {
                    presenter.validatePhone(tv_country_code.getText().toString(), et_forgot_mob.getText().toString(), true);
                }
                break;

            case R.id.flag:
            case R.id.tv_country_code:
                if(!mCountryPicker.isVisible())
                    mCountryPicker.show(getSupportFragmentManager(), countrypicker);
                presenter.setCountryPicker(mCountryPicker);
                break;
        }
    }

    @Override
    public void onGettingOfCountryInfo(int countryFlag, String countryCode, int phoneMaxLength, boolean isDefault) {
        if(mCountryPicker.isVisible())
            mCountryPicker.dismiss();

        flag.setBackgroundResource(countryFlag);
        tv_country_code.setText(countryCode);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(phoneMaxLength);
        et_forgot_mob.setFilters(fArray);

        this.phoneMaxLength = phoneMaxLength;
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
    public void onFailure(String msg, String title) {
        DialogHelper.customAlertDialog(this,title,msg,OK);
    }

    @Override
    public void onSuccessPhoneValidation(String countryCode, String mobile, String userId) {
        /*finish();*/
        Intent intent = new Intent(ForgotPasswordActivity.this, OTPVerificationActivity.class);
        intent.putExtra(MOBILE, mobile);
        intent.putExtra(COUNTRY_CODE, countryCode);
        intent.putExtra(USER_ID, userId);
        intent.putExtra(TRIGGER, 2);
        startActivity(intent);
    }

    @Override
    public void onSuccessEmailValidation(String message) {
        DialogHelper.customAlertDialogCloseActivity( this,message,message,OK);
    }

    @Override
    public void disableClick() {
        presenter.setSeekBarProgress(seek_bar_button,0);
        tv_forgot_next.setTextColor(colorPrimary);
    }

    @Override
    public void enableClick() {
        presenter.setSeekBarProgress(seek_bar_button,100);
        tv_forgot_next.setTextColor(white);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }
}
