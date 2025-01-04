package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NavigationView 설정
        NavigationView navigationView = findViewById(R.id.navigation_bar);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_timetable) {
            startActivity(new Intent(this, TimetableActivity.class));
        } else if (itemId == R.id.navigation_rating) {
            startActivity(new Intent(this, RatingActivity.class));
        } else if (itemId == R.id.navigation_notifications) {
            Toast.makeText(this, "Notifications Selected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid menu option", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
}