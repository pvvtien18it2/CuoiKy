package com.tienmonkey.cuoiky.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tienmonkey.cuoiky.Admin.HomeAdminActivity;
import com.tienmonkey.cuoiky.Module.Orders;
import com.tienmonkey.cuoiky.Module.Prevalent;
import com.tienmonkey.cuoiky.Module.Product;
import com.tienmonkey.cuoiky.ProductDetailActivity;
import com.tienmonkey.cuoiky.R;

import java.text.NumberFormat;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView tvBack, tvOrderName, tvOrderPrice, tvOrderPhone,
            tvOrderTime, tvOrderDay;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        FirebaseApp.initializeApp(this);
        Paper.init(this);

        id = getIntent().getExtras().get("id").toString();

        tvBack = findViewById(R.id.tvBack);
        tvOrderName = findViewById(R.id.tvOrderName);
        tvOrderPrice = findViewById(R.id.tvOrderPrice);
        tvOrderPhone = findViewById(R.id.tvOrderPhone);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvOrderDay = findViewById(R.id.tvOrderDay);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        show_order();
    }

    private void show_order() {
        String number = Paper.book().read(Prevalent.PhoneKey);
        FirebaseDatabase.getInstance().getReference()
                .child("Order list").child("User View").child(number).child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Orders orders = snapshot.getValue(Orders.class);
                            tvOrderName.setText(orders.getName());
                            tvOrderPrice.setText(orders.getPrice());
                            tvOrderPhone.setText(orders.getPhone());
                            tvOrderTime.setText(orders.getTime());
                            tvOrderDay.setText(orders.getDate());
                        }
                        else{
                            Toast.makeText(OrderDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void back() {
        startActivity(new Intent(OrderDetailActivity.this, OrderActivity.class));
        finish();
    }
}