package com.karry.side_screens.profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.karry.authentication.forgotpassword.OTPVerificationActivity;
import com.karry.countrypic.countrypic.CountryPicker;
import com.heride.partner.R;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;

import javax.inject.Inject;

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

public class EditProfileActivity extends DaggerAppCompatActivity implements EditProfileContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_search)ImageView iv_search;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_phone_num)TextView tv_phone_num;
    @BindView(R.id.et_phone_email) EditText et_phone_email;
    @BindView(R.id.et_old_pass)  EditText et_old_pass;
    @BindView(R.id.et_new_pass) EditText et_new_pass;
    @BindView(R.id.et_confirm_pass) EditText et_confirm_pass;
    @BindView(R.id.tv_country_code)  TextView tv_country_code;
    @BindView(R.id.flag)  ImageView flag;
    @BindView(R.id.tv_change_msg) TextView tv_change_msg;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.seek_bar_button)SeekBar seek_bar_button;
    @BindView(R.id.tv_confirm)TextView tv_confirm;
    @BindView(R.id.rl_phone_email) RelativeLayout rl_phone_email;
    @BindView(R.id.ll_password_change) LinearLayout ll_password_change;

    @BindString(R.string.editPhoneNumber)  String titlePhone;
    @BindString(R.string.editEmail)   String titleEmail;
    @BindString(R.string.change_pass)   String titleChangePassword;

    @BindString(R.string.edit_phone_msg)   String msgPhone;
    @BindString(R.string.edit_email_msg)   String msgEmail;
    @BindString(R.string.edit_pass_msg)  String msgPassword;

    @BindString(R.string.Email) String Email;
    @BindString(R.string.get_verification) String get_verification;
    @BindString(R.string.save) String save;
    @BindString(R.string.continue_txt) String continue_txt;
    @BindString(R.string.Countrypicker)  String countrypicker;

    @Inject   EditProfileContract.Presenter presenter;
    @Inject   AppTypeFace appTypeFace;
    @Inject   CountryPicker mCountryPicker;
    private int phoneMaxLength = 10;
    private int type = 0;

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone_email);
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);
        ButterKnife.bind(this);
        initializeViews();

    }

    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     */
    private void initializeViews() {

        type = getIntent().getIntExtra("type", 0);

        if (type == 1)
            tv_confirm.setText(get_verification);
        else if (type == 2)
            tv_confirm.setText(save);
        else if (type == 3)
            tv_confirm.setText(continue_txt);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }

        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        iv_search.setVisibility(View.GONE);
        et_phone_email.setTypeface(appTypeFace.getPro_News());
        tv_phone_num.setTypeface(appTypeFace.getPro_News());
        et_old_pass.setTypeface(appTypeFace.getPro_News());
        et_new_pass.setTypeface(appTypeFace.getPro_News());
        et_confirm_pass.setTypeface(appTypeFace.getPro_News());
        tv_change_msg.setTypeface(appTypeFace.getPro_News());
        tv_country_code.setTypeface(appTypeFace.getPro_News());

        presenter.getCountryInfo(mCountryPicker);
        presenter.setType(type);
    }

    @Override
    public void onEmailSelection() {
        tv_phone_num.setText(Email);
        ll_password_change.setVisibility(View.GONE);
        tv_title.setText(titleEmail);
        tv_change_msg.setText(msgEmail);
        flag.setVisibility(View.GONE);
        tv_country_code.setVisibility(View.GONE);
        et_phone_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(50);
        et_phone_email.setFilters(fArray);
    }

    @Override
    public void onMobileSelection() {
        ll_password_change.setVisibility(View.GONE);
        tv_title.setText(titlePhone);
        tv_change_msg.setText(msgPhone);
        et_phone_email.setInputType(InputType.TYPE_CLASS_NUMBER);
        flag.setVisibility(View.VISIBLE);
        tv_country_code.setVisibility(View.VISIBLE);

        /*InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(phoneMaxLength);
        et_phone_email.setFilters(fArray);*/
    }


    @Override
    public void onPasswordSelection() {
        rl_phone_email.setVisibility(View.GONE);
        tv_title.setText(titleChangePassword);
        tv_change_msg.setText(msgPassword);

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
        overridePendingTransition(R.anim.stay_activity, R.anim.bottem_slide_up);
    }


    @OnTextChanged({R.id.et_phone_email, R.id.et_old_pass, R.id.et_new_pass, R.id.et_confirm_pass})
    public void afterTextChanged(Editable editable) {
        if (type == 3) {
            presenter.validatePasswordField(et_old_pass.getText().toString(), et_new_pass.getText().toString(),
                    et_confirm_pass.getText().toString(), false);
        } else {
            presenter.validateField(et_phone_email.getText().toString(), tv_country_code.getText().toString(), false);
        }
    }

    @OnClick({R.id.tv_confirm, R.id.flag, R.id.tv_country_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                if (type == 3) {
                    presenter.validatePasswordField(et_old_pass.getText().toString(), et_new_pass.getText().toString(),
                            et_confirm_pass.getText().toString(), true);
                } else {
                    presenter.validateField(et_phone_email.getText().toString(), tv_country_code.getText().toString(), true);
                }

                break;

            case R.id.flag:
            case R.id.tv_country_code:
                if (!mCountryPicker.isVisible())
                    mCountryPicker.show(getSupportFragmentManager(), countrypicker);
                presenter.setCountryPicker(mCountryPicker);
                break;
        }
    }

    @Override
    public void onGettingOfCountryInfo(int countryFlag, String countryCode, int phoneMaxLength, boolean isDefault) {
        if (mCountryPicker.isVisible())
            mCountryPicker.dismiss();

        flag.setBackgroundResource(countryFlag);
        tv_country_code.setText(countryCode);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(phoneMaxLength);
        et_phone_email.setFilters(fArray);

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
    public void onFailure(String msg) {
        DialogHelper.customAlertDialog(this, getString(R.string.message), msg, getString(R.string.ok));
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onSuccessPhoneValidation(String userId) {
        finish();
        Intent intent = new Intent(this, OTPVerificationActivity.class);
        intent.putExtra(MOBILE, et_phone_email.getText().toString());
        intent.putExtra(COUNTRY_CODE, tv_country_code.getText().toString());
        intent.putExtra(USER_ID, userId);
        intent.putExtra(TRIGGER, 3);
        startActivity(intent);
    }

    @Override
    public void onSuccessUpdate(String message) {
        DialogHelper.customAlertDialogCloseActivity(this, getString(R.string.message), message, getString(R.string.ok));
    }

    @Override
    public void disableClick() {
        presenter.setSeekBarProgress(seek_bar_button, 0);
        tv_confirm.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    public void enableClick() {
        presenter.setSeekBarProgress(seek_bar_button, 100);
        tv_confirm.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public void enableClick33() {
        presenter.setSeekBarProgress(seek_bar_button, 33);
        tv_confirm.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    @Override
    public void enableClick66() {
        presenter.setSeekBarProgress(seek_bar_button, 66);
        tv_confirm.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }
}
