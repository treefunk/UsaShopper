package com.myoptimind.usashopper.api;

import com.myoptimind.usashopper.models.Order;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface OrderService {

    class OrdersResponse {
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
    Flowable<OrdersResponse> listOrders(
            @Field("text")
            String keyword
    );

    class OrderResponse{

        private Order data;
        private Meta meta;

        public Order getData() {
            return data;
        }

        public void setData(Order data) {
            this.data = data;
        }

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }
    }

    @GET("item/{item_id}")
    Flowable<OrderResponse> getOrder(
            @Path("item_id") String itemId
    );

    class OrderUploadResponse {
        public OrderUpload data;

        public OrderUpload getData() {
            return data;
        }

        public void setData(OrderUpload data) {
            this.data = data;
        }
    }

    /**
     * Upload image for single item
     */
    @Multipart
    @POST("item/upload/{item_id}")
    Flowable<OrderUploadResponse> uploadOrder(
                @Path("item_id") String itemId,
                @Part MultipartBody.Part image
    );


    /**
     * Remove Uploaded image id
     */
    @POST("items/remove")
    @FormUrlEncoded
    Flowable<OrderRemoveUploadResponse> removeImage(
            @Field("image_id") String imageId
    );

    class OrderRemoveUploadResponse {
        private Boolean data;
        private Meta meta;

        public Boolean getData() {
            return data;
        }

        public void setData(Boolean data) {
            this.data = data;
        }

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }

    }

    /**
     * Update item status
     */
    @POST("items/update")
    @FormUrlEncoded
    Flowable<OrderResponse> updateItemStatus(
        @Field("id") String id,
        @Field("status") String status
    );





}
