package com.myoptimind.usashopper.features.orderdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class DialogStatusFragment extends BottomSheetDialogFragment {

    private View view;

    private static final String TAG = "DialogStatusFragment";

    private static final String KEY_STATUS_LIST  = "key_status_list";

    public static final String KEY_STATUS_LABEL  = "key_status_label";
    public static final String KEY_STATUS_ID     = "key_status_id";

    private OrderViewModel mOrderViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderViewModel = new ViewModelProvider(getTargetFragment()).get(OrderViewModel.class);
    }

    public static DialogStatusFragment newInstance() {

        Bundle args = new Bundle();

        DialogStatusFragment fragment = new DialogStatusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogStatusFragment newInstance(ArrayList<OrderStatus> statusList) {

        Bundle args = new Bundle();
        DialogStatusFragment fragment = new DialogStatusFragment();
        args.putParcelableArrayList(KEY_STATUS_LIST, statusList);
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dialog_status,container,false);

        initStatusList();
        
        return view;
    }

    private void initStatusList() {
        RecyclerView rvStatus = view.findViewById(R.id.rv_status);

        rvStatus.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));

        OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(
                new ArrayList<>(),
                new OrderStatusAdapter.StatusListener() {
                    @Override
                    public void onClickStatus(OrderStatus orderStatus, int position) {
                        Intent intent = new Intent();
                        intent.putExtra(KEY_STATUS_LABEL,orderStatus.getLabel());
                        intent.putExtra(KEY_STATUS_ID,orderStatus.getId());
                        getTargetFragment().onActivityResult(OrderFragment.REQUEST_ORDER_STATUS, OrderActivity.RESULT_OK,intent);
                        DialogStatusFragment.this.dismiss();
                    }
                }
        );

        rvStatus.setAdapter(orderStatusAdapter);

        mOrderViewModel.getStatusList().observe(getViewLifecycleOwner(), new Observer<List<OrderStatus>>() {
            @Override
            public void onChanged(List<OrderStatus> orderStatuses) {
                if(orderStatuses != null){
                    orderStatusAdapter.setStatusList(orderStatuses);
                    orderStatusAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
