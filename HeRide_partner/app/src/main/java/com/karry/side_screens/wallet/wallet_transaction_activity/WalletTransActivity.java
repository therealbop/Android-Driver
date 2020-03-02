package com.karry.side_screens.wallet.wallet_transaction_activity;


import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.heride.partner.R;

import com.karry.side_screens.wallet.wallet_transaction_activity.model.TransctionsItem;
import com.karry.utility.AppTypeFace;
import com.karry.side_screens.wallet.adapter.Alerts;
import com.karry.side_screens.wallet.adapter.WalletViewPagerAdapter;


import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>WalletFragment</h1>
 * <p> Class to load WalletTransActivity and show all transactions list </p>
 */
public class WalletTransActivity extends DaggerAppCompatActivity implements WalletTransactionContract.WalletTrasactionView {
    private ArrayList<TransctionsItem> allTransactionsAL;
    private ArrayList<TransctionsItem> debitTransactionsAL;
    private ArrayList<TransctionsItem> creditTransactionsAL;
    private WalletTransactionsFragment allTransactionsFrag, debitsFrag, creditsFrag;
    private Alerts alerts;
    private ProgressDialog pDialog;


    @BindView(R.id.toolbar)
    Toolbar toolBar;
    @BindView(R.id.tv_title)
    TextView tvAbarTitle;
    @BindView(R.id.pager)
    ViewPager viewPager;


    @Inject
    AppTypeFace appTypeface;
    @Inject
    WalletTransactionContract.WalletTransactionPresenter walletTransPresenter;

    @BindString(R.string.recentTransactions)
    String recentTransactions;
    @BindString(R.string.all)
    String all;
    @BindString(R.string.debit)
    String debit;
    @BindString(R.string.credit)
    String credit;
    @BindString(R.string.wait)
    String wait;
    TabLayout tabLayout;

    String tabTitles[];
    @BindDrawable(R.drawable.back_white_btn)
    Drawable back_white_btn;

    int txn_type;
    private int allPageIndex = 0, craditpageIndex = 0, debitPageIndex = 0;
    private boolean allPageIndexClick = false, craditpageIndexClick = true, debitPageIndexClick = true;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_transactions);
        ButterKnife.bind(this);

        alerts = new Alerts(this, appTypeface);
        initToolBar();
        initViews();
    }

    /* <h2>initToolBar</h2>
     * <p> method to initialize customer toolbar </p>
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initToolBar() {
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(back_white_btn);
        }

        tvAbarTitle.setTypeface(appTypeface.getPro_narMedium());
        tvAbarTitle.setText(recentTransactions);
    }

    public void setAllTransactionsAL(ArrayList<TransctionsItem> allTransactionsAL) {

        allPageIndex += 1;
        this.allTransactionsAL = allTransactionsAL;
        walletTransactionsApiSuccessViewNotifier("allTransactionsAL");


    }

    public void setDebitTransactionsAL(ArrayList<TransctionsItem> debitTransactionsAL) {
        debitPageIndex += 1;
        this.debitTransactionsAL = debitTransactionsAL;
        walletTransactionsApiSuccessViewNotifier("debitTransactionsAL");
    }

    public void setCreditTransactionsAL(ArrayList<TransctionsItem> creditTransactionsAL) {
        craditpageIndex += 1;
        this.creditTransactionsAL = creditTransactionsAL;
        walletTransactionsApiSuccessViewNotifier("creditTransactionsAL");
    }


    /**
     * <h2>initViews</h2>
     * <P> custom method to initializes all the views of the screen </P>
     */
    private void initViews() {
        viewPager.setOffscreenPageLimit(3);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabTitles = new String[]{all, debit, credit};
        WalletViewPagerAdapter viewPagerAdapter = new WalletViewPagerAdapter(getSupportFragmentManager());


        this.allTransactionsFrag = WalletTransactionsFragment.getNewInstance();
        viewPagerAdapter.addFragment(allTransactionsFrag, tabTitles[0]);

        this.debitsFrag = WalletTransactionsFragment.getNewInstance();
        viewPagerAdapter.addFragment(debitsFrag, tabTitles[1]);

        this.creditsFrag = WalletTransactionsFragment.getNewInstance();
        viewPagerAdapter.addFragment(creditsFrag, tabTitles[2]);
        viewPagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(viewPagerAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        viewPagerListener();
        loadTransactions(false, 0);

    }
    private void viewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        if (allPageIndexClick) {
                            allPageIndexClick = false;
                            walletTransPresenter.initLoadTransactions(3, false, allPageIndex);
                        }
                        break;
                    case 1:
                        if (debitPageIndexClick) {
                            debitPageIndexClick = false;
                            walletTransPresenter.initLoadTransactions(1, false, debitPageIndex);
                        }
                        break;
                    case 2:
                        if (craditpageIndexClick) {
                            craditpageIndexClick = false;
                            walletTransPresenter.initLoadTransactions(2, false, craditpageIndex);
                        }
                        break;
                }
