package com.example.re;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.util.HashMap;
import java.util.Map;

public class TimetableUtils {

    // 색상 배열: 과목별 색상을 지정하기 위해 사용
    private static final int[] colors = {
            R.color.color1, R.color.color2, R.color.color3,
            R.color.color4, R.color.color5, R.color.color6,
            R.color.color7, R.color.color8
    };
    private static final Map<String, Integer> courseColorMap = new HashMap<>();
    private static int colorIndex = 0;
    private static int getColorForCourse(Context context, String courseName) {
        if (!courseColorMap.containsKey(courseName)) {
            int color = ContextCompat.getColor(context, colors[colorIndex]);
            courseColorMap.put(courseName, color);
            colorIndex = (colorIndex + 1) % colors.length; // 색상 순환
        }
        return courseColorMap.get(courseName);
    } //색상 순환

    /**
     * 시간표 데이터를 GridLayout에 표시합니다.
     */
    public static void populateTimetable(GridLayout timetableGrid, PreferencesResponse.Timetable timetable, Context context) {
        timetableGrid.removeAllViews();
        Log.d("DEBUG", "populateTimetable 호출됨. 과목 수: " + timetable.getCourses().size());

        // 요일 헤더와 시간 헤더 추가
        addDayHeaders(timetableGrid, context);
        addTimeHeaders(timetableGrid, context);

        for (PreferencesResponse.Timetable.Course course : timetable.getCourses()) {
            String[] lectureTimes = course.getLecture_time().split("\n");
            for (String lecture : lectureTimes) {
                String[] parts = lecture.split(" ");
                if (parts.length != 2) continue;

                String day = parts[0];
                String timeRange = parts[1];
                int column = getColumnForDay(day);
                int row = getRowForTime(timeRange);

                if (column == -1 || row == -1) continue;

                addCourseToGrid(timetableGrid, course, column, row, context);
            }
        }
    }



    /**
     * GridLayout에 과목 데이터를 추가합니다.
     */

    private static void addDayHeaders(GridLayout timetableGrid, Context context) {
        String[] days = {"월", "화", "수", "목", "금"};
        for (int i = 0; i < days.length; i++) {
            TextView dayHeader = new TextView(context);
            dayHeader.setText(days[i]);
            dayHeader.setGravity(Gravity.CENTER);
            dayHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.darker_gray));
            dayHeader.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            dayHeader.setPadding(8, 8, 8, 8);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(0, 1),
                    GridLayout.spec(i + 1, 1)
            );
            params.width = 200; // 고정된 가로 길이
            params.height = 100; // 고정된 세로 길이
            params.setMargins(4, 4, 4, 4);
            dayHeader.setLayoutParams(params);

            timetableGrid.addView(dayHeader);
        }
    }

    private static void addTimeHeaders(GridLayout timetableGrid, Context context) {
        String[] times = {"09:00~10:30", "10:30~12:00", "12:00~13:00", "13:00~14:30",
                "14:30~16:00", "16:00~17:30", "17:30~19:00", "19:00~20:30",
                "20:30~22:00", "22:00~23:30"};
        for (int i = 0; i < times.length; i++) {
            TextView timeHeader = new TextView(context);
            timeHeader.setText(times[i]);
            timeHeader.setGravity(Gravity.CENTER);
            timeHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.darker_gray));
            timeHeader.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            timeHeader.setPadding(8, 8, 8, 8);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(i + 1, 1),
                    GridLayout.spec(0, 1)
            );
            params.width = 260; // 고정된 가로 길이
            params.height = 100; // 고정된 세로 길이
            params.setMargins(4, 4, 4, 4);
            timeHeader.setLayoutParams(params);

            timetableGrid.addView(timeHeader);
        }
    }

    private static void addCourseToGrid(GridLayout timetableGrid, PreferencesResponse.Timetable.Course course,
                                        int column, int row, Context context) {
        int color = getColorForCourse(context, course.getCourse_name());
        String lectureRoom = course.getLecture_room() != null ? course.getLecture_room() : "강의실 미정";

        TextView courseCell = new TextView(context);
        courseCell.setText(course.getCourse_name() + "\n" + lectureRoom);
        courseCell.setGravity(Gravity.CENTER);
        courseCell.setTextSize(12);
        courseCell.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        courseCell.setBackgroundColor(color);
        courseCell.setPadding(8, 8, 8, 8);
        courseCell.setSingleLine(false);
        courseCell.setEllipsize(TextUtils.TruncateAt.END);
        courseCell.setMaxLines(2);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(row, 1),
                GridLayout.spec(column, 1)
        );
        params.width = 200; // 고정된 가로 길이
        params.height = 100; // 고정된 세로 길이
        params.setMargins(4, 4, 4, 4);
        courseCell.setLayoutParams(params);

        timetableGrid.addView(courseCell);
    }
    /**
     * 요일에 따라 열(column)을 반환합니다.
     */
    private static int getColumnForDay(String day) {
        switch (day) {
            case "월": return 1;
            case "화": return 2;
            case "수": return 3;
            case "목": return 4;
            case "금": return 5;
            default: return -1; // 유효하지 않은 요일
        }
    }

    /**
     * 시간 범위에 따라 행(row)을 반환합니다.
     */
    private static int getRowForTime(String time) {
        switch (time) {
            case "09:00~10:30": return 1;
            case "10:30~12:00": return 2;
            case "12:00~13:00": return 3;
            case "13:00~14:30": return 4;
            case "14:30~16:00": return 5;
            case "16:00~17:30": return 6;
            case "17:30~19:00": return 7;
            case "19:00~20:30": return 8;
            case "20:30~22:00": return 9;
            case "22:00~23:30": return 10;
            default: return -1; // 유효하지 않은 시간 범위
        }
    }
}