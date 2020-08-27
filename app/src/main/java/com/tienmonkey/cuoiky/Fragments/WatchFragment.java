package com.tienmonkey.cuoiky.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tienmonkey.cuoiky.Adapter.ProductAdapter;
import com.tienmonkey.cuoiky.MainActivity;
import com.tienmonkey.cuoiky.Module.Product;
import com.tienmonkey.cuoiky.R;
import com.tienmonkey.cuoiky.User.HomeUserActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = WatchFragment.class.getSimpleName();
    private static final String ARG_PARAM2 = "param2";
//    private ImageView imMenu;
    private RecyclerView recycler_view;
    private RecyclerView.LayoutManager layoutManager;
    private Query productRef;
    private List<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter;
    private static boolean initializedPicasso = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchFragment newInstance(String param1, String param2) {
        WatchFragment fragment = new WatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch, container, false);

        FirebaseApp.initializeApp(container.getContext());
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category").equalTo("watch");

        recycler_view = view.findViewById(R.id.rvShowProductItem);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(container.getContext());
        recycler_view.setLayoutManager(layoutManager);

        adapterWatch();

        return view;
    }

    private void adapterWatch() {
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    products.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        products.add(product);
                    }
                }
                productAdapter = new ProductAdapter(getContext(), products);
                recycler_view.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}