package com.myoptimind.usashopper.features.orderdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.myoptimind.usashopper.models.OrderUpload;
import com.myoptimind.usashopper.repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends ViewModel {

    private LiveData<List<OrderUpload>> orderUploads;
    private OrderRepository mOrderRepository;

    public OrderViewModel() {
        OrderRepository mOrderRepository = new OrderRepository();
        orderUploads = mOrderRepository.getOrderUploads();
    }

    public LiveData<List<OrderUpload>> getOrderUploads() {
        return orderUploads;
    }
}
