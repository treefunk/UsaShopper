package com.myoptimind.usashopper.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

//TODO modify attributes
public class Order {

    public static final String TYPE_ORDER_FULL    = "type_order_full";
    public static final String TYPE_ORDER_PARTIAL = "type_order_partial";

    @SerializedName("order_id")
    private String orderId;

    private String link;

    @SerializedName("formatted_status")
    private String formattedStatus;

    @SerializedName("requested_date")
    private String requestedDate;

    @SerializedName("shopper_email")
    private String shopperEmail;

    private String status;

    private List<String> uploads;

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

    public String getFormattedStatus() {
        return formattedStatus;
    }

    public void setFormattedStatus(String formattedStatus) {
        this.formattedStatus = formattedStatus;
    }

    public String getShopperEmail() {
        return shopperEmail;
    }

    public void setShopperEmail(String shopperEmail) {
        this.shopperEmail = shopperEmail;
    }

    public List<String> getUploads() {
        return uploads;
    }

    public void setUploads(List<String> uploads) {
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
}
