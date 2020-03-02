package com.karry.network;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.utility.AppTypeFace;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>FareBreakdownDialog</h1>
 * Used to show the fare estimate breakdown
 * @author 3Embed
 * @since on 2/19/2018.
 */
public class NetworkErrorDialog extends Dialog
{
    private AppTypeFace appTypeface;

    @BindView(R.id.tv_no_internet_title) TextView tv_no_internet_title;

    public NetworkErrorDialog(@NonNull Context context, AppTypeFace appTypeface)
    {
        super(context, R.style.FullScreenDialogStyle);
        this.appTypeface = appTypeface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.layout_no_network_alert);
        initialize();
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize
     */
    private void initialize()
    {
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
        tv_no_internet_title.setTypeface(appTypeface.getPro_narMedium());
    }
}

