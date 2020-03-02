package com.karry.authentication.forgotpassword;


import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.heride.partner.R;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.Utility;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karry.utility.VariableConstant.USER_ID;


/**************************************************************************************************/
public class ChangePasswordActivity extends DaggerAppCompatActivity implements ChangePasswordContract.View {

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_newpass_msg) TextView tv_newpass_msg;
    @BindView(R.id.tv_new_pass) TextView tv_new_pass;
    @BindView(R.id.tv_reenter_pass) TextView tv_reenter_pass;
    @BindView(R.id.tv_continue) TextView tv_continue;
    @BindView(R.id.et_new_pass) EditText et_new_pass;
    @BindView(R.id.et_reenter_pass) EditText et_reenter_pass;
    @BindView(R.id.sv_signup) LinearLayout ll_first;
    @BindView(R.id.seek_bar_button)  SeekBar seek_bar_button;
    @BindView(R.id.svContent)  ScrollView svContent;

    @BindColor(R.color.colorPrimaryDark) int colorPrimaryDark;
    @BindColor(R.color.white) int white;
    @BindColor(R.color.colorPrimary) int colorPrimary;


    @BindString(R.string.EntNewPass)String title;
    @BindString(R.string.message)String message;
    @BindString(R.string.OK)String OK;
    @BindString(R.string.password_miss)String password_miss;
    @BindString(R.string.passNotMactch)String passNotMactch;

    @Inject ChangePasswordContract.Presenter presenter;
    @Inject AppTypeFace appTypeFace;

    private String userId;

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);
        initializeViews();
    }

    /**
     * <h1>initializeViews</h1>
     * <p>which initilize the Views</p>
     */
    private void initializeViews() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
        }

        tv_title.setText(title);
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        iv_search.setVisibility(View.GONE);
        tv_newpass_msg.setTypeface(appTypeFace.getPro_News());
        tv_new_pass.setTypeface(appTypeFace.getPro_News());
        tv_reenter_pass.setTypeface(appTypeFace.getPro_News());
        tv_continue.setTypeface(appTypeFace.getPro_narMedium());
        et_new_pass.setTypeface(appTypeFace.getPro_News());
        et_reenter_pass.setTypeface(appTypeFace.getPro_News());

        userId = getIntent().getStringExtra(USER_ID);

    }

    @Override
    protected void onResume() {
        super.onResume();
        svContent.postDelayed(() -> {
            svContent.fullScroll(View.FOCUS_DOWN);
            et_new_pass.requestFocus();
        }, 1000);
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

    @OnTextChanged({ R.id.et_new_pass, R.id.et_reenter_pass})
    public void afterTextChanged(Editable editable)
    {
        if(editable == et_new_pass.getEditableText()){
            if(!et_reenter_pass.getText().toString().isEmpty()){
                et_new_pass.setError(null);
                et_reenter_pass.setError(null);
                presenter.validatePassword("",et_new_pass.getText().toString(),et_reenter_pass.getText().toString());
            }
        }
        else if(editable == et_reenter_pass.getEditableText()) {
            if(!et_new_pass.getText().toString().isEmpty()){
                et_reenter_pass.setError(null);
                et_new_pass.setError(null);
                if(et_new_pass.getText().toString().length()<=et_reenter_pass.getText().toString().length())
                    presenter.validatePassword("",et_new_pass.getText().toString(),et_reenter_pass.getText().toString());
            }
        }

    }

    @OnClick(R.id.tv_continue)
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_continue:
                presenter.validatePassword(userId,et_new_pass.getText().toString(),et_reenter_pass.getText().toString());
                break;
        }
    }

    @Override
    public void startProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String title, String msg) {
        Utility.mShowMessage(title,msg,this);
    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.customAlertDialogCloseActivity(this,message,msg,OK);
    }

    @Override
    public void enableHalfClick() {
        presenter.setSeekBarProgress(seek_bar_button,50);
        tv_continue.setTextColor(colorPrimaryDark);
    }

    @Override
    public void enableClick() {
        presenter.setSeekBarProgress(seek_bar_button,100);
        tv_continue.setTextColor(white);
    }

    @Override
    public void invalidPassword() {
        et_new_pass.setError(password_miss);
        presenter.setSeekBarProgress(seek_bar_button,0);
        tv_continue.setTextColor(colorPrimary);
    }

    @Override
    public void invalidConformPassword() {
        et_reenter_pass.setError(passNotMactch);
    }
}
