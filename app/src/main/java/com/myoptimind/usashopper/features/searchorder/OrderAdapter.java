package com.myoptimind.usashopper.features.searchorder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.Order;

import java.util.Currency;
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

        TextView tvLabel,tvNum,tvRequestDate,tvShopperName,tvQuantity,tvTotalPrice,tvStatus;
        Button btnView;
        OrderListener mOrderListener;
        Context mContext;

        public OrderViewHolder(@NonNull View itemView,OrderListener orderListener) {

            super(itemView);

            tvLabel       = itemView.findViewById(R.id.tv_order_label);
            tvNum         = itemView.findViewById(R.id.tv_order_num);
            tvRequestDate = itemView.findViewById(R.id.tv_order_request_date);
            tvShopperName = itemView.findViewById(R.id.tv_order_shopper_name);
            tvQuantity    = itemView.findViewById(R.id.tv_order_quantity);
            tvTotalPrice  = itemView.findViewById(R.id.tv_order_price);
            tvStatus      = itemView.findViewById(R.id.tv_order_status);

            btnView       = itemView.findViewById(R.id.btn_view);

            mOrderListener = orderListener;
            mContext = itemView.getContext();
        }

        public void bind(final Order order, final int position){

            tvLabel.setText(order.getLabel());
            tvNum.setText(order.getOrderId());
            tvRequestDate.setText(
                    mContext.getString(
                            R.string.order_requested_on,
                            order.getRequestedDate()
                    )
            );

            tvShopperName.setText(
                    mContext.getString(
                            R.string.order_requested_by,
                            order.getShopperName()
                    )
            );
            tvQuantity.setText(
                    mContext.getResources().getQuantityString(
                            R.plurals.item_plurals,
                            Integer.parseInt(order.getQuantity()),
                            Integer.parseInt(order.getQuantity())
                    )
            );

            tvTotalPrice.setText("₱" + Double.parseDouble(order.getPrice()));
            tvStatus.setText(order.getFormattedStatus());
            tvStatus.setTextColor(Color.parseColor(order.getStatusColorHex()));

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
