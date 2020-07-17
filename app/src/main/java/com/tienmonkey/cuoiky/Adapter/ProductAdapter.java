package com.tienmonkey.cuoiky.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.tienmonkey.cuoiky.Admin.HomeAdminActivity;
import com.tienmonkey.cuoiky.Interface.ItemClickListner;
import com.tienmonkey.cuoiky.Module.Product;
import com.tienmonkey.cuoiky.ProductDetailActivity;
import com.tienmonkey.cuoiky.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//public class ProductAdapter extends RecyclerView.ViewHolder  implements View.OnClickListener{
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{
    private Context context;
    private List<Product> products = new ArrayList<>();
    private ItemClickListner itemClickListner;

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public ProductAdapter(Context context, List<Product> products){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rows = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_show_item_product, parent, false);
        return new ProductHolder(rows);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductHolder holder, int position) {
        Product product = products.get(position);

        holder.tvNameProduct.setText(product.getName());
        holder.tvPriceProduct.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(product.getPrice())) +" VND");
        Picasso.get().load(product.getImage()).into(holder.imItemProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = product.getDate()+product.getTime();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        private ImageView imItemProduct;
        private TextView tvNameProduct, tvPriceProduct;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);

            imItemProduct = itemView.findViewById(R.id.imItemProduct);
        }
    }
}
