package com.karry.telecall.callingapi;

import android.app.Application;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.karry.network.UnsafeOkHttpClient;
import com.heride.partner.BuildConfig;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <h1>NetworkModule</h1>
 * This class is used to provide the instance of retrofit API call
 * @author  3Embed on 03-Nov-17.
 */
@Module
public class CallingModule
{
    private static final String NAME_CALL_URL = "NAME_CALL_URL";
    private static final long CACHE_SIZE = 10 * 1024 * 1024; //10 MB

    @Provides
    @Named(NAME_CALL_URL)
    String provideCallUrlString() {
        return BuildConfig.INAPPCALLURL;
    }

    @Provides
    @Singleton
    @Named(NAME_CALL_URL)
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    @Named(NAME_CALL_URL)
    Cache provideOkhttpCache(Application application) {
        return new Cache(application.getCacheDir(), CACHE_SIZE);
    }

    @Provides
    @Singleton
    @Named(NAME_CALL_URL)
    OkHttpClient provideOkHttpClient(Cache cache)
    {
        return UnsafeOkHttpClient.getUnsafeOkHttpClient();
    }



    @Provides
    @Singleton
    @Named(NAME_CALL_URL)
    Retrofit provideRetrofit(Converter.Factory converter, @Named(NAME_CALL_URL) String callUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(callUrl)
                .addConverterFactory(converter)

                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    CallingService provideCallingService(@Named(NAME_CALL_URL) Retrofit retrofit) {
        return retrofit.create(CallingService.class);
    }
}
