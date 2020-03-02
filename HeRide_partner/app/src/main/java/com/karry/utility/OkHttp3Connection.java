package com.karry.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by ankur on 10/6/16.
 */
public class OkHttp3Connection {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * <h2>doOkhttpRequest</h2>
     * <p>
     * This method receive all the data and Store then into to single
     * array of class
     * Service Call using okHttp Request.
     * </p>
     * <p>
     * this Method Take a Request Body and a url,and OkHttpRequestCallback and does a Asyntask,
     * and does a request to the given Url
     * </p>
     *
     * @param request_Url contains the url of the given Service link to do performance.
     * @param requestBody contains the require data to send the given Url link.
     * @param callbacks   contains the reference to set the call back response to the calling class.
     */
    public static void doOkHttp3Connection(String request_Url, Request_type request_type, JSONObject requestBody, OkHttp3RequestCallback callbacks, String token) {
        OkHttpRequestData data = new OkHttpRequestData();
        data.request_Url = request_Url;
        data.requestBody = requestBody;
        data.callbacks = callbacks;
        data.request_type = request_type;
        data.token = token;
        /**
         * Calling the Async task to perform the Service call.*/
        new OkHttpRequest().execute(data);
    }

    public static void doOkHttp3Connection(String auth, String request_Url, Request_type request_type,
                                           JSONObject requestBody, OkHttp3RequestCallback callbacks, String token) {
        OkHttpRequestData data = new OkHttpRequestData();
        data.request_Url = request_Url;
        data.requestBody = requestBody;
        data.callbacks = callbacks;
        data.request_type = request_type;
        data.token = token;
        data.auth = auth;
        /**
         * Calling the Async task to perform the Service call.*/
        new OkHttpRequest().execute(data);
    }

