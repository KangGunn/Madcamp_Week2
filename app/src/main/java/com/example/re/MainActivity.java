package com.example.re;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // EditText와 Button을 XML과 연결
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getHello().enqueue(new Callback<HelloResponse>() {
            @Override
            public void onResponse(Call<HelloResponse> call, Response<HelloResponse> response) {
                if (response.isSuccessful()) {
                    HelloResponse helloResponse = response.body();
                    Log.d("Retrofit", "Response: " + helloResponse.getMessage());
                } else {
                    Log.e("Retrofit", "Response failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<HelloResponse> call, Throwable t) {
                Log.e("Retrofit", "API call failed", t);
            }
        });

        PostRequest request = new PostRequest("Hello from Android");

        apiService.postHello(request).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Retrofit", "Response: " + response.body().getReceivedData());
                } else {
                    Log.e("Retrofit", "Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.e("Retrofit", "API call failed", t);
            }
        });
    }
}