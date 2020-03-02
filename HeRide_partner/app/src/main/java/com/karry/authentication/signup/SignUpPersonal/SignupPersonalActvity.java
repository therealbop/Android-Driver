package com.karry.authentication.signup.SignUpPersonal;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.karry.authentication.signup.SignUpData;
import com.karry.countrypic.countrypic.CountryPicker;
import com.karry.pojo.Signup.Gender;
import com.squareup.picasso.Picasso;
import com.heride.partner.R;
import com.karry.pojo.Signup.StateData;
import com.karry.authentication.signup.GenericListActivity;
import com.karry.authentication.signup.SignUpVehicle.SignupVehicleActivity;
import com.karry.utility.AppTypeFace;
import com.karry.utility.CircleTransform;
import com.karry.utility.ImageEditUpload;
import com.karry.utility.Scaler;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;
import eu.janmuller.android.simplecropimage.CropImage;
import pub.devrel.easypermissions.EasyPermissions;

import static com.karry.utility.VariableConstant.CAMERA_PIC;
import static com.karry.utility.VariableConstant.CROP_IMAGE;
import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.GALLERY_PIC;
import static com.karry.utility.VariableConstant.GENDER;
import static com.karry.utility.VariableConstant.SELECT_AN_STATE;
import static com.karry.utility.VariableConstant.SELECT_A_GENDER;
import static com.karry.utility.VariableConstant.SIGNUP_DATA;
import static com.karry.utility.VariableConstant.STATE;
import static com.karry.utility.VariableConstant.TITLE;
import static com.karry.utility.VariableConstant.TYPE;

/**
 * <h1>SignupPersonalActvity</h1>
 * <p>This is a Controller class for SignupPersonal Actvity</p>
 * This class is used to provide the SignUp screen for driver personal details.
 * @version 1.0.0
 * @since 18/01/18
 */
