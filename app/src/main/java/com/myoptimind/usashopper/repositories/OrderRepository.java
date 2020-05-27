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

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class OrderRepository {

    private static final String TAG = "OrderRepository";

    private OrderService mOrderService;

    public OrderRepository() {
        mOrderService = UsaShopperApi.createOrderService();
    }

    public Flowable<OrderService.OrderResponse> getOrderList(String keyword){
        return mOrderService.listOrders(keyword);
    }

/*    public LiveData<List<Order>> getOrders(){

        final MutableLiveData<List<Order>> orders = new MutableLiveData<>();

        mOrderService.listOrders().enqueue(new Callback<OrderService.OrderResponse>() {
            @Override
            public void onResponse(Call<OrderService.OrderResponse> call, Response<OrderService.OrderResponse> response) {
                Timber.v("SUCCESS");

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
                Timber.v("error");
                Timber.v(t.getMessage());
            }
        });

        return orders;
    }*/

    public MutableLiveData<List<OrderUpload>> getOrderUploads(){

        MutableLiveData<List<OrderUpload>> orderUploads = new MutableLiveData<>();

            ArrayList<OrderUpload> uploads = new ArrayList<>();
            int imageCount = new Random().nextInt(5);

            for(int i = 0 ; i < imageCount ; i++){
                OrderUpload orderUpload = new OrderUpload();
                orderUpload.setImage("http://placekitten.com/1000/1000");
                orderUpload.setId(i);
                uploads.add(orderUpload);
            }

            uploads.add(null);

            orderUploads.setValue(uploads);


            return orderUploads;
    }


}
