package com.myoptimind.usashopper;

import android.util.Log;

import com.myoptimind.usashopper.api.ErrorResponse;
import com.myoptimind.usashopper.api.UsaShopperApi;

import java.net.UnknownHostException;

import retrofit2.HttpException;

public class Utils {
    public static String handleRequestExceptions(Throwable error){
        if(error instanceof UnknownHostException){
            return "Internet Connection is required. Please try again.";
        }
        try{
            HttpException e = (HttpException) error;
            ErrorResponse errorResponse = UsaShopperApi.getConverter().convert(e.response().errorBody());
            return errorResponse.getMeta().getMessage();
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
