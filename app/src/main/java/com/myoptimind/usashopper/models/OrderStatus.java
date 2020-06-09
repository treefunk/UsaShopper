package com.myoptimind.usashopper.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderStatus implements Parcelable {

    private String id;
    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.label);
    }

    public OrderStatus() {
        // empty constructor
    }

    public OrderStatus(Parcel p) {
        this.id = p.readString();
        this.label = p.readString();
    }

    public Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public OrderStatus createFromParcel(Parcel source) {
            return new OrderStatus(source);
        }

        @Override
        public OrderStatus[] newArray(int size) {
            return new OrderStatus[size];
        }
    };
}
