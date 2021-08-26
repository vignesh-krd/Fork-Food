package com.example.forkandfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.MenuViewHolder> {

    Context context;
    ArrayList<dish_model_class> list;

    String dishname;
    int dishprice;

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public AdapterMenu(Context context, ArrayList<dish_model_class> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_items,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        dish_model_class dishModelClass = list.get(position);
        holder.tvdishname.setText(dishModelClass.getDishname());
        holder.tvdishprice.setText("₹"+ dishModelClass.getDishprice());
        Glide.with(context).load(list.get(position).getImageurl()).into(holder.imageurl);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView tvdishname,tvdishprice;
        ImageView imageurl;
        int choice = 0,item_count = 0;
        String path = "Test";
        Button add;
        ImageView plus,minus;
        TextView count;
        boolean add_flag = true;

        FirebaseAuth firebaseAuth;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            tvdishname = itemView.findViewById(R.id.tv_food_dishname);
            tvdishprice = itemView.findViewById(R.id.tv_food_dishprice);
            imageurl = itemView.findViewById(R.id.img_food_imageurl);
            add = itemView.findViewById(R.id.btn_item_add);
            plus = itemView.findViewById(R.id.btn_item_plus);
            minus = itemView.findViewById(R.id.btn_item_minus);
            count = itemView.findViewById(R.id.tv_item_count);
                        
            plus.setVisibility(View.INVISIBLE);
            minus.setVisibility(View.INVISIBLE);
            count.setVisibility(View.INVISIBLE);

            choice = home.choice;
            if(choice == 1){
                path = dinein_layout.path;
            } else if (choice == 2){
                path = takeaway_layout.path;
            }

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add.setVisibility(View.INVISIBLE);
                    plus.setVisibility(View.VISIBLE);
                    minus.setVisibility(View.VISIBLE);
                    item_count++;
                    count.setVisibility(View.VISIBLE);
                    dish_model_class dishModelClass = list.get(getAdapterPosition());
                    dishprice = dishModelClass.getDishprice();
                    dishname = dishModelClass.getDishname();
                    updateFoodList(dishname,dishprice,item_count);
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_count++;
                    dish_model_class dishModelClass = list.get(getAdapterPosition());
                    dishprice = dishModelClass.getDishprice();
                    dishname = dishModelClass.getDishname();
                    updateFoodList(dishname,dishprice,item_count);
                    count.setText(String.valueOf(item_count));
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item_count > 1) {
                        item_count--;
                        dish_model_class dishModelClass = list.get(getAdapterPosition());
                        dishprice = dishModelClass.getDishprice();
                        dishname = dishModelClass.getDishname();
                        updateFoodList(dishname,dishprice,item_count);
                        count.setText(String.valueOf(item_count));
                    } else {
                        add_flag = false;
                        dish_model_class dishModelClass = list.get(getAdapterPosition());
                        dishprice = dishModelClass.getDishprice();
                        dishname = dishModelClass.getDishname();
                        updateFoodList(dishname,dishprice,item_count);
                        add.setVisibility(View.VISIBLE);
                        plus.setVisibility(View.INVISIBLE);
                        minus.setVisibility(View.INVISIBLE);
                        count.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        public void updateFoodList(String dishname,int dishprice,int dishquantity){
            FoodList foodList = new FoodList(dishname,dishprice,dishquantity);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Orders").child(firebaseAuth.getUid()).child(path).child("Foods").child(dishname);
            if (add_flag)
                databaseReference.setValue(foodList);
            else {
                databaseReference.removeValue();
                add_flag = true;
            }
        }
    }
}
