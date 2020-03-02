package com.karry.side_screens.help_center.zendeskHelpIndex;


import com.karry.BasePresenter;
import com.karry.BaseView;
import com.karry.side_screens.help_center.zendeskpojo.OpenClose;

/**
 * <h>ZendeskHelpIndexContract</h>
 * Created by Ali on 2/26/2018.
 */

public interface ZendeskHelpIndexContract
{
    interface Presenter extends BasePresenter
    {
        void onToGetZendeskTicket();


    }
    interface  ZendeskView extends BaseView
    {
        void onGetTicketSuccess();

        void onEmptyTicket();

        void onTicketStatus(OpenClose openClose, int openCloseSize, boolean isOpenClose);

        void onNotifyData();
        void onError(String error);

        void hideChat();
    }
}
