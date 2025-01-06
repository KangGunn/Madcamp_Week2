package com.example.re;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity started"); // Activity 시작 로그 추가

        // 하단 네비게이션 바 설정
        BottomNavigationView navigationBar = findViewById(R.id.navigation_bar);
        if (navigationBar == null) {
            Log.e(TAG, "Navigation bar not found! Check the ID or XML structure.");
            return; // `navigationBar`가 null이면 실행 중지
        }

        navigationBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Log.d(TAG, "Navigation item selected: " + itemId);

            if (itemId == R.id.navigation_search) {
                Log.d("hello", "서치 시작");
                loadContent(R.layout.activity_search); // SearchActivity 레이아웃 로드
                return true;
            } else if (itemId == R.id.navigation_timetable) {
                loadContent(R.layout.activity_timetable);
                return true;
            } else if (itemId == R.id.navigation_rating) {
                loadContent(R.layout.activity_preferences);
                return true;
            } else {
                return false;
            }
        });


        // 기본 화면 설정//로그인으로 바꾸기
        loadContent(R.layout.activity_timetable);
    }
    private void loadContent(int layoutResId) {

        // FrameLayout으로 타입캐스팅
        ViewGroup contentFrame = findViewById(R.id.content_frame);

        if (contentFrame != null) {
            Log.d(TAG, "Removing all views from content_frame");
            // 기존 뷰 제거
            contentFrame.removeAllViews();
            // 새로운 레이아웃 로드
            getLayoutInflater().inflate(layoutResId, contentFrame, true);

            Log.d("MainActivity", "Loaded layout: " + getResources().getResourceEntryName(layoutResId));
        }
    }
}
