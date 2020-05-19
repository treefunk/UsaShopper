package com.myoptimind.usashopper.features.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myoptimind.usashopper.SingleFragmentActivity;
import com.myoptimind.usashopper.features.login.LoginFragment;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setBottomNavVisibility(false);
        super.onCreate(savedInstanceState);
    }

}
