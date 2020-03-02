package com.karry.side_screens.wallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.karry.app.mainActivity.MainActivity;
import com.heride.partner.R;
import com.karry.side_screens.wallet.adapter.Alerts;
import com.karry.side_screens.wallet.changeCard.ChangeCardActivity;
import com.karry.side_screens.wallet.wallet_transaction_activity.WalletTransActivity;
import com.karry.utility.AppTypeFace;
import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

import static android.app.Activity.RESULT_OK;

import static com.karry.utility.VariableConstant.CARD_BRAND;
import static com.karry.utility.VariableConstant.LAST_DIGITS;
import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

public class WalletFragment extends DaggerFragment implements WalletActivityContract.WalletView
{
    @Inject WalletActivityContract.WalletPresenter walletPresenter;
    @Inject
    AppTypeFace fontUtils;
    @Inject
    Alerts alerts;

    @BindView(R.id.tv_wallet_balance) TextView tv_wallet_balance;
    @BindView(R.id.tv_wallet_softLimitValue) TextView tv_wallet_softLimitValue;
    @BindView(R.id.tv_wallet_hardLimitValue) TextView tv_wallet_hardLimitValue;
    @BindView(R.id.tv_wallet_cardNo) TextView tv_wallet_cardNo;
    @BindView(R.id.tv_wallet_currencySymbol) TextView tv_wallet_currencySymbol;
    @BindView(R.id.et_wallet_payAmountValue) EditText et_wallet_payAmountValue;
    @BindView(R.id.tv_wallet_curCredit_label) TextView tv_wallet_curCredit_label;
    @BindView(R.id.tv_wallet_softLimitLabel) TextView tv_wallet_softLimitLabel;
    @BindView(R.id.tv_wallet_hardLimitLabel) TextView tv_wallet_hardLimitLabel;
    @BindView(R.id.btn_wallet_recentTransactions) Button btn_wallet_recentTransactions;
    @BindView(R.id.tv_wallet_payUsing_cardLabel) TextView tv_wallet_payUsing_cardLabel;
    @BindView(R.id.tv_wallet_payAmountLabel) TextView tv_wallet_payAmountLabel;
    @BindView(R.id.tv_wallet_softLimitMsgLabel) TextView tv_wallet_softLimitMsgLabel;
    @BindView(R.id.tv_wallet_softLimitMsg) TextView tv_wallet_softLimitMsg;
    @BindView(R.id.tv_wallet_hardLimitMsgLabel) TextView tv_wallet_hardLimitMsgLabel;
    @BindView(R.id.tv_wallet_hardLimitMsg) TextView tv_wallet_hardLimitMsg;
    @BindView(R.id.tv_wallet_credit_desc) TextView tv_wallet_credit_desc;
    @BindView(R.id.btn_wallet_ConfirmAndPay) Button btn_wallet_ConfirmAndPay;
    @BindString(R.string.cancel) String action_cancel;
    @BindString(R.string.confirm) String confirm;
    @BindString(R.string.paymentMsg1) String paymentMsg1;
    @BindString(R.string.app_name) String app_name;
    @BindString(R.string.paymentMsg2) String paymentMsg2;
    @BindString(R.string.confirmPayment) String confirmPayment;
    @BindString(R.string.cardNoHidden) String cardNoHidden;
    @BindString(R.string.wait) String wait;
    @BindString(R.string.hardLimitMsg) String hardLimitMsg;
    @BindString(R.string.softLimitMsg) String softLimitMsg;
    @BindString(R.string.wallet) String wallet;
    @BindString(R.string.choose_card) String choose_card;

    @BindColor(R.color.black) int black;
    @BindColor(R.color.yellow_light) int yellow_light;
    @BindColor(R.color.red_light) int red_light;
    @BindColor(R.color.gray) int gray;

    @BindDrawable(R.drawable.ic_payment_card_icon) Drawable ic_payment_card_icon;
    @BindDrawable(R.drawable.home_next_arrow_icon_off) Drawable home_next_arrow_icon_off;

