package com.karry.authentication.login;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


import com.google.gson.Gson;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.authentication.vehicleTypeList.VehicleTypeListActivity;
import com.karry.authentication.vehiclelist.VehicleListActivity;
import com.karry.network.NetworkErrorDialog;
import com.heride.partner.R;
import com.karry.authentication.forgotpassword.ForgotPasswordActivity;
import com.karry.authentication.login.model.VehiclesDetails;
import com.karry.authentication.signup.SignUpWeb.RegisterWebActivity;
import com.karry.pojo.VehicleTypes;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.VariableConstant;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;
import pub.devrel.easypermissions.EasyPermissions;

import static com.karry.utility.VariableConstant.BOOKING_FLOW_OPEN;
import static com.karry.utility.VariableConstant.FROM;
import static com.karry.utility.VariableConstant.LOGIN;
import static com.karry.utility.VariableConstant.LOGIN_VEHICLE_LIST;
import static com.karry.utility.VariableConstant.LOGIN_VEHICLE_TYPE_LIST;
import static com.karry.utility.VariableConstant.TERMS_CONDITION;
import static com.karry.utility.VariableConstant.URL;

/**
 * <h1>Login Activity</h1>
 * This class is used to provide the Login screen, where we can do our login and if we forget
 * <p>
 *     our etNewPassword then here we also can make a request to forgot etNewPassword
 *      and if login successful, then it directly opens Main Activity.
 * </p>
 * @author 3embed
 * @since 31 Jan 2018.*/

