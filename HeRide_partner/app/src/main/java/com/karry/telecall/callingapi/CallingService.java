package com.karry.telecall.callingapi;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * <h1>CallingService</h1>
 * This class is used to provide the Observable<Response<ResponseBody>>
 * @author 3Embed
 * @since on 15-03-2019.
 */
public interface CallingService
{


    /************************************IN APP CALLING********************************************/

    @FormUrlEncoded
    @POST("/call")
    Observable<Response<ResponseBody>> initCall(@Header("authorization") String auth, @Header("lan") String lang, @Field("type") String type, @Field("room") String room, @Field("to") String to);
    @GET("/call/{callId}")
    Observable<Response<ResponseBody>> checkIsAvailable(@Header("authorization") String auth, @Header("lan") String lang, @Path("callId") String callId);

    @PUT("/call/{callId}")
    Observable<Response<ResponseBody>> callAnswer(@Header("authorization") String auth, @Header("lan") String lang, @Path("callId") String callId);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/call",hasBody = true)
    Observable<Response<ResponseBody>> endCall(@Header("authorization") String auth, @Header("lan") String lang, @Field("callId") String callId, @Field("callFrom") String callFrom);
}
