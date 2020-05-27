package com.myoptimind.usashopper.api;


import android.util.Log;

import com.myoptimind.usashopper.models.User;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

    class LoginResponse {
        private User data;
        private Meta meta;

        public User getData() {
            return data;
        }

        public void setData(User data) {
            this.data = data;
        }

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }
    }


    @POST("users/login")
    @FormUrlEncoded
    Flowable<LoginResponse> authenticateUser(
            @Field("email")
            String email,
            @Field("password")
            String password
    );
}
