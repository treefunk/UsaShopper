package com.myoptimind.usashopper.features.orderdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myoptimind.usashopper.SingleFragmentActivity;

public class OrderActivity extends SingleFragmentActivity {

    private static final String KEY_ORDER_ID = "key_order_id";

    public static Intent createIntent(Context context, int orderId){
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra(KEY_ORDER_ID,orderId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int orderId = getIntent().getExtras().getInt(KEY_ORDER_ID);
        return OrderFragment.newInstance(orderId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setBottomNavVisibility(false);
        super.onCreate(savedInstanceState);
    }
}
