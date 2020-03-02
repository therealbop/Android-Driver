package com.karry.side_screens.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karry.adapter.PreferenceListAdapter;
import com.karry.app.mainActivity.MainActivity;
import com.karry.authentication.login.LoginActivity;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.dagger.ActivityScoped;
import com.heride.partner.R;
import com.karry.pojo.Signup.PreferencesList;
import com.karry.side_screens.profile.profile_model.ProfileData;
import com.karry.service.LocationUpdateService;
import com.karry.utility.AppConstants;
import com.karry.utility.AppTypeFace;
import com.karry.utility.CircleTransform;
import com.karry.utility.DialogHelper;
import com.karry.utility.ImageEditUpload;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import eu.janmuller.android.simplecropimage.CropImage;

import static com.karry.utility.VariableConstant.LOGIN;

/**************************************************************************************************/
@ActivityScoped
public class MyProfileFrag extends DaggerFragment implements MyProfileContract.View{

    @BindView(R.id.et_first_name) EditText et_first_name;
    @BindView(R.id.et_last_name) EditText et_last_name;
    @BindView(R.id.et_phone) EditText et_phone;
    @BindView(R.id.et_email) EditText et_email;
    @BindView(R.id.et_vehicle_type) EditText et_vehicle_type;
    @BindView(R.id.et_vehicle_number) EditText et_vehicle_number;
    @BindView(R.id.et_commission_plan) EditText et_commission_plan;
    @BindView(R.id.til_mob) TextInputLayout til_mob;
    @BindView(R.id.til_email) TextInputLayout til_email;
    @BindView(R.id.iv_profile) ImageView iv_profile;
    @BindView(R.id.iv_profile_plus) ImageView iv_profile_plus;
    @BindView(R.id.bt_change_password) Button bt_change_password;
    @BindView(R.id.bt_logout) Button bt_logout;
    @BindView(R.id.ll_non_editable) LinearLayout ll_non_editable;
    @BindView(R.id.tv_vehicleTitle) TextView tv_vehicleTitle;
    @BindView(R.id.et_vehicle_service) EditText et_vehicle_service;

    @BindView(R.id.tv_profile_makeValue) TextView tv_profile_makeValue;
    @BindView(R.id.tv_profile_modelValue) TextView tv_profile_modelValue;
    @BindView(R.id.tv_profile_colorValue) TextView tv_profile_colorValue;
    @BindView(R.id.tv_profile_yearValue) TextView tv_profile_yearValue;
    @BindView(R.id.tv_profile_makeTitle) TextView tv_profile_makeTitle;
    @BindView(R.id.tv_profile_modelTitle) TextView tv_profile_modelTitle;
    @BindView(R.id.tv_profile_colorTitle) TextView tv_profile_colorTitle;
    @BindView(R.id.tv_profile_yearTitle) TextView tv_profile_yearTitle;
    @BindView(R.id.tv_selected_language) TextView tv_selected_language;
    @BindView(R.id.tv_language) TextView tv_language;

    @BindView(R.id.ll_edit_phone) LinearLayout ll_edit_phone;
    @BindView(R.id.ll_edit_email) LinearLayout ll_edit_email;
    private MenuItem item;

    @BindString(R.string.app_name) String app_name;
    @BindString(R.string.edit) String edit;
    @BindString(R.string.save) String save;
    @BindString(R.string.enter_first_name) String enter_first_name;
    @BindString(R.string.enter_last_name) String enter_last_name;
    @BindString(R.string.alert) String alert;
    @BindString(R.string.ok) String ok;
    @BindString(R.string.message) String message;
    @BindString(R.string.updating) String updating;
    @BindDrawable(R.drawable.ic_edit_24dp) Drawable ic_edit;
    @BindDrawable(R.drawable.drop_down) Drawable drop_down;

    @Inject AppTypeFace appTypeFace;
    @Inject MyProfileContract.Presenter presenter;
    @Inject DialogHelper dialogHelper;
    @Inject @Named(LOGIN) ArrayList<LanguagesList> languagesLists = new ArrayList<>();


