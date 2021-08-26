package com.example.forkandfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    EditText etusername,etpassword;
    Button btnlogin,btnsignup;
    TextView tvforgot_password;

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        setUiViews();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this,home.class));
            finish();
        }
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });
        tvforgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,forgot_password.class));
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLogin();
            }
        });
    }
    void setUiViews(){
        etusername = findViewById(R.id.et_username);
        etpassword = findViewById(R.id.et_password);
        btnlogin = findViewById(R.id.btn_login);
        btnsignup = findViewById(R.id.btn_signup);
        tvforgot_password = findViewById(R.id.tv_forget_password);
    }
    void setLogin(){
        String username = etusername.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this,"Enter the values",Toast.LENGTH_SHORT).show();
        }else{
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Verifying");
            progressDialog.show();
            try {
                firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            checkEmailVerfication();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Credentials Wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    void checkEmailVerfication(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        boolean verify_flag = firebaseUser.isEmailVerified();
        if (verify_flag){
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,home.class));
            finish();
        }else{
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Email hasn't Verified, Verify to Continue", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
    }
}