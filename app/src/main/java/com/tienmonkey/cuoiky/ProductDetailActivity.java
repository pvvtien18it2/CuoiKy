package com.tienmonkey.cuoiky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tienmonkey.cuoiky.Admin.HomeAdminActivity;
import com.tienmonkey.cuoiky.Module.Orders;
import com.tienmonkey.cuoiky.Module.Prevalent;
import com.tienmonkey.cuoiky.Module.Product;
import com.tienmonkey.cuoiky.User.HomeUserActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvBack, tvName, tvDes, tvPrice;
    private ImageView imProductDetail;
    private Button btnOrder;
    private String id, date, time, admin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        FirebaseApp.initializeApp(this);
        Paper.init(this);

        id = getIntent().getExtras().get("id").toString();
        progressDialog = new ProgressDialog(this);


        admin = Paper.book().read(Prevalent.AdminKey);



        tvBack = findViewById(R.id.tvBack);
        tvName = findViewById(R.id.tvName);
        tvDes = findViewById(R.id.tvDes);
        tvPrice = findViewById(R.id.tvPrice);

        imProductDetail = findViewById(R.id.imProductDetail);

        btnOrder = findViewById(R.id.btnOrder);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (admin.equals("1")){
                    String message = "You are admin, so you can't order";
                    Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setTitle("Order");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    OrderNow();
                }

            }
        });


    show_item_product();

}

    private void OrderNow() {
        Calendar calendar = Calendar.getInstance();

        String name = tvName.getText().toString();
        String price = tvPrice.getText().toString();
        String phone = Paper.book().read(Prevalent.PhoneKey);
        String add = Paper.book().read(Prevalent.AddressKey);
        String nameUserBuy = Paper.book().read(Prevalent.UsernameKey);

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd yyyy");
        date = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        time = currentTime.format(calendar.getTime());

        DatabaseReference orderListRef = FirebaseDatabase.getInstance().getReference().child("Order list");

        orderListRef.child("User View").child(phone).child(date+time).
                setValue(new Orders(id, name, price, date, time, add, phone, nameUserBuy))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            orderListRef.child("Admin View").child(phone).child("Product").child(id)
                                    .setValue(new Orders(id, name, price, date, time, add, phone, nameUserBuy))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                deleteItemProduct(id);
                                            }else{
                                                progressDialog.dismiss();
                                                String message = "Something wrong...";
                                                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ProductDetailActivity.this, HomeUserActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void deleteItemProduct(String id) {
        DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference();
        Query successfull = deleteRef.child("Products").child(id);

        successfull.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Product product = snapshot.getValue(Product.class);
                    StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(product.getImage());
                    photoRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                String message = "Delete image successfully";
                                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                            }else{
                                String message = "Something wrong when delete image";
                                Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    snapshot.getRef().removeValue();
                    progressDialog.dismiss();
                    String message = "Congratulations on your successful order";
                    Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProductDetailActivity.this, HomeUserActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error delete", "onCancelled", databaseError.toException());
            }
        });
    }

    private void show_item_product() {

        FirebaseDatabase.getInstance().getReference().child("Products").child(id).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Product product = snapshot.getValue(Product.class);
                            tvName.setText(product.getName());
                            tvDes.setText(product.getDes());
                            tvPrice.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(product.getPrice())) +" VND");

                            Picasso.get().load(product.getImage()).into(imProductDetail);
                        }
                        else{
                            Toast.makeText(ProductDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void back() {
        if (Paper.book().read(Prevalent.AdminKey).equals("1")){
            startActivity(new Intent(ProductDetailActivity.this, HomeAdminActivity.class));
            finish();
        }else{
            startActivity(new Intent(ProductDetailActivity.this, HomeUserActivity.class));
            finish();
        }
    }
    

}