package com.karry.side_screens.bankDetails.bankStripeAccountAdd;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karry.authentication.signup.GenericListActivity;
import com.heride.partner.R;
import com.karry.data.source.local.PreferencesHelper;
import com.karry.pojo.bank.LegalEntity;
import com.karry.pojo.bank.StripeDetailsPojo;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCountryList;
import com.karry.utility.AppTypeFace;
import com.karry.utility.ImageEditUpload;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import dagger.android.support.DaggerAppCompatActivity;
import eu.janmuller.android.simplecropimage.CropImage;

import static com.karry.utility.VariableConstant.CAMERA_PIC;
import static com.karry.utility.VariableConstant.COUNTRY_SELECT;
import static com.karry.utility.VariableConstant.CROP_IMAGE;
import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.GALLERY_PIC;
import static com.karry.utility.VariableConstant.TITLE;
import static com.karry.utility.VariableConstant.TYPE;
import static com.karry.utility.VariableConstant.UNVERIFIED_BANK_DETAILS;

public class BankStripeAddActivity extends DaggerAppCompatActivity
        implements BankStripeAddContract.BankStripeAddView {


    @Inject BankStripeAddContract.BankStripeAddPresenter bankStripeAddPresenter;
    @Inject AppTypeFace appTypeFace;


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_logout) TextView tv_save;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindString(R.string.addStripeAcount)String title;
    @BindString(R.string.save)String save;
    @BindString(R.string.app_name) String app_name;

    @BindView(R.id.tvIdProof)TextView tvIdProof;
    @BindView(R.id.ivAddFile)ImageView ivAddFile;

    @BindView(R.id.etFName)EditText etFName;
    @BindView(R.id.etLName)EditText etLName;
    @BindView(R.id.etDob)EditText etDob;
    @BindView(R.id.etPersonalId)EditText etPersonalId;
    @BindView(R.id.etAddress)EditText etAddress;
    @BindView(R.id.etCity)EditText etCity;
    @BindView(R.id.etState)EditText etState;
    @BindView(R.id.etCountry)EditText etCountry;
    @BindView(R.id.etPostalCode)EditText etPostalCode;
    @BindView(R.id.et_accountCountry)EditText et_accountCountry;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @BindView(R.id.tilName)TextInputLayout tilName;
    @BindView(R.id.tilLastName)TextInputLayout tilLastName;
    @BindView(R.id.tilDob)TextInputLayout tilDob;
    @BindView(R.id.tilPersonalId)TextInputLayout tilPersonalId;
    @BindView(R.id.tilAddress)TextInputLayout tilAddress;
    @BindView(R.id.tilCity)TextInputLayout tilCity;
    @BindView(R.id.tilState)TextInputLayout tilState;
    @BindView(R.id.tilCountry)TextInputLayout tilCountry;
    @BindView(R.id.tilPostalCode)TextInputLayout tilPostalCode;
    @BindView(R.id.til_accountCountry)TextInputLayout til_accountCountry;


    @BindView(R.id.progressBar)ProgressBar progressBar;


    private static final int SELECT_AN_COUNTRY = 409;


    private static String ipAddress;
    int currentYear,month,day;
    String[] dateofbirth;
    private String documentURL = "";
    private LegalEntity mLegalEntity = null;
    private Boolean isSecondTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_new_stripe_details);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);
        bankStripeAddPresenter.setActionBar();
        bankStripeAddPresenter.fetchIP();
        bankStripeAddPresenter.getData(getIntent().getExtras());
        if(getIntent().hasExtra(UNVERIFIED_BANK_DETAILS)){
            Intent intent = getIntent();
             mLegalEntity = (LegalEntity)intent.getExtras().get(UNVERIFIED_BANK_DETAILS);
            isSecondTime = true;
        }
        else {
            isSecondTime = false;
        }
        initViews();


    }

    /**
     * <h1>initActionBar</h1>
     * initilize the action bar
     */

    private void initViews() {

        if(isSecondTime){
           // Glide.with(this).load(mPreferencesHelper.getStripImage()).into(ivAddFile);
            String dob = mLegalEntity.getDob().getYear()+"-"+mLegalEntity.getDob().getMonth()+"-"+mLegalEntity.getDob().getDay();
            etDob.setText(dob);
            etFName.setText(mLegalEntity.getFirstName());
            etLName.setText(mLegalEntity.getLastName());
           // etPersonalId.setText(mLegalEntity.get);
            etAddress.setText(mLegalEntity.getAddress().getLine1());
            etCity.setText(mLegalEntity.getAddress().getCity());
            etState.setText(mLegalEntity.getAddress().getState());
            etPostalCode.setText(mLegalEntity.getAddress().getPostalCode());
            et_accountCountry.setText(mLegalEntity.getAddress().getCountry());

        }
        else {
            et_accountCountry.setInputType(InputType.TYPE_NULL);


            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.vector_cancel);
            }

        }
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateofbirth = new String[3];

    }


    @Override
    public void setText(StripeDetailsPojo stripeDetailsPojo) {
        if(stripeDetailsPojo.getLegal_entity().getFirstName()!=null)
            etFName.setText(stripeDetailsPojo.getLegal_entity().getFirstName());
        if (stripeDetailsPojo.getLegal_entity().getLastName()!=null)
            etLName.setText(stripeDetailsPojo.getLegal_entity().getLastName());
        etCity.setText(stripeDetailsPojo.getLegal_entity().getAddress().getCity());
        etCountry.setText(stripeDetailsPojo.getLegal_entity().getAddress().getCountry());
        etPostalCode.setText(stripeDetailsPojo.getLegal_entity().getAddress().getPostalCode());
        etAddress.setText(stripeDetailsPojo.getLegal_entity().getAddress().getLine1());
        etState.setText(stripeDetailsPojo.getLegal_entity().getAddress().getState());

        String dob=Utility.sentingDateFormat(Integer.parseInt(stripeDetailsPojo.getLegal_entity().getDob().getDay()),
                Integer.parseInt(stripeDetailsPojo.getLegal_entity().getDob().getMonth()),
                Integer.parseInt(stripeDetailsPojo.getLegal_entity().getDob().getYear()));
        etDob.setText(dob);

    }


    @OnClick({R.id.ivAddFile, R.id.tv_logout, R.id.etDob, R.id.tilDob })
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.ivAddFile:
                new ImageEditUpload(this,null);
                break;

            case R.id.tv_logout:
                bankStripeAddPresenter.validateData(etFName.getText().toString(),
                        etLName.getText().toString(),
                        etDob.getText().toString(),
                        etPersonalId.getText().toString(),
                        etAddress.getText().toString(),
                        etCity.getText().toString(),
                        etState.getText().toString(),
                        etPostalCode.getText().toString(),
                        documentURL);
                break;

            case R.id.etDob:
            case R.id.tilDob:
                bankStripeAddPresenter.hideKeyboardAndClearFocus();
                datePicker();
                break;

            case R.id.et_accountCountry:
                hideSoftKeyboard();
                bankStripeAddPresenter.accountCountryAPI();
                break;

        }
    }
    @OnFocusChange({R.id.etDob, R.id.et_accountCountry})
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {

            case R.id.etDob:
                if (hasFocus)
                {
                    bankStripeAddPresenter.hideKeyboardAndClearFocus();
                    datePicker();
                }
                break;

            case R.id.et_accountCountry:
                if(hasFocus) {
                    bankStripeAddPresenter.hideKeyboardAndClearFocus();
                    bankStripeAddPresenter.accountCountryAPI();
                }
                break;

            case R.id.etPostalCode:
                if(!hasFocus) {
                    bankStripeAddPresenter.hideKeyboardAndClearFocus();
                    et_accountCountry.requestFocus();
                }
                break;
        }
    }


    void datePicker()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dob, currentYear, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dob=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            int month = monthOfYear+1;

            String dob=Utility.sentingDateFormat(dayOfMonth,monthOfYear,year);
            etDob.setText(dob);
            dateofbirth[0] = String.valueOf(dayOfMonth);
            dateofbirth[1] = String.valueOf(month);
            dateofbirth[2] = String.valueOf(year);
        }
    };


    /**********************************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

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
                    bankStripeAddPresenter.cropImage(data);
                break;

            case SELECT_AN_COUNTRY:
                if(data!=null)
                    bankStripeAddPresenter.onCountrySelected(data);
                break;
        }
    }

    private void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, VariableConstant.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);
        intent.putExtra("locale", Locale.getDefault().getDisplayLanguage());
        startActivityForResult(intent, CROP_IMAGE);
    }

    @Override
    public void amazonUploadSuccess(String url) {
        documentURL = url;

        Picasso.get()
                .load(url)
                .into(ivAddFile);
        Utility.printLog("documentURL : "+url);
    }

    @Override
    public void editTextErr(String errorEditText) {
        switch (errorEditText){
            case "Fname":
                tilName.setErrorEnabled(true);
                tilName.setError(getString(R.string.enterAccountHoldername));
                break;
            case "Lname":
                tilName.setErrorEnabled(false);
                tilLastName.setErrorEnabled(true);
                tilLastName.setError(getString(R.string.enterLastName));
                break;
            case "DOB":
                tilName.setErrorEnabled(false);
                tilLastName.setErrorEnabled(false);
                tilDob.setErrorEnabled(true);
                tilDob.setError(getString(R.string.enterDob));
                break;
            case "PersonalID":
                tilLastName.setErrorEnabled(false);
                tilName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(true);
                tilPersonalId.setError(getString(R.string.enterPersonalid));
                break;
            case "PersonalIDLength":
                tilLastName.setErrorEnabled(false);
                tilName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(true);
                tilPersonalId.setError(getString(R.string.enterValidPersonalid));
                break;
            case "Address":
                tilLastName.setErrorEnabled(false);
                tilName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(false);
                tilAddress.setErrorEnabled(true);
                tilAddress.setError(getString(R.string.enterAddress));
                break;
            case "City":
                tilLastName.setErrorEnabled(false);
                tilName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                tilCity.setErrorEnabled(true);
                tilCity.setError(getString(R.string.enterCity));
                break;
            case "State":
                tilLastName.setErrorEnabled(false);
                tilName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(false);
                tilCity.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                tilState.setErrorEnabled(true);
                tilState.setError(getString(R.string.enterState));
                break;
            case "Country":
                tilLastName.setErrorEnabled(false);
                tilName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(false);
                tilCity.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                tilState.setErrorEnabled(false);
                tilCountry.setErrorEnabled(true);
                tilCountry.setError(getString(R.string.enteCountry));
                break;
            case "PostalCode":
                tilName.setErrorEnabled(false);
                tilLastName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(false);
                tilState.setErrorEnabled(false);
                tilCity.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                tilPostalCode.setErrorEnabled(true);
                tilPostalCode.setError(getString(R.string.enterPostalCode));
                break;
            case "CountryID":
                tilName.setErrorEnabled(false);
                tilLastName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(false);
                tilState.setErrorEnabled(false);
                tilPostalCode.setErrorEnabled(false);
                tilCity.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                til_accountCountry.setErrorEnabled(true);
                til_accountCountry.setError(getString(R.string.select_counter));
                break;
            case "DocumentImage":
                tilName.setErrorEnabled(false);
                tilLastName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(false);
                tilState.setErrorEnabled(false);
                tilPostalCode.setErrorEnabled(false);
                tilCity.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                til_accountCountry.setErrorEnabled(false);
                Toast.makeText(this, getString(R.string.plsUploadIdProf), Toast.LENGTH_SHORT).show();
                break;
            default:
                tilLastName.setErrorEnabled(false);
                tilName.setErrorEnabled(false);
                tilDob.setErrorEnabled(false);
                tilPersonalId.setErrorEnabled(false);
                tilState.setErrorEnabled(false);
                tilPostalCode.setErrorEnabled(false);
                tilCity.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                til_accountCountry.setErrorEnabled(false);
                bankStripeAddPresenter.postConnectAccountAPI(etFName.getText().toString(),
                        etLName.getText().toString(),
                        dateofbirth,
                        etPersonalId.getText().toString(),
                        etAddress.getText().toString(),
                        etCity.getText().toString(),
                        etState.getText().toString(),
                        etPostalCode.getText().toString(),
                        documentURL, ipAddress);
                break;
        }
    }

    @Override
    public void getIpAddress(String ipAddres) {
        ipAddress = ipAddres;
    }

    @Override
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
    }

    @Override
    public void addStripeSuccess(String msg) {
        onBackPressed();
    }

    @Override
    public void setCountryListforSelect(ArrayList<ConnectAccountCountryList> countryListforSelect) {
        if(countryListforSelect != null && countryListforSelect.size()>0){
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putString(TITLE, getResources().getString(R.string.accountCountry));
            mBundle.putString(TYPE, COUNTRY_SELECT);
            mBundle.putSerializable(DATA, countryListforSelect);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_COUNTRY);
        }
    }

    @Override
    public void setCountryText(String country, String countryCode) {
        et_accountCountry.setText(country);
        etFName.requestFocus();
        bankStripeAddPresenter.hideKeyboardAndClearFocus();
    }

    /**********************************************************************************************/
    @Override
    public void initActionBar() {

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        tv_save.setVisibility(View.VISIBLE);
        tv_save.setTypeface(appTypeFace.getPro_narMedium());
        bankStripeAddPresenter.setActionBarTitle();

    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
        tv_save.setText(save);
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
    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("FIRST_NAME",etFName.getText().toString());
        outState.putString("LAST_NAME",etLName.getText().toString());
        outState.putString("DOB",etDob.getText().toString());
        outState.putString("PERSONAL_ID",etPersonalId.getText().toString());
        outState.putString("ADDRESS",etAddress.getText().toString());
        outState.putString("CITY",etCity.getText().toString());
        outState.putString("STATE",etState.getText().toString());
        outState.putString("POSTAL_CODE",etPostalCode.getText().toString());
        outState.putString("COUNTRY",et_accountCountry.getText().toString());
        Utility.printLog("Form data is saved into");
    }
}
