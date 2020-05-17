package com.github.studenttimetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.github.studenttimetracker.activities.InfoActivity;
import com.github.studenttimetracker.activities.SettingsActivity;
import com.github.studenttimetracker.database.DatabaseHelper;
import com.github.studenttimetracker.fragments.ProfileFragment;
import com.github.studenttimetracker.fragments.StatisticsFragment;
import com.github.studenttimetracker.fragments.TimelineFragment;
import com.github.studenttimetracker.fragments.TrackTimeFragment;
import com.github.studenttimetracker.models.Project;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);

        NavigationView leftNav = findViewById(R.id.left_navigation);
        leftNav.setNavigationItemSelectedListener(leftNavListener);

    }

    private NavigationView.OnNavigationItemSelectedListener leftNavListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch (item.getItemId()) {
                        case R.id.nav_settings:
                            intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_info:
                            intent = new Intent(getApplicationContext(), InfoActivity.class);
                            startActivity(intent);
                        default:
                            return false;
                    }
                    return false;
                }
            };

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment;

                    switch (item.getItemId()) {
                        case R.id.nav_timeline:
                            selectedFragment = new TimelineFragment();
                            break;
                        case R.id.nav_trackTime:
                            selectedFragment = new TrackTimeFragment();
                            break;
                        case R.id.nav_statistics:
                            selectedFragment = new StatisticsFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                        default:
                            selectedFragment = new TimelineFragment();
                            break;
                    }
                    loadFragment(selectedFragment);


                    return true;
                }
            };

    private void loadFragment(Fragment selectedFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                selectedFragment).commit();
    }
}
