package com.example.forkandfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {
    EditText etreg_name, etreg_mob_no, etreg_mail, etreg_password, etreg_conf_password;
    CircleImageView imgprofile_pic;
    static int PICK_IMAGE = 1;
    Uri prof_pic_path;
    Button btnreg_submit;
    TextInputLayout tilreg_password, tilregconf_password, tilreg_mail,tilreg_mob_no;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    ProgressDialog progressDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            prof_pic_path = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), prof_pic_path);
                imgprofile_pic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_register);
        setUiViews();
        btnreg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseStorage = FirebaseStorage.getInstance();
                storageReference = firebaseStorage.getReference();
                setSubmit();
            }
        });
        imgprofile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });
    }

    void setUiViews() {
        etreg_name = findViewById(R.id.et_name);
        etreg_mail = findViewById(R.id.et_mail);
        etreg_mob_no = findViewById(R.id.et_mob_no);
        etreg_password = findViewById(R.id.et_reg_pass);
        etreg_conf_password = findViewById(R.id.et_conf_reg_pass);
        imgprofile_pic = findViewById(R.id.img_profile_pic);
        btnreg_submit = findViewById(R.id.btn_reg_submit);
        tilreg_password = findViewById(R.id.til_reg_pass);
        tilregconf_password = findViewById(R.id.til_conf_reg_pass);
        tilreg_mail = findViewById(R.id.til_mail);
        tilreg_mob_no = findViewById(R.id.til_mob_no);
    }

    void setSubmit() {
        tilreg_password.setErrorEnabled(false);
        tilregconf_password.setErrorEnabled(false);
        tilreg_mail.setErrorEnabled(false);
        String reg_name = etreg_name.getText().toString();
        String reg_mail = etreg_mail.getText().toString();
        String reg_mob_no = etreg_mob_no.getText().toString();
        String reg_password = etreg_password.getText().toString();
        String reg_conf_password = etreg_conf_password.getText().toString();
        if (reg_name.isEmpty() || reg_mail.isEmpty() || reg_mob_no.isEmpty() || reg_password.isEmpty() || reg_conf_password.isEmpty()) {
            Toast.makeText(Register.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            if (reg_mob_no.length() == 10) {
                if (Patterns.EMAIL_ADDRESS.matcher(reg_mail).matches()) {
                    if (reg_password.equals(reg_conf_password)) {
                        if (reg_password.length() >= 6) {
                            try {
                                progressDialog = new ProgressDialog(Register.this);
                                progressDialog.setMessage("Profile Creating");
                                progressDialog.show();
                                firebaseAuth.createUserWithEmailAndPassword(reg_mail, reg_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            if (prof_pic_path != null){
                                                StorageReference imgRef = storageReference.child("User Profile Pictures").child(firebaseAuth.getUid()).child("ProfilePic");
                                                UploadTask uploadTask = null;
                                                try {
                                                    uploadTask = imgRef.putFile(prof_pic_path);
                                                } catch (Exception e) {
                                                    firebaseAuth.getCurrentUser().delete();
                                                    startActivity(new Intent(Register.this,Register.class));
                                                    finish();
                                                    e.printStackTrace();
                                                }
                                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        System.out.println("Image upload failed "+e);
                                                        firebaseAuth.getCurrentUser().delete();
                                                        progressDialog.dismiss();
                                                        Toast.makeText(Register.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        UserRegister userRegister = new UserRegister(reg_name, reg_mail, reg_mob_no);
                                                        DatabaseReference dbRef = firebaseDatabase.getReference("Users");
                                                        dbRef.child(firebaseAuth.getUid()).setValue(userRegister).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    sendVerificationEmail();
                                                                    Toast.makeText(Register.this, "Profile Created", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    firebaseAuth.getCurrentUser().delete();
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(Register.this, "Failed Try again", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            } else {
                                                UserRegister userRegister = new UserRegister(reg_name, reg_mail, reg_mob_no);
                                                DatabaseReference dbRef = firebaseDatabase.getReference("Users");
                                                dbRef.child(firebaseAuth.getUid()).setValue(userRegister).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            sendVerificationEmail();
                                                            Toast.makeText(Register.this, "Profile Created", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            firebaseAuth.getCurrentUser().delete();
                                                            progressDialog.dismiss();
                                                            Toast.makeText(Register.this, "Failed Try again", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(Register.this, "Failed Try again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            tilreg_password.setErrorEnabled(true);
                            tilreg_password.setError("Password too short, Minimum 6 Characters Required");
                        }
                    } else {
                        tilregconf_password.setErrorEnabled(true);
                        tilregconf_password.setError("Password Didn't match");
                    }
                } else {
                    tilreg_mail.setErrorEnabled(true);
                    tilreg_mail.setError("Not a valid email");
                }
            } else {
                tilreg_mob_no.setErrorEnabled(true);
                tilreg_mob_no.setError("Invalid Mobile Number");
            }
        }
    }

    void sendVerificationEmail() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Verification Email sent", Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                    startActivity(new Intent(Register.this, MainActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    firebaseAuth.signOut();
                    Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

