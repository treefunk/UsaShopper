package com.myoptimind.usashopper.models;


import android.widget.Switch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//TODO modify attributes
public class Order {

    public static final String TYPE_ORDER_FULL    = "type_order_full";
    public static final String TYPE_ORDER_PARTIAL = "type_order_partial";

    private String id;

    private String label;

    @SerializedName("order_id")
    private String orderId;

    private String link;

    @SerializedName("qty")
    private String quantity;

    private String price;

    @SerializedName("status")
    private String status;

    @SerializedName("formatted_status")
    private String formattedStatus;

    @SerializedName("requested_date")
    private String requestedDate;

    @SerializedName("shopper_email")
    private String shopperEmail;

    @SerializedName("shopper")
    private String shopperName;

    @SerializedName("other_info")
    private String otherInfo;


    @SerializedName("warehouse")
    private List<OrderUpload> uploads;

    @SerializedName("item_status")
    private List<OrderStatus> statusList;

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFormattedStatus() {
        return formattedStatus;
    }

    public void setFormattedStatus(String formattedStatus) {
        this.formattedStatus = formattedStatus;
    }

    public String getShopperName() {
        return shopperName;
    }

    public void setShopperName(String shopperName) {
        this.shopperName = shopperName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public List<OrderStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<OrderStatus> statusList) {
        this.statusList = statusList;
    }

    public String getStatusColorHex(){
        switch(getFormattedStatus()){
            case "Waiting" : {
                return "#F18237";
            }
            case "Complete Delivery": {
                return "#38B21E";
            }
            case "Partial Delivery": {
                return "#91EB7E";
            }
            default: {
                return "#25305D";
            }
        }
    }
}
