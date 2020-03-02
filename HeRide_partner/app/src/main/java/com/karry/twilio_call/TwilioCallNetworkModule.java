package com.karry.twilio_call;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.heride.partner.BuildConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

@Module
public class TwilioCallNetworkModule
{
    private static final String NAMED_VAL="VALUE";
    private static final String TWILIO_DOMAIN = "twilio_domain";

    @Provides
    @Singleton
    @Named(NAMED_VAL)
    OkHttpClient provideOkHttpClient( @Named(TWILIO_DOMAIN) HostnameVerifier hostnameVerifier) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        return builder.hostnameVerifier(hostnameVerifier).build();
    }

    @Provides
    @Singleton
    @Named(TWILIO_DOMAIN)
    HostnameVerifier provideHostNameVerifier()
    {
        return (hostname, session) -> {
            HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify(BuildConfig.TWILIO_DOMAIN, session);
        };
    }

    @Provides
    @Singleton
    @Named(NAMED_VAL)
    Retrofit provideRetrofit(Converter.Factory converter, @Named(NAMED_VAL) OkHttpClient okHttpClient)//, @Named(NAME_BASE_URL_TWILIO) String baseUrl,
                              {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.TWILLIOURL)      //getcapability/
                .addConverterFactory(converter)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    TwilioCallNetworkService provideNetworkService(@Named(NAMED_VAL) Retrofit retrofit) {
        return retrofit.create(TwilioCallNetworkService.class);
    }
}
