package com.myoptimind.usashopper.features.searchorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.myoptimind.usashopper.models.Order;
import com.myoptimind.usashopper.repositories.OrderRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {

    OrderRepository mOrderRepository;
    private LiveData<List<Order>> orders;


    public SearchViewModel() {
        mOrderRepository = new OrderRepository();
        orders = mOrderRepository.getOrders();
    }

    public LiveData<List<Order>> getOrders() {
        return orders;
    }
}