public class LoginActivity extends DaggerAppCompatActivity implements
        LoginContract.View {

    @BindView(R.id.tv_log_login) TextView tv_log_login;
    @BindView(R.id.tv_appname) TextView tv_appname ;
    @BindView(R.id.tv_splash_msg) TextView tv_splash_msg;
    @BindView(R.id.tv_log_forgortpass) TextView tv_log_forgortpass ;
    @BindView(R.id.tv_log_signup) TextView tv_log_signup ;
    @BindView(R.id.et_log_mail_mob) EditText et_log_mail_mob;
    @BindView(R.id.et_log_password) EditText et_log_password;
    @BindView(R.id.et_log_vehNo) EditText et_log_vehNo;
    @BindView(R.id.progressBar) ProgressBar  progressBar;
    @BindView(R.id.til_log_mail_mob) TextInputLayout til_log_mail_mob;
    @BindView(R.id.til_log_password) TextInputLayout til_log_password;
    @BindView(R.id.til_log_vehNo) TextInputLayout til_log_vehNo;
    @BindView(R.id.seek_bar_button) SeekBar seek_bar_button;
    @BindView(R.id.tv_selected_language) TextView tv_selected_language;
    /*@BindView(R.id.spin_language) Spinner spin_language;
    @BindView(R.id.ll_spinner_lang) LinearLayout ll_spinner_lang;*/

    @BindColor(R.color.colorPrimaryDark) int colorPrimaryDark;
    @BindColor(R.color.white) int white;
    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindString(R.string.location_permission_message) String location_permission_message;
    @BindString(R.string.login_signup_txt) String login_signup_txt;
    @BindString(R.string.message) String message;
    @BindString(R.string.ok) String ok;
    @BindString(R.string.english) String english;

    @Inject AppTypeFace appTypeFace;
    @Inject LoginContract.Presenter presenter;
    @Inject NetworkErrorDialog networkErrorDialog;
    @Inject DialogHelper dialogHelper;
    @Inject @Named(LOGIN)
    ArrayList<LanguagesList> languagesLists = new ArrayList<>();
    @BindDrawable(R.drawable.drop_down) Drawable drop_down;


    public static boolean loginActivity_opened = false;
    /*private ArrayAdapter<String> languageAdapter;
    private List<String> languages_list;*/

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        initializeViews();
        presenter.getUsernamePass();
        if(getIntent().getExtras()!=null)
            presenter.checkMessage(getIntent().getExtras());

    }


    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     */
    private void initializeViews() {
        presenter.checkLoginResponse();
        BOOKING_FLOW_OPEN  = true;
        tv_appname.setTypeface(appTypeFace.getSFAutomaton());
        tv_splash_msg.setTypeface(appTypeFace.getPro_narMedium());
        tv_log_login.setTypeface(appTypeFace.getPro_narMedium());
        tv_log_forgortpass.setTypeface(appTypeFace.getPro_News());
        et_log_mail_mob.setTypeface(appTypeFace.getPro_News());
        tv_log_signup.setTypeface(appTypeFace.getPro_News());
        til_log_mail_mob.setTypeface(appTypeFace.getPro_News());
        et_log_password.setTypeface(appTypeFace.getPro_News());
        til_log_password.setTypeface(appTypeFace.getPro_News());
        et_log_vehNo.setTypeface(appTypeFace.getPro_News());
        til_log_vehNo.setTypeface(appTypeFace.getPro_News());
        tv_selected_language.setTypeface(appTypeFace.getPro_News());

        tv_log_signup.setText(Html.fromHtml(login_signup_txt));

        presenter.storeFcmToken();
        presenter.checkIsAlreadyLogin();
        presenter.getDeviceId();

        dialogHelper.getDialogCallbackHelper(new DialogHelper.DialogCallbackHelper() {

            @Override
            public void walletFragOpen() {

            }

            @Override
            public void changeLanguage(String langCode, String langName, int dir) {
                presenter.languageChanged(langCode,langName,dir);
                VariableConstant.lan = langCode;
                /*setLanguage(langName,true);*/
            }

        });

        //set the adapter for the language spinner

       /* spin_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        languages_list = new ArrayList<>();
        languages_list.add(english);
        languageAdapter = new ArrayAdapter<>(this, R.layout.single_spinner_text, languages_list);
        languageAdapter.setDropDownViewResource(R.layout.single_spinner_dropdown);
        spin_language.setAdapter(languageAdapter);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginActivity_opened = true;
        presenter.unSubScribeFCMTopics();

        if (Build.VERSION.SDK_INT >= 23) {
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (!EasyPermissions.hasPermissions(this, perms)) {
                EasyPermissions.requestPermissions(this, location_permission_message,
                        VariableConstant.RC_LOCATION_STATE, perms);
            }
        }

        /*presenter.subscribeNetworkObserver();*/
        /*presenter.networkCheckOnresume();*/
    }


    @Override
    protected void onPause() {
        super.onPause();
        loginActivity_opened = false;
        presenter.clear();
    }

    @OnClick({R.id.tv_log_login,R.id.tv_log_forgortpass,R.id.tv_log_signup,R.id.tv_selected_language})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_log_login:
                startCurrLocation();
                break;

            case R.id.tv_log_forgortpass:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;

            case R.id.tv_log_signup:

                presenter.setWebUrl(TERMS_CONDITION);


                /*startActivity(new Intent(LoginActivity.this, RegisterWebActivity.class));*/
                break;

            case R.id.tv_selected_language:
                presenter.getLanguages();
                break;

        }
    }

    @Override
    public void openWebView(String url) {
        Intent intent = new Intent(LoginActivity.this, RegisterWebActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(FROM, TERMS_CONDITION);
        mBundle.putString(URL,url );
        intent.putExtras(mBundle);
        startActivity(intent);
    }


    /**
     * <h1>startCurrLocation</h1>
     * This method is used to get the current location
     */
    private void startCurrLocation()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (EasyPermissions.hasPermissions(this, perms)) {
                tv_log_login.setEnabled(false);
                presenter.onClickLoginButton(et_log_mail_mob.getText().toString(),
                        et_log_password.getText().toString(),et_log_vehNo.getText().toString());
                tv_log_login.postDelayed(() -> {
                    tv_log_login.setEnabled(true);
                }, 1000);
            } else {
                EasyPermissions.requestPermissions(this, location_permission_message,
                        VariableConstant.RC_LOCATION_STATE, perms);
            }

        } else {
            tv_log_login.setEnabled(false);
            presenter.onClickLoginButton(et_log_mail_mob.getText().toString(),
                    et_log_password.getText().toString(),et_log_vehNo.getText().toString());
            tv_log_login.postDelayed(() -> {
                tv_log_login.setEnabled(true);
            }, 1000);
        }
    }

    @Override
    public void setLoginCreds(String username, String pass) {
        et_log_mail_mob.setText(username);
        et_log_password.setText(pass);
    }


    @OnTextChanged({ R.id.et_log_mail_mob, R.id.et_log_password, R.id.et_log_vehNo})
    public void afterTextChanged(Editable editable)
    {
        presenter.validateField(et_log_mail_mob.getText().toString(),et_log_password.getText().toString(),
                et_log_vehNo.getText().toString());
    }

    @Override
    public void enableLogIn() {
        til_log_mail_mob.setErrorEnabled(false);
        til_log_password.setErrorEnabled(false);
        /*til_log_vehNo.setErrorEnabled(false);*/

        tv_log_login.setTextColor(white);
        presenter.setSeekBarProgress(seek_bar_button,100);
    }

    @Override
    public void enableHalf() {
        presenter.setSeekBarProgress(seek_bar_button,50);
        tv_log_login.setTextColor(colorPrimaryDark);
    }

    @Override
    public void enableThirty() {
        presenter.setSeekBarProgress(seek_bar_button,33);
        tv_log_login.setTextColor(colorPrimaryDark);
    }

    @Override
    public void enableSixty() {
        presenter.setSeekBarProgress(seek_bar_button,66);
        tv_log_login.setTextColor(colorPrimaryDark);
    }

    @Override
    public void disableLoginIn() {
        tv_log_login.setTextColor(colorPrimary);
        presenter.setSeekBarProgress(seek_bar_button,0);
    }


    @Override
    public void setDisableUserError() {
        til_log_mail_mob.setErrorEnabled(false);
    }

    @Override
    public void setDisablePasswordError() {
        til_log_password.setErrorEnabled(false);
    }

    @Override
    public void setDisableVehPinError() {
        /*til_log_vehNo.setErrorEnabled(false);*/
    }

    @Override
    public void onUsernameError(String msg) {
        til_log_mail_mob.setErrorEnabled(true);
        til_log_mail_mob.setError(msg);
    }

    @Override
    public void onPasswordError(String msg) {
        til_log_password.setErrorEnabled(true);
        til_log_password.setError(msg);
    }

    @Override
    public void onVehNoError(String msg) {
        /*til_log_vehNo.setErrorEnabled(true);
        til_log_vehNo.setError(msg);*/
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
    public void onFailure(String msg, String titleHead) {
        DialogHelper.customAlertDialog(this,titleHead,msg,ok);
    }

    @Override
    public void onSuccessLogin(ArrayList<VehiclesDetails> vehicleList) {


        Intent intent=new Intent(this, VehicleListActivity.class);
        intent.putExtra(LOGIN_VEHICLE_LIST,vehicleList);
        startActivity(intent);
        finish();
        //        startActivity(new Intent(this, MainActivity.class));

    }

    @Override
    public void gotoVehicleTypeActivity(VehicleTypes vehicleTypes) {

        Gson gson = new Gson();
        String vehicleTypes_str = gson.toJson(vehicleTypes);

        Intent intent=new Intent(this, VehicleTypeListActivity.class);
        intent.putExtra(LOGIN_VEHICLE_TYPE_LIST,vehicleTypes_str);
        startActivity(intent);
        finish();
    }

    @Override
    public void setUserNamePass(String userName, String password,String veh_no) {
        et_log_mail_mob.setText(userName.trim());
        et_log_password.setText(password.trim());
        et_log_vehNo.setText(veh_no.trim());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.onDestoryView();
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
    public void networkNotAvailable() {
        if(loginActivity_opened && networkErrorDialog!=null && !networkErrorDialog.isShowing())
            networkErrorDialog.show();
    }

    @Override
    public void networkAvailable() {
        if(networkErrorDialog!=null && networkErrorDialog.isShowing()) {
            networkErrorDialog.dismiss();
        }
    }

    @Override
    public void setLanguageDialog(int indexSelected) {
        DialogHelper.languageSelectDialog(this,appTypeFace,languagesLists,indexSelected);

        /*languages_list = new ArrayList<>();
        for(int i =0; languagesList.size()>i;i++)
        {
            Utility.printLog("language list isss:  "+languagesList.get(i).getName());
            languages_list.add(languagesList.get(i).getName());
        }

        languageAdapter = new ArrayAdapter<>(this, R.layout.single_spinner_text, languages_list);
        languageAdapter.setDropDownViewResource(R.layout.single_spinner_dropdown);
        spin_language.setAdapter(languageAdapter);*/
    }


    @Override
    public void setLanguage(String language,boolean restart)    {
        tv_selected_language.setText(language);
        tv_selected_language.setCompoundDrawablesWithIntrinsicBounds(null,null ,drop_down,null);
        if(restart)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Runtime.getRuntime().exit(0);
        }
    }


}
