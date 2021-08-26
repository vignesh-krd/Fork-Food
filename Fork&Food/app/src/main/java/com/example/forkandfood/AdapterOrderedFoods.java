package com.example.forkandfood;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class AdapterOrderedFoods extends RecyclerView.Adapter<AdapterOrderedFoods.ListViewHolder> {

    Context context;
    public ArrayList<FoodList> list;
    public static int total_price = 0;
    boolean first = true;

    public AdapterOrderedFoods(Context context, ArrayList<FoodList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ordered_food_list,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        total_price = getTotal_price(list);
        FoodList foodList = list.get(position);
        holder.tvod_dishname.setText(foodList.getDishname());
        holder.tvod_dishquantity.setText(String.valueOf(foodList.getDishquantity()));
        holder.tvod_dishprice.setText("₹"+String.valueOf(foodList.getDishquantity()*foodList.getDishprice()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tvod_dishname,tvod_dishquantity,tvod_dishprice;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvod_dishname = itemView.findViewById(R.id.tv_od_dishname);
            tvod_dishprice = itemView.findViewById(R.id.tv_od_dishprice);
            tvod_dishquantity = itemView.findViewById(R.id.tv_od_dishquantity);
        }
    }
    public int getTotal_price (ArrayList<FoodList> items){
        int total = 0;
        for(int i=0;i<items.size();i++){
            total += (items.get(i).getDishquantity())*(items.get(i).getDishprice());
        }
        return total;
    }
}
