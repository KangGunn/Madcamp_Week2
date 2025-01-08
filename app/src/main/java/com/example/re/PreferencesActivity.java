package com.example.re;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesActivity extends BaseActivity {

    private ChipGroup chipGroupDislikedTime;
    private ChipGroup chipGroupMandatoryCourses;
    private ChipGroup chipGroupDislikedCourses;
    private ChipGroup chipGroupMajorCourses;

    private EditText etMandatoryCourseInput;
    private ImageButton btnAddMandatoryCourse;
    private EditText etDislikedCourseInput;
    private ImageButton btnAddDislikedCourse;

    private EditText etMajorCourseInput, etMajorCourseCount;
    private ImageButton btnAddMajorCourse;
    private EditText etGeneralCourses;

    private Button btnSubmitPreferences;

    // Retrofit API 서비스
    private ApiService apiService;
    @Override
    protected int getSelectedMenuId() {
        return R.id.navigation_preferences; // 네비게이션 메뉴에서 Preferences에 해당하는 ID를 반환
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_preferences, findViewById(R.id.content_frame), true);

        // 초기화
        chipGroupDislikedTime = findViewById(R.id.chipGroup_disliked_time);
        chipGroupMandatoryCourses = findViewById(R.id.chipGroup_mandatory_courses);
        chipGroupDislikedCourses = findViewById(R.id.chipGroup_disliked_courses);
        chipGroupMajorCourses = findViewById(R.id.chipGroup_major_courses);

        etMandatoryCourseInput = findViewById(R.id.et_mandatory_course_input);
        btnAddMandatoryCourse = findViewById(R.id.btn_add_mandatory_course);
        etDislikedCourseInput = findViewById(R.id.et_disliked_course_input);
        btnAddDislikedCourse = findViewById(R.id.btn_add_disliked_course);

        etMajorCourseInput = findViewById(R.id.et_major_course_input);
        etMajorCourseCount = findViewById(R.id.et_major_course_count);
        btnAddMajorCourse = findViewById(R.id.btn_add_major_course);
        etGeneralCourses = findViewById(R.id.et_general_courses);

        btnSubmitPreferences = findViewById(R.id.btn_submit_preferences);

        // Retrofit 초기화
        apiService = RetrofitClient.getApiService();

        // 필수 과목 추가 버튼 리스너
        btnAddMandatoryCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCourse(etMandatoryCourseInput, chipGroupMandatoryCourses, "mandatory");
            }
        });

        // 기피 과목 추가 버튼 리스너
        btnAddDislikedCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCourse(etDislikedCourseInput, chipGroupDislikedCourses, "disliked");
            }
        });

        // 전공 과목 추가 버튼 리스너
        btnAddMajorCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMajorCourse(etMajorCourseInput, etMajorCourseCount, chipGroupMajorCourses);
            }
        });

        // 제출 버튼 리스너
        btnSubmitPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPreferences();
            }
        });
    }

    /**
     * 과목을 Chip으로 추가하는 메소드
     *
     * @param editText   과목 입력 필드
     * @param chipGroup  과목 Chip을 추가할 ChipGroup
     * @param courseType 과목 유형 ("mandatory" 또는 "disliked")
     */
    private void addCourse(EditText editText, ChipGroup chipGroup, String courseType) {
        String course = editText.getText().toString().trim();
        if (TextUtils.isEmpty(course)) {
            Toast.makeText(this, "과목을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 중복 과목 체크
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip existingChip = (Chip) chipGroup.getChildAt(i);
            if (existingChip.getText().toString().equalsIgnoreCase(course)) {
                Toast.makeText(this, "이미 추가된 과목입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Chip 생성
        Chip chip = new Chip(this);
        chip.setText(course);
        chip.setCloseIconVisible(true);
        chip.setClickable(true);
        chip.setCheckable(false);
        chip.setChipBackgroundColorResource(R.color.blue);
        chip.setCloseIconResource(R.drawable.close);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                chipGroup.removeView(chip);
            }
        });

        // ChipGroup에 추가
        chipGroup.addView(chip);

        // 입력 필드 초기화
        editText.setText("");
    }

    /**
     * 전공 과목을 Chip으로 추가하는 메소드
     *
     * @param majorInput EditText for major name
     * @param countInput EditText for course count
     * @param chipGroup  ChipGroup to add the major chip
     */
    private void addMajorCourse(EditText majorInput, EditText countInput, ChipGroup chipGroup) {
        String majorName = majorInput.getText().toString().trim();
        String courseCountStr = countInput.getText().toString().trim();

        if (TextUtils.isEmpty(majorName) || TextUtils.isEmpty(courseCountStr)) {
            Toast.makeText(this, "전공 학과명과 과목 수를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int courseCount;
        try {
            courseCount = Integer.parseInt(courseCountStr);
            if (courseCount <= 0) {
                Toast.makeText(this, "과목 수는 1 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "과목 수는 숫자여야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 중복 전공 체크
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip existingChip = (Chip) chipGroup.getChildAt(i);
            if (existingChip.getText().toString().startsWith(majorName + ":")) {
                Toast.makeText(this, "이미 추가된 전공 학과입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Chip 생성
        Chip chip = new Chip(this);
        chip.setText(majorName + ": " + courseCount + "과목");
        chip.setCloseIconVisible(true);
        chip.setClickable(true);
        chip.setCheckable(false);
        chip.setChipBackgroundColorResource(R.color.blue);
        chip.setCloseIconResource(R.drawable.close);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.removeView(chip);
            }
        });

        // ChipGroup에 추가
        chipGroup.addView(chip);

        // 입력 필드 초기화
        majorInput.setText("");
        countInput.setText("");
    }

    /**
     * 사용자로부터 입력된 선호 조건을 수집하고 서버로 전송하는 메소드
     */
    private void submitPreferences() {
        // 1. 기피 시간대 수집
        List<String> dislikedTimes = new ArrayList<>();
        for (int i = 0; i < chipGroupDislikedTime.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupDislikedTime.getChildAt(i);
            if (chip.isChecked()) {
                dislikedTimes.add(chip.getText().toString());
            }
        }

        // 2. 필수 과목 수집
        List<String> mandatoryCourses = new ArrayList<>();
        for (int i = 0; i < chipGroupMandatoryCourses.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupMandatoryCourses.getChildAt(i);
            mandatoryCourses.add(chip.getText().toString());
        }

        // 3. 기피 과목 수집
        List<String> dislikedCourses = new ArrayList<>();
        for (int i = 0; i < chipGroupDislikedCourses.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupDislikedCourses.getChildAt(i);
            dislikedCourses.add(chip.getText().toString());
        }

        // 4. 희망 학점 범위 수집
        int minCredits = 0;
        int maxCredits = 0;

        // 5. 전공 과목 수집
        List<MajorCourse> majorCourses = new ArrayList<>();
        for (int i = 0; i < chipGroupMajorCourses.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupMajorCourses.getChildAt(i);
            String chipText = chip.getText().toString();
            // 예: "컴퓨터공학: 3과목"
            if (chipText.contains(":")) {
                String[] parts = chipText.split(":");
                String majorName = parts[0].trim();
                String courseCountStr = parts[1].replace("과목", "").replace(" ", "").trim();
                try {
                    int courseCount = Integer.parseInt(courseCountStr);
                    majorCourses.add(new MajorCourse(majorName, courseCount));
                } catch (NumberFormatException e) {
                    Log.e("Preferences", "Invalid course count format for major: " + chipText);
                }
            }
        }

        // 6. 교양 과목 수집
        String generalCoursesStr = etGeneralCourses.getText().toString().trim();
        int generalCourses;
        try {
            generalCourses = Integer.parseInt(generalCoursesStr);
            if (generalCourses < 0) {
                Toast.makeText(this, "교양 과목 수는 0 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "교양 과목 수는 숫자여야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 데이터 출력 (디버깅 용도)
//        Log.d("Preferences", "기피 시간대: " + dislikedTimes.toString());
//        Log.d("Preferences", "필수 과목 (" + mandatoryCourses.size() + "개): " + mandatoryCourses.toString());
//        Log.d("Preferences", "기피 과목 (" + dislikedCourses.size() + "개): " + dislikedCourses.toString());
//        Log.d("Preferences", "희망 학점 범위: " + minCredits + " ~ " + maxCredits);
//        Log.d("Preferences", "전공 과목: " + majorCourses.toString());
//        Log.d("Preferences", "교양 과목 수: " + generalCourses);

        // 서버로 데이터 전송
        PreferencesRequest preferencesRequest = new PreferencesRequest(
                dislikedTimes,
                mandatoryCourses,
                dislikedCourses,
                minCredits,
                maxCredits,
                majorCourses,
                generalCourses
        );
        // 요청 메시지 로그 출력
        Gson gson = new Gson();
        String requestJson = gson.toJson(preferencesRequest);
        Log.d("DEBUG", "전송된 요청 메시지: " + requestJson);


        Call<PreferencesResponse> call = apiService.submitPreferences(preferencesRequest);
        call.enqueue(new Callback<PreferencesResponse>() {
            @Override
            public void onResponse(Call<PreferencesResponse> call, Response<PreferencesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("DEBUG", "서버 응답 성공: " + jsonResponse);
                    List<PreferencesResponse.Timetable> timetables = response.body().getCandidateTimetables();
                    if (timetables != null && !timetables.isEmpty()) {
                        Log.d("DEBUG", "받은 시간표 개수: " + timetables.size());
                        for (PreferencesResponse.Timetable timetable : timetables) {
                            Log.d("DEBUG", "Timetable: " + timetable);
                            for (PreferencesResponse.Timetable.Course course : timetable.getCourses()) {
                                Log.d("DEBUG", "과목명: " + course.getCourse_name() +
                                        ", 시간: " + course.getLecture_time() +
                                        ", 강의실: " + course.getLecture_room() +
                                        ", 분반: " + course.getSection());
                            }
                        }
                        displayCandidateTimetablesPopup(timetables);
                    } else {
                        Log.e("DEBUG", "후보 시간표가 비어 있음");
                    }
                } else {
                    Log.e("DEBUG", "서버 응답 실패: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PreferencesResponse> call, Throwable t) {
                Log.e("DEBUG", "onFailure 호출됨: " + t.getMessage());
                Toast.makeText(PreferencesActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 서버로부터 받은 후보 시간표를 표시하는 메소드
     *
     * @param candidateTimetables 후보 시간표 리스트
     */

    private void displayCandidateTimetablesPopup(List<PreferencesResponse.Timetable> candidateTimetables) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.dialog_multiple_timetables, null);
        RecyclerView recyclerView = popupView.findViewById(R.id.recycler_view_timetables);

        // RecyclerView 설정
        CandidateTimetableAdapter adapter = new CandidateTimetableAdapter(this, candidateTimetables);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        // LinearLayoutManager를 가로 방향으로 설정
       // LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(adapter);

        // AlertDialog 생성 및 크기 조정
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .setTitle("후보 시간표")
                .setPositiveButton("닫기", null)
                .create();

        dialog.show();

        // 다이얼로그 크기 조정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 전공 과목 정보를 담는 클래스
     */
    public static class MajorCourse {
        private String majorName;
        private int courseCount;

        public MajorCourse(String majorName, int courseCount) {
            this.majorName = majorName;
            this.courseCount = courseCount;
        }

        public String getMajorName() {
            return majorName;
        }

        public void setMajorName(String majorName) {
            this.majorName = majorName;
        }

        public int getCourseCount() {
            return courseCount;
        }

        public void setCourseCount(int courseCount) {
            this.courseCount = courseCount;
        }

        @Override
        public String toString() {
            return majorName + ": " + courseCount + "과목";
        }
    }
}
