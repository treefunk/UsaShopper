package com.myoptimind.usashopper.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myoptimind.usashopper.api.OrderService;
import com.myoptimind.usashopper.api.UsaShopperApi;
import com.myoptimind.usashopper.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {

    private static final String TAG = "OrderRepository";

    private OrderService mOrderService;

    public OrderRepository() {
        mOrderService = UsaShopperApi.createOrderService();
    }

    public LiveData<List<Order>> getOrders(){

        final MutableLiveData<List<Order>> orders = new MutableLiveData<>();

        mOrderService.listOrders().enqueue(new Callback<OrderService.OrderResponse>() {
            @Override
            public void onResponse(Call<OrderService.OrderResponse> call, Response<OrderService.OrderResponse> response) {
                Log.v(TAG,"SUCCESS");
                orders.postValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<OrderService.OrderResponse> call, Throwable t) {
                Log.v(TAG,"error");
                Log.v(TAG,t.getMessage());
            }
        });

        return orders;
    }


}
