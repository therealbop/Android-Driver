package com.karry.network;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by embed on 3/2/18.
 */

public interface CarApiService
{
    @GET("0.3/?callback=?&cmd=getYears")
    Observable<Response<ResponseBody>> year ();

    @GET("0.3/?callback=?&cmd=getMakes&year=?")
    Observable<Response<ResponseBody>> vehicleMake (@Query("year") String year);

    @GET("0.3/?callback=?&cmd=getModels&make=?&year=?")
    Observable<Response<ResponseBody>> vehicleModel (@Query("year") String year,
                                                    @Query("make") String make);

}
