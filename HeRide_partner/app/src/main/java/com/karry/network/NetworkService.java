package com.karry.network;

import com.karry.service.LatLngBody;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by DELL on 27-12-2017.
 */

public interface NetworkService {

    @POST("master/signIn")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> signIn(@Header("lan") String lan ,
                                              @Field("mobileOrEmail") String mobile,
                                              @Field("password") String password,
                                              @Field("deviceType")int deviceType,
                                              @Field("deviceId") String deviceId,
                                              @Field("appVersion") String version,
                                              @Field("deviceMake") String deviceMake,
                                              @Field("deviceModel")String deviceModel,
                                              @Field("deviceOsVersion")String deviceOs,
                                              @Field("batteryPercentage")Double deviceBatary,
                                              @Field("locationHeading")String devicelocation,
                                              @Field("deviceTime")String deviceTime);

    @POST("master/signInWithVehicle")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> signInWithVehicle(@Header("lan") String lan ,
                                                         @Field("mobileOrEmail") String mobile,
                                                         @Field("password") String password,
                                                         @Field("vehicleId") String vehicleId,
                                                         @Field("deviceType")int deviceType,
                                                         @Field("deviceId") String deviceId,
                                                         @Field("appVersion") String version,
                                                         @Field("deviceMake") String deviceMake,
                                                         @Field("deviceModel")String deviceModel,
                                                         @Field("deviceOsVersion")String deviceOs,
                                                         @Field("batteryPercentage")Double deviceBatary,
                                                         @Field("locationHeading")String devicelocation,
                                                         @Field("deviceTime")String deviceTime);

    @POST("master/forgotPassword")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> forgotPassword(@Header("lan")String language,
                                                      @Field("countryCode")String countryCode,
                                                      @Field("emailOrPhone")String emailOrMobile,
                                                      @Field("type")int verifyType);


    @POST("master/resendOtp")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> sendOtp (@Header("lan")String language,
                                                @Field("userId") String userId,
                                                @Field("trigger")int trigger);

    @POST("master/verifyVerificationCode")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> verifyOtp (@Header("lan")String language,
                                                  @Field("code") String otp,
                                                  @Field("userId")String userId,
                                                  @Field("trigger")int trigger);

    @PATCH("master/password")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> password (@Header("lan")String language,
                                                 @Field("password") String password,
                                                 @Field("userId") String userId);

    /****************************************SignUp Personal***********/

    @POST("master/phoneNumberValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> phoneNumberValidation (@Header("lan")String language,
                                                              @Field("countryCode") String countryCode,
                                                              @Field("mobile") String mobile);

    @POST("master/verifyPhoneNumber")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> verifyPhoneNumber (@Header("lan")String language,
                                                          @Field("code") String otp,
                                                          @Field("providerId") String driverId);

    @POST("master/emailValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> emailValidation (@Header("lan")String language,
                                                        @Field("email") String email);

    @POST("master/referralCodeValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> referralCodeValidation (@Header("lan")String language,
                                                               @Field("code") String email,
                                                               @Field("lat") Double lat,
                                                               @Field("long") Double lng);

    @POST("master/platNumberValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> platNumberValidation (@Header("lan")String language,
                                                             @Field("plateNo") String plateNo);


    @GET("customer/languages")
    Observable<Response<ResponseBody>> getLanguages ();

    /*@GET("allCalims")
    Observable<Response<ResponseBody>> checkInternet();*/

    @GET("master/state")
    Observable<Response<ResponseBody>> state (@Header("lan")String language);

    @GET
    Observable<Response<ResponseBody>> checkInternet(@Url String url);

    @GET("master/city/{state}")
    Observable<Response<ResponseBody>> city (@Header("lan")String language,
                                             @Path("state")String state);

    @GET("master/vehicleTypeAndSpecialities/{cityId}")
    Observable<Response<ResponseBody>> vehicleTypeAndSpecialities (@Header("lan")String language,
                                                                   @Path("cityId")String cityId);
    @GET("master/preferences/{cityId}/{typeId}/{serviceType}")
    Observable<Response<ResponseBody>> preferences (@Header("lan")String language,
                                                    @Path("cityId")String cityId,
                                                    @Path("typeId")String typeId,
                                                    @Path("serviceType")Integer serviceType);

