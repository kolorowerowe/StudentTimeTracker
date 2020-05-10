package com.github.studenttimetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.github.studenttimetracker.fragments.ProfileFragment;
import com.github.studenttimetracker.fragments.StatisticsFragment;
import com.github.studenttimetracker.fragments.TimelineFragment;
import com.github.studenttimetracker.fragments.TrackTimeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

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

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}
