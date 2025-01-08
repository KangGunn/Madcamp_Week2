package com.example.re;

import android.content.Context;
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

    /**
     * 과목별 고유 색상을 반환합니다.
     */
    private static int getColorForCourse(Context context, String courseName) {
        if (!courseColorMap.containsKey(courseName)) {
            int color = ContextCompat.getColor(context, colors[colorIndex]);
            courseColorMap.put(courseName, color);
            colorIndex = (colorIndex + 1) % colors.length; // 색상 순환
        }
        return courseColorMap.get(courseName);
    }

    /**
     * 시간표 데이터를 GridLayout에 표시합니다.
     */
    public static void populateTimetable(GridLayout timetableGrid, PreferencesResponse.Timetable timetable, Context context) {
        timetableGrid.removeAllViews(); // 기존 데이터 초기화
        Log.d("DEBUG", "GridLayout 초기화 완료. 과목 개수: " + timetable.getCourses().size());

        for (PreferencesResponse.Timetable.Course course : timetable.getCourses()) {
            // 강의 시간 데이터를 \n으로 분리
            String[] lectureTimes = course.getLecture_time().split("\n");
            for (String lecture : lectureTimes) {
                // 요일과 시간 분리 ("월 10:30~12:00" -> ["월", "10:30~12:00"])
                String[] parts = lecture.split(" ");
                if (parts.length != 2) {
                    Log.e("DEBUG", "잘못된 강의 시간 데이터: " + lecture);
                continue; // 잘못된 데이터 건너뛰기
                }

                String day = parts[0];
                String timeRange = parts[1];

                int column = getColumnForDay(day); // 요일에 따른 열 계산
                int row = getRowForTime(timeRange); // 시간 범위에 따른 행 계산

                Log.d("DEBUG", "강의 데이터 처리: day=" + day + ", timeRange=" + timeRange +
                        ", column=" + column + ", row=" + row);

                if (column == -1 || row == -1) {
                    Log.d("DEBUG", "시간 좌표 계산 실패: day=" + day + ", timeRange=" + timeRange);
                    continue; // 좌표 계산 실패 시 건너뛰기
                }

                // GridLayout에 과목 추가
                addCourseToGrid(timetableGrid, course, column, row, context);
            }
        }
        Log.d("DEBUG", "GridLayout 데이터 추가 완료.");
    }


    /**
     * GridLayout에 과목 데이터를 추가합니다.
     */
    private static void addCourseToGrid(GridLayout timetableGrid, PreferencesResponse.Timetable.Course course,
                                        int column, int row, Context context) {

        // 과목 색상 설정
        int color = getColorForCourse(context, course.getCourse_name());

        // TextView 생성 및 스타일 설정
        TextView courseCell = new TextView(context);
        courseCell.setText(course.getCourse_name() + "\n" + course.getLecture_room());
        courseCell.setGravity(Gravity.CENTER);
        courseCell.setTextSize(12);
        courseCell.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        courseCell.setBackgroundColor(color);

        // LayoutParams 설정
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(row, 1), // 해당 시간 범위에 대한 행
                GridLayout.spec(column, 1) // 해당 요일에 대한 열
        );
        params.setMargins(4, 4, 4, 4); // 여백 설정
        courseCell.setLayoutParams(params);

        // 로그: GridLayout에 추가 전
        Log.d("DEBUG", "GridLayout에 추가: 과목명=" + course.getCourse_name() +
                ", 위치(row=" + row + ", column=" + column + ")");

        // GridLayout에 추가
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
