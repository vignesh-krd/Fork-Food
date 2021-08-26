package com.example.forkandfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpandedMyOrders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ImageView nav_toggle,exp_my_orders_prof_pic;
    NavigationView navigationView;
    TextView tvexp_myod_order_number,tvexp_myod_category,tvexp_myod_dof,tvexp_myod_tof,tvexp_myod_price;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    AdapterOrderedFoods adapterOrderedFoods;
    ArrayList<FoodList> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getColor(R.color.opyellow));
        setContentView(R.layout.activity_expanded_my_orders);

        setupUiViews();

        dinein_layout dineinLayout = new dinein_layout();
        dineinLayout.headerView(navigationView,ExpandedMyOrders.this);
        dineinLayout.getProfPic(ExpandedMyOrders.this,exp_my_orders_prof_pic);

        navigationView.setNavigationItemSelectedListener(this);
        nav_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        Bundle intent_extras = getIntent().getExtras();
        int item_pos = intent_extras.getInt("ItemPosition");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ArrayList<MyOrdersList> details_list = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("Orders").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MyOrdersList myOrdersList = dataSnapshot.child("Details").getValue(MyOrdersList.class);
                    details_list.add(myOrdersList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("details data fetching error"+error);
            }
        });
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MyOrdersList myOrdersList1 = details_list.get(item_pos);
                tvexp_myod_order_number.setText("Order Number - "+myOrdersList1.getOrder_num());
                tvexp_myod_category.setText(myOrdersList1.getOrder_category());
                tvexp_myod_dof.setText(myOrdersList1.getDate());
                tvexp_myod_tof.setText(myOrdersList1.getTime());
                tvexp_myod_price.setText(myOrdersList1.getTotal_price());
            }
        },500);

        // Listing Food details

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapterOrderedFoods = new AdapterOrderedFoods(this,list);
        recyclerView.setAdapter(adapterOrderedFoods);

        databaseReference = firebaseDatabase.getReference("Orders").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (item_pos == i) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Foods").getChildren()){
                            FoodList foodList = dataSnapshot1.getValue(FoodList.class);
                            list.add(foodList);
                        }
                        adapterOrderedFoods.notifyDataSetChanged();
                        break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Food data fetching error"+error);
            }
        });
    }

    private void setupUiViews(){
        drawerLayout = findViewById(R.id.dl_exp_myod);
        nav_toggle = findViewById(R.id.img_exp_myod_nav_toggle);
        exp_my_orders_prof_pic = findViewById(R.id.img_exp_myod_prof_pic);
        navigationView = findViewById(R.id.exp_myod_nav_view);
        tvexp_myod_order_number = findViewById(R.id.tv_exp_myod_order_number);
        tvexp_myod_category = findViewById(R.id.tv_exp_myod_order_category);
        tvexp_myod_dof = findViewById(R.id.tv_exp_myod_dof);
        tvexp_myod_tof = findViewById(R.id.tv_exp_myod_tof);
        tvexp_myod_price = findViewById(R.id.tv_exp_myod_paid_price);
        recyclerView = findViewById(R.id.rv_exp_myod_foods);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home: {
                startActivity(new Intent(this,home.class));
                finish();
                break;
            }
            case R.id.menu_my_orders:{
                startActivity(new Intent(ExpandedMyOrders.this,MyOrders.class));
                finish();
                break;
            }
            case R.id.menu_logout:{
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(ExpandedMyOrders.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}