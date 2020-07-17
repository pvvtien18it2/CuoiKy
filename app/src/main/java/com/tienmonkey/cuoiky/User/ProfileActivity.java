package com.tienmonkey.cuoiky.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tienmonkey.cuoiky.Admin.HomeAdminActivity;
import com.tienmonkey.cuoiky.MainActivity;
import com.tienmonkey.cuoiky.Module.Prevalent;
import com.tienmonkey.cuoiky.Module.Users;
import com.tienmonkey.cuoiky.ProductDetailActivity;
import com.tienmonkey.cuoiky.R;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {
    private EditText edName, edPhone, edAdd;
    private TextView  tvBack, tvUpdate, tvAdmin;
    private String name, phone, address, password, admin;
    private DatabaseReference userRef;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Paper.init(this);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        edAdd = findViewById(R.id.edAdd);

        tvBack = findViewById(R.id.tvBack);
        tvUpdate = findViewById(R.id.tvUpdate);
        tvAdmin = findViewById(R.id.tvAdmin);

        admin = Prevalent.crurrentOnlineUser.getAdmin();
        if (admin.equals("1")){
            tvAdmin.setText("You are admin!");
        }else{
            tvAdmin.setText("You are user!");
        }

        name = Prevalent.crurrentOnlineUser.getName();
        phone = Prevalent.crurrentOnlineUser.getPhone();
        address = Prevalent.crurrentOnlineUser.getAddress();
        password = Prevalent.crurrentOnlineUser.getPassword();

        edName.setText(name);
        edPhone.setText(phone);
        edAdd.setText(address);


        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        progressDialog = new ProgressDialog(this);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.equals(edName.getText().toString()) || !phone.equals(edPhone.getText().toString()) || !address.equals(edAdd.getText().toString())) {

                    String name_change = edName.getText().toString();
                    String phone_change = edPhone.getText().toString();
                    String add_change = edAdd.getText().toString();

                    Log.d("kiemtra", name_change + phone_change + add_change);

                    progressDialog.setTitle("Update Infomation Of User");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    updateUser(name_change, phone_change, add_change);
                }else{
                    String message = "Please change something before pressing";
                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUser(String name_change, String phone_change, String add_change) {
        userRef.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (!TextUtils.isEmpty(name_change) && !TextUtils.isEmpty(phone_change) && !TextUtils.isEmpty(add_change)){
                        Users users = new Users(name_change,password, phone_change, add_change, admin);
                        if (!phone_change.equals(phone)){
                            userRef.child(phone_change).setValue(users);
                            snapshot.getRef().removeValue();

                            progressDialog.dismiss();

                            String message = "Update successfully";
                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            Paper.book().destroy();
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class).
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ));
                            finish();
                            System.exit(0);
                        }else{
                            snapshot.getRef().setValue(users);
                            Prevalent.crurrentOnlineUser = users;

                            progressDialog.dismiss();

                            String message = "Update successfully";
                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            if (Paper.book().read(Prevalent.AdminKey).equals("1")){
                                startActivity(new Intent(ProfileActivity.this, HomeAdminActivity.class));
                            }else{
                                startActivity(new Intent(ProfileActivity.this, HomeUserActivity.class));
                            }
                            finish();
                        }



                    }else{
                        Toast.makeText(ProfileActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void back() {
        if (Paper.book().read(Prevalent.AdminKey).equals("1")){
            startActivity(new Intent(ProfileActivity.this, HomeAdminActivity.class));
            finish();
        }else{
            startActivity(new Intent(ProfileActivity.this, HomeUserActivity.class));
            finish();
        }
    }
}