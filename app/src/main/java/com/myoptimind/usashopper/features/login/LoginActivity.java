package com.myoptimind.usashopper.features.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myoptimind.usashopper.SingleFragmentActivity;
import com.myoptimind.usashopper.features.login.LoginFragment;
import com.myoptimind.usashopper.features.searchorder.SearchActivity;
import com.myoptimind.usashopper.features.shared.AppSharedPref;

public class LoginActivity extends SingleFragmentActivity {

    public static Intent createIntent(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(AppSharedPref.getInstance(getApplicationContext()).hasLogin()){
            startActivity(SearchActivity.createIntent(this));
            finish();
        }
        setBottomNavVisibility(false);
        setSideNavVisibility(false);
        super.onCreate(savedInstanceState);
    }

}
