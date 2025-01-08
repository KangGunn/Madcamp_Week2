package com.example.re;


import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.re.PreferencesResponse;
import com.example.re.R;

import java.util.List;

public class CandidateTimetableAdapter extends RecyclerView.Adapter<CandidateTimetableAdapter.ViewHolder> {
    private final Context context;
    private final List<PreferencesResponse.Timetable> candidateTimetables;

    public CandidateTimetableAdapter(Context context, List<PreferencesResponse.Timetable> candidateTimetables) {
        this.context = context;
        this.candidateTimetables = candidateTimetables;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_timetable, parent, false);// fragment_timetable.xml을 사용
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PreferencesResponse.Timetable timetable = candidateTimetables.get(position);

        Log.d("DEBUG", "후보 시간표 position = " + position + ", 과목 수 = " + timetable.getCourses().size());
        for (PreferencesResponse.Timetable.Course course : timetable.getCourses()) {
            Log.d("DEBUG", "과목명: " + course.getCourse_name() + ", 시간: " + course.getLecture_time());
        }

        if (timetable.getCourses() == null || timetable.getCourses().isEmpty()) {
            Log.e("DEBUG", "시간표가 null입니다. position: " + position);
            // 빈 시간표 처리
            holder.timetableGrid.removeAllViews();
            TextView emptyView = new TextView(context);
            emptyView.setText("시간표가 없습니다.");
            emptyView.setGravity(Gravity.CENTER);
            holder.timetableGrid.addView(emptyView);
        } else {
            Log.d("DEBUG", "시간표 데이터: position = " + position + ", courses = " + timetable.getCourses());
            // 시간표 데이터를 GridLayout에 표시
            TimetableUtils.populateTimetable(holder.timetableGrid, timetable, context);
        }
        if (timetable.getCourses() == null || timetable.getCourses().isEmpty()) {
            // 빈 시간표 처리
            holder.timetableGrid.removeAllViews();
            TextView emptyView = new TextView(context);
            emptyView.setText("시간표가 없습니다.");
            emptyView.setGravity(Gravity.CENTER);
            holder.timetableGrid.addView(emptyView);
        } else {
            // 시간표 데이터를 GridLayout에 표시
            Log.d("DEBUG", "GridLayout 초기화 및 데이터 추가 시작: position = " + position);
            TimetableUtils.populateTimetable(holder.timetableGrid, timetable, context);
        }
    }

    @Override
    public int getItemCount() {
        return candidateTimetables.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        GridLayout timetableGrid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timetableGrid = itemView.findViewById(R.id.timetable_grid);
            if (timetableGrid == null) { // GridLayout 확인
                throw new IllegalStateException("timetable_grid가 null입니다. fragment_timetable.xml 확인 필요.");
            }
        }
    }
}