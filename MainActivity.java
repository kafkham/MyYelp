package com.example.myyelp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FavouritesFragment favouritesFragment = null;
    SearchFragment searchFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // launch a new search fragment unless the activity has a saved instance state
        if (savedInstanceState == null) {
            searchFragment = SearchFragment.getInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_for_replacing, searchFragment).commit();
        }

        // drawer listener
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // drawer controls
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if (id == R.id.search_drawer) {
            searchFragment = SearchFragment.getInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_for_replacing, searchFragment).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (id == R.id.fav_drawer) {
            favouritesFragment = FavouritesFragment.getInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_for_replacing, favouritesFragment).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}