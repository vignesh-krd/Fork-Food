package com.example.forkandfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forkandfood.dinein_layout.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class takeaway_layout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText ettakeaway_date,ettakeaway_time,ettakeaway_name,ettakeaway_mob_no;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView nav_toggle,takeaway_prof_pic;
    Button btntakeaway_proceed;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getColor(R.color.opyellow));
        setContentView(R.layout.activity_takeaway_layout);

        setupUiViews();

        navigationView.setNavigationItemSelectedListener(this);
        nav_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        dinein_layout dineinLayout = new dinein_layout();
        dineinLayout.getProfPic(this,takeaway_prof_pic);
        dineinLayout.headerView(navigationView,takeaway_layout.this);

        try {
            ettakeaway_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dineinLayout.dateDialog(takeaway_layout.this,ettakeaway_date);
                }
            });
            ettakeaway_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dineinLayout.timeDialog(takeaway_layout.this,ettakeaway_time);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        btntakeaway_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTakeawayDetails();
            }
        });
    }
    void setupUiViews(){
        ettakeaway_name = findViewById(R.id.et_takeaway_name);
        ettakeaway_mob_no = findViewById(R.id.et_takeaway_mob_no);
        ettakeaway_date = findViewById(R.id.et_takeaway_date);
        ettakeaway_time = findViewById(R.id.et_takeaway_time);
        drawerLayout = findViewById(R.id.dl_takeaway);
        navigationView = findViewById(R.id.takeaway_nav_view);
        nav_toggle = findViewById(R.id.img_takeaway_nav_toggle);
        btntakeaway_proceed = findViewById(R.id.btn_takeaway_proceed);
        takeaway_prof_pic = findViewById(R.id.img_takeaway_prof_pic);
    }
    void sendTakeawayDetails(){
        try {
            String takeaway_name = ettakeaway_name.getText().toString();
            String takeaway_mob_no = ettakeaway_mob_no.getText().toString();
            String takeaway_date = ettakeaway_date.getText().toString();
            String takeaway_time = ettakeaway_time.getText().toString();

            if (!(takeaway_name.isEmpty() || takeaway_mob_no.isEmpty() || takeaway_date.isEmpty() || takeaway_time.isEmpty())) {
                if (takeaway_mob_no.length() == 10 ) {
                    CustomerDetails customerDetails = new CustomerDetails(takeaway_name,takeaway_mob_no,takeaway_date,takeaway_time,"Takeaway");
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    path = "Takeaway "+takeaway_date+"_"+takeaway_time;
                    path = path.replaceAll("/","_");
                    databaseReference = firebaseDatabase.getReference("Orders").child(firebaseAuth.getUid()).child(path);
                    databaseReference.child("Details").setValue(customerDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(takeaway_layout.this,Food_Menu.class));
                        }
                    });
                } else {
                    Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else{
            startActivity(new Intent(this,home.class));
            finish();
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
                startActivity(new Intent(takeaway_layout.this,MyOrders.class));
                break;
            }
            case R.id.menu_logout:{
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(takeaway_layout.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);;
                break;
            }
        }
        return true;
    }
}