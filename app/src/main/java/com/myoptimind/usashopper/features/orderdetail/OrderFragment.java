package com.myoptimind.usashopper.features.orderdetail;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment{

    private static final String TAG = "OrderFragment";

    private static final String ARGS_ORDER_ID = "args_order_id";
    private static final int REQUEST_IMAGE_CAPTURE = 300;


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

        final RecyclerView rvOrderUploads = view.findViewById(R.id.rv_upload_orders);
        final Button btnMark              = view.findViewById(R.id.btn_mark_as_arrived);
        initOrderUploads(rvOrderUploads,btnMark);




        return view;
    }


    private int getOrderId(){
        return getArguments().getInt(ARGS_ORDER_ID,-1);
    }



    private void initOrderUploads(final RecyclerView rvOrderUploads, final Button btnMark) {

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
                            Toast.makeText(getActivity(),markMessage,Toast.LENGTH_SHORT).show();
                        }
                    });

                    final UploadOrderAdapter uploadOrderAdapter = new UploadOrderAdapter(orderUploads);

                    uploadOrderAdapter.setUploadOrderListener(new UploadOrderListener() {
                        @Override
                        public void onClickUpload(int pos) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
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
                    });

                    rvOrderUploads.setAdapter(uploadOrderAdapter);
                    uploadOrderAdapter.notifyDataSetChanged();

                }
            }
        });

    }

}
