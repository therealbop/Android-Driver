package com.karry.bookingFlow.model;

import java.util.ArrayList;

/**
 * <h1>FavAddressDataPOJO</h1>
 * This class is used to parse the data for the fav address
 * @author 3Embed
 * @since on 28/07/17.
 */

public class FavAddressDataPOJO
{
    private String message;

    private ArrayList<FavAddressDataModel> data;

    public ArrayList<FavAddressDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<FavAddressDataModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }
}
