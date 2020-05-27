package com.myoptimind.usashopper.features.orderdetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment{

    private static final String TAG = "OrderFragment";

    private static final String ARGS_ORDER_ID = "args_order_id";
    private static final int REQUEST_IMAGE_CAPTURE = 300;

    ConstraintLayout mConstraintLayout;


    OrderViewModel orderViewModel;

    public static OrderFragment newInstance(int orderId) {

        Bundle args = new Bundle();

        OrderFragment fragment = new OrderFragment();
        args.putInt(ARGS_ORDER_ID,orderId);
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order,container,false);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        final RecyclerView rvOrderUploads                = view.findViewById(R.id.rv_upload_orders);
        final Button btnMark                             = view.findViewById(R.id.btn_mark_as_arrived);
        final MaterialButtonToggleGroup btnToggleStatus  = view.findViewById(R.id.toggleButton);
        initOrderUploads(
                rvOrderUploads,
                btnMark,
                btnToggleStatus
        );

        mConstraintLayout = (ConstraintLayout) view;


        return view;
    }


    private int getOrderId(){
        return getArguments().getInt(ARGS_ORDER_ID,-1);
    }



    private void initOrderUploads(
            final RecyclerView rvOrderUploads,
            final Button btnMark,
            final MaterialButtonToggleGroup btnToggleStatus) {

        // set grid layout
        rvOrderUploads.setLayoutManager(new GridLayoutManager(getActivity(),2));

        // observe order uploads
        orderViewModel.getOrderUploads().observe(getViewLifecycleOwner(), new Observer<List<OrderUpload>>() {
            @Override
            public void onChanged(List<OrderUpload> orderUploads) {
                if(orderUploads != null){

                    String markMessage;

                    if(orderUploads.size() == 1){
                        markMessage = "Please upload an image first before proceeding.";
                    }else{
                        markMessage = "Order successfully marked as arrived.";
                    }

                    btnMark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG,"Marked as arrived");

                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            MarkDialogFragment markDialogFragment = new MarkDialogFragment();
                            markDialogFragment.show(fm,"t");


                        }
                    });

                    final UploadOrderAdapter uploadOrderAdapter = new UploadOrderAdapter(orderUploads);

                    uploadOrderAdapter.setUploadOrderListener(new UploadOrderListener() {
                        @Override
                        public void onClickUpload(int pos) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                                OrderFragment.this.startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
                            }
                        }

                        @Override
                        public void onClickImage(int pos) {
                            Intent intent = SlideShowActivity.createIntent(
                                    getActivity(),
                                    (ArrayList<OrderUpload>)orderUploads,
                                    pos
                            );
                            startActivity(intent);
                        }

                        @Override
                        public void onClickRemove(int pos) {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("Are you sure you want to remove this image?")
                                    .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            orderUploads.remove(pos);
                                            uploadOrderAdapter.notifyItemRemoved(pos);
                                        }
                                    }).setNegativeButton("Cancel",null)
                                    .show();

                        }
                    });

                    rvOrderUploads.setAdapter(uploadOrderAdapter);
                    uploadOrderAdapter.notifyDataSetChanged();

                }


            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            Log.d(TAG,"res" + resultCode);
            OrderUpload orderUpload = new OrderUpload();
            orderUpload.setId(99);
            orderUpload.setImage("");
            orderUpload.setBitmap((Bitmap)data.getExtras().get("data"));
            orderViewModel.addOrderUpload(orderUpload);
        }
    }
}
