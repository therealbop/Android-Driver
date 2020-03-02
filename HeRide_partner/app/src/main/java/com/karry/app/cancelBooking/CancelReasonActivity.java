package com.karry.app.cancelBooking;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karry.adapter.BookingCancelRVA;
import com.karry.app.mainActivity.MainActivity;
import com.karry.network.NetworkErrorDialog;
import com.heride.partner.R;
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

public class CancelReasonActivity extends DaggerAppCompatActivity implements
        CancelReasonContract.CancelReasonView {


    @BindView(R.id.pb_progressBar)  ProgressBar pb_sign_up_progressBar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindString(R.string.cancelReason) String title;

    @BindView(R.id.tv_commmts) TextView tv_commmts;
    @BindView(R.id.tv_cancel_confirm) TextView tv_cancel_confirm;
    @BindView(R.id.et_comment) EditText et_comment;
    @BindView(R.id.rv_cancel_reason) RecyclerView rv_cancel_reason;
    @BindView(R.id.rl_comment) RelativeLayout rl_comment;

    @Inject AppTypeFace appTypeFace;
    @Inject CancelReasonContract.CancelReasonPresenter cancelReasonPresenter;
    @Inject BookingCancelRVA bookingCancelRVA;
    @Inject NetworkErrorDialog networkErrorDialog;

    @BindString(R.string.ok) String ok;
    @BindString(R.string.message) String message;

    public ArrayList<String> cancel_reasons = new ArrayList<>();

    private ImageView iv_search, iv_jobdetails;
    public static boolean cancelReasonActivity_opened = false;



    private String reason = "";

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reason);
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);
        ButterKnife.bind(this);
        initializeViews();
        cancelReasonPresenter.setActionBar();
        cancelReasonPresenter.getCancelationReason();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelReasonActivity_opened = true;
        cancelReasonPresenter.subscribeNetworkObserver();
        cancelReasonPresenter.networkCheckOnresume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelReasonActivity_opened = false;
    }

    @Override
    public void networkNotAvailable() {
        if(cancelReasonActivity_opened && networkErrorDialog!=null && !networkErrorDialog.isShowing())
            networkErrorDialog.show();
    }

    @Override
    public void networkAvailable() {
        if(networkErrorDialog!=null && networkErrorDialog.isShowing())
            networkErrorDialog.dismiss();
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
        cancelReasonPresenter.setActionBarTitle();
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

    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     */
    private void initializeViews() {

        tv_commmts.setTypeface(appTypeFace.getPro_News());
        tv_cancel_confirm.setTypeface(appTypeFace.getPro_narMedium());
        et_comment.setTypeface(appTypeFace.getPro_News());

    }

    /**********************************************************************************************/
    @OnClick({R.id.tv_cancel_confirm})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_cancel_confirm:
                reason = et_comment.getText().toString();
                if (!reason.equals("")) {
                    cancelReasonPresenter.cancelBooking(reason);
                } else {
                    Utility.BlueToast(this, getResources().getString(R.string.emptyReason));
                }
                break;

            default:
                break;
        }
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

    @Override
    public void goToLogin(String errMsg) {
        DialogHelper.customAlertDialogSignupSuccess(this,message,errMsg,ok);
    }

    @Override
    public void apiFailure(String msg) {
        Utility.BlueToast(this,msg);
    }

    @Override
    public void cancellationReasonSuccess(ArrayList<String> reasons) {
        if (reasons.size() != 0) {
            cancel_reasons.addAll(reasons);
            rv_cancel_reason.setLayoutManager(new LinearLayoutManager(CancelReasonActivity.this));
            rv_cancel_reason.setNestedScrollingEnabled(true);
            bookingCancelRVA.setCancelReasons(cancel_reasons);
            bookingCancelRVA.setComentsCancel(reason -> {
                et_comment.setText(reason);
            });

            rv_cancel_reason.setAdapter(bookingCancelRVA);
        } else {
            rv_cancel_reason.setVisibility(View.GONE);
        }

    }

    @Override
    public void cancelSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        finish();
    }
}
