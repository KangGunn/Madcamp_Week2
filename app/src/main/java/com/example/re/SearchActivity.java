package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    // ActivityResultLauncher 선언
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

                    System.out.println("SearchActivity received: " +
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search); // 레이아웃 파일 이름 변경

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 검색 버튼 설정
        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // FilteringActivity로 이동
                Intent intent = new Intent(SearchActivity.this, FilteringActivity.class);
                filteringActivityLauncher.launch(intent);
            }
        });

        // 기본 데이터 가져오기
        fetchCourses("", "", "", "", "", "");
    }
        ApiService apiService = RetrofitClient.getApiService();

        // CourseRequest 생성 방법 개선 (선택 사항)
        // Map<String, String> filters = new HashMap<>();
        // filters.put("department", "전산학부");
        // filters.put("course_type", "학사과정");
        // filters.put("subject_type", "전공필수");
        // List<String> displayColumns = Arrays.asList("학과", "교과목코드", "교과목명", "담당교수", "강:실:학");
        // CourseRequest request = new CourseRequest(filters, displayColumns);

        // 현재 CourseRequest 생성 방식 유지
        private void fetchCourses(String department, String courseName, String professor,
                                  String courseType, String subjectType, String lectureType) {
            department = (department == null || department.isEmpty()) ? "" : department;
            courseName = (courseName == null || courseName.isEmpty()) ? "" : courseName;
            professor = (professor == null || professor.isEmpty()) ? "" : professor;
            courseType = (courseType == null || courseType.equals("전체")) ? "" : courseType;
            subjectType = (subjectType == null || subjectType.equals("전체")) ? "" : subjectType;
            lectureType = (lectureType == null || lectureType.equals("전체")) ? "" : lectureType;
            ApiService apiService = RetrofitClient.getApiService();

            // CourseRequest 생성
            CourseRequest request = new CourseRequest(
                    department,
                    courseType,
                    subjectType,
                    courseName,
                    "",
                    professor,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    lectureType,
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
}