    @GET("master/makeModel")
    Observable<Response<ResponseBody>> makeModel (@Header("lan")String language);



    @POST("master/signUp")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> signUp (@Header("lan")String language,
                                               @Field("firstName") String firstName,
                                               @Field("lastName")String lastName,
                                               @Field("email") String email,
                                               @Field("password") String password,
                                               @Field("countryCode") String countryCode,
                                               @Field("mobile") String mobile,
                                               @Field("cityId") String cityId,
                                               @Field("latitude") double latitude,
                                               @Field("longitude") double longitude,
                                               @Field("profilePic") String profilePic,
                                               @Field("deviceId") String deviceId,
                                               @Field("deviceType") Integer deviceType,
                                               @Field("deviceMake") String deviceMake,
                                               @Field("deviceModel") String deviceModel,
                                               @Field("deviceOsVersion") String deviceOsVersion,
                                               @Field("batteryPercentage") String batteryPercentage,
                                               @Field("locationHeading") String locationHeading,
                                               @Field("appVersion") String appVersion,
                                               @Field("deviceTime") String deviceTime,
                                               @Field("referralCode") String referralCode,
                                               @Field("zipcode") String zipcode,
                                               @Field("services") String services,
                                               @Field("platNo") String platNo,
                                               @Field("vehicleImage") String vehicleImage,
                                               @Field("specialities") String specialities,
                                               @Field("typeId") String typeId,
                                               @Field("makeId") String makeId,
                                               @Field("modelId") String modelId,
                                               @Field("insuranceNumber") String insuranceNumber,
                                               @Field("insuranceYear") String insuranceYear,
                                               @Field("motorInsuImage") String motorInsuImage,
                                               @Field("carriagePermitCert") String carriagePermitCert,
                                               @Field("registrationCert") String registrationCert,
                                               @Field("operatorId") String operatorId,
                                               @Field("policeClearance") String policeClearance,
                                               @Field("inspectionReport") String inspectionReport,
                                               @Field("goodsInTransit") String goodsInTransit,
                                               @Field("workWithChildrenImageUrl") String workWithChildrenImageUrl,
                                               @Field("driverLicenseFront") String driverLicenseFront,
                                               @Field("driverLicenseBack") String driverLicenseBack,
                                               @Field("accountType") double accountType,
                                               @Field("state") String state,
                                               @Field("dateOfBirth") String dateOfBirth,
                                               @Field("vehicleYear") String vehicleYear,
                                               @Field("colour") String color,
                                               @Field("gender") String gender,
                                               @Field("driverLicenseDate") String driverLicenseDate,
                                               @Field("motorInsuImageDate") String motorInsuImageDate,
                                               @Field("registrationDate") String registrationDate,
                                               @Field("policeClearanceDate") String policeClearanceDate,
                                               @Field("inspectionReportDate") String inspectionReportDate,
                                               @Field("goodsInTransitDate") String goodsInTransitDate,
                                               @Field("workWithChildrenDate") String workWithChildrenDate,
                                               @Field("driverPreference") String driverPreference,
                                               @Field("vehiclePreference") String vehiclePreference

    );

    @POST("master/vehicleDefault")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> defaultVehicle (@Header("authorization")String authorization,
                                                       @Header("lan")String language,
                                                       /*@Field("masterId") String maserId,*/
                                                       @Field("vehicleId")String vehicleId
                                                       /*@Field("typeId")String vehicleTypeId,*/
                                                       /*@Field("goodTypes") String goodTypes*/);



    @POST("master/optInVehicleTypes")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> optInVehicleTypes (@Header("authorization")String authorization,
                                                          @Header("lan")String language,
                                                          @Field("vehicleTypesId")String vehicleTypeId);

    @POST("master/signOut")
    Observable<Response<ResponseBody>> logout (@Header("lan") String language,
                                               @Header("authorization") String authorization);

    @GET("master/profile")
    Observable<Response<ResponseBody>> getProfile (@Header("lan") String language,
                                                   @Header("authorization") String authorization);

