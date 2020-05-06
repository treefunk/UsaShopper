package com.myoptimind.usashopper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> mOrders;

    public OrderAdapter(List<Order> orders) {
        mOrders = orders;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderNum,tvOrderSub,tvOrderDate;
        Button btnView;

        public OrderViewHolder(@NonNull View itemView) {

            super(itemView);

            tvOrderNum  = itemView.findViewById(R.id.tv_order_num);
            tvOrderSub  = itemView.findViewById(R.id.tv_order_sub);
            tvOrderDate = itemView.findViewById(R.id.tv_date);
            btnView     = itemView.findViewById(R.id.btn_view);

        }

        public void bind(Order order){
            tvOrderNum.setText("Order #" + order.getDiameter());
            tvOrderSub.setText(order.getName());
            tvOrderDate.setText(order.getGravity());
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderViewHolder viewHolder = new OrderViewHolder(inflater.inflate(R.layout.list_item_order,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(mOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }


}
