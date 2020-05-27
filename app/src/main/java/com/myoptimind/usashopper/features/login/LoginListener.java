package com.myoptimind.usashopper.features.login;

import com.myoptimind.usashopper.models.User;

public interface LoginListener {
    void onSuccessLogin(User user);
    void onErrorLogin(String message,String code);
    void onComplete();
}
