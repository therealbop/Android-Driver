package com.karry.twilio_call;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TwilioCallNetworkService
{
    @POST("getcapability")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> makeTwillioCall(@Field("phoneNumber") String phoneNumber);
}
