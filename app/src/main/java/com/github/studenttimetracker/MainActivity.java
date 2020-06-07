package com.github.studenttimetracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.github.studenttimetracker.activities.InfoActivity;
import com.github.studenttimetracker.activities.SettingsActivity;
import com.github.studenttimetracker.database.DatabaseHelper;
import com.github.studenttimetracker.fragments.ProfileFragment;
import com.github.studenttimetracker.fragments.StatisticsFragment;
import com.github.studenttimetracker.fragments.TimelineFragment;
import com.github.studenttimetracker.fragments.TrackTimeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

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

        // Filling database with data
        if(!this.getDatabasePath(DatabaseHelper.DATABASE_NAME).exists()) {
            try {
                SQLiteDatabase database = new DatabaseHelper(this).getWritableDatabase();
                InputStream inputStream = getResources().getAssets().open("databaseSetup.sql");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (bufferedReader.ready()) {
                    // In case of error: check if databaseSetup.sql contains no-SQL lines. Like empty lines, comments (yes even sql comments!)
                    database.execSQL(bufferedReader.readLine());
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private NavigationView.OnNavigationItemSelectedListener leftNavListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch (item.getItemId()) {
                        case R.id.nav_settings:
                            intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            break;
                        case R.id.nav_info:
                            intent = new Intent(getApplicationContext(), InfoActivity.class);
                            break;
                        case R.id.nav_contact:
                            intent = new Intent(Intent.ACTION_SENDTO);
                            String uriText = "mailto:dominos55555@gmail.com" +
                                    "?subject=" + Uri.encode("Contact form - Student Time Tracker app") +
                                    "&body=" + Uri.encode("Hi!\nI want to ...");
                            intent.setData(Uri.parse(uriText));
                            break;
                        default:
                            return false;
                    }
                    startActivity(intent);
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
