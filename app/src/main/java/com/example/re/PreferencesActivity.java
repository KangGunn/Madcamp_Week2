package com.example.re;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesActivity extends AppCompatActivity {

    private ChipGroup chipGroupDislikedTime;
    private ChipGroup chipGroupMandatoryCourses;
    private ChipGroup chipGroupDislikedCourses;
    private ChipGroup chipGroupMajorCourses;

    private EditText etMandatoryCourseInput;
    private ImageButton btnAddMandatoryCourse;
    private EditText etDislikedCourseInput;
    private ImageButton btnAddDislikedCourse;

    private EditText etMinCredits, etMaxCredits;
    private EditText etMajorCourseInput, etMajorCourseCount;
    private ImageButton btnAddMajorCourse;
    private EditText etGeneralCourses;

    private Button btnSubmitPreferences;

    // Retrofit API 서비스
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // 초기화
        chipGroupDislikedTime = findViewById(R.id.chipGroup_disliked_time);
        chipGroupMandatoryCourses = findViewById(R.id.chipGroup_mandatory_courses);
        chipGroupDislikedCourses = findViewById(R.id.chipGroup_disliked_courses);
        chipGroupMajorCourses = findViewById(R.id.chipGroup_major_courses);

        etMandatoryCourseInput = findViewById(R.id.et_mandatory_course_input);
        btnAddMandatoryCourse = findViewById(R.id.btn_add_mandatory_course);
        etDislikedCourseInput = findViewById(R.id.et_disliked_course_input);
        btnAddDislikedCourse = findViewById(R.id.btn_add_disliked_course);

        etMinCredits = findViewById(R.id.et_min_credits);
        etMaxCredits = findViewById(R.id.et_max_credits);
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
        String minCreditsStr = etMinCredits.getText().toString().trim();
        String maxCreditsStr = etMaxCredits.getText().toString().trim();

        if (TextUtils.isEmpty(minCreditsStr) || TextUtils.isEmpty(maxCreditsStr)) {
            Toast.makeText(this, "희망 학점 범위를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int minCredits, maxCredits;
        try {
            minCredits = Integer.parseInt(minCreditsStr);
            maxCredits = Integer.parseInt(maxCreditsStr);
            if (minCredits <= 0 || maxCredits <= 0) {
                Toast.makeText(this, "학점은 1 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "학점은 숫자여야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (minCredits > maxCredits) {
            Toast.makeText(this, "최소 학점은 최대 학점보다 작거나 같아야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

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

        Call<PreferencesResponse> call = apiService.submitPreferences(preferencesRequest);
        call.enqueue(new Callback<PreferencesResponse>() {
            @Override
            public void onResponse(Call<PreferencesResponse> call, Response<PreferencesResponse> response) {
                if (response.isSuccessful()) {
                    PreferencesResponse preferencesResponse = response.body();
                    if (preferencesResponse != null) {
                        List<String> candidateTimetables = preferencesResponse.getCandidateTimetables();
                        displayCandidateTimetables(candidateTimetables);
                    }
                } else {
                    Toast.makeText(PreferencesActivity.this, "제출 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("Preferences", "Server response code: " + response.code());
                    Log.e("Preferences", "Server response message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PreferencesResponse> call, Throwable t) {
                Toast.makeText(PreferencesActivity.this, "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e("Preferences", "Error: " + t.getMessage());
            }
        });
    }

    /**
     * 서버로부터 받은 후보 시간표를 표시하는 메소드
     *
     * @param candidateTimetables 후보 시간표 리스트
     */
    private void displayCandidateTimetables(List<String> candidateTimetables) {
        if (candidateTimetables == null || candidateTimetables.isEmpty()) {
            Toast.makeText(this, "후보 시간표가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 예시: 후보 시간표를 로그에 출력하고, Toast로 첫 번째 시간표를 표시
        for (int i = 0; i < candidateTimetables.size(); i++) {
            Log.d("Preferences", "후보 시간표 " + (i + 1) + ": " + candidateTimetables.get(i));
        }

        // 실제 앱에서는 RecyclerView 등을 사용하여 사용자에게 시간표를 표시할 수 있습니다.
        Toast.makeText(this, "후보 시간표 " + candidateTimetables.size() + "개를 받았습니다.", Toast.LENGTH_LONG).show();
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
