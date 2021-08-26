package com.example.forkandfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class dinein_layout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText etdinein_date, etdinein_time,etdinein_name;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView dinein_prof_pic;
    ImageView nav_toggle, btn_dinein_plus, btn_dinein_minus,nav_prof_pic;
    ImageView imgtable_1, imgtable_2, imgtable_3, imgtable_4, imgtable_5, imgtable_6;
    TextView tvmember_count,nav_name;
    Button btndinein_proceed;

    int member_count = 1;
    String table_choice = "none";
    public static String path;
    boolean t1 = true, t2 = true, t3 = true, t4 = true, t5 = true, t6 = true;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    public DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getColor(R.color.opyellow));
        setContentView(R.layout.activity_dinein_layout);

        setupUiViews();
        headerView(navigationView,dinein_layout.this);
        getProfPic(this,dinein_prof_pic);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        navigationView.setNavigationItemSelectedListener(this);
        nav_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        btn_dinein_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (member_count < 24) {
                    member_count++;
                    tvmember_count.setText(String.valueOf(member_count));
                } else {
                    Toast.makeText(dinein_layout.this, "Maximum Limit Reached", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_dinein_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (member_count > 1) {
                    member_count--;
                    tvmember_count.setText(String.valueOf(member_count));
                }
            }
        });

        etdinein_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog(dinein_layout.this, etdinein_date);
            }
        });

        etdinein_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog(dinein_layout.this, etdinein_time);
            }
        });

        imgtable_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t1) {
                    table_choice = "Table 1";
                    imgtable_1.setBackgroundResource(R.drawable.table_stroke);
                    t1 = false;
                } else {
                    imgtable_1.setBackgroundResource(0);
                    t1 = true;
                }
            }
        });

        imgtable_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t2) {
                    table_choice = "Table 2";
                    imgtable_2.setBackgroundResource(R.drawable.table_stroke);
                    t2 = false;
                } else {
                    imgtable_2.setBackgroundResource(0);
                    t2 = true;
                }
            }
        });

        imgtable_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t3) {
                    table_choice = "Table 3";
                    imgtable_3.setBackgroundResource(R.drawable.table_stroke);
                    t3 = false;
                } else {
                    imgtable_3.setBackgroundResource(0);
                    t3 = true;
                }
            }
        });

        imgtable_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t4) {
                    table_choice = "Table 4";
                    imgtable_4.setBackgroundResource(R.drawable.table_stroke);
                    t4 = false;
                } else {
                    imgtable_4.setBackgroundResource(0);
                    t4 = true;
                }
            }
        });

        imgtable_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t5) {
                    table_choice = "Table 5";
                    imgtable_5.setBackgroundResource(R.drawable.table_stroke);
                    t5 = false;
                } else {
                    imgtable_5.setBackgroundResource(0);
                    t5 = true;
                }
            }
        });

        imgtable_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t6) {
                    table_choice = "Table 6";
                    imgtable_6.setBackgroundResource(R.drawable.table_stroke);
                    t6 = false;
                } else {
                    imgtable_6.setBackgroundResource(0);
                    t6 = true;
                }
            }
        });
        btndinein_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDineinDetails();
            }
        });
    }

    public void headerView(NavigationView navigationView, Context context) {
        View header = navigationView.getHeaderView(0);
        nav_name = header.findViewById(R.id.tv_nav_name);
        nav_prof_pic = header.findViewById(R.id.img_nav_profile_pic);

        getProfPic(context,nav_prof_pic);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserRegister userRegister = snapshot.getValue(UserRegister.class);
                nav_name.setText(userRegister.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                nav_name.setText("Error");
            }
        });
    }

    void getProfPic(Context context,ImageView imageView) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("User Profile Pictures").child(firebaseAuth.getUid()).child("ProfilePic");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(imageView);
            }
        });
    }

    void setupUiViews() {
        etdinein_name = findViewById(R.id.et_dinein_name);
        etdinein_date = findViewById(R.id.et_dinein_date);
        etdinein_time = findViewById(R.id.et_dinein_time);
        drawerLayout = findViewById(R.id.dl_dinein);
        dinein_prof_pic = findViewById(R.id.img_dinein_prof_pic);
        navigationView = findViewById(R.id.dinein_nav_view);
        nav_toggle = findViewById(R.id.img_dinein_nav_toggle);
        btn_dinein_plus = findViewById(R.id.btn_dinein_plus);
        btn_dinein_minus = findViewById(R.id.btn_dinein_minus);
        tvmember_count = findViewById(R.id.tv_members_count);
        imgtable_1 = findViewById(R.id.img_table_1);
        imgtable_2 = findViewById(R.id.img_table_2);
        imgtable_3 = findViewById(R.id.img_table_3);
        imgtable_4 = findViewById(R.id.img_table_4);
        imgtable_5 = findViewById(R.id.img_table_5);
        imgtable_6 = findViewById(R.id.img_table_6);
        btndinein_proceed = findViewById(R.id.btn_dinein_proceed);
    }

    public void dateDialog(Context context, EditText et_date) {
        final Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                CharSequence dateCharSequence = DateFormat.format("dd/MM/yyyy", calendar1);
                et_date.setText(dateCharSequence);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void timeDialog(Context context, EditText et_time) {
        Calendar calendar = Calendar.getInstance();

        int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar.get(Calendar.MINUTE);

        boolean hourformat = DateFormat.is24HourFormat(context);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY, hour);
                calendar1.set(Calendar.MINUTE, minute);
                CharSequence timeCharSequence = DateFormat.format("hh:mm a", calendar1);
                et_time.setText(timeCharSequence);
            }
        }, HOUR, MINUTE, hourformat);
        timePickerDialog.show();
    }

    void sendDineinDetails() {
        try {
            String dinein_name = etdinein_name.getText().toString();
            String dinein_date = etdinein_date.getText().toString();
            String dinein_time = etdinein_time.getText().toString();
            String members = tvmember_count.getText().toString();

            if (!(dinein_name.isEmpty() || dinein_date.isEmpty() || dinein_time.isEmpty() || table_choice.equals("none"))) {
                CustomerDetails customerDetails = new CustomerDetails(dinein_name,dinein_date,dinein_time,members,table_choice,"Dine In");

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                path = "Dinein "+dinein_date+"_"+dinein_time;
                path = path.replaceAll("/","_");
                DatabaseReference databaseReference = firebaseDatabase.getReference("Orders").child(firebaseAuth.getUid()).child(path).child("Details");
                databaseReference.setValue(customerDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(dinein_layout.this, Food_Menu.class));
                    }
                });
            } else {
                Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            startActivity(new Intent(this, home.class));
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home: {
                startActivity(new Intent(this, home.class));
                finish();
                break;
            }
            case R.id.menu_my_orders:{
                startActivity(new Intent(dinein_layout.this,MyOrders.class));
                break;
            }
            case R.id.menu_logout: {
                firebaseAuth.signOut();
                Intent intent = new Intent(dinein_layout.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}