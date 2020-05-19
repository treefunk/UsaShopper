package com.myoptimind.usashopper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myoptimind.usashopper.ui.SearchFragment;

public class HomeActivity extends SingleFragmentActivity {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new SearchFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
