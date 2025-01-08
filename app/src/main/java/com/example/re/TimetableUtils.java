package com.example.re;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시간표를 GridLayout에 그리는 유틸 클래스
 */
public class TimetableUtils {

    // 과목별 색상 배열
    private static final int[] colors = {
            R.color.color1, R.color.color2, R.color.color3,
            R.color.color4, R.color.color5, R.color.color6,
            R.color.color7, R.color.color8
    };

    // 과목명 -> 색상 매핑
    private static final Map<String, Integer> courseColorMap = new HashMap<>();
    private static int colorIndex = 0;

    /**
     * 과목명에 대한 색상을 가져오거나 새로 할당
     */
    private static int getColorForCourse(Context context, String courseName) {
        if (!courseColorMap.containsKey(courseName)) {
            int color = ContextCompat.getColor(context, colors[colorIndex]);
            courseColorMap.put(courseName, color);
            colorIndex = (colorIndex + 1) % colors.length;
        }
        return courseColorMap.get(courseName);
    }

    /**
     * 9:00 ~ 22:00까지 30분 간격으로 timeSlots를 구성
     *  - 22:00 이후(22:30, 23:00, 23:30)는 표시하지 않음
     */
    private static List<String> buildTimeSlots() {
        List<String> timeSlots = new ArrayList<>();

        int startHour = 9;
        int endHour = 22;  // 여기까지만
        int currentHour = startHour;
        int currentMinute = 0;

        while (true) {
            String timeString = String.format("%02d:%02d", currentHour, currentMinute);
            timeSlots.add(timeString);

            // 30분 단위 증가
            currentMinute += 30;
            if (currentMinute == 60) {
                currentMinute = 0;
                currentHour++;
            }

            // 22:00 정각까지 생성 후 종료
            // (22:00이 추가된 뒤 다음 루프에서 빠져나감)
            if (currentHour == endHour && currentMinute == 0) {
                break;
            }
            if (currentHour > endHour) {
                break;
            }
        }

        return timeSlots;
    }

    /**
     * "HH:mm" 형태 문자열을 timeSlots에서 찾아 인덱스 반환 (없으면 -1)
     */
    private static int getTimeIndex(List<String> timeSlots, String timeString) {
        return timeSlots.indexOf(timeString);
    }

    /**
     * 요일 -> column 인덱스(1..5)
     *  월=1, 화=2, 수=3, 목=4, 금=5
     */
    private static int getColumnForDay(String day, String[] days) {
        for (int i = 0; i < days.length; i++) {
            if (days[i].equals(day)) {
                // col=0은 시간열이므로 +1
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * GridLayout에 시간표 표시
     */
    public static void populateTimetable(GridLayout timetableGrid,
                                         PreferencesResponse.Timetable timetable,
                                         Context context) {
        timetableGrid.removeAllViews(); // 기존 뷰 초기화

        // 1) 9:00~22:00까지 30분 간격 타임슬롯 생성
        List<String> timeSlots = buildTimeSlots();

        // 2) 요일 배열 (월~금)
        String[] days = {"월", "화", "수", "목", "금"};

        // 3) GridLayout 행/열 설정
        //    - 맨 윗줄(요일 헤더) + timeSlots 개수
        //    - colCount = 1(시간열) + 5(월화수목금)
        int rowCount = timeSlots.size() + 1;
        int colCount = days.length + 1;

        timetableGrid.setRowCount(rowCount);
        timetableGrid.setColumnCount(colCount);

        // (row=0, col=0) 빈 칸(좌상단)
        {
            TextView emptyHeader = new TextView(context);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(0, 1),
                    GridLayout.spec(0, 1)
            );
            setThinTimeColSize(params);
            emptyHeader.setLayoutParams(params);
            timetableGrid.addView(emptyHeader);
        }

        // (row=0, col=1..5)에 요일 헤더
        for (int i = 0; i < days.length; i++) {
            TextView dayHeader = createHeaderTextView(context, days[i]);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(0, 1),
                    GridLayout.spec(i + 1, 1)
            );
            setDayHeaderCellSize(params);
            dayHeader.setLayoutParams(params);
            timetableGrid.addView(dayHeader);
        }

        // (row=1.., col=0)에 시간 표시 (왼쪽 열)
        for (int r = 1; r < rowCount; r++) {
            String timeStr = timeSlots.get(r - 1);

            TextView timeCell = new TextView(context);
            timeCell.setGravity(Gravity.CENTER);

            // 시, 분 추출
            int hour = Integer.parseInt(timeStr.substring(0, 2));  // 예: "09"
            int minute = Integer.parseInt(timeStr.substring(3));   // "00" 또는 "30"

            // 왼쪽에 표시할 정각: 9,12,13,16,19,22
            if (minute == 0 && (hour == 9 || hour == 12 || hour == 13 ||
                    hour == 16 || hour == 19 || hour == 22)) {
                // 해당 시 정각이면 숫자로 표시
                timeCell.setText(String.valueOf(hour));
                timeCell.setTextSize(12);
            } else {
                // 그 외(30분 등)는 "."으로 표시
                timeCell.setText(".");
                timeCell.setTextSize(8);
            }

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(r, 1),
                    GridLayout.spec(0, 1)
            );
            setThinTimeColSize(params);
            timeCell.setLayoutParams(params);

            timetableGrid.addView(timeCell);
        }

        // (옵션) 빈 칸 깔기: (row=1.., col=1..5)
        for (int r = 1; r < rowCount; r++) {
            for (int c = 1; c < colCount; c++) {
                TextView empty = new TextView(context);
                // 테스트용 파랑색 배경
                empty.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(r, 1),
                        GridLayout.spec(c, 1)
                );
                setBodyCellSize(params);
                empty.setLayoutParams(params);

                timetableGrid.addView(empty);
            }
        }

