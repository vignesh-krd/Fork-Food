package com.example.forkandfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton dinein,takeaway;
    public static int choice;
    TextView tvdinein,tvtakeaway;
    DrawerLayout drawerLayout;
    ImageView nav_toggle,imgnext;
    NavigationView navigationView;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_home);

        setupUiViews();

        dinein_layout dineinLayout = new dinein_layout();
        dineinLayout.headerView(navigationView,home.this);
        
        dinein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = 1;
                dinein.setBackgroundResource(R.drawable.ic_choice_clicked);
                tvdinein.setTextColor(getColor(R.color.black));
                takeaway.setBackgroundResource(R.drawable.home_human_bg);
                tvtakeaway.setTextColor(getColor(R.color.grey));
            }
        });
        takeaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = 2;
                takeaway.setBackgroundResource(R.drawable.ic_choice_clicked);
                tvtakeaway.setTextColor(getColor(R.color.black));
                dinein.setBackgroundResource(R.drawable.home_human_bg);
                tvdinein.setTextColor(getColor(R.color.grey));
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        nav_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        try {
            imgnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(choice == 1){
                        startActivity(new Intent(home.this,dinein_layout.class));
                        finish();
                    }else if(choice == 2) {
                        startActivity(new Intent(home.this,takeaway_layout.class));
                        finish();
                    }else {
                        Toast.makeText(home.this, "Make a choice first", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupUiViews() {
        dinein = findViewById(R.id.img_dinein);
        takeaway = findViewById(R.id.img_takeaway);
        tvdinein = findViewById(R.id.tv_dinein);
        tvtakeaway = findViewById(R.id.tv_takeaway);
        drawerLayout = findViewById(R.id.dl_home);
        nav_toggle = findViewById(R.id.img_home_nav_toggle);
        navigationView = findViewById(R.id.home_nav_view);
        imgnext = findViewById(R.id.img_next);
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
                Toast.makeText(this, "Already in home", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_my_orders:{
                startActivity(new Intent(home.this,MyOrders.class));
                break;
            }
            case R.id.menu_logout:{
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(home.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}