    private ProgressDialog pDialog;
    private Unbinder unbinder;
    private String currencySymbol = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Inject
    public WalletFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_wallet, container, false);
        unbinder=ButterKnife.bind(this,view);
        walletPresenter.attachView(this);
        initViews();
        initPayViews();
        initSoftHardLimitDescriptionsView();
        showAddBalanceNotifier();
        ((MainActivity)getActivity()).setFragmentRefreshListener(this::onResume);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        walletPresenter.detachview();
    }

    @OnClick({R.id.btn_wallet_recentTransactions,R.id.tv_wallet_cardNo,R.id.btn_wallet_ConfirmAndPay})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_wallet_recentTransactions:
                Intent intent = new Intent(getActivity(), WalletTransActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_wallet_cardNo:
                Intent cardsIntent = new Intent(getActivity(), ChangeCardActivity.class);
                startActivityForResult(cardsIntent, 1);
                break;

            case R.id.btn_wallet_ConfirmAndPay:
                walletPresenter.checkAmount(et_wallet_payAmountValue.getText().toString());
                showRechargeConfirmationAlert();
                break;
        }
    }

    @OnTextChanged({R.id.et_wallet_payAmountValue})
    public void onTextChange(Editable editable)
    {
        walletPresenter.checkAmount(et_wallet_payAmountValue.getText().toString());
    }


    /**
     *<h2>initViews</h2>
     * <P> custom method to load tooth_top views of the screen </P>
     */
    private void initViews()
    {
        walletPresenter.getLastCardNo();
        tv_wallet_credit_desc.setTypeface(fontUtils.getPro_News());
        tv_wallet_curCredit_label.setTypeface(fontUtils.getPro_News());
        tv_wallet_balance.setTypeface(fontUtils.getPro_News());
        tv_wallet_softLimitLabel.setTypeface(fontUtils.getPro_News());
        tv_wallet_softLimitValue.setTypeface(fontUtils.getPro_News());
        tv_wallet_hardLimitLabel.setTypeface(fontUtils.getPro_News());
        tv_wallet_hardLimitValue.setTypeface(fontUtils.getPro_News());
        btn_wallet_recentTransactions.setTypeface(fontUtils.getPro_News());
        btn_wallet_ConfirmAndPay.setTypeface(fontUtils.getPro_narMedium());
        et_wallet_payAmountValue.setTypeface(fontUtils.getPro_News());
    }

    @Override
    public void setBalanceValues(String balance,String hardLimit,String softLimit,String currencySymbol)
    {
        tv_wallet_softLimitValue.setText(softLimit);
        tv_wallet_hardLimitValue.setText(hardLimit);
        tv_wallet_balance.setText(balance);
        this.currencySymbol = currencySymbol;
        tv_wallet_currencySymbol.setText(currencySymbol);
    }


    /**
     *<h2>initPayViews</h2>
     * <P> custom method to load payment views of the screen </P>
     */
    private void initPayViews()
    {
        tv_wallet_payUsing_cardLabel.setTypeface(fontUtils.getPro_narMedium());
        tv_wallet_cardNo.setTypeface(fontUtils.getPro_News());
        tv_wallet_payAmountLabel.setTypeface(fontUtils.getPro_narMedium());
        tv_wallet_currencySymbol.setTypeface(fontUtils.getPro_News());
        tv_wallet_hardLimitLabel.setTypeface(fontUtils.getPro_News());
        tv_wallet_hardLimitValue.setTypeface(fontUtils.getPro_News());
    }


    /**
     *<h2>initSoftHardLimitDescriptionsView</h2>
     * <P> custom method to init soft and hard limit description views of the screen </P>
     */
    private void initSoftHardLimitDescriptionsView()
    {
        tv_wallet_softLimitMsgLabel.setTypeface(fontUtils.getPro_News());
        tv_wallet_softLimitMsg.setTypeface(fontUtils.getPro_News());
        tv_wallet_softLimitMsg.setText(softLimitMsg);
        tv_wallet_hardLimitMsgLabel.setTypeface(fontUtils.getPro_News());
        tv_wallet_hardLimitMsg.setTypeface(fontUtils.getPro_News());
        tv_wallet_hardLimitMsg.setText(hardLimitMsg);
        btn_wallet_ConfirmAndPay.setTypeface(fontUtils.getPro_News());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        walletPresenter.getWalletLimits();
    }


    @Override
    public void hideProgressDialog()
    {
        if(pDialog != null && pDialog.isShowing())
        {
            pDialog.dismiss();
        }
    }



    @Override
    public void showProgressDialog()
    {
        if(pDialog == null)
        {
            pDialog = alerts.getProcessDialog(getActivity());
        }

        if(!pDialog.isShowing())
        {
            pDialog = alerts.getProcessDialog(getActivity());
            pDialog.show();
        }
    }


    @Override
    public void showToast(String msg, int duration)
    {
        hideProgressDialog();
        Toast.makeText(getActivity(), msg, duration).show();
    }


    @Override
    public void showAlert(String title, String msg)
    {
        hideProgressDialog();
        Toast.makeText(getActivity(), "SHOW ALERT", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void noInternetAlert()
    {
        alerts.showNetworkAlert(getActivity());
    }

    /**
     *<h2>walletDetailsApiErrorViewNotifier</h2>
     * <p> method to notify api error </p>
     */
    @Override
    public void walletDetailsApiErrorViewNotifier(String error)
    {
        alerts.userPromptWithOneButton(error,getActivity());
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            Bundle bundle;
            bundle=data.getExtras();

            assert bundle != null;
            String cardNum = bundle.getString(LAST_DIGITS);
            String cardType = bundle.getString(CARD_BRAND);
            setCard(cardNum,cardType);
        }
        /*else
            setNoCard();
*/

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void setNoCard()
    {
        btn_wallet_ConfirmAndPay.setEnabled(false);
        btn_wallet_ConfirmAndPay.setBackgroundColor(gray);
        Drawable cardBrand = ic_payment_card_icon;
        tv_wallet_cardNo.setText(choose_card);
        tv_wallet_cardNo.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,home_next_arrow_icon_off,null);
    }

    @Override
    public void showAddBalanceNotifier()
    {
        btn_wallet_ConfirmAndPay.setEnabled(false);
        btn_wallet_ConfirmAndPay.setBackgroundColor(gray);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void setCard(String cardNum,String cardType)
    {
        btn_wallet_ConfirmAndPay.setEnabled(true);
        btn_wallet_ConfirmAndPay.setBackground(this.getResources().getDrawable(R.drawable.selector_layout));
        Drawable cardBrand = ic_payment_card_icon;
        if(cardType!=null){
            cardBrand = getResources().getDrawable(BRAND_RESOURCE_MAP.get(cardType));
            tv_wallet_cardNo.setText(cardNoHidden+" "+cardNum);
        }
        tv_wallet_cardNo.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,home_next_arrow_icon_off,null);
    }

    public void enableButton()
    {
        btn_wallet_ConfirmAndPay.setEnabled(true);
        btn_wallet_ConfirmAndPay.setBackground(this.getResources().getDrawable(R.drawable.selector_layout));
    }


    @Override
    public void onPause()
    {
        super.onPause();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void showRechargeConfirmationAlert()
    {
        String amount = et_wallet_payAmountValue.getText().toString();

            Dialog dialog = alerts.userPromptWithTwoButtons(getActivity());
        TextView tv_alert_yes =  dialog.findViewById(R.id.tv_alert_yes);
        TextView tv_alert_title =  dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body =  dialog.findViewById(R.id.tv_alert_body);
        tv_alert_title.setText(confirmPayment);
        tv_alert_body.setText(paymentMsg1.concat(" ").concat(app_name).concat(" ").concat(paymentMsg2).concat(" ").concat(currencySymbol).concat(" ").concat(amount));

        tv_alert_yes.setOnClickListener(v ->
        {
            walletPresenter.rechargeWallet(amount);
            dialog.dismiss();
        });
        dialog.show();
    }
}
