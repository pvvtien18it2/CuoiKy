package com.tienmonkey.cuoiky.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.tienmonkey.cuoiky.R;

public class CategoryActivity extends AppCompatActivity {

    private ImageButton imLatop, imMobile , imHeadset, imWatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        imLatop = findViewById(R.id.imLaptop);
        imMobile = findViewById(R.id.imMobile);
        imHeadset = findViewById(R.id.imHeadset);
        imWatch = findViewById(R.id.imWatch);

        imLatop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CategoryActivity.this, AddItemProductActivity.class);
//                intent.putExtra("category","laptop");
//                startActivity(intent);
                startActivity(new Intent(CategoryActivity.this, AddItemProductActivity.class)
                .putExtra("category", "laptop"));
            }
        });

        imMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, AddItemProductActivity.class)
                        .putExtra("category", "mobile"));
            }
        });

        imHeadset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, AddItemProductActivity.class)
                        .putExtra("category", "headset"));
            }
        });

        imWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, AddItemProductActivity.class)
                        .putExtra("category", "watch"));
            }
        });
    }
}