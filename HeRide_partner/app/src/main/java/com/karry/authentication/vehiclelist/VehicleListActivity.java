package com.karry.authentication.vehiclelist;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karry.app.mainActivity.MainActivity;
import com.karry.authentication.vehicleTypeList.VehicleTypeListActivity;
import com.heride.partner.R;
import com.karry.authentication.login.LoginActivity;
import com.karry.authentication.login.model.VehiclesDetails;
import com.karry.pojo.SigninDriverVehicle;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.Utility;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karry.utility.VariableConstant.LOGIN_VEHICLE_LIST;
import static com.karry.utility.VariableConstant.LOGIN_VEHICLE_TYPE_LIST;

/**************************************************************************************************/
public class VehicleListActivity extends DaggerAppCompatActivity implements VehicleListContract.VehicleListView {

    public ArrayList<SigninDriverVehicle> vDataList=new ArrayList<>();
    @Inject  VehicleListRCA currentJobRVA;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_title)  TextView tv_title;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_logout) TextView tv_logout;
    @BindView(R.id.tv_confirm) TextView tv_confirm ;
    @BindView(R.id.rv_vehiclelist) RecyclerView rv_vehiclelist;
    @BindString(R.string.selectVeh_vl) String title;
    @BindString(R.string.message) String msg;

    @Inject VehicleListContract.VehicleListPresenter  presenter;
    @Inject DialogHelper dialogHelper;
    @Inject AppTypeFace appTypeFace;

    @BindString(R.string.ok) String ok;
    @BindString(R.string.message) String message;


    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehicle_list);
        ButterKnife.bind(this);
        if(getIntent().getExtras()!=null)
        {
            ArrayList<VehiclesDetails> myList = (ArrayList<VehiclesDetails>) getIntent().getSerializableExtra(LOGIN_VEHICLE_LIST);
            currentJobRVA.setData(myList);
        }
        presenter.setActionBar();
        initializeViews();

    }

    /**
     * <h1>initActionBar</h1>
     * <p>for initiliaze the action bar, including the close button and text type face</p>
     */
    public void initActionBar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        tv_logout.setVisibility(View.VISIBLE);
        tv_logout.setTypeface(appTypeFace.getPro_narMedium());
        iv_search.setVisibility(View.GONE);

        presenter.setActionBarTitle();

    }

    @Override
    public void setTitle() {

        tv_title.setText(title);
    }

    @Override
    public void setListData(ArrayList<SigninDriverVehicle> listData) {
        vDataList.clear();
        vDataList.addAll(listData);
    }

    @Override
    public void notifyAdapter() {
        currentJobRVA.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {
        Utility.mShowMessage(msg,error,this);
    }

    @Override
    public void onSuccess(String vehicleTypes) {

        Intent intent=new Intent(this, VehicleTypeListActivity.class);
        intent.putExtra(LOGIN_VEHICLE_TYPE_LIST,vehicleTypes);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSuccessMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * <h1>initializeViews</h1>
     * <p>for initliaze the Views</p>
     */

    private void initializeViews() {
        tv_confirm.setTypeface(appTypeFace.getPro_narMedium());
        rv_vehiclelist.setLayoutManager(new LinearLayoutManager(this));
        rv_vehiclelist.setNestedScrollingEnabled(true);
        rv_vehiclelist.setAdapter(currentJobRVA);
        /*presenter.getList();*/
        dialogHelper.getDialogCallbackHelper(new DialogHelper.DialogCallbackHelper() {

            @Override
            public void walletFragOpen() {

            }

            @Override
            public void changeLanguage(String langCode, String langName, int dir) {

            }

        });

        dialogHelper.getDialogLogoutCallbackHelper(new DialogHelper.DialogLogoutCallBackHelper() {
            @Override
            public void Logout() {
                presenter.logoutOnclick();
            }
        });
    }

    @OnClick({R.id.tv_logout, R.id.tv_confirm})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_logout:
                DialogHelper.customAlertDialogLogout( this);
                break;

            case R.id.tv_confirm:
                presenter.confirmOnclick();
                break;
        }

    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccesLogout() {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentLogin);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToLogin(String errMsg) {
        DialogHelper.customAlertDialogSignupSuccess(this,message,errMsg,ok);
    }
}