public class SignupPersonalActvity extends DaggerAppCompatActivity implements
        SignUpPersonalContract.SignUpPersonalView{

    @Inject SignUpPersonalContract.SignUpPersonalPresenter signupPersonalPresenter;
    @Inject AppTypeFace appTypeFace;
    @Inject CountryPicker mCountryPicker;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_sign_up_header) TextView tv_sign_up_header;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindViews({R.id.et_sign_up_fname,
            R.id.et_sign_up_lname,
            R.id.et_signup_mob,
            R.id.et_sign_up_email,
            R.id.et_sign_up_password,
            R.id.et_sign_up_confirmPass,
            R.id.et_sign_up_referral}) List<EditText> et_signup_personal;

    @BindViews({ R.id.til_sign_up_fname,
            R.id.til_sign_up_lname,
            R.id.til_signup_mob,
            R.id.til_sign_up_email,
            R.id.til_sign_up_password,
            R.id.til_sign_up_confirmPass,
            R.id.til_sign_up_referral}) List<TextInputLayout> til_signup_personal;
    @BindView(R.id.til_sign_up_dob) TextInputLayout til_sign_up_dob;
    @BindView(R.id.til_sign_up_state) TextInputLayout til_sign_up_state;
    @BindView(R.id.til_sign_up_gender) TextInputLayout til_sign_up_gender;

    @BindView(R.id.tv_sign_up_nextBtn) TextView tv_sign_up_nextBtn;
    @BindView(R.id.iv_sign_up_profile_pic) ImageView iv_sign_up_profile_pic;
    @BindView(R.id.iv_sign_up_flag) ImageView iv_sign_up_flag;
    @BindView(R.id.tv_sign_up_countryCode) TextView tv_sign_up_countryCode;

    @BindView(R.id.pb_sign_up_progressBar) ProgressBar pb_sign_up_progressBar;
    @BindView(R.id.et_sign_up_dob) EditText et_sign_up_dob;
    @BindView(R.id.et_sign_up_state) EditText et_sign_up_state;
    @BindView(R.id.et_signup_postal) EditText et_signup_postal;
    @BindView(R.id.et_sign_up_gender) EditText et_sign_up_gender;


    @BindString(R.string.signup_) String title;
    @BindString(R.string.mandatory) String mandatory;
    @BindString(R.string.Countrypicker) String countrypicker;
    @BindString(R.string.app_name) String app_name;
    @BindString(R.string.no_network) String no_network;
    @BindString(R.string.smthWentWrong) String smthWentWrong;
    @BindString(R.string.err_phone_no) String err_phone_no;
    @BindString(R.string.invalidEmail) String invalidEmail;
    @BindString(R.string.pass_mismatch) String pass_mismatch;
    @BindString(R.string.choose_prof_pic) String choose_prof_pic;
    @BindString(R.string.dateOfBirthError) String dateOfBirthError;
    @BindString(R.string.enterState) String enterState;
    @BindString(R.string.enterGender) String enterGender;
    @BindString(R.string.enterColor) String enterColor;
    @BindString(R.string.location_permission_message) String location_permission_message;
    @BindColor(R.color.gray) int grey_bg;

    @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;

    private int maxPhoneLength , minPhoneLength ;
    private String Profile_pic_url,selectedDob;
    int currentYear,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_personal);
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);
        ButterKnife.bind(this);
        initializeViews();
        signupPersonalPresenter.setActionBar();
    }

    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views including set Type</p>
     */
    private void initializeViews() {
        Calendar calendar = Calendar.getInstance();
        tv_sign_up_header.setTypeface(appTypeFace.getPro_News());

        for(int i=0;i<et_signup_personal.size();i++)
        {
            et_signup_personal.get(i).setTypeface(appTypeFace.getPro_News());
        }

        tv_sign_up_nextBtn.setTypeface(appTypeFace.getPro_narMedium());
        et_signup_postal.setTypeface(appTypeFace.getPro_News());
        et_sign_up_dob.setTypeface(appTypeFace.getPro_News());
        et_sign_up_dob.setInputType(InputType.TYPE_NULL);
        et_sign_up_gender.setTypeface(appTypeFace.getPro_News());
        et_sign_up_gender.setInputType(InputType.TYPE_NULL);
        et_sign_up_state.setTypeface(appTypeFace.getPro_News());
        et_sign_up_state.setInputType(InputType.TYPE_NULL);
        tv_sign_up_nextBtn.setEnabled(false);
        currentYear = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        signupPersonalPresenter.getCountryInfo(mCountryPicker);

        et_signup_personal.get(6).setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signupPersonalPresenter.referralCodeValidation(et_signup_personal.get(6).getText().toString());
            }
            return false;
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        startCurrLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        signupPersonalPresenter.disposeObservables();
    }

    /**
     * <h1>startCurrLocation</h1>
     * <p>This method is used to get the current location</p>
     */
    private void startCurrLocation()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (EasyPermissions.hasPermissions(this, perms)) {
                signupPersonalPresenter.getCurrentLocation();
            } else {
                EasyPermissions.requestPermissions(this, location_permission_message,
                        VariableConstant.RC_LOCATION_STATE, perms);
            }

        } else {
            signupPersonalPresenter.getCurrentLocation();
        }
    }

    /**
     * <h1>dob</h1>
     * <p>which is the datePicker set date from the picker,
     * used in datePicker().</p>
     */
    private DatePickerDialog.OnDateSetListener dob = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String dob=Utility.sentingDateFormat(dayOfMonth,monthOfYear,year);
            et_sign_up_dob.setText(dob);
            signupPersonalPresenter.hideKeyboardAndClearFocus();
            selectedDob = dob;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        hideSoftKeyboard();
        switch (requestCode) {

            case CAMERA_PIC:
                startCropImage();
                break;

            case GALLERY_PIC:
                try {
                    String takenNewImage;
                    String state = Environment.getExternalStorageState();
                    takenNewImage = app_name + String.valueOf(System.nanoTime()) + ".png";

                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        VariableConstant.newFile = new File(Environment.getExternalStorageDirectory(), takenNewImage);
                    } else {
                        VariableConstant.newFile = new File(getFilesDir(), takenNewImage);
                    }

                    InputStream inputStream = getContentResolver().openInputStream(
                            data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(VariableConstant.newFile.getAbsolutePath()));

                    Utility.copyStream(inputStream, fileOutputStream);

                    fileOutputStream.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
                    startCropImage();
                } catch (Exception e) {
                    Utility.printLog("Error"+e.getMessage());
                }
                break;

            case CROP_IMAGE:
                if(data!=null)
                    signupPersonalPresenter.cropImage(data);
                break;

            case SELECT_AN_STATE:
                if(data!=null)
                    signupPersonalPresenter.onStateSelected(data);
                else
                    til_sign_up_state.setErrorEnabled(false);
                break;

            case SELECT_A_GENDER:
                if(data!=null)
                    signupPersonalPresenter.onGenderSelected(data);
                else
                    til_sign_up_gender.setErrorEnabled(false);

            default:

                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        hideSoftKeyboard();
    }

    /**
     * <h1>startCropImage</h1>
     * <p>intent for Crop the Image selected</p>
     */
    private void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, VariableConstant.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);
        intent.putExtra("locale", Locale.getDefault().getDisplayLanguage());
        startActivityForResult(intent, CROP_IMAGE);
    }

    @OnFocusChange({R.id.et_sign_up_fname,
            R.id.et_sign_up_lname,
            R.id.et_signup_mob,
            R.id.et_sign_up_email,
            R.id.et_sign_up_gender,
            R.id.et_sign_up_password,
            R.id.et_sign_up_confirmPass,
            R.id.et_sign_up_referral,R.id.et_sign_up_dob,R.id.et_sign_up_state})
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId())
        {
            case R.id.et_sign_up_fname:
                if (!hasFocus)
                {
                    signupPersonalPresenter.validateFname(et_signup_personal.get(0).getText().toString());
                    signupPersonalPresenter.validateAllFieldsFlags();
                }
                break;
            case R.id.et_signup_mob:
                if (!hasFocus)
                {
                    signupPersonalPresenter.validatePhone(tv_sign_up_countryCode.getText().toString(),et_signup_personal.get(2).getText().toString(),minPhoneLength,maxPhoneLength);
                    signupPersonalPresenter.validateAllFieldsFlags();
                }
                break;
            case R.id.et_sign_up_email:
                if (!hasFocus)
                {
                    signupPersonalPresenter.validateEmailFormat(et_signup_personal.get(3).getText().toString());
                    signupPersonalPresenter.validateAllFieldsFlags();
                }
                break;

            case R.id.et_sign_up_gender:
                if (hasFocus)
                {
                    signupPersonalPresenter.hideKeyboardAndClearFocus();
                    signupPersonalPresenter.genderCheck();
                }
                break;

            case R.id.et_sign_up_dob:
                if (hasFocus)
                {
                    signupPersonalPresenter.hideKeyboardAndClearFocus();
                    new Handler().postDelayed(this::datePicker, 800);

                }
                break;
            case R.id.et_sign_up_state:
                if (hasFocus)
                {
                    signupPersonalPresenter.hideKeyboardAndClearFocus();
                    signupPersonalPresenter.stateCheck();
                }
                break;

            case R.id.et_signup_postal:
                if (hasFocus)
                {
                    signupPersonalPresenter.showKeyboard();
                    signupPersonalPresenter.genderCheck();
                }
                break;
            case R.id.et_sign_up_password:
                if (!hasFocus)
                {
                    signupPersonalPresenter.validatePass(et_signup_personal.get(4).getText().toString());
                    signupPersonalPresenter.validateAllFieldsFlags();

                }
                break;
            case R.id.et_sign_up_confirmPass:
                if (!hasFocus)
                {
                    signupPersonalPresenter.validatePass(et_signup_personal.get(4).getText().toString(),
                            et_signup_personal.get(5).getText().toString());
                    signupPersonalPresenter.validateAllFieldsFlags();
                }
                break;
            case R.id.et_sign_up_referral:
                if(!hasFocus)
                    signupPersonalPresenter.referralCodeValidation(et_signup_personal.get(6).getText().toString());
                break;
        }
    }


    @OnTextChanged({R.id.et_sign_up_fname,
            R.id.et_sign_up_lname,
            R.id.et_signup_mob,
            R.id.et_sign_up_email,
            R.id.et_sign_up_password,
            R.id.et_sign_up_confirmPass,
            R.id.et_sign_up_referral})
    public void afterTextChanged(Editable editable) {

        if (editable == et_signup_personal.get(0).getEditableText())
        {
            signupPersonalPresenter.validateFname(et_signup_personal.get(0).getText().toString());
            signupPersonalPresenter.validateAllFieldsFlags();
        }
        else if (editable == et_signup_personal.get(1).getEditableText())
        {
            signupPersonalPresenter.validateAllFieldsFlags();
        }
        else if (editable == et_signup_personal.get(2).getEditableText())
        {
//            signupPersonalPresenter.validatePhone(et_signup_personal.get(2).getText().toString(),minPhoneLength,maxPhoneLength);
            signupPersonalPresenter.validateAllFieldsFlags();
        }
        else if (editable == et_signup_personal.get(3).getEditableText())
        {
//            signupPersonalPresenter.validateEmailFormat(et_signup_personal.get(3).getText().toString());
            signupPersonalPresenter.validateAllFieldsFlags();

        }
        else if (editable == et_signup_personal.get(4).getEditableText())
        {
            signupPersonalPresenter.validatePass(et_signup_personal.get(4).getText().toString());
            signupPersonalPresenter.validateAllFieldsFlags();
        }
        else if (editable == et_signup_personal.get(5).getEditableText())
        {
            signupPersonalPresenter.validatePass(et_signup_personal.get(4).getText().toString(),
                    et_signup_personal.get(5).getText().toString());
            signupPersonalPresenter.validateAllFieldsFlags();
        }
        else if (editable == et_signup_personal.get(6).getEditableText())
        {
            signupPersonalPresenter.validateAllFieldsFlags();
            signupPersonalPresenter.refCodeEmptyValidation(et_signup_personal.get(6).getText().toString());
        }

    }

    @OnClick({R.id.iv_sign_up_flag, R.id.tv_sign_up_countryCode,  R.id.tv_sign_up_nextBtn,
            R.id.iv_sign_up_profile_pic, R.id.et_sign_up_dob,R.id.et_sign_up_state,R.id.et_sign_up_gender})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_sign_up_flag:
            case R.id.tv_sign_up_countryCode:
                signupPersonalPresenter.hideKeyboardAndClearFocus();
                mCountryPicker.show(getSupportFragmentManager(), countrypicker);
                signupPersonalPresenter.addListenerForCountry(mCountryPicker, this);
                break;

            case R.id.tv_sign_up_nextBtn:
                signupPersonalPresenter.validateAndStartActivity(et_signup_personal.get(0).getText().toString(),
                        et_signup_personal.get(1).getText().toString(),
                        et_signup_personal.get(2).getText().toString(),
                        et_signup_personal.get(3).getText().toString(),
                        et_signup_personal.get(4).getText().toString(),
                        et_signup_personal.get(6).getText().toString(),
                        tv_sign_up_countryCode.getText().toString(),
                        Profile_pic_url,
                        et_sign_up_state.getText().toString(),
                        selectedDob,
                        et_sign_up_gender.getText().toString());

                break;

            case R.id.iv_sign_up_profile_pic:
                new ImageEditUpload(this,null);
                break;

            case R.id.et_sign_up_dob:
                signupPersonalPresenter.hideKeyboardAndClearFocus();
                datePicker();
                break;

            case R.id.et_sign_up_state:
                signupPersonalPresenter.hideKeyboardAndClearFocus();
                signupPersonalPresenter.stateCheck();
                break;

            case R.id.et_sign_up_gender:
                signupPersonalPresenter.hideKeyboardAndClearFocus();
                signupPersonalPresenter.genderCheck();
                break;
        }
    }

    @Override
    public void dobError() {
        Utility.BlueToast(this, dateOfBirthError);
    }

    @Override
    public void profilePicError() {
        Utility.BlueToast(this, choose_prof_pic);
    }

    @Override
    public void startVehicleSignUp(SignUpData signUpData) {

        Intent mIntent = new Intent(this, SignupVehicleActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(SIGNUP_DATA,signUpData);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    @Override
    public void validReferralCode() {
        til_signup_personal.get(6).setErrorEnabled(false);

//        signupPersonalPresenter.referralCodeAPI(et_signup_personal.get(6).getText().toString());
    }

    @Override
    public void invalidReferralCode(String errMsg) {
        til_signup_personal.get(6).setError(errMsg);
        til_signup_personal.get(6).setFocusable(true);
        til_signup_personal.get(6).setErrorEnabled(true);
    }

    /**
     * <h1>datePicker</h1>
     * <p>which is the default datePicker dialog</p>
     */
    private void datePicker()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dob, currentYear, month, day);
        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis()-5.676e+11));
        datePickerDialog.getDatePicker().setMinDate((long) (System.currentTimeMillis()-3.154e+12));
        datePickerDialog.show();
    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        signupPersonalPresenter.setActionBarTitle();
    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    @Override
    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void showSoftKeyboard() {
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onGettingOfCountryInfo(int countryFlag, String countryCode,int phoneMinLength, int phoneMaxLength, boolean isDefault) {
        if(mCountryPicker.isVisible())
            mCountryPicker.dismiss();

        Utility.printLog("the max and min len : "+phoneMinLength+" , "+phoneMaxLength);
        tv_sign_up_countryCode.setText(countryCode);
        iv_sign_up_flag.setImageResource(countryFlag);
//        et_signup_personal.get(2).setFilters(Utility.getInputFilterForPhoneNo(phoneMaxLength));

        minPhoneLength = phoneMinLength;
        maxPhoneLength = phoneMaxLength;

    }

    @Override
    public void clearFocus() {
        if(et_signup_personal.get(0).hasFocus())
            et_signup_personal.get(0).clearFocus();

        if(et_signup_personal.get(1).hasFocus())
            et_signup_personal.get(1).clearFocus();

        if(et_signup_personal.get(2).hasFocus())
            et_signup_personal.get(2).clearFocus();

        if(et_signup_personal.get(3).hasFocus())
            et_signup_personal.get(3).clearFocus();

        if(et_signup_personal.get(4).hasFocus())
            et_signup_personal.get(4).clearFocus();

        if(et_signup_personal.get(5).hasFocus())
            et_signup_personal.get(5).clearFocus();

        if(et_signup_personal.get(6).hasFocus())
            et_signup_personal.get(6).clearFocus();
    }

    @Override
    public void validFName() {
        til_signup_personal.get(0).setErrorEnabled(false);
    }

    @Override
    public void invalidFName() {
        til_signup_personal.get(0).setError(mandatory);
        til_signup_personal.get(0).setErrorEnabled(true);
    }

    @Override
    public void phoneEmptyError() {
        til_signup_personal.get(2).setError(mandatory);
        til_signup_personal.get(2).setErrorEnabled(true);
    }

    @Override
    public void PhoneLengthValid() {
        til_signup_personal.get(2).setErrorEnabled(false);
        signupPersonalPresenter.validateAPIPhone(et_signup_personal.get(2).getText().toString(),
                tv_sign_up_countryCode.getText().toString());
    }

    @Override
    public void PhoneLengthInValid() {
        til_signup_personal.get(2).setError(err_phone_no);
        til_signup_personal.get(2).setErrorEnabled(true);
    }

    @Override
    public void phoneAPIValide() {
        til_signup_personal.get(2).setErrorEnabled(false);
    }

    @Override
    public void phoneAPIInValide(String msg) {
        til_signup_personal.get(2).setError(msg);
        til_signup_personal.get(2).setErrorEnabled(true);
    }

    @Override
    public void emailEmptyError() {
        til_signup_personal.get(3).setError(mandatory);
        til_signup_personal.get(3).setErrorEnabled(true);
    }

    @Override
    public void emailFormatValid() {
        Utility.printLog("Email is : "+et_signup_personal.get(3).getText().toString());
        signupPersonalPresenter.validateAPIEmail(et_signup_personal.get(3).getText().toString());
        til_signup_personal.get(3).setErrorEnabled(false);
    }

    @Override
    public void emailFormatInValid() {
        til_signup_personal.get(3).setErrorEnabled(true);
        til_signup_personal.get(3).setError(invalidEmail);
    }

    @Override
    public void emailAPIValid() {
        signupPersonalPresenter.validateAllFieldsFlags();
        til_signup_personal.get(3).setErrorEnabled(false);
    }

    @Override
    public void emailAPIInValid(String msg) {
        signupPersonalPresenter.validateAllFieldsFlags();
        til_signup_personal.get(3).setErrorEnabled(true);
        til_signup_personal.get(3).setError(msg);
    }

    @Override
    public void passwordEmpty() {
        til_signup_personal.get(4).setError(mandatory);
        til_signup_personal.get(4).setErrorEnabled(true);
    }

    @Override
    public void passwordNotEmpty() {
        til_signup_personal.get(4).setErrorEnabled(false);
    }

    @Override
    public void confirmPasswordEmpty() {
        til_signup_personal.get(5).setError(mandatory);
        til_signup_personal.get(5).setErrorEnabled(true);
    }

    @Override
    public void confirmPasswordNotEmpty() {
        til_signup_personal.get(5).setErrorEnabled(false);
    }

    @Override
    public void passMatching() {
        til_signup_personal.get(4).setErrorEnabled(false);
        til_signup_personal.get(5).setErrorEnabled(false);
    }

    @Override
    public void passNotMatching() {
        til_signup_personal.get(5).setError(pass_mismatch);
        til_signup_personal.get(5).setErrorEnabled(true);
    }

    @Override
    public void enableNext() {
        tv_sign_up_nextBtn.setEnabled(true);
        tv_sign_up_nextBtn.setBackgroundResource(R.drawable.selector_layout);
    }

    @Override
    public void disableNext() {
        tv_sign_up_nextBtn.setBackgroundColor(grey_bg);
        tv_sign_up_nextBtn.setEnabled(false);
    }

    @Override
    public void amazonUploadSuccess(String url) {
        Profile_pic_url = url;
        Uri uri = Uri.parse(Profile_pic_url);
        double size[]= Scaler.getScalingFactor(this);
        double height = (120)*size[1];
        double width = (120)*size[0];
        Picasso.get().load(uri)
                .resize((int)width, (int)height)
                .transform(new CircleTransform())
                .placeholder(signup_profile_default_image)
                .into(iv_sign_up_profile_pic);
        Utility.printLog("svg_profile pic : "+url);
    }

    @Override
    public void getStateList(ArrayList<StateData> data) {
        if (data != null && data.size() > 0) {
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putSerializable(DATA, data);
            mBundle.putSerializable(TYPE, STATE);
            mBundle.putString(TITLE, enterState);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_STATE);
        }
    }

    @Override
    public void setState(String state) {
        signupPersonalPresenter.validateAllFieldsFlags();
        et_sign_up_state.setText(state);
        et_signup_postal.requestFocus();
        signupPersonalPresenter.showKeyboard();
    }

    @Override
    public void getGenderList(ArrayList<Gender> genderList) {
        if (genderList != null && genderList.size() > 0) {
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);

            mBundle.putSerializable(DATA, genderList);
            mBundle.putSerializable(TYPE, GENDER);
            mBundle.putString(TITLE, enterGender);

            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_A_GENDER);
        }
    }

    @Override
    public void setGender(String gender) {
        signupPersonalPresenter.validateAllFieldsFlags();
        et_sign_up_gender.setText(gender);
    }


    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        pb_sign_up_progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideProgress() {
        pb_sign_up_progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
        overridePendingTransition(R.anim.stay_activity, R.anim.bottem_slide_up);
    }
}
