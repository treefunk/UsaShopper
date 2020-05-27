package com.myoptimind.usashopper.api;

import com.myoptimind.usashopper.models.Order;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OrderService {

    class OrderResponse {

        private List<Order> data;

        public List<Order> getData() {
            return data;
        }

        public void setData(List<Order> data) {
            this.data = data;
        }

    }

    @POST("orders/search")
    @FormUrlEncoded
    Flowable<OrderResponse> listOrders(
            @Field("text")
            String keyword
    );

}
