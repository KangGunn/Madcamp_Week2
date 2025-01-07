package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 네비게이션 바 설정
        BottomNavigationView navigationBar = findViewById(R.id.navigation_bar);
        navigationBar.setOnItemSelectedListener(this::onNavigationItemSelected);

        // 현재 선택된 네비게이션 아이템 강조
        int selectedMenuId = getSelectedMenuId();
        if (selectedMenuId != -1) {
            navigationBar.setSelectedItemId(selectedMenuId);
        }
    }

    // 현재 Activity에 해당하는 네비게이션 메뉴 ID를 반환
    protected abstract int getSelectedMenuId();

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_timetable) {
            if (!(this instanceof TimetableActivity)) {
                startActivity(new Intent(this, TimetableActivity.class));
            }
            return true;
        } else if (itemId == R.id.navigation_preferences) {
            if (!(this instanceof PreferencesActivity)) {
                startActivity(new Intent(this, PreferencesActivity.class));
            }
            return true;
        } else if (itemId == R.id.navigation_search) {
            if (!(this instanceof SearchActivity)) {
                startActivity(new Intent(this, SearchActivity.class));
            }
            return true;
        } else {
            return false;
        }
    }
}
