package com.karry.side_screens.wallet.changeCard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>GetCardsPOJO</h1>
 * This class is used to hold the card details
 * @author embed
 * @since on 25/11/15.
 */
public class GetCardsPOJO implements Serializable
{

    @SerializedName("cards")
    @Expose
    private CardDetailsDataModel[] cards;

    public CardDetailsDataModel[] getCards() {
        return cards;
    }
}
