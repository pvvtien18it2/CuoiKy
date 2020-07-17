package com.tienmonkey.cuoiky.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tienmonkey.cuoiky.LoginActivity;
import com.tienmonkey.cuoiky.Module.Users;
import com.tienmonkey.cuoiky.R;
import com.tienmonkey.cuoiky.SignInActivity;

public class AddAdminActivity extends AppCompatActivity {

    private EditText edName, edPhoneSignIn, edPasswordSignIn;
    private Button btnSignIn;
    private DatabaseReference signInRef;
    private String name, phone, password;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FirebaseApp.initializeApp(this);

        edName = findViewById(R.id.edName);
        edPhoneSignIn = findViewById(R.id.edPhoneSignIn);
        edPasswordSignIn = findViewById(R.id.edPasswordSignIn);

        progressDialog = new ProgressDialog(this);

        signInRef = FirebaseDatabase.getInstance().getReference();

        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

    }

    private void signInUser() {
        name = edName.getText().toString();
        phone = edPhoneSignIn.getText().toString();
        password = edPasswordSignIn.getText().toString();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }else if (phone.length() < 10 || phone.length() > 11){
            Toast.makeText(this, "Phone number is wrong format", Toast.LENGTH_SHORT).show();
        }else if (password.length() < 6){
            Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setTitle("Create Account");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            successSignIn();
        }
    }

    private void successSignIn() {
        signInRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phone).exists()){
                    Toast.makeText(AddAdminActivity.this, "This phone number is exists"
                            , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddAdminActivity.this, LoginActivity.class));
                    progressDialog.dismiss();
                    finish();
                }else {
                    signInRef.child("Users").child(phone).setValue(new Users(name, password, phone,
                            "", "1")).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(AddAdminActivity.this, HomeAdminActivity.class));
                                finish();
                                progressDialog.dismiss();
                            }else {
                                Toast.makeText(AddAdminActivity.this, "Something wrong!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}