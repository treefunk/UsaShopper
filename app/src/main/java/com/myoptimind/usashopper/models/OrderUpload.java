package com.myoptimind.usashopper.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrderUpload implements Parcelable {


    private String id;

    @SerializedName("img")
    private String image;

    @SerializedName("caption")
    private String caption;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getCaption() {
        return caption;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.image);
        dest.writeString(this.caption);
    }

    public OrderUpload() {
    }

    public OrderUpload(Parcel in) {
        this.id = in.readString();
        this.image = in.readString();
        this.caption = in.readString();
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