    @PATCH("master/profile")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateProfile (@Header("lan") String language,
                                                      @Header("authorization") String authorization,
                                                      @Field("firstName") String firstName,
                                                      @Field("lastName")String lastName,
                                                      @Field("profilePic")String profilePic);



    @PATCH("master/phoneNumber")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updatePhoneNumber (@Header("lan") String language,
                                                          @Header("authorization") String authorization,
                                                          @Field("countryCode") String countryCode,
                                                          @Field("phone")String phone);


    @PATCH("master/email")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateEmail (@Header("lan") String language,
                                                    @Header("authorization") String authorization,
                                                    @Field("email") String email);


    @PATCH("master/password/me")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updatePassword (@Header("lan") String language,
                                                       @Header("authorization") String authorization,
                                                       @Field("oldPassword") String oldPassword,
                                                       @Field("newPassword") String newPassword);

    /**********************************************************************************************/
    @GET("master/support")
    Observable<Response<ResponseBody>> support (@Header("authorization")String authorization,
                                                @Header("lan")String language);

    /**********************************************************************************************/


    @GET("connectAccountCountry")
    Observable<Response<ResponseBody>> getConnectAccountCountry (@Header("authorization")String authorization,
                                                                 @Header("lan")String language);


    @GET("connectAccountCurrency/{countryId}")
    Observable<Response<ResponseBody>> getAccountCurrency (@Header("authorization")String authorization,
                                                           @Header("lan")String language,
                                                           @Path("countryId")String countryId);


    @GET("connectAccount")
    Observable<Response<ResponseBody>> getConnectAccount (@Header("authorization")String authorization,
                                                          @Header("lan")String language);

    @POST("connectAccount")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> postConnectAccount (@Header("authorization")String authorization,
                                                           @Header("lan")String language,
                                                           @Field("email")String email,
                                                           @Field("city")String city,
                                                           @Field("country")String country,
                                                           @Field("line1")String line1,
                                                           @Field("postal_code")String postal_code,
                                                           @Field("state")String state,
                                                           @Field("day")String day,
                                                           @Field("month")String month,
                                                           @Field("year")String year,
                                                           @Field("first_name")String first_name,
                                                           @Field("last_name")String last_name,
                                                           @Field("document")String document,
                                                           @Field("personal_id_number")String personal_id_number,
                                                           @Field("date")String date,
                                                           @Field("ip")String ip);

    @POST("externalAccount")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> postExternalAccount (@Header("authorization")String authorization,
                                                            @Header("lan")String language,
                                                            @Field("email")String email,
                                                            @Field("account_number")String account_number,
                                                            @Field("routing_number")String routing_number,
                                                            @Field("account_holder_name")String account_holder_name,
                                                            @Field("country")String country,
                                                            @Field("currency")String currency
    );

    @GET("master/appConfig/{deviceType}")
    Observable<Response<ResponseBody>> getAppConfig(@Header("authorization")String authorization,
                                                    @Header("lan")String language,
                                                    @Path("deviceType") double deviceType);

    @POST("master/status")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> setDriverStatus(@Header("authorization")String authorization,
                                                       @Header("lan")String language,
                                                       @Field("status") int status,
                                                       @Field("latitude") String latitude,
                                                       @Field("longitude") String longitude);



    @POST("master/location")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> location(@Header("authorization")String authorization,
                                                @Header("lan")String language,
                                                @Field("longitude") Double longitude,
                                                @Field("latitude") Double latitude,
                                                @Field("locationHeading") String locationHeading,
                                                @Field("appVersion") String appVersion,
                                                @Field("batteryPer") String batteryPer,
                                                @Field("locationCheck") String locationCheck,
                                                @Field("presenceTime") String presenceTime,
                                                @Field("transit") Double transit,
                                                @Field("bookingId") String bookingId,
                                                @Field("bookingDistance") String bookingDistance);

    @POST("master/locationLogs")
    @Headers( "Content-Type: application/json;charset=UTF-8")
    Observable<Response<ResponseBody>> locationLogs(@Header("authorization")String authorization,
                                                    @Header("lan")String language,
                                                    @Body LatLngBody body);

