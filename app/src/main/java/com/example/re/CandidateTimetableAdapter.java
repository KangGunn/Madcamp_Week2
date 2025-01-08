package com.example.re;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.re.PreferencesResponse;
import com.example.re.R;

import java.util.List;

public class CandidateTimetableAdapter extends RecyclerView.Adapter<CandidateTimetableAdapter.ViewHolder> {

    private static final String TAG = "CandidateTimetableAdapter";
    private final Context context;
    private final List<PreferencesResponse.Timetable> candidateTimetables;

    // Constructor
    public CandidateTimetableAdapter(Context context, List<PreferencesResponse.Timetable> candidateTimetables) {
        Log.d(TAG, "Adapter 생성됨. 아이템 개수: " + candidateTimetables.size());
        this.context = context;
        this.candidateTimetables = candidateTimetables;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // fragment_timetable 레이아웃을 inflate
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_timetable, parent, false);
        Log.d(TAG, "onCreateViewHolder 호출됨.");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PreferencesResponse.Timetable timetable = candidateTimetables.get(position);
        Log.d(TAG, "onBindViewHolder 호출됨. position=" + position + ", 과목 수=" + (timetable.getCourses() != null ? timetable.getCourses().size() : 0));

        // Remove existing views in the timetable grid
        holder.timetableGrid.removeAllViews();

        if (timetable.getCourses() == null || timetable.getCourses().isEmpty()) {
            Log.d(TAG, "시간표가 비어 있음. position: " + position);
            TextView emptyView = new TextView(context);
            emptyView.setText("시간표가 없습니다.");
            emptyView.setGravity(Gravity.CENTER);
            holder.timetableGrid.addView(emptyView);
        } else {
            Log.d(TAG, "시간표 데이터 로드 중: position=" + position);

            // Populate timetable using TimetableUtils
            TimetableUtils.populateTimetable(holder.timetableGrid, timetable, context);

            // 추가: timetableGrid 크기 설정
            ViewGroup.LayoutParams layoutParams = holder.timetableGrid.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.timetableGrid.setLayoutParams(layoutParams);
        }
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount 호출됨. 아이템 개수: " + candidateTimetables.size());
        return candidateTimetables.size();
    }

    // ViewHolder 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        HorizontalScrollView scrollView; // 시간표 전체를 감싸는 ScrollView
        GridLayout timetableGrid; // 시간표 GridLayout

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // ScrollView로 감싸진 시간표 레이아웃
            scrollView = itemView.findViewById(R.id.scroll_view_timetable);
            if (scrollView == null) {
                throw new IllegalStateException("scroll_view_timetable가 null입니다. fragment_timetable.xml 확인 필요.");
            }

            // GridLayout 초기화
            timetableGrid = itemView.findViewById(R.id.timetable_grid);
            if (timetableGrid == null) {
                throw new IllegalStateException("timetable_grid가 null입니다. fragment_timetable.xml 확인 필요.");
            }
        }
    }
}
