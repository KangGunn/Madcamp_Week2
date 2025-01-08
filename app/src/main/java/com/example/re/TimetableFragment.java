package com.example.re;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class TimetableFragment extends Fragment {
    private GridLayout timetableGrid;
    private PreferencesResponse.Timetable timetable; // 서버에서 전달받은 시간표 데이터

    // Factory 메서드로 TimetableFragment 생성
    public static TimetableFragment newInstance(PreferencesResponse.Timetable timetable) {
        TimetableFragment fragment = new TimetableFragment();
        fragment.timetable = timetable; // 시간표 데이터 전달
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        // GridLayout 참조
        timetableGrid = view.findViewById(R.id.timetable_grid);

        // 시간표 데이터가 있으면 채우기
        if (timetable != null) {
            TimetableUtils.populateTimetable(timetableGrid, timetable, requireContext());
        }

        return view;
    }
}
