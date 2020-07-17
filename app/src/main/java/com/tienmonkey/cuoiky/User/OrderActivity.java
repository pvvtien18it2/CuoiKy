package com.tienmonkey.cuoiky.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tienmonkey.cuoiky.Adapter.OrdersAdapter;
import com.tienmonkey.cuoiky.Adapter.ProductAdapter;
import com.tienmonkey.cuoiky.Admin.HomeAdminActivity;
import com.tienmonkey.cuoiky.Module.Orders;
import com.tienmonkey.cuoiky.Module.Prevalent;
import com.tienmonkey.cuoiky.Module.Product;
import com.tienmonkey.cuoiky.ProductDetailActivity;
import com.tienmonkey.cuoiky.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView rvShowOrdersItem;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ordersRef;
    private List<Orders> orders_item = new ArrayList<>();
    private OrdersAdapter ordersAdapter;
    private TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Paper.init(this);

        FirebaseApp.initializeApp(this);

        String number = Paper.book().read(Prevalent.PhoneKey);
        Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Order list").child("User View").child(number);

        rvShowOrdersItem = findViewById(R.id.rvShowOrdersItem);
        rvShowOrdersItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvShowOrdersItem.setLayoutManager(layoutManager);

        tvBack = findViewById(R.id.tvBack);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        show_orders();
    }

    private void show_orders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    orders_item.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Orders orders = dataSnapshot.getValue(Orders.class);
                        orders_item.add(orders);
                    }
                    ordersAdapter = new OrdersAdapter(OrderActivity.this, orders_item);
                    rvShowOrdersItem.setAdapter(ordersAdapter);
                    ordersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void back() {
        startActivity(new Intent(OrderActivity.this, HomeUserActivity.class));
        finish();
    }
}