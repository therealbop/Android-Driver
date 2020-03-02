package com.karry.side_screens.portal;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.utility.AppTypeFace;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;



public class PortalActivity extends DaggerAppCompatActivity implements PortalContract.PortalView{

    @Inject PortalContract.PortalPresenter portalPresenter;
    @Inject AppTypeFace appTypeFace;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.web_portal) WebView web_portal;
    @BindString(R.string.portal) String title;

    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        ButterKnife.bind(this);
        portalPresenter.setActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        portalPresenter.checklanguageLocale();
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
        portalPresenter.setActionBarTitle();
        portalPresenter.setPortalURL();

    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    @Override
    public void loadPortalWeb(String portalURL) {
        web_portal.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                portalPresenter.webPageLoaded(true);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                portalPresenter.webPageLoaded(false);
            }
        });

        web_portal.loadUrl(portalURL);
        web_portal.getSettings().setJavaScriptEnabled(true);
    }

    /**********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if(web_portal.canGoBack())
            {
                web_portal.goBack();
                web_portal.clearHistory();
            }
            else
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
