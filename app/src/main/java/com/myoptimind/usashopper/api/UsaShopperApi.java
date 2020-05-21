package com.myoptimind.usashopper.api;

import com.myoptimind.usashopper.models.Order;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public abstract class UsaShopperApi {


    public static final String BASE_URL = "https://swapi.dev/api/"; //TODO modify base url


    public static OrderService createOrderService(){
        return create(HttpUrl.parse(BASE_URL)).create(OrderService.class);
    }

    private static Retrofit create(HttpUrl httpUrl){

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
