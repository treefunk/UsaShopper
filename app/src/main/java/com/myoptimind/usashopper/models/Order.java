package com.myoptimind.usashopper.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

//TODO modify attributes
public class Order {

    public static final String TYPE_ORDER_FULL    = "type_order_full";
    public static final String TYPE_ORDER_PARTIAL = "type_order_partial";

    private String id;

    @SerializedName("order_id")
    private String orderId;

    private String link;

    @SerializedName("status")
    private String status;

    @SerializedName("requested_date")
    private String requestedDate;

    @SerializedName("shopper_email")
    private String shopperEmail;

    @SerializedName("warehouse")
    private List<OrderUpload> uploads;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShopperEmail() {
        return shopperEmail;
    }

    public void setShopperEmail(String shopperEmail) {
        this.shopperEmail = shopperEmail;
    }

    public List<OrderUpload> getUploads() {
        return uploads;
    }

    public void setUploads(List<OrderUpload> uploads) {
        this.uploads = uploads;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
