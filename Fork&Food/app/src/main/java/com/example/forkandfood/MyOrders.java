package com.example.forkandfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ImageView nav_toggle,my_orders_prof_pic;
    NavigationView navigationView;
    RecyclerView recyclerView;
    ArrayList<MyOrdersList> list;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getColor(R.color.opyellow));
        setContentView(R.layout.activity_my_orders);

        setupUiViews();

        dinein_layout dineinLayout = new dinein_layout();
        dineinLayout.headerView(navigationView,MyOrders.this);
        dineinLayout.getProfPic(MyOrders.this,my_orders_prof_pic);

        navigationView.setNavigationItemSelectedListener(this);
        nav_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        AdapterMyOrders adapterMyOrders = new AdapterMyOrders(this,list);
        recyclerView.setAdapter(adapterMyOrders);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MyOrdersList myOrdersList = dataSnapshot.child("Details").getValue(MyOrdersList.class);
                    list.add(myOrdersList);
                }

                adapterMyOrders.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error on getting items"+error);
            }
        });
    }

    private void setupUiViews(){
        drawerLayout = findViewById(R.id.dl_my_orders);
        navigationView = findViewById(R.id.my_orders_nav_view);
        nav_toggle = findViewById(R.id.img_my_orders_nav_toggle);
        my_orders_prof_pic = findViewById(R.id.img_my_orders_prof_pic);
        recyclerView = findViewById(R.id.rv_my_orders);
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
                Toast.makeText(this, "Already in My Orders", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_logout:{
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(MyOrders.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}