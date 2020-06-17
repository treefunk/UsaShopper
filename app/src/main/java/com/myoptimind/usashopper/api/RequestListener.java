package com.myoptimind.usashopper.api;

public interface RequestListener {
    void onRequestStart();
    void onFinishRequest(Boolean isSuccess);
    void onRequestError(String message, int errorCode);
}