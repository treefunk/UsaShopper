package com.myoptimind.usashopper.features.searchorder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.textfield.TextInputEditText;
import com.myoptimind.usashopper.features.orderdetail.OrderActivity;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.features.shared.AppSharedPref;
import com.myoptimind.usashopper.models.Order;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private final String TAG = "SearchFragment";

    public static final int REQUEST_SINGLE_ORDER = 444;
    public static final String REQUEST_STATUS_LABEL_KEY = "status_label_key";
    public static final String REQUEST_STATUS_KEY = "status_key";


    private ConstraintLayout root;
    ConstraintSet constraintSet = null;
    private int currentPos = -1;
    private OrderAdapter orderAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search,container,false);

        RecyclerView rvOrders      = v.findViewById(R.id.rv_orders);
        TextInputEditText etSearch = v.findViewById(R.id.et_search);
        Button btnSearch           = v.findViewById(R.id.btn_search);

        getActivity().setTitle("Admin: " + AppSharedPref.getInstance(getActivity()).getLoggedInName());


        setupSearch(
                rvOrders,
                etSearch,
                btnSearch
        );

        root = (ConstraintLayout) v;

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupSearch(final RecyclerView rvOrders, final TextInputEditText etSearch, Button btnSearch) {

        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        orderAdapter = new OrderAdapter(new ArrayList<>());


        rvOrders.setAdapter(orderAdapter);
        searchViewModel.getIsFetchingOrders().observe(getViewLifecycleOwner(), (Boolean isFetching) -> {
            btnSearch.setEnabled(!isFetching);
            if(isFetching){
                btnSearch.setText("");
            }else{
                btnSearch.setText("Search");
            }
        });

        searchViewModel.getAlertMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            }
        });


        searchViewModel.getOrders().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {


                if(orders != null){

                    if(constraintSet == null){
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(getActivity(),R.layout.fragment_search);
                        constraintSet.connect(root.findViewById(R.id.card_search).getId(),ConstraintSet.BOTTOM,
                                rvOrders.getId(),ConstraintSet.TOP);
                        TransitionManager.beginDelayedTransition(root);
                        constraintSet.applyTo(root);
                        rvOrders.setVisibility(View.VISIBLE);
                    }


                    orderAdapter = new OrderAdapter(orders);

                    orderAdapter.setOrderListener(new OrderAdapter.OrderListener() {
                        @Override
                        public void onClickView(Order order, int position) {
                            Intent intent = OrderActivity.createIntent(getActivity(),order.getId());
                            currentPos = position;
                            SearchFragment.this.startActivityForResult(intent,REQUEST_SINGLE_ORDER);
                        }
                    });

                    rvOrders.setAdapter(orderAdapter);
                    orderAdapter.notifyDataSetChanged();

                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewModel.getOrders(etSearch.getText().toString());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_SINGLE_ORDER: {
                if(data != null){
                    Bundle bundle = data.getExtras();
                    if(currentPos != -1 && orderAdapter != null){
                        Log.v(TAG, "notified item " + currentPos);
                        orderAdapter.getOrders().get(currentPos).setFormattedStatus(bundle.getString(REQUEST_STATUS_LABEL_KEY));
                        orderAdapter.getOrders().get(currentPos).setStatus(bundle.getString(REQUEST_STATUS_KEY));
                        orderAdapter.notifyItemChanged(currentPos);
                        currentPos = -1; // reset
                    }
                }
                break;
            }
        }
    }
}
