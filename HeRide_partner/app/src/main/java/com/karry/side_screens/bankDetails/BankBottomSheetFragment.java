package com.karry.side_screens.bankDetails;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.karry.adapter.BankDetailsRVA;
import com.heride.partner.R;
import com.karry.pojo.bank.BankList;
import com.karry.utility.SessionManager;
import com.karry.utility.Utility;

import org.json.JSONObject;

public class BankBottomSheetFragment extends BottomSheetDialogFragment
        implements BankBottomSheetPresenter.BankBottomSheetPresenterImplem {

    private static final String PARAM1 = "param1";
    private static final String PARAM2 = "param2";
    BankList bankData;
    BankDetailsRVA.RefreshBankDetails refreshBankDetails;
    private Typeface fontRegular, fontLight;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    private BankBottomSheetPresenter bankBottomSheetPresenter;

    private static final String TAG = "BankBottomSheetFragment";
    LayoutInflater inflater;

    public BankBottomSheetFragment()
    {

    }

    public static BankBottomSheetFragment newInstance(BankList bankData/*, BankDetailsRVA.RefreshBankDetails refreshBankDetails*/) {
        BankBottomSheetFragment fragment = new BankBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM1, bankData);
        /*args.putSerializable(PARAM2, refreshBankDetails);*/

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bankData = (BankList) getArguments().getSerializable(PARAM1);
            refreshBankDetails = (BankDetailsRVA.RefreshBankDetails) getArguments().getSerializable(PARAM2);
        }

        fontRegular = Utility.getFontRegular(getActivity());
        fontLight = Utility.getFontRegular(getActivity());
        sessionManager = new SessionManager(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);

        bankBottomSheetPresenter = new BankBottomSheetPresenter(this);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.alert_dialog_bank_details,null);
        dialog.setContentView(view);

        TextView tvAccoutDetails= (TextView) view.findViewById(R.id.tvAccoutDetails);
        TextView tvAccountHolderLabel= (TextView) view.findViewById(R.id.tvAccountHolderLabel);
        TextView tvAccountHolder= (TextView) view.findViewById(R.id.tvAccountHolder);
        TextView tvAccountNoLabel= (TextView) view.findViewById(R.id.tvAccountNoLabel);
        TextView tvAccountNo= (TextView) view.findViewById(R.id.tvAccountNo);
        TextView tvRoutingNoLabel= (TextView) view.findViewById(R.id.tvRoutingNoLabel);
        TextView tvRoutinNo= (TextView) view.findViewById(R.id.tvRoutinNo);
        TextView tvBankNameLabel= (TextView) view.findViewById(R.id.tvBankNameLabel);
        TextView tvBankName= (TextView) view.findViewById(R.id.tvBankName);
        TextView tvCurrencyLabel= (TextView) view.findViewById(R.id.tvCurrencyLabel);
        TextView tvCurrency= (TextView) view.findViewById(R.id.tvCurrency);
        TextView tvCountryLabel= (TextView) view.findViewById(R.id.tvCountryLabel);
        TextView tvCountry= (TextView) view.findViewById(R.id.tvCountry);
        TextView tvMakeDefault= (TextView) view.findViewById(R.id.tvMakeDefault);
        TextView tvDeleteAccount= (TextView) view.findViewById(R.id.tvDeleteAccount);
        ImageView ivCancel= (ImageView) view.findViewById(R.id.ivCancel);


        tvAccountHolderLabel.setTypeface(fontLight);
        tvAccountNoLabel.setTypeface(fontLight);
        tvRoutingNoLabel.setTypeface(fontLight);
        tvBankNameLabel.setTypeface(fontLight);
        tvCurrencyLabel.setTypeface(fontLight);
        tvCountryLabel.setTypeface(fontLight);

        tvMakeDefault.setTypeface(fontRegular);
        tvDeleteAccount.setTypeface(fontRegular);
        tvAccountHolder.setTypeface(fontRegular);
        tvAccountNo.setTypeface(fontRegular);
        tvRoutinNo.setTypeface(fontRegular);
        tvBankName.setTypeface(fontRegular);
        tvCurrency.setTypeface(fontRegular);
        tvCountry.setTypeface(fontRegular);
        tvAccoutDetails.setTypeface(fontRegular);

        if(bankData!=null)
        {
            tvAccountHolder.setText(bankData.getAccount_holder_name());
            tvAccountNo.setText("xxxxxxxx"+bankData.getLast4());
            tvRoutinNo.setText(bankData.getRouting_number());
            tvBankName.setText(bankData.getBank_name());
            tvCurrency.setText(bankData.getCurrency());
            tvCountry.setText(bankData.getCountry());

        }
        else
        {
            return;
        }

        tvMakeDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",bankData.getId());

                    bankBottomSheetPresenter.makeDefault(sessionManager.getSessionToken(),jsonObject);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        tvDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",bankData.getId());
                    bankBottomSheetPresenter.deleteAccount(sessionManager.getSessionToken(),jsonObject);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
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
        dismiss();
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure() {
        dismiss();
        Toast.makeText(getActivity(),getString(R.string.smthWentWrong),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
        dismiss();
        refreshBankDetails.onRefresh();
    }
}