    @BindView(R.id.tv_driver_preference_head) TextView tv_driver_preference_head;
    @BindView(R.id.rv_driver_preference_list) RecyclerView rv_driver_preference_list;
    @BindView(R.id.tv_vehicle_preference_head) TextView tv_vehicle_preference_head;
    @BindView(R.id.rv_vehicle_preference_list) RecyclerView rv_vehicle_preference_list;

    @Inject PreferenceListAdapter driverpreferenceListAdapter;
    @Inject PreferenceListAdapter vehiclepreferenceListAdapter;


    public static boolean profileFrag = false;
    private String TAG = MyProfileFrag.class.getSimpleName();
    private ProgressDialog pDialog;
    private static final int CAMERA_PIC = 11, GALLERY_PIC = 12, CROP_IMAGE = 13, REMOVE_IMAGE = 14;
    private String imageUrl ="";
    private Unbinder unbinder;

    @Inject public MyProfileFrag()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    /**********************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        unbinder = ButterKnife.bind(this,rootView);
        initializeViews();
        ((MainActivity)getActivity()).setFragmentRefreshListener(this::onResume);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        profileFrag = true;
        if(VariableConstant.IS_PROFILE_EDITED)
        {
            VariableConstant.IS_PROFILE_EDITED = false;
            presenter.getProfileDetails();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        profileFrag = false;
    }

    /*********************************************************************************************/
    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     */
    private void initializeViews() {

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(updating);
        pDialog.setCancelable(false);

        et_first_name.setTypeface(appTypeFace.getPro_News());
        et_last_name.setTypeface(appTypeFace.getPro_News());
        et_phone.setTypeface(appTypeFace.getPro_News());
        et_email.setTypeface(appTypeFace.getPro_News());
        et_vehicle_type.setTypeface(appTypeFace.getPro_News());
        et_vehicle_service.setTypeface(appTypeFace.getPro_News());
        et_vehicle_number.setTypeface(appTypeFace.getPro_News());
        et_commission_plan.setTypeface(appTypeFace.getPro_News());
        bt_change_password.setTypeface(appTypeFace.getPro_News());
        bt_logout.setTypeface(appTypeFace.getPro_News());
        tv_vehicleTitle.setTypeface(appTypeFace.getPro_News());

        tv_profile_makeValue.setTypeface(appTypeFace.getPro_News());
        tv_profile_modelValue.setTypeface(appTypeFace.getPro_News());
        tv_profile_colorValue.setTypeface(appTypeFace.getPro_News());
        tv_profile_yearValue.setTypeface(appTypeFace.getPro_News());
        tv_profile_makeTitle.setTypeface(appTypeFace.getPro_News());
        tv_profile_modelTitle.setTypeface(appTypeFace.getPro_News());
        tv_profile_colorTitle.setTypeface(appTypeFace.getPro_News());
        tv_profile_yearTitle.setTypeface(appTypeFace.getPro_News());
        tv_selected_language.setTypeface(appTypeFace.getPro_News());
        tv_language.setTypeface(appTypeFace.getPro_News());
        tv_driver_preference_head.setTypeface(appTypeFace.getPro_News());
        tv_vehicle_preference_head.setTypeface(appTypeFace.getPro_News());

        presenter.attachView(this);
        presenter.getProfileDetails();
        enableDisableEdit(false);

        dialogHelper.getDialogCallbackHelper(new DialogHelper.DialogCallbackHelper() {

            @Override
            public void walletFragOpen() {

            }

            @Override
            public void changeLanguage(String langCode, String langName, int dir) {

                if(Utility.isMyServiceRunning(LocationUpdateService.class,getActivity())){
                    Intent stopIntent = new Intent(getActivity(), LocationUpdateService.class);
                    stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                    getActivity().startService(stopIntent);
                }
                VariableConstant.lan = langCode;
                presenter.languageChanged(langCode,langName,dir);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case  R.id.menu_profile_edit:
                this.item = item;
                if(item.getTitle().equals(edit))
                {
                    enableDisableEdit(true);
                }
                else
                {
                    presenter.validateField(et_first_name.getText().toString(),et_last_name.getText().toString(),imageUrl);
                }
                break;
        }
        return true;
    }


    @OnClick({R.id.ll_edit_phone, R.id.ll_edit_email, R.id.iv_profile,
            R.id.bt_change_password, R.id.bt_logout,R.id.tv_selected_language})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_edit_phone:
                Intent intentName = new Intent(getActivity(), EditProfileActivity.class);
                intentName.putExtra("type", 1);
                startActivity(intentName);
                getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.ll_edit_email:
                Intent intentPhone = new Intent(getActivity(), EditProfileActivity.class);
                intentPhone.putExtra("type", 2);
                startActivity(intentPhone);
                getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.iv_profile:
                new ImageEditUpload(getActivity(),this);
                break;

            case R.id.bt_logout:
                pDialog.setMessage(getString(R.string.logging_out));
                DialogHelper.customAlertDialogLogout( getActivity());
                break;

            case R.id.bt_change_password:
                Intent intentChangePassword = new Intent(getActivity(), EditProfileActivity.class);
                intentChangePassword.putExtra("type", 3);
                startActivity(intentChangePassword);
                getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.tv_selected_language:
                presenter.getLanguages();
                break;

        }
    }

