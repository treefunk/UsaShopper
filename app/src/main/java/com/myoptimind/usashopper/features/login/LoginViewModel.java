package com.myoptimind.usashopper.features.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.myoptimind.usashopper.api.AuthService;
import com.myoptimind.usashopper.api.ErrorResponse;
import com.myoptimind.usashopper.api.UsaShopperApi;
import com.myoptimind.usashopper.features.shared.AppSharedPref;
import com.myoptimind.usashopper.repositories.UserRepository;

import java.net.UnknownHostException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";

    private UserRepository mUserRepository;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private AppSharedPref mSharedPref;
    private MutableLiveData<String> mUsername = new MutableLiveData<>();
    private MutableLiveData<String> mPassword = new MutableLiveData<>();


    public LoginViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository();
        mSharedPref = AppSharedPref.getInstance(application);
    }

    public void authenticateUser(
            String email,
            String password,
            final LoginListener loginListener
    ){
        mDisposables.add(mUserRepository.login(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loginResponse -> {
                            loginListener.onSuccessLogin(loginResponse.getData());
                            mSharedPref.setLogin(
                                    loginResponse.getData().getId(),
                                    loginResponse.getData().getEmail()
                            );
                            loginListener.onComplete();
                        },
                        error -> {
                            if(error instanceof UnknownHostException){
                                loginListener.onErrorLogin("Internet Connection is required. Please try again.", "400");
                            }
                            try{
                                HttpException e = (HttpException) error;
                                ErrorResponse errorResponse = UsaShopperApi.getConverter().convert(e.response().errorBody());
                                loginListener.onErrorLogin(errorResponse.getMeta().getMessage(),"400");
                            }catch (Exception e){
                                Log.e(TAG,e.getMessage());
                            }
                            loginListener.onComplete();
                        }
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.clear();
    }

    public LiveData<String> getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername.setValue(username);
    }

    public LiveData<String> getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword.setValue(password);
    }
}
