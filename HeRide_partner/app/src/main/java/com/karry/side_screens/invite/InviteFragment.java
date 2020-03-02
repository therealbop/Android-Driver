package com.karry.side_screens.invite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karry.app.mainActivity.MainActivity;
import com.karry.dagger.ActivityScoped;
import com.heride.partner.R;
import com.karry.utility.AppTypeFace;
import com.karry.utility.ServiceUrl;
import com.facebook.share.widget.ShareDialog;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;


/**
 * <h1>InviteFragment</h1>
 * <p>the is the class for Invite Fragment</p>
 */
@ActivityScoped
public class InviteFragment extends DaggerFragment implements InviteFragmentContract.InviteFragmentView{

    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.tvShareText) TextView tvShareText;
    @BindView(R.id.tvFacebook) TextView tvFacebook;
    @BindView(R.id.tvTwitter) TextView tvTwitter;
    @BindView(R.id.tvMail) TextView tvMail;
    @BindView(R.id.tvMessage) TextView tvMessage;
    @BindView(R.id.tvReferral) TextView tvReferral;
    @BindView(R.id.ivFacebook) ImageView ivFacebook;
    @BindView(R.id.ivTwitter) ImageView ivTwitter;
    @BindView(R.id.ivMail) ImageView ivMail;
    @BindView(R.id.ivMessage) ImageView ivMessage;

    @BindString(R.string.link1) String inviteMessage;
    @BindString(R.string.link2) String inviteMessage2;
    @BindString(R.string.link3) String inviteMessage3;
    @BindString(R.string.referral_text) String referral_text;
    @BindString(R.string.app_name) String app_name;

    @Inject AppTypeFace appTypeface;
    @Inject InviteFragmentContract.InviteFragPresenter inviteFragPresenter;

    private Unbinder unbinder;
    private ShareDialog shareDialog;


    @Inject
    public InviteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.invite_fragment, container, false);

        unbinder = ButterKnife.bind(this,rootView);

        shareDialog=new ShareDialog(getActivity());

        initLayoutID();
        ((MainActivity)getActivity()).setFragmentRefreshListener(this::onResume);
        return rootView;
    }

    /**
     * <h1>initLayoutID</h1>
     * <p>the method for initilize the view.</p>
     */
    void initLayoutID() {

        tvHeader.setTypeface(appTypeface.getPro_News());
        tvReferral.setTypeface(appTypeface.getPro_News());
        tvShareText.setTypeface(appTypeface.getPro_News());
        tvFacebook.setTypeface(appTypeface.getPro_News());
        tvTwitter.setTypeface(appTypeface.getPro_News());
        tvMail.setTypeface(appTypeface.getPro_News());
        tvMessage.setTypeface(appTypeface.getPro_News());

        tvReferral.setText(inviteFragPresenter.getInviteCode());
        tvHeader.setText(referral_text.concat(" ").concat(app_name));
    }

    @OnClick({R.id.ivFacebook,R.id.ivTwitter,R.id.ivMail,R.id.ivMessage})
    public void onClick(View view){

        switch (view.getId())
        {
            case R.id.ivFacebook:
                inviteViaFacebook();
                break;

            case R.id.ivTwitter:
                inviteViaTwitter();
                break;

            case R.id.ivMail:
                inviteViaEmail();
                break;

            case R.id.ivMessage:
                inviteViaMessage();
                break;
        }
    }

    private void inviteViaTwitter() {
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ServiceUrl.TWITTER_URL));

        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ServiceUrl.TWITTER_URL));
        }
        startActivity(intent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void inviteViaMessage() {
        String smsBody = inviteMessage+" "+inviteFragPresenter.getInviteCode()+" "+inviteMessage2+" "+inviteMessage3;
        Intent sms=new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"));
        sms.putExtra("sms_body",smsBody);
        startActivity(sms);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void inviteViaEmail() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:" + ""));
        i.putExtra(Intent.EXTRA_SUBJECT, "Invite:" + app_name);
        i.putExtra(Intent.EXTRA_TEXT, inviteMessage+" "+inviteFragPresenter.getInviteCode()+" "+inviteMessage2+" "+inviteMessage3);
        startActivity(Intent.createChooser(i, "Send email"));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void inviteViaFacebook() {

        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Cybertaxi-466475137455266/"));

        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Cybertaxi-466475137455266/"));
        }
        startActivity(intent);

        /*if (Utility.isNetworkAvailable(getActivity())) {

            if (ShareDialog.canShow(ShareLinkContent.class))
            {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(app_name)
                        .setContentDescription(inviteMessage+" "+inviteFragPresenter.getInviteCode()+" "+inviteMessage2+" "+inviteMessage3)
                        .setContentUrl(Uri.parse("https://www.facebook.com/Cybertaxi-466475137455266/"))
                        .build();

                shareDialog.show(linkContent);
            }
        }*/

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inviteFragPresenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        inviteFragPresenter.attachView(this);
    }
}
