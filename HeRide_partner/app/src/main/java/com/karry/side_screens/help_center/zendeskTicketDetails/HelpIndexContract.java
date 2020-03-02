package com.karry.side_screens.help_center.zendeskTicketDetails;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.karry.BasePresenter;
import com.karry.BaseView;
import com.karry.side_screens.help_center.zendeskpojo.ZendeskDataEvent;

import java.util.ArrayList;


/**
 * <h>HelpIndexContract</h>
 * Created by Ali on 2/26/2018.
 */

public interface HelpIndexContract
{
    interface presenter extends BasePresenter
    {

        void onPriorityImage(Context helpIndexTicketDetails, String priority, ImageView ivHelpCenterPriorityPre);

        void callApiToCommentOnTicket(String trim, int zenId);

        void callApiToCreateTicket(String trim, String subject, String priority, Activity activity);

        void callApiToGetTicketInfo(int zenId);


    }
    interface HelpView extends BaseView
    {

        void onTicketInfoSuccess(ArrayList<ZendeskDataEvent> events, String timeToSet, String subject, String priority, String type);

        void onZendeskTicketAdded(String response);
        void onError(String errMsg);

        void success();
    }
}
