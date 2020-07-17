package com.tienmonkey.cuoiky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tienmonkey.cuoiky.Admin.HomeAdminActivity;
import com.tienmonkey.cuoiky.Module.Prevalent;
import com.tienmonkey.cuoiky.Module.Users;
import com.tienmonkey.cuoiky.User.HomeUserActivity;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin, btnSignIn;
    private ProgressDialog progressDialog;
    private DatabaseReference rememberRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(this);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignIn = findViewById(R.id.btnSignIn);

        rememberRef = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        Permissions(new RxPermissions(this));
        Paper.init(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                progressDialog.dismiss();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Sign In");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                progressDialog.dismiss();
            }
        });

        String PhoneKey = Paper.book().read(Prevalent.PhoneKey);
        String PasswordKey = Paper.book().read(Prevalent.PasswordKey);

        if (PhoneKey != "" && PasswordKey != ""){
            if (!TextUtils.isEmpty(PhoneKey) && !TextUtils.isEmpty(PasswordKey)){
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                AllowAccess(PhoneKey,PasswordKey);
            }
        }
    }

    private void AllowAccess(final String phoneKey, final String passwordKey) {
        rememberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phoneKey).exists()){
                    Users userData = snapshot.child("Users").child(phoneKey).getValue(Users.class);
                    if (!userData.getPassword().equals(passwordKey)){
                        Toast.makeText(MainActivity.this, "Password wrong", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else{
                        if (userData.getAdmin().equals("1")){
                            startActivity(new Intent(MainActivity.this, HomeAdminActivity.class));
                            Toast.makeText(MainActivity.this, "Admin", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Prevalent.crurrentOnlineUser = userData;
                            Paper.book().write(Prevalent.UsernameKey, userData.getName());
                            Paper.book().write(Prevalent.AdminKey, userData.getAdmin());
                            Paper.book().write(Prevalent.AddressKey, userData.getAddress());
                        }else{
                            startActivity(new Intent(MainActivity.this, HomeUserActivity.class));
                            Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Prevalent.crurrentOnlineUser = userData;
                            Paper.book().write(Prevalent.UsernameKey, userData.getName());
                            Paper.book().write(Prevalent.AdminKey, userData.getAdmin());
                            Paper.book().write(Prevalent.AddressKey, userData.getAddress());
                        }
                    }
                }else {
                    Toast.makeText(MainActivity.this, "This phone number isn't exists", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Permissions(@NotNull RxPermissions rxPermissions){
        String[] permissions = { Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};
        rxPermissions
                .request(permissions)
                .subscribe(granted -> {
                    if (granted) {
                        // Tất cả quyền đã cấp
                    } else {
                        // Ít nhất 1 quyền đã bị từ chối
                    }
                });
    }
}