    @GET("master/bookingDistance/{bookingId}/{latitude}/{longitude}")
    Observable<Response<ResponseBody>> getBookingDistance(@Header("authorization")String authorization,
                                                          @Header("lan")String language,
                                                          @Path("bookingId") String bookingId,
                                                          @Path("latitude") String latitude,
                                                          @Path("longitude") String longitude);

    @POST("master/bookingAck")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> bookingAck(@Header("authorization")String authorization,
                                                  @Header("lan")String language,
                                                  @Field("bookingId") String bookingId);

    @POST("master/respondToRequest")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> respondToRequest(@Header("authorization")String authorization,
                                                        @Header("lan")String language,
                                                        @Field("bookingId") String bookingId,
                                                        @Field("status") int status,
                                                        @Field("lat") Double lat,
                                                        @Field("long") Double lang);

    /**********************************************************************************************/

    @GET("master/areaZone/{latitude}/{longitude}")
    Observable<Response<ResponseBody>> getAreaZone (@Header("authorization")String authorization,
                                                    @Header("lan")String language,
                                                    @Path("latitude")String latitude,
                                                    @Path("longitude")String longitude);

    @GET("master/bookingsAssigned")
    Observable<Response<ResponseBody>> getBookingsAssigned (@Header("authorization")String authorization,
                                                            @Header("lan")String lan);

    /**********************************************************************************************/
    @POST("master/bookingStatusRide")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> bookingStatusRide(@Header("authorization")String authorization,
                                                         @Header("lan")String language,
                                                         @Field("bookingId") String bookingId,
                                                         @Field("status") Integer status,
                                                         @Field("lat") String lat,
                                                         @Field("long") String lng,
                                                         @Field("distance") String distance,
                                                         @Field("dateTime") String dateTime);

    /**********************************************************************************************/
    @POST("master/meterBooking")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> startMeterBooking(@Header("authorization")String authorization,
                                                         @Header("lan")String language,
                                                         @Field("bookingDate") String bookingDate,
                                                         @Field("pickupPlaceId") String pickupPlaceId,
                                                         @Field("pickupPlaceName") String pickupPlaceName,
                                                         @Field("pickupAddress") String pickupAddress,
                                                         @Field("pickupCity") String pickupCity,
                                                         @Field("pickupArea") String pickupArea,
                                                         @Field("pickupState") String pickupState,
                                                         @Field("pickupPostalCode") String pickupPostalCode,
                                                         @Field("pickupCountry") String pickupCountry,
                                                         @Field("pickupLongitude") String pickupLongitude,
                                                         @Field("pickupLatitude") String pickupLatitude,
                                                         @Field("dropPlaceId") String dropPlaceId,
                                                         @Field("dropPlaceName") String dropPlaceName,
                                                         @Field("dropAddress") String dropAddress,
                                                         @Field("dropCity") String dropCity,
                                                         @Field("dropArea") String dropArea,
                                                         @Field("dropState") String dropState,
                                                         @Field("dropPostalCode") String dropPostalCode,
                                                         @Field("dropCountry") String dropCountry,
                                                         @Field("dropLongitude") String dropLongitude,
                                                         @Field("dropLatitude") String dropLatitude);

    @POST("master/dropLocation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> setDropLocation(@Header("authorization")String authorization,
                                                       @Header("lan")String language,
                                                       @Field("bookingId")String bookingId,
                                                       @Field("dropPlaceId")String dropPlaceId,
                                                       @Field("dropPlaceName")String dropPlaceName,
                                                       @Field("dropAddress")String dropAddress,
                                                       @Field("dropCity")String dropCity,
                                                       @Field("dropArea")String dropArea,
                                                       @Field("dropState")String dropState,
                                                       @Field("dropPostalCode")String dropPostalCode,
                                                       @Field("dropCountry")String dropCountry,
                                                       @Field("dropLongitude")String dropLongitude,
                                                       @Field("dropLatitude")String dropLatitude);

