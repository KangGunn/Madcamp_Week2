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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        ApiService apiService = RetrofitClient.getApiService();

        CourseRequest request = new CourseRequest(
              "전산학부",
                "학사과정",
                "전공필수",
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



//        apiService.getHello().enqueue(new Callback<HelloResponse>() {
//            @Override
//            public void onResponse(Call<HelloResponse> call, Response<HelloResponse> response) {
//                if (response.isSuccessful()) {
//                    HelloResponse helloResponse = response.body();
//                    Log.d("Retrofit", "Response: " + helloResponse.getMessage());
//                } else {
//                    Log.e("Retrofit", "Response failed with code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HelloResponse> call, Throwable t) {
//                Log.e("Retrofit", "API call failed", t);
//            }
//        });

//        PostRequest request = new PostRequest("Hello from Android");
//
//        apiService.postHello(request).enqueue(new Callback<PostResponse>() {
//            @Override
//            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
//                if (response.isSuccessful()) {
//                    Log.d("Retrofit", "Response: " + response.body().getReceivedData());
//                } else {
//                    Log.e("Retrofit", "Error code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PostResponse> call, Throwable t) {
//                Log.e("Retrofit", "API call failed", t);
//            }
//        });
    }
}