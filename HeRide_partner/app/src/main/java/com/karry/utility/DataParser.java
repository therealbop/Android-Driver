package com.karry.utility;

import android.location.Address;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h21>DataParser</h1>
 * This class is used to parse the data
 * @author  3Embed
 * @since on 18-12-2017.
 */

public class DataParser
{
    /**
     * <h2>fetchErrorMessage</h2>
     * This method is used to fetch the error response message from json
     * @param jsonStringResponse Json response to be parsed
     * @return returns the error body message
     */
    public static String fetchErrorMessage(Response<ResponseBody> jsonStringResponse)
    {
        try
        {
            String responseToBeParsed = jsonStringResponse.errorBody().string();
            JSONObject jsonResponse = new JSONObject(responseToBeParsed);
            return jsonResponse.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <h2>fetchSuccessMessage</h2>
     * This method is used to fetch the success message from json
     * @param jsonStringResponse Json response to be parsed
     * @return returns the success response body message
     */
    public static String fetchSuccessMessage(Response<ResponseBody> jsonStringResponse)
    {
        try
        {
            String responseToBeParsed = jsonStringResponse.body().string();
            JSONObject jsonResponse = new JSONObject(responseToBeParsed);
            return jsonResponse.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <h2>fetchSidFromData</h2>
     * This method is used to parse the sid from response
     * @param jsonStringResponse Json response
     * @return String response for sid
     */
    public static String fetchSidFromData(Response<ResponseBody> jsonStringResponse)
    {
        try {
            String responseToBeParsed = jsonStringResponse.body().string();
            JSONObject jsonResponse = new JSONObject(responseToBeParsed);
            JSONObject dataObject= jsonResponse.getJSONObject("data");
            return dataObject.getString("sid");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <h2>fetchIDFromData</h2>
     * This method is used to parse the id from response
     * @param jsonStringResponse Json response
     * @return String response for id
     */
    public static String fetchIDFromData(Response<ResponseBody> jsonStringResponse)
    {
        try {
            String responseToBeParsed = jsonStringResponse.body().string();
            JSONObject jsonResponse = new JSONObject(responseToBeParsed);
            JSONObject dataObject= jsonResponse.getJSONObject("data");
            return dataObject.getString("id");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * <h2>fetchDataString</h2>
     * This method is used to fetch the data from response
     * @param jsonStringResponse response from API
     * @return returns the data string
     */
    public static String fetchDataString(Response<ResponseBody> jsonStringResponse)
    {
        JSONObject object;
        try
        {
            object = new JSONObject(jsonStringResponse.body().string());
            return object.getJSONObject("data").toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * <h2>fetchDataObjectString</h2>
     * This method is used to fetch the data object from response
     * @param jsonStringResponse response from API
     * @return returns the data string
     */
    public static String fetchDataObjectString(Response<ResponseBody> jsonStringResponse)
    {
        JSONObject object;
        try
        {
            String response = jsonStringResponse.body().string();
            Utility.printLog("data string "+response);
            object = new JSONObject(response);
            return object.getJSONObject("data").toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <h1>stringToJsonAndPublish</h1>
     * <p>
     *    This method is used to convert our string into json file and then publish on amazon.
     *    </p>
     * @param fileName contains the name of file.
     * @param uri contains the uri.
     * @return the json object.
     */
    public static JSONObject stringToJsonAndPublish(String fileName, Uri uri) {
        JSONObject message = new JSONObject();
        try {
            message.put("type", "image");
            message.put("filename", fileName);
            message.put("uri", uri.toString());
            message.put("uploaded", "inprocess");
            message.put("confirm", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * <h2>getStringAddress</h2>
     * This method is used to get the address string from address object
     * @param address adress object
     * @return returns the address string
     */
    public static String getStringAddress(Address address)
    {
        String addressString = null;
        if(address.getAddressLine(0)!=null)
            addressString = address.getAddressLine(0);
        if (address.getAddressLine(1)!= null)
            addressString = addressString+","+address.getAddressLine(1);
        if (address.getAddressLine(2)!= null)
            addressString = addressString+","+address.getAddressLine(2);
        if (address.getAddressLine(3)!= null)
            addressString = addressString+","+address.getAddressLine(3);
        if (address.getAddressLine(4)!= null)
            addressString = addressString+","+address.getAddressLine(4);
        if (address.getAddressLine(5)!= null)
            addressString = addressString+","+address.getAddressLine(5);
        if (address.getAddressLine(6)!= null)
            addressString = addressString+","+address.getAddressLine(6);
        return addressString;
    }



}
