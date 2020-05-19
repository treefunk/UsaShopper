package com.myoptimind.usashopper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.usashopper.OrderActivity;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.adapters.OrderAdapter;
import com.myoptimind.usashopper.models.Order;
import com.myoptimind.usashopper.viewmodels.SearchViewModel;

import java.util.List;

public class SearchFragment extends Fragment {

    private final String TAG = "SearchFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search,container,false);

        RecyclerView rvOrders = v.findViewById(R.id.rv_orders);
        setupSearch(rvOrders);

        return v;
    }

    private void setupSearch(final RecyclerView rvOrders) {

        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));

        searchViewModel.getOrders().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {

                if(orders != null){

                    final OrderAdapter orderAdapter = new OrderAdapter(orders);

                    orderAdapter.setOrderListener(new OrderAdapter.OrderListener() {
                        @Override
                        public void onClickView(Order order, int position) {
                            Intent intent = OrderActivity.createIntent(getActivity(),999);
                            startActivity(intent);
                        }
                    });

                    rvOrders.setAdapter(orderAdapter);
                    orderAdapter.notifyDataSetChanged();

                }
            }
        });

    }


}
