package com.myoptimind.usashopper.features.orderdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.List;

public class OrderFragment extends Fragment {

    private static final String TAG = "OrderFragment";

    private static final String ARGS_ORDER_ID = "args_order_id";

    OrderViewModel orderViewModel;

    public static OrderFragment newInstance(int orderId) {

        Bundle args = new Bundle();

        OrderFragment fragment = new OrderFragment();
        args.putInt(ARGS_ORDER_ID,orderId);
        fragment.setArguments(args);
        return fragment;

    }

    private int getOrderId(){
        return getArguments().getInt(ARGS_ORDER_ID,-1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order,container,false);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        final RecyclerView rvOrderUploads = view.findViewById(R.id.rv_upload_orders);
        setupUploads(rvOrderUploads);

        return view;
    }

    private void setupUploads(final RecyclerView rvOrderUploads) {

        rvOrderUploads.setLayoutManager(new GridLayoutManager(getActivity(),2));

        orderViewModel.getOrderUploads().observe(getViewLifecycleOwner(), new Observer<List<OrderUpload>>() {
            @Override
            public void onChanged(List<OrderUpload> orderUploads) {
                if(orderUploads != null){

                    final UploadOrderAdapter uploadOrderAdapter = new UploadOrderAdapter(orderUploads);

                    rvOrderUploads.setAdapter(uploadOrderAdapter);
                    uploadOrderAdapter.notifyDataSetChanged();

                }
            }
        });

    }

}
