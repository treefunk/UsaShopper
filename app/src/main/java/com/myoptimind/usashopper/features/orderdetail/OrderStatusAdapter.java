package com.myoptimind.usashopper.features.orderdetail;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.OrderStatus;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.OrderStatusViewHolder> {

    List<OrderStatus> mStatusList;
    StatusListener mStatusListener;

    public class OrderStatusViewHolder extends RecyclerView.ViewHolder{

        private MaterialButton btnStatus;
        StatusListener mStatusListener;


        public OrderStatusViewHolder(@NonNull View itemView, StatusListener statusListener) {

            super(itemView);
            mStatusListener = statusListener;
            btnStatus = itemView.findViewById(R.id.btn_status);

        }

        public void bind(OrderStatus orderStatus){


            btnStatus.setText(orderStatus.getLabel());
            btnStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStatusListener.onClickStatus(orderStatus,getAdapterPosition());
                }
            });

        }
    }

    public OrderStatusAdapter(List<OrderStatus> statusList, StatusListener statusListener) {
        mStatusList     = statusList;
        mStatusListener = statusListener;
    }

    public void setStatusList(List<OrderStatus> statusList) {
        mStatusList = statusList;
    }

    @NonNull
    @Override
    public OrderStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new OrderStatusViewHolder(inflater.inflate(R.layout.list_item_status,parent,false),mStatusListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusViewHolder holder, int position) {
        OrderStatus orderStatus = mStatusList.get(position);
        holder.bind(orderStatus);
    }

    @Override
    public int getItemCount() {
        return mStatusList.size();
    }

    public interface StatusListener {
        void onClickStatus(OrderStatus orderStatus, int position);
    }
}
