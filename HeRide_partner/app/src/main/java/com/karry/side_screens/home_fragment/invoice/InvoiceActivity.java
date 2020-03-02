package com.karry.side_screens.home_fragment.invoice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.squareup.picasso.Picasso;
import com.karry.app.mainActivity.MainActivity;
import com.karry.network.NetworkErrorDialog;
import com.heride.partner.R;
import com.karry.pojo.invoice.BookingDetailsPojo;
import com.karry.utility.AppPermissionsRunTime;
import com.karry.utility.AppTypeFace;
import com.karry.utility.CircleTransformation;
import com.karry.utility.DialogHelper;
import com.karry.utility.Utility;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class InvoiceActivity extends DaggerAppCompatActivity implements  InvoiceContract.InvoiceView,
        SignaturePad.OnSignedListener {
    @Inject InvoiceContract.InvoicePresenter invoicePresenter;
    @Inject AppTypeFace appTypeFace;
    @Inject InvoiceDialogHelper invoiceDialogHelper;
    @Inject SpecialChargeRVA specialChargeRVA;
    @Inject NetworkErrorDialog networkErrorDialog;

    @BindView(R.id.tv_invoice_bid) TextView tv_invoice_bid;
    @BindView(R.id.tv_invoice_trip_label) TextView tv_invoice_trip_label;
    @BindView(R.id.tv_invoice_trip_date) TextView tv_invoice_trip_date;
    @BindView(R.id.tv_invoice_bid_val) TextView tv_invoice_bid_val;
    @BindView(R.id.tv_invoice_sys_label) TextView tv_invoice_sys_label;
    @BindView(R.id.tv_invoice_cost) TextView tv_invoice_cost;
    @BindView(R.id.tv_invoice_distance) TextView tv_invoice_distance;
    @BindView(R.id.tv_invoice_time) TextView tv_invoice_time;
    @BindView(R.id.tv_invoice_reciept) TextView tv_invoice_reciept;
    @BindView(R.id.tv_invoice_rating_label) TextView tv_invoice_rating_label;
    @BindView(R.id.tv_invoice_submit) TextView tv_invoice_submit;
    @BindView(R.id.tv_invoice_payment_type) TextView tv_invoice_payment_type;
    @BindView(R.id.tv_rate_msg) TextView tv_rate_msg;
    @BindView(R.id.tv_pass_name) TextView tv_pass_name;
    @BindView(R.id.ratingbar_invoice) RatingBar ratingbar_invoice;
    @BindView(R.id.et_email) TextView et_email;
    @BindView(R.id.iv_payment) ImageView iv_payment;
    @BindView(R.id.ll_sendEmail) LinearLayout ll_sendEmail;
    @BindView(R.id.ll_rating) LinearLayout ll_rating;
    @BindView(R.id.rl_wallet)  RelativeLayout rl_wallet;
    @BindView(R.id.rl_card)  RelativeLayout rl_card;
    @BindView(R.id.rl_cash)  RelativeLayout rl_cash;
    @BindView(R.id.rl_corporate_wallet)  RelativeLayout rl_corporate_wallet;
    @BindView(R.id.tv_wallet_pay)  TextView tv_wallet_pay;
    @BindView(R.id.tv_card_pay)  TextView tv_card_pay;
    @BindView(R.id.tv_cash_pay)  TextView tv_cash_pay;
    @BindView(R.id.tv_corporate_wallet_pay)  TextView tv_corporate_wallet_pay;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindString(R.string.ok) String ok;
    @BindString(R.string.message) String message;
    private BookingDetailsPojo bookingDetailsPojo;
    private boolean doubleBackToExitPressedOnce = false;
    public static boolean invoiceActivity_opened = false;

    @BindDrawable(R.drawable.history_stuff_card_icon) Drawable history_stuff_card_icon;
    @BindDrawable(R.drawable.history_stuff_credit_icon) Drawable history_stuff_credit_icon;
    @BindDrawable(R.drawable.hostory_stuff_help_cash_icon) Drawable hostory_stuff_help_cash_icon;
    @BindString(R.string.card) String card;
    @BindString(R.string.cash) String cash;
    @BindString(R.string.walet) String wallet;
    @BindString(R.string.corporateWalet) String corporateWalet;
    @BindString(R.string.thankyou_booking) String thankyou_booking;



    @BindView(R.id.tvSignTitle) TextView tvSignTitle;
    @BindView(R.id.ivSignature) ImageView ivSignature;
    @BindView(R.id.ll_sign_here1) LinearLayout ll_sign_here1;
    @BindView(R.id.tvSignHere1) TextView tvSignHere1;
    @BindView(R.id.rlSignaturePad) RelativeLayout rlSignaturePad;
    @BindView(R.id.ivBackBtn) ImageView ivBackBtn;
    @BindView(R.id.ivRefresh) ImageView ivRefresh;
    @BindView(R.id.tv_approve) TextView tv_approve;
    @BindView(R.id.signature_pad)  SignaturePad signature_pad;
    @BindView(R.id.ll_sign_here) LinearLayout ll_sign_here;
    @BindView(R.id.tvSignHere) TextView tvSignHere;
    @BindView(R.id.ll_bottom_layer) LinearLayout ll_bottom_layer;
    @BindView(R.id.rl_signature) RelativeLayout rl_signature;
    @BindView(R.id.iv_agentlogo) ImageView iv_agentlogo;
    @BindView(R.id.tv_agency) TextView tv_agency;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);

        if(getIntent().getExtras()!=null)
        {
            invoicePresenter.getmeterBookingInvoiceData(getIntent().getExtras());
        }
        setTypeFace();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invoiceActivity_opened = true;
        invoicePresenter.subscribeNetworkObserver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        invoiceActivity_opened = false;
    }

    @Override
    public void networkNotAvailable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (invoiceActivity_opened && networkErrorDialog != null && !networkErrorDialog.isShowing())
                    networkErrorDialog.show();
            }
        });
    }

    @Override
    public void networkAvailable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (networkErrorDialog != null && networkErrorDialog.isShowing())
                    networkErrorDialog.dismiss();
            }
        });
    }

    @Override
    public void enableWallet(String walletValue) {
        rl_wallet.setVisibility(View.VISIBLE);
        tv_wallet_pay.setText(walletValue);
    }

    @Override
    public void enableCard(String cardValue) {
        rl_card.setVisibility(View.VISIBLE);
        tv_card_pay.setText(cardValue);
    }

    @Override
    public void enableCash(String cashValue) {
        rl_cash.setVisibility(View.VISIBLE);
        tv_cash_pay.setText(cashValue);
    }

    @Override
    public void enableCorporateWallet(String walletValue) {
        rl_corporate_wallet.setVisibility(View.VISIBLE);
        tv_corporate_wallet_pay.setText(walletValue);
    }

    @Override
    public void disableWallet() {
        rl_wallet.setVisibility(View.GONE);
    }

    @Override
    public void disableCard() {
        rl_card.setVisibility(View.GONE);
    }

    @Override
    public void disableCash() {
        rl_cash.setVisibility(View.GONE);
    }

    @Override
    public void disableCorporateBooking() {
        rl_corporate_wallet.setVisibility(View.GONE);
    }

    /**
     * <h1>setTypeFace</h1>
     * <p>initialize the Type of text</p>
     */
    private void setTypeFace()
    {
        tv_invoice_bid.setTypeface(appTypeFace.getPro_narMedium());
        tv_invoice_trip_label.setTypeface(appTypeFace.getPro_narMedium());
        tv_invoice_trip_date.setTypeface(appTypeFace.getPro_narMedium());
        tv_invoice_bid_val.setTypeface(appTypeFace.getPro_narMedium());
        tv_invoice_sys_label.setTypeface(appTypeFace.getPro_narMedium());
        tv_invoice_cost.setTypeface(appTypeFace.getPro_narMedium());
        tv_invoice_distance.setTypeface(appTypeFace.getPro_News());
        tv_invoice_time.setTypeface(appTypeFace.getPro_News());
        tv_invoice_reciept.setTypeface(appTypeFace.getPro_narMedium());
        tv_invoice_rating_label.setTypeface(appTypeFace.getPro_narMedium());
        tv_invoice_submit.setTypeface(appTypeFace.getPro_narMedium());
        tv_invoice_payment_type.setTypeface(appTypeFace.getPro_narMedium());
        tv_rate_msg.setTypeface(appTypeFace.getPro_narMedium());
        tv_pass_name.setTypeface(appTypeFace.getPro_narMedium());
        tv_wallet_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_card_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_cash_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_corporate_wallet_pay.setTypeface(appTypeFace.getPro_narMedium());


        tv_approve.setTypeface(appTypeFace.getPro_narMedium());
        tvSignTitle.setTypeface(appTypeFace.getPro_News());
        tvSignHere1.setTypeface(appTypeFace.getPro_News());
        tvSignHere.setTypeface(appTypeFace.getPro_News());
        tv_agency.setTypeface(appTypeFace.getPro_News());

        signature_pad.setOnSignedListener(this);

    }

    /**********************************************************************************************/
    @OnClick({R.id.tv_invoice_submit, R.id.tv_invoice_reciept,
            R.id.rl_signature,R.id.ivRefresh,R.id.ivBackBtn, R.id.tv_approve})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_invoice_submit:
                invoicePresenter.completeInvoice(et_email.getText().toString(),String.valueOf(ratingbar_invoice.getRating()));
                break;

            case R.id.tv_invoice_reciept:
                if(bookingDetailsPojo!=null)
                    invoiceDialogHelper.invoiceDialog(this,bookingDetailsPojo, appTypeFace,
                            specialChargeRVA);
                break;
            case R.id.rl_signature:
                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow
                    ArrayList<AppPermissionsRunTime.MyPermissionConstants> myPermissionConstantsArrayList = new ArrayList<>();
                    myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_WRITE_EXTERNAL_STORAGE);
                    myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_READ_EXTERNAL_STORAGE);

                    int REQUEST_CODE_PERMISSION_MULTIPLE = 123;
                    if (AppPermissionsRunTime.checkPermission(this, myPermissionConstantsArrayList, REQUEST_CODE_PERMISSION_MULTIPLE)) {
                        invoicePresenter.openSignaturePad();
                    }
                } else {
                    // Pre-Marshmallow
                    invoicePresenter.openSignaturePad();
                }

                /*invoicePresenter.openSignaturePad();*/

                break;

            case R.id.ivRefresh:
                invoicePresenter.refresh();
                break;

            case R.id.ivBackBtn:
                invoicePresenter.onBackPressSign();
                break;

            case R.id.tv_approve:
                invoicePresenter.onSignatureApprove();
                break;

            default:
                break;
        }
    }

    /**********************************************************************************************/
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Utility.BlueToast(this,getResources().getString(R.string.exit_double_back));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
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
    /**********************************************************************************************/


    @Override
    public void setValues(BookingDetailsPojo bookingDetailsPojo) {

        this.bookingDetailsPojo = bookingDetailsPojo;
        tv_invoice_trip_date.setText(Utility.convertUTCToServerFormat(bookingDetailsPojo.getData().getTimeStamp(), "dd MMM yyyy"));
        tv_invoice_bid_val.setText(bookingDetailsPojo.getData().getBookingId());
        tv_pass_name.setText(bookingDetailsPojo.getData().getCustomerName());

        if(bookingDetailsPojo.getData().isTowTruckBooking()) {
            tv_invoice_sys_label.setText(getResources().getString(R.string.system_cal_fee));
            tv_rate_msg.setText(getResources().getString(R.string.rate_service_provided));
        }

        if(bookingDetailsPojo.getData().isTravelAgentBooking()){

            /*if (Build.VERSION.SDK_INT >= 23) {

                ArrayList<AppPermissionsRunTime.MyPermissionConstants> myPermissionConstantsArrayList = new ArrayList<>();
                myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_WRITE_EXTERNAL_STORAGE);
                myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_READ_EXTERNAL_STORAGE);


                String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (!EasyPermissions.hasPermissions(this, perms)) {
                    EasyPermissions.requestPermissions(this, "error",
                            VariableConstant.RC_LOCATION_STATE, perms);
                }
            }*/

            rl_signature.setVisibility(View.VISIBLE);
            tv_invoice_submit.setVisibility(View.GONE);

            if(bookingDetailsPojo.getData().getTravelAgentLogo()!=null && !bookingDetailsPojo.getData().getTravelAgentLogo().matches(""))
                Picasso.get()
                        .load(bookingDetailsPojo.getData().getTravelAgentLogo())
                        .placeholder(R.drawable.signup_profile_default_image)
                        .transform(new CircleTransformation())
                        .into(iv_agentlogo);
            String agency_txt = thankyou_booking.concat(" ").concat(bookingDetailsPojo.getData().getTravelAgentName());
            tv_agency.setText(agency_txt);


        }else {
            rl_signature.setVisibility(View.GONE);
            tv_invoice_submit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setCardPaymentType() {
        Drawable cardBrand = history_stuff_card_icon;
        tv_invoice_payment_type.setText(card);
        iv_payment.setImageDrawable(cardBrand);
        tv_invoice_payment_type.setVisibility(View.VISIBLE);
        iv_payment.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCashPaymentType() {
        Drawable cardBrand = hostory_stuff_help_cash_icon;
        tv_invoice_payment_type.setText(cash);
        iv_payment.setImageDrawable(cardBrand);
        tv_invoice_payment_type.setVisibility(View.VISIBLE);
        iv_payment.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCreditPaymentType() {
        Drawable cardBrand = history_stuff_credit_icon;
        tv_invoice_payment_type.setText(wallet);
        iv_payment.setImageDrawable(cardBrand);
        tv_invoice_payment_type.setVisibility(View.VISIBLE);
        iv_payment.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCorporateBookng() {
        Drawable cardBrand = history_stuff_credit_icon;
        tv_invoice_payment_type.setText(corporateWalet);
        iv_payment.setImageDrawable(cardBrand);
        tv_invoice_payment_type.setVisibility(View.VISIBLE);
        iv_payment.setVisibility(View.VISIBLE);
    }

    @Override
    public void setMeterBookingView() {
        ll_rating.setVisibility(View.GONE);
        ll_sendEmail.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRideBookingView() {
        ll_rating.setVisibility(View.VISIBLE);
        ll_sendEmail.setVisibility(View.GONE);
    }

    @Override
    public void setTotal(String total) {
        tv_invoice_cost.setText(total);
    }

    @Override
    public void goToLogin(String errMsg) {
        DialogHelper.customAlertDialogSignupSuccess(this,message,errMsg,ok);
    }

    @Override
    public void apiFailure(String msg) {
        DialogHelper.customAlertDialog(this,message,msg,ok);
        /*Utility.BlueToast(this,msg);*/
    }

    @Override
    public void finishActivity() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        finish();
    }

    @Override
    public void onStartSigning() {
        ll_sign_here.setVisibility(View.GONE);
    }

    @Override
    public void onSigned() {
        Bitmap signBitmap = signature_pad.getSignatureBitmap();
        invoicePresenter.onSigned(signBitmap);
    }

    @Override
    public void onClear() {
        invoicePresenter.onSigned(null);
        ll_sign_here.setVisibility(View.VISIBLE);


    }

    @Override
    public void clearSignature() {
        signature_pad.clear();
        tv_invoice_submit.setVisibility(View.GONE);
    }

    @Override
    public void hideSignature(Bitmap signBitmap, boolean signatureUrl){
        rlSignaturePad.setVisibility(View.GONE);
        ivSignature.setImageBitmap(signBitmap);
        if(signBitmap==null){
            ll_sign_here1.setVisibility(View.VISIBLE);
        }else {
            ll_sign_here1.setVisibility(View.GONE);
        }

        if(signatureUrl) {
            tv_invoice_submit.setVisibility(View.VISIBLE);
            ll_bottom_layer.setVisibility(View.VISIBLE);

        }
        else {
            tv_invoice_submit.setVisibility(View.GONE);
            ll_bottom_layer.setVisibility(View.GONE);
        }


    }

    @Override
    public void showSignature(){
        tv_invoice_submit.setVisibility(View.GONE);
        ll_bottom_layer.setVisibility(View.GONE);
        rlSignaturePad.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSignatureApprove(Bitmap bitmap){
        rlSignaturePad.setVisibility(View.GONE);
        ll_sign_here1.setVisibility(View.GONE);
        tv_invoice_submit.setVisibility(View.VISIBLE);
        ll_bottom_layer.setVisibility(View.VISIBLE);
        ivSignature.setImageBitmap(bitmap);

    }
}