    @POST("master/meterBookingComplete")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> completeMeterBooking(@Header("authorization")String authorization,
                                                            @Header("lan")String language,
                                                            @Field("bookingId")String bookingId,
                                                            @Field("lat")String lat,
                                                            @Field("long")String lng,
                                                            @Field("tripTimeCalc")String tripTimeCalc,
                                                            @Field("tripTimeFee")String tripTimeFee,
                                                            @Field("tripDistanceCalc")String tripDistanceCalc,
                                                            @Field("tripDistanceFee")String tripDistanceFee,
                                                            @Field("totalAmount")String totalAmount);

    @GET("master/cancellationReasonRide")
    Observable<Response<ResponseBody>> getCancellationReasonRide(@Header("authorization")String authorization,
                                                                 @Header("lan")String language);

    @POST("master/bookingCancel")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> bookingCancel(@Header("authorization")String authorization,
                                                     @Header("lan")String language,
                                                     @Field("bookingId")String bookingId,
                                                     @Field("reason")String customerEmail);

    @POST("master/meterBookingEmail")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> meterBookingEmail(@Header("authorization")String authorization,
                                                         @Header("lan")String language,
                                                         @Field("bookingId")String bookingId,
                                                         @Field("customerEmail")String customerEmail);

    @GET("master/bookingDetail/{bookingId}")
    Observable<Response<ResponseBody>> getBookingDetail(@Header("authorization")String authorization,
                                                        @Header("lan")String language,
                                                        @Path("bookingId") String bookingId);

    @POST("master/meterBookingEmail")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> sendMeterBookingEmail(@Header("authorization")String authorization,
                                                             @Header("lan")String language,
                                                             @Field("bookingId")String bookingId,
                                                             @Field("customerEmail")String customerEmail);

    @POST("master/reviewAndRating")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> reviewForRideBooking(@Header("authorization")String authorization,
                                                            @Header("lan")String language,
                                                            @Field("bookingId")String bookingId,
                                                            @Field("rating")Double rating,
                                                            @Field("customerSignature")String customerSignature
    );

    @GET("master/bookingHistory/{startDate}")
    Observable<Response<ResponseBody>> order (@Header("lan") String language,
                                              @Header("authorization") String authorization,
                                              @Path("startDate") String startDate);

    @GET("master/bookingHistory/{startDate}/{pageIndex}")
    Observable<Response<ResponseBody>> bookingHistory (@Header("lan") String language,
                                              @Header("authorization") String authorization,
                                              @Path("startDate") String startDate,
                                                       @Path("pageIndex") Double pageIndex);

    /*Payment Fragment*/
    @GET("master/card")
    Observable<Response<ResponseBody>>  getPaymentService(@Header("authorization") String authToken,
                                                          @Header("lan") String language);


    @HTTP(method = "DELETE", path = "master/card", hasBody = true)
    @FormUrlEncoded
    Observable<Response<ResponseBody>> deleteCard(@Header("authorization") String authToken,
                                                  @Header("lan") String language,
                                                  @Field("cardId") String cardId);


    @POST("master/card")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> addCard(@Header("authorization") String language,
                                               @Header("lan") String languageCode,
                                               @Field("email") String email,
                                               @Field("cardToken") String cardToken);

    @GET("master/walletTransaction/{pageIndex}")
    Observable<Response<ResponseBody>> getWalletTransaction(@Header("authorization") String authToken,
                                                            @Header("lan") String language,
                                                            @Path("pageIndex") String pageIndex);


    @GET("master/walletTransaction/{txnType}/{pageIndex}")
    Observable<Response<ResponseBody>> getWalletTransactionIndex(@Header("authorization") String authToken,
                                                            @Header("lan") String language,
                                                            @Path("txnType") String txnType,
                                                            @Path("pageIndex") String pageIndex);

    @GET("master/walletDetail")
    Observable<Response<ResponseBody>> getWalletLimits(@Header("authorization") String authToken,
                                                       @Header("lan") String language);

    @PATCH("master/card")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> makeDefaultCard(@Header("authorization") String language,
                                                       @Header("lan") String languageCode,
                                                       @Field("cardId") String cardId);

    @POST("master/rechargeWallet")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> rechargeWallet(@Header("authorization") String authToken,
                                                      @Header("lan") String language,
                                                      @Field("cardId") String cardId,
                                                      @Field("amount") String amount);



