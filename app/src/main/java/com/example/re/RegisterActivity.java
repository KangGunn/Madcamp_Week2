package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // XML 요소 연결
        EditText idField = findViewById(R.id.input_id);
        EditText passwordField = findViewById(R.id.input_password);
        EditText confirmPasswordField = findViewById(R.id.input_checkpassword);
        Button registerButton = findViewById(R.id.sign_in_button);

        // Google 계정 정보 가져오기 (회원가입 화면에서 자동 입력)
        Intent intent = getIntent();
        String googleEmail = intent.getStringExtra("google_email");
        String googleName = intent.getStringExtra("google_name");
        if (googleEmail != null) {
            idField.setText(googleEmail);
        }

        // 회원가입 버튼 클릭 이벤트
        registerButton.setOnClickListener(view -> {
            String id = idField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (id.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "아이디와 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            } else {
                // 서버에 회원가입 데이터 전송 로직 추가
                Toast.makeText(RegisterActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                // 회원가입 후 LoginActivity로 이동
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
}
