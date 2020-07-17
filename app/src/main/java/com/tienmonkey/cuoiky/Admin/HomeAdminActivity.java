package com.tienmonkey.cuoiky.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;
import com.tienmonkey.cuoiky.Adapter.ProductAdapter;
import com.tienmonkey.cuoiky.MainActivity;
import com.tienmonkey.cuoiky.Module.Product;
import com.tienmonkey.cuoiky.R;
import com.tienmonkey.cuoiky.User.HomeUserActivity;
import com.tienmonkey.cuoiky.User.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class HomeAdminActivity extends AppCompatActivity {
    private ImageView imMenu;
    private RecyclerView rvShowProductItem;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference productRef;
    private List<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter;
    private static boolean initializedPicasso = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        Paper.init(this);

        imMenu = findViewById(R.id.imMenu);

        FirebaseApp.initializeApp(this);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        if (!initializedPicasso) {
            Picasso picasso = new Picasso.Builder(this).build();
            Picasso.setSingletonInstance(picasso);
            initializedPicasso = true;
        }

        rvShowProductItem = findViewById(R.id.rvShowProductItem);
        rvShowProductItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvShowProductItem.setLayoutManager(layoutManager);

        imMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                products.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    products.add(product);
                }
                productAdapter = new ProductAdapter(HomeAdminActivity.this, products);
                rvShowProductItem.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_admin, popup.getMenu());

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        startActivity(new Intent(HomeAdminActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.nav_add_admin:
                        AddAdmin();
                        return true;
                    case R.id.nav_add_product:
                        AddNewProduct();
                        return true;
                    case R.id.nav_logout:
                        logout();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void AddNewProduct() {
        startActivity(new Intent(HomeAdminActivity.this, CategoryActivity.class));
    }

    private void AddAdmin() {
        startActivity(new Intent(HomeAdminActivity.this, AddAdminActivity.class));
    }

    private void logout(){
        Paper.book().destroy();
        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ));
        System.exit(0);
    }
}