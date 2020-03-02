package com.karry.side_screens.bankDetails;

import android.util.Log;

import com.karry.utility.OkHttp3Connection;
import com.karry.utility.ServiceUrl;

import org.json.JSONObject;

/**
 * Created by murashid on 29-Aug-17.
 */

public class BankBottomSheetModel {

    private static final String TAG = "BankBottomSheet";

    private BankBottomSheetModelImplem bankBottomSheetModelImplem;
    BankBottomSheetModel(BankBottomSheetModelImplem bankBottomSheetModelImplem)
    {
        this.bankBottomSheetModelImplem = bankBottomSheetModelImplem;
    }

    void setDefaultAccount(String token,JSONObject jsonObject)
    {
        OkHttp3Connection.doOkHttp3Connection(token, ServiceUrl.SET_DEFAULT_BANK, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    if (result!=null)
                    {
                        Log.d(TAG, "onSuccess: "+result);
                        JSONObject jsonObject  = new JSONObject(result);
                        if(jsonObject.getString("errFlag").equals("0"))
                        {
                            bankBottomSheetModelImplem.onSuccess(jsonObject.getString("errMsg"));
                        }
                        else
                        {
                            bankBottomSheetModelImplem.onFailure(jsonObject.getString("errMsg"));
                        }
                    }
                    else
                    {
                        bankBottomSheetModelImplem.onFailure();
                    }
                }
                catch (Exception e)
                {
                    bankBottomSheetModelImplem.onFailure();
                }
            }

            @Override
            public void onError(String error)
            {
                bankBottomSheetModelImplem.onFailure();
            }
        },token);
    }

    void deleteBankAccount(String token,JSONObject jsonObject)
    {
        OkHttp3Connection.doOkHttp3Connection(token, ServiceUrl.ADD_DELETE_BANK_DETAILS, OkHttp3Connection.Request_type.DELETE, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    if (result!=null)
                    {
                        Log.d(TAG, "onSuccess: "+result);
                        JSONObject jsonObject  = new JSONObject(result);
                        if(jsonObject.getString("errFlag").equals("0"))
                        {
                            bankBottomSheetModelImplem.onSuccess(jsonObject.getString("errMsg"));
                        }
                        else
                        {
                            bankBottomSheetModelImplem.onFailure(jsonObject.getString("errMsg"));
                        }
                    }
                    else
                    {
                        bankBottomSheetModelImplem.onFailure();
                    }
                }
                catch (Exception e)
                {
                    bankBottomSheetModelImplem.onFailure();
                }
            }

            @Override
            public void onError(String error)
            {
                bankBottomSheetModelImplem.onFailure();
            }
        },token);
    }



    interface BankBottomSheetModelImplem
    {
        void onFailure(String msg);
        void onFailure();
        void onSuccess(String msg);
    }
}
