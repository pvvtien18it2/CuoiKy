package com.tienmonkey.cuoiky.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tienmonkey.cuoiky.Fragments.AllFragment;
import com.tienmonkey.cuoiky.Fragments.HeadsetFragment;
import com.tienmonkey.cuoiky.Fragments.LaptopFragment;
import com.tienmonkey.cuoiky.Fragments.MobileFragment;
import com.tienmonkey.cuoiky.Fragments.WatchFragment;
import com.tienmonkey.cuoiky.MainActivity;
import com.tienmonkey.cuoiky.R;


import io.paperdb.Paper;

public class HomeUserActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ImageView imMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        Paper.init(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        loadFragment(new AllFragment());

        imMenu = findViewById(R.id.imMenu);

        imMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_all:
                    selectFragment = new AllFragment();
                    break;
                case R.id.navigation_laptop:
                    selectFragment = new LaptopFragment();
                    break;
                case R.id.navigation_mobile:
                    selectFragment = new MobileFragment();
                    break;
                case R.id.navigation_headset:
                    selectFragment = new HeadsetFragment();
                    break;
                case R.id.navigation_watch:
                    selectFragment = new WatchFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    selectFragment).commit();
            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(HomeUserActivity.this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_user, popup.getMenu());

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        startActivity(new Intent(HomeUserActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.nav_order:
                        startActivity(new Intent(HomeUserActivity.this, OrderActivity.class));
                        return true;
                    case R.id.nav_logout:
                        logout(HomeUserActivity.this);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void logout(Context context){
        Paper.book().destroy();
        context.startActivity(new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ));
        System.exit(0);
    }

}