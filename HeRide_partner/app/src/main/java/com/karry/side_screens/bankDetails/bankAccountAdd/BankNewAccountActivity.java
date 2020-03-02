package com.karry.side_screens.bankDetails.bankAccountAdd;

import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.karry.authentication.signup.GenericListActivity;
import com.heride.partner.R;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCurrencyListSelection;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karry.utility.VariableConstant.COUNTRY;
import static com.karry.utility.VariableConstant.CURRENCY_SELECT;
import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.TITLE;
import static com.karry.utility.VariableConstant.TYPE;

public class BankNewAccountActivity extends DaggerAppCompatActivity implements
        BankNewAccountContract.BankNewAccountView {


    @Inject BankNewAccountContract.BankNewAccountPresenter bankNewAccountPresenter;
    @Inject AppTypeFace appTypeFace;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_logout) TextView tv_save;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindString(R.string.addBank)String title;
    @BindString(R.string.save)String save;

    @BindView(R.id.progressBar)ProgressBar progressBar;

    @BindView(R.id.etName)EditText etName;
    @BindView(R.id.etAccountNo)EditText etAccountNo;
    @BindView(R.id.etRoutingNo)EditText etRoutingNo;

    @BindView(R.id.tilName)TextInputLayout tilName;
    @BindView(R.id.tilAccountNo)TextInputLayout tilAccountNo;
    @BindView(R.id.tilRoutingNo)TextInputLayout tilRoutingNo;

    @BindView(R.id.til_accountCurrency)TextInputLayout til_accountCurrency;
    @BindView(R.id.et_accountCurrency)EditText et_accountCurrency;
    private static final int SELECT_AN_CURRENCY = 410;

    private String countryID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_new_account);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);
        bankNewAccountPresenter.setActionBar();
        countryID = getIntent().getStringExtra(COUNTRY);
        Utility.printLog("country code isss :  "+countryID);

        initViews();
    }

    /**
     * <h1>initViews</h1>
     * <p>Initalize the Views.</p>
     */
    private void initViews()
    {
        etName.setTypeface(appTypeFace.getPro_narMedium());
        etAccountNo.setTypeface(appTypeFace.getPro_narMedium());
        etRoutingNo.setTypeface(appTypeFace.getPro_narMedium());
        et_accountCurrency.setTypeface(appTypeFace.getPro_narMedium());
        et_accountCurrency.setInputType(InputType.TYPE_NULL);
    }


    @OnClick({R.id.tv_logout})
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.tv_logout:
                bankNewAccountPresenter.validateData(etName.getText().toString(),
                        etAccountNo.getText().toString(),
                        etRoutingNo.getText().toString());
                break;
        }
    }



    @OnFocusChange({R.id.et_accountCurrency,R.id.etRoutingNo})
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {

            case R.id.etRoutingNo:
                if(!hasFocus) {
                    getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
                    et_accountCurrency.requestFocus();
                }
                break;

            case R.id.et_accountCurrency:
                if(hasFocus) {
                    getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
                    bankNewAccountPresenter.accountCurrencyAPI( countryID);
                }
                break;
        }
    }


    /**********************************************************************************************/
    @Override
    public void initActionBar() {

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        tv_save.setVisibility(View.VISIBLE);
        tv_save.setTypeface(appTypeFace.getPro_narMedium());
        bankNewAccountPresenter.setActionBarTitle();

    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
        tv_save.setText(save);
    }

    @Override
    public void editTextErr(String errorEditText) {

        switch (errorEditText){

            case "Name":
                tilName.setErrorEnabled(true);
                tilName.setError(getString(R.string.enterAccountHoldername));
                break;
            case "AccountNo":
                tilName.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(true);
                tilAccountNo.setError(getString(R.string.enterAccountNo));
                break;
            case "RoutingNo":
                tilName.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(false);
                tilRoutingNo.setErrorEnabled(true);
                tilRoutingNo.setError(getString(R.string.enterRoutinNo));
                break;

            case "selectedCurrency":
                tilName.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(false);
                tilRoutingNo.setErrorEnabled(false);
                til_accountCurrency.setErrorEnabled(true);
                til_accountCurrency.setError(getString(R.string.select_curr));
                break;
            default:
                tilName.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(false);
                tilRoutingNo.setErrorEnabled(false);
                til_accountCurrency.setErrorEnabled(false);
                bankNewAccountPresenter.externalAccountAPI(etName.getText().toString(),
                        etAccountNo.getText().toString(),
                        etRoutingNo.getText().toString());
                break;
        }
    }

    @Override
    public void externalAccountAPISuccess(String msg) {
        Utility.BlueToast(this,msg);
        onBackPressed();
    }

    @Override
    public void setCurrencyListforSelect(ArrayList<ConnectAccountCurrencyListSelection> currencylist) {
        if(currencylist != null && currencylist.size()>0){
            Bundle mBundle = new Bundle();
            Intent intent= new Intent(this, GenericListActivity.class);
            mBundle.putString(TITLE, getResources().getString(R.string.accountCurrency));
            mBundle.putString(TYPE, CURRENCY_SELECT);
            mBundle.putSerializable(DATA, currencylist);
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_CURRENCY);
        }


    }

    @Override
    public void setCurrencyText(String currency) {
        et_accountCurrency.setText(currency);
        etName.requestFocus();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case SELECT_AN_CURRENCY:
                if(data!=null)
                    bankNewAccountPresenter.onCurrencySelected(data);
                break;
        }
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
        finish();
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
}
