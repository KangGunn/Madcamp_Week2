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
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
                        } else {
                            Toast.makeText(this, "Google Sign-In 실패", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
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

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(Exception.class);
            String email = account.getEmail();
            String displayName = account.getDisplayName();

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("google_email", email);
            intent.putExtra("google_name", displayName);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("Google Sign-In", "Sign-In failed", e);
            Toast.makeText(this, "Google Sign-In 실패", Toast.LENGTH_LONG).show();
        }
    }
}
