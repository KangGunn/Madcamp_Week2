package com.example.re;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button priorityTimeButton = view.findViewById(R.id.preferred_time);
        Button requiredElectiveButton = view.findViewById(R.id.mandatory_subject);
        Button requiredMajorButton = view.findViewById(R.id.avoided_subject);

        // Set click listeners for the buttons
        priorityTimeButton.setOnClickListener(v -> {
            // Handle preferred time button click
        });

        requiredElectiveButton.setOnClickListener(v -> {
            // Handle mandatory subject button click
        });

        requiredMajorButton.setOnClickListener(v -> {
            // Handle avoided subject button click
        });
    }
}
