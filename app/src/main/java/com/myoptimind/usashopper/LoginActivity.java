package com.myoptimind.usashopper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myoptimind.usashopper.ui.LoginFragment;

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
