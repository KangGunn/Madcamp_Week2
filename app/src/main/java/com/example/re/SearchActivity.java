package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
    private final String TAG = "SearchActivity";

    // 필터 데이터를 받는 ActivityResultLauncher 추가
    private final ActivityResultLauncher<Intent> filteringActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // FilteringActivity에서 전달된 데이터 가져오기
                    Intent data = result.getData();
                    String department = data.getStringExtra("department");
                    String courseName = data.getStringExtra("course_name");
                    String professor = data.getStringExtra("professor");
                    String courseType = data.getStringExtra("course_type");
                    String subjectType = data.getStringExtra("subject_type");
                    String lectureType = data.getStringExtra("lecture_type");

                    Log.d(TAG, "FilteringActivity returned: " +
                            department + ", " +
                            courseName + ", " +
                            professor + ", " +
                            courseType + ", " +
                            subjectType + ", " +
                            lectureType);

                    // CourseRequest 생성 및 서버 호출
                    fetchCourses(department, courseName, professor, courseType, subjectType, lectureType);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // BaseActivity의 content_frame에 activity_search.xml 주입
        getLayoutInflater().inflate(R.layout.activity_search, findViewById(R.id.content_frame), true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Search 버튼 초기화 및 클릭 리스너 설정
        searchButton = findViewById(R.id.search_button); // 버튼 ID와 연결
        searchButton.setOnClickListener(v -> {
            // FilteringActivity 실행
            Intent intent = new Intent(SearchActivity.this, FilteringActivity.class);
            filteringActivityLauncher.launch(intent);
        });

        // 기본 데이터 가져오기
        fetchCourses("", "", "", "", "", "");
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
                courseType == null || courseType.equals("전체") ? "" : courseType,
                subjectType == null || subjectType.equals("전체") ? "" : subjectType,
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
                lectureType == null || lectureType.equals("전체") ? "" : lectureType,
                "",
                ""
        );

        apiService.getFilteredCourses(request).enqueue(new Callback<List<CourseResponse>>() {
            @Override
            public void onResponse(Call<List<CourseResponse>> call, Response<List<CourseResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CourseResponse> courses = response.body();
                    adapter.updateData(courses);
                } else {
                    Log.e("API error", "Response failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CourseResponse>> call, Throwable t) {
                Log.e("API Error", "API call failed: " + t.getMessage());
            }
        });
    }

    @Override
    protected int getSelectedMenuId() {
        return R.id.navigation_search;
    }
}
