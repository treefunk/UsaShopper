package com.myoptimind.usashopper.api;

import com.myoptimind.usashopper.models.Order;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public abstract class UsaShopperApi {

    private static Retrofit INSTANCE;


    public static final String BASE_URL = "http://usashopper.betaprojex.com/dev/api/"; //TODO modify base url


    public static OrderService createOrderService(){
        return create(HttpUrl.parse(BASE_URL)).create(OrderService.class);
    }

    public static AuthService createAuthService(){
        return create(HttpUrl.parse(BASE_URL)).create(AuthService.class);
    }

    private static Retrofit create(HttpUrl httpUrl){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        if(INSTANCE == null){
            INSTANCE = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return INSTANCE;
    }

    public static Converter<ResponseBody, ErrorResponse> getConverter(){
        return INSTANCE.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
    }


}
