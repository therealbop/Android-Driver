package com.karry.authentication.vehicleTypeList;

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
import com.karry.authentication.login.LoginActivity;
import com.heride.partner.R;
import com.karry.pojo.VehicleTypeList;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>VehicleTypeListActivity</h1>
 * <p>add the vehicle Type listing and select the type.</p>
 */
public class VehicleTypeListActivity extends DaggerAppCompatActivity implements VehicleTypeListContract.VehicleTypeListView{

    @Inject VehicleTypeListContract.VehicleTypeListPresenter vehicleTypeListPresenter;
    @Inject DialogHelper dialogHelper;
    @Inject AppTypeFace appTypeFace;
    @Inject VehicleTypeListRCA vehicleTypeListRCA;


    @BindView(R.id.progressBar)  ProgressBar progressBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_logout) TextView tv_logout;
    @BindView(R.id.tv_confirm) TextView tv_confirm ;
    @BindString(R.string.selectVeh_vehicle_type) String title;
    @BindString(R.string.message) String msg;
    @BindView(R.id.rv_vehicleTypeList) RecyclerView rv_vehicleTypeList;
    @BindString(R.string.ok) String ok;
    @BindString(R.string.message) String message;

    private ArrayList<String> al_vehicleTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_type_list);
        ButterKnife.bind(this);
        vehicleTypeListPresenter.getVehicleTypeList(getIntent());
        vehicleTypeListPresenter.setActionBar();

        initializeViews();




    }


    /**
     * <h1>initializeViews</h1>
     * <p>for initliaze the Views</p>
     */

    private void initializeViews() {

        al_vehicleTypeList = new ArrayList<>();
        tv_confirm.setTypeface(appTypeFace.getPro_narMedium());
        rv_vehicleTypeList.setLayoutManager(new LinearLayoutManager(this));
        rv_vehicleTypeList.setNestedScrollingEnabled(true);
        rv_vehicleTypeList.setAdapter(vehicleTypeListRCA);


        dialogHelper.getDialogLogoutCallbackHelper(new DialogHelper.DialogLogoutCallBackHelper() {
            @Override
            public void Logout() {
                vehicleTypeListPresenter.logoutOnclick();
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
                vehicleTypeListPresenter.confirmOnclick(al_vehicleTypeList);
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

        vehicleTypeListPresenter.setActionBarTitle();
    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    @Override
    public void setListData(ArrayList<VehicleTypeList> listData) {

        vehicleTypeListRCA.setData(listData);
        vehicleTypeListRCA.getSelectVehicleType(new VehicleTypeListRCA.selectVehicleType() {
            @Override
            public void vehicleTypeAdd(int position) {
                al_vehicleTypeList.add(listData.get(position).getVehicleTypeId());
            }

            @Override
            public void vehicleTypeRemove(int position) {
                al_vehicleTypeList.remove(listData.get(position).getVehicleTypeId());
            }
        });
    }

    @Override
    public void notifyAdapter() {
        vehicleTypeListRCA.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
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
