package com.tienmonkey.cuoiky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tienmonkey.cuoiky.Admin.HomeAdminActivity;
import com.tienmonkey.cuoiky.Module.Prevalent;
import com.tienmonkey.cuoiky.Module.Users;
import com.tienmonkey.cuoiky.User.HomeUserActivity;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText edPhoneLogin, edPasswordLogin;
    private Button btnLogin;
    private CheckBox cbRememberMe;
    private String phone, password;
    private DatabaseReference loginRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        edPhoneLogin = findViewById(R.id.edPhoneLogin);
        edPasswordLogin = findViewById(R.id.edPasswordLogin);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        progressDialog = new ProgressDialog(this);

        Paper.init(this);

        loginRef = FirebaseDatabase.getInstance().getReference();
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                loginUser();
            }
        });
    }

    private void loginUser() {

        final String phone = edPhoneLogin.getText().toString();
        final String password = edPasswordLogin.getText().toString();

        if (cbRememberMe.isChecked()){
            Paper.book().write(Prevalent.PhoneKey, phone);
            Paper.book().write(Prevalent.PasswordKey, password);

        }
        loginRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phone).exists()){
                    Users userData = snapshot.child("Users").child(phone).getValue(Users.class);
                    if (!password.equals(userData.getPassword())){
                        Toast.makeText(LoginActivity.this, "Password wrong", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else{
                        if (userData.getAdmin().equals("1")){
                            startActivity(new Intent(LoginActivity.this, HomeAdminActivity.class));
                            progressDialog.dismiss();
                            Prevalent.crurrentOnlineUser = userData;
                            Paper.book().write(Prevalent.UsernameKey, userData.getName());
                            Paper.book().write(Prevalent.AdminKey, userData.getAdmin());
                            Paper.book().write(Prevalent.AddressKey, userData.getAddress());
                        }else{
                            startActivity(new Intent(LoginActivity.this, HomeUserActivity.class));
                            progressDialog.dismiss();
                            Prevalent.crurrentOnlineUser = userData;
                            Paper.book().write(Prevalent.UsernameKey, userData.getName());
                            Paper.book().write(Prevalent.AdminKey, userData.getAdmin());
                            Paper.book().write(Prevalent.AddressKey, userData.getAddress());
                        }
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "This phone number isn't exists", Toast.LENGTH_SHORT).show();
                    edPhoneLogin.setText("");
                    edPasswordLogin.setText("");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}