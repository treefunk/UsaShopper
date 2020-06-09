package com.myoptimind.usashopper.features.searchorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.myoptimind.usashopper.models.Order;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private final String TAG = "SearchFragment";
    private ConstraintLayout root;
    ConstraintSet constraintSet = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search,container,false);

        RecyclerView rvOrders      = v.findViewById(R.id.rv_orders);
        TextInputEditText etSearch = v.findViewById(R.id.et_search);
        Button btnSearch           = v.findViewById(R.id.btn_search);


        setupSearch(
                rvOrders,
                etSearch,
                btnSearch
        );

        root = (ConstraintLayout) v;

        return v;
    }

    private void setupSearch(final RecyclerView rvOrders, final TextInputEditText etSearch,Button btnSearch) {

        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rvOrders.setAdapter(new OrderAdapter(new ArrayList<>()));
        searchViewModel.getIsFetchingOrders().observe(getViewLifecycleOwner(), (Boolean isFetching) -> {
            btnSearch.setEnabled(!isFetching);
            if(isFetching){
                btnSearch.setText("");
            }else{
                btnSearch.setText("Search");
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
                        /*getActivity().setTitle("Search Order");*/
                        rvOrders.setVisibility(View.VISIBLE);
                    }


                    final OrderAdapter orderAdapter = new OrderAdapter(orders);

                    orderAdapter.setOrderListener(new OrderAdapter.OrderListener() {
                        @Override
                        public void onClickView(Order order, int position) {
                            Intent intent = OrderActivity.createIntent(getActivity(),order.getId());
                            startActivity(intent);
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


}
