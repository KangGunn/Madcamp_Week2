package com.example.re;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("userdb/api/register/")
    Call<Void> register(@Body RegisterRequest registerRequest);  // 회원가입 API

    @POST("userdb/api/login/")
    Call<Void> login(@Body LoginRequest loginRequest);  // 로그인 API

    @POST("userdb/api/google-login/")
    Call<Void> googleLogin(@Body GoogleLoginRequest googleLoginRequest);

    @POST("myapp/api/filter/")
    Call<List<CourseResponse>> getFilteredCourses(@Body CourseRequest request);

    @POST("myapp/api/preferences/")
    Call<PreferencesResponse> submitPreferences(@Body PreferencesRequest preferencesRequest);
}