    @OnFocusChange({R.id.et_first_name, R.id.et_last_name})
    public void onFocuChange(View view,boolean hasFocus) {
        if (hasFocus) {
            switch (view.getId()) {
                case R.id.et_first_name:
                    et_first_name.setSelection(et_first_name.getText().length());
                    break;

                case R.id.et_last_name:
                    et_last_name.setSelection(et_last_name.getText().length());
                    break;
            }
        }
    }

    private void enableDisableEdit(boolean isEnable)
    {
        et_first_name.setEnabled(isEnable);
        et_last_name.setEnabled(isEnable);
        ll_edit_phone.setEnabled(isEnable);
        ll_edit_email.setEnabled(isEnable);
        iv_profile.setEnabled(isEnable);

        if(isEnable)
        {
            presenter.showKeyboard();
            item.setTitle(save);
            iv_profile_plus.setVisibility(View.VISIBLE);
            presenter.checkRTL();
            et_first_name.setSelection(et_first_name.getText().length());

            ll_non_editable.setVisibility(View.GONE);
        }
        else
        {
            if(item !=null)
                item.setTitle(edit);
            iv_profile_plus.setVisibility(View.GONE);
            et_phone.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            et_email.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

            ll_non_editable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setProfileImage(String profileImage) {

        imageUrl = profileImage;

        if (imageUrl != (null) && !imageUrl.equals("")) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.signup_profile_default_image)//signup_profile_default_image)
                    .error(R.drawable.signup_profile_default_image)//signup_profile_default_image)
                    .transform(new CircleTransform())
                    .into(iv_profile, new Callback() {
                        @Override
                        public void onSuccess() {
                            Utility.printLog(TAG+"  onSuccess  ");
                        }

                        @Override
                        public void onError(Exception e) {
                            Utility.printLog(TAG+"  onError  "+e.getMessage());
                        }


                    });
        }

    }

    @Override
    public void startProgressBar() {
        pDialog.show();
    }

