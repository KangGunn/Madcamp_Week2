package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
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

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private Button searchButton;

    // Activity Result Launcher 선언
    private final ActivityResultLauncher<Intent> filteringActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // FilteringActivity에서 전달된 필터 데이터를 가져옴
                    Intent data = result.getData();
                    String department = data.getStringExtra("department");
                    String courseType = data.getStringExtra("courseType");
                    String subjectType = data.getStringExtra("subjectType");

                    // 필터링된 데이터를 서버로 요청
                    fetchCourses(department, courseType, subjectType);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        //View 패딩 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // RecyclerView 설정
        recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 검색 버튼 설정
        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, FilteringActivity.class);
            filteringActivityLauncher.launch(intent); // FilteringActivity로 이동
        });

        // 첫 화면에 기본 데이터를 가져오기
        fetchCourses(null, null, null); // 필터 없이 원래 데이터를 로드
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
//            // FilteringActivity에서 전달된 필터 데이터 가져오기
//            String department = data.getStringExtra("department");
//            String courseType = data.getStringExtra("courseType");
//            String subjectType = data.getStringExtra("subjectType");
//
//            // 필터링된 데이터를 서버로 요청
//            fetchCourses(department, courseType, subjectType);
//        }
//    }
    private void fetchCourses(String department, String courseType, String subjectType) {
        ApiService apiService = RetrofitClient.getApiService();

        // 필터 값이 null 또는 빈 문자열이면 필터링하지 않도록 설정
        String dept = (department == null || department.isEmpty()) ? null : department;
        String course = (courseType == null || courseType.isEmpty()) ? null : courseType;
        String subject = (subjectType == null || subjectType.isEmpty()) ? null : subjectType;

        // CourseRequest 객체 생성
        CourseRequest request = new CourseRequest(
                dept == null ? "" : department,
                course == null ? "" : courseType,
                subject == null ? "" : subjectType,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                null,
                "",
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

        //private void applyFilters() {
            // Log.d("Filters", "과정구분: " + item1 + ", 과목구분: " + item2 + ", 강의유형: " + item3);
        //}

        // CourseRequest 생성 방법 개선 (선택 사항)
        // Map<String, String> filters = new HashMap<>();
        // filters.put("department", "전산학부");
        // filters.put("course_type", "학사과정");
        // filters.put("subject_type", "전공필수");
        // List<String> displayColumns = Arrays.asList("학과", "교과목코드", "교과목명", "담당교수", "강:실:학");
        // CourseRequest request = new CourseRequest(filters, displayColumns);
    }
}
