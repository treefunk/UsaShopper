package com.myoptimind.usashopper.features.uploadedorderview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myoptimind.usashopper.SingleFragmentActivity;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.ArrayList;

public class SlideShowActivity extends SingleFragmentActivity {

    public static final String KEY_IMAGES = "KEY_IMAGES";
    public static final String KEY_POSITION = "KEY_POSITION";

    @Override
    protected Fragment createFragment() {

        Bundle extras = getIntent().getExtras();

        return SlideShowFragment.newInstance(
                extras.getParcelableArrayList(KEY_IMAGES),
                extras.getInt(KEY_POSITION)
        );
    }

    public static Intent createIntent(Context context, ArrayList<OrderUpload> orderUploads, int position){

        Intent intent = new Intent(context,SlideShowActivity.class);
        intent.putParcelableArrayListExtra(KEY_IMAGES,orderUploads);
        intent.putExtra(KEY_POSITION,position);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setBottomNavVisibility(false);
        super.onCreate(savedInstanceState);
    }

}
