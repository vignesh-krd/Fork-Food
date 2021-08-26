package com.example.forkandfood;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMyOrders extends RecyclerView.Adapter<AdapterMyOrders.ListViewHolder> {

    Context context;
    ArrayList<MyOrdersList> list;

    public AdapterMyOrders(Context context, ArrayList<MyOrdersList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_orders_list,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        MyOrdersList myOrdersList = list.get(position);
        holder.tvorder_number.setText("Order Number - "+myOrdersList.getOrder_num());
        holder.tvorder_category.setText(myOrdersList.getOrder_category());
        holder.tvdate_of_order.setText(myOrdersList.getDate());
        holder.tvtime_of_order.setText(myOrdersList.getTime());
        holder.tvpaid_price.setText(myOrdersList.getTotal_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tvorder_category,tvdate_of_order,tvtime_of_order,tvorder_number,tvpaid_price;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvorder_category = itemView.findViewById(R.id.tv_myod_order_category);
            tvdate_of_order = itemView.findViewById(R.id.tv_myod_dof);
            tvtime_of_order = itemView.findViewById(R.id.tv_myod_tof);
            tvorder_number = itemView.findViewById(R.id.tv_myod_order_number);
            tvpaid_price = itemView.findViewById(R.id.tv_myod_paid_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ExpandedMyOrders.class);
                    intent.putExtra("ItemPosition",getAdapterPosition());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
