package com.tienmonkey.cuoiky.Admin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tienmonkey.cuoiky.Module.Prevalent;
import com.tienmonkey.cuoiky.Module.Product;
import com.tienmonkey.cuoiky.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class AddItemProductActivity extends AppCompatActivity {

    private ImageView imAddImage;
    private EditText pName, pDes, pPrice;
    private Button btnAdd;
    private String category, date, time, randomkey, downloadImg, name, des, price;
    private DatabaseReference productRef;
    private StorageReference imgProductRef;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_product);

        imAddImage = findViewById(R.id.imAddImage);

        Paper.init(this);

        String category_name = getIntent().getExtras().get("category").toString();

        category = category_name;

        progressDialog = new ProgressDialog(this);

        pName = findViewById(R.id.pName);
        pDes = findViewById(R.id.pDes);
        pPrice = findViewById(R.id.pPrice);

        FirebaseApp.initializeApp(this);

        productRef = FirebaseDatabase.getInstance().getReference();

        imgProductRef = FirebaseStorage.getInstance().getReference();

        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Add Item Product");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                addProduct();
            }
        });

        imAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }


    private void openGallery(){
        startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT
        ).setType("image/*"), GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            imAddImage.setImageURI(ImageUri);
        }
    }

    private void addProduct() {

        name = pName.getText().toString();
        des = pDes.getText().toString();
        price = pPrice.getText().toString();

        if (ImageUri == null){
            Toast.makeText(this, "Please input image product", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please input name product", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(des)){
            Toast.makeText(this, "Please input description product", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(price)){
            Toast.makeText(this, "Please input price product", Toast.LENGTH_SHORT).show();
        }else{
            StoreProductInfomation(category);
        }
    }

    private void StoreProductInfomation(String category) {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd yyyy");
        date = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        time = currentTime.format(calendar.getTime());

        randomkey = date + time;

        StorageReference filePath = imgProductRef.child("Products").child(ImageUri.getLastPathSegment() + randomkey + "img");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddItemProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddItemProductActivity.this, "Product image upload success", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw  task.getException();
                        }

                        downloadImg = filePath.getDownloadUrl().toString();
                        return  filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){

                            downloadImg = task.getResult().toString();
                            Toast.makeText(AddItemProductActivity.this, "got the product image Url successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfomation(category);
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfomation(String category) {
        String nameUserPost = Paper.book().read(Prevalent.UsernameKey);
        productRef.child("Products").child(randomkey).setValue(new Product(name, des, price,
                downloadImg, date, time, nameUserPost, category, randomkey)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AddItemProductActivity.this, "Save product infomation is successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(AddItemProductActivity.this, HomeAdminActivity.class));
                    finish();
                }else{
                    String message = task.getException().toString();
                    Toast.makeText(AddItemProductActivity.this, "Something wrong: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}