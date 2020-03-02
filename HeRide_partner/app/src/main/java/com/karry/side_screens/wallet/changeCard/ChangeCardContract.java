package com.karry.side_screens.wallet.changeCard;


import com.karry.side_screens.wallet.changeCard.model.CardInfoModel;

import java.util.List;

/**
 * <h1>ChangeCardContract</h1>
 * This is link for view and presenter
 * @author 3Embed
 * @since on 2/12/2018.
 */
public interface ChangeCardContract
{
    interface View
    {
        /**
         * <h>showProgressDialog</h>
         * <p>this method is using to show the Progress Dialog</p>
         */
        void showProgressDialog();

        /**
         * <h>Dismiss Progress Dialog</h>
         * <p>this method is using to dismiss the Progress Dialog</p>
         */
        void dismissProgressDialog();

        /**
         * <h>Internet Error Display</h>
         * <p>this method is using to display Internet connection Error</p>
         */
        void internetError();

        /**
         * <h>Bad GAteWay Error</h>
         * <p>this method is using to Display badGateWay Error to user</p>
         */
        void badGateWayError();

        /**
         * <h>Error response</h>
         * <p>this method is using to send back the Error response to UI</p>
         */
        void errorResponse();

        /**
         * <h>Clear List</h>
         * <p>this method is using to clear the ArrayList</p>
         */
        void clearRowItem();

        /**
         * <h>Send Back API's Success Response</h>
         * <p>this method is using to send successfull response and setting response to Arraylist </p>
         * @param item parameter contains the response object
         */
        void responseItem(CardInfoModel item);

        /**
         * <h2>OnClickOfDeleteButton</h2>
         * This method is triggered when delete icon is clicked
         */
        void onClickOfDelete(int position);

        /**
         * <h2>onClickOfItem</h2>
         * This method is triggered when item layout is clicked
         */
        void onClickOfItem(int position);

        /**
         * <h2>deleteItemData</h2>
         * This method is triggered when item layout is to be deleted
         */
        void deleteItemData(int position);
    }

    interface Presenter
    {
        /**
         * <h2>getAllCards</h2>
         * This method is used to get all cards
         */
        void getAllCards();


        void makeCardDefault(CardInfoModel cardInfoModel);

        /**
         * <h2>deleteCard</h2>
         * used to delete card
         * @param cardId card id to be deleted
         * @param position position of card
         */
        void deleteCard(String cardId, int position, List<CardInfoModel> cardList);
    }
}