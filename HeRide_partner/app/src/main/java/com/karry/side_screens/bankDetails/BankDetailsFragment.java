package com.karry.side_screens.bankDetails;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.karry.adapter.BankDetailsRVA;
import com.karry.app.mainActivity.MainActivity;
import com.karry.pojo.bank.LegalEntity;
import com.karry.pojo.bank.StripeDetailsPojo;
import com.karry.pojo.bank.Verification;
import com.karry.side_screens.bankDetails.bankAccountAdd.BankNewAccountActivity;
import com.karry.side_screens.bankDetails.bankStripeAccountAdd.BankStripeAddActivity;
import com.karry.dagger.ActivityScoped;
import com.heride.partner.R;
import com.karry.pojo.bank.BankList;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

import static com.karry.utility.VariableConstant.COUNTRY;
import static com.karry.utility.VariableConstant.STRIPE_DATA;
import static com.karry.utility.VariableConstant.UNVERIFIED_BANK_DETAILS;


/**
 * <h1>BankDetailsFragment</h1>
 * <p>this is the fragment which is for stripe and Bank adding.</p>
 */
@ActivityScoped
public class BankDetailsFragment extends DaggerFragment
        implements BankDetailsFragContract.BankDetailsFragView {

    @Inject BankDetailsFragContract.BankDetailsFragPresenter bankDetailsFragPresenter;
    @Inject AppTypeFace appTypeFace;

    @Inject
    public BankDetailsFragment() {
    }

    @BindView(R.id.tvStep1)TextView tvStep1;
    @BindView(R.id.tvAddStripeAccount)TextView tvAddStripeAccount;
    @BindView(R.id.tvStipeAccountNo)TextView tvStipeAccountNo;
    @BindView(R.id.tvStatus)TextView tvStatus;
    @BindView(R.id.tvStep2)TextView tvStep2;
    @BindView(R.id.tvAddBankAccount)TextView tvAddBankAccount;
    @BindView(R.id.ivStatus)ImageView ivStatus;
    @BindView(R.id.cvStipeDetails)CardView cvStipeDetails;
    @BindView(R.id.cvLinkBankAcc)CardView cvLinkBankAcc;
    @BindView(R.id.rvBank)RecyclerView rvBank;

    private Unbinder unbinder;

    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Inject BankDetailsRVA bankDetailsRVA;
    private ArrayList<BankList> bankLists;
    private LegalEntity mLegalEntity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bank_list_fragment2, container, false);

        unbinder = ButterKnife.bind(this,rootView);
        bankDetailsFragPresenter.attachView(this);

        initLayout();
        ((MainActivity)getActivity()).setFragmentRefreshListener(this::onResume);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bankDetailsFragPresenter.detachView();
    }

    /**
     * <h1>initLayout</h1>
     * <p>initialize the view.</p>
     */
    private void initLayout() {

        tvStep2.setTypeface(appTypeFace.getPro_News());
        tvStep1.setTypeface(appTypeFace.getPro_News());
        tvStatus.setTypeface(appTypeFace.getPro_News());
        tvStipeAccountNo.setTypeface(appTypeFace.getPro_News());
        tvAddBankAccount.setTypeface(appTypeFace.getPro_News());
        tvAddStripeAccount.setTypeface(appTypeFace.getPro_News());


        cvStipeDetails.setEnabled(false);
        tvAddBankAccount.setClickable(false);
        tvAddBankAccount.setFocusable(false);

        rvBank.setLayoutManager(new LinearLayoutManager(getActivity()));
        bankLists = new ArrayList<>();
        bankDetailsRVA.setBankData(bankLists, getFragmentManager()/*, this*/);
        rvBank.setAdapter(bankDetailsRVA);
    }

    @OnClick({R.id.tvAddStripeAccount, R.id.tvAddBankAccount,R.id.tvStatus,R.id.ivStatus})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.tvAddStripeAccount:
                Intent intent = new Intent(getActivity(), BankStripeAddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            case R.id.ivStatus:
            case R.id.tvStatus:
                Intent statusIntent = new Intent(getActivity(), BankStripeAddActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(UNVERIFIED_BANK_DETAILS,mLegalEntity);
                statusIntent.putExtras(mBundle);
                startActivity(statusIntent);
                break;


            case R.id.tvAddBankAccount:
                bankDetailsFragPresenter.addNewBank();
                break;
            case R.id.cvStipeDetails:
                bankDetailsFragPresenter.updateStripe();
                break;
            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.printLog("BankDetailsFragment onResume...");
        bankDetailsFragPresenter.getConnectAccount();
    }


    @Override
    public void showAddStipe(String msg) {
        Utility.BlueToast(getContext(),msg);
        tvAddStripeAccount.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.GONE);
        ivStatus.setVisibility(View.GONE);
        tvStipeAccountNo.setVisibility(View.GONE);
        tvAddBankAccount.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_corner_bank_acc_gunsmoke));
        tvAddBankAccount.setTextColor(getResources().getColor(R.color.gunsmoke));
    }

    @Override
    public void sripeAccAddSuccess(StripeDetailsPojo stripeDetailsPojo) {

        VariableConstant.IS_STRIPE_ADDED = true;
        tvAddStripeAccount.setVisibility(View.GONE);
        String status = stripeDetailsPojo.getLegal_entity().getVerification().getStatus();
        tvStatus.setText(status);

        tvStipeAccountNo.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.VISIBLE);
        ivStatus.setVisibility(View.VISIBLE);
        cvStipeDetails.setEnabled(false);
        tvAddBankAccount.setEnabled(false);
        cvStipeDetails.setClickable(false);
        tvAddBankAccount.setClickable(false);
        tvAddBankAccount.setFocusable(false);

        mLegalEntity  = stripeDetailsPojo.getLegal_entity();
        if (stripeDetailsPojo.getLegal_entity().getVerification().getStatus().equals("verified")) {
            ivStatus.setClickable(false);
            ivStatus.setEnabled(false);
            tvStatus.setClickable(false);
            tvStatus.setEnabled(false);
            ivStatus.setImageResource(R.drawable.verified);
            tvStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
            tvAddBankAccount.setFocusable(true);
            tvAddBankAccount.setEnabled(true);
            tvAddBankAccount.setClickable(true);

            tvAddBankAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bankDetailsFragPresenter.addNewBank();

                }
            });


            if(stripeDetailsPojo.getExternal_accounts().getData().size()>0){
                bankLists.clear();
                cvLinkBankAcc.setVisibility(View.GONE);
                bankLists.addAll(stripeDetailsPojo.getExternal_accounts().getData());
                bankDetailsRVA.notifyDataSetChanged();
            }


        } else {
            ivStatus.setClickable(true);
            ivStatus.setEnabled(true);
            tvStatus.setClickable(true);
            tvStatus.setEnabled(true);
            ivStatus.setImageResource(R.drawable.unverified);
            cvStipeDetails.setEnabled(true);
            cvStipeDetails.setClickable(true);
            tvStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
            tvAddBankAccount.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_corner_bank_acc_gunsmoke));
            tvAddBankAccount.setTextColor(getResources().getColor(R.color.gunsmoke));
        }


    }




    @Override
    public void openUpdateStripe(StripeDetailsPojo stripeDetailsPojo) {
        Intent editStripe = new Intent(getActivity(), BankStripeAddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(STRIPE_DATA,stripeDetailsPojo);
        editStripe.putExtras(bundle);
        startActivity(editStripe);
    }

    @Override
    public void openActivityForBankAdd(String countryID) {
        Intent mintent = new Intent(getActivity(), BankNewAccountActivity.class);
        mintent.putExtra(COUNTRY,countryID);
        startActivity(mintent);
    }

    /**********************************************************************************************/
    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onFailure(String msg) {
        Utility.BlueToast(getActivity() ,msg);
    }

    @Override
    public void onStop() {
        super.onStop();
        bankDetailsFragPresenter.compositeDisposableClear();
    }
}
