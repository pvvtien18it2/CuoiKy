package com.tienmonkey.cuoiky.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tienmonkey.cuoiky.Interface.ItemClickListner;
import com.tienmonkey.cuoiky.Module.Orders;
import com.tienmonkey.cuoiky.Module.Product;
import com.tienmonkey.cuoiky.R;
import com.tienmonkey.cuoiky.User.OrderActivity;
import com.tienmonkey.cuoiky.User.OrderDetailActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersHolder> {
    private Context context;
    private List<Orders> orders_item = new ArrayList<>();
    private ItemClickListner itemClickListner;

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public OrdersAdapter(Context context, List<Orders> orders_item){
        this.context = context;
        this.orders_item = orders_item;
    }

    @NonNull
    @Override
    public OrdersAdapter.OrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rows = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_show_order, parent, false);
        return new OrdersAdapter.OrdersHolder(rows);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrdersHolder holder, int position) {
        Orders orders = orders_item.get(position);

        holder.tvNameProduct.setText(orders.getName());
        holder.tvPriceProduct.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(orders.getPrice())) +" VND");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = orders.getDate()+orders.getTime();
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders_item.size();
    }

    public class OrdersHolder extends RecyclerView.ViewHolder {
        private TextView tvNameProduct, tvPriceProduct;
        public OrdersHolder(@NonNull View itemView) {
            super(itemView);

            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
        }
    }
}
