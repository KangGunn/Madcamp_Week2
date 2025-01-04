package com.example.re;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
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

    public SearchActivity() {
        //
    }

    private RecyclerView recyclerView;
    private CourseAdapter adapter;

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

        ApiService apiService = RetrofitClient.getApiService();

        // CourseRequest 생성 방법 개선 (선택 사항)
        // Map<String, String> filters = new HashMap<>();
        // filters.put("department", "전산학부");
        // filters.put("course_type", "학사과정");
        // filters.put("subject_type", "전공필수");
        // List<String> displayColumns = Arrays.asList("학과", "교과목코드", "교과목명", "담당교수", "강:실:학");
        // CourseRequest request = new CourseRequest(filters, displayColumns);

        // 현재 CourseRequest 생성 방식 유지
        CourseRequest request = new CourseRequest(
                "전산학부",
                "학사과정",
                "기초필수",
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

    }
}
