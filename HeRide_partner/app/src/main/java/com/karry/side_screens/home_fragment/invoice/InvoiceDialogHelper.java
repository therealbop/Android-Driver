package com.karry.side_screens.home_fragment.invoice;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.pojo.invoice.BookingDetailsPojo;
import com.karry.utility.AppConstants;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import javax.inject.Inject;

public class InvoiceDialogHelper {

    @Inject
    InvoiceDialogHelper() {
    }



    private static int getPixelValue(Context context, int dimenId) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dimenId,
                resources.getDisplayMetrics()
        );
    }

    void invoiceDialog(final Activity mActivity,BookingDetailsPojo bookingDetailsPojo,AppTypeFace appTypeFace,
                       SpecialChargeRVA specialChargeRVA)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.invoice_dialog,null);
        alertDialogBuilder.setView(view);

        TextView tv_receipt_details,tv_pickup_head,tv_pickup_address,tv_drop_head,tv_drop_address;
        TextView tv_bill_details,tv_minimumfare,tv_minimumfare_value,tv_base_fare,tv_base_val,tv_distance_fare,tv_dist_fare_val,tv_time_fare,tv_time_fare_val,tv_specialCharges,tv_waitingfare;
        TextView tv_total,tv_total_val,tv_sub_total,tv_sub_total_val,tv_discount,tv_sub_discount_val,tv_waitingfare_value;
        LinearLayout ll_minimumfare,ll_above_minFare,ll_discount,ll_waitingfare,ll_lastDue;
        RecyclerView rv_specialcharges,rv_towtrayServices;
        TextView tv_payment_head,tv_payment_type,tv_lastDue,tv_lastDue_val,tv_tip,tv_tip_val;
        LinearLayout ll_wallet,ll_card,ll_cash,ll_corporate_wallet,ll_tip;
        TextView tv_wallet,tv_wallet_pay,tv_card,tv_card_pay,tv_cash,tv_cash_pay,tv_corporate_wallet,tv_corporate_wallet_pay;
        RelativeLayout rl_bill_details;


        ll_wallet= view.findViewById(R.id.ll_wallet);
        ll_card= view.findViewById(R.id.ll_card);
        ll_cash= view.findViewById(R.id.ll_cash);
        ll_corporate_wallet= view.findViewById(R.id.ll_corporate_wallet);
        tv_wallet= view.findViewById(R.id.tv_wallet);
        tv_wallet_pay= view.findViewById(R.id.tv_wallet_pay);
        tv_card= view.findViewById(R.id.tv_card);
        tv_card_pay= view.findViewById(R.id.tv_card_pay);
        tv_cash= view.findViewById(R.id.tv_cash);
        tv_cash_pay= view.findViewById(R.id.tv_cash_pay);
        tv_corporate_wallet= view.findViewById(R.id.tv_corporate_wallet);
        tv_corporate_wallet_pay= view.findViewById(R.id.tv_corporate_wallet_pay);

        tv_receipt_details= view.findViewById(R.id.tv_receipt_details);
        tv_pickup_head= view.findViewById(R.id.tv_pickup_head);
        tv_drop_head= view.findViewById(R.id.tv_drop_head);
        tv_pickup_address= view.findViewById(R.id.tv_pickup_address);
        tv_drop_address= view.findViewById(R.id.tv_drop_address);
        tv_bill_details= view.findViewById(R.id.tv_bill_details);
        tv_specialCharges= view.findViewById(R.id.tv_specialCharges);
        tv_total= view.findViewById(R.id.tv_total);
        tv_total_val= view.findViewById(R.id.tv_total_val);
        tv_minimumfare= view.findViewById(R.id.tv_minimumfare);
        tv_minimumfare_value= view.findViewById(R.id.tv_minimumfare_value);
        tv_base_fare= view.findViewById(R.id.tv_base_fare);
        tv_base_val= view.findViewById(R.id.tv_base_val);
        tv_distance_fare= view.findViewById(R.id.tv_distance_fare);
        tv_dist_fare_val= view.findViewById(R.id.tv_dist_fare_val);
        tv_time_fare= view.findViewById(R.id.tv_time_fare);
        tv_time_fare_val= view.findViewById(R.id.tv_time_fare_val);
        tv_sub_total= view.findViewById(R.id.tv_sub_total);
        tv_sub_total_val= view.findViewById(R.id.tv_sub_total_val);
        tv_discount= view.findViewById(R.id.tv_discount);
        tv_sub_discount_val= view.findViewById(R.id.tv_sub_discount_val);
        tv_waitingfare= view.findViewById(R.id.tv_waitingfare);
        tv_waitingfare_value= view.findViewById(R.id.tv_waitingfare_value);
        ll_minimumfare= view.findViewById(R.id.ll_minimumfare);
        ll_above_minFare= view.findViewById(R.id.ll_above_minFare);
        ll_discount= view.findViewById(R.id.ll_discount);
        ll_waitingfare= view.findViewById(R.id.ll_waitingfare);
        ll_lastDue= view.findViewById(R.id.ll_lastDue);
        rv_specialcharges= view.findViewById(R.id.rv_specialcharges);
        rv_towtrayServices= view.findViewById(R.id.rv_towtrayServices);
        rl_bill_details= view.findViewById(R.id.rl_bill_details);
        tv_payment_head= view.findViewById(R.id.tv_payment_head);
        tv_payment_type= view.findViewById(R.id.tv_payment_type);
        tv_lastDue= view.findViewById(R.id.tv_lastDue);
        tv_lastDue_val= view.findViewById(R.id.tv_lastDue_val);
        ll_tip= view.findViewById(R.id.ll_tip);
        tv_tip= view.findViewById(R.id.tv_tip);
        tv_tip_val= view.findViewById(R.id.tv_tip_val);

        tv_receipt_details.setTypeface(appTypeFace.getPro_narMedium());
        tv_pickup_head.setTypeface(appTypeFace.getPro_narMedium());
        tv_drop_head.setTypeface(appTypeFace.getPro_narMedium());
        tv_bill_details.setTypeface(appTypeFace.getPro_narMedium());
        tv_specialCharges.setTypeface(appTypeFace.getPro_narMedium());
        tv_total.setTypeface(appTypeFace.getPro_narMedium());
        tv_pickup_address.setTypeface(appTypeFace.getPro_News());
        tv_drop_address.setTypeface(appTypeFace.getPro_News());
        tv_total_val.setTypeface(appTypeFace.getPro_narMedium());
        tv_minimumfare.setTypeface(appTypeFace.getPro_News());
        tv_minimumfare_value.setTypeface(appTypeFace.getPro_News());
        tv_base_fare.setTypeface(appTypeFace.getPro_News());
        tv_base_val.setTypeface(appTypeFace.getPro_News());
        tv_distance_fare.setTypeface(appTypeFace.getPro_News());
        tv_dist_fare_val.setTypeface(appTypeFace.getPro_News());
        tv_time_fare.setTypeface(appTypeFace.getPro_News());
        tv_time_fare_val.setTypeface(appTypeFace.getPro_News());
        tv_sub_total.setTypeface(appTypeFace.getPro_narMedium());
        tv_sub_total_val.setTypeface(appTypeFace.getPro_narMedium());
        tv_discount.setTypeface(appTypeFace.getPro_News());
        tv_sub_discount_val.setTypeface(appTypeFace.getPro_News());
        tv_waitingfare_value.setTypeface(appTypeFace.getPro_News());
        tv_waitingfare.setTypeface(appTypeFace.getPro_News());
        tv_lastDue.setTypeface(appTypeFace.getPro_News());
        tv_lastDue_val.setTypeface(appTypeFace.getPro_News());
        tv_payment_head.setTypeface(appTypeFace.getPro_narMedium());
        tv_payment_type.setTypeface(appTypeFace.getPro_narMedium());
        tv_wallet.setTypeface(appTypeFace.getPro_narMedium());
        tv_wallet_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_card.setTypeface(appTypeFace.getPro_narMedium());
        tv_card_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_cash.setTypeface(appTypeFace.getPro_narMedium());
        tv_cash_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_corporate_wallet.setTypeface(appTypeFace.getPro_narMedium());
        tv_corporate_wallet_pay.setTypeface(appTypeFace.getPro_narMedium());
        tv_tip.setTypeface(appTypeFace.getPro_News());
        tv_tip_val.setTypeface(appTypeFace.getPro_News());

        /*PreferencesHelper preferencesHelper=new PreferencesHelper(mActivity);
        String timeZoneString =  TimezoneMapper.latLngToTimezoneString(preferencesHelper.getCurrLatitude(),
                preferencesHelper.getCurrLongitude());
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        tv_pickup_head.setText(mActivity.getResources().getString(R.string.pickup_location).concat(" (").concat(Utility.convertUTCToServerFormat(bookingDetailsPojo.getData().getPickupTime(),VariableConstant.TIME_FORMAT_TIME_DISPLAY)).concat(")"));
        tv_drop_head.setText(mActivity.getResources().getString(R.string.drop_location).concat(" (").concat(Utility.convertUTCToServerFormat(bookingDetailsPojo.getData().getDropTime(),VariableConstant.TIME_FORMAT_TIME_DISPLAY)).concat(")"));*/

        if(bookingDetailsPojo.getData().isTowTruckBooking())
            tv_minimumfare.setText(mActivity.getResources().getString(R.string.min_fee));

        String distance_fare = mActivity.getString(R.string.distance_fare);
        String time_fare = mActivity.getString(R.string.time_fare);
        String currencySymbol;
        String minimumFee = bookingDetailsPojo.getData().getInvoice().getMinFee();
        String discountFee = bookingDetailsPojo.getData().getInvoice().getDiscount();
        String waitingFee = bookingDetailsPojo.getData().getInvoice().getWaitingFee();
        String lastDueFee = bookingDetailsPojo.getData().getInvoice().getLastDue();

        tv_pickup_head.setText(mActivity.getResources().getString(R.string.pickup_location).concat(" (").concat(Utility.convertUTCToServerFormat(bookingDetailsPojo.getData().getPickupTimeStamp(),VariableConstant.TIME_FORMAT_TIME_DISPLAY1)).concat(")"));
        tv_drop_head.setText(mActivity.getResources().getString(R.string.drop_location).concat(" (").concat(Utility.convertUTCToServerFormat(bookingDetailsPojo.getData().getDropTimeStamp(),VariableConstant.TIME_FORMAT_TIME_DISPLAY1)).concat(")"));
        tv_waitingfare.setText(mActivity.getResources().getString(R.string.waiting_fee).concat(" (").concat(bookingDetailsPojo.getData().getInvoice().getWaitingTime().concat(")")));

        if(bookingDetailsPojo.getData().getCurrencyAbbr().matches("1"))
        {
            currencySymbol = bookingDetailsPojo.getData().getCurrencySbl().concat(" ");

            tv_minimumfare_value.setText(currencySymbol.concat(minimumFee));
            tv_pickup_address.setText(bookingDetailsPojo.getData().getPickupAddress());
            tv_drop_address.setText(bookingDetailsPojo.getData().getDropAddress());
            tv_base_val.setText(currencySymbol.concat(bookingDetailsPojo.getData().getInvoice().getBaseFee()));
            tv_dist_fare_val.setText(currencySymbol.concat(bookingDetailsPojo.getData().getInvoice().getDistanceFee()));
            tv_time_fare_val.setText(currencySymbol.concat(bookingDetailsPojo.getData().getInvoice().getTimeFee()));
            tv_total_val.setText(currencySymbol.concat(bookingDetailsPojo.getData().getInvoice().getTotal()));
            tv_sub_total_val.setText(currencySymbol.concat(bookingDetailsPojo.getData().getInvoice().getSubTotalCalc()));
            tv_sub_discount_val.setText(currencySymbol.concat(bookingDetailsPojo.getData().getInvoice().getDiscount()));
            tv_waitingfare_value.setText(currencySymbol.concat(waitingFee));
            tv_lastDue_val.setText(currencySymbol.concat(lastDueFee));
            tv_tip_val.setText(currencySymbol.concat(bookingDetailsPojo.getData().getInvoice().getTip()));

            tv_wallet_pay.setText(currencySymbol.concat(bookingDetailsPojo.getData().getPaymentMethod().getWalletTransaction()));
            tv_card_pay.setText(currencySymbol.concat(bookingDetailsPojo.getData().getPaymentMethod().getCardDeduct()));
            tv_cash_pay.setText(currencySymbol.concat(bookingDetailsPojo.getData().getPaymentMethod().getCashCollected()));
            tv_corporate_wallet_pay.setText(currencySymbol.concat(bookingDetailsPojo.getData().getInvoice().getTotal()));

            tv_distance_fare.setText(distance_fare.concat(" (").concat(bookingDetailsPojo.getData().getInvoice().getDistanceCalc()).concat(")"));
            tv_time_fare.setText(time_fare.concat(" (").concat(bookingDetailsPojo.getData().getInvoice().getTimeCalc()).concat(")"));

        }else {
            currencySymbol = " ".concat(bookingDetailsPojo.getData().getCurrencySbl());

            tv_minimumfare_value.setText(minimumFee.concat(currencySymbol));
            tv_pickup_address.setText(bookingDetailsPojo.getData().getPickupAddress());
            tv_drop_address.setText(bookingDetailsPojo.getData().getDropAddress());
            tv_base_val.setText(bookingDetailsPojo.getData().getInvoice().getBaseFee().concat(currencySymbol));
            tv_dist_fare_val.setText(bookingDetailsPojo.getData().getInvoice().getDistanceFee().concat(currencySymbol));
            tv_time_fare_val.setText(bookingDetailsPojo.getData().getInvoice().getTimeFee().concat(currencySymbol));
            tv_total_val.setText(bookingDetailsPojo.getData().getInvoice().getTotal().concat(currencySymbol));
            tv_sub_total_val.setText(bookingDetailsPojo.getData().getInvoice().getSubTotalCalc().concat(currencySymbol));
            tv_sub_discount_val.setText(bookingDetailsPojo.getData().getInvoice().getDiscount().concat(currencySymbol));
            tv_waitingfare_value.setText(waitingFee.concat(currencySymbol));
            tv_lastDue_val.setText(lastDueFee.concat(currencySymbol));
            tv_tip_val.setText(bookingDetailsPojo.getData().getInvoice().getTimeCalc().concat(currencySymbol));

            tv_wallet_pay.setText(bookingDetailsPojo.getData().getPaymentMethod().getWalletTransaction().concat(currencySymbol));
            tv_card_pay.setText(bookingDetailsPojo.getData().getPaymentMethod().getCardDeduct().concat(currencySymbol));
            tv_cash_pay.setText(bookingDetailsPojo.getData().getPaymentMethod().getCashCollected().concat(currencySymbol));
            tv_corporate_wallet_pay.setText(bookingDetailsPojo.getData().getInvoice().getTotal().concat(currencySymbol));

            tv_distance_fare.setText(currencySymbol.concat(distance_fare).concat(" (").concat(bookingDetailsPojo.getData().getInvoice().getDistanceCalc()).concat(")").concat(currencySymbol));
            tv_time_fare.setText(currencySymbol.concat(time_fare).concat(" (").concat(bookingDetailsPojo.getData().getInvoice().getTimeCalc()).concat(")").concat(currencySymbol));

        }

        if(!(Double.parseDouble(bookingDetailsPojo.getData().getInvoice().getTip())>0)){
            ll_tip.setVisibility(View.GONE);
        }
        if(Double.parseDouble(bookingDetailsPojo.getData().getPaymentMethod().getWalletTransaction())>0){
            ll_wallet.setVisibility(View.VISIBLE);
        }
        if(Double.parseDouble(bookingDetailsPojo.getData().getPaymentMethod().getCardDeduct())>0){
            ll_card.setVisibility(View.VISIBLE);
        }
        if(Double.parseDouble(bookingDetailsPojo.getData().getPaymentMethod().getCashCollected())>0){
            ll_cash.setVisibility(View.VISIBLE);
        }
        if(bookingDetailsPojo.getData().getPaymentMethod().isCorporateBooking()){
            ll_corporate_wallet.setVisibility(View.VISIBLE);
            ll_cash.setVisibility(View.GONE);
            ll_card.setVisibility(View.GONE);
            ll_wallet.setVisibility(View.GONE);
        }


        String type = bookingDetailsPojo.getData().getInvoice().getPaymentType();
        switch (type) {
            case "1": {
                Drawable cardBrand = mActivity.getResources().getDrawable(R.drawable.history_stuff_card_icon);
                tv_payment_type.setText(mActivity.getResources().getString(R.string.card));
                tv_payment_type.setCompoundDrawablesWithIntrinsicBounds(cardBrand, null, null, null);
                break;
            }
            case "2": {
                Drawable cardBrand = mActivity.getResources().getDrawable(R.drawable.hostory_stuff_help_cash_icon);
                tv_payment_type.setText(mActivity.getResources().getString(R.string.cash));
                tv_payment_type.setCompoundDrawablesWithIntrinsicBounds(cardBrand, null, null, null);
                break;
            }
            case "3": {
                Drawable cardBrand = mActivity.getResources().getDrawable(R.drawable.history_stuff_credit_icon);
                tv_payment_type.setText(mActivity.getResources().getString(R.string.walet));
                tv_payment_type.setCompoundDrawablesWithIntrinsicBounds(cardBrand, null, null, null);
                if(bookingDetailsPojo.getData().isCorporateBooking())
                    tv_payment_type.setText(mActivity.getResources().getString(R.string.corporateWalet));

                break;
            }
        }

        if(bookingDetailsPojo.getData().getInvoice().getIsMinFeeApplied().matches("1")){
            ll_above_minFare.setVisibility(View.GONE);
        }else {
            ll_minimumfare.setVisibility(View.GONE);
        }

        if(Double.parseDouble(discountFee)==0){
            ll_discount.setVisibility(View.GONE);
        }

        if(Double.parseDouble(lastDueFee)==0){
            ll_lastDue.setVisibility(View.GONE);
        }

        if(Double.parseDouble(waitingFee)==0){
            ll_waitingfare.setVisibility(View.GONE);
        }

        if(bookingDetailsPojo.getData().getInvoice().getExtraFees().size()==0) {
            tv_specialCharges.setVisibility(View.GONE);
            rv_specialcharges.setVisibility(View.GONE);
        }else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,true);
            rv_specialcharges.setLayoutManager(linearLayoutManager);
            specialChargeRVA.setData(bookingDetailsPojo.getData().getInvoice().getExtraFees(),currencySymbol,bookingDetailsPojo.getData().getCurrencyAbbr());
            rv_specialcharges.setAdapter(specialChargeRVA);
            specialChargeRVA.notifyDataSetChanged();
        }


        if(bookingDetailsPojo.getData().getTowTruckBookingService()!=null)
            switch ( bookingDetailsPojo.getData().getTowTruckBookingService()){
                case AppConstants.TowTrayService.Fixed:
                    rl_bill_details.setVisibility(View.GONE);
                    rv_towtrayServices.setVisibility(View.VISIBLE);
                    SpecialChargeRVA towtryServiceRVA = new SpecialChargeRVA(mActivity);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,true);
                    rv_towtrayServices.setLayoutManager(linearLayoutManager);
                    towtryServiceRVA.setTowtryData(bookingDetailsPojo.getData().getTowTruckServices(),currencySymbol,bookingDetailsPojo.getData().getCurrencyAbbr());
                    rv_towtrayServices.setAdapter(towtryServiceRVA);
                    towtryServiceRVA.notifyDataSetChanged();
                    break;
                case AppConstants.TowTrayService.Towing:
                    rv_towtrayServices.setVisibility(View.GONE);
                    break;
                case AppConstants.TowTrayService.FixedAndTowing:
                    rv_towtrayServices.setVisibility(View.VISIBLE);
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,true);
                    rv_towtrayServices.setLayoutManager(linearLayoutManager1);
                    SpecialChargeRVA towtryServiceRVABoth = new SpecialChargeRVA(mActivity);
                    towtryServiceRVABoth.setTowtryData(bookingDetailsPojo.getData().getTowTruckServices(),currencySymbol,bookingDetailsPojo.getData().getCurrencyAbbr());
                    rv_towtrayServices.setAdapter(towtryServiceRVABoth);
                    towtryServiceRVABoth.notifyDataSetChanged();
                    break;
            }

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        }
        else
        {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }


        ImageView iv_close = view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if ( animHide[0]!=null)
                    {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();

                            }
                        });
                        animHide[0].start();
                    }
                    else
                    {
                        alertDialog.dismiss();
                    }
                }
                else
                {
                    alertDialog.dismiss();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
                }
            }
        });


        alertDialog.setCancelable(false);
    }


}
