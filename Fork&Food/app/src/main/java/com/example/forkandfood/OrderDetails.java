package com.example.forkandfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static javax.mail.internet.InternetAddress.*;

public class OrderDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ImageView nav_toggle,od_prof_pic;
    NavigationView navigationView;
    RecyclerView recyclerView;
    TextView tvod_cust_name,tvod_cust_mob_no,tvod_cust_date,tvod_cust_time,tvod_cust_category;
    TextView tvod_mob_no,tvod_cust_food_price,tvod_cust_tax,tvod_cust_total;

    Button btn_confirm_order;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    AdapterOrderedFoods adapterOrderedFoods;
    ArrayList<FoodList> list;

    int choice = 0;
    String path;
    String user_email;
    int order_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getColor(R.color.opyellow));
        setContentView(R.layout.activity_order_details);

        setupUiViews();

        dinein_layout dineinLayout = new dinein_layout();
        dineinLayout.getProfPic(OrderDetails.this,od_prof_pic);
        dineinLayout.headerView(navigationView,OrderDetails.this);

        navigationView.setNavigationItemSelectedListener(this);
        nav_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        choice = home.choice;
        if(choice == 1){
            path = dinein_layout.path;
            tvod_mob_no.setText("Members Count");
        } else if (choice == 2){
            path = takeaway_layout.path;
        }

        databaseReference = firebaseDatabase.getReference("Orders").child(firebaseAuth.getUid()).child(path).child("Details");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CustomerDetails customerDetails = snapshot.getValue(CustomerDetails.class);
                tvod_cust_name.setText(customerDetails.getName());
                tvod_cust_date.setText(customerDetails.getDate());
                tvod_cust_time.setText(customerDetails.getTime());
                if (choice == 1){
                    tvod_cust_category.setText(customerDetails.getOrder_category()+"\t"+customerDetails.getTable_chioce());
                    tvod_cust_mob_no.setText(customerDetails.getMembers());
                }
                else{
                    tvod_cust_category.setText(customerDetails.getOrder_category());
                    tvod_cust_mob_no.setText(customerDetails.getMobile_number());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                startActivity(new Intent(OrderDetails.this,Food_Menu.class));
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapterOrderedFoods = new AdapterOrderedFoods(this,list);
        recyclerView.setAdapter(adapterOrderedFoods);

        databaseReference = firebaseDatabase.getReference("Orders").child(firebaseAuth.getUid()).child(path).child("Foods");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FoodList foodList = dataSnapshot.getValue(FoodList.class);
                    list.add(foodList);
                }
                adapterOrderedFoods.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetails.this,"Exit Caused by Database Error",Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                displayTotalPrice();
            }
        },2000);
        /*while (AdapterOrderedFoods.total_price == 0 ){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayTotalPrice();
                }
            },500);
        }*/

        btn_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserRegister userRegister = snapshot.getValue(UserRegister.class);
                        String mail = userRegister.getMail();
                        readEmail(mail);
                        sendOdMail();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("Database error"+error);
                        Toast.makeText(OrderDetails.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setupUiViews() {
        drawerLayout = findViewById(R.id.dl_order_details);
        nav_toggle = findViewById(R.id.img_order_details_nav_toggle);
        navigationView = findViewById(R.id.order_details_nav_view);
        recyclerView = findViewById(R.id.rv_ordered_foods);
        tvod_cust_name = findViewById(R.id.tv_order_details_cust_name);
        tvod_cust_mob_no = findViewById(R.id.tv_order_details_cust_mob_no);
        tvod_cust_date = findViewById(R.id.tv_order_details_cust_date);
        tvod_cust_time = findViewById(R.id.tv_order_details_cust_time);
        tvod_mob_no = findViewById(R.id.tv_order_details_mob_no);
        tvod_cust_food_price = findViewById(R.id.tv_cust_food_price);
        tvod_cust_tax = findViewById(R.id.tv_cust_tax);
        tvod_cust_total = findViewById(R.id.tv_cust_total);
        od_prof_pic = findViewById(R.id.img_order_details_prof_pic);
        btn_confirm_order = findViewById(R.id.btn_order_details_confirm_order);
        tvod_cust_category = findViewById(R.id.tv_order_details_cust_category);
    }

    public void displayTotalPrice(){
        int food_price = AdapterOrderedFoods.total_price;
        int tax = ((food_price*5)/100);
        int total_payable = food_price+tax;
        tvod_cust_food_price.setText("₹"+String.valueOf(food_price));
        tvod_cust_tax.setText("₹"+tax);
        tvod_cust_total.setText("₹"+total_payable);
    }

    private void getOrderNumber(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = firebaseDatabase.getReference("OrderNumber");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int od_num = snapshot.child("order_num").getValue(Integer.class);
                order_num = od_num;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error on getting order number");
            }
        });
    }

    private void sendOdMail(){
        final String mail = getString(R.string.email);
        final String password = getString(R.string.password);
        String email_context;
        ArrayList<String> orderedFoods = new ArrayList<>();

        for (int i=0;i<list.size();i++) {
            orderedFoods.add("\n\t"+list.get(i).getDishname()+"    "+list.get(i).getDishquantity()+"\t₹"+(list.get(i).getDishquantity()*list.get(i).getDishprice()));
        }

        String odFoods = orderedFoods.toString().replace("["," ");
        //odFoods.replace("[","").replace("]","");

        email_context = ("Order Details \n\tName: "+tvod_cust_name.getText().toString()+"\n\t"+tvod_mob_no.getText().toString()+": "
                        +tvod_cust_mob_no.getText().toString()+"\n\tDate: "+tvod_cust_date.getText().toString()+"\n\tTime: "
                        +tvod_cust_time.getText().toString()+"\n\tOrder Category: "+tvod_cust_category.getText().toString()+"\n\nOrdered Foods\n"
                        +odFoods+"\n\nBill Details \n\tFood Price: "+tvod_cust_food_price.getText().toString()+"\n\tTaxes and Charges: "
                        +tvod_cust_tax.getText().toString()+"\n\tTotal Payable: "+tvod_cust_total.getText().toString());

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail,password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail));
            Address[] address_list = new Address[] {
                    new InternetAddress(user_email),new InternetAddress("forkandfood.billing@gmail.com")
            };
            message.setRecipients(Message.RecipientType.TO, address_list);
            message.setSubject("Fork&Food Order Confirmation");
            message.setText(email_context);

            new SendEmail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void readEmail (String mail){
        user_email = mail;
    }

    private class SendEmail extends AsyncTask<Message,String,String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getOrderNumber();
            progressDialog = ProgressDialog.show(OrderDetails.this,"Please wait","Order is Processing",true,false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                System.out.println("Transport error"+e);
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("Success")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetails.this);
                builder.setCancelable(false);
                builder.setTitle("Food Ordered");
                order_num++;
                builder.setMessage("Your Order Number "+order_num);
                databaseReference = firebaseDatabase.getReference("OrderNumber");
                databaseReference.child("order_num").setValue(order_num);
                databaseReference = firebaseDatabase.getReference("Orders").child(firebaseAuth.getUid()).child(path).child("Details");
                databaseReference.child("order_num").setValue(order_num);
                databaseReference.child("total_price").setValue(tvod_cust_total.getText().toString());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(OrderDetails.this,home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                builder.show();
            }else{
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        }
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
                startActivity(new Intent(OrderDetails.this,MyOrders.class));
                break;
            }
            case R.id.menu_logout:{
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(OrderDetails.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}