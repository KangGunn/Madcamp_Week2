package com.example.re;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("myapp/api/hello/")
    Call<HelloResponse> getHello();

    @POST("myapp/api/hello/")
    Call<PostResponse> postHello(@Body PostRequest request);

    @POST("myapp/api/filter/")
    Call<List<CourseResponse>> getFilteredCourses(@Body CourseRequest request);
}
