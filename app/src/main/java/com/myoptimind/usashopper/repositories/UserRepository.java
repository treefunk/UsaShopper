package com.myoptimind.usashopper.repositories;

import com.myoptimind.usashopper.api.AuthService;
import com.myoptimind.usashopper.api.UsaShopperApi;

import io.reactivex.Flowable;

public class UserRepository {

    private AuthService mAuthService;

    public UserRepository() {
        mAuthService = UsaShopperApi.createAuthService();
    }

    public Flowable<AuthService.LoginResponse> login(String email, String password){
        return mAuthService.authenticateUser(email,password);
    }


}
