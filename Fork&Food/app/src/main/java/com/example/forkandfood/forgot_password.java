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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {

    EditText etreset_email;
    Button btnreset_password;

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_forgot_password);

        setUiViews();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mail Sending");
        btnreset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String reset_email = etreset_email.getText().toString();
                firebaseAuth.sendPasswordResetEmail(reset_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(forgot_password.this, "Password Reset Mail has been sent", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(forgot_password.this,MainActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(forgot_password.this, "Failed, Check the Mail Id", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    void setUiViews(){
        etreset_email = findViewById(R.id.et_reset_email);
        btnreset_password = findViewById(R.id.btn_reset_password);
    }
}