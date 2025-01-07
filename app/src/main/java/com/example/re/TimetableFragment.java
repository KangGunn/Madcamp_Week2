package com.example.re;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TimetableFragment extends Fragment {
    private RecyclerView recyclerView;
    private TimetableAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        recyclerView = view.findViewById(R.id.timetable_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 데이터 생성 및 어댑터 설정
        List<TimetableRow> timetableData = createTimetableData();
        adapter = new TimetableAdapter(timetableData);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<TimetableRow> createTimetableData() {
        List<TimetableRow> timetableData = new ArrayList<>();

        // 첫 행: 요일 제목
        timetableData.add(new TimetableRow("시간", "월", "화", "수", "목", "금"));

        // 시간대 추가
        String[] timeSlots = {
                "09:00-10:30",
                "10:30-12:00",
                "13:00-14:30",
                "14:30-16:00",
                "16:00-17:30",
                "17:30-19:00",
                "19:00-20:30",
                "20:30-22:00",
                "22:00-23:30"
        };

        for (String time : timeSlots) {
            timetableData.add(new TimetableRow(time, "", "", "", "", ""));
        }
        return timetableData;
    }
}
