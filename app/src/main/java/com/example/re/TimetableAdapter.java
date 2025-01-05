package com.example.re;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {
    private final List<TimetableRow> timetableData;

    public TimetableAdapter(List<TimetableRow> timetableData) {
        this.timetableData = timetableData;
    }

    @NonNull
    @Override
    public TimetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_row, parent, false);
        return new TimetableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableViewHolder holder, int position) {
        TimetableRow row = timetableData.get(position);

        holder.timeText.setText(row.getTime());
        holder.mondayCell.setText(row.getMonday());
        holder.tuesdayCell.setText(row.getTuesday());
        holder.wednesdayCell.setText(row.getWednesday());
        holder.thursdayCell.setText(row.getThursday());
        holder.fridayCell.setText(row.getFriday());
    }

    @Override
    public int getItemCount() {
        return timetableData.size();
    }

    static class TimetableViewHolder extends RecyclerView.ViewHolder {
        TextView timeText, mondayCell, tuesdayCell, wednesdayCell, thursdayCell, fridayCell;

        public TimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.time_text);
            mondayCell = itemView.findViewById(R.id.monday_cell);
            tuesdayCell = itemView.findViewById(R.id.tuesday_cell);
            wednesdayCell = itemView.findViewById(R.id.wednesday_cell);
            thursdayCell = itemView.findViewById(R.id.thursday_cell);
            fridayCell = itemView.findViewById(R.id.friday_cell);
        }
    }
}
