package com.example.re;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("accounts/register/")
    Call<Void> register(@Body User user);  // 회원가입 API

    @POST("/api/login")
    Call<Void> login(@Body LoginRequest loginRequest);  // 로그인 API
    @POST("myapp/api/hello/")
    Call<PostResponse> postHello(@Body PostRequest request);

    @POST("myapp/api/filter/")
    Call<List<CourseResponse>> getFilteredCourses(@Body CourseRequest request);
}
