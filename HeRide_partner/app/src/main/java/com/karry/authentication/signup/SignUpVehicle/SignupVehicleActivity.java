package com.karry.authentication.signup.SignUpVehicle;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.karry.adapter.PreferenceListAdapter;
import com.karry.authentication.signup.SignUpData;
import com.heride.partner.R;
import com.karry.pojo.Signup.City;
import com.karry.pojo.Signup.ColorData;
import com.karry.pojo.Signup.MakeData;
import com.karry.pojo.Signup.ModelData;
import com.karry.pojo.Signup.PreferencesList;
import com.karry.pojo.Signup.Services;
import com.karry.pojo.Signup.TypeAndSpecialitiesData;
import com.karry.pojo.Signup.YearData;
import com.karry.authentication.signup.GenericListActivity;
import com.karry.authentication.signup.SignUpDocument.SignUpDocumentActivity;
import com.karry.utility.AppTypeFace;
import com.karry.utility.CircleTransform;
import com.karry.utility.ImageEditUpload;
import com.karry.utility.Scaler;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import dagger.android.support.DaggerAppCompatActivity;
import eu.janmuller.android.simplecropimage.CropImage;

import static com.karry.utility.VariableConstant.CAMERA_PIC;
import static com.karry.utility.VariableConstant.CITY;
import static com.karry.utility.VariableConstant.COLOR;
import static com.karry.utility.VariableConstant.CROP_IMAGE;
import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.GALLERY_PIC;
import static com.karry.utility.VariableConstant.SELECT_AN_COLOR;
import static com.karry.utility.VariableConstant.SELECT_AN_YEAR;
import static com.karry.utility.VariableConstant.SIGNUP_DATA;
import static com.karry.utility.VariableConstant.TITLE;
import static com.karry.utility.VariableConstant.TYPE;
import static com.karry.utility.VariableConstant.VEHICLE_MAKE;
import static com.karry.utility.VariableConstant.VEHICLE_MODEL;
import static com.karry.utility.VariableConstant.VEHICLE_SERVICE;
import static com.karry.utility.VariableConstant.VEHICLE_TYPE;
import static com.karry.utility.VariableConstant.YEAR;

/**
 * <h1>SignupVehicleActivity</h1>
 * <p>this is the activity of part SignUp. The Activity will start from the SignUpPersonalActivity,
 * which pass the bundle of date that filled. and this Activity contain the vehicle details has to fill.
 * after validate the details which filled in this activity next goes to SignUpDocumentActivity.</p>
 */
