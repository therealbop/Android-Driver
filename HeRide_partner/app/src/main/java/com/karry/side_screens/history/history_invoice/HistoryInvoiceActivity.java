package com.karry.side_screens.history.history_invoice;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.side_screens.history.new_model.AppointmentData;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class HistoryInvoiceActivity extends DaggerAppCompatActivity implements HistoryInvoiceContract.HistoryInvoiceView
{
    @BindView(R.id.tv_hist_invoice_earning) TextView tv_hist_invoice_earning;
    @BindView(R.id.tv_hist_invioce_total_title) TextView tv_hist_invioce_total_title;
    @BindView(R.id.tv_hist_invoice_trip_title) TextView tv_hist_invoice_trip_title;
    @BindView(R.id.tv_hist_invoice_receipt_title) TextView tv_hist_invoice_receipt_title;
    @BindView(R.id.tv_history_details_help) TextView tv_history_details_help;
    @BindView(R.id.ll_receipt) LinearLayout ll_receipt;
    @BindView(R.id.ll_help) LinearLayout ll_help;

    @BindView(R.id.tv_hist_inoice_subTot_title) TextView tv_hist_inoice_subTot_title;
    @BindView(R.id.tv_hist_invoice_earning_header) TextView tv_hist_invoice_earning_header;
    @BindView(R.id.tv_hist_invoice_earnedAmt) TextView tv_hist_invoice_earnedAmt;
    @BindView(R.id.tv_hist_invoice_appCommision) TextView tv_hist_invoice_appCommision;
    @BindView(R.id.tv_hist_invoice_grandTot) TextView tv_hist_invoice_grandTot;
    @BindView(R.id.tv_hist_invoice_distance) TextView tv_hist_invoice_distance;
    @BindView(R.id.tv_hist_invoice_time) TextView tv_hist_invoice_time;
    @BindView(R.id.tv_hist_invoice_pickupLoc) TextView tv_hist_invoice_pickupLoc;
    @BindView(R.id.tv_hist_invoice_dropLoc) TextView tv_hist_invoice_dropLoc;
    @BindView(R.id.rv_hist_invoice_receipt) RecyclerView rv_hist_invoice_receipt;
    @BindView(R.id.rv_receipt_help) RecyclerView rv_receipt_help;
    @BindView(R.id.rv_hist_invoice_specialCharge) RecyclerView rv_hist_invoice_specialCharge;
    @BindView(R.id.tv_hist_invoice_totalVal) TextView tv_hist_invoice_totalVal;
    @BindView(R.id.tv_hist_invoice_EarnedAmt_value) TextView tv_hist_invoice_EarnedAmt_value;
    @BindView(R.id.tv_hist_invoice_AppCommision_value) TextView tv_hist_invoice_AppCommision_value;
    @BindView(R.id.tv_hist_invoice_customerName) TextView tv_hist_invoice_customerName;
    @BindView(R.id.tv_hist_invoice_pickTime) TextView tv_hist_invoice_pickTime;
    @BindView(R.id.tv_hist_invoice_dropTime) TextView tv_hist_invoice_dropTime;
    @BindView(R.id.tv_hist_invoice_grandTotalVal) TextView tv_hist_invoice_grandTotalVal;
    @BindView(R.id.tv_hist_toolbar_date) TextView tv_hist_toolbar_date;
    @BindView(R.id.tv_hist_toolbar_bid) TextView tv_hist_toolbar_bid;
    @BindView(R.id.tv_hist_specail) TextView tv_hist_specail;
    @BindView(R.id.tv_hist_invoice_paymentTitle) TextView tv_hist_invoice_paymentTitle;
    @BindView(R.id.tv_hist_invoice_payment) TextView tv_hist_invoice_payment;
    @BindView(R.id.btn_hist_invoice_status) Button btn_hist_invoice_status;
    @BindView(R.id.ll_hist_invoice_minFare) LinearLayout ll_hist_invoice_minFare;
    @BindView(R.id.ll_hist_invoice_price_details) LinearLayout ll_hist_invoice_price_details;
    @BindView(R.id.ll_hist_invoice_grandTot) LinearLayout ll_hist_invoice_grandTot;
    @BindView(R.id.rl_hist_toolBar) RelativeLayout rl_hist_toolBar;
    @BindView(R.id.rl_hist_invoice_dropLoc) RelativeLayout rl_hist_invoice_dropLoc;
    @BindView(R.id.view_special) View view_special;
    @BindView(R.id.ll_lastDue) LinearLayout ll_lastDue;
    @BindView(R.id.tv_lastDue) TextView tv_lastDue;
    @BindView(R.id.tv_lastDue_val) TextView tv_lastDue_val;
    @BindView(R.id.ll_discount) LinearLayout ll_discount;
    @BindView(R.id.tv_discount) TextView tv_discount;
    @BindView(R.id.tv_sub_discount_val) TextView tv_sub_discount_val;
    @BindView(R.id.tv_invoice_minFare) TextView tv_invoice_minFare;
    @BindView(R.id.tv_invoice_minFare_value) TextView tv_invoice_minFare_value;
    @BindView(R.id.ll_pgCommission) LinearLayout ll_pgCommission;
    @BindView(R.id.tv_hist_invoice_pgCommission) TextView tv_hist_invoice_pgCommission;
    @BindView(R.id.tv_hist_invoice_pgCommission_value) TextView tv_hist_invoice_pgCommission_value;

    @BindView(R.id.ll_wallet) LinearLayout ll_wallet;
    @BindView(R.id.ll_card) LinearLayout ll_card;
    @BindView(R.id.ll_cash) LinearLayout ll_cash;
    @BindView(R.id.ll_corporate_wallet) LinearLayout ll_corporate_wallet;
    @BindView(R.id.tv_wallet) TextView tv_wallet;
    @BindView(R.id.tv_wallet_pay) TextView tv_wallet_pay;
    @BindView(R.id.tv_card) TextView tv_card;
    @BindView(R.id.tv_card_pay) TextView tv_card_pay;
    @BindView(R.id.tv_cash) TextView tv_cash;
    @BindView(R.id.tv_cash_pay) TextView tv_cash_pay;
    @BindView(R.id.tv_corporate_wallet) TextView tv_corporate_wallet;
    @BindView(R.id.tv_corporate_wallet_pay) TextView tv_corporate_wallet_pay;
    @BindView(R.id.ll_tip) LinearLayout ll_tip;
    @BindView(R.id.tv_tip) TextView tv_tip;
    @BindView(R.id.tv_tip_val) TextView tv_tip_val;
    @BindView(R.id.ll_earning_commision) LinearLayout ll_earning_commision;

    @BindString(R.string.base_fare) String base_fee;
    @BindString(R.string.distance_fare) String distance_fee;
    @BindString(R.string.close_par) String close_par;
    @BindString(R.string.open_par) String open_par;
    @BindString(R.string.time_fare) String time_fee;
    @BindString(R.string.waiting_fee) String waiting_fee;
    @BindString(R.string.pgCommision) String pgCommision;
    @BindString(R.string.card) String card;
    @BindString(R.string.cash) String cash;
    @BindString(R.string.walet) String wallet;
    @BindString(R.string.corporateWalet) String corporateWalet;
    @BindString(R.string.cancelation_fee) String cancelation_fee;
    @BindString(R.string.grand_total) String grand_total;
    @BindString(R.string.ok) String ok;
    @BindString(R.string.message) String message;
    private String currency;

    @BindDrawable(R.drawable.history_stuff_card_icon) Drawable history_stuff_card_icon;
    @BindDrawable(R.drawable.history_stuff_credit_icon) Drawable history_stuff_credit_icon;
    @BindDrawable(R.drawable.hostory_stuff_help_cash_icon) Drawable hostory_stuff_help_cash_icon;

    @Inject
    AppTypeFace appTypeFace;
    @Inject HistoryInvoiceContract.HistoryInvoicePresenter presenter;

    private AppointmentData appointment;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindColor(R.color.tablayout) int tab_unselect;

    @Inject HistoryDetailsHelpAdapter historyDetailsHelpAdapter;

    public void setCardPaymentType()
    {
        Drawable cardBrand = history_stuff_card_icon;
        tv_hist_invoice_payment.setText(card);
        tv_hist_invoice_payment.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,null,null);
    }

    public void hideDropLocation()
    {
        rl_hist_invoice_dropLoc.setVisibility(View.GONE);
    }

    @Override
    public void enableWallet(String walletValue) {
        ll_wallet.setVisibility(View.VISIBLE);
        tv_wallet_pay.setText(walletValue);
    }

    @Override
    public void enableCard(String cardValue) {
        ll_card.setVisibility(View.VISIBLE);
        tv_card_pay.setText(cardValue);
    }

    @Override
    public void enableCash(String cashValue) {
        ll_cash.setVisibility(View.VISIBLE);
        tv_cash_pay.setText(cashValue);
    }

    @Override
    public void enableCorporateWallet(String walletValue) {
        ll_corporate_wallet.setVisibility(View.VISIBLE);
        tv_corporate_wallet_pay.setText(walletValue);
    }

    @Override
    public void disableWallet() {
        ll_wallet.setVisibility(View.GONE);
    }

    @Override
    public void disableCard() {
        ll_card.setVisibility(View.GONE);
    }

    @Override
    public void disableCash() {
        ll_cash.setVisibility(View.GONE);
    }

    @Override
    public void disablePayment() {
        tv_hist_invoice_paymentTitle.setVisibility(View.GONE);
    }

    @Override
    public void disableCorporateBooking() {
        ll_corporate_wallet.setVisibility(View.GONE);
    }

    @Override
    public void hideAppCommision() {
        ll_earning_commision.setVisibility(View.GONE);
    }

    public void setCashPaymentType()
    {
        Drawable cardBrand = hostory_stuff_help_cash_icon;
        tv_hist_invoice_payment.setText(cash);
        tv_hist_invoice_payment.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,null,null);
    }


    public void setCreditPaymentType()
    {
        Drawable cardBrand = history_stuff_credit_icon;
        tv_hist_invoice_payment.setText(wallet);
        tv_hist_invoice_payment.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,null,null);
    }

    @Override
    public void setCorporatePaymentType() {
        Drawable cardBrand = history_stuff_credit_icon;
        tv_hist_invoice_payment.setText(corporateWalet);
        tv_hist_invoice_payment.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,null,null);
    }

    @SuppressLint("SetTextI18n")
    public void setCancelationFee()
    {
        ll_hist_invoice_grandTot.setVisibility(View.VISIBLE);
        presenter.isDropLacationEmpty(appointment.getDropAddress());
        ll_hist_invoice_price_details.setVisibility(View.GONE);
        tv_hist_invoice_grandTot.setText(cancelation_fee);
        if(appointment.getCurrencyAbbr().equals("1"))
            tv_hist_invoice_grandTotalVal.setText(currency+" "+appointment.getInvoice().getTotal());
        else
            tv_hist_invoice_grandTotalVal.setText(appointment.getInvoice().getTotal()+" "+currency);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_invoice);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);
        if(getIntent()!=null) {
            appointment = (AppointmentData) getIntent().getSerializableExtra("data");
            setValues(appointment);
            initViews(appointment);
            setAppTypeFace();
        }
        presenter.getHelpDetails();
        if(presenter.checkhelpCenter())
            tv_history_details_help.setVisibility(View.VISIBLE);
        else
            tv_history_details_help.setVisibility(View.GONE);
    }

    private void setAppTypeFace()
    {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_receipt_help.setLayoutManager(layoutManager);
        rv_receipt_help.setAdapter(historyDetailsHelpAdapter);

        tv_hist_invoice_earning.setTypeface(appTypeFace.getPro_News());
        tv_hist_toolbar_date.setTypeface(appTypeFace.getPro_News());
        tv_hist_toolbar_bid.setTypeface(appTypeFace.getPro_narMedium());
        tv_hist_specail.setTypeface(appTypeFace.getPro_narMedium());
        tv_hist_invoice_distance.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_time.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_pickupLoc.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_dropLoc.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_totalVal.setTypeface(appTypeFace.getPro_narMedium());
        tv_hist_invoice_grandTotalVal.setTypeface(appTypeFace.getPro_narMedium());
        tv_hist_invoice_grandTot.setTypeface(appTypeFace.getPro_narMedium());
        tv_hist_inoice_subTot_title.setTypeface(appTypeFace.getPro_narMedium());
        tv_hist_invoice_dropTime.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_pickTime.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_customerName.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_AppCommision_value.setTypeface(appTypeFace.getPro_News());
        tv_hist_invioce_total_title.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_trip_title.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_receipt_title.setTypeface(appTypeFace.getPro_News());
        tv_history_details_help.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_earning_header.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_earnedAmt.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_appCommision.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_EarnedAmt_value.setTypeface(appTypeFace.getPro_News());
        btn_hist_invoice_status.setTypeface(appTypeFace.getPro_narMedium());
        tv_hist_invoice_paymentTitle.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_payment.setTypeface(appTypeFace.getPro_narMedium());
        tv_hist_invoice_pgCommission.setTypeface(appTypeFace.getPro_News());
        tv_hist_invoice_pgCommission_value.setTypeface(appTypeFace.getPro_News());


        tv_discount.setTypeface(appTypeFace.getPro_News());
        tv_sub_discount_val.setTypeface(appTypeFace.getPro_News());
        tv_lastDue.setTypeface(appTypeFace.getPro_News());
        tv_lastDue_val.setTypeface(appTypeFace.getPro_News());
        tv_invoice_minFare.setTypeface(appTypeFace.getPro_News());
        tv_invoice_minFare_value.setTypeface(appTypeFace.getPro_News());
        tv_tip.setTypeface(appTypeFace.getPro_News());
        tv_tip_val.setTypeface(appTypeFace.getPro_News());

        tv_wallet.setTypeface(appTypeFace.getPro_narMedium());
        tv_wallet_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_card.setTypeface(appTypeFace.getPro_narMedium());
        tv_card_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_cash.setTypeface(appTypeFace.getPro_narMedium());
        tv_cash_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_corporate_wallet.setTypeface(appTypeFace.getPro_narMedium());
        tv_corporate_wallet_pay.setTypeface(appTypeFace.getPro_narMedium());
        ll_receipt.setVisibility(View.VISIBLE);
        ll_help.setVisibility(View.GONE);
        tv_hist_invoice_receipt_title.setTextColor(colorPrimary);
        tv_history_details_help.setTextColor(tab_unselect);
    }


    public void hideFeeDetails()
    {
        ll_hist_invoice_minFare.setVisibility(View.VISIBLE);
        rv_hist_invoice_receipt.setVisibility(View.GONE);
    }

    @Override
    public void showFeeDetails()
    {
        ll_hist_invoice_minFare.setVisibility(View.GONE);
        rv_hist_invoice_receipt.setVisibility(View.VISIBLE);

        ArrayList<HistoryInvoiceReceipt> historyInvoiceReceipts = new ArrayList<>();
        if(appointment.getCurrencyAbbr().equals("1")) {
            HistoryInvoiceReceipt basrFee = new HistoryInvoiceReceipt(base_fee, currency+" "+appointment.getInvoice().getBaseFee());
            HistoryInvoiceReceipt TimeFee = new HistoryInvoiceReceipt(time_fee +open_par + appointment.getInvoice().getTimeCalc() + close_par
                    , currency+" "+appointment.getInvoice().getTimeFee());

            historyInvoiceReceipts.add(basrFee);
            historyInvoiceReceipts.add(TimeFee);

            if (Double.parseDouble(appointment.getInvoice().getWaitingFee()) > 0) {

                String waitingTime = waiting_fee.concat("(").concat(appointment.getInvoice().getWaitingTime().concat(")"));
                HistoryInvoiceReceipt waitingFee = new HistoryInvoiceReceipt(waitingTime
                        , currency+" "+appointment.getInvoice().getWaitingFee());
                historyInvoiceReceipts.add(waitingFee);
            }

            if (Double.parseDouble(appointment.getInvoice().getTimeFee()) > 0) {
                HistoryInvoiceReceipt distanceFee = new HistoryInvoiceReceipt(distance_fee+ open_par + appointment.getInvoice().getDistanceCalc() + close_par
                        , currency+" "+appointment.getInvoice().getDistanceFee());
                historyInvoiceReceipts.add(distanceFee);
            }

            if(!(Double.parseDouble(appointment.getInvoice().getLastDue())>0)){
                ll_lastDue.setVisibility(View.GONE);
            }
            if(!(Double.parseDouble(appointment.getInvoice().getDiscount())>0)){
                ll_discount.setVisibility(View.GONE);
            }
            if(!(Double.parseDouble(appointment.getInvoice().getTip())>0)){
                ll_tip.setVisibility(View.GONE);
            }


            if(appointment.getInvoice().getPgCommission().matches("NaN") || !(Double.parseDouble(appointment.getInvoice().getPgCommission())>0)){
                /*HistoryInvoiceReceipt pgCommision_add = new HistoryInvoiceReceipt(pgCommision
                        , currency+" "+appointment.getInvoice().getPgCommission());
                historyInvoiceReceipts.add(pgCommision_add);*/

                ll_pgCommission.setVisibility(View.GONE);
            }


        }


        HistoryInvoiceReceiptAdapter adapter = new HistoryInvoiceReceiptAdapter(historyInvoiceReceipts,appTypeFace);
        adapter.notifyDataSetChanged();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_hist_invoice_receipt.setLayoutManager(mLayoutManager);
        rv_hist_invoice_receipt.setItemAnimator(new DefaultItemAnimator());
        rv_hist_invoice_receipt.setAdapter(adapter);
    }


    @OnClick({R.id.rl_hist_toolBar, R.id.tv_hist_invoice_receipt_title, R.id.tv_history_details_help})
    public void onClickEvent(View view)
    {

        switch (view.getId())
        {
            case R.id.rl_hist_toolBar:
                overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                this.onBackPressed();
                break;

            case R.id.tv_hist_invoice_receipt_title:
                ll_receipt.setVisibility(View.VISIBLE);
                tv_hist_invoice_receipt_title.setTextColor(colorPrimary);
                tv_history_details_help.setTextColor(tab_unselect);
                ll_help.setVisibility(View.GONE);

                break;

            case R.id.tv_history_details_help:
                ll_help.setVisibility(View.VISIBLE);
                ll_receipt.setVisibility(View.GONE);
                tv_hist_invoice_receipt_title.setTextColor(tab_unselect);
                tv_history_details_help.setTextColor(colorPrimary);
                rv_receipt_help.setAdapter(historyDetailsHelpAdapter);

                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    public void initViews(AppointmentData appointment)
    {
        if(appointment.getInvoice().getSubTotalCalc().matches(appointment.getInvoice().getTotal())){
            tv_hist_specail.setVisibility(View.GONE);
            view_special.setVisibility(View.GONE);
            ll_hist_invoice_grandTot.setVisibility(View.GONE);
            ll_discount.setVisibility(View.GONE);
            ll_lastDue.setVisibility(View.GONE);
            tv_hist_inoice_subTot_title.setText(grand_total);
        }

        if(appointment.getInvoice().getExtraFees().size()>0) {
            HistorySpecailChargeAdapter specailChargeAdapter = new HistorySpecailChargeAdapter(appointment.getInvoice().getExtraFees(), appointment.getCurrencyAbbr(),
                    appointment.getCurrencySbl(),appTypeFace);
            specailChargeAdapter.notifyDataSetChanged();
            RecyclerView.LayoutManager mSpecialLayoutManager = new LinearLayoutManager(getApplicationContext());
            rv_hist_invoice_specialCharge.setLayoutManager(mSpecialLayoutManager);
            rv_hist_invoice_specialCharge.setItemAnimator(new DefaultItemAnimator());
            rv_hist_invoice_specialCharge.setAdapter(specailChargeAdapter);
        }
        /*else {
            tv_hist_specail.setVisibility(View.GONE);
            view_special.setVisibility(View.GONE);
            ll_hist_invoice_grandTot.setVisibility(View.GONE);
        }*/
        presenter.isMinFare(appointment.getInvoice().getIsMinFeeApplied(),
                appointment.getStatus());

    }



    @SuppressLint("SetTextI18n")
    public void setValues(AppointmentData appointment)
    {
        currency=appointment.getCurrencySbl();

        tv_hist_toolbar_bid.setText(appointment.getBookingId());
        tv_hist_toolbar_date.setText(Utility.convertUTCToServerFormat(appointment.getTimeStamp(), VariableConstant.TIME_FORMAT_TIME_DISPLAY1));

        if(appointment.getCurrencyAbbr().equals("1")){
            tv_hist_invoice_earning.setText(currency+" "+appointment.getInvoice().getTotal());
            tv_hist_invoice_grandTotalVal.setText(currency+" "+appointment.getInvoice().getTotal());
            tv_hist_invoice_EarnedAmt_value.setText(currency+" "+appointment.getInvoice().getMasEarning());
            tv_hist_invoice_AppCommision_value.setText(currency+" "+appointment.getInvoice().getAppCom());
            tv_hist_invoice_totalVal.setText(currency+" "+appointment.getInvoice().getSubTotalCalc());
            tv_sub_discount_val.setText(currency+" "+appointment.getInvoice().getDiscount());
            tv_lastDue_val.setText(currency+" "+appointment.getInvoice().getLastDue());
            tv_invoice_minFare_value.setText(currency+" "+appointment.getInvoice().getMinFee());
            tv_tip_val.setText(currency+" "+appointment.getInvoice().getTip());
            tv_hist_invoice_pgCommission_value.setText(currency+" "+appointment.getInvoice().getPgCommission());
        }
        else {
            tv_hist_invoice_earning.setText(appointment.getInvoice().getTotal() + " " + currency);
            tv_hist_invoice_grandTotalVal.setText(appointment.getInvoice().getTotal()+ " " + currency);
            tv_hist_invoice_EarnedAmt_value.setText(appointment.getInvoice().getMasEarning()+ " " + currency);
            tv_hist_invoice_AppCommision_value.setText(appointment.getInvoice().getAppCom()+ " " + currency);
            tv_hist_invoice_totalVal.setText(appointment.getInvoice().getSubTotalCalc()+ " " + currency);
            tv_sub_discount_val.setText(appointment.getInvoice().getDiscount()+ " " + currency);
            tv_lastDue_val.setText(appointment.getInvoice().getLastDue()+ " " + currency);
            tv_invoice_minFare_value.setText(appointment.getInvoice().getMinFee()+ " " + currency);
            tv_tip_val.setText(appointment.getInvoice().getTip()+ " " + currency);
            tv_hist_invoice_pgCommission_value.setText(appointment.getInvoice().getPgCommission()+ " " + currency);
        }

        tv_hist_invoice_distance.setText(appointment.getInvoice().getDistanceCalc());
        tv_hist_invoice_time.setText(appointment.getInvoice().getTimeCalc());
        tv_hist_invoice_pickupLoc.setText(appointment.getPickupAddress());
        tv_hist_invoice_dropLoc.setText(appointment.getDropAddress());

        tv_hist_invoice_customerName.setText(appointment.getCustomerName());
        btn_hist_invoice_status.setText(appointment.getStatusText());
        tv_hist_invoice_pickTime.setText(appointment.getPickupTime());
        tv_hist_invoice_dropTime.setText(appointment.getDropTime());

        presenter.checkPaymentType(appointment.getInvoice().getPaymentType(),
                appointment.isCorporateBooking());

        presenter.checkPaymentType(appointment);
        presenter.checkAppCommission(appointment);

    }


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
    public void goToLogin(String errMsg) {
        DialogHelper.customAlertDialogSignupSuccess(this,message,errMsg,ok);
    }

    @Override
    public void apiFailure(String msg) {
        DialogHelper.customAlertDialog(this,message,msg, ok);
    }

    @Override
    public void setHelpDetailsList() {
        historyDetailsHelpAdapter.notifyDataSetChanged();
    }
}