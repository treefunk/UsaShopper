package com.myoptimind.usashopper.features.orderdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myoptimind.usashopper.models.OrderUpload;
import com.myoptimind.usashopper.repositories.OrderRepository;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class OrderViewModel extends ViewModel {

    private MutableLiveData<List<OrderUpload>> orderUploads = new MutableLiveData<>();
    private OrderRepository mOrderRepository;

    public OrderViewModel() {
        OrderRepository mOrderRepository = new OrderRepository();
        orderUploads = mOrderRepository.getOrderUploads();
    }

    public void addOrderUpload(OrderUpload orderUpload){
        if(orderUploads != null){
            List<OrderUpload> list = orderUploads.getValue();
            list.remove(list.size() - 1);
            list.add(orderUpload);
            list.add(null);
            orderUploads.setValue(list);
        }
    }


    public LiveData<List<OrderUpload>> getOrderUploads() {
        return orderUploads;
    }
}