// Check if this is the page you want.
            }
        });
    }

    /**
     * <h2>loadTransactions</h2>
     * <p> method to init getTransactionsList api </p>
     */
    public void loadTransactions(boolean isFromOnRefresh, int pageIndex) {
        switch (tabLayout.getSelectedTabPosition()){

            case 0:
                txn_type=3;
                break;
            case 1:
                txn_type=1;
                break;
            case 2:
                txn_type=2;
                break;
        }

        walletTransPresenter.initLoadTransactions(txn_type, isFromOnRefresh, pageIndex);
    }


    @Override
    public void hideProgressDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }




    @Override
    public void showProgressDialog(String msg) {
        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
        }

        if (!pDialog.isShowing()) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(wait);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }


    @Override
    public void showToast(String msg, int duration) {
        hideProgressDialog();
        Toast.makeText(this, msg, duration).show();
    }


    @Override
    public void showAlert(String title, String msg) {
        hideProgressDialog();
    }


    /**
     * <h2>showAlert</h2>
     * <p> method to show alert that these is no internet connectivity there </p>
     */
    @Override
    public void noInternetAlert() {
        alerts.showNetworkAlert(this);
    }


    /**
     * <h2>walletTransactionsApiSuccessViewNotifier</h2>
     * <p> method to update fields data on the success response of api </p>
     * @param s
     */
    @Override
    public void walletTransactionsApiSuccessViewNotifier(String s) {
        /*hideProgressDialog();
        String TAG = "WalletTransactionAct";
        Log.d(TAG, "walletTransactionsApiSuccessViewNotifier onSuccess: ");

        if (this.allTransactionsFrag != null) {
            this.allTransactionsFrag.hideRefreshingLayout();
            this.allTransactionsFrag.notifyDataSetChangedCustom(allTransactionsAL);
        }


        if (this.debitsFrag != null) {
            this.debitsFrag.hideRefreshingLayout();
            this.debitsFrag.notifyDataSetChangedCustom(debitTransactionsAL);
        }

        if (this.creditsFrag != null) {
            this.creditsFrag.hideRefreshingLayout();
            this.creditsFrag.notifyDataSetChangedCustom(creditTransactionsAL);
        }*/
        hideProgressDialog();
        String TAG = "WalletTransactionAct";
        Log.d(TAG, "walletTransactionsApiSuccessViewNotifier showToast: ");

        if (s.equals("allTransactionsAL")) {
            this.allTransactionsFrag.notifyDataSetChangedCustom(this.allTransactionsAL);
        }
        if (s.equals("debitTransactionsAL")) {
            this.debitsFrag.notifyDataSetChangedCustom(debitTransactionsAL);
        }
        if (s.equals("creditTransactionsAL")) {
            this.creditsFrag.notifyDataSetChangedCustom(creditTransactionsAL);
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

}

