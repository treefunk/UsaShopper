package com.myoptimind.usashopper.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myoptimind.usashopper.api.OrderService;
import com.myoptimind.usashopper.api.UsaShopperApi;
import com.myoptimind.usashopper.models.Order;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

                for (Order order : response.body().getResults()) {

                    ArrayList<String> uploads = new ArrayList<>();
                    int imageCount = new Random().nextInt(5);

                    for(int i = 0 ; i < imageCount ; i++){
                        uploads.add("https://loremflickr.com/400/400");
                    }
                    order.setUploads(uploads);
                }

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

    public LiveData<List<OrderUpload>> getOrderUploads(){
        MutableLiveData<List<OrderUpload>> orderUploads = new MutableLiveData<>();

            ArrayList<OrderUpload> uploads = new ArrayList<>();
            int imageCount = new Random().nextInt(5);

            for(int i = 0 ; i < imageCount ; i++){
                OrderUpload orderUpload = new OrderUpload();
                orderUpload.setImage("https://vignette.wikia.nocookie.net/amberstars-legend/images/3/34/EB89E9DE-24EC-4A11-8410-B8D99766B4CD.jpeg");
                orderUpload.setId(i);
                uploads.add(orderUpload);
            }

            uploads.add(null);

            orderUploads.setValue(uploads);


            return orderUploads;
    }


}