    /**
     * <h2></h2>
     */
    private static String getUrl(String url, JSONObject jsonObject) {
        String service_url = url + "?";
        String query = "";
        Iterator<String> object_keys = jsonObject.keys();
        try {
            while (object_keys.hasNext()) {
                String keys_value = object_keys.next();
                query = query + keys_value + "=" + jsonObject.getString(keys_value) + "&";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Vallue", service_url + query);

        return service_url + query;
    }

    public static void printLog(String message) {
        Log.d("OKHTTPCONNECTION", message);
    }

    /**
     * <H3>Request_type</H3>
     * <p>
     * <p>
     * </p>
     */
    public enum Request_type {
        GET("getRequest"),
        URl("urlRequest"),
        POST("postRequest"),
        PUT("putRequest"),
        DELETE("deleteRequest");

        public String value;

        Request_type(String value) {
            this.value = value;
        }
    }

    /**
     * interface for Session Call back request
     */
    public interface OkHttp3RequestCallback {
        /**
         * Called When Success result of JSON request
         *
         * @param result
         */
        void onSuccess(String result);

        /**
         * Called When Error result of JSON request
         *
         * @param error
         */
        void onError(String error);

    }

    /**
     * <h1>OkHttpRequestData</h1>
     * <p>
     * Class is use to hold three parameter i.e String object,RequestBody object and OkHttpRequestCallback
     * in a single place.
     * Because async Task takes only single parameter nad i have to send three parameter so.
     * Wrapping three things into a single object and sending one object to async task.
     * </p>
     *
     * @see RequestBody
     */
    private static class OkHttpRequestData {
        public String request_Url, token, auth = "";
        public JSONObject requestBody;
        OkHttp3RequestCallback callbacks;
        Request_type request_type;
    }

    /**
     * <h1>OkHttpRequest</h1>
     * OkHttpRequest extends async task to perform the function indecently .
     * Does a service call using OkHttp client.
     * <p>
     * This class extends async task and override the method of async task .
     * on doInBackground method of async task.
     * performing a service call to th given url and sending data given to the class.
     * By the help of the OkHttpClient and sending the call back method to the calling Activity by setting
     * data to the given reference of call-Back Interface object.
     * </P>
     * If Any thing Happened to the service call like Connection Failed or any thin else.
     * Telling to the User that connection is too slow when handling Exception.
     *
     * @see Response
     * @see OkHttpClient
     */
    private static class OkHttpRequest extends AsyncTask<OkHttpRequestData, Void, String> {
        OkHttp3RequestCallback callbacks;
        boolean error = false;
        //String credential = Credentials.basic(" ","3embedMobifyixxqtwxAppscrip");
        String credential = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(final OkHttpRequestData... params) {
            callbacks = params[0].callbacks;
            String result = "";

            if (!params[0].auth.isEmpty()) {
                credential = params[0].auth;
            }

            try {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();/*.authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        System.out.println("Authenticating for response: " + response);
                        System.out.println("Challenges: " + response.challenges());
                        String credential = Credentials.basic("Embed","3embedMobifyixxqtwxAppscrip");
                               // .concat(VariableConstant.USER_TYPE+"").concat(params[0].token);
                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                //.header("Authorization", Credentials.basic(VariableConstant.USER_TYPE+"",params[0].token))
                                .build();
                    }
                });*/
                builder.connectTimeout(30, TimeUnit.SECONDS);
                builder.readTimeout(30, TimeUnit.SECONDS);
                builder.writeTimeout(30, TimeUnit.SECONDS);
                OkHttpClient httpClient = builder.build();

                Request request = null;
                if (params[0].request_type.equals(Request_type.URl)) {
                    String url = getUrl(params[0].request_Url, params[0].requestBody);
                    request = new Request.Builder()
                            .url(url)
                            .header("authorization", credential)
                            .addHeader("token", params[0].token)
                            .addHeader("userType", VariableConstant.USER_TYPE + "")
                            .build();
                    Log.e("Request Header", credential);
                    Log.e("Request Body", url);

                } else if (params[0].request_type.equals(Request_type.POST)) {
                    RequestBody body = RequestBody.create(JSON, params[0].requestBody.toString());

                    request = new Request.Builder()
                            .url(params[0].request_Url)
                            .addHeader("authorization", credential)
                            .addHeader("token", params[0].token)
                            .addHeader("userType", VariableConstant.USER_TYPE + "")
//                            .addHeader("Content-Type", "text/json; Charset=UTF-8")
                            .post(body)
                            .build();
                    final Buffer buffer = new Buffer();
                    try {
                        request.body().writeTo(buffer);
                        buffer.close();
                        Log.e("Request Header", credential);
                        Log.e("Request Body", buffer.readUtf8());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (params[0].request_type.equals(Request_type.GET)) {


                    request = new Request.Builder()
                            .url(params[0].request_Url)
                            .addHeader("authorization", credential)
                            .addHeader("token", params[0].token)
                            .addHeader("userType", VariableConstant.USER_TYPE + "")
//                            .addHeader("Content-Type", "text/json; Charset=UTF-8")
                            .get()
                            .build();
                    final Buffer buffer = new Buffer();

                    Log.e("Request Header", credential);
                    Log.e("Request Url", params[0].request_Url);

                } else if (params[0].request_type.equals(Request_type.PUT)) {
                    RequestBody body = RequestBody.create(JSON, params[0].requestBody.toString());

                    request = new Request.Builder()
                            .url(params[0].request_Url)
                            .header("authorization", credential)
                            .addHeader("token", params[0].token)
                            .addHeader("userType", VariableConstant.USER_TYPE + "")
                            .addHeader("Content-Type", "text/json; Charset=UTF-8")
                            .put(body)
                            .build();
                    final Buffer buffer = new Buffer();
                    try {
                        request.body().writeTo(buffer);
                        buffer.close();
                        Log.e("Request Header", credential);
                        Log.e("Request Body", buffer.readUtf8());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (params[0].request_type.equals(Request_type.DELETE)) {
                    RequestBody body = RequestBody.create(JSON, params[0].requestBody.toString());

                    request = new Request.Builder()
                            .url(params[0].request_Url)
                            .header("authorization", credential)
                            .addHeader("token", params[0].token)
                            .addHeader("userType", VariableConstant.USER_TYPE + "")
                            .addHeader("Content-Type", "text/json; Charset=UTF-8")
                            .delete(body)
                            .build();
                    final Buffer buffer = new Buffer();
                    try {
                        request.body().writeTo(buffer);
                        buffer.close();
                        Log.e("Request Header", credential);
                        Log.e("Request Body", buffer.readUtf8());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    RequestBody body = RequestBody.create(JSON, params[0].requestBody.toString());
                    request = new Request.Builder()
                            .url(params[0].request_Url)
                            .header("authorization", credential)
                            .addHeader("token", params[0].token)
                            .addHeader("userType", VariableConstant.USER_TYPE + "")
                            .addHeader("Content-Type", "text/json; Charset=UTF-8")
                            .put(body)
                            .get()
                            .build();
                    Log.e("Request Header", credential);
                    Log.e("Request Body", params[0].request_Url);
                }
                Response response = httpClient.newCall(request).execute();
                result = response.body().string();
            } catch (UnsupportedEncodingException e) {

                error = true;
                OkHttp3Connection.printLog("UnsupportedEncodingException" + e.toString());
                result = "Connection Failed..Retry !";
                e.printStackTrace();
            } catch (IOException e) {

                error = true;
                OkHttp3Connection.printLog("Read IO exception" + e.toString());
                result = "Connection is too slow...Retry!";
                e.printStackTrace();
            } catch (Exception e) {
                error = true;
                printLog("Read Exception" + e.toString());
                result = "Could not connect to the server.";
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!error) {
                callbacks.onSuccess(result);
            } else {
                callbacks.onError(result);
            }
        }
    }
}
/*how to use :

JSONObject jsonObject=new JSONObject();

            try
            {
                jsonObject.put("ent_dev_id", Utility.getDeviceId(mactivity));
                jsonObject.put("ent_sess_token", session.getSessionToken());
                jsonObject.put("ent_fbid", session.getFbId());
                jsonObject.put("ent_user_fbid",last_swap_details.getFbId());
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            OkHttp3Connection.doOkHttp3Connection(ServiceUrl.REWIND, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback()
            {
                @Override
                public void onSuccess(String result)
                {
                    
                }
                @Override
                public void onError(String error)
                {

                }
            });*/
