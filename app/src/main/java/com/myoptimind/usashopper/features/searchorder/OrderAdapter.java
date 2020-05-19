package com.myoptimind.usashopper.features.searchorder;

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
    private OrderListener mOrderListener;

    public OrderAdapter(List<Order> orders) {
        mOrders = orders;
    }

    public void setOrderListener(OrderListener orderListener) {
        mOrderListener = orderListener;
    }

    /*
    *
    *   View
    * */

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderNum,tvOrderSub,tvOrderDate;
        Button btnView;
        OrderListener mOrderListener;

        public OrderViewHolder(@NonNull View itemView,OrderListener orderListener) {

            super(itemView);

            tvOrderNum  = itemView.findViewById(R.id.tv_order_num);
            tvOrderSub  = itemView.findViewById(R.id.tv_order_sub);
            tvOrderDate = itemView.findViewById(R.id.tv_date);
            btnView     = itemView.findViewById(R.id.btn_view);
            mOrderListener = orderListener;

        }

        public void bind(final Order order, final int position){

            tvOrderNum.setText("Order #" + order.getDiameter());
            tvOrderSub.setText(order.getName());
            tvOrderDate.setText(order.getGravity());

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOrderListener.onClickView(order,position);
                }
            });

        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderViewHolder viewHolder = new OrderViewHolder(
                inflater.inflate(R.layout.list_item_order,parent,false),
                mOrderListener
        );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = mOrders.get(position);
        holder.bind(order,position);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public interface OrderListener{
        void onClickView(Order order, int position);
    }




}