    @POST("message")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> message(@Header("language") String language,
                                               @Header("authorization") String authorization,
                                               @Field("type") String type,
                                               @Field("timestamp") String timestamp,
                                               @Field("content") String content,
                                               @Field("fromID") String fromID,
                                               @Field("bid") String bid,
                                               @Field("targetId") String targetId
    );


    @GET("chatHistory/{bookingId}/{pageNo}")
    Observable<Response<ResponseBody>> chatHistory(@Header("language") String language,
                                                   @Header("authorization") String authorization,
                                                   @Path("bookingId") String bid,
                                                   @Path("pageNo") String page
    );


    @PATCH("/utility/notification")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateNotificationStatus(@Header("authorization") String language,
                                                                @Header("lan") String languageCode,
                                                                @Field("status") int status,
                                                                @Field("messageId") String messageId);

    @PATCH("master/preferedAreaZone")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> patchPreferedAreaZone (@Header("lan") String language,
                                                              @Header("authorization") String authorization,
                                                              @Field("areaZoneId") String areaZoneId,
                                                              @Field("bookingNumber")String bookingNumber);

    @DELETE("master/preferedAreaZone")
    Observable<Response<ResponseBody>> deletePreferedAreaZone (@Header("lan") String language,
                                                               @Header("authorization") String authorization);


    @GET("master/getYears")
    Observable<Response<ResponseBody>> getYears (@Header("lan")String language);

    @POST("master/getMake")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> getMake (@Header("lan")String language,
                                                @Field("year") String year);

    @POST("master/getModel")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> getModel (@Header("lan")String language,
                                                 @Field("year") String year,
                                                 @Field("makeId") String makeId);


    @GET("master/towTruckServices")
    Observable<Response<ResponseBody>> getTowTruckServices(@Header("lan") String language,
                                                           @Header("authorization") String authorization);



    @POST("master/towTruckServices")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> postTowTruckServices (@Header("authorization")String authorization,
                                                             @Header("lan") String language,
                                                             @Field("bookingId") String bookingId,
                                                             @Field("towTruckServicesIds") String towTruckServicesIds);

    @POST("master/updateTowing")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> postUpdateTowing (@Header("authorization")String authorization,
                                                             @Header("lan") String language,
                                                             @Field("bookingId") String bookingId);

    /**
     * <p>if the booking is in card payment, then call this API</p>
     */
    @POST("master/bookingStatusRideCard")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> bookingStatusRideCard(@Header("authorization")String authorization,
                                                             @Header("lan")String language,
                                                             @Field("bookingId") String bookingId,
                                                             @Field("status") Integer status,
                                                             @Field("lat") String lat,
                                                             @Field("long") String lng,
                                                             @Field("distance") String distance,
                                                             @Field("dateTime") String dateTime);


    @GET("zendesk/user/ticket/{emailId}")
    Observable<Response<ResponseBody>> onToGetZendeskTicket(@Header("authorization") String authorization,
                                                            @Header("language") String language,
                                                            @Path("emailId") String emailId);

    @PUT("zendesk/ticket/comments")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> commentOnTicket(@Header("authorization") String authorization,
                                                       @Header("language") String language,
                                                       @Field("id") String id,
                                                       @Field("body") String body,
                                                       @Field("author_id") String author_id);

    @POST("zendesk/ticket")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> createTicket(@Header("authorization") String authorization,
                                                    @Header("language") String language,
                                                    @Field("subject") String subject,
                                                    @Field("body") String body,
                                                    @Field("status") String status,
                                                    @Field("priority") String priority,
                                                    @Field("type") String type,
                                                    @Field("requester_id") String requester_id);

    @GET("zendesk/ticket/history/{id}")
    Observable<Response<ResponseBody>> onToGetZendeskHistory(@Header("authorization") String authorization,
                                                             @Header("language") String language,
                                                             @Path("id") String id);

    @GET("master/helpText/{status}")
    Observable<Response<ResponseBody>> getHelpText(@Header("authorization") String authorization,
                                                   @Header("language") String language,
                                                   @Path("status") double status);

}

