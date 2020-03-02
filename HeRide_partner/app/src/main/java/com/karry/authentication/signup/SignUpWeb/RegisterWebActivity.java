package com.karry.authentication.signup.SignUpWeb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.heride.partner.BuildConfig;
import com.heride.partner.R;
import com.karry.authentication.signup.SignUpPersonal.SignupPersonalActvity;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karry.utility.VariableConstant.FROM;
import static com.karry.utility.VariableConstant.PRIVACY;
import static com.karry.utility.VariableConstant.SUPPORT;
import static com.karry.utility.VariableConstant.TERMS_CONDITION;
import static com.karry.utility.VariableConstant.TITLE;
import static com.karry.utility.VariableConstant.URL;

public class RegisterWebActivity extends DaggerAppCompatActivity implements RegisterWebContract.RegisterWebView{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_header) TextView tv_header;
    @BindView(R.id.tv_register_now) TextView tv_register_now;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindString(R.string.appname_title) String title;
    @BindString(R.string.support_content) String support_content;
    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindDrawable(R.drawable.selector_signup_close)  Drawable selector_signup_close;

    @Inject RegisterWebContract.RegisterWebPresenter registerWebPresenter;
    @Inject AppTypeFace appTypeFace;
    private String WEB_URL = BuildConfig.SIGNUP_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_web);
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);

        ButterKnife.bind(this);
        initializeViews();

        try {
            Bundle mBundle= getIntent().getExtras();

            String from = mBundle.getString(FROM);
            title = mBundle.getString(TITLE);
            WEB_URL = mBundle.getString(URL);
            tv_register_now.setVisibility(View.GONE);
            Utility.printLog("the weburl is: "+WEB_URL);


            switch (from){
                case PRIVACY:
                    /*WEB_URL = VariableConstant.PRIVACY_POLICY;*/
                    tv_header.setVisibility(View.GONE);
                    break;
                case TERMS_CONDITION:
                    title = getResources().getString(R.string.appname_title);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_header.setVisibility(View.GONE);
                    tv_register_now.setVisibility(View.VISIBLE);
                    break;
                case SUPPORT:
                    tv_header.setVisibility(View.VISIBLE);
                    tv_header.setText(title);
                    title = support_content;
                    break;
            }

            initializeViews();
        }catch (Exception e){

        }
        registerWebPresenter.setActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerWebPresenter.checklanguageLocale();
    }

    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     */
    private void initializeViews() {

        tv_register_now.setTypeface(appTypeFace.getPro_narMedium());
        tv_header.setTypeface(appTypeFace.getPro_narMedium());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(0);
        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                registerWebPresenter.webPageLoaded(true);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                registerWebPresenter.webPageLoaded(false);
            }
        });

        webView.loadUrl(WEB_URL);
        enableActivityTxt();
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(selector_signup_close);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        registerWebPresenter.setActionBarTitle();
    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    @Override
    public void enableActivityTxt() {
        tv_register_now.setBackgroundColor(colorPrimary);
        tv_register_now.setOnClickListener(v -> registerWebPresenter.success());
    }

    @Override
    public void startNextActivity() {
        startActivity(new Intent(this, SignupPersonalActvity.class));
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


}
