package com.myoptimind.usashopper.features.orderdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myoptimind.usashopper.SingleFragmentActivity;


public class OrderActivity extends SingleFragmentActivity {

    private static final String KEY_ORDER_ID = "key_order_id";
    private String orderId;

    public static Intent createIntent(Context context, String orderId){
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra(KEY_ORDER_ID,orderId);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
//        Timber.plant(new Timber.DebugTree());
    }

    @Override
    protected Fragment createFragment() {
        Bundle bundle = getIntent().getExtras();

        orderId = getIntent().getExtras().getString(KEY_ORDER_ID);

        return OrderFragment.newInstance(orderId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setBottomNavVisibility(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