    @Override
    public void stopProgressBar() {
        pDialog.dismiss();
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure() {
        Toast.makeText(getActivity(), getString(R.string.serverError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String msg, String titleHead) {
        DialogHelper.customAlertDialog(getActivity(),titleHead,msg,ok);
    }

    @Override
    public void onFirstNameError() {
        et_first_name.requestFocus();
        Toast.makeText(getActivity(),enter_first_name,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLastNameError() {
        et_last_name.requestFocus();
        Toast.makeText(getActivity(),enter_last_name,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageUploadedResult(String url) {
        imageUrl = url;
    }

    @Override
    public void onSuccessProfileUpdate(String msg) {
        /*Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();*/
        enableDisableEdit(false);
    }

    @Override
    public void logout() {
        presenter.logout();
    }

    @Override
    public void onSuccesLogout() {
        stopUpdateLocation();

        Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intentLogin);
    }

    public void stopUpdateLocation()
    {
        if(Utility.isMyServiceRunning(LocationUpdateService.class,getActivity())){
            Intent stopIntent = new Intent(getActivity(), LocationUpdateService.class);
            stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
            getActivity().startService(stopIntent);
        }

    }

    /**
     * <h1>setProfileDetails</h1>
     * <p>this is the handler whic handle the response of getmaster svg_profile.
     * in this method the svg_profile details are setting and load the image through Picasso and till load the url the progress is visible.</p>
     *
     * @param profileData :pojo class which pass the the getmasterprofile response
     */
    @Override
    public void setProfileDetails(ProfileData profileData) {

        et_first_name.setText(profileData.getDriverDetail().getFirstName());
        et_last_name.setText(profileData.getDriverDetail().getLastName());
        et_email.setText(profileData.getDriverDetail().getEmail());
        et_commission_plan.setText(profileData.getVehicleDetail().getPlanName());
        et_vehicle_type.setText(profileData.getVehicleDetail().getTypeName());
        et_vehicle_number.setText(profileData.getVehicleDetail().getPlateNo());
        et_phone.setText(profileData.getDriverDetail().getCountryCode().concat(" ").concat(profileData.getDriverDetail().getPhone()));
        tv_profile_makeValue.setText(profileData.getVehicleDetail().getMake());
        tv_profile_modelValue.setText(profileData.getVehicleDetail().getModel());
        tv_profile_colorValue.setText(profileData.getVehicleDetail().getColour());
        tv_profile_yearValue.setText(profileData.getVehicleDetail().getYear());

        et_vehicle_service.setText(profileData.getVehicleDetail().getServiceName());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: "+data);

        switch (requestCode)
        {
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
                        VariableConstant.newFile = new File(getActivity().getFilesDir(), takenNewImage);
                    }
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
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
                if(data == null)
                {
                    return;
                }
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }
                iv_profile.setImageBitmap(Utility.getCircleCroppedBitmap(BitmapFactory.decodeFile(path)));
                presenter.uploadToAmazon(VariableConstant.newFile);
                break;

            default:
                break;
        }

    }

    private void startCropImage() {
        Intent intent = new Intent(getActivity(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, VariableConstant.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);
        intent.putExtra("locale", Locale.getDefault().getDisplayLanguage());
        startActivityForResult(intent, CROP_IMAGE);
    }

    /**********************************************************************************************/


    @Override
    public void showSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        }
    }

    @Override
    public void goToLogin(String errMsg) {
        DialogHelper.customAlertDialogSignupSuccess(getActivity(),message,errMsg,ok);
    }

    @Override
    public void supportRTLpencil() {
        et_phone.setCompoundDrawablesWithIntrinsicBounds(ic_edit,null,null,null);
        et_email.setCompoundDrawablesWithIntrinsicBounds(ic_edit,null,null,null);
    }

    @Override
    public void notSupportRTLpencil() {
        et_phone.setCompoundDrawablesWithIntrinsicBounds(null,null,ic_edit,null);
        et_email.setCompoundDrawablesWithIntrinsicBounds(null,null,ic_edit,null);
    }

    @Override
    public void setLanguageDialog(int indexSelected) {
        DialogHelper.languageSelectDialog(getActivity(),appTypeFace,languagesLists,indexSelected);
    }

    @Override
    public void setLanguage(String language,boolean restart)    {
        tv_selected_language.setText(language);
        tv_selected_language.setCompoundDrawablesWithIntrinsicBounds(null,null ,drop_down,null);
        if(restart)
        {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Runtime.getRuntime().exit(0);
        }
    }



    @Override
    public void driverPreferenceDataForAdapter(ArrayList<PreferencesList> preferencesList) {

        if(preferencesList.size()==0)
            tv_driver_preference_head.setVisibility(View.GONE);
        else
            tv_driver_preference_head.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        rv_driver_preference_list.setLayoutManager(linearLayoutManager);
        driverpreferenceListAdapter.setData(preferencesList,1);
        rv_driver_preference_list.setAdapter(driverpreferenceListAdapter);
        driverpreferenceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void vehiclePreferenceDataForAdapter(ArrayList<PreferencesList> preferencesList) {

        if(preferencesList.size()==0)
            tv_vehicle_preference_head.setVisibility(View.GONE);
        else
            tv_vehicle_preference_head.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        rv_vehicle_preference_list.setLayoutManager(linearLayoutManager);
        vehiclepreferenceListAdapter.setData(preferencesList,1);
        rv_vehicle_preference_list.setAdapter(vehiclepreferenceListAdapter);
        vehiclepreferenceListAdapter.notifyDataSetChanged();
    }



}