        // 4) 수업 정보 배치
        Log.d("DEBUG", "과목 개수: " + timetable.getCourses().size());
        for (PreferencesResponse.Timetable.Course course : timetable.getCourses()) {
            // 예) "월 09:00~10:30\n화 10:00~11:00"
            String[] lectureLines = course.getLecture_time().split("\n");
            for (String line : lectureLines) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    Log.e("DEBUG", "잘못된 형식: " + line);
                    continue;
                }
                String day = parts[0];   // "월"
                String range = parts[1]; // "09:00~10:30"

                int colIndex = getColumnForDay(day, days);
                if (colIndex == -1) {
                    Log.e("DEBUG", "유효하지 않은 요일: " + day);
                    continue;
                }

                // 시간 범위 파싱
                String[] rangeSplit = range.split("~");
                if (rangeSplit.length != 2) {
                    Log.e("DEBUG", "잘못된 시간 범위: " + range);
                    continue;
                }
                String startTime = rangeSplit[0];
                String endTime   = rangeSplit[1];

                int startIdx = getTimeIndex(timeSlots, startTime);
                int endIdx   = getTimeIndex(timeSlots, endTime);
                if (startIdx == -1 || endIdx == -1) {
                    Log.e("DEBUG", "유효하지 않은 시간: " + range);
                    continue;
                }

                // rowSpan 계산 (종료 시점 미포함)
                int rowSpan = endIdx - startIdx;
                if (rowSpan <= 0) {
                    Log.e("DEBUG", "종료시간이 시작시간과 같거나 이전: " + range);
                    continue;
                }

                // row=0 (헤더) 때문에 +1
                int rowStart = startIdx + 1;

                addCourseCell(timetableGrid, course, rowStart, rowSpan, colIndex, context);
            }
        }

        Log.d("DEBUG", "populateTimetable 완료.");
    }

    /**
     * 하나의 수업 정보를 GridLayout의 병합 셀로 추가
     */
    private static void addCourseCell(GridLayout timetableGrid,
                                      PreferencesResponse.Timetable.Course course,
                                      int rowStart, int rowSpan,
                                      int colIndex, Context context) {

        int color = getColorForCourse(context, course.getCourse_name());

        TextView tv = new TextView(context);
        // "과목명 + 강의실" 형태로 표시
        tv.setText(course.getCourse_name() + "\n" + course.getLecture_room());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(12);
        tv.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        tv.setBackgroundColor(color);

        // 병합 레이아웃
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(rowStart, rowSpan),
                GridLayout.spec(colIndex, 1)
        );
        setBodyCellSize(params);

        // 좌우 2px 마진
        params.setMargins(2, 0, 2, 0);

        tv.setLayoutParams(params);
        timetableGrid.addView(tv);
    }

    /**
     * 요일 헤더용 TextView 생성
     */
    private static TextView createHeaderTextView(Context context, String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(14);
        tv.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        tv.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
        return tv;
    }

    /**
     * "시간 표시" 열(왼쪽) 크기 설정
     */
    private static void setThinTimeColSize(GridLayout.LayoutParams params) {
        // 폭/높이는 기기나 디자인에 맞게 조절 가능
        params.width = 60;   // 예시
        params.height = 80;  // 예시
        params.setMargins(1, 1, 1, 1);
    }

    /**
     * 요일 헤더 셀 크기
     */
    private static void setDayHeaderCellSize(GridLayout.LayoutParams params) {
        params.width = 140;  // 예시
        params.height = 80;  // 예시
        params.setMargins(1, 1, 1, 1);
    }

    /**
     * 일반 본문 칸 크기 (수업 표시되는 영역)
     */
    private static void setBodyCellSize(GridLayout.LayoutParams params) {
        params.width = 140;  // 예시
        params.height = 80;  // 예시
        params.setMargins(1, 0, 1, 0);
    }
}