public class SignupVehicleActivity extends DaggerAppCompatActivity implements
        SignupVehicleContract.SignupVehicleView {


    @Inject AppTypeFace appTypeFace;
    @Inject PreferenceListAdapter preferenceListAdapter;
    @Inject SignupVehicleContract.SignupVehiclePresenter signupVehiclePresenter;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindViews({R.id.et_sign_up_city,
            R.id.et_sign_up_type,
            R.id.et_sign_up_year,
            R.id.et_sign_up_make,
            R.id.et_sign_up_model,
            R.id.et_sign_up_color})List<EditText> et_vehicle_list;
    @BindView(R.id.et_sign_up_service) EditText et_sign_up_service;
    @BindView(R.id.til_plateNo) TextInputLayout til_plateNo;
    @BindView(R.id.et_plate_no) EditText et_plate_no;


    @BindView(R.id.tv_driver_preference_head) TextView tv_driver_preference_head;
    @BindView(R.id.rv_driver_preference_list) RecyclerView rv_driver_preference_list;
    @BindView(R.id.tv_vehicle_preference_head) TextView tv_vehicle_preference_head;
    @BindView(R.id.rv_vehicle_preference_list) RecyclerView rv_vehicle_preference_list;



    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_next) TextView tv_next;
    @BindView(R.id.iv_signup_vp) ImageView iv_signup_vp;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindColor(R.color.gray) int grey_bg;
    @BindString(R.string.signup_) String title;
    @BindString(R.string.mandatory) String mandatory;
    @BindString(R.string.enterYear) String enterYear;
    @BindString(R.string.select_city) String select_city;
    @BindString(R.string.select_service) String select_service;
    @BindString(R.string.err_make) String err_make;
    @BindString(R.string.err_model) String err_model;
    @BindString(R.string.err_type) String err_type;
    @BindString(R.string.enterColor) String enterColor;
    @BindString(R.string.err_veh_img) String err_veh_img;
    @BindString(R.string.read_phone_state_permission_message) String read_phone_state_permission_message;

    @BindDrawable(R.drawable.signup_vechicle_default_image) Drawable signup_vehicle_default_image;
    @BindDrawable(R.drawable.selector_layout) Drawable selector_layout;

    private static final int SELECT_AN_TYPE = 405;
    private static final int SELECT_A_CITY = 410;
    private static final int SELECT_AN_SERVICE = 411;
    private static final int SELECT_AN_Make = 407;
    private static final int SELECT_AN_MODEL = 408;

    private String City_ID,Type_ID,Service_ID,Make_ID,Model_ID;

    private String state;
    private String vehicle_pic_url = "";
    private String selectedYear;
    private String selectedMake;
    private String selectedColor;

    @Inject PreferenceListAdapter driverpreferenceListAdapter;
    @Inject PreferenceListAdapter vehiclepreferenceListAdapter;

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_vehicle);
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);
        ButterKnife.bind(this);
        initializeViews();
        signupVehiclePresenter.setActionBar();
    }

    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     */
    private void initializeViews() {
        et_plate_no.setTypeface(appTypeFace.getPro_News());
        tv_next.setTypeface(appTypeFace.getPro_narMedium());
        et_sign_up_service.setTypeface(appTypeFace.getPro_News());
        tv_vehicle_preference_head.setTypeface(appTypeFace.getPro_News());
        tv_driver_preference_head.setTypeface(appTypeFace.getPro_News());

        et_sign_up_service.setInputType(InputType.TYPE_NULL);
        for(int i=0;i<et_vehicle_list.size();i++)
        {
            et_vehicle_list.get(i).setTypeface(appTypeFace.getPro_News());
            et_vehicle_list.get(i).setInputType(InputType.TYPE_NULL);
        }
        tv_next.setEnabled(false);

        signupVehiclePresenter.getPersonalData(getIntent().getExtras());

    }

    /**********************************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case CAMERA_PIC:
                startCropImage();
                break;

            case GALLERY_PIC:
                try {
                    Utility.printLog("RegistrationAct in GALLERY_PIC:");
                    String takenNewImage;
                    state = Environment.getExternalStorageState();
                    takenNewImage = VariableConstant.PARENT_FOLDER + String.valueOf(System.nanoTime()) + ".png";

                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        VariableConstant.newFile = new File(Environment.getExternalStorageDirectory() , takenNewImage);
                    } else {
                        VariableConstant.newFile = new File(getFilesDir(), takenNewImage);
                    }

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(VariableConstant.newFile);

                    Utility.copyStream(inputStream, fileOutputStream);

                    fileOutputStream.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);

                    Utility.printLog("RegistrationAct in GALLERY_PIC fileOutputStream: " + fileOutputStream);
                    startCropImage();
                } catch (Exception e) {
                    Utility.printLog("RegistrationAct in GALLERY_PIC Error while creating newfile:" + e);
                }
                break;

            case CROP_IMAGE:
                if(data!=null)
                    signupVehiclePresenter.cropImage(data);
                break;

            case SELECT_A_CITY:
                if(data!=null)
                    signupVehiclePresenter.onCitySelected(data);
                break;

            case SELECT_AN_TYPE:
                if(data!=null)
                    signupVehiclePresenter.onTypeSelected(data,Type_ID);
                break;

            case SELECT_AN_SERVICE:
                if(data!=null)
                    signupVehiclePresenter.onServiceSelected(data,Type_ID);
                break;

            case SELECT_AN_YEAR:
                if(data!=null)
                    signupVehiclePresenter.onYearSelected(data);
                break;

            case SELECT_AN_Make:
                if(data!=null)
                    signupVehiclePresenter.onmakeSelected(data);
                break;

            case SELECT_AN_MODEL:
                if(data!=null)
                    signupVehiclePresenter.onModelSelected(data);
                break;
            case SELECT_AN_COLOR:
                if(data!=null)
                    signupVehiclePresenter.onColorSelected(data);
                break;

            default:
                break;
        }

    }


    /**
     * <h1>startCropImage</h1>
     * <p>the intent for crop the Image</p>
     */
    private void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, VariableConstant.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);
        startActivityForResult(intent, CROP_IMAGE);
    }


    /**********************************************************************************************/
    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        signupVehiclePresenter.setActionBarTitle();
    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    /**********************************************************************************************/
    @OnFocusChange({R.id.et_plate_no,
            R.id.et_sign_up_city,
            R.id.et_sign_up_type,
            R.id.et_sign_up_service,
            R.id.et_sign_up_year,
            R.id.et_sign_up_make,
            R.id.et_sign_up_model,
            R.id.et_sign_up_color})
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId())
        {
            case R.id.et_plate_no:
                if (!hasFocus)
                {
                    signupVehiclePresenter.validatePlateNo(et_plate_no.getText().toString());
                    signupVehiclePresenter.validateToNext();
                }
                break;
            case R.id.et_sign_up_city:
                if (hasFocus)
                {
                    signupVehiclePresenter.hideKeyboardAndClearFocus();
                    signupVehiclePresenter.cityOnClick();
                    signupVehiclePresenter.validateToNext();
                }
                break;
            case R.id.et_sign_up_type:
                if (hasFocus) {
                    signupVehiclePresenter.hideKeyboardAndClearFocus();
                    signupVehiclePresenter.typeOnClick();
                    signupVehiclePresenter.validateToNext();
                }
                break;

            case R.id.et_sign_up_service:
                if (hasFocus) {
                    signupVehiclePresenter.hideKeyboardAndClearFocus();
                    signupVehiclePresenter.serviceOnClick();
                    signupVehiclePresenter.validateToNext();
                }
                break;
            case R.id.et_sign_up_year:
                if (hasFocus) {
                    signupVehiclePresenter.hideKeyboardAndClearFocus();
                    signupVehiclePresenter.yearOnClick();
                    signupVehiclePresenter.validateToNext();
                }
                break;
            case R.id.et_sign_up_make:
                if (hasFocus) {
                    signupVehiclePresenter.hideKeyboardAndClearFocus();
                    if (selectedYear != null) {
                        signupVehiclePresenter.makeOnClick(selectedYear);
                    }
                    signupVehiclePresenter.validateToNext();
                }
                break;
            case R.id.et_sign_up_model:
                if (hasFocus) {
                    signupVehiclePresenter.hideKeyboardAndClearFocus();
                    if (selectedYear != null && selectedMake != null) {
                        signupVehiclePresenter.modelOnClick(selectedYear, selectedMake);
                    }
                    signupVehiclePresenter.validateToNext();
                }
                break;
            case R.id.et_sign_up_color:
                if (hasFocus) {
                    signupVehiclePresenter.colorOnClick();
                    signupVehiclePresenter.validateToNext();
                }
                break;
        }
    }

    /**********************************************************************************************/
    @OnClick
            ({R.id.tv_next,R.id.iv_signup_vp,R.id.et_sign_up_service,
                    R.id.et_sign_up_city,R.id.et_sign_up_type,
                    R.id.et_sign_up_year,R.id.et_sign_up_make,
                    R.id.et_sign_up_model,R.id.et_sign_up_color})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.et_sign_up_city:
                signupVehiclePresenter.cityOnClick();
                break;

            case R.id.et_sign_up_type:
                signupVehiclePresenter.typeOnClick();
                break;

            case R.id.et_sign_up_service:
                signupVehiclePresenter.serviceOnClick();
                break;

            case R.id.et_sign_up_year:
                signupVehiclePresenter.yearOnClick();
                break;

            case R.id.et_sign_up_make:
                if (selectedYear!=null)
                    signupVehiclePresenter.makeOnClick(selectedYear);
                break;

            case R.id.et_sign_up_model:
                if (selectedYear!=null && selectedMake!=null)
                    signupVehiclePresenter.modelOnClick(selectedYear,selectedMake);
                break;

            case R.id.iv_signup_vp:
                new ImageEditUpload(this,null);
                break;

            case R.id.tv_next:
                signupVehiclePresenter.validateAndStartActivity(et_plate_no.getText().toString(),
                        City_ID,Type_ID,Service_ID,Make_ID,Model_ID,selectedYear,selectedColor,vehicle_pic_url);
                break;

            case R.id.et_sign_up_color:
                signupVehiclePresenter.colorOnClick();
                break;
        }

    }

    @Override
    public void vehicleImgErr() {
        Utility.BlueToast(this, err_veh_img);
    }

    @Override
    public void startDocumentSignUp(SignUpData signUpData) {
        Intent mIntent = new Intent(this, SignUpDocumentActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(SIGNUP_DATA,signUpData);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }


    @Override
    public void inValidPlateNoAPI(String errMsg) {
        til_plateNo.setErrorEnabled(true);
        til_plateNo.setError(errMsg);
    }

    @Override
    public void driverPreferenceDataForAdapter(ArrayList<PreferencesList> preferencesList) {

        if(preferencesList.size()==0)
            tv_driver_preference_head.setVisibility(View.GONE);
        else
            tv_driver_preference_head.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        rv_driver_preference_list.setLayoutManager(linearLayoutManager);
        driverpreferenceListAdapter.setData(preferencesList,0);
        driverpreferenceListAdapter.getNotifyPreferneceCheck(new PreferenceListAdapter.NotifyPreferneceCheck() {
            @Override
            public void preferenceChange(ArrayList<PreferencesList> preferencesList) {
                final String[] prefernceID = {""};
                for(int i = 0;i<(preferencesList.size());i++){
                    if(preferencesList.get(i).isSelected()) {
                        if(!prefernceID[0].matches(""))
                            prefernceID[0] = prefernceID[0].concat(",");
                        prefernceID[0] = prefernceID[0].concat(preferencesList.get(i).getId());
                        signupVehiclePresenter.setDriverBookingPreferenceList(prefernceID[0]);

                    }
                }
                Utility.printLog("the prefernce list is : "+prefernceID[0]);
            }
        });
        rv_driver_preference_list.setAdapter(driverpreferenceListAdapter);
        driverpreferenceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void vehiclePreferenceDataForAdapter(ArrayList<PreferencesList> preferencesList) {
        if(preferencesList.size()==0)
            tv_vehicle_preference_head.setVisibility(View.GONE);
        else
            tv_vehicle_preference_head.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        rv_vehicle_preference_list.setLayoutManager(linearLayoutManager);
        vehiclepreferenceListAdapter.setData(preferencesList,0);
        vehiclepreferenceListAdapter.getNotifyPreferneceCheck(new PreferenceListAdapter.NotifyPreferneceCheck() {
            @Override
            public void preferenceChange(ArrayList<PreferencesList> preferencesList) {
                final String[] prefernceID = {""};
                for(int i = 0;i<(preferencesList.size());i++){
                    if(preferencesList.get(i).isSelected()) {
                        if(!prefernceID[0].matches(""))
                            prefernceID[0] = prefernceID[0].concat(",");
                        prefernceID[0] = prefernceID[0].concat(preferencesList.get(i).getId());
                        signupVehiclePresenter.setVehicleBookingPreferenceList(prefernceID[0]);

                    }
                }
                Utility.printLog("the prefernce list is : "+prefernceID[0]);
            }
        });
        rv_vehicle_preference_list.setAdapter(vehiclepreferenceListAdapter);
        vehiclepreferenceListAdapter.notifyDataSetChanged();
    }

    /**********************************************************************************************/
    /*@OnFocusChange({R.id.et_plate_no})
    public void afterTextChanged(View v, boolean hasFocus) {

            signupVehiclePresenter.validatePlateNo(et_plate_no.getText().toString());
            signupVehiclePresenter.validateToNext();
    }*/

    /**********************************************************************************************/
    @Override
    public void getCitylist(ArrayList<City> data) {
        if (data != null && data.size() > 0) {
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putSerializable(DATA, data);
            mBundle.putSerializable(TYPE, CITY);
            mBundle.putString(TITLE, select_city);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_A_CITY);
        }

    }

    @Override
    public void setCityText(String city, String City_ID) {
        et_vehicle_list.get(0).setText(city);
        this.City_ID = City_ID;
    }

    @Override
    public void getTypelist(ArrayList<TypeAndSpecialitiesData> typeAndSpecialitiesData) {
        if (typeAndSpecialitiesData != null && typeAndSpecialitiesData.size() != 0) {
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putString(TITLE, err_type);
            mBundle.putString(TYPE, VEHICLE_TYPE);
            mBundle.putSerializable(DATA, typeAndSpecialitiesData);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_TYPE);
        }
    }

    @Override
    public void setTypeText(String Type, String Type_ID) {
        et_vehicle_list.get(1).setText(Type);
        this.Type_ID = Type_ID;
        signupVehiclePresenter.preferencesAPI(Type_ID);


    }

    @Override
    public void getServicelist(ArrayList<Services> services) {
        if (services != null && services.size() != 0) {
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putString(TITLE, select_service);
            mBundle.putString(TYPE, VEHICLE_SERVICE);
            mBundle.putSerializable(DATA, services);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_SERVICE);
        }
    }

    @Override
    public void setServiceText(String services, String Service_ID) {
        et_sign_up_service.setText(services);
        this.Service_ID = Service_ID;
    }

    @Override
    public void getMakelist(ArrayList<MakeData> makeModelData) {
        if (makeModelData != null && makeModelData.size() != 0) {
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putString(TITLE, err_make);
            mBundle.putString(TYPE, VEHICLE_MAKE);
            mBundle.putSerializable(DATA, makeModelData);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_Make);
        }
    }

    @Override
    public void setMakeText(String make, String Make_ID) {
        et_vehicle_list.get(3).setText(make);
        this.Make_ID = Make_ID;
        selectedMake = Make_ID;
    }

    @Override
    public void getModellist(ArrayList<ModelData> models) {
        if (models != null && models.size() != 0) {
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putString(TITLE, err_model);
            mBundle.putString(TYPE, VEHICLE_MODEL);
            mBundle.putSerializable(DATA, models);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_MODEL);
        }
    }

    @Override
    public void setModelText(String model,String Model_ID) {
        et_vehicle_list.get(4).setText(model);
        this.Model_ID = Model_ID;
    }

    @Override
    public void clearTypeService() {
        et_vehicle_list.get(1).setText(err_type);
        signupVehiclePresenter.validateToNext();
        et_sign_up_service.setText(select_service);

    }

    @Override
    public void clearMakeModel() {
        et_vehicle_list.get(3).setText(err_make);
        et_vehicle_list.get(4).setText(err_model);
        signupVehiclePresenter.validateToNext();
    }

    @Override
    public void clearModel() {
        et_vehicle_list.get(4).setText(err_model);
        signupVehiclePresenter.validateToNext();
    }

    @Override
    public void validPlateNo() {
        til_plateNo.setErrorEnabled(false);
    }

    @Override
    public void InvalidPlateNo() {
        til_plateNo.setErrorEnabled(true);
        til_plateNo.setError(mandatory);
    }


    @Override
    public void enableNext() {
        tv_next.setEnabled(true);
        tv_next.setBackground(selector_layout);
    }

    @Override
    public void disableNext() {
        tv_next.setEnabled(false);
        tv_next.setBackgroundColor(grey_bg);
    }

    @Override
    public void amazonUploadSuccess(String url) {
        vehicle_pic_url = url;
        Uri uri = Uri.parse(vehicle_pic_url);
        double size[]= Scaler.getScalingFactor(this);
        double height = (120)*size[1];
        double width = (120)*size[0];
        Picasso.get().load(uri)
                .resize((int)width, (int)height)
                .transform(new CircleTransform())
                .placeholder(signup_vehicle_default_image)
                .into(iv_signup_vp);
        Utility.printLog("Vehicle pic : "+url);
    }

    @Override
    public void getYearList(ArrayList<YearData> yearList) {
        if (yearList != null && yearList.size() > 0) {
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putSerializable(DATA, yearList);
            mBundle.putSerializable(TYPE, YEAR);
            mBundle.putString(TITLE, enterYear);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_YEAR);
        }
    }

    @Override
    public void getColorList(ArrayList<ColorData> colorList) {
        if (colorList != null && colorList.size() > 0) {
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putSerializable(DATA, colorList);
            mBundle.putSerializable(TYPE, COLOR);
            mBundle.putString(TITLE, enterColor);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_COLOR);
        }
    }

    @Override
    public void setYear(String year) {
        et_vehicle_list.get(2).setText(year);
        selectedYear = year;
    }

    @Override
    public void setColor(String color) {
        et_vehicle_list.get(5).setText(color);
        selectedColor = color;
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
    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
