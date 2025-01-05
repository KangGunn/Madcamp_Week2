package com.example.re;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class FilteringActivity extends AppCompatActivity {

    private EditText departmentFilter, courseNameFilter, professorFilter;
    private Spinner courseTypeFilter, subjectTypeFilter, lectureTypeFilter;
    private Button applyFilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filtering); // filtering.xml 연결

        // EditText 필드
        departmentFilter = findViewById(R.id.department_filter);
        courseNameFilter = findViewById(R.id.course_name_filter);
        professorFilter = findViewById(R.id.professor_filter);

        // Spinner 필드
        courseTypeFilter = findViewById(R.id.course_type_filter);
        subjectTypeFilter = findViewById(R.id.subject_type_filter);
        lectureTypeFilter = findViewById(R.id.lecture_type_filter);

        // Button
       applyFilterButton = findViewById(R.id.apply_filter_button);

        // 필터 적용 버튼 클릭 시 데이터 반환
        applyFilterButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("department", departmentFilter.getText().toString());
            resultIntent.putExtra("course_name", courseNameFilter.getText().toString());
            resultIntent.putExtra("professor", professorFilter.getText().toString());
            resultIntent.putExtra("course_type", courseTypeFilter.getSelectedItem().toString());
            resultIntent.putExtra("subject_type", subjectTypeFilter.getSelectedItem().toString());
            resultIntent.putExtra("lecture_type", lectureTypeFilter.getSelectedItem().toString());
            setResult(RESULT_OK, resultIntent); // 결과 반환
            finish(); // FilteringActivity 종료
        });
    }
}