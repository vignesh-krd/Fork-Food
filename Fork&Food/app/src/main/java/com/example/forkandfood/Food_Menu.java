package com.example.forkandfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Food_Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DatabaseReference databaseReference;
    AdapterMenu adapterMenu;
    ArrayList<dish_model_class> list;
    Button btnstarters,btnmaincourse,btnsides,btndesserts;
    Button btnfood_menu_proceed;
    ImageView nav_toggle,food_menu_prof_pic;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getColor(R.color.opyellow));
        setContentView(R.layout.activity_food_menu);

        setupUiViews();

        dinein_layout dineinLayout = new dinein_layout();
        dineinLayout.getProfPic(this,food_menu_prof_pic);
        dineinLayout.headerView(navigationView,Food_Menu.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        list = new ArrayList<>();
        adapterMenu = new AdapterMenu(this,list);
        recyclerView.setAdapter(adapterMenu);

        btnstarters.setBackgroundResource(R.drawable.menu_choices_click);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodMenu/Starters");
        menuCategoryChoice();

        btnstarters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnstarters.setBackgroundResource(R.drawable.menu_choices_click);
                btnmaincourse.setBackgroundResource(R.drawable.menu_choices);
                btnsides.setBackgroundResource(R.drawable.menu_choices);
                btndesserts.setBackgroundResource(R.drawable.menu_choices);
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodMenu/Starters");
                recyclerView.setAdapter(adapterMenu);
                menuCategoryChoice();
            }
        });
        btnmaincourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnmaincourse.setBackgroundResource(R.drawable.menu_choices_click);
                btnstarters.setBackgroundResource(R.drawable.menu_choices);
                btnsides.setBackgroundResource(R.drawable.menu_choices);
                btndesserts.setBackgroundResource(R.drawable.menu_choices);
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodMenu/Maincourse");
                recyclerView.setAdapter(adapterMenu);
                menuCategoryChoice();
            }
        });
        btnsides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnsides.setBackgroundResource(R.drawable.menu_choices_click);
                btnstarters.setBackgroundResource(R.drawable.menu_choices);
                btnmaincourse.setBackgroundResource(R.drawable.menu_choices);
                btndesserts.setBackgroundResource(R.drawable.menu_choices);
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodMenu/Sides");
                recyclerView.setAdapter(adapterMenu);
                menuCategoryChoice();
            }
        });
        btndesserts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btndesserts.setBackgroundResource(R.drawable.menu_choices_click);
                btnstarters.setBackgroundResource(R.drawable.menu_choices);
                btnmaincourse.setBackgroundResource(R.drawable.menu_choices);
                btnsides.setBackgroundResource(R.drawable.menu_choices);
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodMenu/Desserts");
                recyclerView.setAdapter(adapterMenu);
                menuCategoryChoice();
            }
        });
        btnfood_menu_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Food_Menu.this,OrderDetails.class));
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        nav_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void menuCategoryChoice() {
        list.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dish_model_class dishModelClass = dataSnapshot.getValue(dish_model_class.class);
                    list.add(dishModelClass);
                }
                adapterMenu.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Food_Menu.this,"Exit Caused by Database Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUiViews() {
        recyclerView = findViewById(R.id.rv_menu);
        btnstarters = findViewById(R.id.btn_starters);
        btnmaincourse = findViewById(R.id.btn_maincourse);
        btnsides = findViewById(R.id.btn_sides);
        btndesserts = findViewById(R.id.btn_desserts);
        nav_toggle = findViewById(R.id.img_food_menu_nav_toggle);
        navigationView = findViewById(R.id.food_menu_nav_view);
        drawerLayout = findViewById(R.id.dl_food_menu);
        food_menu_prof_pic = findViewById(R.id.img_food_menu_prof_pic);
        btnfood_menu_proceed = findViewById(R.id.btn_food_menu_proceed);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else{
            super.onBackPressed();
        }
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
                startActivity(new Intent(Food_Menu.this,MyOrders.class));
                break;
            }
            case R.id.menu_logout:{
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(Food_Menu.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}