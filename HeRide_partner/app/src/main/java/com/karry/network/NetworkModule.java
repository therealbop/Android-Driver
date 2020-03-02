package com.karry.network;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.heride.partner.BuildConfig;

import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class NetworkModule
{
    private static final String NAME_BASE_URL = "NAME_BASE_URL";
    private static final String NAME_CAR_BASE_URL = "NAME_CAR_BASE_URL";

    @Provides
    @Named(NAME_BASE_URL)
    String provideBaseUrlString() {
        return BuildConfig.BASEURL;
    }


    @Provides
    @Named(NAME_CAR_BASE_URL)
    String provideCarBaseUrlString() {
        return BuildConfig.CARAPI;
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory converter, @Named(NAME_BASE_URL) String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    NetworkService provideUsdaApi(Retrofit retrofit)
    {
        return retrofit.create(NetworkService.class);
    }

    @Provides
    @Singleton
    CarApiService provideCarApi(Converter.Factory converter, @Named(NAME_CAR_BASE_URL) String baseUrl)
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(CarApiService.class);
    }



}
