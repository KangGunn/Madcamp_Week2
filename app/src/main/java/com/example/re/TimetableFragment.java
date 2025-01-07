package com.example.re;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class TimetableFragment extends Fragment {
    private GridLayout timetableGrid;

    // 색상 배열
    private final int[] colors = {
            R.color.color1, R.color.color2, R.color.color3,
            R.color.color4, R.color.color5, R.color.color6,
            R.color.color7, R.color.color8
    };

    // 과목과 색상 맵핑을 위한 Map
    private final Map<String, Integer> courseColorMap = new HashMap<>();
    private int colorIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        // GridLayout 참조
        timetableGrid = view.findViewById(R.id.timetable_grid);

        // 시간표 채우기
        populateTimetable();

        return view;
    }

    private void populateTimetable() {
        // GridLayout 초기화
        timetableGrid.removeAllViews();

        // 첫 행: 요일 헤더
        String[] days = {"시간", "월", "화", "수", "목", "금"};
        for (int column = 0; column < days.length; column++) {
            TextView header = new TextView(getContext());
            header.setText(days[column]);
            header.setBackgroundResource(R.drawable.cell_border); // 테두리 적용
            header.setGravity(Gravity.CENTER);
            header.setTextSize(16);
            header.setTextColor(getResources().getColor(android.R.color.black));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(0, 1f), // 첫 행
                    GridLayout.spec(column, 1f) // 각 열
            );
            params.setMargins(1, 1, 1, 1); // 셀 간격 추가
            header.setLayoutParams(params);
            timetableGrid.addView(header);
        }

        // 첫 열: 시간 헤더
        String[] timeSlots = {
                "09:00-10:30", "10:30-12:00", "12:00-13:00", "13:00-14:30",
                "14:30-16:00", "16:00-17:30", "17:30-19:00",
                "19:00-20:30", "20:30-22:00"
        };
        for (int row = 1; row <= timeSlots.length; row++) {
            TextView timeCell = new TextView(getContext());
            timeCell.setText(timeSlots[row - 1]);
            timeCell.setBackgroundResource(R.drawable.cell_border);
            timeCell.setGravity(Gravity.CENTER);
            timeCell.setTextSize(14);
            timeCell.setTextColor(getResources().getColor(android.R.color.black));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row, 1f), // 각 행
                    GridLayout.spec(0, 1f) // 첫 열
            );
            params.setMargins(1, 1, 1, 1); // 셀 간격 추가
            timeCell.setLayoutParams(params);
            timetableGrid.addView(timeCell);
        }

        // 빈 셀 채우기
        for (int row = 1; row <= timeSlots.length; row++) {
            for (int column = 1; column <= 5; column++) { // 월~금
                TextView emptyCell = new TextView(getContext());
                emptyCell.setGravity(Gravity.CENTER);
                emptyCell.setBackgroundResource(R.drawable.cell_border);
                emptyCell.setText("");

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(row, 1f), // 각 행
                        GridLayout.spec(column, 1f) // 각 열
                );
                params.setMargins(1, 1, 1, 1); // 셀 간격 추가
                emptyCell.setLayoutParams(params);
                timetableGrid.addView(emptyCell);
            }
        }

        // 과목 데이터 추가
        addCourseToTimetable("최윤지", "월", "09:00-10:30", "강의실 A");
        addCourseToTimetable("최윤지", "목", "10:30-12:00", "강의실 B");
        addCourseToTimetable("이현서", "월", "10:30-12:00", "강의실 C");
        addCourseToTimetable("이현서", "수", "09:00-10:30", "강의실 D");
        addCourseToTimetable("김윤지", "월", "13:00-14:30", "강의실 E");
        addCourseToTimetable("김윤지", "목", "14:30-16:00", "강의실 F");
        addCourseToTimetable("김윤지", "금", "10:30-12:00", "강의실 G");
        addCourseToTimetable("장현서", "월", "14:30-16:00", "강의실 H");
        addCourseToTimetable("장현서", "수", "13:00-14:30", "강의실 I");
        addCourseToTimetable("강강건", "화", "14:30-16:00", "강의실 J");
        addCourseToTimetable("강강건", "수", "14:30-16:00", "강의실 K");
        addCourseToTimetable("최강건", "화", "17:30-19:00", "강의실 L");
        addCourseToTimetable("최강건", "수", "16:00-17:30", "강의실 M");
    }

    private void addCourseToTimetable(String courseName, String day, String time, String room) {
        int column = getColumnForDay(day);
        int row = getRowForTime(time);

        if (column < 1 || column > 5 || row < 1 || row > 9) return; // 범위 초과 시 무시

        // 과목 색상 가져오기
        int color = getColorForCourse(courseName);

        TextView courseCell = new TextView(getContext());
        courseCell.setText(courseName + "\n" + room);
        courseCell.setBackgroundResource(R.drawable.cell_border);
        courseCell.setBackgroundColor(color);
        courseCell.setGravity(Gravity.CENTER);
        courseCell.setTextSize(12);
        courseCell.setTextColor(getResources().getColor(android.R.color.white, null));
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(row, 1f),
                GridLayout.spec(column, 1f)
        );
        params.setMargins(1, 1, 1, 1);
        courseCell.setLayoutParams(params);
        timetableGrid.addView(courseCell);
    }

    private int getColorForCourse(String courseName) {
        if (!courseColorMap.containsKey(courseName)) {
            int color = getResources().getColor(colors[colorIndex], null);
            courseColorMap.put(courseName, color);
            colorIndex = (colorIndex + 1) % colors.length;
        }
        return courseColorMap.get(courseName);
    }

    private int getColumnForDay(String day) {
        switch (day) {
            case "월": return 1;
            case "화": return 2;
            case "수": return 3;
            case "목": return 4;
            case "금": return 5;
            default: return -1;
        }
    }

    private int getRowForTime(String time) {
        switch (time) {
            case "09:00-10:30": return 1;
            case "10:30-12:00": return 2;
            case "12:00-13:00": return 3;
            case "13:00-14:30": return 4;
            case "14:30-16:00": return 5;
            case "16:00-17:30": return 6;
            case "17:30-19:00": return 7;
            case "19:00-20:30": return 8;
            case "20:30-22:00": return 9;
            default: return -1;
        }
    }
}
