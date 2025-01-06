package com.example.re;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        if (contentFrame == null) {
            throw new IllegalStateException("content_frame is not found in activity_main.xml");
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(getLayoutResId(), contentFrame, true);

        setupViews();
    }

    // 각 Activity에서 구현할 메서드
    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void setupViews();
}
