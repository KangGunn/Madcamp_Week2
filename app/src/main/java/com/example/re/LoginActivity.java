package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ApiService apiService;
    private GoogleSignInClient mGoogleSignInClient;

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Log.d("GoogleSignIn", "Result Code: " + result.getResultCode());
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
                            } else {
                                Log.e("GoogleSignIn", "Intent data is null");
                                Toast.makeText(this, "Google Sign-In 실패1: 데이터 없음", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("GoogleSignIn", "Sign-In failed with result code: " + result.getResultCode());
                            Toast.makeText(this, "Google Sign-In 실패1", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = RetrofitClient.getApiService();

        EditText idField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);
        SignInButton googleSignInButton = findViewById(R.id.google_sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(view -> signInWithGoogle());

        loginButton.setOnClickListener(view -> {
            String enteredId = idField.getText().toString().trim();
            String enteredPassword = passwordField.getText().toString().trim();

            if (enteredId.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "ID와 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(enteredId, enteredPassword);
            }
        });

        registerButton.setOnClickListener(view -> {
            String enteredId = idField.getText().toString().trim();
            String enteredPassword = passwordField.getText().toString().trim();

            if (enteredId.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "ID와 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(enteredId, enteredPassword);
            }
        });
    }

    private void loginUser(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        apiService.login(loginRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, TimetableActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패. 아이디 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e("Login", "Error: " + t.getMessage());
            }
        });
    }

    private void registerUser(String username, String password) {
        RegisterRequest registerRequest = new RegisterRequest(username, password);
        apiService.register(registerRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(LoginActivity.this, "회원가입 실패: 필수 입력값이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 409) {
                        Toast.makeText(LoginActivity.this, "회원가입 실패: 이미 존재하는 사용자입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "회원가입 실패: 오류 코드 " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            Log.d("Google ID Token", idToken);

            if (idToken != null) {
                sendGoogleTokenToServer(idToken);
            } else {
                Toast.makeText(this, "Google ID Token을 가져올 수 없습니다.", Toast.LENGTH_LONG).show();
            }
        } catch (ApiException e) {
            Log.e("Google Sign-In", "Sign-In failed", e);
            Toast.makeText(this, "Google Sign-In 실패2", Toast.LENGTH_LONG).show();
        }
    }

    private void sendGoogleTokenToServer(String idToken) {
        GoogleLoginRequest googleLoginRequest = new GoogleLoginRequest(idToken);
        apiService.googleLogin(googleLoginRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Google 로그인 성공!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, TimetableActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Google 로그인 실패: 서버에서 인증 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e("Google Login", "Error: " + t.getMessage());
            }
        });
    }
}
