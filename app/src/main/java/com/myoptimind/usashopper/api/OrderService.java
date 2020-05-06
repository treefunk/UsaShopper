package com.myoptimind.usashopper.api;

import com.myoptimind.usashopper.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OrderService {

    public class OrderResponse {

        private List<Order> results;

        public List<Order> getResults() {
            return results;
        }

        public void setResults(List<Order> results) {
            this.results = results;
        }
    }

    @GET("planets")
    Call<OrderResponse> listOrders();

}