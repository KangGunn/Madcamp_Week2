package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private Button searchButton;
    private TextView emptyStateText;
    private ImageView emptyStateImage;
    private final String TAG = "SearchActivity";

    private String department = "";
    private String courseName = "";
    private String professor = "";
    private String courseType = "과정구분: 전체";
    private String subjectType = "과목구분: 전체";
    private String lectureType = "강의유형: 전체";

    private final ActivityResultLauncher<Intent> filteringActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // FilteringActivity에서 전달된 데이터 가져오기
                    Intent data = result.getData();
                    department = data.getStringExtra("department");
                    courseName = data.getStringExtra("course_name");
                    professor = data.getStringExtra("professor");
                    courseType = data.getStringExtra("course_type");
                    subjectType = data.getStringExtra("subject_type");
                    lectureType = data.getStringExtra("lecture_type");

                    // 조건 확인 및 UI 업데이트
                    if (isFilterEmpty()) {
                        showEmptyState();
                    } else {
                        fetchCourses(department, courseName, professor, courseType, subjectType, lectureType);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // BaseActivity의 content_frame에 activity_search.xml 주입
        getLayoutInflater().inflate(R.layout.activity_search, findViewById(R.id.content_frame), true);

        emptyStateText = findViewById(R.id.empty_state_text);
        emptyStateImage = findViewById(R.id.empty_state_image);

        recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, FilteringActivity.class);
            filteringActivityLauncher.launch(intent);
        });

        // 초기화: 기본 조건 확인
        if (isFilterEmpty()) {
            showEmptyState();
        } else {
            fetchCourses(department, courseName, professor, courseType, subjectType, lectureType);
        }
    }

    private boolean isFilterEmpty() {
        // 필터링 조건이 비어 있는지 확인
        return (department == null || department.isEmpty()) &&
                (courseName == null || courseName.isEmpty()) &&
                (professor == null || professor.isEmpty()) &&
                "과정구분: 전체".equals(courseType) &&
                "과목구분: 전체".equals(subjectType) &&
                "강의유형: 전체".equals(lectureType);
    }

    private void showEmptyState() {
        // 빈 상태 UI 표시
        recyclerView.setVisibility(View.GONE);
        emptyStateText.setVisibility(View.VISIBLE);
        emptyStateImage.setVisibility(View.VISIBLE);
    }

    private void hideEmptyState() {
        // 빈 상태 UI 숨기기
        recyclerView.setVisibility(View.VISIBLE);
        emptyStateText.setVisibility(View.GONE);
        emptyStateImage.setVisibility(View.GONE);
    }

    private void fetchCourses(String department, String courseName, String professor,
                              String courseType, String subjectType, String lectureType) {
        if (adapter == null || recyclerView.getAdapter() == null) {
            Log.e(TAG, "Adapter is not attached to RecyclerView.");
            return;
        }

        adapter.updateData(new ArrayList<>());

        ApiService apiService = RetrofitClient.getApiService();

        CourseRequest request = new CourseRequest(
                department == null ? "" : department,
                courseType.equals("과정구분: 전체") ? "" : courseType,
                subjectType.equals("과목구분: 전체") ? "" : subjectType,
                courseName == null ? "" : courseName,
                "",
                professor == null ? "" : professor,
                "",
                "",
                "",
                "",
                "",
                "",
                null,
                lectureType.equals("강의유형: 전체") ? "" : lectureType,
                "",
                ""
        );

        apiService.getFilteredCourses(request).enqueue(new Callback<List<CourseResponse>>() {
            @Override
            public void onResponse(Call<List<CourseResponse>> call, Response<List<CourseResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CourseResponse> courses = response.body();
                    adapter.updateData(courses);
                    if (courses.isEmpty()) {
                        showEmptyState();
                    } else {
                        hideEmptyState();
                    }
                } else {
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<CourseResponse>> call, Throwable t) {
                showEmptyState();
            }
        });
    }

    @Override
    protected int getSelectedMenuId() {
        return R.id.navigation_search;
    }
}