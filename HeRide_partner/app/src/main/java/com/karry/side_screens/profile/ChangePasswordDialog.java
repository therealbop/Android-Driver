package com.karry.side_screens.profile;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.karry.data.source.local.PreferenceHelperDataSource;
import com.heride.partner.R;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import javax.inject.Inject;

/**
 * Created by embed on 20/5/17.
 */


public class ChangePasswordDialog extends Dialog {
    private RefreshProfile refreshProfile;
    private Context context;
    private Dialog passwordDialog;
    private String TAG = ChangePasswordDialog.class.getSimpleName(), from;
    private EditText et_oldpass;
    private TextView tv_confirm;
    private String from_pass = "Password", from_name = "Name", from_signup = "from_signup",from_rcvr="receiver";
    private View view_;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    /**********************************************************************************************/
    public ChangePasswordDialog(Context context, String from, RefreshProfile refreshProfile) {
        super(context);
        this.context = context;
        this.from = from;
        this.refreshProfile = refreshProfile;
        this.setCanceledOnTouchOutside(true);

    }

    /**********************************************************************************************/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setCanceledOnTouchOutside(true);

        ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(context.getString(R.string.loading));
        pDialog.setCancelable(false);


        passwordDialog = new Dialog(context);
        passwordDialog.setCancelable(true);
        passwordDialog.setCanceledOnTouchOutside(true);
        passwordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        passwordDialog.setContentView(R.layout.single_password_conform_dialog);
        passwordDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Typeface ClanaproNarrNews = Typeface.createFromAsset(getContext().getAssets(), "fonts/ClanPro-NarrNews.otf");
        Typeface ClanaproNarrMedium = Typeface.createFromAsset(getContext().getAssets(), "fonts/ClanPro-NarrMedium.otf");
        TextView tv_verify_msg, tv_oldpass;

        tv_verify_msg = (TextView) passwordDialog.findViewById(R.id.tv_verify_msg);
        tv_verify_msg.setTypeface(ClanaproNarrNews);

        tv_oldpass = (TextView) passwordDialog.findViewById(R.id.tv_oldpass);
        tv_oldpass.setTypeface(ClanaproNarrNews);

        et_oldpass = (EditText) passwordDialog.findViewById(R.id.et_oldpass);
        tv_oldpass.setTypeface(ClanaproNarrNews);

        tv_confirm = (TextView) passwordDialog.findViewById(R.id.tv_confirm);
        tv_confirm.setTypeface(ClanaproNarrMedium);
        view_ = passwordDialog.findViewById(R.id.view_);

        if (from.equals(from_pass)) {
            tv_verify_msg.setText(context.getResources().getString(R.string.enter_old_pass));
            tv_oldpass.setText(context.getResources().getString(R.string.old_pas));
            tv_confirm.setText(context.getResources().getString(R.string.ok));
            et_oldpass.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else if (from.equals(from_signup)) {
            et_oldpass.setVisibility(View.GONE);
            tv_verify_msg.setText(context.getResources().getString(R.string.message));
            tv_oldpass.setText(context.getResources().getString(R.string.signup_success_msg));
            tv_confirm.setText(context.getResources().getString(R.string.ok));
            view_.setVisibility(View.GONE);

        }
        else if (from.equals(from_rcvr)) {
            et_oldpass.setVisibility(View.GONE);
            tv_verify_msg.setText(context.getResources().getString(R.string.message));
            tv_oldpass.setText(context.getResources().getString(R.string.collect_cash_from_rcvr));
            tv_oldpass.setTextSize(20);
            tv_confirm.setText(context.getResources().getString(R.string.ok));
            view_.setVisibility(View.GONE);

        }

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (from.equals(from_pass)) {
                    /*if(et_oldpass.getText().toString().matches(from))
                        Utility.BlueToast(context,context.getResources().getString(R.string.pass_mismatch));
                    else*/
                    OldPasswordVerify();
                } else  {
                    refreshProfile.onRefresh();
                }
            }
        });

    }
    /**********************************************************************************************/
    @Override
    public void show() {
        super.show();
        passwordDialog.show();
    }
    @Override
    public void dismiss() {
        super.dismiss();
        passwordDialog.dismiss();
    }

    /**********************************************************************************************/
    private void OldPasswordVerify() {
        String oldpass = et_oldpass.getText().toString();

        if (!oldpass.matches("")) {

            if(oldpass.equals(from_pass)) {
                Utility.BlueToast(context,context.getResources().getString(R.string.try_diff_pass));
            } else if (oldpass.equals(preferenceHelperDataSource.getPassword())) {
                refreshProfile.onRefresh();
                VariableConstant.EDIT_PASSWORD_DIALOG = true;
                ChangePasswordDialog.this.dismiss();
            } else {
                Utility.BlueToast(context, context.getResources().getString(R.string.invalid_pass));
            }

        } else {
            Utility.BlueToast(context, context.getResources().getString(R.string.err_pass));
        }

    }


    /**
     * <p>Interface to close the dialog and refresh the MyProfile details after change the name</p>
     */
    public interface RefreshProfile {
        void onRefresh();
    }

}
