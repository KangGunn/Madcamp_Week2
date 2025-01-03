package com.example.re;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("myapp/api/hello/")
    Call<HelloResponse> getHello();

    @POST("myapp/api/hello/")
    Call<PostResponse> postHello(@Body PostRequest request);
}
