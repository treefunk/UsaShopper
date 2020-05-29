package com.myoptimind.usashopper.repositories;

import androidx.lifecycle.MutableLiveData;

import com.myoptimind.usashopper.api.OrderService;
import com.myoptimind.usashopper.api.UsaShopperApi;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;

public class OrderRepository {

    private static final String TAG = "OrderRepository";

    private OrderService mOrderService;

    public OrderRepository() {
        mOrderService = UsaShopperApi.createOrderService();
    }

    public Flowable<OrderService.OrdersResponse> getOrderList(String keyword){
        return mOrderService.listOrders(keyword);
    }

    public Flowable<OrderService.OrderResponse> getOrder(String orderId){
        return mOrderService.getOrder(orderId);
    }

    public Flowable<OrderService.OrderUploadResponse> uploadOrderImage(String itemId, MultipartBody.Part uploadMultipart){
        return mOrderService.uploadOrder(itemId,uploadMultipart);
    }


}
