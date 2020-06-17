package com.myoptimind.usashopper.features.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.internal.bind.util.ISO8601Utils;

public class AppSharedPref {
    private static final String SP_NAME = "APP_SHARED_PREFERNCE";

    private static AppSharedPref INSTANCE;
    private static final String LOGGED_IN_KEY = "LOGGED_IN_KEY";
    private static final String LOGGED_IN_NAME = "LOGGED_IN_NAME";
    SharedPreferences mSharedPreferences;

    public AppSharedPref(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }

    public static AppSharedPref getInstance(Context context) {
        if(INSTANCE == null){
            synchronized (AppSharedPref.class){
                if(INSTANCE == null){
                    INSTANCE = new AppSharedPref(context);
                }
            }
        }
        return INSTANCE;
    }

    public void setLogin(String id,String name){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LOGGED_IN_KEY,id);
        editor.putString(LOGGED_IN_NAME,name);
        editor.apply();
    }

    public String getLoggedInName(){
        return mSharedPreferences.getString(LOGGED_IN_NAME,"");
    }

    public Boolean hasLogin(){
        return mSharedPreferences.contains(LOGGED_IN_KEY);
    }

    public void clear(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
