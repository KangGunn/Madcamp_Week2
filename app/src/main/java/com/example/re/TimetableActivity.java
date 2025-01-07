package com.example.re;

import android.os.Bundle;

public class TimetableActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // content_frame에 TimetableFragment를 로드
        getLayoutInflater().inflate(R.layout.activity_timetable, findViewById(R.id.content_frame), true);

        // Fragment를 추가
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new TimetableFragment())
                .commit();
    }

    @Override
    protected int getSelectedMenuId() {
        return R.id.navigation_timetable;
    }
}
