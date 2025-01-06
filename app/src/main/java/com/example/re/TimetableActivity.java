package com.example.re;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class TimetableActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TimetableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        recyclerView = findViewById(R.id.timetable_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 데이터 생성
        List<TimetableRow> timetableData = createTimetableData();

        // Adapter 설정
        adapter = new TimetableAdapter(timetableData);
        recyclerView.setAdapter(adapter);
    }

    private List<TimetableRow> createTimetableData() {
        List<TimetableRow> timetableData = new ArrayList<>();

        // 첫 행: 월, 화, 수, 목, 금 제목
        timetableData.add(new TimetableRow("시간", "월", "화", "수", "목", "금"));

        // 반복문으로 시간대 추가
        int hour = 9;
        int minute = 0;

        for (int i = 0; i < 30; i++) {
            String startTime = String.format("%02d:%02d", hour, minute);
            minute += 30;
            if (minute == 60) {
                hour++;
                minute = 0;
            }
            String endTime = String.format("%02d:%02d", hour, minute);
            timetableData.add(new TimetableRow(startTime + "-" + endTime, "", "", "", "", ""));
        }
        return timetableData;
    }
}
