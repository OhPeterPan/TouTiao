package com.kotlin.toutiao.net;

import com.kotlin.toutiao.BuildConfig;
import com.kotlin.toutiao.util.SdkManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCreator {
    private static volatile Retrofit retrofit;

    public static Retrofit retrofitCreate() {

        if (retrofit == null) {
            synchronized (RetrofitCreator.class) {
                if (retrofit == null) {

                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true);

                    // Log 拦截器
                    if (BuildConfig.DEBUG) {
                        builder = SdkManager.initInterceptor(builder);
                    }

                    retrofit = new Retrofit.Builder()
                            .baseUrl("http://toutiao.com/")
                            .client(builder.build())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

}
