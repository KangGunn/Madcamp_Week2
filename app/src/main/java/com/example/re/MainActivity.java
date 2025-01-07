package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NavigationView 설정
        NavigationView navigationView = findViewById(R.id.navigation_bar);
        navigationView.setNavigationItemSelectedListener(this);

        // 콘텐츠 프레임 초기화
        contentFrame = findViewById(R.id.content_frame);

        // 기본 화면 설정
        loadContentView(R.layout.activity_timetable); // 초기 화면
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_timetable) {
            loadContentView(R.layout.activity_timetable);
        } else if (itemId == R.id.navigation_preferences) {
            loadContentView(R.layout.activity_preferences);
        } else if (itemId == R.id.navigation_search) {
            Toast.makeText(this, "Notifications Selected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid menu option", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void loadContentView(int layoutResId) {
        // content_frame에 해당 레이아웃 로드
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(layoutResId, contentFrame, false);
        contentFrame.removeAllViews(); // 이전 뷰 제거
        contentFrame.addView(view); // 새 뷰 추가
    }
}