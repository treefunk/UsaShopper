package com.myoptimind.usashopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.myoptimind.usashopper.features.login.LoginActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String TAG = "SingleFragmentActivity";

    protected abstract Fragment createFragment();

    protected Boolean bottomNavVisibility = true;
    protected Boolean sideNavVisibility   = true;

    public void setBottomNavVisibility(Boolean bottomNavVisibility) {
        this.bottomNavVisibility = bottomNavVisibility;
    }

    public void setSideNavVisibility(Boolean sideNavVisibility) {
        this.sideNavVisibility = sideNavVisibility;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(!bottomNavVisibility){
            findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        }

        if(!sideNavVisibility){
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }


        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.side_menu_nav,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle() != null){
            if(item.getTitle().toString().equalsIgnoreCase("log out")){
                Intent intent = LoginActivity.createIntent(this);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
