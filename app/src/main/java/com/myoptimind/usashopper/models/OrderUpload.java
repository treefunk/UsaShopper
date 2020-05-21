package com.myoptimind.usashopper.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderUpload implements Parcelable {

    private int id;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.image);
    }

    public OrderUpload() {
    }

    public OrderUpload(Parcel in) {
        this.id = in.readInt();
        this.image = in.readString();
    }

    public static final Creator CREATOR = new Parcelable.Creator<OrderUpload>(){
        @Override
        public OrderUpload createFromParcel(Parcel source) {
            return new OrderUpload(source);
        }

        @Override
        public OrderUpload[] newArray(int size) {
            return new OrderUpload[size];
        }
    };
}

